package tech.ixor.routes.controller.mcserver

import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory
import tech.ixor.I18N
import tech.ixor.entity.MinecraftServerEntity
import tech.ixor.entity.MinecraftServers

fun Route.mcServerStatus() {
    val logger = LoggerFactory.getLogger(javaClass)

    get("/mcserver/status") {
        logger.info(I18N.logging_mcserver_statusRequestReceived())

        val request = call.receive<MCServerStatusRequest>()

        val host: String
        val port: Int

        if (request.host != null || request.port != null) {
            host = request.host!!
            port = request.port!!
        } else {
            logger.warn(I18N.logging_mcserver_fallbackToDefaultServer())
            host = MinecraftServers.getDefaultServer()?.host!!
            port = MinecraftServers.getDefaultServer()?.port!!
        }

        logger.info(I18N.logging_mcserver_hostAndPort(host, port))

        val minecraftServer: MinecraftServerEntity? = MinecraftServers.getServer(host, port)
        if (minecraftServer != null) {
            val mcServerResponse = minecraftServer.status()
            when (mcServerResponse.statusCode) {
                200 ->  {
                    logger.info(I18N.logging_mcserver_statusReceived() + " \n${mcServerResponse.message}")
                    call.respondText(mcServerResponse.message)
                }
                503 -> {
                    logger.warn(I18N.mcserverOffline())
                    call.respondText(I18N.mcserverOffline(), status = HttpStatusCode.ServiceUnavailable)
                }
                else ->  {
                    logger.warn(I18N.logging_mcserver_unknownError(mcServerResponse.statusCode, mcServerResponse.message))
                    call.respondText(
                        I18N.unknownError(mcServerResponse.statusCode, mcServerResponse.message),
                        status = HttpStatusCode.InternalServerError
                    )
                }
            }
        } else {
            logger.warn(I18N.mcserverNotFound())
            call.respondText(I18N.mcserverNotFound(), status = HttpStatusCode.NotFound)
        }
    }
}
