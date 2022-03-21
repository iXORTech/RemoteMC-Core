package tech.ixor.plugins

import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.html.*
import tech.ixor.utils.VersionUtil

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondHtml(block = Index(VersionUtil.getVersion()).index)
        }
    }
}
