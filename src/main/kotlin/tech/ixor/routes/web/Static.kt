package tech.ixor.routes.web

import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Route.static() {
    static("/") {
        staticBasePackage = "web"
        static("assets") {
            resources("style")
        }
    }
}
