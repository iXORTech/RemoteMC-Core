package tech.ixor.routes.controller.mcserver

data class MCServerExecuteCommandRequest(
    val host: String?, val port: Int?,
    val authKey: String?, val command: String?
)

data class MCServerStatusRequest(
    val host: String?, val port: Int?
)
