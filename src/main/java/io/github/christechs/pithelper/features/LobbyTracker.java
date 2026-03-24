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
import io.github.christechs.pithelper.utils.ServerState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LobbyTracker {

    private static final Pattern STRIP_COLOR = Pattern.compile("(?i)§[0-9A-FK-OR]");
    private static final Pattern CHAT_PRESTIGE_PATTERN = Pattern.compile("\\[(?:([IVXLCDM]+)-)?\\d+\\]\\s*(?:[^a-zA-Z0-9\\s\\[\\]]+\\s*)?(?:\\[[A-Za-z+]+\\]\\s*)?([a-zA-Z0-9_]{2,16})");
    private static final String[] BRACKET_NAMES = {
            "§7[0]", "§9[1-4]", "§e[5-9]", "§6[10-14]", "§c[15-19]", "§5[20-24]",
            "§d[25-29]", "§f[30-34]", "§b[35-39]", "§1[40-44]", "§0[45-47]", "§4[48-49]", "§8[50]"
    };
    public static LobbyTracker INSTANCE;
    private final Set<String> knownLobbyUuids = new HashSet<>();
    private final Map<String, Integer> exactPrestiges = new HashMap<>();

    private long lastScanTime = 0;
    private double lastAveragePrestige = 0.0;
    private boolean firstScanDone = false;

    public LobbyTracker() {
        INSTANCE = this;
    }

    public void printManualStats() {
        if (!ServerState.onHypixel || ServerState.currentGameMode != HypixelGameMode.PIT) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer != null) {
            scanLobby(mc, true);
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.thePlayer == null || mc.getNetHandler() == null || mc.theWorld == null) return;

        if (!ServerState.onHypixel || ServerState.currentGameMode != HypixelGameMode.PIT) {
            knownLobbyUuids.clear();
            exactPrestiges.clear();
            lastAveragePrestige = 0.0;
            firstScanDone = false;
            return;
        }

        long now = System.currentTimeMillis();
        if (now - lastScanTime > 3000) {
            scanLobby(mc, false);
            lastScanTime = now;
        }
    }

    private void scanLobby(Minecraft mc, boolean forcePrint) {
        Collection<NetworkPlayerInfo> players = mc.getNetHandler().getPlayerInfoMap();
        Scoreboard scoreboard = mc.theWorld.getScoreboard();
        if (scoreboard == null) return;

        Set<String> currentUuids = new HashSet<>();
        int totalPrestige = 0, validPlayers = 0;
        int[] brackets = new int[13];

        List<String> friendsInLobby = new ArrayList<>();
        List<String> enemiesInLobby = new ArrayList<>();

        for (NetworkPlayerInfo info : players) {
            String uuid = info.getGameProfile().getId().toString().replace("-", "");
            String rawName = info.getGameProfile().getName();
            currentUuids.add(uuid);

            if (!firstScanDone) {
                if (PitConfig.social().friends.containsKey(uuid)) friendsInLobby.add(rawName);
                if (PitConfig.social().enemies.containsKey(uuid)) enemiesInLobby.add(rawName);
            } else if (!knownLobbyUuids.contains(uuid)) {
                if (PitConfig.social().notifyFriends && PitConfig.social().friends.containsKey(uuid)) {
                    mc.thePlayer.addChatMessage(new ChatComponentText("§a§l[Friend] §e" + rawName + " §ajoined the lobby!"));
                } else if (PitConfig.social().notifyEnemies && PitConfig.social().enemies.containsKey(uuid)) {
                    mc.thePlayer.addChatMessage(new ChatComponentText("§c§l[Enemy] §e" + rawName + " §cjoined the lobby!"));
                }
            }

            int prestige;
            if (exactPrestiges.containsKey(rawName)) prestige = exactPrestiges.get(rawName);
            else {
                ScorePlayerTeam team = scoreboard.getPlayersTeam(rawName);
                prestige = getApproxPrestigeFromColor(ScorePlayerTeam.formatPlayerName(team, rawName));
            }

            totalPrestige += prestige;
            validPlayers++;
            brackets[getBracketIndex(prestige)]++;
        }

        knownLobbyUuids.clear();
        knownLobbyUuids.addAll(currentUuids);

        if (!firstScanDone) {
            if (PitConfig.social().notifyFriends && !friendsInLobby.isEmpty()) {
                mc.thePlayer.addChatMessage(new ChatComponentText("§a§l[Friends in Lobby] §7" + String.join(", ", friendsInLobby)));
            }
            if (PitConfig.social().notifyEnemies && !enemiesInLobby.isEmpty()) {
                mc.thePlayer.addChatMessage(new ChatComponentText("§c§l[Enemies in Lobby] §7" + String.join(", ", enemiesInLobby)));
            }
        }

        if (validPlayers > 0 && PitConfig.social().lobbyScanner) {
            double currentAverage = (double) totalPrestige / validPlayers;
            if (forcePrint) {
                printLobbyStats(mc, currentAverage, validPlayers, brackets, false, true);
            } else if (!firstScanDone) {
                printLobbyStats(mc, currentAverage, validPlayers, brackets, false, false);
                lastAveragePrestige = currentAverage;
                firstScanDone = true;
            } else if (Math.abs(currentAverage - lastAveragePrestige) >= 2.0) {
                printLobbyStats(mc, currentAverage, validPlayers, brackets, true, false);
                lastAveragePrestige = currentAverage;
            }
        }
    }

    private void printLobbyStats(Minecraft mc, double avg, int total, int[] brackets, boolean isUpdate, boolean manual) {
        String prefix = manual ? "§6§lSTATS" : "§b§lLOBBY";
        String type = isUpdate ? "§fShifted" : "§fPrestige";

        mc.thePlayer.addChatMessage(new ChatComponentText("§8§m----------------------------------------"));
        mc.thePlayer.addChatMessage(new ChatComponentText(prefix + " §8» " + type + ": §e" + String.format("%.2f", avg) + " §7(" + total + " players)"));

        for (int i = 0; i < brackets.length; i++) {
            if (brackets[i] > 0) {
                int pct = (int) Math.round((brackets[i] / (double) total) * 100);
                mc.thePlayer.addChatMessage(new ChatComponentText(" §7• " + BRACKET_NAMES[i] + " §8» §f" + pct + "% §7(" + brackets[i] + ")"));
            }
        }
        mc.thePlayer.addChatMessage(new ChatComponentText("§8§m----------------------------------------"));
    }

    private int getBracketIndex(int p) {
        if (p == 0) return 0;
        if (p <= 4) return 1;
        if (p <= 9) return 2;
        if (p <= 14) return 3;
        if (p <= 19) return 4;
        if (p <= 24) return 5;
        if (p <= 29) return 6;
        if (p <= 34) return 7;
        if (p <= 39) return 8;
        if (p <= 44) return 9;
        if (p <= 47) return 10;
        if (p <= 49) return 11;
        return 12;
    }

    private int getApproxPrestigeFromColor(String formatted) {
        int index = formatted.indexOf('§');
        if (index != -1 && index + 1 < formatted.length()) {
            char color = Character.toLowerCase(formatted.charAt(index + 1));
            switch (color) {
                case '7':
                    return 0;
                case '9':
                    return 2;
                case 'e':
                    return 7;
                case '6':
                    return 12;
                case 'c':
                    return 17;
                case '5':
                    return 22;
                case 'd':
                    return 27;
                case 'f':
                    return 32;
                case 'b':
                    return 37;
                case '1':
                    return 42;
                case '0':
                    return 46;
                case '4':
                    return 48;
                case '8':
                    return 50;
            }
        }
        return 0;
    }

    private int romanToInt(String s) {
        int total = 0, prevValue = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            int value = 0;
            switch (s.charAt(i)) {
                case 'I':
                    value = 1;
                    break;
                case 'V':
                    value = 5;
                    break;
                case 'X':
                    value = 10;
                    break;
                case 'L':
                    value = 50;
                    break;
                case 'C':
                    value = 100;
                    break;
                case 'D':
                    value = 500;
                    break;
                case 'M':
                    value = 1000;
                    break;
            }
            if (value < prevValue) total -= value;
            else total += value;
            prevValue = value;
        }
        return total;
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (event.type == 2 || !ServerState.onHypixel || ServerState.currentGameMode != HypixelGameMode.PIT) return;

        String unformatted = event.message.getUnformattedText();
        String formatted = event.message.getFormattedText();
        String clean = STRIP_COLOR.matcher(unformatted).replaceAll("").trim();

        Matcher m = CHAT_PRESTIGE_PATTERN.matcher(clean);
        while (m.find()) {
            String roman = m.group(1);
            String name = m.group(2);
            int prestige = (roman != null) ? romanToInt(roman) : 0;

            Integer existing = exactPrestiges.get(name);
            if (prestige != 0 && existing == null || existing != prestige) {
                exactPrestiges.put(name, prestige);

                if (false) {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(
                            "§8[PitHelper Debug] §7Saved exact prestige for §e" + name + " §7as §d" + prestige
                    ));
                }

                scanLobby(Minecraft.getMinecraft(), false);
            }
        }

        boolean modified = false;

        if (PitConfig.social().highlightFriends) {
            for (String friendName : PitConfig.social().friends.values()) {
                if (formatted.contains(friendName)) {
                    formatted = formatted.replace(friendName, "§a§l" + friendName + "§r");
                    modified = true;
                }
            }
        }

        if (PitConfig.social().highlightEnemies) {
            for (String enemyName : PitConfig.social().enemies.values()) {
                if (formatted.contains(enemyName)) {
                    formatted = formatted.replace(enemyName, "§c§l" + enemyName + "§r");
                    modified = true;
                }
            }
        }

        if (modified) {
            event.message = new ChatComponentText(formatted);
        }
    }
}