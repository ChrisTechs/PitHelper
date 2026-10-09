package io.github.christechs.pithelper.ui.components;

import io.github.christechs.clayj.ClayJ.ClayElement;
import io.github.christechs.clayj.config.FloatingConfigBuilder;
import io.github.christechs.clayj.config.LayoutConfigBuilder;
import io.github.christechs.clayj.core.ElementId;
import io.github.christechs.clayj.core.LayoutElementHashMapItem;
import io.github.christechs.clayj.core.ScrollContainerData;
import io.github.christechs.clayj.enums.AttachToElement;
import io.github.christechs.clayj.enums.FloatingAttachPoint;
import io.github.christechs.clayj.math.Vector2;
import io.github.christechs.clayj.util.HashUtil;
import io.github.christechs.pithelper.ui.utils.ThemeManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static io.github.christechs.clayj.ClayJ.*;

public class ClayComponents {

    private static final Map<String, ScrollBounds> scrollCache = new HashMap<>();
    public static int mouseDeltaY = 0;
    public static String activeDragElementId = "";
    public static int mouseX = 0, mouseY = 0;
    public static String activeTextInputId = "";
    public static StringBuilder activeText = new StringBuilder();
    public static Consumer<String> activeTextCallback = null;
    public static int cursorPos = 0;
    private static int previousMouseY = 0;
    private static boolean mouseClicked = false;
    private static boolean clickConsumed = false;

    public static void updateInputState(int x, int y, boolean clicked) {
        mouseDeltaY = y - previousMouseY;
        previousMouseY = y;
        mouseX = x;
        mouseY = y;
        mouseClicked = clicked;
        clickConsumed = false;

        if (!Mouse.isButtonDown(0)) activeDragElementId = "";
    }

    public static boolean isClicked(String id) {
        if (mouseClicked && !clickConsumed && pointerOver(id)) {
            clickConsumed = true;
            return true;
        }
        return false;
    }

    public static void button(String id, String text, Runnable onClick) {
        boolean hover = pointerOver(id);
        if (isClicked(id)) onClick.run();

        try (ClayElement btn = el(decl()
                .id(id)
                .bg(ThemeManager.get(hover ? "accent_hover" : "accent"))
                .radius(6)
                .layout(layout()
                        .widthFit().heightFit()
                        .padding(8, 8, 4, 4)
                        .alignCenter()))) {
            text(text, txt().size(1).color(ThemeManager.get("background")));
        }
    }

    public static void tab(String id, String label, boolean active, Runnable onClick) {
        boolean hover = pointerOver(id);
        if (isClicked(id)) onClick.run();

        try (ClayElement tab = el(decl()
                .id(id)
                .bg(ThemeManager.get(active ? "accent" : (hover ? "element" : "background")))
                .radius(6)
                .layout(layout()
                        .widthGrow().heightFixed(30)
                        .padding(10, 10, 0, 0)
                        .alignLeft()
                        .alignCenterY()))) {
            text(label, txt().size(1).color(ThemeManager.get(active ? "background" : "foreground")));
        }
    }

    public static void scrollbar(String containerId) {
        ScrollContainerData data = new ScrollContainerData();
        ElementId eId = new ElementId();
        HashUtil.hashString(containerId, 0, 0, eId);
        getScrollContainerData(eId, data);

        ScrollBounds bounds = scrollCache.computeIfAbsent(containerId, k -> new ScrollBounds());

        if (data.found && data.scrollContainerDimensions.height() > 0) {
            bounds.containerHeight = data.scrollContainerDimensions.height();
            bounds.contentHeight = data.contentDimensions.height();
            if (data.scrollPosition != null) {
                bounds.scrollY = data.scrollPosition.y();
            }
        }

        float trackHeight = bounds.containerHeight;
        float contentHeight = Math.max(bounds.contentHeight, trackHeight);
        float scrollableRange = contentHeight - trackHeight;

        if (scrollableRange <= 2.0f) return;

        String trackId = "ScrollTrack_" + containerId;
        try (ClayElement track = el(decl()
                .id(trackId)
                .bg(ThemeManager.get("background"))
                .radius(2)
                .layout(layout().widthFixed(4).heightGrow()))) {
            renderScrollThumb(containerId, data, bounds, trackHeight, contentHeight, scrollableRange);
        }
    }

    private static void renderScrollThumb(
            String containerId,
            ScrollContainerData data,
            ScrollBounds bounds,
            float trackHeight,
            float contentHeight,
            float scrollableRange) {
        String thumbId = "ScrollThumb_" + containerId;
        String trackId = "ScrollTrack_" + containerId;

        float thumbHeight = Math.max(20.0f, trackHeight * (trackHeight / contentHeight));
        float maxThumbY = trackHeight - thumbHeight;

        boolean isHovered = pointerOver(thumbId) || pointerOver(trackId);
        boolean isActive = activeDragElementId.equals(thumbId);

        if (isHovered && mouseClicked) {
            activeDragElementId = thumbId;
            isActive = true;
        }

        float scrollRatio = -bounds.scrollY / scrollableRange;
        float thumbY = scrollRatio * maxThumbY;

        if (isActive && data.scrollPosition != null) {
            float deltaRatio = (float) mouseDeltaY / maxThumbY;
            float newY = data.scrollPosition.y() - (deltaRatio * scrollableRange);

            if (newY > 0) newY = 0;
            if (newY < -scrollableRange) newY = -scrollableRange;

            data.scrollPosition = new Vector2(data.scrollPosition.x(), newY);
            thumbY = (-data.scrollPosition.y() / scrollableRange) * maxThumbY;
        }

        float finalThumbY = Math.max(0, Math.min(thumbY, maxThumbY));

        try (ClayElement thumb = el(decl()
                .id(thumbId)
                .bg(ThemeManager.get(isActive || isHovered ? "accent_hover" : "accent"))
                .radius(2)
                .floating(new FloatingConfigBuilder()
                        .attachTo(AttachToElement.PARENT)
                        .attach(FloatingAttachPoint.LEFT_TOP, FloatingAttachPoint.LEFT_TOP)
                        .offset(0, finalThumbY)
                        .zIndex((short) 0))
                .layout(layout().widthGrow().heightFixed(thumbHeight)))) {
        }
    }

