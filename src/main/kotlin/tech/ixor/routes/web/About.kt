package tech.ixor.routes.web

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import kotlinx.html.*
import tech.ixor.I18N

fun Route.about() {
    get("/about") {
        call.respondHtml(HttpStatusCode.OK) {
            htmlPageHead("${I18N.about}")
            body {
                p { +"${I18N.about}" }
            }
        }
    }
}
