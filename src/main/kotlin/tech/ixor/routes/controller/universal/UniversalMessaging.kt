package tech.ixor.routes.controller.universal

import io.ktor.server.routing.*
import tech.ixor.entity.ConfigEntity

fun Route.universalMessaging() {
    val authKey = ConfigEntity().loadConfig().authKey

    post("/send-message") {
        // TODO：Add messaging route that send message to all clients (Minecraft Servers, QQ ChatBots, etc.)

    }

    post("/broadcast") {
        // TODO：Add broadcast route that broadcast message to all clients (Minecraft Servers, QQ ChatBots, etc.)

    }
}
