package tech.ixor.job

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import dev.inmo.krontab.doInfinity
import tech.ixor.entity.QQBot

class QQBotAliveMonitor {
    fun start() {
        forceUpdate()
        val bot = QQBot.getBot()
        CoroutineScope(Dispatchers.Default).launch {
            doInfinity("0 /5 * * *") {
                bot.updateOnlineStatus()
            }
        }
    }

    fun forceUpdate() {
        val bot = QQBot.getBot()
        bot.updateOnlineStatus()
    }

}
