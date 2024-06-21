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

neoForge {
    version = neoforged.versions.neoforge

    this.mods.create(modId) {
        sourceSet(sourceSets.main.get())
        sourceSet(coreApi.sourceSets.main.get())
        sourceSet(mainProject.sourceSets.main.get())
    }

    this.runs {
        configureEach {
            logLevel.set(Level.DEBUG)
        }

        create("data") {
            data()

            programArguments.addAll("--mod", modId)
            programArguments.addAll("--all")
            programArguments.addAll("--output", mainProject.file("src/generated/resources").absolutePath)
            programArguments.addAll("--existing", mainProject.file("src/main/resources").absolutePath)
        }
    }
}

repositories {
    mavenLocal()
}

dependencies {
    implementation(coreApi)
    implementation(mainProject)
}

tasks.compileJava {
    options.encoding = "UTF-8";
}

tasks.withType<ProcessResources> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}