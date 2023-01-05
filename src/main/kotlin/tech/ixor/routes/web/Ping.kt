package tech.ixor.routes.web

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory
import tech.ixor.I18N
import tech.ixor.utils.CompatibilityStatus
import tech.ixor.utils.CompatibilityUtil

data class PingRequest(
    val module: String, val version: String, val stage: String, val revision: String
)

fun Route.ping() {
    val logger = LoggerFactory.getLogger(javaClass)

    get("/ping") {
        logger.info(I18N.logging_pingRequestReceived())

        val request = call.receive<PingRequest>()

        val module = request.module
        val version = request.version
        val stage = request.stage
        val revision = request.revision

        logger.info(I18N.logging_pingRequestReceivedFor(module, version, stage, revision))

        when (CompatibilityUtil().checkComaptibility(module=module, version=version, stage=stage)) {
            CompatibilityStatus.COMPATIBLE -> {
                logger.info(I18N.logging_moduleIsCompatible(module, version, stage))
                logger.error(I18N.logging_return200("/ping"))
                call.respond(HttpStatusCode.OK)
            }
            CompatibilityStatus.INCOMPATIBLE -> {
                logger.info(I18N.logging_moduleIsNotCompatible(module, version, stage))
                logger.error(I18N.logging_return426("/ping"))
                call.respond(HttpStatusCode.UpgradeRequired)
            }
            else -> {
                logger.info(I18N.logging_moduleIsUnknown(module))
                logger.error(I18N.logging_return400("/ping"))
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}
