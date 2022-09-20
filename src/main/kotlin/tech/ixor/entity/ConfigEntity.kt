package tech.ixor.entity

import java.io.File

import com.sksamuel.hoplite.ConfigLoader
import org.slf4j.LoggerFactory
import tech.ixor.I18N
import tech.ixor.utils.FileDownloader

class ConfigEntity {
    private val logger = LoggerFactory.getLogger(javaClass)

    data class Ktor(val port: Int)

    data class QQGroupConfig(val groupName: String, val groupCode: Long)

    data class QQBotConfig(
        val host: String, val port: Int, val ssl: Boolean, val groups: List<QQGroupConfig>
    )

    data class MinecraftServerConfig(
        val serverName: String, val host: String, val port: Int,
        val ssl: Boolean, val default: Boolean
    )

    data class Config(
        val language: String, val authKey: String, val ktor: Ktor,
        val qqBot: QQBotConfig, val minecraftServers: List<MinecraftServerConfig>
    )

    fun loadConfig(): Config {
        val pwd = System.getProperty("user.dir")
        val confFile = "$pwd/conf/config.yaml"
        if (!File(confFile).exists()) {
            logger.info(I18N.configFileNotFound())
            val fileDownloader = FileDownloader()
            fileDownloader.downloadFile(
                "https://cdn.jsdelivr.net/gh/iXORTech/RemoteMC-Core/src/main/resources/conf/config.yaml",
                confFile,
                I18N.configFileDownloadDescription()
            )
        }
        return ConfigLoader().loadConfigOrThrow(confFile)
    }
}
