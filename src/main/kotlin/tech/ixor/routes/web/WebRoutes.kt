package tech.ixor.routes.web

import io.ktor.application.*
import io.ktor.routing.*

fun Application.registerWebRoutes() {
    routing {
        index()
        status()
    }
}
