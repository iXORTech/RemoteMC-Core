package tech.ixor.routes.controller.mcserver

import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory
import org.slf4j.Logger

fun Application.registerMCServerRoutes() {
    routing {
        mcServerStatus()
        mcServerExecuteCommand()
    }
}
