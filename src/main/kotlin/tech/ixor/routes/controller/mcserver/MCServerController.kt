package tech.ixor.routes.controller.mcserver

import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory
import tech.ixor.I18N
import tech.ixor.entity.ConfigEntity
import tech.ixor.entity.MinecraftServerEntity
import tech.ixor.entity.MinecraftServers

fun Route.mcServerExecuteCommand() {
    val authKey = ConfigEntity().loadConfig().authKey
    val logger = LoggerFactory.getLogger(javaClass)

    post("/mcserverw/execute_command") {
        logger.info(I18N.logging_mcserver_executeCommandRequestReceived())

        val request = call.receive<MCServerExecuteCommandRequest>()
        if (authKey != request.authKey) {
            logger.warn(I18N.logging_authkeyReceivedInvalid())
            call.respondText(I18N.clientAuthkeyInvalid(), status = HttpStatusCode.Forbidden)
            return@post
        }

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
            val command = request.command

            if (command == null) {
                logger.warn(I18N.logging_mcserver_commandToExecuteIsNull())
                call.respondText(I18N.commandOfRequestIsNull(), status = HttpStatusCode.BadRequest)
                return@post
            }

            logger.info(I18N.logging_mcserver_commandToExecute(command))

            val mcServerResponse = minecraftServer.executeCommand(command)
            when (mcServerResponse.statusCode) {
                200 -> {
                    logger.info(I18N.logging_mcserver_commandSuccessfullyExecuted(mcServerResponse.message))
                    call.respondText(mcServerResponse.message, status = HttpStatusCode.OK)
                }
                401 -> {
                    logger.warn(I18N.coreAuthkeyInvalid())
                    call.respondText(I18N.coreAuthkeyInvalid(), status = HttpStatusCode.Unauthorized)
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
        return@post
    }
}
