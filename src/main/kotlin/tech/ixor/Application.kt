package tech.ixor

import de.comahe.i18n4k.i18n4k
import de.comahe.i18n4k.config.I18n4kConfigDefault
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import tech.ixor.entity.ConfigEntity
import tech.ixor.entity.MinecraftServers
import tech.ixor.entity.QQBots
import tech.ixor.job.MinecraftServerAliveMonitor
import tech.ixor.job.QQBotAliveMonitor
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

    println("Starting RemoteMC-Core...\n")
    println("${I18N.selectedLanguage()} ${I18N.language()}")

    val version = VersionUtil.getVersion()
    println("Version $version")
    if (version.contains("dev") || version.contains("alpha") || version.contains("beta")) {
        println("THIS IS IN EXPERIMENTAL STAGE, DO NOT USE IN PRODUCTION ENVIRONMENT!")
    } else if (version.contains("rc")) {
        println("THIS IS A RELEASE CANDIDATE, DO NOT USE IN PRODUCTION ENVIRONMENT!")
    }
    println()

    loadMinecraftServers(config)
    loadQQBots(config)

    embeddedServer(Netty, port = config.ktor.port, host = config.ktor.host) {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
                disableHtmlEscaping()
            }
        }

        registerWebRoutes()
        registerMCServerRoutes()
        registerQQBotRoutes()
    }.start(wait = true)
}
