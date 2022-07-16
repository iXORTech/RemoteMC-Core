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
            call.respondText(I18N.client_authkey_invalid(), status = HttpStatusCode.Forbidden)
            return@post
        }

        val host = request.host ?: MinecraftServers.getDefaultServer()?.host
        val port = request.port ?: MinecraftServers.getDefaultServer()?.port

        if (host == null || port == null) {
            call.respondText(I18N.mcserver_null_host_port(), status = HttpStatusCode.BadRequest)
            return@post
        }

        val minecraftServer: MinecraftServerEntity? = MinecraftServers.getServer(host, port)
        if (minecraftServer != null) {
            val command = request.command

            if (command == null) {
                call.respondText(I18N.command_of_request_is_null(), status = HttpStatusCode.BadRequest)
                return@post
            }

            val mcServerResponse = minecraftServer.executeCommand(command)
            when (mcServerResponse.statusCode) {
                200 -> call.respondText(mcServerResponse.message, status = HttpStatusCode.OK)
                401 -> call.respondText(I18N.core_authkey_invalid(), status = HttpStatusCode.Unauthorized)
                503 -> call.respondText(I18N.mcserver_offline(), status = HttpStatusCode.ServiceUnavailable)
                else -> call.respondText(
                    I18N.unknown_error(mcServerResponse.statusCode, mcServerResponse.message),
                    status = HttpStatusCode.InternalServerError
                )
            }
        } else {
            call.respondText(I18N.mcserver_not_found(), status = HttpStatusCode.NotFound)
        }
        return@post
    }
}
