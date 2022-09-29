package github.christechs.pithelper.config

import gg.essential.universal.ChatColor
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import net.minecraft.launchwrapper.Launch

object PitSolverConfig : Vigilant(
    Launch.minecraftHome
        .resolve("config/ChrisTechs/PitHelper/config.toml")
        .also { it.parentFile.mkdirs() },
    guiTitle = "${ChatColor.GREEN}PitHelper",
    sortingBehavior = PitSortingBehaviour
) {

    // General
    @Property(
        type = PropertyType.SWITCH,
        name = "Enabled",
        category = "General",
        description = "Mod enabled."
    )
    var enabled = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Hypixel Only",
        category = "General",
        description = "Mod only enabled on hypixel."
    )
    var hypixelOnly = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Quick Math Solver",
        category = "General",
        description = "Solve Quick Math"
    )
    var quickMaths = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Events Notifications (Can be inaccurate)",
        category = "General",
        description = "Sends you notifications when events are about to start"
    )
    var eventsNotifications = true

    // Quick Maths
    @Property(
        type = PropertyType.SWITCH,
        name = "Copy To Clipboard",
        category = "Quick Maths",
        description = "Copy Quick Math answer to clipboard"
    )
    var quickMathsClipboard = false

    // Quick Maths Auto Answer
    @Property(
        type = PropertyType.SWITCH,
        name = "Enabled (Bannable)",
        category = "Quick Maths",
        subcategory = "Auto Answer",
        description = "Automatically sends Quick Math answers to chat"
    )
    var quickMathsAutoAnswer = false

    @Property(
        type = PropertyType.DECIMAL_SLIDER,
        name = "Random Offset Range",
        category = "Quick Maths",
        subcategory = "Auto Answer",
        description = "Max range for random Quick Maths answer delay",
        minF = 0f,
        maxF = 5f
    )
    var quickMathsAutoAnswerOffset = 5f

    @Property(
        type = PropertyType.DECIMAL_SLIDER,
        name = "Answer Delay",
        category = "Quick Maths",
        subcategory = "Auto Answer",
        description = "Quick Maths answer send delay",
        minF = 0f,
        maxF = 5f
    )
    var quickMathsAutoAnswerDelay = 5f

    // Events
    @Property(
        type = PropertyType.SWITCH,
        name = "Major Events Notifications",
        category = "Events",
        subcategory = "Notifications",
        description = "Sends you notifications for major event"
    )
    var majorEventsNotifications = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Minor Events Notifications",
        category = "Events",
        subcategory = "Notifications",
        description = "Sends you notifications for minor event"
    )
    var minorEventsNotifications = false

}