plugins {
    id("java")
    id("eclipse")
    id("idea")
    id("maven-publish")
    alias(neoforged.plugins.userdev)
}

val mod_id: String by extra
val mainProject: Project = project(":neoforge-main")
evaluationDependsOn(mainProject.path)

val coreApi = project(":core-api")
val roomApi = project(":room-api")
val roomUpgradeApi = project(":room-upgrade-api")

val coreProjects = listOf(coreApi, roomApi, roomUpgradeApi)

coreProjects.forEach {
    project.evaluationDependsOn(it.path)
}

base {
    group = "dev.compactmods.compactmachines"
    archivesName.set(mod_id)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

minecraft {
    modIdentifier.set(mod_id)
}

runs {
    // applies to all the run configs below
    configureEach {
        systemProperty("forge.logging.markers", "") // 'SCAN,REGISTRIES,REGISTRYDUMP'

        // Recommended logging level for the console
        systemProperty("forge.logging.console.level", "debug")

        modSource(project.sourceSets.main.get())
        modSource(mainProject.sourceSets.main.get())

        coreProjects.forEach { modSource(it.sourceSets.main.get()) }
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

    maven("https://maven.pkg.github.com/compactmods/compactmachines-core") {
        name = "Github PKG Core"
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
            password = project.findProperty("gpr.token") as String? ?: System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
    implementation(neoforged.neoforge)
    compileOnly(mainProject)
    coreProjects.forEach {
        compileOnly(it)
    }
}

tasks.compileJava {
    options.encoding = "UTF-8";
}

tasks.withType<ProcessResources> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}