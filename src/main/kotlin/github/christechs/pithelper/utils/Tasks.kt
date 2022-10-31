package github.christechs.pithelper.utils

import kotlinx.coroutines.*
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@OptIn(DelicateCoroutinesApi::class)
object Tasks {

    val PIT_HELPER_DISPATCHER = Executors.newScheduledThreadPool(2).asCoroutineDispatcher()

    val mainThreadTasks: MutableList<Runnable> = ArrayList()

    fun runAsyncTask(
        runnable: suspend CoroutineScope.() -> Unit,
        delay: Long = 0L,
        timeUnit: TimeUnit = TimeUnit.MILLISECONDS
    ): Job {
        val startTime = System.currentTimeMillis()
        return GlobalScope.launch(PIT_HELPER_DISPATCHER) {
            delay((timeUnit.toMillis(delay) - (System.currentTimeMillis() - startTime)).coerceAtLeast(0))
            runnable.invoke(this)
        }
    }

    fun runAsyncRepeatingTask(
        runnable: suspend CoroutineScope.() -> Unit,
        delay: Long = 0L,
        period: Long = 0L,
        timeUnit: TimeUnit = TimeUnit.MILLISECONDS
    ): Job {
        val startTime = System.currentTimeMillis()
        return GlobalScope.launch(PIT_HELPER_DISPATCHER) {
            delay((timeUnit.toMillis(delay) - (System.currentTimeMillis() - startTime)).coerceAtLeast(0))
            while (isActive) {
                delay(timeUnit.toMillis(period))
                runnable.invoke(this)
            }
        }
    }

    fun runTask(runnable: Runnable) {
        synchronized(mainThreadTasks) {
            mainThreadTasks.add(runnable)
        }
    }

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if (mainThreadTasks.isEmpty()) return

        synchronized(mainThreadTasks) {
            for (task in mainThreadTasks) {
                task.run()
            }
            mainThreadTasks.clear()
        }
    }

}