package tech.ixor

import de.comahe.i18n4k.i18n4k
import de.comahe.i18n4k.config.I18n4kConfigDefault
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.gson.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import tech.ixor.entity.ConfigEntity
import tech.ixor.entity.MinecraftServers
import tech.ixor.entity.QQBots
import tech.ixor.job.MinecraftServerAliveMonitor
import tech.ixor.job.QQBotAliveMonitor
import tech.ixor.plugins.configureRouting
import tech.ixor.plugins.configureSerialization
import tech.ixor.routes.controller.mcserver.registerMCServerRoutes
import tech.ixor.routes.controller.qqbot.registerQQBotRoutes
import tech.ixor.routes.web.registerWebRoutes
import tech.ixor.utils.*
import java.util.*

fun loadMinecraftServers(config: ConfigEntity.Config) {
    val minecraftServers = MinecraftServers
    config.minecraftServers.forEach {
        minecraftServers.addServer(it)
    }
    val minecraftServerAliveMonitor = MinecraftServerAliveMonitor()
    minecraftServerAliveMonitor.start()
}

fun loadQQBots(config: ConfigEntity.Config) {
    val qqBots = QQBots
    config.qqBots.forEach {
        qqBots.addBot(it)
    }
    val qqBotAliveMonitor = QQBotAliveMonitor()
    qqBotAliveMonitor.start()
}

fun main() {
    val config = ConfigEntity().loadConfig()

    val i18n4kConfig = I18n4kConfigDefault()
    i18n4k = i18n4kConfig
    i18n4kConfig.locale = Locale(config.language)

    println("${I18N.starting}\n")
    println("${I18N.selectedLanguage} ${I18N.language}")

    val version = VersionUtil.getVersion()
    println("${I18N.version} $version")
    if (version.contains("dev") || version.contains("alpha") || version.contains("beta")) {
        println("${I18N.experimental}")
    } else if (version.contains("rc")) {
        println("${I18N.releaseCandidate}")
    }
    println()

    loadMinecraftServers(config)
    loadQQBots(config)

    embeddedServer(Netty, port = config.ktor.port, host = config.ktor.host) {
        configureRouting()
        configureSerialization()
    }.start(wait = true)
}
