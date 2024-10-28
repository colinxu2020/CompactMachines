rootProject.name = "Compact Machines"

pluginManagement {
    plugins {
        id("idea")
        id("eclipse")
        id("maven-publish")
    }

    repositories {
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()

        maven("https://maven.minecraftforge.net")
        maven("https://maven.parchmentmc.org")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
