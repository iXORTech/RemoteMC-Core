package tech.ixor.routes.controller.universal

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory
import tech.ixor.I18N
import tech.ixor.entity.*
import tech.ixor.utils.UniversalMessagingUtil

fun Route.universalMessaging() {
    val authKey = ConfigEntity().loadConfig().authKey
    val logger = LoggerFactory.getLogger(javaClass)

    post("/send_message") {
        logger.info(I18N.logging_universalMessaging_sendMessageRequestReceived())

        val request = call.receive<UniversalSendMessageRequest>()
        val response = UniversalMessagingResponse(0, mutableListOf<HTTPResponse>())

        if (authKey != request.authKey) {
            logger.warn(I18N.logging_authkeyReceivedInvalid())
            response.responseCount = 1
            response.responseList.add(
                HTTPResponse(HttpStatusCode.Forbidden.value, I18N.clientAuthkeyInvalid())
            )
            call.respond(response)
            return@post
        }

        val senderID = request.senderID
        val source = request.source
        val sender = request.sender
        val message = request.message
        if (senderID == null || source == null || sender == null || message == null) {
            logger.warn(I18N.logging_universalMessaging_sendMessageRequestParamsMissing())
            response.responseCount = 1
            response.responseList.add(
                HTTPResponse(HttpStatusCode.BadRequest.value, I18N.senderIDSourceSenderOrMessageIsNull())
            )
            call.respond(response)
            return@post
        }
        logger.info(I18N.logging_universalMessaging_sendMessageRequestParams(senderID, source, sender, message))

        call.respond(UniversalMessagingUtil.sendMessage(senderID, source, sender, message, emptyList()))

        return@post

    }

    post("/broadcast") {
        logger.info(I18N.logging_universalMessaging_broadcastRequestReceived())

        val request = call.receive<UniversalBroadcastRequest>()
        val response = UniversalMessagingResponse(0, mutableListOf<HTTPResponse>())

        if (authKey != request.authKey) {
            logger.warn(I18N.logging_authkeyReceivedInvalid())
            response.responseCount = 1
            response.responseList.add(
                HTTPResponse(HttpStatusCode.Forbidden.value, I18N.clientAuthkeyInvalid())
            )
            call.respond(response)
            return@post
        }

        val message = request.message
        if (message == null) {
            logger.warn(I18N.logging_universalMessaging_broadcastRequestParamsMissing())
            response.responseCount = 1
            response.responseList.add(
                HTTPResponse(HttpStatusCode.BadRequest.value, I18N.messageIsNull())
            )
            call.respond(response)
            return@post
        }
        logger.info(I18N.logging_universalMessaging_broadcastRequestParams(message))

        call.respond(UniversalMessagingUtil.broadcast(message, emptyList()))

        return@post

    }
}