    public static void textInput(String id, String placeholder, String value, float width, Consumer<String> onChange) {
        boolean isFocused = activeTextInputId.equals(id);
        handleTextInputInteraction(id, value, onChange, isFocused);

        String display = value.isEmpty() && !isFocused ? "§8" + placeholder : value;

        LayoutConfigBuilder lyt = layout()
                .heightFixed(24)
                .padding(8, 8, 0, 0)
                .alignLeft().alignCenterY();

        if (width > 0) lyt.widthFixed(width);
        else lyt.widthGrow();

        try (ClayElement input = el(decl()
                .id(id)
                .bg(ThemeManager.get(isFocused ? "element" : "background"))
                .radius(4)
                .layout(lyt))) {

            text(display, txt().size(1).color(ThemeManager.get("foreground")));
            if (isFocused && (System.currentTimeMillis() / 500) % 2 == 0) {
                renderCursor(value);
            }
        }
    }

    private static void handleTextInputInteraction(String id, String value, Consumer<String> onChange, boolean isFocused) {
        if (isClicked(id)) {
            activeTextInputId = id;
            activeText = new StringBuilder(value);
            activeTextCallback = onChange;

            ElementId eid = new ElementId();
            HashUtil.hashString(id, 0, 0, eid);
            LayoutElementHashMapItem item = getContext().getHashMapItem(eid.id);

            if (item != null) {
                float localX = mouseX - item.boundingBox.x() - 8;
                int newCursorPos = 0;
                for (int i = 0; i <= value.length(); i++) {
                    int w = Minecraft.getMinecraft().fontRendererObj.getStringWidth(value.substring(0, i));
                    if (localX < w + 4) {
                        newCursorPos = i;
                        break;
                    }
                    newCursorPos = i;
                }
                cursorPos = newCursorPos;
            } else {
                cursorPos = value.length();
            }
        } else if (mouseClicked && !pointerOver(id) && isFocused) {
            activeTextInputId = "";
        }

        if (isFocused && !activeText.toString().equals(value)) {
            activeText = new StringBuilder(value);
            cursorPos = Math.min(cursorPos, value.length());
        }
    }

    private static void renderCursor(String value) {
        int safeCursor = Math.min(cursorPos, value.length());
        int cursorOffset = Minecraft.getMinecraft().fontRendererObj.getStringWidth(value.substring(0, safeCursor));

        try (ClayElement cursor = el(decl()
                .bg(ThemeManager.get("foreground"))
                .floating(new FloatingConfigBuilder()
                        .attachTo(AttachToElement.PARENT)
                        .attach(FloatingAttachPoint.LEFT_CENTER, FloatingAttachPoint.LEFT_CENTER)
                        .offset(cursorOffset + 8, 0)
                        .zIndex((short) 0))
                .layout(layout().widthFixed(2).heightFixed(12)))) {
        }
    }

    public static void onKeyTyped(char typedChar, int keyCode) {
        if (activeTextInputId.isEmpty() || activeTextCallback == null) return;
        if (keyCode == Keyboard.KEY_BACK && cursorPos > 0) {
            activeText.deleteCharAt(cursorPos - 1);
            cursorPos--;
            activeTextCallback.accept(activeText.toString());
        } else if (keyCode == Keyboard.KEY_DELETE && cursorPos < activeText.length()) {
            activeText.deleteCharAt(cursorPos);
            activeTextCallback.accept(activeText.toString());
        } else if (keyCode == Keyboard.KEY_LEFT && cursorPos > 0) {
            cursorPos--;
        } else if (keyCode == Keyboard.KEY_RIGHT && cursorPos < activeText.length()) {
            cursorPos++;
        } else if (keyCode == Keyboard.KEY_RETURN) {
            activeTextInputId = "";
            activeTextCallback = null;
        } else if (keyCode == Keyboard.KEY_V
                && (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))) {
            String clipboard = GuiScreen.getClipboardString();
            if (clipboard != null && !clipboard.isEmpty()) {
                activeText.insert(cursorPos, clipboard);
                cursorPos += clipboard.length();
                activeTextCallback.accept(activeText.toString());
            }
        } else if (keyCode == Keyboard.KEY_C
                && (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))) {
            GuiScreen.setClipboardString(activeText.toString());
        } else if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
            activeText.insert(cursorPos, typedChar);
            cursorPos++;
            activeTextCallback.accept(activeText.toString());
        }
    }

    private static class ScrollBounds {
        float containerHeight;
        float contentHeight;
        float scrollY;
    }
}