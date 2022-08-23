package tech.ixor.routes.controller.universal

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import tech.ixor.I18N
import tech.ixor.entity.*

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

        val minecraftServers = MinecraftServers.getOnlineServers()
        for (minecraftServer in minecraftServers) {
            val mcServerResponse = minecraftServer.sendMessage(senderID, source, sender, message)
            when (mcServerResponse.statusCode) {
                200 -> {
                    response.responseCount++
                    response.responseList.add(
                        HTTPResponse(
                            statusCode = HttpStatusCode.OK.value,
                            message = I18N.messageSentToMCServer(minecraftServer.serverName)
                        )
                    )
                }
                401 -> {
                    response.responseCount++
                    response.responseList.add(
                        HTTPResponse(
                            statusCode = HttpStatusCode.Unauthorized.value,
                            message = I18N.coreAuthkeyInvalid()
                        )
                    )
                }
                503 -> {
                    response.responseCount++
                    response.responseList.add(
                        HTTPResponse(
                            statusCode = HttpStatusCode.ServiceUnavailable.value,
                            message = I18N.mcserverOfflineCannotSend(minecraftServer.serverName)
                        )
                    )
                }
                else -> {
                    response.responseCount++
                    response.responseList.add(
                        HTTPResponse(
                            statusCode = HttpStatusCode.InternalServerError.value,
                            message = I18N.unknownError(mcServerResponse.statusCode, mcServerResponse.message)
                        )
                    )
                }
            }
        }

        val qqBot = QQBot.getBot()
        if (qqBot.checkOnlineStatus()) {
            val qqGroups = QQGroups.getQQGroups()
            for (qqGroup in qqGroups) {
                val qqBotResponse = qqGroup.sendMessage(qqBot, senderID, source, sender, message)
                when (qqBotResponse.statusCode) {
                    200 -> {
                        response.responseCount++
                        response.responseList.add(
                            HTTPResponse(
                                statusCode = HttpStatusCode.OK.value,
                                message = I18N.messageSentToGroup(qqGroup.groupName, qqGroup.groupCode)
                            )
                        )
                    }
                    401 -> {
                        response.responseCount++
                        response.responseList.add(
                            HTTPResponse(
                                statusCode = HttpStatusCode.Unauthorized.value,
                                message = I18N.coreAuthkeyInvalid()
                            )
                        )
                    }
                    503 -> {
                        response.responseCount++
                        response.responseList.add(
                            HTTPResponse(
                                statusCode = HttpStatusCode.ServiceUnavailable.value,
                                message = I18N.qqBotOfflineCannotSendGroup(qqGroup.groupName, qqGroup.groupCode)
                            )
                        )
                    }
                    else -> {
                        response.responseCount++
                        response.responseList.add(
                            HTTPResponse(
                                statusCode = HttpStatusCode.InternalServerError.value,
                                message = I18N.unknownError(qqBotResponse.statusCode, qqBotResponse.message)
                            )
                        )
                    }
                }
            }
        } else {
            response.responseCount++
            response.responseList.add(
                HTTPResponse(
                    statusCode = HttpStatusCode.ServiceUnavailable.value,
                    message = I18N.qqBotOffline()
                )
            )
        }

        call.respond(response)

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

        val minecraftServers = MinecraftServers.getOnlineServers()
        for (minecraftServer in minecraftServers) {
            val mcServerResponse = minecraftServer.broadcast(message)
            when (mcServerResponse.statusCode) {
                200 -> {
                    response.responseCount++
                    response.responseList.add(
                        HTTPResponse(
                            statusCode = HttpStatusCode.OK.value,
                            message = I18N.broadcastSentToMCServer(minecraftServer.serverName)
                        )
                    )
                }
                401 -> {
                    response.responseCount++
                    response.responseList.add(
                        HTTPResponse(
                            statusCode = HttpStatusCode.Unauthorized.value,
                            message = I18N.coreAuthkeyInvalid()
                        )
                    )
                }
                503 -> {
                    response.responseCount++
                    response.responseList.add(
                        HTTPResponse(
                            statusCode = HttpStatusCode.ServiceUnavailable.value,
                            message = I18N.mcserverOfflineCannotSend(minecraftServer.serverName)
                        )
                    )
                }
                else -> {
                    response.responseCount++
                    response.responseList.add(
                        HTTPResponse(
                            statusCode = HttpStatusCode.InternalServerError.value,
                            message = I18N.unknownError(mcServerResponse.statusCode, mcServerResponse.message)
                        )
                    )
                }
            }
        }

        val qqBot = QQBot.getBot()
        if (qqBot.checkOnlineStatus()) {
            val qqGroups = QQGroups.getQQGroups()
            for (qqGroup in qqGroups) {
                val qqBotResponse = qqGroup.broadcast(qqBot, message)
                when (qqBotResponse.statusCode) {
                    200 -> {
                        response.responseCount++
                        response.responseList.add(
                            HTTPResponse(
                                statusCode = HttpStatusCode.OK.value,
                                message = I18N.broadcastSentToGroup(qqGroup.groupName, qqGroup.groupCode)
                            )
                        )
                    }
                    401 -> {
                        response.responseCount++
                        response.responseList.add(
                            HTTPResponse(
                                statusCode = HttpStatusCode.Unauthorized.value,
                                message = I18N.coreAuthkeyInvalid()
                            )
                        )
                    }
                    503 -> {
                        response.responseCount++
                        response.responseList.add(
                            HTTPResponse(
                                statusCode = HttpStatusCode.ServiceUnavailable.value,
                                message = I18N.qqBotOfflineCannotSendGroup(qqGroup.groupName, qqGroup.groupCode)
                            )
                        )
                    }
                    else -> {
                        response.responseCount++
                        response.responseList.add(
                            HTTPResponse(
                                statusCode = HttpStatusCode.InternalServerError.value,
                                message = I18N.unknownError(qqBotResponse.statusCode, qqBotResponse.message)
                            )
                        )
                    }
                }
            }
        } else {
            response.responseCount++
            response.responseList.add(
                HTTPResponse(
                    statusCode = HttpStatusCode.ServiceUnavailable.value,
                    message = I18N.qqBotOffline()
                )
            )
        }

        call.respond(response)

        return@post

    }
}
