package github.christechs.pithelper.versions

import gg.essential.api.EssentialAPI
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.minecraft.launchwrapper.Launch

object PitHelperInfo {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    private val infoFile = Launch.minecraftHome
        .resolve("config/ChrisTechs/PitHelper/info.json")

    var info: Info = Info()
        private set

    fun initialize() {

        checkExists()

        info = try {
            json.decodeFromString(infoFile.bufferedReader().readText())
        }catch (e: Exception) {
            Info()
        }

        EssentialAPI.getShutdownHookUtil().register {

            checkExists()

            save()

        }

    }

    private fun checkExists() {
        if (!infoFile.exists()) {
            infoFile.parentFile.mkdirs()
            infoFile.createNewFile()
        }
    }

    fun save() {
        try {
            val writer = infoFile.bufferedWriter()
            writer.write(json.encodeToString(info))
            writer.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Serializable
    data class Info(
        var version: String = "unknown",
    )

}