package tech.ixor.job

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import dev.inmo.krontab.doInfinity
import org.slf4j.LoggerFactory
import tech.ixor.entity.QQBot

class QQBotAliveMonitor {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun start() {
        forceUpdate()
        val bot = QQBot.getBot()
        CoroutineScope(Dispatchers.Default).launch {
            doInfinity("0 /5 * * *") {
                logger.info("[Scheduled Task] Updating QQ Bot online status")
                bot.updateOnlineStatus()
            }
        }
    }

    fun forceUpdate() {
        val bot = QQBot.getBot()
        logger.info("[Force Trigger] Updating QQ Bot online status")
        bot.updateOnlineStatus()
    }

}
