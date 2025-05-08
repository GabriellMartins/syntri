pluginManagement {
    plugins {
        kotlin("jvm") version "2.1.20"
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "Syntri"

include("commons")
include("version-api")
include("version-1_8_9")
include("version-1_20_4")
include("syntri")
include("syntri-engine")
