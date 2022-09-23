package tech.ixor.utils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import tech.ixor.I18N
import java.io.InputStream
import java.util.*


class VersionUtil {
    private val logger = LoggerFactory.getLogger(javaClass)

    companion object {
        private val logger: Logger = VersionUtil().logger

        private fun getVersionProperties(): Properties {
            val inputStream: InputStream? = this::class.java.getResourceAsStream("/version.properties")
            val properties = Properties()
            properties.load(inputStream)
            logger.info(I18N.logging_versionUtil_gettingVersionProperties(properties.toString()))
            return properties
        }

        fun getProperty(key: String): String {
            val property = getVersionProperties().getProperty(key)
            logger.info(I18N.logging_versionUtil_gettingProperty(key, property))
            return property
        }

        fun getVersion(): String {
            val versionProperty = getProperty("version")
            logger.info(I18N.logging_versionUtil_versionProperty(versionProperty))
            var stageProperty = getProperty("stage")
            logger.info(I18N.logging_versionUtil_stageProperty(stageProperty))
            stageProperty = stageProperty.replace(regex = Regex("dev"), replacement = "DEV")
            stageProperty = stageProperty.replace(regex = Regex("alpha\\."), replacement = "Alpha ")
            stageProperty = stageProperty.replace(regex = Regex("alpha"), replacement = "Alpha")
            stageProperty = stageProperty.replace(regex = Regex("beta\\."), replacement = "Beta ")
            stageProperty = stageProperty.replace(regex = Regex("beta"), replacement = "Beta")
            stageProperty = stageProperty.replace(regex = Regex("rc\\."), replacement = "Release Candidate ")
            stageProperty = stageProperty.replace(regex = Regex("rc"), replacement = "Release Candidate")
            logger.info(I18N.logging_versionUtil_readableStageProperty(stageProperty))
            var revisionProperty = getProperty("revision")
            revisionProperty = revisionProperty.uppercase()
            logger.info(I18N.logging_versionUtil_revisionProperty(revisionProperty))
            val version = "$versionProperty $stageProperty ($revisionProperty)"
            logger.info(I18N.logging_versionUtil_version(version))
            return version
        }
    }
}
