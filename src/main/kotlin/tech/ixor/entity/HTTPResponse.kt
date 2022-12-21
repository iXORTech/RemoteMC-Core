package tech.ixor.entity

import org.slf4j.Logger
import tech.ixor.I18N

class HTTPResponse(
    val statusCode: Int,
    val body: String
) {
    override fun toString(): String {
        return "HTTPResponse(statusCode=$statusCode, body='$body')"
    }

    companion object {
        fun get503(logger: Logger, serverName: String): HTTPResponse {
            val response = HTTPResponse(503, "Service Unavailable")
            logger.info(I18N.logging_mcServerOffline(serverName))
            logger.info(I18N.logging_returningResponse(response.toString()))
            return response
        }
    }
}

class PingResponse(
    val module: String = "invalid",
    val version: String = "0.0.0",
    val stage: String = "invalid",
    val revision: String = "0000000"
) {
    override fun toString(): String {
        return "PingResponse(module='$module', version='$version', stage='$stage', revision='$revision')"
    }
}
