package github.christechs.pithelper.features.events

private var eventId = -1
    @Synchronized get() {
        field++
        return field
    }

data class Event(
    val date: String = "",
    val timestamp: Long = 0,
    val event: Events = Events.NONE,
    val type: EventTypes = EventTypes.NONE,
    val notifications: MutableSet<Int> = mutableSetOf(299, 59, 30, 10),
    val id: Int = eventId
)
