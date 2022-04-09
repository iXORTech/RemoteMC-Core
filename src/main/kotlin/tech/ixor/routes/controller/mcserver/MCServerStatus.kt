package tech.ixor.routes.controller.mcserver

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import tech.ixor.entity.MinecraftServerEntity
import tech.ixor.entity.MinecraftServers

fun Route.mcServerStatus() {
    get("/mcserver/status") {
        val request = call.receive<MCServerStatusRequest>()
        val host = request.host
        val port = request.port
        val minecraftServer: MinecraftServerEntity? = MinecraftServers.getServer(host, port)
        if (minecraftServer != null) {
            call.respondText(minecraftServer.status())
        } else {
            call.respondText("Server not found", status = HttpStatusCode.NotFound)
        }
    }
}
