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

package io.github.christechs.pithelper.ui.hud;

import io.github.christechs.clayj.ClayJ;
import io.github.christechs.clayj.LayoutResults;
import io.github.christechs.clayj.config.FloatingConfigBuilder;
import io.github.christechs.clayj.config.ImageConfigBuilder;
import io.github.christechs.clayj.enums.LayoutAlignmentY;
import io.github.christechs.clayj.math.Dimensions;
import io.github.christechs.clayj.math.Vector2;
import io.github.christechs.pithelper.config.PitConfig;
import io.github.christechs.pithelper.data.EventFetcher;
import io.github.christechs.pithelper.data.PitEvent;
import io.github.christechs.pithelper.features.NotificationManager;
import io.github.christechs.pithelper.ui.ClayRenderer;
import io.github.christechs.pithelper.ui.base.ClayScreen;
import io.github.christechs.pithelper.ui.screen.EditOverlayScreen;
import io.github.christechs.pithelper.ui.utils.IconCache;
import io.github.christechs.pithelper.utils.ServerState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.concurrent.TimeUnit;

import static io.github.christechs.clayj.ClayJ.*;
import static io.github.christechs.clayj.enums.AttachToElement.ROOT;
import static io.github.christechs.clayj.enums.LayoutAlignmentX.LEFT;
import static io.github.christechs.clayj.enums.LayoutDirection.LEFT_TO_RIGHT;
import static io.github.christechs.clayj.enums.LayoutDirection.TOP_TO_BOTTOM;
import static io.github.christechs.clayj.enums.SizingType.*;

public class EventOverlay {

    public static boolean isClayMenuOpen(Minecraft mc) {
        return mc.currentScreen instanceof ClayScreen;
    }

    public static void drawOverlayStatic(Minecraft mc) {
        ScaledResolution sr = new ScaledResolution(mc);

        if (ClayJ.getContext() == null) {
            ClayJ.initialize(
                    2048,
                    2048,
                    new Dimensions(sr.getScaledWidth(), sr.getScaledHeight())
            );
            ClayJ.setMeasureTextFunction((text, start, len, config, outDimensions) -> {
                String sub = text.subSequence(start, start + len).toString();
                float scale = config.fontSize > 0 ? (float) config.fontSize : 1.0f;
                outDimensions.set((mc.fontRendererObj.getStringWidth(sub) * scale), (mc.fontRendererObj.FONT_HEIGHT * scale));
            });
        }

        boolean isEditScreen = mc.currentScreen instanceof EditOverlayScreen;

        if ((PitConfig.hud().overlayEnabled || isEditScreen) && !isClayMenuOpen(mc)) {
            drawHUD(mc, sr);
        }

        if (!NotificationManager.activeNotifications.isEmpty()) {
            drawNotification(mc, sr);
        }
    }

