import org.slf4j.event.Level

plugins {
    id("java")
    id("eclipse")
    id("idea")
    id("maven-publish")
    alias(neoforged.plugins.moddev)
}

val modId: String = "compactmachines"

val coreApi = project(":core-api")
val mainProject: Project = project(":neoforge-main")

project.evaluationDependsOn(coreApi.path)
project.evaluationDependsOn(mainProject.path)

java {
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

        programArguments.addAll("--mod", modId)
        programArguments.addAll("--all")
        programArguments.addAll("--output", mainProject.file("src/generated/resources").absolutePath)
        programArguments.addAll("--existing", mainProject.file("src/main/resources").absolutePath)
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

    // https://github.com/neoforged/NeoForge/pull/1303
    maven("https://prmaven.neoforged.net/NeoForge/pr1303") {
        content {
            includeModule("net.neoforged", "neoforge")
        }
    }
}

dependencies {
    implementation("net.neoforged:neoforge:21.0.159-pr-1303-feat-recipe-provider-lookup")
    compileOnly(coreApi)
    compileOnly(mainProject)
}

tasks.compileJava {
    options.encoding = "UTF-8";
}

tasks.withType<ProcessResources> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}