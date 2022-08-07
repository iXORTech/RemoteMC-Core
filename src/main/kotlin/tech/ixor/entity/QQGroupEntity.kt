package tech.ixor.entity

import com.beust.klaxon.Klaxon
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import java.net.ConnectException

class QQGroupEntity constructor(
    val groupName: String, val groupCode: Long
) {
    private val authKey = ConfigEntity().loadConfig().authKey

    suspend fun sendMessage(qqBot: QQBotEntity, senderID: String, source: String, sender: String, message: String): HTTPResponse {
        if (!qqBot.checkOnlineStatus()) {
            return HTTPResponse(statusCode = 503, message = "SERVICE_UNAVAILABLE")
        }
        val url = qqBot.getUrl() +
                "/groupMessage?authKey=$authKey&group=$groupCode&sender_id=$senderID&source=$source&sender=$sender&message=$message"
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

    suspend fun broadcast(qqBot: QQBotEntity, message: String): HTTPResponse {
        if (!qqBot.checkOnlineStatus()) {
            return HTTPResponse(statusCode = 503, message = "SERVICE_UNAVAILABLE")
        }
        val url = qqBot.getUrl() +
                "/groupBroadcast?authKey=$authKey&group=$groupCode&message=$message"
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

object QQGroups {
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
