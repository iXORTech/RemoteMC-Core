package tech.ixor.routes.controller.universal

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import tech.ixor.I18N
import tech.ixor.entity.*
import tech.ixor.utils.UniversalMessagingUtil

fun Route.universalMessaging() {
    val authKey = ConfigEntity().loadConfig().authKey

    post("/send_message") {
        val request = call.receive<UniversalSendMessageRequest>()
        val response = UniversalMessagingResponse(0, mutableListOf<HTTPResponse>())

        if (authKey != request.authKey) {
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
            response.responseCount = 1
            response.responseList.add(
                HTTPResponse(HttpStatusCode.BadRequest.value, I18N.senderIDSourceSenderOrMessageIsNull())
            )
            call.respond(response)
            return@post
        }

        call.respond(UniversalMessagingUtil.sendMessage(senderID, source, sender, message, emptyList()))

        return@post

    }

    post("/broadcast") {
        val request = call.receive<UniversalBroadcastRequest>()
        val response = UniversalMessagingResponse(0, mutableListOf<HTTPResponse>())

        if (authKey != request.authKey) {
            response.responseCount = 1
            response.responseList.add(
                HTTPResponse(HttpStatusCode.Forbidden.value, I18N.clientAuthkeyInvalid())
            )
            call.respond(response)
            return@post
        }

        val message = request.message
        if (message == null) {
            response.responseCount = 1
            response.responseList.add(
                HTTPResponse(HttpStatusCode.BadRequest.value, I18N.messageIsNull())
            )
            call.respond(response)
            return@post
        }

        call.respond(UniversalMessagingUtil.broadcast(message, emptyList()))

        return@post

    }
}
