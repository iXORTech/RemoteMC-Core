package tech.ixor

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import tech.ixor.entity.ConfigEntity
import tech.ixor.plugins.*
import tech.ixor.utils.*

fun main() {
    println("Starting RemoteMC-Core...\n")
    println("Version ${VersionUtil.getVersion()}\n")

    val config = ConfigEntity().loadConfig()

    embeddedServer(Netty, port = config.ktor.port, host = config.ktor.host) {
        configureRouting()
    }.start(wait = true)
}
