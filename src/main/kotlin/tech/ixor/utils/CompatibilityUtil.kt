package tech.ixor.utils

import com.beust.klaxon.JsonReader
import org.slf4j.LoggerFactory
import tech.ixor.I18N
import java.io.InputStream
import java.io.StringReader

enum class CompatibilityStatus {
    COMPATIBLE, INCOMPATIBLE, UNKNOWN_MODULE
}

class CompatibilityUtil {
    private val logger = LoggerFactory.getLogger(javaClass)

    private fun loadCompatibilityList(module: String): String {
        val moduleID = module.lowercase()
        logger.info(I18N.logging_compatibilityUtil_loadingCompatibilityList(moduleID))

        val inputStream: InputStream? = this::class.java.getResourceAsStream(
            "/compatibility-list/$moduleID.json"
        )

        return if (inputStream != null) {
            val compatibilityList = inputStream.bufferedReader().use { it.readText() }
            logger.info(I18N.logging_compatibilityUtil_compatibilityListLoaded(moduleID))
            logger.info(I18N.logging_compatibilityUtil_compatibilityListContent(compatibilityList))
            compatibilityList
        } else {
            logger.error(I18N.logging_compatibilityUtil_compatibilityListNotFound(moduleID))
            "UNKNOWN_MODULE"
        }
    }

    fun checkComaptibility(module: String, version: String, stage: String): CompatibilityStatus {
        logger.info(I18N.logging_compatibilityUtil_checkingCompatibility(module, version, stage))

        var stageList: List<String> = arrayListOf<String>()
        val compatibilityList = loadCompatibilityList(module)

        if (compatibilityList == "UNKNOWN_MODULE") {
            return CompatibilityStatus.UNKNOWN_MODULE
        }

        try {
            JsonReader(StringReader(compatibilityList)).use { reader ->
                reader.beginObject() {
                    while (reader.hasNext()) {
                        stageList = when (reader.nextName()) {
                            version -> reader.nextArray() as List<String>
                            else -> emptyList()
                        }
                    }
                }
            }
        } catch (klaxonException: com.beust.klaxon.KlaxonException) {
            logger.error(klaxonException.message?.let { I18N.logging_compatibilityUtil_klaxonException(it) })
        }

        return if (stageList.isEmpty()) {
            logger.error(I18N.logging_compatibilityUtil_versionNotFound(version, module))
            CompatibilityStatus.INCOMPATIBLE
        } else {
            logger.info(I18N.logging_compatibilityUtil_versionFound(version, module))
            if (stageList.contains(stage)) {
                logger.info(I18N.logging_compatibilityUtil_stageFound(stage, module, version))
                CompatibilityStatus.COMPATIBLE
            } else {
                logger.error(I18N.logging_compatibilityUtil_stageNotFound(stage, module, version))
                CompatibilityStatus.INCOMPATIBLE
            }
        }
    }
}
