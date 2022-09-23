package tech.ixor.entity

import com.beust.klaxon.Klaxon
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.slf4j.LoggerFactory
import tech.ixor.I18N
import java.net.ConnectException

class QQBotEntity constructor(
    host: String, port: Int, ssl: Boolean
) : ServerEntity(I18N.qqBotServerName(), host, port, ssl) {
    private val logger = LoggerFactory.getLogger(javaClass)

    inner class QQGroupEntity constructor(
        val groupName: String, val groupCode: Long
    ) {
        private val authKey = ConfigEntity().loadConfig().authKey

        override fun toString(): String {
            return "QQGroupEntity(groupName='$groupName', groupCode=$groupCode)"
        }

        private suspend fun sendRequest(url: String): HTTPResponse {
            logger.info(I18N.logging_sendingRequestToUrl(url))
            val client = HttpClient(CIO)
            val response = Klaxon().parse<HTTPResponse>(
                try {
                    client.post(url) {
                        method = HttpMethod.Post
                    }
                        .body<String>().toString()
                } catch (e: ConnectException) {
                    return HTTPResponse.get503(logger, serverName)
                }
            )

            return if (response != null) {
                logger.info(I18N.logging_responseFromUrl(url, response.toString()))
                logger.info(I18N.logging_returningResponse(response.toString()))
                response
            } else {
                HTTPResponse.get503(logger, serverName)
            }
        }

        suspend fun sendMessage(senderID: String, source: String, sender: String, message: String): HTTPResponse {
            logger.info(I18N.logging_qqGroupSendMessageRequestReceived(groupName, groupCode))
            logger.info(I18N.logging_qqGroupSendMessageRequestParams(senderID, source, sender, message))
            if (!checkOnlineStatus()) {
                return HTTPResponse.get503(logger, serverName)
            }
            val url = getUrl() +
                    "/groupMessage?authKey=$authKey&group=$groupCode&sender_id=$senderID&source=$source&sender=$sender&message=$message"
            return sendRequest(url)
        }

        suspend fun broadcast(message: String): HTTPResponse {
            logger.info(I18N.logging_qqGroupBroadcastRequestReceived(groupName, groupCode, message))
            if (!checkOnlineStatus()) {
                return HTTPResponse.get503(logger, serverName)
            }
            val url = getUrl() +
                    "/groupBroadcast?authKey=$authKey&group=$groupCode&message=$message"
            return sendRequest(url)
        }
    }

    private val qqGroups = mutableListOf<QQGroupEntity>()

    fun addQQGroup(qqGroup: QQGroupEntity) {
        logger.info(I18N.logging_addingQQGroup(qqGroup.toString()))
        qqGroups.add(qqGroup)
    }

    fun addQQGroup(qqGroup: ConfigEntity.QQGroupConfig) {
        val group = QQGroupEntity(qqGroup.groupName, qqGroup.groupCode)
        logger.info(I18N.logging_addingQQGroup(group.toString()))
        qqGroups.add(group)
    }

    fun addQQGroup(groupName: String, groupCode: Long) {
        val group = QQGroupEntity(groupName, groupCode)
        logger.info(I18N.logging_addingQQGroup(group.toString()))
        qqGroups.add(group)
    }

    fun getQQGroups(): List<QQGroupEntity> {
        logger.info(I18N.logging_gettingAllQQGroups())
        return qqGroups
    }

    fun getQQGroup(groupCode: Long): QQGroupEntity? {
        logger.info(I18N.logging_gettingQQGroupAccordingToCode(groupCode))
        return qqGroups.find { it.groupCode == groupCode }
    }
}

object QQBot {
    private lateinit var qqBot: QQBotEntity
    private val logger = LoggerFactory.getLogger(javaClass)

    fun setBot(bot: QQBotEntity) {
        if (bot.ssl) {
            logger.info(I18N.logging_settingQQBot(bot.host, bot.port, I18N.enabled()))
        } else {
            logger.info(I18N.logging_settingQQBot(bot.host, bot.port, I18N.disabled()))
        }
        qqBot = bot
    }

    fun setBot(botConfig: ConfigEntity.QQBotConfig) {
        val bot = QQBotEntity(botConfig.host, botConfig.port, botConfig.ssl)
        if (bot.ssl) {
            logger.info(I18N.logging_settingQQBot(bot.host, bot.port, I18N.enabled()))
        } else {
            logger.info(I18N.logging_settingQQBot(bot.host, bot.port, I18N.disabled()))
        }
        qqBot = bot
    }

    fun setBot(host: String, port: Int, ssl: Boolean) {
        val bot = QQBotEntity(host, port, ssl)
        if (bot.ssl) {
            logger.info(I18N.logging_settingQQBot(bot.host, bot.port, I18N.enabled()))
        } else {
            logger.info(I18N.logging_settingQQBot(bot.host, bot.port, I18N.disabled()))
        }
        qqBot = bot
    }

    fun getBot(): QQBotEntity {
        logger.info(I18N.logging_gettingQQBot())
        return qqBot
    }
}
