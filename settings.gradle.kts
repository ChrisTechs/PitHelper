pluginManagement {
    repositories {

        mavenLocal()
        gradlePluginPortal()
        mavenCentral()

        maven("https://maven.unifycraft.xyz/releases")
        maven("https://maven.fabricmc.net")
        maven("https://maven.architectury.dev/")
        maven("https://maven.minecraftforge.net")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://repo.essential.gg/repository/maven-public")

        maven("https://jitpack.io/")

    }
}

rootProject.name = "PitHelper"
