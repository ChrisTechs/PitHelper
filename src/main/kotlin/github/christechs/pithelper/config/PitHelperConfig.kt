package github.christechs.pithelper.config

import gg.essential.universal.ChatColor
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Category
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import gg.essential.vigilance.data.SortingBehavior
import net.minecraft.launchwrapper.Launch

object PitHelperConfig : Vigilant(

    Launch.minecraftHome
        .resolve("config/ChrisTechs/PitHelper/config.toml")
        .also { it.parentFile.mkdirs() },

    guiTitle = "${ChatColor.GREEN}PitHelper",

    sortingBehavior = object: SortingBehavior() {

        override fun getCategoryComparator(): Comparator<in Category> {
            return compareBy({ it.name != "General" }, { it.name })
        }

    }

) {

    // General
    @Property(
        type = PropertyType.SWITCH,
        i18nName = "Enabled",
        name = "pit_helper_enabled",
        i18nCategory = "General",
        category = "general",
        description = "Mod enabled."
    )
    var enabled = true

    @Property(
        type = PropertyType.SWITCH,
        i18nName = "Hypixel Only",
        name = "hypixel_only_enabled",
        i18nCategory = "General",
        category = "general",
        description = "Mod only enabled on hypixel."
    )
    var hypixelOnly = true

    @Property(
        type = PropertyType.SWITCH,
        i18nName = "Quick Maths Solver",
        name = "quick_maths_solver_enabled",
        i18nCategory = "General",
        category = "general",
        description = "Solve Quick Math"
    )
    var quickMaths = true

    @Property(
        type = PropertyType.SWITCH,
        i18nName = "Events Notifications (Can be inaccurate)",
        name = "event_notifications_enabled",
        i18nCategory = "General",
        category = "general",
        description = "Sends you notifications about upcoming events"
    )
    var eventsNotifications = true

    // Quick Maths
    @Property(
        type = PropertyType.SWITCH,
        i18nName = "Copy To Clipboard",
        name = "quick_maths_copy_clipboard_enabled",
        i18nCategory = "Quick Maths",
        category = "quick_maths",
        description = "Copy Quick Math answer to clipboard"
    )
    var quickMathsClipboard = false

    // Quick Maths Auto Answer
    @Property(
        type = PropertyType.SWITCH,
        i18nName = "Auto Answer (Bannable)",
        name = "quick_maths_auto_answer_enabled",
        i18nCategory = "Quick Maths",
        category = "quick_maths",
        description = "Automatically sends Quick Math answer to chat at the end of Quick Maths"
    )
    var quickMathsAutoAnswer = false

    // Events
    @Property(
        type = PropertyType.SWITCH,
        i18nName = "Major Events Notifications",
        name = "major_events_notifications_enabled",
        category = "Events",
        subcategory = "Notifications",
        description = "Sends you notifications for upcoming major event"
    )
    var majorEventsNotifications = true

    @Property(
        type = PropertyType.SWITCH,
        i18nName = "Minor Events Notifications",
        name = "minor_events_notifications_enabled",
        category = "Events",
        subcategory = "Notifications",
        description = "Sends you notifications for upcoming minor event"
    )
    var minorEventsNotifications = false

}