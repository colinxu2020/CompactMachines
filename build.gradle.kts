plugins {
    id("org.jetbrains.gradle.plugin.idea-ext") version ("1.1.7")

//    alias(neoforged.plugins.common)
    alias(neoforged.plugins.userdev).apply(false)
}

subprojects {
    afterEvaluate {
        extensions.configure<JavaPluginExtension> {
            // toolchain.vendor.set(JvmVendorSpec)
            toolchain.languageVersion.set(JavaLanguageVersion.of(21))
        }
    }
}

