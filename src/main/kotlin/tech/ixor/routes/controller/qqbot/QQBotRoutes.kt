package tech.ixor.routes.controller.qqbot

import io.ktor.application.*
import io.ktor.routing.*

fun Application.registerQQBotRoutes() {
    routing {
        qqBotMessaging()
    }
}
