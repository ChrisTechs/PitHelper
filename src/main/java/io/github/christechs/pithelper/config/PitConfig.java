package io.github.christechs.pithelper.config;

import io.github.christechs.config.*;
import io.github.christechs.config.annotations.ConfigCategory;
import io.github.christechs.config.annotations.ConfigHidden;
import io.github.christechs.config.annotations.ConfigProperty;
import io.github.christechs.config.annotations.ConfigRange;
import io.github.christechs.pithelper.data.HypixelGameMode;
import io.github.christechs.pithelper.data.PitEventType;
import io.github.christechs.pithelper.ui.screen.EditOverlayScreen;
import io.github.christechs.pithelper.ui.utils.IconCache;
import io.github.christechs.pithelper.ui.utils.ThemeManager;
import io.github.christechs.pithelper.utils.ServerState;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PitConfig {

    public static PitConfig INSTANCE = new PitConfig();
    private static boolean dynamicConfigsRegistered = false;

    @ConfigCategory(name = "General Settings")
    public General general = new General();
    @ConfigCategory(name = "Themes")
    public Themes themes = new Themes();
    @ConfigCategory(name = "HUD Settings")
    public Hud hud = new Hud();
    @ConfigCategory(name = "Active Game Modes")
    public GameModes gamemodes = new GameModes();
    @ConfigCategory(name = "Event Notifications")
    public Events events = new Events();
    @ConfigCategory(name = "Social & Lobby")
    public Social social = new Social();

    public static Social social() {
        return INSTANCE.social;
    }

    public static General general() {
        return INSTANCE.general;
    }

    public static Hud hud() {
        return INSTANCE.hud;
    }

    public static GameModes gamemodes() {
        return INSTANCE.gamemodes;
    }

    public static Events events() {
        return INSTANCE.events;
    }

    public static void registerDynamicConfigs(File file) {
        if (dynamicConfigsRegistered) return;
        dynamicConfigsRegistered = true;

        if (ThemeManager.THEMES.size() <= 1) {
            ThemeManager.loadThemesFromZip();
        }

        if (ThemeManager.THEMES.containsKey(INSTANCE.general.activeTheme)) {
            ThemeManager.activeTheme = ThemeManager.THEMES.get(INSTANCE.general.activeTheme);
        } else {
            INSTANCE.general.activeTheme = "Challenger Deep";
            ThemeManager.activeTheme = ThemeManager.THEMES.get("Challenger Deep");
        }

        ConfigCategoryData generalCat = getCategoryByName("General Settings");
        ConfigCategoryData themesCat = getCategoryByName("Themes");
        ConfigCategoryData hudCat = getCategoryByName("HUD Settings");
        ConfigCategoryData gamemodesCat = getCategoryByName("Active Game Modes");
        ConfigCategoryData eventsCat = getCategoryByName("Event Notifications");

        if (generalCat != null) {
            generalCat.elements.add(0, ConfigBuilder.button("resetDefaultsBtn", "Reset to Defaults", "Warning: This clears all custom settings.", "Reset All", () -> {
                INSTANCE = new PitConfig();
                ConfigManager.save();
                ConfigManager.init(file);
                dynamicConfigsRegistered = false;
                registerDynamicConfigs(file);
            }));

            generalCat.elements.add(1, ConfigBuilder.display("serverStatus", "Current Server", "", () ->
                    ServerState.onHypixel && ServerState.currentGameMode != null ? ServerState.currentGameMode.niceName : "Not on Hypixel"));
        }

        if (themesCat != null) {
            themesCat.elements.clear();
            themesCat.elements.add(ConfigBuilder.display("activeThemeDisp", "Current Active Theme", "", () -> INSTANCE.general.activeTheme));

            List<String> themeNames = new ArrayList<>(ThemeManager.THEMES.keySet());
            themeNames.sort(String::compareToIgnoreCase);

            for (String themeName : themeNames) {
                themesCat.elements.add(ConfigBuilder.button(
                        "theme_btn_" + themeName.replace(" ", "_"),
                        themeName,
                        "Click to apply the " + themeName + " theme.",
                        "Apply",
                        () -> {
                            ThemeManager.activeTheme = ThemeManager.THEMES.get(themeName);
                            INSTANCE.general.activeTheme = themeName;
                            ConfigManager.save();
                        }
                ));
            }
        }

        if (hudCat != null) {
            hudCat.elements.add(0, ConfigBuilder.button("editHudBtn", "Edit HUD Layout", "Click to drag and resize the HUD.", "Edit HUD", () ->
                    Minecraft.getMinecraft().displayGuiScreen(new EditOverlayScreen())));
        }

        if (gamemodesCat != null) {
            for (HypixelGameMode mode : HypixelGameMode.values()) {
                if (mode != HypixelGameMode.UNKNOWN) {
                    ConfigOption<Boolean> opt = ConfigBuilder.virtual(mode.name(), mode.niceName, "Enable features in " + mode.niceName,
                            () -> INSTANCE.gamemodes.toggles.getOrDefault(mode, false),
                            (val) -> {
                                INSTANCE.gamemodes.toggles.put(mode, val);
                                ConfigManager.save();
                            }
                    );
                    gamemodesCat.elements.add(opt);
                }
            }
        }

        if (eventsCat != null) {
            for (PitEventType type : PitEventType.values()) {
                if (type != PitEventType.NONE) {
                    String prefix = type.name().toLowerCase();
                    EventData data = INSTANCE.events.settings.computeIfAbsent(type, k -> new EventData(true, 3, 0));

                    ConfigOption<Boolean> enabled = ConfigBuilder.virtual(prefix + "_enabled", type.eventName + " Enabled", "",
                            () -> data.enabled, (v) -> {
                                data.enabled = v;
                                ConfigManager.save();
                            });

                    ConfigOption<Integer> min = ConfigBuilder.virtualInt(prefix + "_min", "Notify Minutes", "",
                            () -> data.notifyMinutes, (v) -> {
                                data.notifyMinutes = v;
                                ConfigManager.save();
                            }).setBounds(0, 60);

                    ConfigOption<Integer> sec = ConfigBuilder.virtualInt(prefix + "_sec", "Notify Seconds", "",
                            () -> data.notifySeconds, (v) -> {
                                data.notifySeconds = v;
                                ConfigManager.save();
                            }).setBounds(0, 59);

                    ConfigGroup group = ConfigBuilder.group(prefix + "_grp", type.eventName, type.description, IconCache.get(type.eventName), enabled);
                    group.add(min);
                    group.add(sec);
                    eventsCat.elements.add(group);
                }
            }
        }
    }

    private static ConfigCategoryData getCategoryByName(String name) {
        return ConfigManager.CATEGORIES.stream().filter(c -> c.name.equals(name)).findFirst().orElse(null);
    }

    public static class Themes {
    }

    public static class Social {
        @ConfigProperty(name = "Notify Friends Join", description = "Alerts you when a friend enters the lobby.")
        public boolean notifyFriends = true;
        @ConfigProperty(name = "Notify Enemies Join", description = "Alerts you when an enemy enters the lobby.")
        public boolean notifyEnemies = true;
        @ConfigProperty(name = "Highlight Friends", description = "Highlights friends to be green.")
        public boolean highlightFriends = true;
        @ConfigProperty(name = "Highlight Enemies", description = "Highlights enemies to be red.")
        public boolean highlightEnemies = true;
        @ConfigProperty(name = "Highlight Chain Players", description = "Highlights chain players (0-1 iron pieces) in pink.")
        public boolean highlightChain = false;
        @ConfigProperty(name = "Highlight Iron Players", description = "Highlights iron players (2+ iron pieces) in pink.")
        public boolean highlightIron = false;
        @ConfigProperty(name = "Lobby Prestige Scanner", description = "Calculates average lobby prestige on join.")
        public boolean lobbyScanner = true;

        @ConfigHidden
        public Map<String, String> friends = new HashMap<>();
        @ConfigHidden
        public Map<String, String> enemies = new HashMap<>();
    }

    public static class General {
        @ConfigProperty(name = "Only on Hypixel", description = "Enable mod only when connected to Hypixel.")
        public boolean onlyOnHypixel = true;
        @ConfigProperty(name = "Chat Alerts", description = "Enable chat alerts for events.")
        public boolean chatNotifications = true;
        @ConfigProperty(name = "Visual Popups", description = "Enable visual popups for events.")
        public boolean visualNotifications = true;
        @ConfigProperty(name = "Quick Maths to Clipboard", description = "Copy Quick Maths answers to clipboard.")
        public boolean quickMathsClipboard = false;
        @ConfigProperty(name = "Quick Maths Auto Chat", description = "Automatically open Minecraft chat with the answer.")
        public boolean quickMathsAutoOpenChat = false;
        @ConfigProperty(name = "Block Movement After Death", description = "Prevents you from walking immediately after respawning.")
        public boolean blockMovementAfterDeath = false;
        @ConfigRange(min = 1, max = 10)
        @ConfigProperty(name = "Block Movement Time", description = "Seconds to block movement.")
        public int blockMovementSeconds = 3;
        @ConfigProperty(name = "Auto Spawn Keybind", description = "Automatically retries /spawn until combat timer expires.")
        public boolean autoSpawnEnabled = true;
        @ConfigHidden
        public String activeTheme = "Challenger Deep";
    }

    public static class Hud {
        @ConfigProperty(name = "HUD Overlay", description = "Show the event HUD.")
        public boolean overlayEnabled = true;
        @ConfigProperty(name = "Live Event Countdown", description = "Show countdown timers.")
        public boolean countdownEnabled = false;
        @ConfigProperty(name = "Drop Shadow", description = "Draw shadow behind HUD text.")
        public boolean textDropShadow = true;
        @ConfigRange(min = 1, max = 10)
        @ConfigProperty(name = "HUD Event Count", description = "Max events shown on HUD.")
        public int overlayEventCount = 4;

        @ConfigHidden
        public float overlayX = 0.60f;
        @ConfigHidden
        public float overlayY = 0.15f;
        @ConfigHidden
        public float overlayWidth = 150f;
        @ConfigHidden
        public float overlayScale = 1.0f;
    }

    public static class GameModes {
        public Map<HypixelGameMode, Boolean> toggles = new HashMap<>();

        public GameModes() {
            for (HypixelGameMode mode : HypixelGameMode.values()) {
                if (mode != HypixelGameMode.UNKNOWN) toggles.put(mode, mode == HypixelGameMode.PIT);
            }
        }
    }

    public static class Events {
        public Map<PitEventType, EventData> settings = new HashMap<>();

        public Events() {
            for (PitEventType type : PitEventType.values()) {
                if (type != PitEventType.NONE) {
                    settings.put(type, new EventData(true, 3, 0));
                }
            }
        }
    }

    public static class EventData {
        public boolean enabled;
        public int notifyMinutes;
        public int notifySeconds;

        public EventData(boolean enabled, int notifyMinutes, int notifySeconds) {
            this.enabled = enabled;
            this.notifyMinutes = notifyMinutes;
            this.notifySeconds = notifySeconds;
        }
    }
}