package tech.ixor.routes.controller.mcserver

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.registerMCServerRoutes() {
    routing {
        mcServerStatus()
        mcServerMessaging()
        mcServerExecuteCommand()
    }
}
