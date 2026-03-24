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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class PitItemUtil {

    public static int parseSafeInt(String str, int fallback) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return fallback;
        }
    }

    public static Map<String, ItemStack[]> parseBase64Inventories(JsonObject raw) {
        Map<String, ItemStack[]> map = new HashMap<>();
        if (raw == null) return map;
        for (Map.Entry<String, JsonElement> entry : raw.entrySet()) {
            try {
                byte[] bytes = Base64.getDecoder().decode(entry.getValue().getAsString());
                NBTTagCompound root = CompressedStreamTools.readCompressed(new ByteArrayInputStream(bytes));
                NBTTagList list = root.getTagList("i", 10);
                ItemStack[] items = new ItemStack[list.tagCount()];
                for (int i = 0; i < list.tagCount(); i++) {
                    NBTTagCompound tag = list.getCompoundTagAt(i);
                    if (!tag.hasNoTags()) items[i] = ItemStack.loadItemStackFromNBT(tag);
                }
                map.put(entry.getKey(), items);
            } catch (Exception ignored) {
            }
        }
        return map;
    }

    public static ItemStack synthesizeItem(JsonObject json) {
        int itemId = json.has("item_id") && !json.get("item_id").isJsonNull() ? json.get("item_id").getAsInt() : 300;
        Item mcItem = Item.getItemById(itemId);
        if (mcItem == null) mcItem = Items.leather_leggings;

        ItemStack stack = new ItemStack(mcItem, 1, 0);
        NBTTagCompound root = new NBTTagCompound();
        NBTTagCompound display = new NBTTagCompound();

        if (json.has("custom_name") && !json.get("custom_name").isJsonNull())
            display.setString("Name", json.get("custom_name").getAsString());

        NBTTagList lore = new NBTTagList();
        if (json.has("owner_uuid") && !json.get("owner_uuid").isJsonNull()) {
            String uuid = json.get("owner_uuid").getAsString();
            lore.appendTag(new NBTTagString("§7Owner: " + (uuid.length() > 8 ? uuid.substring(0, 8) + "..." : uuid)));
            lore.appendTag(new NBTTagString(""));
        }

        if (json.has("enchants") && json.get("enchants").isJsonObject()) {
            JsonObject enchants = json.getAsJsonObject("enchants");
            for (Map.Entry<String, JsonElement> e : enchants.entrySet()) {
                String friendly = EnchantDictionary.getFriendlyName(e.getKey());
                lore.appendTag(new NBTTagString("§9" + friendly + " " + e.getValue().getAsInt()));
            }
        }

        if (json.has("lives") && !json.get("lives").isJsonNull() && json.has("max_lives") && !json.get("max_lives").isJsonNull()) {
            lore.appendTag(new NBTTagString(""));
            lore.appendTag(new NBTTagString("§cLives: " + json.get("lives").getAsInt() + "/" + json.get("max_lives").getAsInt()));
        }

        lore.appendTag(new NBTTagString(""));
        lore.appendTag(new NBTTagString("§eClick to view owner profile"));

        display.setTag("Lore", lore);
        root.setTag("display", display);
        stack.setTagCompound(root);

        return stack;
    }

    public static boolean itemMatchesFilters(ItemStack stack, String query, String enchant, String type, int minLives, int maxLives) {
        if (stack == null) return false;

        String qGeneral = query == null ? "" : query.toLowerCase().trim();
        String qEnchant = enchant == null ? "" : enchant.toLowerCase().trim();
        String qType = type == null ? "" : type.toLowerCase().trim();

        if (qGeneral.isEmpty() && qEnchant.isEmpty() && qType.isEmpty() && minLives == -1 && maxLives == -1)
            return false;

        if (!qGeneral.isEmpty() || !qEnchant.isEmpty() || minLives != -1 || maxLives != -1) {
            boolean generalFound = qGeneral.isEmpty() || stack.getDisplayName().toLowerCase().contains(qGeneral);
            boolean enchantFound = qEnchant.isEmpty() || stack.getDisplayName().toLowerCase().contains(qEnchant);
            int sLives = -1;

            try {
                for (String line : stack.getTooltip(Minecraft.getMinecraft().thePlayer, false)) {
                    String clean = line.replaceAll("(?i)\\u00A7[0-9A-FK-OR]", "");
                    String lowerClean = clean.toLowerCase();

                    if (!qGeneral.isEmpty() && lowerClean.contains(qGeneral)) generalFound = true;
                    if (!qEnchant.isEmpty() && lowerClean.contains(qEnchant)) enchantFound = true;

                    if (clean.startsWith("Lives: ")) {
                        String[] parts = clean.replace("Lives: ", "").split("/");
                        if (parts.length == 2) {
                            sLives = Integer.parseInt(parts[0].trim());
                        }
                    }
                }
            } catch (Exception ignored) {
            }

            if (!generalFound) return false;
            if (!enchantFound) return false;
            if (minLives != -1 && sLives < minLives) return false;
            if (maxLives != -1 && sLives > maxLives) return false;
        }

        if (!qType.isEmpty()) {
            Item item = stack.getItem();
            if (qType.contains("sword") && item != Items.golden_sword) return false;
            if (qType.contains("bow") && item != Items.bow) return false;
            return !qType.contains("pant") || item == Items.leather_leggings;
        }

        return true;
    }
}