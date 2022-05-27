package tech.ixor.routes.controller.qqbot

import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import tech.ixor.entity.ConfigEntity
import tech.ixor.entity.QQBotEntity
import tech.ixor.entity.QQBots

fun Route.qqBotMessaging() {
    val authKey = ConfigEntity().loadConfig().authKey

    post("/qqbot/message") {
        val request = call.receive<QQBotSendMessageRequest>()
        if (authKey != request.authKey) {
            call.respondText("Auth key (on your client) is not valid", status = HttpStatusCode.Forbidden)
            return@post
        }

        val host = request.host
        val port = request.port
        val groupCode = request.groupCode
        val qqBot: QQBotEntity? = QQBots.getBot(groupCode)
        if (qqBot != null) {
            if (qqBot.host != host || qqBot.port != port) {
                call.respondText(
                    "The host and port of QQBot might be wrong! Verify your request!",
                    status = HttpStatusCode.NotFound
                )
                return@post
            }
            val source = request.source
            val sender = request.sender
            val message = request.message
            val qqBotResponse = qqBot.sendMessage(source, sender, message)
            if (qqBotResponse.statusCode == 200) {
                call.respondText("Message sent!", status = HttpStatusCode.OK)
            } else if (qqBotResponse.statusCode == 401) {
                call.respondText("Auth key (on RemoteMC-Core) is not valid", status = HttpStatusCode.Forbidden)
            } else if (qqBotResponse.statusCode == 500) {
                if (qqBotResponse.message == "The chat bot is offline") {
                    call.respondText("Target QQ Bot offline", status = HttpStatusCode.InternalServerError)
                } else if (qqBotResponse.message == "Error: Group Not Found!") {
                    call.respondText("Target QQ Group could not be found", status = HttpStatusCode.InternalServerError)
                } else {
                    call.respondText("Unknown error", status = HttpStatusCode.InternalServerError)
                }
            } else {
                call.respondText("Unknown error", status = HttpStatusCode.InternalServerError)
            }

        } else {
            call.respondText("QQBot not found", status = HttpStatusCode.NotFound)
            return@post
        }

    }

}
