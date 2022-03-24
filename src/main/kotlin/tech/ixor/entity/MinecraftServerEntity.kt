package tech.ixor.entity

class MinecraftServerEntity constructor(val serverName: String, val host: String, val port: Int) {
    var isOnline: Boolean = false
}

object MinecraftServers {
    private val servers = mutableListOf<MinecraftServerEntity>()

    fun addServer(server: MinecraftServerEntity) {
        servers.add(server)
    }

    fun addServer(serverName: String, host: String, port: Int) {
        servers.add(MinecraftServerEntity(serverName, host, port))
    }

    fun getServerByName(serverName: String): MinecraftServerEntity? {
        return servers.find { it.serverName == serverName }
    }

    fun getServerByAddress(host: String, port: Int): MinecraftServerEntity? {
        return servers.find { it.host == host && it.port == port }
    }
}
