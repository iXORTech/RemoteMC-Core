package tech.ixor.entity

import com.beust.klaxon.Klaxon
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import java.net.ConnectException

class QQBotEntity constructor(
    host: String, port: Int, ssl: Boolean
) : ServerEntity(host, port, ssl) {
    suspend fun sendMessage(groupCode: Long, source: String, sender: String, message: String): HTTPResponse {
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

object QQBot {
    private lateinit var qqBot: QQBotEntity

    fun setBot(bot: QQBotEntity) {
        qqBot = bot
    }

    fun setBot(bot: ConfigEntity.QQBotConfig) {
        qqBot = QQBotEntity(bot.host, bot.port, bot.ssl)
    }

    fun setBot(host: String, port: Int, ssl: Boolean) {
        qqBot = QQBotEntity(host, port, ssl)
    }

    fun getBot(): QQBotEntity {
        return qqBot
    }
}
