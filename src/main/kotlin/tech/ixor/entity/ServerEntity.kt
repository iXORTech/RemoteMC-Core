package tech.ixor.entity

import com.beust.klaxon.Klaxon
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import tech.ixor.I18N
import tech.ixor.utils.UniversalMessagingUtil
import java.net.ConnectException

open class ServerEntity constructor(val serverName: String, val host: String, val port: Int, val ssl: Boolean) {
    private var isOnline: Boolean = false
    protected val authKey = ConfigEntity().loadConfig().authKey
    private var logger = LoggerFactory.getLogger(javaClass)

    protected suspend fun getResponse(url: String): HTTPResponse {
        logger.info(I18N.logging_sendingGetRequest(url))
        val client = HttpClient(CIO)
        val response = Klaxon().parse<HTTPResponse>(
            try {
                client.get(url) {
                    method = HttpMethod.Get
                }
                    .body<String>().toString()
            } catch (e: ConnectException) {
                return HTTPResponse.get503(logger, serverName)
            }
        )
        return if (response != null) {
            logger.info(I18N.logging_responseFromUrl(url, response.toString()))
            logger.info(I18N.logging_sendingResponse(response.toString()))
            response
        } else {
            HTTPResponse.get503(logger, serverName)
        }
    }

    protected fun getUrl(): String {
        logger.info(I18N.logging_gettingUrl(serverName))
        return if (ssl) {
            "https://$host:$port"
        } else {
            "http://$host:$port"
        }
    }

    private suspend fun ping(): HTTPResponse {
        logger.info(I18N.logging_pingingServer(serverName))
        val url = getUrl() + "/ping"
        return getResponse(url)
    }

    fun updateOnlineStatus() {
        logger.info(I18N.logging_updatingOnlineStatus(serverName))
        val online = runBlocking { ping().statusCode == 200 }
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
