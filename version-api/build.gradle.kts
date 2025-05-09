plugins {
    kotlin("jvm") version "1.9.10"
    java
}

group = "com.br.gabrielmartins.version"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}
