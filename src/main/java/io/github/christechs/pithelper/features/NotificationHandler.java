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

package io.github.christechs.pithelper.features;

import io.github.christechs.pithelper.config.PitConfig;
import io.github.christechs.pithelper.data.EventFetcher;
import io.github.christechs.pithelper.data.PitEvent;
import io.github.christechs.pithelper.utils.ServerState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashSet;
import java.util.Set;

public class NotificationHandler {

    private static final Set<String> notifiedEvents = new HashSet<>();
    private static final Set<String> countdownTicks = new HashSet<>();
    public static GuiScreen pendingScreen = null;
    private static long lastPurgeTime = 0;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getMinecraft();

        if (pendingScreen != null) {
            mc.displayGuiScreen(pendingScreen);
            pendingScreen = null;
        }

        NotificationManager.tick();
        EventFetcher.updateState();

        long currentTime = System.currentTimeMillis();

        if (currentTime - lastPurgeTime > 10000) {
            notifiedEvents.removeIf(key -> {
                try {
                    long time = Long.parseLong(key.replaceAll("[^0-9]", ""));
                    return currentTime > time + 60000;
                } catch (Exception e) {
                    return true;
                }
            });
            countdownTicks.removeIf(key -> !notifiedEvents.contains(key.split("_sec_")[0]));
            lastPurgeTime = currentTime;
        }

        if (mc.thePlayer == null || EventFetcher.cachedEvents == null) return;

        if (PitConfig.general().onlyOnHypixel && !ServerState.onHypixel) return;
        if (ServerState.onHypixel && ServerState.currentGameMode != null) {
            Boolean toggle = PitConfig.gamemodes().toggles.get(ServerState.currentGameMode);
            if (!toggle) return;
        }

        for (PitEvent e : EventFetcher.cachedEvents) {
            long actualStart = e.timestamp + e.eventType.startOffset;
            if (actualStart < currentTime) continue;

            long timeDiff = actualStart - currentTime;
            String eventKey = e.event + actualStart;

            PitConfig.EventData settings = PitConfig.events().settings.get(e.eventType);
            if (settings != null && settings.enabled) {
                long targetNotifyTime = (settings.notifyMinutes * 60L + settings.notifySeconds) * 1000L;

                if (timeDiff <= targetNotifyTime && !notifiedEvents.contains(eventKey)) {
                    notifiedEvents.add(eventKey);

                    long totalSecondsRemaining = timeDiff / 1000L;
                    long displayMins = totalSecondsRemaining / 60L;
                    long displaySecs = totalSecondsRemaining % 60L;

                    if (PitConfig.general().chatNotifications) {
                        mc.thePlayer.addChatMessage(new ChatComponentText("§6§l[PitHelper] §e" + e.event + " §7starts in §a" + displayMins + "m " + displaySecs + "s§7!"));
                    }
                    if (PitConfig.general().visualNotifications) {
                        NotificationManager.add(e, "", 6000);
                    }
                }
            }

            if (PitConfig.hud().countdownEnabled && timeDiff <= 10000 && timeDiff > 0) {
                int secondsLeft = (int) Math.ceil(timeDiff / 1000.0);
                String tickKey = eventKey + "_sec_" + secondsLeft;

                if (!countdownTicks.contains(tickKey)) {
                    countdownTicks.add(tickKey);

                    mc.thePlayer.playSound("note.pling", 1.0F, secondsLeft <= 3 ? 1.5F : 1.0F);
                    String colorCode = secondsLeft <= 3 ? "§c§l" : "§e§l";
                    mc.ingameGUI.displayTitle(colorCode + secondsLeft, "§7" + e.event + " is starting!", 2, 16, 2);
                }
            }
        }
    }
}