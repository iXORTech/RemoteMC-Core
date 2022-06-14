package tech.ixor.entity

import com.beust.klaxon.Klaxon
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import java.net.ConnectException

class MinecraftServerEntity constructor(
    val serverName: String, val host: String, val port: Int,
    val ssl: Boolean, val default: Boolean
) {

    private val authKey = ConfigEntity().loadConfig().authKey

    var isOnline: Boolean = false

    private fun getUrl(): String {
        return if (ssl) {
            "https://$host:$port"
        } else {
            "http://$host:$port"
        }
    }

    fun updateOnlineStatus() {
        val online = runBlocking { ping().statusCode == 200 }
        if (online != isOnline) {
            isOnline = online
        }
    }

    fun checkOnlineStatus(): Boolean {
        updateOnlineStatus()
        return isOnline
    }

    suspend fun ping(): HTTPResponse {
        val url = getUrl() + "/ping"
        val client = HttpClient(CIO)
        val response = Klaxon().parse<HTTPResponse>(
            try {
                client.get(url) {
                    method = HttpMethod.Get
                }
                    .body<String>().toString()
            } catch (e: ConnectException) {
                return HTTPResponse(statusCode = 503, message = "SERVICE_UNAVAILABLE")
            }
        )

        return response ?: HTTPResponse(statusCode = 503, message = "SERVICE_UNAVAILABLE")
    }

    suspend fun status(): HTTPResponse {
        if (!checkOnlineStatus()) {
            return HTTPResponse(statusCode = 503, message = "SERVICE_UNAVAILABLE")
        }
        val url = getUrl() + "/api/v1/mcserver/status"
        val client = HttpClient(CIO)
        val response = Klaxon().parse<HTTPResponse>(
            try {
                client.get(url) {
                    method = HttpMethod.Get
                }
                    .body<String>().toString()
            } catch (e: ConnectException) {
                return HTTPResponse(statusCode = 503, message = "SERVICE_UNAVAILABLE")
            }
        )

        return response ?: HTTPResponse(statusCode = 503, message = "SERVICE_UNAVAILABLE")
    }

    suspend fun executeCommand(command: String): HTTPResponse {
        if (!checkOnlineStatus()) {
            return HTTPResponse(statusCode = 503, message = "SERVICE_UNAVAILABLE")
        }
        val url = getUrl() + "/api/v1/mcserver/execute_command"
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
                return HTTPResponse(statusCode = 503, message = "SERVICE_UNAVAILABLE")
            }
        )

        return response ?: HTTPResponse(statusCode = 503, message = "SERVICE_UNAVAILABLE")
    }

    suspend fun say(source: String, sender: String, message: String): HTTPResponse {
        if (!checkOnlineStatus()) {
            return HTTPResponse(statusCode = 503, message = "SERVICE_UNAVAILABLE")
        }
        val url = getUrl() + "/api/v1/mcserver/say"
        val client = HttpClient(CIO)
        val response = Klaxon().parse<HTTPResponse>(
            try {
                client.post(url) {
                    contentType(ContentType.Application.Json)
                    setBody(
                        Klaxon().toJsonString(
                            mapOf(
                                "auth_key" to authKey,
                                "source" to source,
                                "sender" to sender,
                                "message" to message
                            )
                        )
                    )
                }
                    .body<String>().toString()
            } catch (e: ConnectException) {
                return HTTPResponse(statusCode = 503, message = "SERVICE_UNAVAILABLE")
            }
        )

        return response ?: HTTPResponse(statusCode = 503, message = "SERVICE_UNAVAILABLE")
    }

    suspend fun broadcast(message: String): HTTPResponse {
        if (!checkOnlineStatus()) {
            return HTTPResponse(statusCode = 503, message = "SERVICE_UNAVAILABLE")
        }
        val url = getUrl() + "/api/v1/mcserver/broadcast"
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
                return HTTPResponse(statusCode = 503, message = "SERVICE_UNAVAILABLE")
            }
        )

        return response ?: HTTPResponse(statusCode = 503, message = "SERVICE_UNAVAILABLE")
    }

}

object MinecraftServers {
    private val servers = mutableListOf<MinecraftServerEntity>()

    fun addServer(server: MinecraftServerEntity) {
        servers.add(server)
    }

    fun addServer(server: ConfigEntity.MinecraftServer) {
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
        return servers.find { it.default && it.isOnline }
    }

    fun getOnlineServers(): List<MinecraftServerEntity> {
        return servers.filter { it.isOnline }
    }

    fun getOfflineServers(): List<MinecraftServerEntity> {
        return servers.filter { !it.isOnline }
    }

}
