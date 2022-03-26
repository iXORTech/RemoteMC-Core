package tech.ixor.job

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import dev.inmo.krontab.doInfinity

import tech.ixor.entity.MinecraftServers

class MinecraftServerAliveMonitor() {
    fun start() {
        forceUpdate()
        val servers = MinecraftServers.getAllServers()
        GlobalScope.launch {
            doInfinity("0 /5 * * *") {
                for (server in servers) {
                    server.updateOnlineStatus()
                }
            }
        }
    }

    fun forceUpdate() {
        val servers = MinecraftServers.getAllServers()
        for (server in servers) {
            server.updateOnlineStatus()
        }
    }

}
