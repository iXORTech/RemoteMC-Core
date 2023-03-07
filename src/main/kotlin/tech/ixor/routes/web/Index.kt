package tech.ixor.routes.web

import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.http.*
import io.ktor.server.routing.*
import kotlinx.html.*
import tech.ixor.I18N
import tech.ixor.utils.VersionUtil

fun Route.index() {
    get("/") {
        val version = VersionUtil.getVersion()

        call.respondHtml(HttpStatusCode.OK) {
            htmlPageHead("${I18N.welcome}")
            body {
                div {
                    id = "page-wrapper"
                    div("index-container") {
                        div("content") {
                            h1 { +"${I18N.welcome}" }

                            hr {}

                            p {
                                b { +"${I18N.version} " }
                                +version
                            }
                            if (version.contains("dev") || version.contains("alpha") || version.contains("beta")) {
                                p { b { +"${I18N.experimental}" } }
                            } else if (version.contains("rc")) {
                                p { b { +"${I18N.releaseCandidate}" } }
                            }

                            hr {}
                            br {}

                            p {
                                +"${I18N.successfullyInstalled}"
                            }

                            br {}

                            footer {
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
                        }
                    }
                }

                script { +"feather.replace()" }

            }
        }
    }
}
