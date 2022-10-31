package github.christechs.pithelper.features.quickmaths

import com.notkamui.keval.Keval
import gg.essential.api.EssentialAPI
import gg.essential.universal.ChatColor
import github.christechs.pithelper.config.PitHelperConfig
import github.christechs.pithelper.utils.Tasks
import kotlinx.coroutines.delay
import net.minecraft.client.Minecraft
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

object ChatListener {

    private const val COLOR_CHAR = '\u00A7'
    private val STRIP_COLOR_PATTERN = Pattern.compile("(?i)$COLOR_CHAR[0-9A-FK-OR]")

    private val random = Random()

    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent) {

        if (PitHelperConfig.hypixelOnly && !EssentialAPI.getMinecraftUtil().isHypixel()) return

        if (!PitHelperConfig.enabled) return

        if (!PitHelperConfig.quickMaths) return

        val message = STRIP_COLOR_PATTERN.matcher(event.message.unformattedText.lowercase()).replaceAll("")

        if (message.startsWith("quick maths! solve: ")) {

            val problem = message
                .substring(message.indexOf("solve: ") + 7)
                .replace(" ", "").replace("x", "*")

            var result = Keval.eval(problem).toString()

            if (result.split(".")[1].toLong() <= 0)
                result = result.split(".")[0].replace(".", "")

            if (PitHelperConfig.quickMathsAutoAnswer) {

                val delay = 4 + random.nextFloat()

                Tasks.runAsyncTask({
                    Minecraft.getMinecraft().thePlayer.sendChatMessage(result)
                }, (delay * 1000).toLong(), TimeUnit.MILLISECONDS)

            }

            if (PitHelperConfig.quickMathsClipboard) {
                copyToClipboard(result)
            }

            EssentialAPI.getNotifications().push(
                "QUICK MATH",
                "Answer: $result. " +
                        "\nClick me to copy answer",
                5f,
            ) { copyToClipboard(result) }

            EssentialAPI.getMinecraftUtil().sendMessage(
                ChatColor.translateAlternateColorCodes('&', "&f&l[&e&lPitHelper&f&l] "),
                ChatColor.translateAlternateColorCodes(
                    '&',
                    "&6&lAnswer: $result. " +
                            if (PitHelperConfig.quickMathsClipboard) "\nCopied answer to clipboard" else ""
                )
            )

            playSound()

        }
    }

    private fun copyToClipboard(answer: String) {
        val selection = StringSelection(answer)
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        clipboard.setContents(selection, selection)
    }


    private fun playSound() {
        Tasks.runAsyncTask({

            for (i in 0 until 10) {
                delay(100)
                Minecraft.getMinecraft().soundHandler.playSound(
                    PositionedSoundRecord.create(
                        ResourceLocation("note.harp")
                    )
                )
            }

        })
    }

}