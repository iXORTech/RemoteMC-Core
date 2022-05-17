package tech.ixor.job

import dev.inmo.krontab.doInfinity
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

import tech.ixor.entity.MinecraftServers

class MinecraftServerAliveMonitor() {
    fun start() = runBlocking {
        forceUpdate()
        val servers = MinecraftServers.getAllServers()
        launch {
            doInfinity("0 /5 * * *") {
                println("Checking servers")
                servers.forEach {
                    it.updateOnlineStatus()
                }
            }
        }
    }

    fun forceUpdate() {
        val servers = MinecraftServers.getAllServers()
        servers.forEach {
            it.updateOnlineStatus()
        }
    }

}
