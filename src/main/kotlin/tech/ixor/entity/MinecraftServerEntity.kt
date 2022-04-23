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
        val statusCode: Int,
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
        val online = runBlocking { ping().statusCode == 200 }
        if (online != isOnline) {
            isOnline = online
        }
    }

    suspend fun ping(): ResponseEntity {
        val url = getUrl() + "/ping"
        val client = HttpClient(CIO)
        val response = Klaxon().parse<HTTPResponse>(
            try {
                client.get<String>(url) {
                    method = HttpMethod.Get
                }.toString()
            } catch (e: ConnectException) {
                return ResponseEntity(statusCode = 500, message = "Server is offline")
            }
        )

        return if (response != null) {
            if (response.statusCode == 200) {
                ResponseEntity(statusCode = 200, message = "OK")
            } else if (response.statusCode == 500) {
                ResponseEntity(statusCode = 500, message = "Server is offline")
            } else {
                ResponseEntity(statusCode = 500, message = "Unknown error")
            }
        } else {
            ResponseEntity(statusCode = 500, message = "Server is offline")
        }

    }

    suspend fun status(): ResponseEntity {
        val url = getUrl() + "/api/v1/mcserver/status"
        val client = HttpClient(CIO)
        val response = Klaxon().parse<HTTPResponse>(
            try {
                client.get<String>(url) {
                    method = HttpMethod.Get
                }.toString()
            } catch (e: ConnectException) {
                return ResponseEntity(statusCode = 500, message = "Server is offline")
            }
        )

        return if (response != null) {
            if (response.statusCode == 200) {
                ResponseEntity(statusCode = 200, message = response.message)
            } else if (response.statusCode == 401) {
                ResponseEntity(statusCode = 401, message = "Unauthorized")
            } else if (response.statusCode == 500) {
                ResponseEntity(statusCode = 500, message = "Server is offline")
            } else {
                ResponseEntity(statusCode = 500, message = "Unknown error")
            }
        } else {
            ResponseEntity(statusCode = 500, message = "Server is offline")
        }
    }

    suspend fun executeCommand(command: String): ResponseEntity {
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
                return ResponseEntity(statusCode = 500, message = "Server is offline")
            }
        )

        return if (response != null) {
            if (response.statusCode == 200) {
                val message = response.message
                if (message == "OK") {
                    ResponseEntity(statusCode = 200, message = "Command executed successfully!")
                } else {
                    ResponseEntity(statusCode = 200, message = message)
                }
            } else if (response.statusCode == 401) {
                ResponseEntity(statusCode = 401, message = "Unauthorized")
            } else if (response.statusCode == 500) {
                ResponseEntity(statusCode = 500, message = "Server is offline")
            } else {
                ResponseEntity(statusCode = 500, message = "Unknown error")
            }
        } else {
            ResponseEntity(statusCode = 500, message = "Server is offline")
        }
    }

    suspend fun say(source: String, sender: String, message: String): ResponseEntity {
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
                return ResponseEntity(statusCode = 500, message = "Server is offline")
            }
        )

        return if (response != null) {
            if (response.statusCode == 200) {
                ResponseEntity(statusCode = 200, message = "Message sent successfully!")
            } else if (response.statusCode == 401) {
                ResponseEntity(statusCode = 401, message = "Unauthorized")
            } else if (response.statusCode == 500) {
                ResponseEntity(statusCode = 500, message = "Server is offline")
            } else {
                ResponseEntity(statusCode = 500, message = "Unknown error")
            }
        } else {
            ResponseEntity(statusCode = 500, message = "Server is offline")
        }
    }

    suspend fun broadcast(message: String): ResponseEntity {
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
                return ResponseEntity(statusCode = 500, message = "Server is offline")
            }
        )

        return if (response != null) {
            if (response.statusCode == 200) {
                ResponseEntity(statusCode = 200, message = "Broadcast sent successfully!")
            } else if (response.statusCode == 401) {
                ResponseEntity(statusCode = 401, message = "Unauthorized")
            } else if (response.statusCode == 500) {
                ResponseEntity(statusCode = 500, message = "Server is offline")
            } else {
                ResponseEntity(statusCode = 500, message = "Unknown error")
            }
        } else {
            ResponseEntity(statusCode = 500, message = "Server is offline")
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
        for (server in servers) {
            if (server.default && server.isOnline) {
                return server
            }
        }
        return null
    }

    fun getOnlineServers(): List<MinecraftServerEntity> {
        return servers.filter { it.isOnline }
    }

    fun getOfflineServers(): List<MinecraftServerEntity> {
        return servers.filter { !it.isOnline }
    }

}
