package tech.ixor.job

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import dev.inmo.krontab.doInfinity

import tech.ixor.entity.QQBots

class QQBotAliveMonitor {
    fun start() {
        forceUpdate()
        val bots = QQBots.getAllBots()
        CoroutineScope(Dispatchers.Default).launch {
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
