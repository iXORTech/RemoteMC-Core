package tech.ixor.plugins

import io.ktor.server.application.*
import tech.ixor.routes.controller.mcserver.registerMCServerRoutes
import tech.ixor.routes.controller.universal.registerUniversalRoutes
import tech.ixor.routes.web.registerWebRoutes

fun Application.configureRouting() {
    registerWebRoutes()
    registerMCServerRoutes()
    registerUniversalRoutes()
}
