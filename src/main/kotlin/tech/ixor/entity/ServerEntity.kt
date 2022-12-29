package tech.ixor.entity

import com.beust.klaxon.Klaxon
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import tech.ixor.I18N
import tech.ixor.utils.CompatibilityUtil
import tech.ixor.utils.UniversalMessagingUtil
import java.net.ConnectException

open class ServerEntity constructor(val serverName: String, val host: String, val port: Int, val ssl: Boolean) {
    private var isOnline: Boolean = false
    private var isCompatible: Boolean = false
    protected val authKey = ConfigEntity().loadConfig().authKey
    private val logger = LoggerFactory.getLogger(javaClass)

    protected suspend fun getResponse(url: String): HTTPResponse {
        logger.info(I18N.logging_sendingGetRequest(url))
        val client = HttpClient(CIO)
        val httpResponse: io.ktor.client.statement.HttpResponse = try {
            client.get(url)
        } catch (e: ConnectException) {
            return HTTPResponse.get503(logger, serverName)
        }
        println("Response: ${httpResponse.status.description}")
        val responseContent = HTTPResponse(
            httpResponse.status.value,
            httpResponse.body<String>().toString()
        )
        logger.info(I18N.logging_responseFromUrl(url, responseContent.toString()))
        logger.info(I18N.logging_returningResponse(responseContent.toString()))
        return responseContent
    }

    protected fun getUrl(): String {
        logger.info(I18N.logging_gettingUrl(serverName))
        return if (ssl) {
            "https://$host:$port"
        } else {
            "http://$host:$port"
        }
    }

    private suspend fun ping(): Int {
        logger.info(I18N.logging_pingingServer(serverName))
        val url = getUrl() + "/ping"
        val response = getResponse(url)
        if (response.statusCode == 200) {
            val responseContent = Klaxon().parse<PingResponse>(response.body)
            isCompatible = CompatibilityUtil().checkComaptibility(
                responseContent?.module ?: "undefined",
                responseContent?.version ?: "undefined",
                responseContent?.stage ?: "undefined"
            )
        }
        return response.statusCode
    }

    fun updateOnlineStatus() {
        logger.info(I18N.logging_updatingOnlineStatus(serverName))
        val online = runBlocking { ping() == 200 }
        if (online != isOnline) {
            logger.info(I18N.logging_onlineStatusChanged(serverName))
            isOnline = online
            val message = if (isOnline) {
                logger.info(I18N.logging_broadcastOnlineMessage(serverName))
                I18N.serverOnlineBroadcast(serverName)
            } else {
                logger.info(I18N.logging_broadcastOfflineMessage(serverName))
                I18N.serverOfflineBroadcast(serverName)
            }
            val excludeServers: List<ServerEntity> = listOf(this)
            runBlocking { UniversalMessagingUtil.broadcast(message, excludeServers) }
        }
    }

    fun checkOnlineStatus(): Boolean {
        logger.info(I18N.logging_checkingOnlineStatus(serverName))
        updateOnlineStatus()
        return isOnline
    }
}
