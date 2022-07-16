package tech.ixor.routes.controller.qqbot

import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import tech.ixor.I18N
import tech.ixor.entity.ConfigEntity
import tech.ixor.entity.QQBotEntity
import tech.ixor.entity.QQBot
import tech.ixor.entity.QQGroups

fun Route.qqBotMessaging() {
    val authKey = ConfigEntity().loadConfig().authKey

    post("/qqbot/message") {
        val request = call.receive<QQBotSendMessageRequest>()
        if (authKey != request.authKey) {
            call.respondText(I18N.client_authkey_invalid(), status = HttpStatusCode.Forbidden)
            return@post
        }

        val qqBot: QQBotEntity = QQBot.getBot()

        val groupCode = request.groupCode
        val qqGroup = QQGroups.getQQGroup(groupCode)
        if (qqGroup == null) {
            call.respondText(I18N.groupcode_invalid(groupCode), status = HttpStatusCode.NotFound)
            return@post
        }

        val source = request.source
        val sender = request.sender
        val message = request.message
        val qqBotResponse = qqBot.sendMessage(groupCode, source, sender, message)
        when (qqBotResponse.statusCode) {
            200 -> call.respondText(I18N.message_sent_to_group(qqGroup.groupName), status = HttpStatusCode.OK)
            401 -> call.respondText(I18N.core_authkey_invalid(), status = HttpStatusCode.Unauthorized)
            503 -> call.respondText(I18N.qqbot_offline(), status = HttpStatusCode.ServiceUnavailable)
            else -> call.respondText(
                I18N.unknown_error(qqBotResponse.statusCode, qqBotResponse.message),
                status = HttpStatusCode.InternalServerError
            )
        }
        return@post
    }
}
