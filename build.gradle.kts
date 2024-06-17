plugins {
    id("org.jetbrains.gradle.plugin.idea-ext") version ("1.1.7")

//    alias(neoforged.plugins.common)
    alias(neoforged.plugins.moddev).apply(false)
}

tasks.create("mcVersion") {
    doFirst {
        val mc = mojang.versions.minecraft.get()
        println("version=$mc")
    }
}

tasks.create("neoVersion") {
    doFirst {
        val neo = neoforged.versions.neoforge.get()
        println("version=$neo")
    }
}

subprojects {
    repositories {
        mavenLocal()
    }
}