package github.christechs.pithelper

import github.christechs.pithelper.commands.PitSolverCommands
import github.christechs.pithelper.config.PitSolverConfig
import github.christechs.pithelper.features.quickmaths.ChatListener
import github.christechs.pithelper.gui.EventsGui
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent

@Mod(
    name = "PitHelper",
    version = "0.0.1",
    modid = "pithelper"
)
class PitHelper {

    companion object {

        @Mod.Instance
        @JvmStatic
        lateinit var instance: PitHelper
            private set

    }

    @Mod.EventHandler
    fun initialize(event: FMLInitializationEvent) {

        PitSolverConfig.initialize()

        MinecraftForge.EVENT_BUS.register(ChatListener)

    }

    @Mod.EventHandler
    fun postInitialize(event: FMLPostInitializationEvent) {
        PitSolverCommands.register()
        EventsGui.refreshEvents(emptyList())
    }

}