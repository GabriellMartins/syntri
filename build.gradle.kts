plugins {
    java
}

group = "com.br.gabrielmartins"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}


tasks.compileJava {
    options.release.set(8)
    options.encoding = "UTF-8"
}

allprojects {
    repositories {
        mavenCentral()
        mavenLocal()

        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        maven("https://repo.md-5.net/content/repositories/snapshots/")
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}
