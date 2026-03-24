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

package io.github.christechs.pithelper.ui;

import io.github.christechs.clayj.LayoutResults;
import io.github.christechs.clayj.core.RenderCommand;
import io.github.christechs.clayj.math.BoundingBox;
import io.github.christechs.clayj.math.Color;
import io.github.christechs.clayj.math.CornerRadius;
import io.github.christechs.pithelper.config.PitConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ClayRenderer {

    private static final Tessellator tessellator = Tessellator.getInstance();
    private static final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
    private static boolean isBatchingShapes = false;

    public static void draw(LayoutResults results, Minecraft mc) {
        ScaledResolution sr = new ScaledResolution(mc);
        int scaleFactor = sr.getScaleFactor();
        boolean isScissoring = false;

        isBatchingShapes = false;

        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

        for (int i = 0; i < results.length(); i++) {
            RenderCommand cmd = results.get(i);
            BoundingBox box = cmd.boundingBox;

            switch (cmd.commandType) {
                case RECTANGLE:
                    renderRectangle(cmd, box);
                    break;
                case BORDER:
                    renderBorder(cmd, box);
                    break;
                case IMAGE:
                    flushShapeBatch();
                    renderImage(cmd, box, mc);
                    break;
                case TEXT:
                    flushShapeBatch();
                    renderText(cmd, box, mc, isScissoring);
                    break;
                case CUSTOM:
                    flushShapeBatch();
                    renderCustomItem(cmd, box, mc, isScissoring);
                    break;
                case SCISSOR_START:
                    flushShapeBatch();
                    isScissoring = true;
                    enableScissor(box, scaleFactor, mc.displayHeight);
                    break;
                case SCISSOR_END:
                    flushShapeBatch();
                    isScissoring = false;
                    GL11.glDisable(GL11.GL_SCISSOR_TEST);
                    break;
            }
        }

        flushShapeBatch();

        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }

    private static void startShapeBatch() {
        if (!isBatchingShapes) {
            GlStateManager.disableTexture2D();
            GlStateManager.disableCull();
            worldrenderer.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_COLOR);
            isBatchingShapes = true;
        }
    }

    private static void flushShapeBatch() {
        if (isBatchingShapes) {
            tessellator.draw();
            GlStateManager.enableCull();
            GlStateManager.enableTexture2D();
            isBatchingShapes = false;
        }
    }

    private static void addTriangle(
            float x1, float y1, float x2, float y2, float x3, float y3, int r, int g, int b, int a) {
        worldrenderer.pos(x1, y1, 0).color(r, g, b, a).endVertex();
        worldrenderer.pos(x2, y2, 0).color(r, g, b, a).endVertex();
        worldrenderer.pos(x3, y3, 0).color(r, g, b, a).endVertex();
    }

    private static void renderRectangle(RenderCommand cmd, BoundingBox box) {
        Color bgColor = cmd.renderData.backgroundColor;
        CornerRadius cr = cmd.renderData.cornerRadius;

        if (cr != null
                && (cr.topLeft > 0 || cr.topRight > 0 || cr.bottomLeft > 0 || cr.bottomRight > 0)) {
            drawRoundedRect(box.x, box.y, box.width, box.height, cr, bgColor);
        } else {
            drawSolidRect(box.x, box.y, box.width, box.height, bgColor);
        }
    }

    private static void renderBorder(RenderCommand cmd, BoundingBox box) {
        Color borderColor = cmd.renderData.borderColor;
        float top = cmd.renderData.borderWidth.top;
        float bottom = cmd.renderData.borderWidth.bottom;
        float left = cmd.renderData.borderWidth.left;
        float right = cmd.renderData.borderWidth.right;

        if (top > 0) drawSolidRect(box.x, box.y, box.width, top, borderColor);
        if (bottom > 0)
            drawSolidRect(box.x, box.y + box.height - bottom, box.width, bottom, borderColor);
        if (left > 0) drawSolidRect(box.x, box.y, left, box.height, borderColor);
        if (right > 0) drawSolidRect(box.x + box.width - right, box.y, right, box.height, borderColor);
    }

    private static void renderImage(RenderCommand cmd, BoundingBox box, Minecraft mc) {
        if (!(cmd.renderData.imageData instanceof ResourceLocation)) return;

        ResourceLocation texture = (ResourceLocation) cmd.renderData.imageData;
        GlStateManager.enableTexture2D();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        mc.getTextureManager().bindTexture(texture);
        Gui.drawModalRectWithCustomSizedTexture(
                (int) box.x,
                (int) box.y,
                0,
                0,
                (int) box.width,
                (int) box.height,
                (int) box.width,
                (int) box.height);
        GlStateManager.disableTexture2D();
    }

    private static void renderText(
            RenderCommand cmd, BoundingBox box, Minecraft mc, boolean isScissoring) {
        GlStateManager.enableTexture2D();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        CharSequence fullText = cmd.renderData.text;
        int start = cmd.renderData.textStart;
        int length = cmd.renderData.textLength;
        String lineText = fullText.subSequence(start, start + length).toString();

        if (start > 0) {
            String previousText = fullText.subSequence(0, start).toString();
            String carriedFormatting = FontRenderer.getFormatFromString(previousText);
            lineText = carriedFormatting + lineText;
        }

        float textScale = cmd.renderData.fontSize > 0 ? cmd.renderData.fontSize : 1.0f;

        GlStateManager.pushMatrix();
        GlStateManager.translate(box.x, box.y, 0);
        GlStateManager.scale(textScale, textScale, 1.0f);

        int colorInt = colorToInt(cmd.renderData.textColor);
        if (PitConfig.hud().textDropShadow) {
            mc.fontRendererObj.drawStringWithShadow(lineText, 0, 0, colorInt);
        } else {
            mc.fontRendererObj.drawString(lineText, 0, 0, colorInt);
        }

        GlStateManager.popMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        if (isScissoring) GL11.glEnable(GL11.GL_SCISSOR_TEST);
    }

    private static void renderCustomItem(
            RenderCommand cmd, BoundingBox box, Minecraft mc, boolean isScissoring) {
        if (!(cmd.renderData.customData instanceof ItemStack)) return;

        ItemStack item = (ItemStack) cmd.renderData.customData;

        GlStateManager.pushMatrix();
        GlStateManager.enableTexture2D();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableDepth();
        RenderHelper.enableGUIStandardItemLighting();

        float oldZ = mc.getRenderItem().zLevel;
        mc.getRenderItem().zLevel = 300f;
        mc.getRenderItem().renderItemAndEffectIntoGUI(item, (int) box.x, (int) box.y);
        mc.getRenderItem()
                .renderItemOverlayIntoGUI(mc.fontRendererObj, item, (int) box.x, (int) box.y, null);
        mc.getRenderItem().zLevel = oldZ;

        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.popMatrix();

        if (isScissoring) GL11.glEnable(GL11.GL_SCISSOR_TEST);
    }

    private static void enableScissor(BoundingBox box, int scaleFactor, int displayHeight) {
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        int scissorX = (int) Math.round(box.x * scaleFactor);
        int scissorY = (int) Math.round(displayHeight - ((box.y + box.height) * scaleFactor));
        int scissorW = Math.max(0, (int) Math.round(box.width * scaleFactor));
        int scissorH = Math.max(0, (int) Math.round(box.height * scaleFactor));
        GL11.glScissor(scissorX, scissorY, scissorW, scissorH);
    }

    private static void drawSolidRect(float x, float y, float width, float height, Color color) {
        if (color.a <= 0) return;
        startShapeBatch();

        int r = (int) color.r, g = (int) color.g, b = (int) color.b, a = (int) color.a;

        addTriangle(x, y + height, x + width, y + height, x + width, y, r, g, b, a);
        addTriangle(x, y + height, x + width, y, x, y, r, g, b, a);
    }

    private static void drawRoundedRect(
            float x, float y, float width, float height, CornerRadius cr, Color color) {
        if (color.a <= 0) return;
        startShapeBatch();

        int r = (int) color.r, g = (int) color.g, b = (int) color.b, a = (int) color.a;

        float maxR = Math.min(width / 2f, height / 2f);
        float rtl = Math.max(0, Math.min(cr.topLeft, maxR));
        float rtr = Math.max(0, Math.min(cr.topRight, maxR));
        float rbl = Math.max(0, Math.min(cr.bottomLeft, maxR));
        float rbr = Math.max(0, Math.min(cr.bottomRight, maxR));

        int resolution = 50;
        float cx = x + width / 2f;
        float cy = y + height / 2f;

        float startX = (float) (x + rtl + Math.cos(Math.PI) * rtl);
        float startY = (float) (y + rtl + Math.sin(Math.PI) * rtl);
        float prevX = startX;
        float prevY = startY;

        for (int i = 1; i <= resolution; i++) {
            double ang = Math.PI + (i * Math.PI / 2 / resolution);
            float px = (float) (x + rtl + Math.cos(ang) * rtl);
            float py = (float) (y + rtl + Math.sin(ang) * rtl);
            addTriangle(cx, cy, prevX, prevY, px, py, r, g, b, a);
            prevX = px;
            prevY = py;
        }
        for (int i = 0; i <= resolution; i++) {
            double ang = Math.PI * 1.5 + (i * Math.PI / 2 / resolution);
            float px = (float) (x + width - rtr + Math.cos(ang) * rtr);
            float py = (float) (y + rtr + Math.sin(ang) * rtr);
            addTriangle(cx, cy, prevX, prevY, px, py, r, g, b, a);
            prevX = px;
            prevY = py;
        }
        for (int i = 0; i <= resolution; i++) {
            double ang = (i * Math.PI / 2 / resolution);
            float px = (float) (x + width - rbr + Math.cos(ang) * rbr);
            float py = (float) (y + height - rbr + Math.sin(ang) * rbr);
            addTriangle(cx, cy, prevX, prevY, px, py, r, g, b, a);
            prevX = px;
            prevY = py;
        }
        for (int i = 0; i <= resolution; i++) {
            double ang = Math.PI / 2 + (i * Math.PI / 2 / resolution);
            float px = (float) (x + rbl + Math.cos(ang) * rbl);
            float py = (float) (y + height - rbl + Math.sin(ang) * rbl);
            addTriangle(cx, cy, prevX, prevY, px, py, r, g, b, a);
            prevX = px;
            prevY = py;
        }

        addTriangle(cx, cy, prevX, prevY, startX, startY, r, g, b, a);
    }

    private static int colorToInt(Color color) {
        return ((int) color.a << 24) | ((int) color.r << 16) | ((int) color.g << 8) | (int) color.b;
    }
}
