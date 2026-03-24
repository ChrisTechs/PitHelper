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

package io.github.christechs.pithelper.ui.utils;

import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class IconCache {
    private static Map<String, ResourceLocation> cache;

    public static ResourceLocation get(String eventName) {
        if (cache == null) {
            cache = new HashMap<>();

            cache.put("Quick Maths", new ResourceLocation("minecraft", "textures/items/paper.png"));
            cache.put("KOTH", new ResourceLocation("minecraft", "textures/items/stick.png"));
            cache.put("KOTL", new ResourceLocation("minecraft", "textures/blocks/ladder.png"));
            cache.put("Team Deathmatch", new ResourceLocation("minecraft", "textures/items/diamond_sword.png"));
            cache.put("Rage Pit", new ResourceLocation("minecraft", "textures/items/blaze_powder.png"));
            cache.put("Beast", new ResourceLocation("minecraft", "textures/items/diamond_chestplate.png"));

            cache.put("2x Rewards", new ResourceLocation("minecraft", "textures/items/experience_bottle.png"));
            cache.put("Auction", new ResourceLocation("minecraft", "textures/items/emerald.png"));
            cache.put("All bounty", new ResourceLocation("minecraft", "textures/blocks/deadbush.png"));
            cache.put("Robbery", new ResourceLocation("minecraft", "textures/items/gold_ingot.png"));
            cache.put("Care Package", new ResourceLocation("minecraft", "textures/items/minecart_chest.png"));
            cache.put("Raffle", new ResourceLocation("minecraft", "textures/items/name_tag.png"));

            cache.put("Squads", new ResourceLocation("minecraft", "textures/items/bed.png"));
            cache.put("Spire", new ResourceLocation("minecraft", "textures/items/ender_pearl.png"));
            cache.put("Giant Cake", new ResourceLocation("minecraft", "textures/items/cake.png"));
            cache.put("Pizza", new ResourceLocation("pithelper", "textures/pizza.png"));
            cache.put("Dragon Egg", new ResourceLocation("minecraft", "textures/items/egg.png"));
            cache.put("Blockhead", new ResourceLocation("minecraft", "textures/blocks/dirt.png"));
        }

        return cache.getOrDefault(eventName, new ResourceLocation("minecraft", "textures/items/book_normal.png"));
    }
}