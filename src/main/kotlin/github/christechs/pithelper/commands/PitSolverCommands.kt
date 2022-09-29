package github.christechs.pithelper.commands

import gg.essential.api.EssentialAPI
import gg.essential.api.commands.Command
import gg.essential.api.commands.DefaultHandler
import gg.essential.api.commands.SubCommand
import github.christechs.pithelper.config.PitSolverConfig
import github.christechs.pithelper.gui.EventsGui

object PitSolverCommands : Command("pithelper") {

    @SubCommand("events")
    fun events() {
        EssentialAPI.getGuiUtil().openScreen(EventsGui)
    }

    @SubCommand("settings")
    fun settings() {
        EssentialAPI.getGuiUtil().openScreen(PitSolverConfig.gui())
    }

    @DefaultHandler
    fun execute() {
        EssentialAPI.getGuiUtil().openScreen(PitSolverConfig.gui())
    }

}