@file:Suppress("SpellCheckingInspection")

import java.text.SimpleDateFormat
import java.util.*

var envVersion: String = System.getenv("VERSION") ?: "9.9.9"
if (envVersion.startsWith("v"))
    envVersion = envVersion.trimStart('v')

val modId: String = "compactmachines"
val isRelease: Boolean = (System.getenv("RELEASE") ?: "false").equals("true", true)

val coreApi = project(":core-api")

plugins {
    java
    id("idea")
    id("eclipse")
    id("maven-publish")
    alias(neoforged.plugins.moddev)
}

project.evaluationDependsOn(coreApi.path)

base {
    archivesName.set(modId)
    group = "dev.compactmods.compactmachines"
    version = envVersion
}

java {
    // toolchain.vendor.set(JvmVendorSpec.JETBRAINS)
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

sourceSets.main {
    java {
        srcDir("src/main/java")
    }

    resources {
        srcDir("src/main/resources")
        srcDir("src/generated/resources")
    }
}

sourceSets.test {
    java {
        srcDir("src/test/java")
    }

    resources {
        srcDir("src/test/resources")
    }
}

minecraft {
    this.modIdentifier = modId
    this.accessTransformers {
        this.file(project.file("src/main/resources/META-INF/accesstransformer.cfg"))
    }
}

runs {
    configureEach {
        this.modSource(coreApi.sourceSets.main.get())

        dependencies {
            runtime(libraries.feather)
            runtime(libraries.jnanoid)
        }

//            if (!System.getenv().containsKey("CI")) {
//                // JetBrains Runtime Hotswap
//                // jvmArgument("-XX:+AllowEnhancedClassRedefinition")
//            }
    }

    this.create("client") {
        client()

        this.workingDirectory.set(file("runs/client"))

        // Comma-separated list of namespaces to load gametests from. Empty = all namespaces.
        systemProperty("forge.enabledGameTestNamespaces", modId)

        programArguments.addAll("--username", "Nano")
        programArguments.addAll("--width", "1920")
        programArguments.addAll("--height", "1080")
    }

    create("server") {
        server()
        workingDirectory.set(file("runs/server"))

        systemProperty("forge.enabledGameTestNamespaces", modId)
        programArgument("nogui")

        environmentVariable("CM_TEST_RESOURCES", file("src/test/resources").path)

        sourceSets.add(project.sourceSets.test.get())
    }

    create("gameTestServer") {
        gameTest()
        workingDirectory.set(file("runs/gametest"))

        systemProperty("forge.enabledGameTestNamespaces", modId)
        environmentVariable("CM_TEST_RESOURCES", file("src/test/resources").path)

        sourceSets.add(project.sourceSets.test.get())
    }
}

repositories {
    mavenLocal()
    mavenCentral {
        name = "Central"
        content {
            includeGroup("com.aventrix.jnanoid")
        }
    }

    maven("https://maven.pkg.github.com/compactmods/feather") {
        name = "Github PKG Core"
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
            password = project.findProperty("gpr.token") as String? ?: System.getenv("GITHUB_TOKEN")
        }

        content {
            includeGroup("dev.compactmods")
        }
    }

    maven("https://maven.blamejared.com/") {
        // location of the maven that hosts JEI files since January 2023
        name = "Jared's maven"
    }

    maven("https://www.cursemaven.com") {
        content {
            includeGroup("curse.maven")
        }
    }

    maven("https://modmaven.dev") {
        // location of a maven mirror for JEI files, as a fallback
        name = "ModMaven"
    }
}

dependencies {
    // Core Projects and Libraries
    this {
        compileOnly(libraries.jnanoid)
        jarJar(libraries.jnanoid)

        compileOnly(coreApi)
        testCompileOnly(coreApi)
        jarJar(coreApi)

        compileOnly(libraries.feather)
        jarJar(libraries.feather) { isTransitive = false }
    }

    implementation(neoforged.neoforge)

    runtimeOnly(neoforged.testframework)
    testImplementation(neoforged.testframework)
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")


    // Mods
//    compileOnly(mods.bundles.jei)
//    compileOnly(mods.jade)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<ProcessResources> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(arrayOf("-Xmaxerrs", "9000"))
}

tasks.withType<Jar> {

    val gitVersion = providers.exec {
        commandLine("git", "rev-parse", "HEAD")
    }.standardOutput.asText.get()

    manifest {
        val now = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(Date())
        attributes(
            mapOf(
                "Specification-Title" to "Compact Machines",
                "Specification-Vendor" to "CompactMods",
                "Specification-Version" to "2",
                "Implementation-Title" to "Compact Machines",
                "Implementation-Version" to archiveVersion,
                "Implementation-Vendor" to "CompactMods",
                "Implementation-Timestamp" to now,
                "Minecraft-Version" to mojang.versions.minecraft.get(),
                "NeoForge-Version" to neoforged.versions.neoforge.get(),
                "Main-Commit" to gitVersion
            )
        )
    }
}

tasks.withType<ProcessResources>().configureEach {
    var replaceProperties: Map<String, Any> = mapOf(
        "minecraft_version" to mojang.versions.minecraft.get(),
        "neo_version" to neoforged.versions.neoforge.get(),
        "minecraft_version_range" to mojang.versions.minecraftRange.get(),
        "neo_version_range" to neoforged.versions.neoforgeRange.get(),
        "loader_version_range" to "[1,)",
        "mod_id" to modId,
        "mod_version" to envVersion
    )

    inputs.properties(replaceProperties)
    filesMatching("META-INF/neoforge.mods.toml") {
        expand(replaceProperties)
    }
}

val PACKAGES_URL = System.getenv("GH_PKG_URL") ?: "https://maven.pkg.github.com/compactmods/compactmachines"
publishing {
    publications.register<MavenPublication>("compactmachines") {
        artifactId = "$modId-neoforge"
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