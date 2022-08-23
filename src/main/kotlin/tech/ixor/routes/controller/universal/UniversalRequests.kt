package tech.ixor.routes.controller.universal

import tech.ixor.entity.HTTPResponse

data class UniversalSendMessageRequest(
    val authKey: String?, val senderID: String?,
    val source: String?, val sender: String?, val message: String?
)

data class UniversalBroadcastRequest(
    val authKey: String?, val message: String?
)

data class UniversalMessagingResponse(
    var responseCount: Int, var responseList: MutableList<HTTPResponse>
)
