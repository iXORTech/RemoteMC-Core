package tech.ixor.routes.controller.mcserver

import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import tech.ixor.entity.ConfigEntity
import tech.ixor.entity.MinecraftServerEntity
import tech.ixor.entity.MinecraftServers

fun Route.mcServerExecuteCommand() {
    val authKey = ConfigEntity().loadConfig().authKey

    post("/mcserver/execute_command") {
        val request = call.receive<MCServerExecuteCommandRequest>()
        if (authKey != request.authKey) {
            call.respondText("Auth key (on your client) is not valid", status = HttpStatusCode.Forbidden)
            return@post
        }

        val host = request.host
        val port = request.port
        val minecraftServer: MinecraftServerEntity? = MinecraftServers.getServer(host, port)
        if (minecraftServer != null) {
            val command = request.command
            val mcServerResponse = minecraftServer.executeCommand(command)
            when (mcServerResponse.statusCode) {
                200 -> call.respondText(mcServerResponse.message, status = HttpStatusCode.OK)
                401 -> call.respondText("Auth key on RemoteMC-Core is not valid! Please check authKey settings and make sure" +
                        " they were the same everywhere!", status = HttpStatusCode.Unauthorized)
                else -> call.respondText("Unknown error! Status Code ${mcServerResponse.statusCode} - Message ${mcServerResponse.message}.",
                    status = HttpStatusCode.InternalServerError)
            }
        } else {
            call.respondText("Server not found", status = HttpStatusCode.NotFound)
            return@post
        }
    }
}
