package github.christechs.pithelper.features.events

import gg.essential.api.EssentialAPI
import gg.essential.universal.ChatColor
import github.christechs.pithelper.config.PitHelperConfig
import github.christechs.pithelper.gui.EventsGui
import github.christechs.pithelper.utils.Tasks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.*
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object EventsHandler {

    private val client = HttpClients.createDefault()

    private val sdf = SimpleDateFormat("dd MMM HH:mm", Locale.getDefault())

    private var previousTask: Job? = null

    private var cooldown = 0L

    fun refresh() {

        Tasks.runAsyncTask({

            if (cooldown < System.currentTimeMillis()) {
                cooldown = System.currentTimeMillis() + 10000L
            } else return@runAsyncTask

            if (previousTask != null) {
                previousTask!!.cancel()
                while (!previousTask!!.isCancelled) {
                    delay(200L)
                }
            }

            val request = HttpGet("https://brookeafk.com/emergency.js?v=${System.currentTimeMillis()}")

            val response = client.execute(request)

            val eventsData = response.entity.content
                    .bufferedReader()
                    .readText()
                    .replace("var emergency_data = ", "")
                    .replace("; doEverything(1);", "")
                    .trim()

            withContext(Dispatchers.IO) {
                response.close()
            }

            var mainEvent: Event? = null

            var currentTime = System.currentTimeMillis()

            val events = Json.decodeFromString(JsonArray.serializer(), eventsData)
                    .mapFilterToMutableList {

                        val eventData = Json.decodeFromString(JsonObject.serializer(), it.toString())

                        val e = eventData["event"]?.jsonPrimitive?.content
                                ?: return@mapFilterToMutableList Optional.empty()

                        val timestamp = (eventData["timestamp"]?.jsonPrimitive?.long
                                ?: return@mapFilterToMutableList Optional.empty())

                        val type = eventData["type"]?.jsonPrimitive?.content
                                ?: return@mapFilterToMutableList Optional.empty()

                        val eventInfo = Events.fromEventName(e)

                        val event = Event(
                                date = sdf.format(Date(timestamp)),
                                event = eventInfo,
                                timestamp = timestamp,
                                type = EventTypes.fromEventTypeName(type),
                        )

                        if (timestamp < currentTime) {

                            val difference = currentTime - timestamp

                            if (difference <= event.event.duration &&
                                    (mainEvent == null || difference < (currentTime - mainEvent!!.timestamp))
                            ) {
                                mainEvent = event
                            }

                            return@mapFilterToMutableList Optional.empty()
                        }

                        return@mapFilterToMutableList Optional.of(event)

                    }

            events.sortedWith(compareBy { it.timestamp })

            Tasks.runTask {
                EventsGui.refreshEvents(events)
            }

            val eventsToRemove = mutableListOf<Event>()
            val notificationToRemove = mutableSetOf<Int>()

            previousTask = Tasks.runAsyncRepeatingTask({

                if (!PitHelperConfig.enabled) return@runAsyncRepeatingTask

                if (mainEvent != null) {

                    currentTime = System.currentTimeMillis()

                    var countdown = mainEvent!!.event.duration - (currentTime - mainEvent!!.timestamp)

                    if (countdown < 0) {

                        Tasks.runTask { EventsGui.refreshEvents(events) }

                        mainEvent = null
                    } else {

                        val minutes = TimeUnit.MILLISECONDS.toMinutes(countdown)
                        countdown -= TimeUnit.MINUTES.toMillis(minutes)
                        val seconds = TimeUnit.MILLISECONDS.toSeconds(countdown)

                        val countdownText =
                                if (minutes <= 0) {
                                    "${seconds}s"
                                } else "${minutes}m${seconds}s"

                        EventsGui.setText(
                                mainEvent!!.id,
                                "${ChatColor.GOLD} ${ChatColor.BOLD}Event Active${ChatColor.RESET}: $countdownText"
                        )
                    }

                }

                for (e in events) {

                    if (mainEvent != null && mainEvent!!.id == e.id) continue

                    currentTime = System.currentTimeMillis()

                    if (e.timestamp < currentTime) {
                        if (PitHelperConfig.eventsNotifications)
                            EssentialAPI.getNotifications().push(e.event.eventName, "Has started")
                        mainEvent = e

                        eventsToRemove.add(e)
                        continue
                    }

                    var countdown = e.timestamp - currentTime

                    val hours = TimeUnit.MILLISECONDS.toHours(countdown)
                    countdown -= TimeUnit.HOURS.toMillis(hours)
                    val minutes = TimeUnit.MILLISECONDS.toMinutes(countdown)
                    countdown -= TimeUnit.MINUTES.toMillis(minutes)
                    val seconds = TimeUnit.MILLISECONDS.toSeconds(countdown)

                    val countdownText =
                            if (minutes <= 0) {
                                "${seconds}s"
                            } else if (hours <= 0) {
                                "${minutes}m${seconds}s"
                            } else {
                                "${hours}h${minutes}m"
                            }

                    EventsGui.setText(
                            e.id,
                            """
                        Event Time: ${e.date}
                        Event starts in: $countdownText
                        """.trimIndent()
                    )

                    if (PitHelperConfig.hypixelOnly && !EssentialAPI.getMinecraftUtil().isHypixel()) continue
                    if (!PitHelperConfig.eventsNotifications) continue

                    notificationToRemove.clear()

                    val secondsLeft = TimeUnit.MILLISECONDS.toSeconds(e.timestamp - currentTime)

                    for (notification in e.notifications) {
                        if (secondsLeft !in (notification - 10)..notification) continue

                        if (e.type == EventTypes.MAJOR && !PitHelperConfig.majorEventsNotifications) continue
                        if (e.type == EventTypes.MINOR && !PitHelperConfig.minorEventsNotifications) continue

                        notificationToRemove.add(notification)

                        val notificationText =
                                if (hours >= 1) {
                                    "${e.type.name} event in ${hours}h${minutes}m"
                                } else if (minutes >= 1) {
                                    "${e.type.name} event in ${minutes}m${seconds}"
                                } else {
                                    "${e.type.name} event in ${seconds}s"
                                }

                        EssentialAPI.getNotifications().push(e.event.eventName, notificationText, 2.5f)
                    }

                    e.notifications.removeAll(notificationToRemove)

                }

                if (eventsToRemove.isNotEmpty()) {
                    events.removeAll(eventsToRemove)
                    eventsToRemove.clear()
                }

            }, 0L, 1L, TimeUnit.SECONDS)

        })

    }

    private fun <T> JsonArray.mapFilterToMutableList(condition: (JsonElement) -> Optional<T>): MutableList<T> {

        val newList = ArrayList<T>()

        for (element in this) {
            val conditionResult = condition.invoke(element)

            if (!conditionResult.isPresent) continue

            newList.add(conditionResult.get())
        }

        return newList
    }

}