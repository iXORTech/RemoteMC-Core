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
        call.respondHtml(HttpStatusCode.OK) {
            htmlPageHead("${I18N.welcome}")
            body {
                pageWrapper("${I18N.welcome}", brBefore = false) {
                    p {
                        b { +"${I18N.version} " }
                        +VersionUtil.getVersion()
                    }
                    val versionStage = VersionUtil.getProperty("stage")
                    if (versionStage.contains("SNAPSHOT") || versionStage.contains("alpha") || versionStage.contains("beta")) {
                        p {
                            style = "color: orange;"
                            b { +I18N.experimental() }
                        }
                    } else if (versionStage.contains("rc")) {
                        p {
                            style = "color: orange;"
                            b { +I18N.releaseCandidate() }
                        }
                    }

                    hr {}
                    br {}

                    p {
                        +I18N.successfullyInstalled()
                    }
                }
            }
        }
    }
}
