import org.slf4j.event.Level

plugins {
    id("java")
    id("eclipse")
    id("idea")
    id("maven-publish")
    alias(neoforged.plugins.userdev)
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
    modIdentifier.set(modId)
}

runs {
    // applies to all the run configs below
    configureEach {
        systemProperty("forge.logging.markers", "") // 'SCAN,REGISTRIES,REGISTRYDUMP'

        // Recommended logging level for the console
        systemProperty("forge.logging.console.level", "debug")

        modSource(project.sourceSets.main.get())
        modSource(mainProject.sourceSets.main.get())

        modSource(coreApi.sourceSets.main.get())
    }

    create("data") {
        dataGenerator(true)

        programArguments("--mod", "compactmachines")
        programArguments("--all")
        programArguments("--output", mainProject.file("src/generated/resources").absolutePath)
        programArguments("--existing", mainProject.file("src/main/resources").absolutePath)
    }
}

repositories {
    mavenLocal()
}

dependencies {
    compileOnly(coreApi)
    compileOnly(mainProject)

    implementation(neoforged.neoforge)
}

tasks.compileJava {
    options.encoding = "UTF-8";
}

tasks.withType<ProcessResources> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}