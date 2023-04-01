package tech.ixor.utils

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import org.slf4j.LoggerFactory
import tech.ixor.I18N

enum class CompatibilityStatus {
    COMPATIBLE, INCOMPATIBLE_VERSION, INCOMPATIBLE_STAGE, UNKNOWN_MODULE
}

class CompatibilityUtil {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun checkComaptibility(module: String, version: String, stage: String): CompatibilityStatus {
        logger.info(I18N.logging_compatibilityUtil_checkingCompatibility(module, version, stage))

        val compatibilityResource = this::class.java.getResourceAsStream(
            "/compatibility-list/$module.json"
        )

        if (compatibilityResource == null) {
            logger.error(I18N.logging_compatibilityUtil_compatibilityListNotFound(module))
            return CompatibilityStatus.UNKNOWN_MODULE
        }

        logger.info(I18N.logging_compatibilityUtil_compatibilityListLoaded(module))

        val compatibleVersion = (
            Parser.default().parse(compatibilityResource) as JsonArray<JsonObject>
        ).filter {
            it.string("version") == version
        }

        if (compatibleVersion.isEmpty()) {
            logger.warn(I18N.logging_compatibilityUtil_versionNotCompatible(module, version))
            return CompatibilityStatus.INCOMPATIBLE_VERSION
        }

        val compatibleStage = compatibleVersion.filter {
            it.array<String>("stages")!!.contains(stage)
        }

        if (compatibleStage.isEmpty()) {
            logger.warn(I18N.logging_compatibilityUtil_stageNotCompatible(module, version, stage))
            return CompatibilityStatus.INCOMPATIBLE_STAGE
        }

        logger.info(I18N.logging_compatibilityUtil_moduleCompatible(module, version, stage))
        return CompatibilityStatus.COMPATIBLE
    }
}
