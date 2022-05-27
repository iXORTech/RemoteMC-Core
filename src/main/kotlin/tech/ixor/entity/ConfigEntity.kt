package tech.ixor.entity

import java.io.File

import com.sksamuel.hoplite.ConfigLoader
import tech.ixor.utils.FileDownloader

class ConfigEntity {
    data class Ktor(val host: String, val port: Int)

    data class QQBot(
        val host: String, val port: Int, val ssl: Boolean,
        val groupName: String, val groupCode: Long, val default: Boolean
    )

    data class MinecraftServer(
        val serverName: String, val host: String, val port: Int,
        val ssl: Boolean, val default: Boolean
    )

    data class Config(
        val language: String, val authKey: String, val ktor: Ktor,
        val qqBots: List<QQBot>, val minecraftServers: List<MinecraftServer>
    )

    fun loadConfig(): Config {
        val pwd = System.getProperty("user.dir")
        val confFile = "$pwd/conf/config.yaml"
        if (!File(confFile).exists()) {
            val fileDownloader = FileDownloader()
            fileDownloader.downloadFile(
                "https://cdn.jsdelivr.net/gh/iXORTech/RemoteMC-Core/src/main/resources/conf/config.yaml",
                confFile
            )
        }
        return ConfigLoader().loadConfigOrThrow(confFile)
    }
}
