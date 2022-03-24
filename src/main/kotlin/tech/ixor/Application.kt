package tech.ixor

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import tech.ixor.entity.ConfigEntity
import tech.ixor.entity.MinecraftServers
import tech.ixor.plugins.*
import tech.ixor.utils.*

fun loadMinecraftServers(config: ConfigEntity.Config) {
    val minecraftServers = MinecraftServers
    for (server in config.minecraftServers) {
        minecraftServers.addServer(server)
    }
}

fun main() {
    println("Starting RemoteMC-Core...\n")

    val version = VersionUtil.getVersion()
    println("Version $version")
    if (version.contains("dev") || version.contains("alpha") || version.contains("beta")) {
        println("THIS IS IN EXPERIMENTAL STAGE, DO NOT USE IN PRODUCTION ENVIRONMENT!")
    } else if (version.contains("rc")) {
        println("THIS IS A RELEASE CANDIDATE, DO NOT USE IN PRODUCTION ENVIRONMENT!")
    }
    println()

    val config = ConfigEntity().loadConfig()
    loadMinecraftServers(config)

    embeddedServer(Netty, port = config.ktor.port, host = config.ktor.host) {
        configureRouting()
    }.start(wait = true)
}
