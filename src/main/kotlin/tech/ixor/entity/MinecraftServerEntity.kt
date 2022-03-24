package tech.ixor.entity

class MinecraftServerEntity constructor(val serverName: String, val host: String, val port: Int, val default: Boolean) {
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

    fun getServer(serverName: String): MinecraftServerEntity? {
        return servers.find { it.serverName == serverName }
    }

    fun getServer(host: String, port: Int): MinecraftServerEntity? {
        return servers.find { it.host == host && it.port == port }
    }

    fun getDefaultServer(): MinecraftServerEntity? {
        return servers.find { it.isOnline && it.default }
    }

}
