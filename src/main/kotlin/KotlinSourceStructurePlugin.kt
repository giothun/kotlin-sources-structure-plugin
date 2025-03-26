import org.gradle.api.Plugin
import org.gradle.api.Project

class KotlinSourceStructurePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val kotlinPluginIds = listOf(
            "org.jetbrains.kotlin.jvm",
            "org.jetbrains.kotlin.android",
            "org.jetbrains.kotlin.multiplatform"
        )

        kotlinPluginIds.forEach { pluginId ->
            project.plugins.withId(pluginId) {
                registerGenerateKotlinSourcesTask(project)
            }
        }
    }

    private fun registerGenerateKotlinSourcesTask(project: Project) {
        project.tasks.register(
            "generateKotlinSourcesStructure",
            GenerateKotlinSourcesStructureTask::class.java
        ) { task ->
            task.group = "reporting"
            task.description = "Generates Kotlin source structure as JSON."
            task.outputFile.convention(
                project.layout.buildDirectory.file("reports/kotlin-sources-structure.json")
            )
        }
    }
}
