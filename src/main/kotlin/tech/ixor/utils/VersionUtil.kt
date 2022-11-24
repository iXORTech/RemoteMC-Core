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
        private val properties = Properties()

        fun loadVersionProperties() {
            val inputStream: InputStream? = this::class.java.getResourceAsStream("/version.properties")
            properties.load(inputStream)
            logger.info(I18N.logging_versionUtil_loadingVersionProperties(properties.toString()))
        }

        fun getProperty(key: String): String {
            val property = properties.getProperty(key)
            logger.info(I18N.logging_versionUtil_gettingProperty(key, property))
            return property
        }

        fun getVersion(): String {
            val versionProperty = getProperty("version")
            logger.info(I18N.logging_versionUtil_versionProperty(versionProperty))
            var revisionProperty = getProperty("revision")
            revisionProperty = revisionProperty.uppercase()
            logger.info(I18N.logging_versionUtil_revisionProperty(revisionProperty))
            var stageProperty = getProperty("stage")
            logger.info(I18N.logging_versionUtil_stageProperty(stageProperty))
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
            logger.info(I18N.logging_versionUtil_readableStageProperty(stageProperty))
            val version = "$versionProperty $stageProperty ($revisionProperty)"
            logger.info(I18N.logging_versionUtil_version(version))
            return version
        }
    }
}
