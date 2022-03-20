package tech.ixor.entity

import kotlin.io.path.Path

import com.sksamuel.hoplite.ConfigLoader

class ConfigEntity {
    data class Ktor(val host: String, val port: Int)

    data class QQBot(val host: String, val port: Int)

    data class QQGroups(val groupName: String, val groupCode: Long, val default: Boolean)

    data class MinecraftServer(val serverName: String, val host: String, val port: Int, val default: Boolean)

    data class Config(val key: String, val ktor: Ktor, val qqBot: QQBot,
                      val qqGroups: List<QQGroups>, val minecraftServers: List<MinecraftServer>)

    fun loadConfig(): Config {
        val pwd = System.getProperty("user.dir")
        val confFile = "$pwd/conf/config.yaml"
        return ConfigLoader().loadConfigOrThrow(Path(confFile))
    }
}