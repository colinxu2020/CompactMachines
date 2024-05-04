import java.text.SimpleDateFormat
import java.util.*

val versionMain: String = System.getenv("VERSION") ?: "0.0.0"

var coreApi: Project;
if (rootProject.name == "Compact Machines Core")
    coreApi = project(":core-api")
else
    coreApi = project(":core:core-api")

plugins {
    id("java-library")
    id("maven-publish")
    alias(neoforged.plugins.vanilla)
}

base {
    group = "dev.compactmods.compactmachines"
    version = versionMain
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    withJavadocJar()
}

dependencies {
    compileOnly(coreApi)
    api(mojang.minecraft)
}

tasks.withType<Jar> {
    manifest {
        val now = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(Date())
        attributes(
            mapOf(
                "Specification-Title" to "Compact Machines - Rooms API",
                "Specification-Version" to "1", // We are version 1 of ourselves
                "Implementation-Title" to "Compact Machines - Rooms API",
                "Implementation-Timestamp" to now,
                "FMLModType" to "GAMELIBRARY"
            )
        )
    }
}

val PACKAGES_URL = System.getenv("GH_PKG_URL") ?: "https://maven.pkg.github.com/compactmods/compactmachines-core"
publishing {
    publications.register<MavenPublication>("room") {
        from(components.getByName("java"))
//        artifact(tasks.named("remapSrgJar"))
    }

    repositories {
        // GitHub Packages
        maven(PACKAGES_URL) {
            name = "GitHubPackages"
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}