package tech.ixor.entity

import com.beust.klaxon.Klaxon
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import java.net.ConnectException

class QQBotEntity constructor(
    host: String, port: Int, ssl: Boolean,
    val groupName: String, val groupCode: Long, val default: Boolean
): ServerEntity(host, port, ssl) {
    suspend fun sendMessage(source: String, sender: String, message: String): HTTPResponse {
        if (!checkOnlineStatus()) {
            return HTTPResponse(statusCode = 503, message = "SERVICE_UNAVAILABLE")
        }
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

    fun getBot(host: String, port: Int): QQBotEntity? {
        return qqBots.find { it.host == host && it.port == port }
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
