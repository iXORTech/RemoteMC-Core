package tech.ixor.entity

import com.beust.klaxon.Klaxon
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import java.net.ConnectException

open class ServerEntity constructor(val serverName: String, val host: String, val port: Int, val ssl: Boolean) {
    var isOnline: Boolean = false

    protected val authKey = ConfigEntity().loadConfig().authKey

    protected suspend fun getResponse(url: String): HTTPResponse {
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

    protected open fun getUrl(): String {
        return if (ssl) {
            "https://$host:$port"
        } else {
            "http://$host:$port"
        }
    }

    private suspend fun ping(): HTTPResponse {
        val url = getUrl() + "/ping"
        return getResponse(url)
    }

    fun updateOnlineStatus() {
        val online = runBlocking { ping().statusCode == 200 }
        if (online != isOnline) {
            isOnline = online
        }
    }

    protected open fun checkOnlineStatus(): Boolean {
        updateOnlineStatus()
        return isOnline
    }
}
