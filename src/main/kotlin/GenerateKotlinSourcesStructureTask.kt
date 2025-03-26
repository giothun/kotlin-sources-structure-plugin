import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import java.io.File

@Serializable
data class KotlinSourceSetInfo(
    val sourceSetName: String,
    val sourceDirectories: List<String>,
    val files: List<String>
)

abstract class GenerateKotlinSourcesStructureTask : DefaultTask() {

    companion object {
        private val JSON_FORMAT = Json {
            prettyPrint = true
            encodeDefaults = true
        }
    }

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    init {
        group = "reporting"
        description = "Generates Kotlin source structure as JSON."
    }

    @TaskAction
    fun generate() {
        val kotlinExtension = project.extensions.findByType(KotlinProjectExtension::class.java)
            ?: throw IllegalStateException(
                "Kotlin plugin is not configured for project '${project.path}'. Ensure Kotlin plugin is applied."
            )

        val kotlinSourceSetsInfo = kotlinExtension.sourceSets.asSequence().map { sourceSet ->
            KotlinSourceSetInfo(
                sourceSetName = sourceSet.name,
                sourceDirectories = sourceSet.kotlin.srcDirs.map {
                    it.relativePathFromProject(project)
                },
                files = sourceSet.kotlin.files.map {
                    it.relativePathFromProject(project)
                }
            )
        }.toList()

        val jsonOutput = JSON_FORMAT.encodeToString(kotlinSourceSetsInfo)

        writeOutput(jsonOutput)

        logger.lifecycle("Kotlin source structure written to ${outputFile.get().asFile.absolutePath}")
    }

    private fun writeOutput(content: String) {
        val file = outputFile.get().asFile
        runCatching {
            file.parentFile.mkdirs()
            file.writeText(content)
        }.onFailure { e ->
            logger.error("Failed to write Kotlin source structure to file: ${file.absolutePath}", e)
        }
    }

    private fun File.relativePathFromProject(project: Project): String =
        relativeTo(project.projectDir).path
}
