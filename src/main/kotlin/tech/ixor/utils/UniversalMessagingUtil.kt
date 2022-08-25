package tech.ixor.utils

import io.ktor.http.*
import tech.ixor.I18N
import tech.ixor.entity.*
import tech.ixor.routes.controller.universal.UniversalMessagingResponse

class UniversalMessagingUtil {
    val authKey = ConfigEntity().loadConfig().authKey

    companion object {
        suspend fun sendMessage(
            senderID: String, source: String,
            sender: String, message: String,
            excludeServers: List<ServerEntity>
        ): UniversalMessagingResponse {
            val response = UniversalMessagingResponse(0, mutableListOf<HTTPResponse>())

            val minecraftServers = MinecraftServers.getOnlineServers()
            for (minecraftServer in minecraftServers) {
                if (excludeServers.contains(minecraftServer)) {
                    continue
                }
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
            if (excludeServers.contains(qqBot)) {
                return response
            }
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

                        404 -> {
                            response.responseCount++
                            response.responseList.add(
                                HTTPResponse(
                                    statusCode = HttpStatusCode.NotFound.value,
                                    message = I18N.qqBotGroupNotFound(qqGroup.groupName, qqGroup.groupCode)
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

            return response
        }

        suspend fun broadcast(
            message: String,
            excludeServers: List<ServerEntity>
        ): UniversalMessagingResponse {
            val response = UniversalMessagingResponse(0, mutableListOf<HTTPResponse>())

            val minecraftServers = MinecraftServers.getOnlineServers()
            for (minecraftServer in minecraftServers) {
                if (excludeServers.contains(minecraftServer)) {
                    continue
                }
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
            if (excludeServers.contains(qqBot)) {
                return response
            }
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

                        404 -> {
                            response.responseCount++
                            response.responseList.add(
                                HTTPResponse(
                                    statusCode = HttpStatusCode.NotFound.value,
                                    message = I18N.qqBotGroupNotFound(qqGroup.groupName, qqGroup.groupCode)
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

            return response
        }
    }
}
