package tech.ixor.plugins

import kotlinx.html.*
import tech.ixor.entity.ConfigEntity

class Status(private val config: ConfigEntity.Config) {
    val status: HTML.() -> Unit = {
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
            ol {
                var gotDefault = false
                for (minecraftServer in config.minecraftServers) {
                    li {
                        b {
                            +minecraftServer.serverName
                        }
                        +" - ${minecraftServer.host} : ${minecraftServer.port}"
                        if (minecraftServer.default && !gotDefault) {
                            gotDefault = true
                            i {
                                +" (default)"
                            }
                        }
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
                + "Powered by "
                a(href = "https://ixor.tech") { +"iXOR Technology" }
                + " with 💗."
            }
        }
    }
}
