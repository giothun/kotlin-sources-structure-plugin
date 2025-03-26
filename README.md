# Kotlin Sources Structure Gradle Plugin

A Gradle plugin that extracts and outputs the Kotlin source structure of your project into a structured JSON file.

## Features

- Automatically detects the presence of the Kotlin Gradle plugins:
  - `org.jetbrains.kotlin.jvm`
  - `org.jetbrains.kotlin.android`
  - `org.jetbrains.kotlin.multiplatform`
- Extracts Kotlin source sets information, including:
  - Source set names
  - Kotlin source directories
  - Individual Kotlin file paths
- Generates a structured, human-readable JSON report.

**Note:**  
The task is registered only if one of the supported Kotlin plugins listed above is applied. If no Kotlin plugin is detected, the task will **not** be available.

## Setup

### Adding the Plugin

#### Kotlin DSL (`build.gradle.kts`):

```kotlin
plugins {
    id("io.github.giothun.kotlin-sources-structure") version "1.0"
}
```

#### Groovy DSL (`build.gradle`):

```groovy
plugins {
    id 'io.github.giothun.kotlin-sources-structure' version '1.0'
}
```

## Running the Task

Execute the following command to generate the Kotlin source structure:

```bash
./gradlew generateKotlinSourcesStructure
```

## Task Output

By default, the JSON report is generated at:

```
build/reports/kotlin-sources-structure.json
```

### JSON Output Example:

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

**Explanation of JSON fields:**

- `sourceSetName`: Name of the Kotlin source set (e.g., `main`, `test`).
- `sourceDirectories`: List of relative directories containing Kotlin source files.
- `files`: List of Kotlin file paths relative to the project root.

## Using the Output in Your Build Scripts

You can easily integrate the task output into custom scripts:

```kotlin
tasks.register("analyzeKotlinSources") {
    dependsOn("generateKotlinSourcesStructure")
    doLast {
        val structureFile = layout.buildDirectory.file("reports/kotlin-sources-structure.json").get().asFile
        if (structureFile.exists()) {
            println("Kotlin source structure available at: ${structureFile.absolutePath}")
            val content = structureFile.readText()
            println(content)
        }
    }
}
```

## Customizing the Output Path (Optional)

You can optionally customize the output file location as follows:

```kotlin
tasks.named<GenerateKotlinSourcesStructureTask>("generateKotlinSourcesStructure") {
    outputFile.set(layout.buildDirectory.file("custom/path/my-kotlin-structure.json"))
}
```

## Building and Publishing the Plugin Locally

To build the plugin locally:

```bash
./gradlew build
```

To publish to Maven Local for local testing:

```bash
./gradlew publishToMavenLocal
```
