package tech.ixor.routes.controller.qqbot

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.registerQQBotRoutes() {
    routing {
        qqBotMessaging()
    }
}
