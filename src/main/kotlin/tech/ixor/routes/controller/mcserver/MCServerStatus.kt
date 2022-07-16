package tech.ixor.routes.controller.mcserver

import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import tech.ixor.I18N
import tech.ixor.entity.MinecraftServerEntity
import tech.ixor.entity.MinecraftServers

fun Route.mcServerStatus() {
    get("/mcserver/status") {
        val request = call.receive<MCServerStatusRequest>()
        val host = request.host ?: MinecraftServers.getDefaultServer()?.host
        val port = request.port ?: MinecraftServers.getDefaultServer()?.port

        if (host == null || port == null) {
            call.respondText(I18N.mcserver_null_host_port(), status = HttpStatusCode.BadRequest)
            return@get
        }

        val minecraftServer: MinecraftServerEntity? = MinecraftServers.getServer(host, port)
        if (minecraftServer != null) {
            val mcServerResponse = minecraftServer.status()
            when (mcServerResponse.statusCode) {
                200 -> call.respondText(mcServerResponse.message)
                503 -> call.respondText(I18N.mcserver_offline(), status = HttpStatusCode.ServiceUnavailable)
                else -> call.respondText(
                    I18N.unknown_error(mcServerResponse.statusCode, mcServerResponse.message),
                    status = HttpStatusCode.InternalServerError
                )
            }
        } else {
            call.respondText(I18N.mcserver_not_found(), status = HttpStatusCode.NotFound)
        }
    }
}
