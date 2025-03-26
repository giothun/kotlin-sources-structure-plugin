plugins {
    kotlin("jvm") version "1.9.21"
    kotlin("plugin.serialization") version "1.9.21"
    id("java-gradle-plugin")
    id("maven-publish")
}

group = "io.github.giothun"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(gradleApi())
    implementation(kotlin("gradle-plugin-api"))
    implementation(kotlin("gradle-plugin"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    
    testImplementation(kotlin("test"))
    testImplementation(gradleTestKit())
}

tasks.withType<ProcessResources> {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

gradlePlugin {
    plugins {
        create("kotlinSourcesStructurePlugin") {
            id = "io.github.giothun.kotlin-sources-structure"
            implementationClass = "KotlinSourceStructurePlugin"
        }
    }
}

kotlin {
    jvmToolchain(17)
}

tasks.test {
    useJUnitPlatform()
} 