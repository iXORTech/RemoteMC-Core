package tech.ixor.routes.web

import kotlinx.html.*
import tech.ixor.I18N

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

fun BODY.pageWrapper(title: String, brBefore: Boolean = true, brAfter: Boolean = true,
                     content: DIV.() -> Unit) = div {
    id = "page-wrapper"
    div("index-container") {
        div("content") {
            h1 { +title }
            hr {}
            if (brBefore) br {}
            content()
            if (brAfter) br {}
            htmlPageFooter()
            script { unsafe { +"feather.replace()" } }
        }
    }
}

fun DIV.htmlPageFooter() = footer {
    hr {}
    a(href = "https://github.com/iXORTech/RemoteMC-Core/issues") {
        +"${I18N.reportBug}"
    }
    hr {}
    a(href = "https://github.com/iXORTech") {
        i { attributes["data-feather"] = "github" }
    }
    +" | ${I18N.poweredBy} "
    a(href = "https://ixor.tech") { +"iXOR Technology" }
    +" ${I18N.withLove}"
    br {}
    +"${I18N.htmlThemeDesigned0}"
    a(href = "https://github.com/athul/archie") { +"Archie Theme" }
    +"${I18N.htmlThemeDesigned1}"
    a(href = "https://github.com/KevinZonda") { +"@KevinZonda" }
    +"${I18N.htmlThemeDesigned2}"
}
