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
            head {
                title { +"${I18N.welcome}" }
            }
            body {
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
                hr {}

                p {
                    a(href = "https://github.com/iXORTech/RemoteMC-Core/issues") {
                        +"${I18N.reportBug}"
                    }
                }

                hr {}

                p {
                    +"${I18N.poweredBy} "
                    a(href = "https://ixor.tech") { +"iXOR Technology" }
                    +" ${I18N.withLove}"
                }
            }
        }
    }
}
