package tech.ixor.entity

class MinecraftServerEntity constructor(val serverName: String, val host: String, val port: Int, default: Boolean) {
    var isOnline: Boolean = false
}

object MinecraftServers {
    private val servers = mutableListOf<MinecraftServerEntity>()

    fun addServer(server: MinecraftServerEntity) {
        servers.add(server)
    }

    fun addServer(server: ConfigEntity.MinecraftServer) {
        servers.add(MinecraftServerEntity(server.serverName, server.host, server.port, server.default))
    }

    fun addServer(serverName: String, host: String, port: Int, default: Boolean) {
        servers.add(MinecraftServerEntity(serverName, host, port, default))
    }

    fun getServerByName(serverName: String): MinecraftServerEntity? {
        return servers.find { it.serverName == serverName }
    }

    fun getServerByAddress(host: String, port: Int): MinecraftServerEntity? {
        return servers.find { it.host == host && it.port == port }
    }
}
