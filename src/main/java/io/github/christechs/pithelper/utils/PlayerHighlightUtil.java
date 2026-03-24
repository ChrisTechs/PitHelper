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

package io.github.christechs.pithelper.utils;

import io.github.christechs.pithelper.config.PitConfig;
import io.github.christechs.pithelper.data.HypixelGameMode;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class PlayerHighlightUtil {

    public static int getHighlightColor(AbstractClientPlayer player) {
        String uuid = player.getUniqueID().toString().replace("-", "");

        if (PitConfig.social().highlightFriends && PitConfig.social().friends.containsKey(uuid)) {
            return 0x55FF55;
        } else if (PitConfig.social().highlightEnemies
                && PitConfig.social().enemies.containsKey(uuid)) {
            return 0xFF5555;
        }

        if (ServerState.onHypixel && ServerState.currentGameMode == HypixelGameMode.PIT) {
            int ironCount = 0;
            int chainCount = 0;
            int diamondCount = 0;
            int airCount = 0;

            for (int i = 0; i < 4; i++) {
                ItemStack stack = player.getCurrentArmor(i);
                if (stack != null && stack.getItem() instanceof ItemArmor) {
                    ItemArmor.ArmorMaterial mat = ((ItemArmor) stack.getItem()).getArmorMaterial();
                    if (mat == ItemArmor.ArmorMaterial.DIAMOND) {
                        diamondCount++;
                    } else if (mat == ItemArmor.ArmorMaterial.IRON) {
                        ironCount++;
                    } else if (mat == ItemArmor.ArmorMaterial.CHAIN) {
                        chainCount++;
                    }
                }
                if (stack == null) airCount++;
            }

            if (diamondCount == 0
                    && (ironCount > 0 || chainCount > 0)
                    && (ironCount + airCount + chainCount) == 4) {
                if (PitConfig.social().highlightIron && ironCount >= 2) {
                    return 0xFF55FF;
                } else if (PitConfig.social().highlightChain && ironCount <= 1) {
                    return 0xFF55FF;
                }
            }
        }

        return -1;
    }
}
