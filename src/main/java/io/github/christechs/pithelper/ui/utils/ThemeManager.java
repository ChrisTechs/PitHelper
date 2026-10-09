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

import io.github.christechs.clayj.math.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ThemeManager {
    public static final Map<String, PitTheme> THEMES = new HashMap<>();
    public static PitTheme activeTheme;

    static {
        PitTheme defaultTheme = new PitTheme("Default");
        defaultTheme.colors.put("background", "#181A1E");
        defaultTheme.colors.put("sidebar", "#1E2026");
        defaultTheme.colors.put("element", "#23262D");
        defaultTheme.colors.put("foreground", "#FFFFFF");
        defaultTheme.colors.put("text_dim", "#969696");
        defaultTheme.colors.put("accent", "#3C78DC");
        defaultTheme.colors.put("accent_hover", "#4B96FF");
        defaultTheme.colors.put("danger", "#FF5555");
        defaultTheme.colors.put("success", "#55FF55");
        THEMES.put("Default", defaultTheme);

        activeTheme = defaultTheme;
    }

    public static void loadThemesFromZip() {
        try {
            ResourceLocation zipLoc = new ResourceLocation("pithelper", "themes/iTerm2ColorSchemes.zip");
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(zipLoc).getInputStream();
            try (ZipInputStream zis = new ZipInputStream(is)) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    if (!entry.isDirectory() && entry.getName().contains("Xresources/")) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) baos.write(buffer, 0, len);
                        String rawData = new String(baos.toByteArray(), "UTF-8");

                        String name = entry.getName().substring(entry.getName().lastIndexOf('/') + 1)
                                .replace(".txt", "").replace(".xresources", "");

                        loadXresourcesTheme(name, rawData);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadXresourcesTheme(String name, String rawData) {
        PitTheme theme = new PitTheme(name);
        for (String line : rawData.split("\n")) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("!")) continue;

            String[] parts = line.split(":", 2);
            if (parts.length == 2) {
                String key = parts[0].replace("*.", "").trim();
                String hex = parts[1].trim();

                if (key.equals("background")) {
                    theme.colors.put("background", hex);
                    theme.colors.put("sidebar", darkenHex(hex, 0.9f));
                } else if (key.equals("color0")) theme.colors.put("sidebar", hex);
                else if (key.equals("color8")) theme.colors.put("element", hex);
                else if (key.equals("foreground")) theme.colors.put("foreground", hex);
                else if (key.equals("color7")) theme.colors.put("text_dim", hex);
                else if (key.equals("color4")) {
                    theme.colors.put("accent", hex);
                    theme.colors.put("accent_hover", lightenHexPure(hex, 1.2f));
                } else if (key.equals("color1")) theme.colors.put("danger", hex);
                else if (key.equals("color2")) theme.colors.put("success", hex);
            }
        }

        theme.colors.putIfAbsent("element", "#303030");
        theme.colors.putIfAbsent("accent_hover", theme.colors.getOrDefault("accent", "#FFFFFF"));
        THEMES.put(name, theme);
    }

    public static Color get(String key) {
        return activeTheme.getColor(key);
    }

    public static Color get(String key, int alpha) {
        return activeTheme.getColor(key, alpha);
    }

    public static String getHex(String key) {
        return activeTheme.getHex(key);
    }

    private static String lightenHexPure(String hex, float factor) {
        hex = hex.replace("#", "");
        if (hex.length() < 6) return "#FFFFFF";
        int r = Math.min(255, (int) (Integer.parseInt(hex.substring(0, 2), 16) * factor));
        int g = Math.min(255, (int) (Integer.parseInt(hex.substring(2, 4), 16) * factor));
        int b = Math.min(255, (int) (Integer.parseInt(hex.substring(4, 6), 16) * factor));
        return String.format("#%02x%02x%02x", r, g, b);
    }

    private static String darkenHex(String hex, float factor) {
        return lightenHexPure(hex, factor);
    }

    public static class PitTheme {
        public final String name;
        public final Map<String, String> colors = new HashMap<>();

        public PitTheme(String name) {
            this.name = name;
        }

        public String getHex(String key) {
            return colors.getOrDefault(key, "#FF00FF");
        }

        public Color getColor(String key) {
            String hex = getHex(key).replace("#", "");
            if (hex.length() < 6) return new Color(255, 0, 255, 255);
            int r = Integer.parseInt(hex.substring(0, 2), 16);
            int g = Integer.parseInt(hex.substring(2, 4), 16);
            int b = Integer.parseInt(hex.substring(4, 6), 16);
            return new Color(r, g, b, 255);
        }

        public Color getColor(String key, int alpha) {
            String hex = getHex(key).replace("#", "");
            if (hex.length() < 6) return new Color(255, 0, 255, alpha);
            int r = Integer.parseInt(hex.substring(0, 2), 16);
            int g = Integer.parseInt(hex.substring(2, 4), 16);
            int b = Integer.parseInt(hex.substring(4, 6), 16);
            return new Color(r, g, b, alpha);
        }
    }
}