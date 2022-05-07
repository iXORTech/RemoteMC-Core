package tech.ixor.job

import dev.inmo.krontab.doInfinity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tech.ixor.entity.QQBots

class QQBotAliveMonitor {
    fun start() {
        forceUpdate()
        val bots = QQBots.getAllBots()
        GlobalScope.launch {
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
