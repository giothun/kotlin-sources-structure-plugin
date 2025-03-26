# Kotlin Sources Structure Gradle Plugin

A Gradle plugin that extracts and outputs the Kotlin source structure of a project to a JSON file.

## Features

- Detects if the Kotlin Gradle Plugin is applied to the project
- Extracts information about Kotlin source sets, including:
  - Source set names
  - Source directories
  - Kotlin files
- Outputs the structure as a JSON file

## Requirements

- Gradle 7.0+
- Java 11+
- Kotlin Gradle Plugin (for meaningful structure extraction)

## Setup

### Adding the Plugin

In your `build.gradle.kts` file:

```kotlin
plugins {
    id("io.github.giothun.kotlin-sources-structure") version "1.0-SNAPSHOT"
}
```

Or in your `build.gradle` file:

```groovy
plugins {
    id 'io.github.giothun.kotlin-sources-structure' version '1.0-SNAPSHOT'
}
```

### Running the Task

```bash
./gradlew generateKotlinSourcesStructure
```

### Output

The task generates a JSON file at:

```
build/reports/kotlin-sources-structure.json
```

Example output:

```json
[
  {
    "sourceSetName": "main",
    "sourceDirectories": [
      "src/main/kotlin"
    ],
    "files": [
      "src/main/kotlin/com/example/Main.kt",
      "src/main/kotlin/com/example/Helper.kt"
    ]
  },
  {
    "sourceSetName": "test",
    "sourceDirectories": [
      "src/test/kotlin"
    ],
    "files": [
      "src/test/kotlin/com/example/MainTest.kt"
    ]
  }
]
```

## Using in Your Build Scripts

You can also use the task output in your own build scripts:

```kotlin
tasks.register("analyzeKotlinSources") {
    dependsOn("generateKotlinSourcesStructure")
    doLast {
        val structureFile = project.layout.buildDirectory.file("reports/kotlin-sources-structure.json").get().asFile
        if (structureFile.exists()) {
            val content = structureFile.readText()
            println("Kotlin structure available at: ${structureFile.absolutePath}")
        }
    }
}
```

## Building the Plugin

To build the plugin locally:

```bash
./gradlew build
```

To publish to Maven Local:

```bash
./gradlew publishToMavenLocal
```
