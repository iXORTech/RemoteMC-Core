package tech.ixor.routes.controller.mcserver

import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import tech.ixor.entity.ConfigEntity
import tech.ixor.entity.MinecraftServerEntity
import tech.ixor.entity.MinecraftServers

fun Route.mcServerMessaging() {
    val authKey = ConfigEntity().loadConfig().authKey

    post("/mcserver/say") {
        val request = call.receive<MCServerSayRequest>()
        if (authKey != request.authKey) {
            call.respondText("Auth key (on your client) is not valid", status = HttpStatusCode.Forbidden)
            return@post
        }

        val host = request.host
        val port = request.port
        val minecraftServer: MinecraftServerEntity? = MinecraftServers.getServer(host, port)
        if (minecraftServer != null) {
            val source = request.source
            val sender = request.sender
            val message = request.message
            val mcServerResponse = minecraftServer.say(source, sender, message)
            if (mcServerResponse.statusCode == 200) {
                call.respondText("Message sent!", status = HttpStatusCode.OK)
            } else if (mcServerResponse.statusCode == 401) {
                call.respondText("Auth key (on RemoteMC-Core) is not valid", status = HttpStatusCode.Forbidden)
            } else if (mcServerResponse.statusCode == 500 && mcServerResponse.message == "Server is offline") {
                call.respondText("Target server offline", status = HttpStatusCode.InternalServerError)
            } else {
                call.respondText("Unknown error", status = HttpStatusCode.InternalServerError)
            }
        } else {
            call.respondText("Server not found", status = HttpStatusCode.NotFound)
            return@post
        }
    }

    post("/mcserver/broadcast") {
        val request = call.receive<MCServerBroadcastRequest>()
        if (authKey != request.authKey) {
            call.respondText("Auth key (on your client) is not valid", status = HttpStatusCode.Forbidden)
            return@post
        }

        val host = request.host
        val port = request.port
        val minecraftServer: MinecraftServerEntity? = MinecraftServers.getServer(host, port)
        if (minecraftServer != null) {
            val message = request.message
            val mcServerResponse = minecraftServer.broadcast(message)
            if (mcServerResponse.statusCode == 200) {
                call.respondText("Broadcast sent!", status = HttpStatusCode.OK)
            } else if (mcServerResponse.statusCode == 401) {
                call.respondText("Auth key (on RemoteMC-Core) is not valid", status = HttpStatusCode.Forbidden)
            } else if (mcServerResponse.statusCode == 500 && mcServerResponse.message == "Server is offline") {
                call.respondText("Target server offline", status = HttpStatusCode.InternalServerError)
            } else {
                call.respondText("Unknown error", status = HttpStatusCode.InternalServerError)
            }
        } else {
            call.respondText("Server not found", status = HttpStatusCode.NotFound)
            return@post
        }
    }

}
