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

package io.github.christechs.pithelper.ui.base;

import io.github.christechs.clayj.ClayJ;
import io.github.christechs.clayj.LayoutResults;
import io.github.christechs.clayj.math.Dimensions;
import io.github.christechs.clayj.math.Vector2;
import io.github.christechs.pithelper.ui.ClayRenderer;
import io.github.christechs.pithelper.ui.components.ClayComponents;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public abstract class ClayScreen extends GuiScreen {

    protected boolean mouseClickedThisFrame = false;
    private boolean initialized = false;
    private long lastFrameTime = System.currentTimeMillis();
    private float scrollMomentum = 0f;

    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        if (!initialized) {
            if (ClayJ.getContext() == null) {
                ClayJ.initialize(2048, 2048, new Dimensions(this.width, this.height));
                ClayJ.setMeasureTextFunction((text, start, len, config, outDimensions) -> {
                    String sub = text.subSequence(start, start + len).toString();
                    int scale = config.fontSize > 0 ? config.fontSize : 1;
                    int stringWidth = mc.fontRendererObj.getStringWidth(sub) * scale;
                    int stringHeight = mc.fontRendererObj.FONT_HEIGHT * scale;
                    outDimensions.set(stringWidth, stringHeight);
                });
            }
            initialized = true;
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        long currentTime = System.currentTimeMillis();
        float deltaTime = (currentTime - lastFrameTime) / 1000f;
        lastFrameTime = currentTime;

        int dWheel = Mouse.getDWheel();
        if (dWheel != 0) {
            scrollMomentum += Math.signum(dWheel) * 15f;
        }

        float scrollDeltaY = scrollMomentum;
        scrollMomentum *= 0.75f;
        if (Math.abs(scrollMomentum) < 0.1f) scrollMomentum = 0f;

        ClayComponents.updateInputState(mouseX, mouseY, this.mouseClickedThisFrame);

        ClayJ.setLayoutDimensions(this.width, this.height);
        ClayJ.setPointerState(new Vector2(mouseX, mouseY), Mouse.isButtonDown(0));
        ClayJ.updateScrollContainers(true, new Vector2(0, scrollDeltaY / 10.0f), deltaTime);

        ClayJ.beginLayout();
        buildLayout(mouseX, mouseY, deltaTime);
        LayoutResults results = ClayJ.endLayout();

        ClayRenderer.draw(results, this.mc);

        this.mouseClickedThisFrame = false;
    }

    protected abstract void buildLayout(int mouseX, int mouseY, float deltaTime);

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0) this.mouseClickedThisFrame = true;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        ClayComponents.onKeyTyped(typedChar, keyCode);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}