package tech.ixor.routes.controller.mcserver

import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import tech.ixor.I18N
import tech.ixor.entity.ConfigEntity
import tech.ixor.entity.MinecraftServerEntity
import tech.ixor.entity.MinecraftServers

fun Route.mcServerExecuteCommand() {
    val authKey = ConfigEntity().loadConfig().authKey

    post("/mcserver/execute_command") {
        val request = call.receive<MCServerExecuteCommandRequest>()
        if (authKey != request.authKey) {
            call.respondText(I18N.clientAuthkeyInvalid(), status = HttpStatusCode.Forbidden)
            return@post
        }

        val host = request.host ?: MinecraftServers.getDefaultServer()?.host
        val port = request.port ?: MinecraftServers.getDefaultServer()?.port

        if (host == null || port == null) {
            call.respondText(I18N.mcserverNullHostPort(), status = HttpStatusCode.BadRequest)
            return@post
        }

        val minecraftServer: MinecraftServerEntity? = MinecraftServers.getServer(host, port)
        if (minecraftServer != null) {
            val command = request.command

            if (command == null) {
                call.respondText(I18N.commandOfRequestIsNull(), status = HttpStatusCode.BadRequest)
                return@post
            }

            val mcServerResponse = minecraftServer.executeCommand(command)
            when (mcServerResponse.statusCode) {
                200 -> call.respondText(mcServerResponse.message, status = HttpStatusCode.OK)
                401 -> call.respondText(I18N.coreAuthkeyInvalid(), status = HttpStatusCode.Unauthorized)
                503 -> call.respondText(I18N.mcserverOffline(), status = HttpStatusCode.ServiceUnavailable)
                else -> call.respondText(
                    I18N.unknownError(mcServerResponse.statusCode, mcServerResponse.message),
                    status = HttpStatusCode.InternalServerError
                )
            }
        } else {
            call.respondText(I18N.mcserverNotFound(), status = HttpStatusCode.NotFound)
        }
        return@post
    }
}
