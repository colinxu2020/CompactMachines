
import java.text.SimpleDateFormat
import java.util.*

val versionMain: String = System.getenv("VERSION") ?: "0.0.0"

var coreApi: Project = project(":core-api")
var roomApi: Project = project(":room-api")

plugins {
    id("java-library")
    id("maven-publish")
    alias(neoforged.plugins.userdev)
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
    compileOnly(roomApi)
    // api(libraries.neoforge)
}

tasks.withType<Jar> {
    manifest {
        val now = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(Date())
        attributes(mapOf(
                "Specification-Title" to "Compact Machines - Room Upgrades API",
                "Specification-Version" to "1", // We are version 1 of ourselves
                "Implementation-Title" to "Compact Machines - Room Upgrades API",
                "Implementation-Timestamp" to now,
                "FMLModType" to "GAMELIBRARY"
        ))
    }
}

val PACKAGES_URL = System.getenv("GH_PKG_URL") ?: "https://maven.pkg.github.com/compactmods/compactmachines-core"
publishing {
    publications.register<MavenPublication>("room-upgrade") {
        from(components.getByName("java"))
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