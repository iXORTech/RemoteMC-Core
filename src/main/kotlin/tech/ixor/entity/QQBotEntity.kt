package tech.ixor.entity

import com.beust.klaxon.Json
import com.beust.klaxon.Klaxon
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import java.net.ConnectException

class QQBotEntity constructor(
    val host: String, val port: Int, val ssl: Boolean,
    val groupName: String, val groupCode: Long, val default: Boolean
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

    suspend fun sendMessage(source: String, sender: String, message: String): HTTPResponse {
        val url =
            getUrl() + "/groupMessage?authKey=$authKey&group=$groupCode&source=$source&sender=$sender&message=$message"
        val client = HttpClient(CIO)
        val response = Klaxon().parse<HTTPResponse>(
            try {
                client.post(url) {
                    method = HttpMethod.Post
                }
                    .body<String>().toString()
            } catch (e: ConnectException) {
                return HTTPResponse(statusCode = 503, message = "SERVICE_UNAVAILABLE")
            }
        )

        return response ?: HTTPResponse(statusCode = 503, message = "SERVICE_UNAVAILABLE")
    }

}

object QQBots {
    private val qqBots = mutableListOf<QQBotEntity>()

    fun addBot(bot: QQBotEntity) {
        qqBots.add(bot)
    }

    fun addBot(bot: ConfigEntity.QQBot) {
        qqBots.add(QQBotEntity(bot.host, bot.port, bot.ssl, bot.groupName, bot.groupCode, bot.default))
    }

    fun addBot(host: String, port: Int, ssl: Boolean, groupName: String, groupCode: Long, default: Boolean) {
        qqBots.add(QQBotEntity(host, port, ssl, groupName, groupCode, default))
    }

    fun getAllBots(): List<QQBotEntity> {
        return qqBots
    }

    fun getBot(groupCode: Long): QQBotEntity? {
        return qqBots.find { it.groupCode == groupCode }
    }

    fun getDefaultBot(): QQBotEntity? {
        return qqBots.find { it.default && it.isOnline }
    }

    fun getOnlineBots(): List<QQBotEntity> {
        return qqBots.filter { it.isOnline }
    }

    fun getOfflineBots(): List<QQBotEntity> {
        return qqBots.filter { !it.isOnline }
    }

}
