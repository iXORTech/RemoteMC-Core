package tech.ixor.routes.web

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import kotlinx.html.*
import tech.ixor.I18N
import tech.ixor.utils.VersionUtil

fun Route.about() {
    get("/about") {
        call.respondHtml(HttpStatusCode.OK) {
            htmlPageHead("${I18N.about}")
            body {
                pageWrapper("${I18N.about}") {
                    p {
                        style = "text-align: center;"
                        b { +"RemoteMC-Core" }
                    }
                    p {
                        style = "text-align: center;"
                        +"${I18N.version} ${VersionUtil.getVersion()}"
                        br {}
                        +I18N.built(VersionUtil.getBuildDate())
                    }
                    h2 { +"Dependencies" }
                    val dependenciesInfo = VersionUtil.getDependenciesInfo()
                    ul {
                        dependenciesInfo.forEach {
                            li { +it }
                        }
                    }
                }
            }
        }
    }
}
