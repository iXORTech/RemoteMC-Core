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

    override fun toString(): String {
        return "MinecraftServerEntity(serverName='$serverName', host='$host', port=$port, ssl=$ssl, default=$default)"
    }

    suspend fun status(): HTTPResponse {
        logger.info(I18N.logging_mcServerStatusRequestReceived(serverName))
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
        logger.info(I18N.logging_mcServerExecuteCommandRequestReceived(serverName, command))
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
        logger.info(I18N.logging_mcServerSendMessageRequestReceived(serverName))
        logger.info(I18N.logging_mcServerSendMessageRequestParams(senderID, source, sender, message))
        if (!checkOnlineStatus()) {
            return HTTPResponse.get503(logger, serverName)
        }
        val url = getUrl() + "/api/v1/mcserver/send_message"
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
        logger.info(I18N.logging_mcServerBroadcastRequestReceived(serverName, message))
        if (!checkOnlineStatus()) {
            return HTTPResponse.get503(logger, serverName)
        }
        val url = getUrl() + "/api/v1/mcserver/broadcast"
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
    private var logger = LoggerFactory.getLogger(javaClass)

    fun addServer(server: MinecraftServerEntity) {
        logger.info(I18N.logging_addingMinecraftServer(server.toString()))
        servers.add(server)
    }

    fun addServer(serverConfig: ConfigEntity.MinecraftServerConfig) {
        val server = MinecraftServerEntity(
            serverConfig.serverName,
            serverConfig.host,
            serverConfig.port,
            serverConfig.ssl,
            serverConfig.default
        )
        logger.info(I18N.logging_addingMinecraftServer(server.toString()))
        servers.add(server)
    }

    fun addServer(serverName: String, host: String, port: Int, ssl: Boolean, default: Boolean) {
        val server = MinecraftServerEntity(
            serverName,
            host,
            port,
            ssl,
            default
        )
        logger.info(I18N.logging_addingMinecraftServer(server.toString()))
        servers.add(server)
    }

    fun getAllServers(): List<MinecraftServerEntity> {
        logger.info(I18N.logging_gettingAllMinecraftServers())
        return servers
    }

    fun getServer(serverName: String): MinecraftServerEntity? {
        logger.info(I18N.logging_gettingMinecraftServerAccordingToName(serverName))
        return servers.find { it.serverName == serverName }
    }

    fun getServer(host: String, port: Int): MinecraftServerEntity? {
        logger.info(I18N.logging_gettingMinecraftServerAccordingToHostAndPort(host, port))
        return servers.find { it.host == host && it.port == port }
    }

    fun getDefaultServer(): MinecraftServerEntity? {
        logger.info(I18N.logging_gettingDefaultMinecraftServer())
        return servers.find { it.default }
    }

    fun getOnlineServers(): List<MinecraftServerEntity> {
        logger.info(I18N.logging_gettingOnlineMinecraftServers())
        for (server in servers) {
            server.updateOnlineStatus()
        }
        return servers.filter { it.checkOnlineStatus() }
    }

    fun getOfflineServers(): List<MinecraftServerEntity> {
        logger.info(I18N.logging_gettingOfflineMinecraftServers())
        for (server in servers) {
            server.updateOnlineStatus()
        }
        return servers.filter { !it.checkOnlineStatus() }
    }
}
