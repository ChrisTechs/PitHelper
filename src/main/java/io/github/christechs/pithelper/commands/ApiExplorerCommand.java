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

import io.github.christechs.pithelper.features.NotificationHandler;
import io.github.christechs.pithelper.ui.screen.ApiExplorerScreen;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.client.ClientCommandHandler;

public class ApiExplorerCommand extends CommandBase {

    public static void register() {
        ClientCommandHandler.instance.registerCommand(new ApiExplorerCommand());
    }

    @Override
    public String getCommandName() {
        return "apiexplorer";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/apiexplorer";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        sender.addChatMessage(new ChatComponentText("§cFeature not supported."));
        //NotificationHandler.pendingScreen = new ApiExplorerScreen(null);
    }
}
