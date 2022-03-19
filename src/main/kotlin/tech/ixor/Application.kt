package tech.ixor

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import tech.ixor.plugins.*
import tech.ixor.utils.*

fun main() {
    println("Starting RemoteMC-Core...\n")
    println("${VersionUtil.getVersion()}\n")

    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting()
    }.start(wait = true)
}
