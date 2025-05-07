plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("net.minecrell.plugin-yml.bukkit") version "0.3.0"
    id("io.freefair.lombok") version "8.4"
    id("checkstyle")
    id("com.github.spotbugs") version "5.0.13"
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

repositories {
    mavenCentral()
    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    compileOnly(files("libs/place.jar"))
    compileOnly("org.jetbrains:annotations:24.0.1")

    implementation("org.json:json:20210307")
    implementation("com.zaxxer:HikariCP:4.0.3")
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("org.mongodb:mongodb-driver-sync:4.11.0")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.3.1")
    implementation("com.oracle.database.jdbc:ojdbc11:23.2.0.0")
    implementation("org.firebirdsql.jdbc:jaybird:4.0.5.java11")
    implementation("com.microsoft.sqlserver:mssql-jdbc:12.6.1.jre11")
    implementation("org.xerial:sqlite-jdbc:3.45.2.0")
    implementation("com.mysql:mysql-connector-j:8.4.0")
    implementation("org.reflections:reflections:0.10.2")

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
