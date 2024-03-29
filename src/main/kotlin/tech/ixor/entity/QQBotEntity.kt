package tech.ixor.entity

import io.ktor.client.call.*
import io.ktor.http.*
import org.slf4j.LoggerFactory
import tech.ixor.I18N
import tech.ixor.utils.HttpUtil
import java.net.ConnectException

class QQBotEntity(
    host: String, port: Int, ssl: Boolean
) : ServerEntity(I18N.qqBotServerName(), host, port, ssl) {
    private val logger = LoggerFactory.getLogger(javaClass)

    inner class QQGroupEntity(
        val groupName: String, val groupCode: Long
    ) {
        private val authKey = ConfigEntity().loadConfig().authKey

        override fun toString(): String {
            return "QQGroupEntity(groupName='$groupName', groupCode=$groupCode)"
        }

        private suspend fun sendRequest(url: String): HTTPResponse {
            logger.info(I18N.logging_sendingRequestToUrl(url))

            val httpResponse: io.ktor.client.statement.HttpResponse = try {
                HttpUtil.PostRequests.request(url) {
                    method = HttpMethod.Post
                }
            } catch (e: ConnectException) {
                return HTTPResponse.get503(logger, serverName)
            }

            val responseContent = HTTPResponse(
                httpResponse.status.value,
                httpResponse.body<String>().toString()
            )

            logger.info(I18N.logging_responseFromUrl(url, responseContent.toString()))
            logger.info(I18N.logging_returningResponse(responseContent.toString()))
            return responseContent
        }

        suspend fun sendMessage(senderID: String, source: String, sender: String, message: String): HTTPResponse {
            logger.info(I18N.logging_qqGroupSendMessageRequestReceived(groupName, groupCode))
            logger.info(I18N.logging_qqGroupSendMessageRequestParams(senderID, source, sender, message))
            if (!checkOnlineStatus()) {
                return HTTPResponse.get503(logger, serverName)
            }
            val url = getUrl() +
                    "/groupMessage?authKey=$authKey&group=$groupCode" +
                    "&sender_id=$senderID&source=$source&sender=$sender&message=$message"
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
