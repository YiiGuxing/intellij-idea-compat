fun properties(key: String) = providers.gradleProperty(key)

plugins {
    id("java-library")
    id("maven-publish")
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
    implementation(project(":common"))
    implementation(project(":compat"))
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_11
}

publishing {
    publications {
        create<MavenPublication>("IntellijCompat") {
            group = "com.github.YiiGuxing"
            from(components["java"])

            pom {
                name.set("IntelliJ IDEA Compat")
                description.set("IntelliJ IDEA plugin development compatibility support library")
                url.set("https://github.com/YiiGuxing/intellij-idea-compat")
                developers {
                    developer {
                        id.set("yiiguxing")
                        name.set("Yii.Guxing")
                        email.set("yii.guxing@outlook.com")
                    }
                }
                withXml {
                    (asNode()["dependencies"] as groovy.util.NodeList).forEach { dependencies ->
                        dependencies as groovy.util.Node
                        dependencies.children().reversed().forEach {
                            dependencies.remove(it as groovy.util.Node)
                        }
                    }
                }
            }
        }
    }
}

tasks {
    jar {
        from(zipTree(project(":common").tasks.jar.get().archiveFile.get()))
        from(zipTree(project(":compat").tasks.jar.get().archiveFile.get()))
        from(zipTree(project(":compat-213").tasks.jar.get().archiveFile.get()))
    }
    named<Jar>("sourcesJar") {
        from(project(":common").sourceSets.main.get().allSource)
        from(project(":compat").sourceSets.main.get().allSource)
        from(project(":compat-213").sourceSets.main.get().allSource)
    }

    withType<GenerateModuleMetadata> {
        enabled = false
    }

    wrapper {
        gradleVersion = properties("gradleVersion").get()
        distributionType = Wrapper.DistributionType.ALL
    }
}

