package tech.ixor.routes.controller.mcserver

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import tech.ixor.entity.ConfigEntity
import tech.ixor.entity.MinecraftServerEntity
import tech.ixor.entity.MinecraftServers

fun Route.mcServerMessaging() {
    val authKey = ConfigEntity().loadConfig().authKey

    post("/mcserver/say") {
        val request = call.receive<MCServerSayRequest>()
        if (authKey != request.authKey) {
            call.respondText("Auth key is not valid", status = HttpStatusCode.Forbidden)
            return@post
        }

        val host = request.host
        val port = request.port
        val minecraftServer: MinecraftServerEntity? = MinecraftServers.getServer(host, port)
        if (minecraftServer != null) {
            val source = request.source
            val sender = request.sender
            val message = request.message
            minecraftServer.say(source, sender, message)
            call.respondText("Message sent")
        } else {
            call.respondText("Server not found", status = HttpStatusCode.NotFound)
            return@post
        }
    }

    post("/mcserver/broadcast") {
        val request = call.receive<MCServerBroadcastRequest>()
        if (authKey != request.authKey) {
            call.respondText("Auth key is not valid", status = HttpStatusCode.Forbidden)
            return@post
        }

        val host = request.host
        val port = request.port
        val minecraftServer: MinecraftServerEntity? = MinecraftServers.getServer(host, port)
        if (minecraftServer != null) {
            val message = request.message
            minecraftServer.broadcast(message)
            call.respondText("broadcast sent")
        } else {
            call.respondText("Server not found", status = HttpStatusCode.NotFound)
            return@post
        }
    }
}
