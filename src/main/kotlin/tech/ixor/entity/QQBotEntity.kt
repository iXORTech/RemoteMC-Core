package tech.ixor.entity

import com.beust.klaxon.Klaxon
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import tech.ixor.I18N
import java.net.ConnectException

class QQBotEntity constructor(
    host: String, port: Int, ssl: Boolean
) : ServerEntity(I18N.qqBotServerName(), host, port, ssl) {
    inner class QQGroupEntity constructor(
        val groupName: String, val groupCode: Long
    ) {
        private val authKey = ConfigEntity().loadConfig().authKey

        private suspend fun sendRequest(url: String): HTTPResponse {
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

        suspend fun sendMessage(senderID: String, source: String, sender: String, message: String): HTTPResponse {
            if (!checkOnlineStatus()) {
                return HTTPResponse(statusCode = 503, message = "SERVICE_UNAVAILABLE")
            }
            val url = getUrl() +
                    "/groupMessage?authKey=$authKey&group=$groupCode&sender_id=$senderID&source=$source&sender=$sender&message=$message"
            return sendRequest(url)
        }

        suspend fun broadcast(message: String): HTTPResponse {
            if (!checkOnlineStatus()) {
                return HTTPResponse(statusCode = 503, message = "SERVICE_UNAVAILABLE")
            }
            val url = getUrl() +
                    "/groupBroadcast?authKey=$authKey&group=$groupCode&message=$message"
            return sendRequest(url)
        }
    }

    private val qqGroups = mutableListOf<QQGroupEntity>()

    fun addQQGroup(qqGroup: QQGroupEntity) {
        qqGroups.add(qqGroup)
    }

    fun addQQGroup(qqGroup: ConfigEntity.QQGroupConfig) {
        qqGroups.add(QQGroupEntity(qqGroup.groupName, qqGroup.groupCode))
    }

    fun addQQGroup(groupName: String, groupCode: Long) {
        qqGroups.add(QQGroupEntity(groupName, groupCode))
    }

    fun getQQGroups(): List<QQGroupEntity> {
        return qqGroups
    }

    fun getQQGroup(groupCode: Long): QQGroupEntity? {
        return qqGroups.find { it.groupCode == groupCode }
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
