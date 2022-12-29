package tech.ixor.utils

import com.beust.klaxon.JsonReader
import org.slf4j.LoggerFactory
import tech.ixor.I18N
import java.io.InputStream
import java.io.StringReader

class CompatibilityUtil {
    private val logger = LoggerFactory.getLogger(javaClass)

    private fun loadCompatibilityList(module: String): String {
        logger.info(I18N.logging_compatibilityUtil_loadingCompatibilityList(module))
        val inputStream: InputStream? = this::class.java.getResourceAsStream(
            "/compatibility-list/$module.json"
        )
        return if (inputStream != null) {
            val compatibilityList = inputStream.bufferedReader().use { it.readText() }
            logger.info(I18N.logging_compatibilityUtil_compatibilityListLoaded(module))
            logger.info(I18N.logging_compatibilityUtil_compatibilityListContent(compatibilityList))
            compatibilityList
        } else {
            logger.error(I18N.logging_compatibilityUtil_compatibilityListNotFound(module))
            ""
        }
    }

    fun checkComaptibility(module: String, version: String, stage: String): Boolean {
        logger.info(I18N.logging_compatibilityUtil_checkingCompatibility(module, version, stage))

        var stageList: List<String> = arrayListOf<String>()

        JsonReader(StringReader(loadCompatibilityList(module))).use { reader ->
            reader.beginObject() {
                while (reader.hasNext()) {
                    stageList = when (reader.nextName()) {
                        version -> reader.nextArray() as List<String>
                        else -> emptyList()
                    }
                }
            }
        }

        return if (stageList.isEmpty()) {
            logger.error(I18N.logging_compatibilityUtil_versionNotFound(module, version))
            false
        } else {
            logger.info(I18N.logging_compatibilityUtil_versionFound(module, version))
            if (stageList.contains(stage)) {
                logger.info(I18N.logging_compatibilityUtil_stageFound(stage, module, version))
                true
            } else {
                logger.error(I18N.logging_compatibilityUtil_stageNotFound(stage, module, version))
                false
            }
        }
    }
}
