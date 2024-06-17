plugins {
    id("java")
    id("eclipse")
    id("idea")
    id("maven-publish")
    alias(neoforged.plugins.moddev)
}

val modId: String = "compactmachines"
val mainProject: Project = project(":neoforge-main")
evaluationDependsOn(mainProject.path)

val coreApi = project(":core-api")

project.evaluationDependsOn(coreApi.path)

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

neoForge {
    version = neoforged.versions.neoforge

    this.mods.create(modId) {
        modSourceSets.add(sourceSets.main)
        this.dependency(coreApi)
        this.dependency(mainProject)
    }

    this.runs {
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
    compileOnly(coreApi)
    compileOnly(mainProject)
}

tasks.compileJava {
    options.encoding = "UTF-8";
}

tasks.withType<ProcessResources> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}