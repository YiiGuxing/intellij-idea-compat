import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java") // Java support
    alias(libs.plugins.kotlin) // Kotlin support
    alias(libs.plugins.gradleIntelliJPlugin) // Gradle IntelliJ Plugin
}

// Set the JVM language level used to build the project. Use Java 11 for 2020.3+, and Java 17 for 2022.2+.
kotlin {
    jvmToolchain(17)
}

java {
    withSourcesJar()
    targetCompatibility = JavaVersion.VERSION_11
}

intellij {
    version.set("2022.3")
    type.set("IC")
    instrumentCode.set(false)
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }
    verifyPluginConfiguration {
        enabled = false
    }
    buildSearchableOptions {
        enabled = false
    }
}