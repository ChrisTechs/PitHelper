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

import io.github.christechs.config.ConfigManager;
import io.github.christechs.pithelper.api.PitHelperAPI;
import io.github.christechs.pithelper.config.PitConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PitEnemyCommand extends CommandBase {

    public static void register() {
        ClientCommandHandler.instance.registerCommand(new PitEnemyCommand());
    }

    @Override
    public String getCommandName() {
        return "pitenemy";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/pitenemy <player>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            List<String> names = new ArrayList<>();
            if (Minecraft.getMinecraft().getNetHandler() != null) {
                for (NetworkPlayerInfo info : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {
                    names.add(info.getGameProfile().getName());
                }
            }
            return getListOfStringsMatchingLastWord(args, names.toArray(new String[0]));
        }
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            sender.addChatMessage(new ChatComponentText("§cUsage: /pitenemy <player>"));
            return;
        }

        String name = args[0];

        String existingUuid = null;
        for (String uuid : PitConfig.social().enemies.keySet()) {
            if (PitConfig.social().enemies.get(uuid).equalsIgnoreCase(name)) {
                existingUuid = uuid;
                break;
            }
        }

        if (existingUuid != null) {
            PitConfig.social().enemies.remove(existingUuid);
            ConfigManager.save();
            sender.addChatMessage(new ChatComponentText("§cRemoved §e" + name + " §cfrom enemies."));
            return;
        }

        sender.addChatMessage(new ChatComponentText("§eAdding enemy..."));

        NetworkPlayerInfo info = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(name);
        if (info != null) {
            String uuid = info.getGameProfile().getId().toString().replace("-", "");
            addEnemy(sender, uuid, info.getGameProfile().getName());
        } else {
            CompletableFuture.runAsync(() -> {
                try {
                    String uuid = PitHelperAPI.resolveUuid(name);
                    Minecraft.getMinecraft().addScheduledTask(() -> addEnemy(sender, uuid, name));
                } catch (Exception e) {
                    Minecraft.getMinecraft().addScheduledTask(() ->
                            sender.addChatMessage(new ChatComponentText("§cFailed to find player: " + e.getMessage()))
                    );
                }
            });
        }
    }

    private void addEnemy(ICommandSender sender, String uuid, String name) {
        PitConfig.social().enemies.put(uuid, name);
        PitConfig.social().friends.remove(uuid);
        ConfigManager.save();
        sender.addChatMessage(new ChatComponentText("§cAdded §e" + name + " §cto enemies!"));
    }
}