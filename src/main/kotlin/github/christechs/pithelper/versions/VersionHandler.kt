package github.christechs.pithelper.versions

import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.PropertyType
import gg.essential.vigilance.data.fullPropertyPath
import gg.essential.vigilance.impl.nightconfig.core.file.FileConfig
import github.christechs.pithelper.Constants
import github.christechs.pithelper.config.PitHelperConfig
import github.christechs.pithelper.versions.previous.ConfigV0d0d1
import github.christechs.pithelper.versions.previous.ConfigV0d0d2
import net.minecraft.launchwrapper.Launch
import java.awt.Color

object VersionHandler {

    private val configFile = Launch.minecraftHome
        .resolve("config/ChrisTechs/PitHelper/config.toml")

    fun updateConfig() {

        if (!configFile.exists()) {
            PitHelperInfo.info.version = Constants.version
        }

        when (PitHelperInfo.info.version) {

            Constants.version -> PitHelperConfig.initialize()

            "unknown" -> update0d0d1()

            "0.0.2" -> update0d0d2()

        }

        PitHelperInfo.info.version = Constants.version

        PitHelperInfo.save()

    }

    private fun update0d0d1() {

        val oldConfig = ConfigV0d0d1()

        val valid = testData(oldConfig)

        resetFiles()
        PitHelperConfig.initialize()

        if (valid) {
            PitHelperConfig.enabled = oldConfig.enabled
            PitHelperConfig.hypixelOnly = oldConfig.hypixelOnly
            PitHelperConfig.quickMaths = oldConfig.quickMaths
            PitHelperConfig.eventsNotifications = oldConfig.eventsNotifications
            PitHelperConfig.quickMathsClipboard = oldConfig.quickMathsClipboard
            PitHelperConfig.quickMathsAutoAnswer = oldConfig.quickMathsAutoAnswer
            PitHelperConfig.majorEventsNotifications = oldConfig.majorEventsNotifications
            PitHelperConfig.minorEventsNotifications = oldConfig.minorEventsNotifications
        }

    }

    private fun update0d0d2() {

        val oldConfig = ConfigV0d0d2()

        val valid = testData(oldConfig)

        resetFiles()
        PitHelperConfig.initialize()

        if (valid) {
            PitHelperConfig.enabled = oldConfig.enabled
            PitHelperConfig.hypixelOnly = oldConfig.hypixelOnly
            PitHelperConfig.quickMaths = oldConfig.quickMaths
            PitHelperConfig.eventsNotifications = oldConfig.eventsNotifications
            PitHelperConfig.quickMathsClipboard = oldConfig.quickMathsClipboard
            PitHelperConfig.quickMathsAutoAnswer = oldConfig.quickMathsAutoAnswer
            PitHelperConfig.majorEventsNotifications = oldConfig.majorEventsNotifications
            PitHelperConfig.minorEventsNotifications = oldConfig.minorEventsNotifications
        }

    }

    private val propertyCollector = PitHelperPropertyCollector()

    private val fileConfig = FileConfig.of(configFile)
        .also { it.load() }

    private fun testData(vigilant: Vigilant): Boolean {

        try {
            try {
                propertyCollector.clear()
                propertyCollector.initialize(vigilant)
            } catch (_: Exception) {
            }

            propertyCollector.getProperties().filter { it.value.writeDataToFile }.forEach {
                val fullPath = it.attributesExt.fullPropertyPath()

                var oldValue: Any? = fileConfig.get(fullPath)

                if (it.attributesExt.type == PropertyType.COLOR) {
                    oldValue = if (oldValue is String) {
                        val split = oldValue.split(",").map(String::toInt)
                        if (split.size == 4) Color(split[1], split[2], split[3], split[0]) else null
                    } else {
                        null
                    }
                }

                it.setValue(oldValue ?: it.getAsAny())
            }

            return true

        } catch (_: Exception) {
            return false
        }

    }

    private fun resetFiles() {
        configFile.mkdirs()
        configFile.delete()
        configFile.createNewFile()
    }

}