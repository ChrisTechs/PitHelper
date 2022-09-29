package github.christechs.pithelper.config

import gg.essential.vigilance.data.Category
import gg.essential.vigilance.data.SortingBehavior

object PitSortingBehaviour : SortingBehavior() {

    override fun getCategoryComparator(): Comparator<in Category> {
        return compareBy({ it.name != "General" }, { it.name })
    }

}