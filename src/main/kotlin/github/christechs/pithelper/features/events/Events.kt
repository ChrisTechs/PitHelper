package github.christechs.pithelper.features.events

import java.awt.Color

enum class Events(val eventName: String, val duration: Long, val colour: Color, val addedTime: Long = 0L) {
    QUICK_MATHS("Quick Maths", 5000, Color(255, 85, 255, 100)),
    DRAGON_EGG("Dragon Egg", 0, Color(170, 0, 170, 100)),
    TWO_X_REWARDS("2x Rewards", 4 * 60000, Color(0, 0, 170, 100)),
    CARE_PACKAGE("Care Package", 0, Color(0, 170, 0, 100)),
    KOTL("KOTL", 3 * 60000, Color(0, 170, 170, 100)),
    KOTH("KOTH", 4 * 60000, Color(170, 0, 0, 100)),
    AUCTION("Auction", 0, Color(170, 170, 170, 100)),
    GIANT_CAKE("Giant Cake", 2 * 60000, Color(85, 85, 255, 100)),
    ALL_BOUNTY("All bounty", 0, Color(85, 255, 85, 100)),
    SQAUDS("Squads", 5 * 60000, Color(85, 255, 255, 100), 3 * 60000),
    BEAST("Beast", 5 * 60000, Color(255, 85, 85, 100), 3 * 60000),
    PIZZA("Pizza", 5 * 60000, Color(255, 255, 85, 100), 3 * 60000),
    RAGE_PIT("Rage Pit", 4 * 60000, Color(139, 13, 13, 100), 3 * 60000),
    SPIRE("Spire", 5 * 60000, Color(18, 12, 12, 100), 3 * 60000),
    ROBBERY("Robbery", 4 * 60000, Color(165, 42, 42, 100), 3 * 60000),
    BLOCKHEAD("Blockhead", 5 * 60000, Color(124, 252, 0, 100), 3 * 60000),
    RAFFLE("Raffle", 5 * 60000, Color(175, 115, 74, 100), 3 * 60000),
    TEAM_DEATHMATCH("Team Deathmatch", 4 * 60000, Color(0, 0, 0, 100), 3 * 60000),
    NONE("None", 0, Color(0, 0, 0, 0));

    companion object {
        fun fromEventName(name: String): Events {

            for (value in values()) {
                if (value.eventName == name) return value
            }

            return NONE
        }
    }

}