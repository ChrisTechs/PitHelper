package github.christechs.pithelper.commands

import gg.essential.api.EssentialAPI
import gg.essential.api.commands.Command
import gg.essential.api.commands.DefaultHandler
import gg.essential.api.commands.SubCommand
import github.christechs.pithelper.config.PitHelperConfig
import github.christechs.pithelper.gui.EventsGui

object PitHelperCommands : Command("pithelper") {

    @SubCommand("events")
    fun events() {
        EssentialAPI.getGuiUtil().openScreen(EventsGui)
    }

    @SubCommand("settings")
    fun settings() {
        EssentialAPI.getGuiUtil().openScreen(PitHelperConfig.gui())
    }

    @DefaultHandler
    fun execute() {
        EssentialAPI.getGuiUtil().openScreen(PitHelperConfig.gui())
    }

}