import org.slf4j.event.Level

plugins {
    id("java")
    id("eclipse")
    id("idea")
    id("maven-publish")
    alias(neoforged.plugins.neogradle)
}

val modId: String = "compactmachines"

val coreApi = project(":core-api")
val mainProject: Project = project(":neoforge-main")

project.evaluationDependsOn(coreApi.path)
project.evaluationDependsOn(mainProject.path)

java {
    toolchain.vendor.set(JvmVendorSpec.JETBRAINS)
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

minecraft {
    this.modIdentifier = modId
}

runs {
    configureEach {
        this.modSource(coreApi.sourceSets.main.get())
        this.modSource(sourceSets.main.get())
        this.modSource(mainProject.sourceSets.main.get())

        dependencies {
            runtime(libraries.feather)
            runtime(libraries.jnanoid)
        }
    }

    this.create("data") {
        this.dataGenerator()

        this.workingDirectory.set(file("runs/data"))

        // Comma-separated list of namespaces to load gametests from. Empty = all namespaces.
        systemProperty("forge.enabledGameTestNamespaces", modId)

        arguments.addAll("--mod", modId)
        arguments.addAll("--all")
        arguments.addAll("--output", mainProject.file("src/generated/resources").absolutePath)
        arguments.addAll("--existing", mainProject.file("src/main/resources").absolutePath)
    }
}

repositories {
    mavenLocal()

    maven("https://maven.pkg.github.com/compactmods/feather") {
        name = "Github PKG Core"
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
            password = project.findProperty("gpr.token") as String? ?: System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
    compileOnly(coreApi)
    compileOnly(mainProject)

    compileOnly(neoforged.neoforge)
}

tasks.compileJava {
    options.encoding = "UTF-8";
}

tasks.withType<ProcessResources> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}