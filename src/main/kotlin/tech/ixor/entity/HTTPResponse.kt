package tech.ixor.entity

import com.beust.klaxon.Json
import org.slf4j.Logger
import tech.ixor.I18N

class HTTPResponse(
    @Json(name = "status_code")
    val statusCode: Int,
    val message: String
) {
    override fun toString(): String {
        return "HTTPResponse(statusCode=$statusCode, message='$message')"
    }

    companion object {
        fun get503(logger: Logger, serverName: String): HTTPResponse {
            val response = HTTPResponse(503, "Service Unavailable")
            logger.info(I18N.logging_mcServerOffline(serverName))
            logger.info(I18N.logging_sendingResponse(response.toString()))
            return response
        }
    }
}
