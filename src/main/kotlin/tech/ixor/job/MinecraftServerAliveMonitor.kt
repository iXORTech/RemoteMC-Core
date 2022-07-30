package tech.ixor.job

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import dev.inmo.krontab.doInfinity
import org.slf4j.LoggerFactory

import tech.ixor.entity.MinecraftServers

class MinecraftServerAliveMonitor() {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun start() {
        forceUpdate()
        val servers = MinecraftServers.getAllServers()
        CoroutineScope(Dispatchers.Default).launch {
            doInfinity("0 /5 * * *") {
                logger.info("[Scheduled Task] Updating Minecraft Server online status")
                servers.forEach {
                    it.updateOnlineStatus()
                }
            }
        }
    }

    fun forceUpdate() {
        val servers = MinecraftServers.getAllServers()
        logger.info("[Force Trigger] Updating Minecraft Server online status")
        servers.forEach {
            it.updateOnlineStatus()
        }
    }

}
