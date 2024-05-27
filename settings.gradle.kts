rootProject.name = "Compact Machines 20.6"

dependencyResolutionManagement {
    versionCatalogs.create("neoforged") {
        version("neogradle", "7.0.135")
        version("neoforge", "20.6.84-beta")

        plugin("userdev", "net.neoforged.gradle.userdev")
            .versionRef("neogradle")

        library("neoforge", "net.neoforged", "neoforge")
            .versionRef("neoforge")
    }

    versionCatalogs.create("mojang") {
        version("minecraft", "1.20.6")
    }

    versionCatalogs.create("libraries") {
        library("feather", "dev.compactmods", "feather")
                .versionRef("feather")

        library("jnanoid", "com.aventrix.jnanoid", "jnanoid")
                .versionRef("jnanoid")

        version("feather", "[0.1.8, 2.0)")
        version("jnanoid", "[2.0.0, 3)")

        version("parchment-mc", "1.20.6")
        version("parchment", "2024.05.01")
    }

    versionCatalogs.create("mods") {
        this.library("jei-common", "mezz.jei", "jei-1.20.4-common-api").versionRef("jei")
        this.library("jei-neo", "mezz.jei", "jei-1.20.4-neoforge-api").versionRef("jei");
        this.bundle("jei", listOf("jei-common", "jei-neo"))
        this.version("jei", "17.3.0.49")

        this.library("jade", "curse.maven", "jade-324717").version("5109393")
    }
}

pluginManagement {
    plugins {
        id("idea")
        id("eclipse")
        id("maven-publish")
    }

    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()

        // maven("https://maven.architectury.dev/")

//        maven("https://maven.parchmentmc.org") {
//            name = "ParchmentMC"
//        }

        maven("https://maven.neoforged.net/releases") {
            name = "NeoForged"
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version ("0.8.0")
}

include(":core-api")
include(":room-api")

include("neoforge-main")
include("neoforge-datagen")

