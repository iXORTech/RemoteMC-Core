package tech.ixor.entity

class QQBotEntity constructor(
    host: String, port: Int, ssl: Boolean
) : ServerEntity(host, port, ssl) {
    public override fun getUrl(): String {
        return if (ssl) {
            "https://$host:$port"
        } else {
            "http://$host:$port"
        }
    }

    public override fun checkOnlineStatus(): Boolean {
        updateOnlineStatus()
        return isOnline
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
