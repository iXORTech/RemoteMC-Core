package tech.ixor.entity

import com.beust.klaxon.Json

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
