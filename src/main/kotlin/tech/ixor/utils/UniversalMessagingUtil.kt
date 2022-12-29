package tech.ixor.utils

import io.ktor.http.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import tech.ixor.I18N
import tech.ixor.entity.*
import tech.ixor.routes.controller.universal.UniversalMessagingResponse

class UniversalMessagingUtil {
    private val logger = LoggerFactory.getLogger(javaClass)

    companion object {
        private val logger: Logger = UniversalMessagingUtil().logger

        suspend fun sendMessage(
            senderID: String, source: String,
            sender: String, message: String,
            excludeServers: List<ServerEntity>
        ): UniversalMessagingResponse {
            logger.info(I18N.logging_universalMessagingUtil_sendMessage())

            val response = UniversalMessagingResponse(0, mutableListOf<HTTPResponse>())

            val minecraftServers = MinecraftServers.getOnlineServers()
            if (minecraftServers.isEmpty()) {
                logger.warn(I18N.noOnlineMcServer())
                response.responseCount++
                response.responseList.add(
                    HTTPResponse(
                        statusCode = HttpStatusCode.ServiceUnavailable.value,
                        body = I18N.noOnlineMcServer()
                    )
                )
            } else {
                for (minecraftServer in minecraftServers) {
                    logger.info(I18N.logging_universalMessagingUtil_sendMessageToMcServer(minecraftServer.serverName))

                    if (excludeServers.contains(minecraftServer)) {
                        logger.info(I18N.logging_universalMessagingUtil_sendMessageMcServerExcluded(minecraftServer.serverName))
                        continue
                    }

                    val mcServerResponse = minecraftServer.sendMessage(senderID, source, sender, message)
                    when (mcServerResponse.statusCode) {
                        200 -> {
                            logger.info(I18N.messageSentToMCServer(minecraftServer.serverName))
                            response.responseCount++
                            response.responseList.add(
                                HTTPResponse(
                                    statusCode = HttpStatusCode.OK.value,
                                    body = I18N.messageSentToMCServer(minecraftServer.serverName)
                                )
                            )
                        }

                        401 -> {
                            logger.warn(I18N.coreAuthkeyInvalid())
                            response.responseCount++
                            response.responseList.add(
                                HTTPResponse(
                                    statusCode = HttpStatusCode.Unauthorized.value,
                                    body = I18N.coreAuthkeyInvalid()
                                )
                            )
                        }

                        503 -> {
                            logger.warn(I18N.mcserverOfflineCannotSend(minecraftServer.serverName))
                            response.responseCount++
                            response.responseList.add(
                                HTTPResponse(
                                    statusCode = HttpStatusCode.ServiceUnavailable.value,
                                    body = I18N.mcserverOfflineCannotSend(minecraftServer.serverName)
                                )
                            )
                        }

                        else -> {
                            logger.warn(
                                I18N.logging_universalMessagingUtil_mcServerUnknownError(
                                    mcServerResponse.statusCode,
                                    mcServerResponse.body
                                )
                            )
                            response.responseCount++
                            response.responseList.add(
                                HTTPResponse(
                                    statusCode = HttpStatusCode.InternalServerError.value,
                                    body = I18N.unknownError(mcServerResponse.statusCode, mcServerResponse.body)
                                )
                            )
                        }
                    }

                    logger.info(I18N.logging_universalMessagingUtil_sendMessageToMcServerProcessed(minecraftServer.serverName))
                }
            }

            val qqBot = QQBot.getBot()

            logger.info(I18N.logging_universalMessagingUtil_sendMessageToQQBot())

            if (excludeServers.contains(qqBot)) {
                logger.info(I18N.logging_universalMessagingUtil_sendMessageQQBotExcluded())
                return response
            }

            if (qqBot.checkOnlineStatus()) {
                val qqGroups = qqBot.getQQGroups()
                for (qqGroup in qqGroups) {
                    logger.info(I18N.logging_universalMessagingUtil_sendMessageToQQGroup(qqGroup.groupName, qqGroup.groupCode))

                    val qqBotResponse = qqGroup.sendMessage(senderID, source, sender, message)
                    when (qqBotResponse.statusCode) {
                        200 -> {
                            logger.info(I18N.messageSentToGroup(qqGroup.groupName, qqGroup.groupCode))
                            response.responseCount++
                            response.responseList.add(
                                HTTPResponse(
                                    statusCode = HttpStatusCode.OK.value,
                                    body = I18N.messageSentToGroup(qqGroup.groupName, qqGroup.groupCode)
                                )
                            )
                        }

                        401 -> {
                            logger.warn(I18N.coreAuthkeyInvalid())
                            response.responseCount++
                            response.responseList.add(
                                HTTPResponse(
                                    statusCode = HttpStatusCode.Unauthorized.value,
                                    body = I18N.coreAuthkeyInvalid()
                                )
                            )
                        }

                        404 -> {
                            logger.warn(I18N.qqBotGroupNotFound(qqGroup.groupName, qqGroup.groupCode))
                            response.responseCount++
                            response.responseList.add(
                                HTTPResponse(
                                    statusCode = HttpStatusCode.NotFound.value,
                                    body = I18N.qqBotGroupNotFound(qqGroup.groupName, qqGroup.groupCode)
                                )
                            )
                        }

                        503 -> {
                            logger.warn(I18N.qqBotOfflineCannotSendGroup(qqGroup.groupName, qqGroup.groupCode))
                            response.responseCount++
                            response.responseList.add(
                                HTTPResponse(
                                    statusCode = HttpStatusCode.ServiceUnavailable.value,
                                    body = I18N.qqBotOfflineCannotSendGroup(qqGroup.groupName, qqGroup.groupCode)
                                )
                            )
                        }

                        else -> {
                            logger.warn(I18N.logging_universalMessagingUtil_qqBotUnknownError(qqBotResponse.statusCode, qqBotResponse.body))
                            response.responseCount++
                            response.responseList.add(
                                HTTPResponse(
                                    statusCode = HttpStatusCode.InternalServerError.value,
                                    body = I18N.unknownError(qqBotResponse.statusCode, qqBotResponse.body)
                                )
                            )
                        }
                    }

                    logger.info(I18N.logging_universalMessagingUtil_sendMessageToQQGroupProcessed(qqGroup.groupName, qqGroup.groupCode))
                }
            } else {
                logger.warn(I18N.qqBotOffline())
                response.responseCount++
                response.responseList.add(
                    HTTPResponse(
                        statusCode = HttpStatusCode.ServiceUnavailable.value,
                        body = I18N.qqBotOffline()
                    )
                )
            }

            return response
        }

        suspend fun broadcast(
            message: String,
            excludeServers: List<ServerEntity>
        ): UniversalMessagingResponse {
            logger.info(I18N.logging_universalMessagingUtil_broadcast())

            val response = UniversalMessagingResponse(0, mutableListOf<HTTPResponse>())

            val minecraftServers = MinecraftServers.getOnlineServers()
            if (minecraftServers.isEmpty()) {
                logger.warn(I18N.noOnlineMcServer())
                response.responseCount++
                response.responseList.add(
                    HTTPResponse(
                        statusCode = HttpStatusCode.ServiceUnavailable.value,
                        body = I18N.noOnlineMcServer()
                    )
                )
            } else {
                for (minecraftServer in minecraftServers) {
                    logger.info(I18N.logging_universalMessagingUtil_broadcastToMcServer(minecraftServer.serverName))

                    if (excludeServers.contains(minecraftServer)) {
                        logger.info(I18N.logging_universalMessagingUtil_broadcastMcServerExcluded(minecraftServer.serverName))
                        continue
                    }

                    val mcServerResponse = minecraftServer.broadcast(message)
                    when (mcServerResponse.statusCode) {
                        200 -> {
                            logger.info(I18N.broadcastSentToMCServer(minecraftServer.serverName))
                            response.responseCount++
                            response.responseList.add(
                                HTTPResponse(
                                    statusCode = HttpStatusCode.OK.value,
                                    body = I18N.broadcastSentToMCServer(minecraftServer.serverName)
                                )
                            )
                        }

                        401 -> {
                            logger.warn(I18N.coreAuthkeyInvalid())
                            response.responseCount++
                            response.responseList.add(
                                HTTPResponse(
                                    statusCode = HttpStatusCode.Unauthorized.value,
                                    body = I18N.coreAuthkeyInvalid()
                                )
                            )
                        }

                        503 -> {
                            logger.warn(I18N.mcserverOfflineCannotSend(minecraftServer.serverName))
                            response.responseCount++
                            response.responseList.add(
                                HTTPResponse(
                                    statusCode = HttpStatusCode.ServiceUnavailable.value,
                                    body = I18N.mcserverOfflineCannotSend(minecraftServer.serverName)
                                )
                            )
                        }

                        else -> {
                            logger.warn(
                                I18N.logging_universalMessagingUtil_mcServerUnknownError(
                                    mcServerResponse.statusCode,
                                    mcServerResponse.body
                                )
                            )
                            response.responseCount++
                            response.responseList.add(
                                HTTPResponse(
                                    statusCode = HttpStatusCode.InternalServerError.value,
                                    body = I18N.unknownError(mcServerResponse.statusCode, mcServerResponse.body)
                                )
                            )
                        }
                    }

                    logger.info(I18N.logging_universalMessagingUtil_broadcastToMcServerProcessed(minecraftServer.serverName))
                }
            }

            val qqBot = QQBot.getBot()

            logger.info(I18N.logging_universalMessagingUtil_broadcastToQQBot())

            if (excludeServers.contains(qqBot)) {
                logger.info(I18N.logging_universalMessagingUtil_broadcastQQBotExcluded())
                return response
            }

            if (qqBot.checkOnlineStatus()) {
                val qqGroups = qqBot.getQQGroups()
                for (qqGroup in qqGroups) {
                    logger.info(I18N.logging_universalMessagingUtil_broadcastToQQGroup(qqGroup.groupName, qqGroup.groupCode))

                    val qqBotResponse = qqGroup.broadcast(message)
                    when (qqBotResponse.statusCode) {
                        200 -> {
                            logger.info(I18N.broadcastSentToGroup(qqGroup.groupName, qqGroup.groupCode))
                            response.responseCount++
                            response.responseList.add(
                                HTTPResponse(
                                    statusCode = HttpStatusCode.OK.value,
                                    body = I18N.broadcastSentToGroup(qqGroup.groupName, qqGroup.groupCode)
                                )
                            )
                        }

                        401 -> {
                            logger.warn(I18N.coreAuthkeyInvalid())
                            response.responseCount++
                            response.responseList.add(
                                HTTPResponse(
                                    statusCode = HttpStatusCode.Unauthorized.value,
                                    body = I18N.coreAuthkeyInvalid()
                                )
                            )
                        }

                        404 -> {
                            logger.warn(I18N.qqBotGroupNotFound(qqGroup.groupName, qqGroup.groupCode))
                            response.responseCount++
                            response.responseList.add(
                                HTTPResponse(
                                    statusCode = HttpStatusCode.NotFound.value,
                                    body = I18N.qqBotGroupNotFound(qqGroup.groupName, qqGroup.groupCode)
                                )
                            )
                        }

                        503 -> {
                            logger.warn(I18N.qqBotOfflineCannotSendGroup(qqGroup.groupName, qqGroup.groupCode))
                            response.responseCount++
                            response.responseList.add(
                                HTTPResponse(
                                    statusCode = HttpStatusCode.ServiceUnavailable.value,
                                    body = I18N.qqBotOfflineCannotSendGroup(qqGroup.groupName, qqGroup.groupCode)
                                )
                            )
                        }

                        else -> {
                            logger.warn(I18N.logging_universalMessagingUtil_qqBotUnknownError(qqBotResponse.statusCode, qqBotResponse.body))
                            response.responseCount++
                            response.responseList.add(
                                HTTPResponse(
                                    statusCode = HttpStatusCode.InternalServerError.value,
                                    body = I18N.unknownError(qqBotResponse.statusCode, qqBotResponse.body)
                                )
                            )
                        }
                    }

                    logger.info(I18N.logging_universalMessagingUtil_broadcastToQQGroupProcessed(qqGroup.groupName, qqGroup.groupCode))
                }
            } else {
                logger.warn(I18N.qqBotOffline())
                response.responseCount++
                response.responseList.add(
                    HTTPResponse(
                        statusCode = HttpStatusCode.ServiceUnavailable.value,
                        body = I18N.qqBotOffline()
                    )
                )
            }

            return response
        }
    }
}
