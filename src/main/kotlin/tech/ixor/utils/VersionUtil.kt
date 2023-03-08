package tech.ixor.utils
import java.io.InputStream
import java.util.*


class VersionUtil {
    companion object {
        private val properties = Properties()

        fun loadVersionProperties() {
            val inputStream: InputStream? = this::class.java.getResourceAsStream("/version.properties")
            properties.load(inputStream)
        }

        fun getProperty(key: String): String {
            val property = properties.getProperty(key)
            return property
        }

        fun getBuildDate(): String {
            val buildDateProperty = getProperty("buildDate")
            return buildDateProperty
        }

        fun getVersion(): String {
            val versionProperty = getProperty("version")
            var revisionProperty = getProperty("revision")
            revisionProperty = revisionProperty.uppercase()
            var stageProperty = getProperty("stage")
            if (stageProperty == "stable") {
                return "$versionProperty ($revisionProperty)"
            }
            stageProperty = stageProperty.replace(regex = Regex("dev"), replacement = "DEV")
            stageProperty = stageProperty.replace(regex = Regex("alpha\\."), replacement = "Alpha ")
            stageProperty = stageProperty.replace(regex = Regex("alpha"), replacement = "Alpha")
            stageProperty = stageProperty.replace(regex = Regex("beta\\."), replacement = "Beta ")
            stageProperty = stageProperty.replace(regex = Regex("beta"), replacement = "Beta")
            stageProperty = stageProperty.replace(regex = Regex("rc\\."), replacement = "Release Candidate ")
            stageProperty = stageProperty.replace(regex = Regex("rc"), replacement = "Release Candidate")
            val version = "$versionProperty $stageProperty ($revisionProperty)"
            return version
        }
    }
}
