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
import io.github.christechs.pithelper.data.HypixelGameMode;
import io.github.christechs.pithelper.utils.PlayerState;
import io.github.christechs.pithelper.utils.ServerState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutoSpawnHandler {

    public static final KeyBinding toggleSpawnKey = new KeyBinding("Toggle Auto Spawn", Keyboard.KEY_NONE, "PitHelper");

    private static final Pattern COMBAT_PATTERN = Pattern.compile("(?i)hold up!.*\\((\\d+)s.*\\)");

    private boolean isActive = false;
    private boolean pendingRetry = false;
    private long nextExecutionTime = 0;
    private long activatedAt = 0;

    public static void registerKeybind() {
        ClientRegistry.registerKeyBinding(toggleSpawnKey);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (!PitConfig.general().autoSpawnEnabled) return;
        if (!ServerState.onHypixel || ServerState.currentGameMode != HypixelGameMode.PIT) return;

        if (toggleSpawnKey.isPressed()) {
            isActive = !isActive;
            Minecraft mc = Minecraft.getMinecraft();

            if (isActive) {
                activatedAt = System.currentTimeMillis();
                pendingRetry = false;
                mc.thePlayer.addChatMessage(new ChatComponentText("§a§l[PitHelper] §eAuto Spawn activated! Attempting /spawn..."));

                mc.thePlayer.sendChatMessage("/spawn");
            } else {
                mc.thePlayer.addChatMessage(new ChatComponentText("§c§l[PitHelper] §eAuto Spawn cancelled."));
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        double distanceXZ = Math.hypot(mc.thePlayer.posX, mc.thePlayer.posZ);

        if (mc.thePlayer.posY > 100.0 && distanceXZ < 30.0) {
            if (isActive || pendingRetry) {
                isActive = false;
                pendingRetry = false;
                mc.thePlayer.addChatMessage(new ChatComponentText("§a§l[PitHelper] §eSafe spawn area detected. Auto Spawn deactivated."));
            }
            return;
        }

        if (!isActive) return;

        if (!PitConfig.general().autoSpawnEnabled || !ServerState.onHypixel || ServerState.currentGameMode != HypixelGameMode.PIT) {
            isActive = false;
            return;
        }

        if (PlayerState.lastDeath > activatedAt) {
            isActive = false;
            pendingRetry = false;
            mc.thePlayer.addChatMessage(new ChatComponentText("§c§l[PitHelper] §eYou died! Auto Spawn queue cancelled."));
            return;
        }

        if (pendingRetry && System.currentTimeMillis() >= nextExecutionTime) {
            pendingRetry = false;
            mc.thePlayer.sendChatMessage("/spawn");
        }
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (!isActive) return;

        String clean = event.message.getUnformattedText().replaceAll("(?i)§[0-9A-FK-OR]", "").trim();

        Matcher m = COMBAT_PATTERN.matcher(clean);
        if (m.find()) {
            int seconds = Integer.parseInt(m.group(1));

            long delay = seconds <= 0 ? 500 : (seconds * 1000L) + 100;

            nextExecutionTime = System.currentTimeMillis() + delay;
            pendingRetry = true;

            event.message = new ChatComponentText("§8[Auto Spawn Tracking] §r" + event.message.getFormattedText());

        } else if (clean.equalsIgnoreCase("Respawning...") || clean.contains("Teleporting to spawn")) {
            isActive = false;
            pendingRetry = false;
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§a§l[PitHelper] §eSpawn successful. Auto Spawn deactivated."));
        }
    }
}