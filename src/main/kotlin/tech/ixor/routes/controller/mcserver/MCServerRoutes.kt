package tech.ixor.routes.controller.mcserver

import io.ktor.application.*
import io.ktor.routing.*

fun Application.registerMCServerRoutes() {
    routing {
        mcServerStatus()
        mcServerMessaging()
        mcServerExecuteCommand()
    }
}
