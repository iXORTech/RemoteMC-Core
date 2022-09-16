package tech.ixor.entity

import tech.ixor.I18N

class QQBotEntity constructor(
    host: String, port: Int, ssl: Boolean
) : ServerEntity(I18N.qqBotServerName(), host, port, ssl)

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
