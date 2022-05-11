package tech.ixor.routes.controller.qqbot

data class QQBotSendMessageRequest(
    val host: String, val port: Int, val authKey: String,
    val groupCode: Long, val source: String,
    val sender: String, val message: String
)
