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
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    compileOnly(files("C:\\Users\\GabrielMartins\\Desktop\\server\\build\\plugins\\place.jar"))
    compileOnly(files("libs/Vault.jar"))

    implementation(kotlin("stdlib-jdk8"))
    compileOnly(project(":version-api"))
    compileOnly(project(":version-1_8_9"))
    compileOnly(project(":version-1_20_4"))
    compileOnly(project(":syntri-engine"))

    api("org.mongodb:mongodb-driver-sync:4.11.0")
    compileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")

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
    softDepend = listOf("PlaceholderAPI")
}

checkstyle {
    toolVersion = "10.12.2"
    configFile = file("config/checkstyle/checkstyle.xml")
    isIgnoreFailures = true
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    archiveClassifier.set("")
    mergeServiceFiles()

    minimize {
        exclude(dependency("org.reflections:reflections"))
        exclude(dependency("org.json:json"))
        exclude(dependency("com.zaxxer:HikariCP"))
        exclude(dependency("org.jetbrains:annotations"))
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
