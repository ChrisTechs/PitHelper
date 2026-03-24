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

package io.github.christechs.pithelper.commands;

import io.github.christechs.pithelper.features.LobbyTracker;
import io.github.christechs.pithelper.features.NotificationManager;
import io.github.christechs.pithelper.ui.screen.PitHelperMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static io.github.christechs.pithelper.utils.ChatUtil.simulateChat;

public class PitHelperCommand extends CommandBase {

    public static void register() {
        ClientCommandHandler.instance.registerCommand(new PitHelperCommand());
    }

    @Override
    public String getCommandName() {
        return "pithelper";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/pithelper [help|test|dumpnames|lobbystats]";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 0) {
            String subCmd = args[0].toLowerCase();

            if (subCmd.equals("help")) {
                sender.addChatMessage(new ChatComponentText("§6§l=== PitHelper Commands ==="));
                sender.addChatMessage(new ChatComponentText("§e/pithelper §7- Opens the main config menu."));
                sender.addChatMessage(new ChatComponentText("§e/apiexplorer §7- Opens the API Explorer & Lobby Search."));
                sender.addChatMessage(new ChatComponentText("§e/viewinv <player> §7- Opens the Profile Viewer for a player."));
                sender.addChatMessage(new ChatComponentText("§e/pitfriend <player> §7- Adds/removes a player as a friend."));
                sender.addChatMessage(new ChatComponentText("§e/pitenemy <player> §7- Adds/removes a player as an enemy."));
                sender.addChatMessage(new ChatComponentText("§e/pithelper lobbystats §7- Manually prints current lobby prestige stats."));
                return;
            }

            if (subCmd.equals("lobbystats")) {
                if (LobbyTracker.INSTANCE != null) {
                    LobbyTracker.INSTANCE.printManualStats();
                } else {
                    sender.addChatMessage(new ChatComponentText("§cLobby Tracker is not currently active (Are you in The Pit?)."));
                }
                return;
            }

            if (subCmd.equals("test")) {
                if (args.length > 1) {
                    String testType = args[1].toLowerCase();
                    switch (testType) {
                        case "quickmaths":
                            simulateChat("§d§lQUICK MATHS! §eSolve: (2+8)x5");
                            return;
                        case "chat":
                            simulateChat("§6§l[PitHelper] §eTEST EVENT §7starts in §a0m 0s§7!");
                            return;
                        case "visual":
                            NotificationManager.add(null, "Mock notification test.", 5000);
                            return;
                    }
                }
                sender.addChatMessage(new ChatComponentText("§cUsage: /pithelper test <quickmaths|chat|visual>"));
                return;
            }
        }

        MinecraftForge.EVENT_BUS.register(new Object() {
            @SubscribeEvent
            public void onClientTick(TickEvent.ClientTickEvent event) {
                if (event.phase == TickEvent.Phase.END) {
                    Minecraft.getMinecraft().displayGuiScreen(new PitHelperMenu());
                    MinecraftForge.EVENT_BUS.unregister(this);
                }
            }
        });
    }
}