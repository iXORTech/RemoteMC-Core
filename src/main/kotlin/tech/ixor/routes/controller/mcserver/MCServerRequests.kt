package tech.ixor.routes.controller.mcserver

data class MCServerStatusRequest(
    val host: String, val port: Int
)

data class MCServerSayRequest(
    val host: String, val port: Int, val authKey: String,
    val source: String, val sender: String, val message: String
)