    private static void drawHUD(Minecraft mc, ScaledResolution sr) {
        float scale = PitConfig.hud().overlayScale;
        ClayJ.setLayoutDimensions(sr.getScaledWidth() / scale, sr.getScaledHeight() / scale);
        ClayJ.setPointerState(new Vector2(-1, -1), false);

        ClayJ.beginLayout();

        float renderX = PitConfig.hud().overlayX * (sr.getScaledWidth() / scale);
        float renderY = PitConfig.hud().overlayY * (sr.getScaledHeight() / scale);
        float renderW = PitConfig.hud().overlayWidth / scale;

        el(decl().floating(new FloatingConfigBuilder()
                .attachTo(ROOT)
                .offset(renderX, renderY))
                .bg(20, 20, 22, 180)
                .radius(8).layout(layout()
                        .sizing(FIXED, renderW, FIT, 0)
                        .dir(TOP_TO_BOTTOM)
                        .padding(8, 8).gap(6)),
                () -> {
            long currentTime = System.currentTimeMillis();
            if (EventFetcher.activeEvent != null) {
                PitEvent ae = EventFetcher.activeEvent;
                long actualEnd = ae.timestamp + ae.eventType.startOffset + ae.eventType.duration;
                if (currentTime < actualEnd)
                    drawUnifiedRow(ae, actualEnd - currentTime, true);
            }
            if (EventFetcher.cachedEvents != null) {
                int count = 0;
                for (PitEvent e : EventFetcher.cachedEvents) {
                    long actualStart = e.timestamp + e.eventType.startOffset;
                    if (actualStart < currentTime)
                        continue;
                    if (count >= PitConfig.hud().overlayEventCount)
                        break;
                    drawUnifiedRow(e, actualStart - currentTime, false);
                    count++;
                }
            }
            el(decl().layout(layout()
                    .sizing(GROW, 0, FIXED, 2)),
                    () -> {}
            );
            text("Credit to BrookeAFK for event data.", txt().size(1).color(120, 120, 120, 255));
        });

        LayoutResults results = ClayJ.endLayout();
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1.0f);
        ClayRenderer.draw(results, mc);
        GlStateManager.popMatrix();
    }

    private static void drawNotification(Minecraft mc, ScaledResolution sr) {
        ClayJ.setLayoutDimensions(sr.getScaledWidth(), sr.getScaledHeight());
        ClayJ.setPointerState(new Vector2(-1, -1), false);
        ClayJ.beginLayout();

        int index = 0;
        for (NotificationManager.VisualNotif notif : NotificationManager.activeNotifications) {
            float yPos = 20 + (index * 45);

            el(decl().floating(new FloatingConfigBuilder()
                    .attachTo(ROOT)
                    .offset(sr.getScaledWidth() / 2f - 100, yPos))
                    .bg(20, 20, 22, 220)
                    .radius(8)
                    .layout(layout()
                            .sizing(FIXED, 200, FIT, 0)
                            .dir(TOP_TO_BOTTOM)
                            .padding(10, 10).gap(4)), () -> {
                text(
                        "Event Starting Soon!",
                        txt().size(1).color(255, 170, 0, 255)
                );

                if (notif.event != null) {
                    drawUnifiedRow(
                            notif.event,
                            notif.event.timestamp - System.currentTimeMillis(),
                            false
                    );
                } else {
                    text(
                            notif.fallbackText,
                            txt().size(1).color(255, 255, 255, 255));
                }
            });

            index++;
        }
        ClayRenderer.draw(ClayJ.endLayout(), mc);
    }

    private static void drawUnifiedRow(PitEvent e, long timeDiff, boolean isActive) {
        el(decl().layout(layout()
                .sizing(GROW, 0, FIT, 0)
                .dir(LEFT_TO_RIGHT)
                .align(LEFT, LayoutAlignmentY.CENTER)
                .gap(6)),
                () -> {

            el(decl().image(new ImageConfigBuilder()
                    .data(IconCache.get(e.event))
                    .sourceDim(16, 16)).layout(layout()
                    .sizing(FIXED, 16, FIXED, 16)),
                    () -> {}
            );

            text((
                    isActive ? "§a" : "§f") + e.event,
                    txt().size(1).color(255, 255, 255, 255)
            );

            el(decl().layout(layout()
                    .sizing(GROW, 0, FIXED, 0)),
                    () -> {}
            );

            text(
                    formatTimeVerbose(timeDiff),
                    txt().size(1)
                            .color(
                                    isActive ? 85 : 170,
                                    255, isActive ? 85 : 170,
                                    255));
        }
        );
    }

    public static String formatTimeVerbose(long ms) {
        if (ms <= 0) return "0s";
        long days = TimeUnit.MILLISECONDS.toDays(ms);
        long hours = TimeUnit.MILLISECONDS.toHours(ms) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(ms) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(ms) % 60;

        StringBuilder sb = new StringBuilder();
        if (days > 0) sb.append(days).append("d ");
        if (hours > 0) sb.append(hours).append("h ");
        if (minutes > 0) sb.append(minutes).append("m ");
        sb.append(seconds).append("s");
        return sb.toString().trim();
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.HOTBAR) return;

        Minecraft mc = Minecraft.getMinecraft();

        if (PitConfig.general().onlyOnHypixel && !ServerState.onHypixel) return;
        if (ServerState.onHypixel && ServerState.currentGameMode != null) {
            Boolean toggle = PitConfig.gamemodes().toggles.getOrDefault(ServerState.currentGameMode, false);

            if (!toggle) return;
        }

        if (isClayMenuOpen(mc) || mc.currentScreen instanceof EditOverlayScreen) return;

        if (!PitConfig.hud().overlayEnabled && NotificationManager.activeNotifications.isEmpty()) return;

        if (EventFetcher.cachedEvents == null && !EventFetcher.isFetching) {
            EventFetcher.fetchAsync();
            return;
        }

        drawOverlayStatic(mc);
    }
}