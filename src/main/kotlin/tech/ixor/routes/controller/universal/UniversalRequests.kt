package tech.ixor.routes.controller.universal

data class UniversalSendMessageRequest(
    val authKey: String?, val messageID: String?,
    val source: String?, val sender: String?, val message: String?
)

data class UniversalBroadcastRequest(
    val authKey: String?, val message: String?
)
