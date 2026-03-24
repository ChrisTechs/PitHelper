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

package io.github.christechs.pithelper;

import io.github.christechs.config.ConfigManager;
import io.github.christechs.pithelper.commands.*;
import io.github.christechs.pithelper.config.PitConfig;
import io.github.christechs.pithelper.features.*;
import io.github.christechs.pithelper.ui.hud.EventOverlay;
import io.github.christechs.pithelper.utils.PlayerState;
import io.github.christechs.pithelper.utils.ServerState;
import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = "pithelper", useMetadata = true)
public class PitHelper {

    public static Logger PH_LOGGER = LogManager.getLogger("PitHelper");

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        File configFile = new File(event.getModConfigurationDirectory(), "pithelper.json");

        ConfigManager.init(configFile);

        PitConfig.registerDynamicConfigs(configFile);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        PitHelperCommand.register();
        ViewInvCommand.register();
        ApiExplorerCommand.register();
        PitFriendCommand.register();
        PitEnemyCommand.register();

        HypixelModAPI.getInstance().subscribeToEventPacket(ClientboundLocationPacket.class);
        HypixelModAPI.getInstance().createHandler(ClientboundLocationPacket.class, (packet) -> {
            ServerState.updateFromPacket(packet.getServerType(), packet.getLobbyName());
        });

        AutoSpawnHandler.registerKeybind();

        MinecraftForge.EVENT_BUS.register(new ServerState());
        MinecraftForge.EVENT_BUS.register(new LobbyTracker());
        MinecraftForge.EVENT_BUS.register(new EventOverlay());
        MinecraftForge.EVENT_BUS.register(new NotificationHandler());
        MinecraftForge.EVENT_BUS.register(new QuickMathsHandler());
        MinecraftForge.EVENT_BUS.register(new AfterDeathHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerState());
        MinecraftForge.EVENT_BUS.register(new AutoSpawnHandler());
    }
}