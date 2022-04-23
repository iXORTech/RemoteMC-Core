package tech.ixor.entity

class QQBotEntity constructor(val host: String, val port: Int, val ssl: Boolean,
                              val groupName: String, val groupCode: Long, val default: Boolean) {
    private val authKey = ConfigEntity().loadConfig().authKey

    var isOnline: Boolean = false

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
        return qqBots.find { it.default }
    }

    fun getOnlineBots(): QQBotEntity? {
        return qqBots.find { it.isOnline }
    }

    fun getOfflineBots(): QQBotEntity? {
        return qqBots.find { !it.isOnline }
    }

}
