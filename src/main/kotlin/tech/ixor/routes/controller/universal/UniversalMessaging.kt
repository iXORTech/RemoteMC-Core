package tech.ixor.routes.controller.universal

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import tech.ixor.I18N
import tech.ixor.entity.ConfigEntity
import tech.ixor.entity.MinecraftServers
import tech.ixor.entity.QQBot
import tech.ixor.entity.QQGroups

fun Route.universalMessaging() {
    val authKey = ConfigEntity().loadConfig().authKey

    post("/send_message") {
        val request = call.receive<UniversalSendMessageRequest>()
        if (authKey != request.authKey) {
            call.respondText(I18N.clientAuthkeyInvalid(), status = HttpStatusCode.Forbidden)
            return@post
        }

        val senderID = request.senderID
        val source = request.source
        val sender = request.sender
        val message = request.message

        if (senderID == null || source == null || sender == null || message == null) {
            call.respondText(I18N.senderIDSourceSenderOrMessageIsNull(), status = HttpStatusCode.BadRequest)
            return@post
        }

        val minecraftServers = MinecraftServers.getOnlineServers()
        for (minecraftServer in minecraftServers) {
            val mcServerResponse = minecraftServer.sendMessage(senderID, source, sender, message)
            when (mcServerResponse.statusCode) {
                200 -> call.respondText(I18N.messageSentToMCServer(minecraftServer.serverName), status = HttpStatusCode.OK)
                401 -> call.respondText(I18N.coreAuthkeyInvalid(), status = HttpStatusCode.Unauthorized)
                503 -> call.respondText(I18N.mcserverOfflineCannotSend(minecraftServer.serverName),
                    status = HttpStatusCode.ServiceUnavailable)
                else -> call.respondText(
                    I18N.unknownError(mcServerResponse.statusCode, mcServerResponse.message),
                    status = HttpStatusCode.InternalServerError
                )
            }
        }

        val qqBot = QQBot.getBot()
        if (qqBot.checkOnlineStatus()) {
            val qqGroups = QQGroups.getQQGroups()
            for (qqGroup in qqGroups) {
                val qqBotResponse = qqGroup.sendMessage(qqBot, senderID, source, sender, message)
                when (qqBotResponse.statusCode) {
                    200 -> call.respondText(I18N.messageSentToGroup(qqGroup.groupName, qqGroup.groupCode),
                        status = HttpStatusCode.OK)
                    401 -> call.respondText(I18N.coreAuthkeyInvalid(), status = HttpStatusCode.Unauthorized)
                    503 -> call.respondText(I18N.qqBotOfflineCannotSendGroup(qqGroup.groupName, qqGroup.groupCode),
                        status = HttpStatusCode.ServiceUnavailable)
                    else -> call.respondText(
                        I18N.unknownError(qqBotResponse.statusCode, qqBotResponse.message),
                        status = HttpStatusCode.InternalServerError
                    )
                }
            }
        } else {
            call.respondText(I18N.qqBotOffline(), status = HttpStatusCode.ServiceUnavailable)
        }

        return@post

    }

    post("/broadcast") {
        // TODO：Add broadcast route that broadcast message to all clients (Minecraft Servers, QQ ChatBots, etc.)

    }
}
