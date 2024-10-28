plugins {
    id("org.jetbrains.gradle.plugin.idea-ext") version ("1.1.7")

//    alias(neoforged.plugins.common)
    alias(neoforged.plugins.moddev).apply(false)
}

tasks.register("ngInitCM") {
    doLast {
        println("ForgeGradle+CM initialized")
    }
}

tasks.register("mcVersion") {
    doLast {
        val mc = mojang.versions.minecraft.get()
        println("version=$mc")
    }
}

tasks.register("neoVersion") {
    doLast {
        val neo = neoforged.versions.neoforge.get()
        println("version=$neo")
    }
}

subprojects {
    repositories {
        mavenLocal()
    }
}