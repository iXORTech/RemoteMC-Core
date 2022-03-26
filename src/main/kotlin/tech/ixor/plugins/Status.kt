package tech.ixor.plugins

import kotlinx.html.*
import tech.ixor.entity.ConfigEntity
import tech.ixor.entity.MinecraftServers
import tech.ixor.job.MinecraftServerAliveMonitor

class Status(private val config: ConfigEntity.Config) {
    val status: HTML.() -> Unit = {
        val minecraftServerAliveMonitor = MinecraftServerAliveMonitor()
        minecraftServerAliveMonitor.forceUpdate()
        head {
            title { +"Status - RemoteMC-Core" }
        }
        body {
            h2 { +"Status - RemoteMC-Core" }

            h3 {
                + "Connections:"
            }

            h4 {
                + "Minecraft Servers"
            }
            h5 {
                + "Online: "
            }
            ol {
                val onlineServers = MinecraftServers.getOnlineServers()
                for (minecraftServer in onlineServers) {
                    li {
                        b {
                            +minecraftServer.serverName
                        }
                        +" - ${minecraftServer.host} : ${minecraftServer.port}"
                        if (minecraftServer.default &&
                            MinecraftServers.getDefaultServer()?.equals(minecraftServer) == true) {
                            i {
                                +" (default)"
                            }
                        }
                    }
                }
            }
            h5 {
                + "Offline: "
            }
            ol {
                val offlineServers = MinecraftServers.getOfflineServers()
                for (minecraftServer in offlineServers) {
                    li {
                        b {
                            +minecraftServer.serverName
                        }
                        +" - ${minecraftServer.host} : ${minecraftServer.port}"
                    }
                }
            }

            h4 {
                + "QQ Chat Bots"
            }
            ol {
                b {
                    +"${config.qqBot.host}:${config.qqBot.port}"
                }
                var gotDefault = false
                for (qqGroup in config.qqGroups) {
                    li {
                        +" - ${qqGroup.groupName} (@${qqGroup.groupCode})"
                        if (qqGroup.default && !gotDefault) {
                            gotDefault = true
                            i {
                                +" (default)"
                            }
                        }
                    }
                }
            }

            br {}
            hr {}

            p {
                a(href = "https://github.com/iXORTech/RemoteMC-Core/issues") {
                    +"Report a Bug"
                }
            }

            hr {}

            p {
                + "Powered by "
                a(href = "https://ixor.tech") { +"iXOR Technology" }
                + " with 💗."
            }
        }
    }
}
