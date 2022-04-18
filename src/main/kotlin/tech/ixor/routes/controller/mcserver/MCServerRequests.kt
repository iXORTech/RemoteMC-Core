package tech.ixor.routes.controller.mcserver

data class MCServerExecuteCommandRequest(
    val host: String, val port: Int,
    val authKey: String, val command: String
)

data class MCServerStatusRequest(
    val host: String, val port: Int
)

data class MCServerSayRequest(
    val host: String, val port: Int, val authKey: String,
    val source: String, val sender: String, val message: String
)

data class MCServerBroadcastRequest(
    val host: String, val port: Int, val authKey: String, val message: String
)
