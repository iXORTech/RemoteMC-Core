package tech.ixor.entity

import com.beust.klaxon.Json
import com.beust.klaxon.Klaxon
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*

class MinecraftServerEntity constructor(val serverName: String, val host: String, val port: Int, val ssl: Boolean, val default: Boolean) {
    class HTTPResponse(
        @Json(name = "status_code")
        val status: Int,
        val message: String
    )

    var isOnline: Boolean = false

    fun switchOnline() {
        isOnline = !isOnline
    }

    suspend fun ping(): Int {
        val url = if (ssl) "https://$host:$port/ping" else "http://$host:$port/ping"
        val client = HttpClient(CIO)
        val response = Klaxon().parse<HTTPResponse>(
            client.get<String>(url) {
                method = HttpMethod.Get
            }.toString()
        )

        if (response != null) {
            return response.status
        } else {
            return 502
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
