package tech.ixor.utils
import java.io.InputStream
import java.util.*


class VersionUtil {
    companion object {
        private fun getVersionProperties(): Properties {
            val inputStream: InputStream? = this::class.java.getResourceAsStream("/version.properties")
            val properties = Properties()
            properties.load(inputStream)
            return properties
        }

        fun getProperty(key: String): String {
            return getVersionProperties().getProperty(key)
        }

        fun getVersion(): String {
            var versionProperty = getProperty("version")
            var stageProperty = getProperty("stage")
            stageProperty = stageProperty.replace(regex = Regex("dev"), replacement = "DEV")
            stageProperty = stageProperty.replace(regex = Regex("alpha\\."), replacement = "Alpha ")
            stageProperty = stageProperty.replace(regex = Regex("alpha"), replacement = "Alpha")
            stageProperty = stageProperty.replace(regex = Regex("beta\\."), replacement = "Beta ")
            stageProperty = stageProperty.replace(regex = Regex("beta"), replacement = "Beta")
            stageProperty = stageProperty.replace(regex = Regex("rc\\."), replacement = "Release Candidate ")
            stageProperty = stageProperty.replace(regex = Regex("rc"), replacement = "Release Candidate")
            var revisionProperty = getProperty("revision")
            revisionProperty = revisionProperty.uppercase()
            return "$versionProperty $stageProperty ($revisionProperty)"
        }
    }
}
