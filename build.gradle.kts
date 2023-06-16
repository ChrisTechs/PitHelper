import gg.essential.gradle.util.noServerRunConfigs
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.8.0"
    id("gg.essential.multi-version")
    id("gg.essential.defaults")
}

val modGroup: String by project
val modBaseName: String by project
group = modGroup
base.archivesName.set("$modBaseName-${platform.mcVersionStr}")

loom {
    noServerRunConfigs()
    launchConfigs {
        getByName("client") {
            arg("--tweakClass", "gg.essential.loader.stage0.EssentialSetupTweaker")
        }
    }
}

val embed by configurations.creating
configurations.implementation.get().extendsFrom(embed)

dependencies {
    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    compileOnly("gg.essential:essential-$platform:12132+g6e2bf4dc5")
    embed("gg.essential:loader-launchwrapper:1.1.3")
}

tasks.jar {
    from(embed.files.map { zipTree(it) })

    manifest.attributes(
            mapOf(
                    "ModSide" to "CLIENT",
                    "FMLCorePluginContainsFMLMod" to "Yes, yes it does",
                    "TweakClass" to "gg.essential.loader.stage0.EssentialSetupTweaker",
                    "TweakOrder" to "0"
            )
    )
}