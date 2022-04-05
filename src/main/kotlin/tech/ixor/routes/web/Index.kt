package tech.ixor.routes.web

import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.routing.*
import kotlinx.html.*
import tech.ixor.utils.VersionUtil

fun Route.index() {
    get("/") {
        val version = VersionUtil.getVersion()

        call.respondHtml(HttpStatusCode.OK) {
            head {
                title { +"Wecome to RemoteMC-Core!" }
            }
            body {
                h1 { +"Wecome to RemoteMC-Core!" }

                hr {}

                p {
                    b { +"Version " }
                    +version
                }
                if (version.contains("dev") || version.contains("alpha") || version.contains("beta")) {
                    p { b { +"THIS IS IN EXPERIMENTAL STAGE, DO NOT USE IN PRODUCTION ENVIRONMENT!" } }
                } else if (version.contains("rc")) {
                    p { b { +"THIS IS A RELEASE CANDIDATE, DO NOT USE IN PRODUCTION ENVIRONMENT!" } }
                }

                hr {}
                br {}

                p {
                    +"If you see this page, the built-in web server of RemoteMC-Core is successfully installed and working."
                }

                br {}
                hr {}

                p {
                    a(href = "https://github.com/iXORTech/RemoteMC-Core/issues") {
                        +"Report a Bug"
                    }
                }

                hr {}

                p {
                    + "Powered by "
                    a(href = "https://ixor.tech") { +"iXOR Technology" }
                    + " with 💗."
                }
            }
        }
    }
}
