package tech.ixor.entity

import com.beust.klaxon.Json
import com.beust.klaxon.Klaxon
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import java.net.ConnectException

class QQBotEntity constructor(val host: String, val port: Int, val ssl: Boolean,
                              val groupName: String, val groupCode: Long, val default: Boolean) {
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
        val response = Klaxon().parse<QQBotEntity.HTTPResponse>(
            try {
                client.get<String>(url) {
                    method = HttpMethod.Get
                }.toString()
            } catch (e: ConnectException) {
                return ResponseEntity(statusCode = 500, message = "The chat bot is offline")
            }
        )

        return if (response != null) {
            if (response.statusCode == 200) {
                ResponseEntity(statusCode = 200, message = "OK")
            } else if (response.statusCode == 500) {
                ResponseEntity(statusCode = 500, message = "The chat bot is offline")
            } else {
                ResponseEntity(statusCode = 500, message = "Unknown error")
            }
        } else {
            ResponseEntity(statusCode = 500, message = "The chat bot is offline")
        }
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

    fun getBot(host: String, port: Int): QQBotEntity? {
        return qqBots.find { it.host == host && it.port == port }
    }

    fun getDefaultBot(): QQBotEntity? {
        for (bot in qqBots) {
            if (bot.default && bot.isOnline) {
                return bot
            }
        }
        return null
    }

    fun getOnlineBots(): List<QQBotEntity> {
        return qqBots.filter { it.isOnline }
    }

    fun getOfflineBots(): List<QQBotEntity> {
        return qqBots.filter { !it.isOnline }
    }

}
