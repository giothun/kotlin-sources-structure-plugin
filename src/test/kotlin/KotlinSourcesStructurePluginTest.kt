import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class KotlinSourcesStructurePluginTest {

    @TempDir
    lateinit var testProjectDir: File

    private lateinit var buildFile: File

    @BeforeEach
    fun setup() {
        testProjectDir.createFile("settings.gradle.kts", """rootProject.name = "test-project"""")

        buildFile = testProjectDir.resolve("build.gradle.kts")

        testProjectDir.createFile(
            "src/main/kotlin/com/example/test/TestClass.kt",
            """
                package com.example.test
                class TestClass
            """
        )
    }

    @Test
    fun `task generates correct JSON when Kotlin plugin applied`() {
        buildFile.writeText("""
            plugins {
                kotlin("jvm") version "1.9.21"
                id("io.github.giothun.kotlin-sources-structure")
            }

            repositories { mavenCentral() }
        """)

        val result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments("generateKotlinSourcesStructure")
            .withPluginClasspath()
            .forwardOutput()
            .build()

        assertEquals(TaskOutcome.SUCCESS, result.task(":generateKotlinSourcesStructure")?.outcome)

        val outputFile = File(testProjectDir, "build/reports/kotlin-sources-structure.json")
        assertTrue(outputFile.exists(), "JSON output file should exist")

        val jsonContent = outputFile.readText()
        val jsonElements = Json.parseToJsonElement(jsonContent).jsonArray

        assertTrue(jsonElements.any { sourceSet ->
            sourceSet.jsonObject["files"]!!.jsonArray.any { file ->
                file.jsonPrimitive.content.endsWith("TestClass.kt")
            }
        }, "Generated JSON should reference 'TestClass.kt'")
    }

    @Test
    fun `task not registered without Kotlin plugin`() {
        buildFile.writeText("""
            plugins { id("io.github.giothun.kotlin-sources-structure") }
        """)

        val result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments("generateKotlinSourcesStructure")
            .withPluginClasspath()
            .buildAndFail()

        assertTrue(result.output.contains("Task 'generateKotlinSourcesStructure' not found"))
    }

    private fun File.createFile(path: String, content: String): File =
        resolve(path).apply {
            parentFile.mkdirs()
            writeText(content.trimIndent())
        }
}
