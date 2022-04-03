package tech.ixor.entity

import com.beust.klaxon.Json
import com.beust.klaxon.Klaxon
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import java.net.ConnectException

class MinecraftServerEntity constructor(val serverName: String, val host: String, val port: Int,
                                        val ssl: Boolean, val default: Boolean) {
    class HTTPResponse(
        @Json(name = "status_code")
        val status: Int,
        val message: String
    )

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
        val online = runBlocking { ping() == 200 }
        if (online != isOnline) {
            isOnline = online
        }
    }

    suspend fun ping(): Int {
        val url = getUrl() + "/ping"
        val client = HttpClient(CIO)
        val response = Klaxon().parse<HTTPResponse>(
            try {
                client.get<String>(url) {
                    method = HttpMethod.Get
                }.toString()
            } catch (e: ConnectException) {
                return 502
            }
        )

        return response?.status ?: 502

    }

    suspend fun executeCommand(command: String): String {
        val url = getUrl() + "/api/v1/mcserver/execute_command"
        val client = HttpClient(CIO)
        val response = Klaxon().parse<HTTPResponse>(
            try {
                client.post<String>(url) {
                    contentType(ContentType.Application.Json)
                    body = Klaxon().toJsonString(
                        mapOf(
                            "auth_key" to authKey,
                            "command" to command
                        )
                    )
                }.toString()
            } catch (e: ConnectException) {
                return "Server is offline"
            }
        )

        val status = response?.status ?: 502
        return if (status == 200) {
            val message = response?.message ?: "OK"
            if (message == "OK") {
                "Command executed successfully!"
            } else {
                message
            }
        } else if (status == 401) {
            "Invalid auth key"
        } else if (status == 502) {
            "Server is offline"
        } else {
            "Unknown error"
        }
    }

    suspend fun say(source: String, sender: String, message: String): String {
        val url = getUrl() + "/api/v1/mcserver/say"
        val client = HttpClient(CIO)
        val response = Klaxon().parse<HTTPResponse>(
            try {
                client.post<String>(url) {
                    contentType(ContentType.Application.Json)
                    body = Klaxon().toJsonString(
                        mapOf(
                            "auth_key" to authKey,
                            "source" to source,
                            "sender" to sender,
                            "message" to message
                        )
                    )
                }.toString()
            } catch (e: ConnectException) {
                return "Server is offline"
            }
        )

        val status = response?.status ?: 502
        return if (status == 200) {
            "Message sent successfully!"
        } else if (status == 401) {
            "Invalid auth key"
        } else if (status == 502) {
            "Server is offline"
        } else {
            "Unknown error"
        }
    }

    suspend fun broadcast(message: String): String {
        val url = getUrl() + "/api/v1/mcserver/broadcast"
        val client = HttpClient(CIO)
        val response = Klaxon().parse<HTTPResponse>(
            try {
                client.post<String>(url) {
                    contentType(ContentType.Application.Json)
                    body = Klaxon().toJsonString(
                        mapOf(
                            "auth_key" to authKey,
                            "message" to message
                        )
                    )
                }.toString()
            } catch (e: ConnectException) {
                return "Server is offline"
            }
        )

        val status = response?.status ?: 502
        return if (status == 200) {
            "Broadcast sent successfully!"
        } else if (status == 401) {
            "Invalid auth key"
        } else if (status == 502) {
            "Server is offline"
        } else {
            "Unknown error"
        }
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
        return servers.find { it.isOnline && it.default }
    }

    fun getOnlineServers(): List<MinecraftServerEntity> {
        return servers.filter { it.isOnline }
    }

    fun getOfflineServers(): List<MinecraftServerEntity> {
        return servers.filter { !it.isOnline }
    }

}
