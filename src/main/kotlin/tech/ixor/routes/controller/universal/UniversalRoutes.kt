package tech.ixor.routes.controller.universal

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.registerUniversalRoutes() {
    routing {
        universalMessaging()
    }
}
