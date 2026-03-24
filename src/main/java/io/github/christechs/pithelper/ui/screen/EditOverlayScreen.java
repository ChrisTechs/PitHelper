/*
 * PitHelper - A Hypixel The Pit Helper Mod.
 * Copyright (C) 2026 Christian Steenkamp
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.christechs.pithelper.ui.screen;

import io.github.christechs.config.ConfigManager;
import io.github.christechs.pithelper.config.PitConfig;
import io.github.christechs.pithelper.data.EventFetcher;
import io.github.christechs.pithelper.ui.hud.EventOverlay;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class EditOverlayScreen extends GuiScreen {

    private DragMode currentMode = DragMode.NONE;
    private float startDragX = 0, startDragY = 0;
    private float startWidth = 0;
    private int startMouseX = 0, startMouseY = 0;

    private OverlayBounds getBounds() {
        ScaledResolution sr = new ScaledResolution(this.mc);
        int activeCount = EventFetcher.activeEvent != null ? 1 : 0;
        int upcomingCount =
                EventFetcher.cachedEvents != null
                        ? Math.min(EventFetcher.cachedEvents.size(), PitConfig.hud().overlayEventCount)
                        : 0;
        int totalRows = activeCount + upcomingCount;

        float scale = PitConfig.hud().overlayScale;
        OverlayBounds bounds = new OverlayBounds();
        bounds.height = (int) ((totalRows == 0 ? 30 : 38 + (totalRows * 22) - 6) * scale);
        bounds.width = (int) PitConfig.hud().overlayWidth;
        bounds.x = (int) (PitConfig.hud().overlayX * sr.getScaledWidth());
        bounds.y = (int) (PitConfig.hud().overlayY * sr.getScaledHeight());
        return bounds;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int dWheel = Mouse.getDWheel();
        if (dWheel != 0) {
            PitConfig.hud().overlayScale += (dWheel > 0 ? 0.05f : -0.05f);
            PitConfig.hud().overlayScale = Math.max(0.5f, Math.min(2.0f, PitConfig.hud().overlayScale));
        }

        this.drawDefaultBackground();
        this.drawCenteredString(
                this.fontRendererObj,
                "Click & Drag to move. Drag right edge to resize.",
                this.width / 2,
                20,
                0xFFFFFF);
        this.drawCenteredString(
                this.fontRendererObj,
                "Scroll Mouse Wheel to scale HUD. Press ESC to save.",
                this.width / 2,
                35,
                0xAAAAAA);

        if (currentMode == DragMode.MOVING) {
            float newX = startDragX + ((mouseX - startMouseX) / (float) this.width);
            float newY = startDragY + ((mouseY - startMouseY) / (float) this.height);
            PitConfig.hud().overlayX = Math.max(0.0f, Math.min(1.0f, newX));
            PitConfig.hud().overlayY = Math.max(0.0f, Math.min(1.0f, newY));
        } else if (currentMode == DragMode.RESIZING_RIGHT) {
            float diffX = mouseX - startMouseX;
            PitConfig.hud().overlayWidth =
                    Math.max(
                            120f * PitConfig.hud().overlayScale,
                            Math.min(400f * PitConfig.hud().overlayScale, startWidth + diffX));
        }

        EventOverlay.drawOverlayStatic(this.mc);

        OverlayBounds bounds = getBounds();
        drawRect(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height, 0x44FFFFFF);
        drawRect(
                bounds.x + bounds.width - 4,
                bounds.y,
                bounds.x + bounds.width + 4,
                bounds.y + bounds.height,
                0xAA00FF00);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton != 0) return;

        OverlayBounds bounds = getBounds();

        if (mouseX >= bounds.x + bounds.width - 8
                && mouseX <= bounds.x + bounds.width + 8
                && mouseY >= bounds.y
                && mouseY <= bounds.y + bounds.height) {
            currentMode = DragMode.RESIZING_RIGHT;
            startWidth = PitConfig.hud().overlayWidth;
        } else {
            currentMode = DragMode.MOVING;
            startDragX = PitConfig.hud().overlayX;
            startDragY = PitConfig.hud().overlayY;
        }

        startMouseX = mouseX;
        startMouseY = mouseY;
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0) currentMode = DragMode.NONE;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            ConfigManager.save();
            this.mc.displayGuiScreen(new PitHelperMenu());
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        ConfigManager.save();
    }

    private enum DragMode {
        NONE,
        MOVING,
        RESIZING_RIGHT
    }

    private static class OverlayBounds {
        int x, y, width, height;
    }
}
