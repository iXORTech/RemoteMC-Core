package tech.ixor

import io.ktor.server.application.Application
import de.comahe.i18n4k.i18n4k
import de.comahe.i18n4k.config.I18n4kConfigDefault
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.slf4j.LoggerFactory
import tech.ixor.entity.ConfigEntity
import tech.ixor.entity.MinecraftServers
import tech.ixor.entity.QQBot
import tech.ixor.entity.QQBotEntity
import tech.ixor.job.MinecraftServerAliveMonitor
import tech.ixor.job.QQBotAliveMonitor
import tech.ixor.plugins.configureRouting
import tech.ixor.plugins.configureSerialization
import tech.ixor.utils.*
import java.util.*

private val logger = LoggerFactory.getLogger("RemoteMC-Core")

fun loadMinecraftServers(config: ConfigEntity.Config) {
    val minecraftServers = MinecraftServers
    config.minecraftServers.forEach {
        minecraftServers.addServer(it)
    }
}

fun startMinecraftServerAliveMonitor() {
    val minecraftServerAliveMonitor = MinecraftServerAliveMonitor()
    minecraftServerAliveMonitor.start()
}

fun loadQQBots(config: ConfigEntity.Config) {
    val qqBot = QQBot
    qqBot.setBot(config.qqBot)
    loadQQGroups(qqBot.getBot(), config.qqBot)
}

fun startQQBotAliveMonitor() {
    val qqBotAliveMonitor = QQBotAliveMonitor()
    qqBotAliveMonitor.start()
}

fun loadQQGroups(qqBot: QQBotEntity, config: ConfigEntity.QQBotConfig) {
    config.groups.forEach {
        qqBot.addQQGroup(it)
    }
}

fun main() {
    val config = ConfigEntity().loadConfig()
    VersionUtil.loadVersionProperties()

    val i18n4kConfig = I18n4kConfigDefault()
    i18n4k = i18n4kConfig
    i18n4kConfig.locale = Locale(config.language)

    logger.info(I18N.starting())
    logger.info("${I18N.selectedLanguage} ${I18N.language}")

    logger.info("${I18N.version} ${VersionUtil.getVersion()}")
    logger.info(I18N.built(VersionUtil.getBuildDate()))
    val stage = VersionUtil.getProperty("stage")
    if (stage.contains("dev") || stage.contains("alpha") || stage.contains("beta")) {
        logger.warn("${I18N.experimental}")
    } else if (stage.contains("rc")) {
        logger.warn("${I18N.releaseCandidate}")
    }

    loadMinecraftServers(config)
    loadQQBots(config)
    startMinecraftServerAliveMonitor()
    startQQBotAliveMonitor()

    embeddedServer(
        Netty,
        port = config.ktor.port,
        host = "127.0.0.1",
        module = Application::remotemcCoreApplicationModule
    ).start(wait = true)
}

fun Application.remotemcCoreApplicationModule() {
    configureRouting()
    configureSerialization()
}
