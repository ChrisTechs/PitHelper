package github.christechs.pithelper.features.events

enum class EventTypes(val eventTypeName: String) {
    MINOR("minor"),
    MAJOR("major"),
    NONE("none");

    companion object {

        fun fromEventTypeName(name: String): EventTypes {

            for (value in EventTypes.values()) {
                if (value.eventTypeName == name) return value
            }

            return NONE
        }

    }

}