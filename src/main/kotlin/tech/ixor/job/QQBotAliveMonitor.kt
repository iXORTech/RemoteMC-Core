package tech.ixor.job

import dev.inmo.krontab.doInfinity
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import tech.ixor.entity.QQBots

class QQBotAliveMonitor {
    fun start() = runBlocking {
        forceUpdate()
        val bots = QQBots.getAllBots()
        launch {
            doInfinity("0 /5 * * *") {
                bots.forEach {
                    it.updateOnlineStatus()
                }
            }
        }
    }

    fun forceUpdate() {
        val bots = QQBots.getAllBots()
        bots.forEach {
            it.updateOnlineStatus()
        }
    }

}
