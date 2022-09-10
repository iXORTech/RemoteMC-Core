package tech.ixor.entity

import com.beust.klaxon.Klaxon
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.slf4j.LoggerFactory
import tech.ixor.I18N
import java.net.ConnectException

class MinecraftServerEntity constructor(
    serverName: String, host: String, port: Int,
    ssl: Boolean, val default: Boolean
) : ServerEntity(serverName, host, port, ssl) {
    private var logger = LoggerFactory.getLogger(javaClass)

    suspend fun status(): HTTPResponse {
        logger.info(I18N.logging_statusRequestReceived(serverName))
        if (!checkOnlineStatus()) {
            return HTTPResponse.get503(logger, serverName)
        }
        val url = getUrl() + "/api/v1/mcserver/status"
        logger.info(I18N.logging_sendingRequestToUrl(url))
        val response = getResponse(url)
        logger.info(I18N.logging_responseFromUrl(url, response.toString()))
        logger.info(I18N.logging_sendingResponse(response.toString()))
        return response
    }

    suspend fun executeCommand(command: String): HTTPResponse {
        logger.info(I18N.logging_executeCommandRequestReceived(serverName, command))
        if (!checkOnlineStatus()) {
            return HTTPResponse.get503(logger, serverName)
        }
        val url = getUrl() + "/api/v1/mcserver/execute_command"
        logger.info(I18N.logging_sendingRequestToUrl(url))
        val client = HttpClient(CIO)
        val response = Klaxon().parse<HTTPResponse>(
            try {
                client.post(url) {
                    contentType(ContentType.Application.Json)
                    setBody(
                        Klaxon().toJsonString(
                            mapOf(
                                "auth_key" to authKey,
                                "command" to command
                            )
                        )
                    )
                }
                    .body<String>().toString()
            } catch (e: ConnectException) {
                return HTTPResponse.get503(logger, serverName)
            }
        )

        return if (response != null) {
            logger.info(I18N.logging_responseFromUrl(url, response.toString()))
            logger.info(I18N.logging_sendingResponse(response.toString()))
            response
        } else {
            HTTPResponse.get503(logger, serverName)
        }
    }

    suspend fun sendMessage(senderID: String, source: String, sender: String, message: String): HTTPResponse {
        logger.info(I18N.logging_sendMessageRequestReceived(serverName))
        logger.info(I18N.logging_sendMessageRequestParams(senderID, source, sender, message))
        if (!checkOnlineStatus()) {
            return HTTPResponse.get503(logger, serverName)
        }
        val url = getUrl() + "/api/v1/mcserver/send_message"
        logger.info(I18N.logging_sendingRequestToUrl(url))
        val client = HttpClient(CIO)
        var response = Klaxon().parse<HTTPResponse>(
            try {
                client.post(url) {
                    contentType(ContentType.Application.Json)
                    setBody(
                        Klaxon().toJsonString(
                            mapOf(
                                "auth_key" to authKey,
                                "sender_id" to senderID,
                                "source" to source,
                                "sender" to sender,
                                "message" to message
                            )
                        )
                    )
                }
                    .body<String>().toString()
            } catch (e: ConnectException) {
                return HTTPResponse.get503(logger, serverName)
            }
        )

        return if (response != null) {
            logger.info(I18N.logging_responseFromUrl(url, response.toString()))
            logger.info(I18N.logging_sendingResponse(response.toString()))
            response
        } else {
            HTTPResponse.get503(logger, serverName)
        }
    }

    suspend fun broadcast(message: String): HTTPResponse {
        logger.info(I18N.logging_broadcastRequestReceived(serverName, message))
        if (!checkOnlineStatus()) {
            return HTTPResponse.get503(logger, serverName)
        }
        val url = getUrl() + "/api/v1/mcserver/broadcast"
        logger.info(I18N.logging_sendingRequestToUrl(url))
        val client = HttpClient(CIO)
        var response = Klaxon().parse<HTTPResponse>(
            try {
                client.post(url) {
                    contentType(ContentType.Application.Json)
                    setBody(
                        Klaxon().toJsonString(
                            mapOf(
                                "auth_key" to authKey,
                                "message" to message
                            )
                        )
                    )
                }
                    .body<String>().toString()
            } catch (e: ConnectException) {
                return HTTPResponse.get503(logger, serverName)
            }
        )

        return if (response != null) {
            logger.info(I18N.logging_responseFromUrl(url, response.toString()))
            logger.info(I18N.logging_sendingResponse(response.toString()))
            response
        } else {
            HTTPResponse.get503(logger, serverName)
        }
    }
}

object MinecraftServers {
    private val servers = mutableListOf<MinecraftServerEntity>()

    fun addServer(server: MinecraftServerEntity) {
        servers.add(server)
    }

    fun addServer(server: ConfigEntity.MinecraftServerConfig) {
        servers.add(MinecraftServerEntity(server.serverName, server.host, server.port, server.ssl, server.default))
    }

    fun addServer(serverName: String, host: String, port: Int, ssl: Boolean, default: Boolean) {
        servers.add(MinecraftServerEntity(serverName, host, port, ssl, default))
    }

    fun getAllServers(): List<MinecraftServerEntity> {
        return servers
    }

    fun getServer(serverName: String): MinecraftServerEntity? {
        return servers.find { it.serverName == serverName }
    }

    fun getServer(host: String, port: Int): MinecraftServerEntity? {
        return servers.find { it.host == host && it.port == port }
    }

    fun getDefaultServer(): MinecraftServerEntity? {
        return servers.find { it.default }
    }

    fun getOnlineServers(): List<MinecraftServerEntity> {
        for (server in servers) {
            server.updateOnlineStatus()
        }
        return servers.filter { it.isOnline }
    }

    fun getOfflineServers(): List<MinecraftServerEntity> {
        for (server in servers) {
            server.updateOnlineStatus()
        }
        return servers.filter { !it.isOnline }
    }
}
