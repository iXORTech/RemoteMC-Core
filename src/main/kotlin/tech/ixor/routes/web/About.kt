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
            head {
                title { +"${I18N.about}" }
                meta { charset = "utf-8" }
                link(rel = "stylesheet", href = "/assets/style.css", type = "text/css")
                meta {
                    name = "viewport"
                    content = "width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0"
                }
                script { src = "https://unpkg.com/feather-icons" }
            }
            body {
                p { +"${I18N.about}" }
            }
        }
    }
}
