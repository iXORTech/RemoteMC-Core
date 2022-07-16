package tech.ixor.routes.controller.qqbot

import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import tech.ixor.entity.ConfigEntity
import tech.ixor.entity.QQBotEntity
import tech.ixor.entity.QQBot
import tech.ixor.entity.QQGroups

fun Route.qqBotMessaging() {
    val authKey = ConfigEntity().loadConfig().authKey

    post("/qqbot/message") {
        val request = call.receive<QQBotSendMessageRequest>()
        if (authKey != request.authKey) {
            call.respondText("Auth key (on your client) is not valid", status = HttpStatusCode.Forbidden)
            return@post
        }

        val qqBot: QQBotEntity = QQBot.getBot()

        val groupCode = request.groupCode
        val qqGroup = QQGroups.getQQGroup(groupCode)
        if (qqGroup == null) {
            call.respondText(
                "The group code ${groupCode} is not valid! Check your RemoteMC-Core config file!",
                status = HttpStatusCode.NotFound
            )
            return@post
        }

        val source = request.source
        val sender = request.sender
        val message = request.message
        val qqBotResponse = qqBot.sendMessage(groupCode, source, sender, message)
        when (qqBotResponse.statusCode) {
            200 -> call.respondText("Message sent successfully to ${qqGroup.groupName}!", status = HttpStatusCode.OK)
            401 -> call.respondText(
                "Auth key on RemoteMC-Core is not valid! Please check authKey settings and make sure" +
                        " they were the same everywhere!", status = HttpStatusCode.Unauthorized
            )
            503 -> call.respondText(
                "The QQ Bot that you are requesting might be offline!",
                status = HttpStatusCode.ServiceUnavailable
            )
            else -> call.respondText(
                "Unknown error! Status Code ${qqBotResponse.statusCode} - Message ${qqBotResponse.message}.",
                status = HttpStatusCode.InternalServerError
            )
        }
        return@post
    }
}
