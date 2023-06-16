package github.christechs.pithelper

import gg.essential.api.EssentialAPI
import github.christechs.pithelper.commands.PitHelperCommands
import github.christechs.pithelper.features.events.EventsHandler
import github.christechs.pithelper.features.quickmaths.ChatListener
import github.christechs.pithelper.utils.Tasks
import github.christechs.pithelper.versions.PitHelperInfo
import github.christechs.pithelper.versions.VersionHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent

@Mod(
    name = Constants.name,
    version = Constants.version,
    modid = Constants.modid
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

        EssentialAPI.getShutdownHookUtil().register {
            Tasks.PIT_HELPER_DISPATCHER.close()
        }

        PitHelperInfo.initialize()

        VersionHandler.updateConfig()

        MinecraftForge.EVENT_BUS.register(ChatListener)

        MinecraftForge.EVENT_BUS.register(Tasks)

        EventsHandler.refresh()

    }

    @Mod.EventHandler
    fun postInitialize(event: FMLPostInitializationEvent) {
        PitHelperCommands.register()
    }

}