plugins {
    java
    kotlin("jvm") version "1.9.10"
}

group = "com.br.gabrielmartins.engione"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    mavenLocal()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.reflections:reflections:0.10.2")
    compileOnly(files("C:\\Users\\GabrielMartins\\Desktop\\server\\build\\plugins\\place.jar"))

    implementation(project(":version-api"))
    implementation(project(":version-1_8_9"))
    implementation(project(":version-1_20_4"))

    compileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")

    implementation("org.json:json:20210307")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    implementation("com.zaxxer:HikariCP:4.0.3")
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("org.mongodb:mongodb-driver-sync:4.11.0")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.3.1")
    implementation("com.oracle.database.jdbc:ojdbc11:23.2.0.0")
    implementation("org.firebirdsql.jdbc:jaybird:4.0.5.java11")
    implementation("com.microsoft.sqlserver:mssql-jdbc:12.6.1.jre11")
    implementation("org.xerial:sqlite-jdbc:3.45.2.0")
    implementation("com.mysql:mysql-connector-j:8.4.0")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

kotlin {
    jvmToolchain(17)
}
