package tech.ixor.plugins

import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.html.*
import tech.ixor.entity.ConfigEntity
import tech.ixor.utils.VersionUtil

fun Application.configureRouting() {
    val config = ConfigEntity().loadConfig()

    routing {
        get("/") {
            call.respondHtml(block = Index(VersionUtil.getVersion()).index)
        }
        get("/status") {
            call.respondHtml(block = Status(config).status)
        }
    }
}
