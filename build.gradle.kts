fun properties(key: String) = providers.gradleProperty(key)

plugins {
    id("java-library")
}

allprojects {
    group = properties("compatGroup").get()
    version = properties("compatVersion").get()

    repositories {
        mavenLocal()
        maven(url = "https://maven.aliyun.com/repository/public")
        maven(url = "https://maven-central.storage-download.googleapis.com/repos/central/data/")
        maven(url = "https://www.jetbrains.com/intellij-repository/releases")
        mavenCentral()
    }
}

dependencies {
    api(project(":common"))
    api(project(":compat"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_11
}

tasks {
    jar {
        from(zipTree(project(":common").tasks.jar.get().archiveFile.get()))
        from(zipTree(project(":compat").tasks.jar.get().archiveFile.get()))
        from(zipTree(project(":compat-213").tasks.jar.get().archiveFile.get()))
    }
}

