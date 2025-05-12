    plugins {
        java
        kotlin("jvm") version "1.9.10"
        id("com.github.johnrengelman.shadow") version "8.1.1"
        id("net.minecrell.plugin-yml.bukkit") version "0.3.0"
        id("checkstyle")
    }

    group = "com.br.gabrielmartins.syntri"
    version = "1.0.0"

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    tasks.compileJava {
        options.release.set(17)
        options.encoding = "UTF-8"
    }

    repositories {
        mavenCentral()
        mavenLocal()

        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
        maven("https://repo.md-5.net/content/repositories/snapshots/")
    }


    dependencies {
        compileOnly("me.clip:placeholderapi:2.11.5")

        compileOnly(files("../libs/Vault.jar"))
        implementation(kotlin("stdlib-jdk8"))
        implementation(project(":version-api"))
        implementation(project(":version-1_8_9"))
        implementation(project(":version-1_20_4"))
        implementation(project(":syntri-engine"))
        implementation("org.reflections:reflections:0.10.2")

        api("org.mongodb:mongodb-driver-sync:4.11.0")

        compileOnly("org.spigotmc:spigot-api:1.21.5-R0.1-SNAPSHOT") {
            isTransitive = false
        }
        compileOnly(files("../libs/BungeeCord.jar"))

        implementation("org.json:json:20210307")
        compileOnly("org.projectlombok:lombok:1.18.30")
        annotationProcessor("org.projectlombok:lombok:1.18.30")

        testImplementation(platform("org.junit:junit-bom:5.10.0"))
        testImplementation("org.junit.jupiter:junit-jupiter")
    }

    bukkit {
        name = "Syntri"
        version = "1.0.0"
        main = "com.br.gabrielmartins.syntri.SyntriPlugin"
        authors = listOf("shaw")
        description = "Plugin modular com suporte a múltiplos bancos e integração PlaceholderAPI"
        softDepend =
            listOf(
            "PlaceholderAPI",
            "Vault")
    }

    checkstyle {
        configFile = rootProject.file("config/checkstyle/checkstyle.xml")
        isIgnoreFailures = true
    }

    tasks.named("checkstyleMain") {
        enabled = false
    }

    tasks {
        build {
            dependsOn(shadowJar)
        }

        withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
            archiveClassifier.set("")
            destinationDirectory.set(file("$buildDir/libs"))
        }
    }

    tasks.processResources {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from("src/main/resources") {
            include("modules/**")
        }
    }


    tasks.build {
        dependsOn(tasks.shadowJar)
    }

    tasks.test {
        useJUnitPlatform()
    }

    kotlin {
        jvmToolchain(17)
    }
