package tech.ixor.routes.web

import kotlinx.html.*

fun HTML.htmlPageHead(title: String) = head {
    title { +title }
    meta { charset = "utf-8" }
    link(rel = "stylesheet", href = "/assets/style.css", type = "text/css")
    meta {
        name = "viewport"
        content = "width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0"
    }
    script { src = "https://unpkg.com/feather-icons" }
}
