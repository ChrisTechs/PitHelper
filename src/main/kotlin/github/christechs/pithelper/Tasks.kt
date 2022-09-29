package github.christechs.pithelper

import gg.essential.api.EssentialAPI
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

object Tasks {

    private val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(2)

    private val tasks = mutableMapOf<Int, ScheduledFuture<*>>()

    private var taskId = -1
        @Synchronized
        get() {
            field++
            return field
        }

    init {
        EssentialAPI.getShutdownHookUtil().register {
            scheduler.shutdownNow()
        }
    }

    fun scheduleTask(task: (Int) -> Unit, delay: Long, timeUnit: TimeUnit = TimeUnit.SECONDS): Int {

        val id = taskId

        synchronized(tasks) {

            val scheduledFuture = scheduler.schedule({
                task.invoke(id)
            }, delay, timeUnit)

            tasks[id] = scheduledFuture
        }

        return id
    }

    fun scheduleRepeatingTask(
        task: (Int) -> Unit,
        delay: Long,
        period: Long,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ): Int {

        val id = taskId

        synchronized(tasks) {

            val scheduledFuture = scheduler.scheduleAtFixedRate({
                task.invoke(id)
            }, delay, period, timeUnit)

            tasks[id] = scheduledFuture
        }

        return id
    }

    @Synchronized
    fun cancel(id: Int, value: Boolean = true) {
        (tasks[id] ?: return).cancel(value)
    }

    @Synchronized
    fun isCancelled(id: Int): Boolean {
        return (tasks[id] ?: return true).isCancelled
    }

}