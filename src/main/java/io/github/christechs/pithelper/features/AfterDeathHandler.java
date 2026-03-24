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
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class AfterDeathHandler {
    private boolean isBlocking = false;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.currentScreen != null) return;

        if (!PitConfig.general().blockMovementAfterDeath) return;
        if (PitConfig.general().onlyOnHypixel
                && (!ServerState.onHypixel || ServerState.currentGameMode != HypixelGameMode.PIT)) return;

        long timeSinceDeath = System.currentTimeMillis() - PlayerState.lastDeath;
        long blockDuration = PitConfig.general().blockMovementSeconds * 1000L;

        KeyBinding[] movementKeys =
                new KeyBinding[]{
                        mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack,
                        mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindRight,
                        mc.gameSettings.keyBindJump, mc.gameSettings.keyBindSneak
                };

        if (timeSinceDeath < blockDuration) {
            isBlocking = true;
            for (KeyBinding key : movementKeys) {
                KeyBinding.setKeyBindState(key.getKeyCode(), false);
            }
        } else if (isBlocking) {
            isBlocking = false;
            for (KeyBinding key : movementKeys) {
                syncKey(key);
            }
        }
    }

    private void syncKey(KeyBinding key) {
        int keyCode = key.getKeyCode();
        boolean isPhysicallyDown = false;

        if (keyCode < 0) {
            isPhysicallyDown = Mouse.isButtonDown(keyCode + 100);
        } else if (keyCode > 0 && keyCode < 256) {
            isPhysicallyDown = Keyboard.isKeyDown(keyCode);
        }

        KeyBinding.setKeyBindState(keyCode, isPhysicallyDown);
    }
}
