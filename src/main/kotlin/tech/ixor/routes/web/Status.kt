package tech.ixor.routes.web

import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.http.*
import io.ktor.server.routing.*
import kotlinx.html.*
import tech.ixor.entity.MinecraftServers
import tech.ixor.entity.QQBots
import tech.ixor.job.MinecraftServerAliveMonitor

fun Route.status() {
    get("/status") {
        call.respondHtml(HttpStatusCode.OK) {
            val minecraftServerAliveMonitor = MinecraftServerAliveMonitor()
            minecraftServerAliveMonitor.forceUpdate()
            head {
                title { +"Status - RemoteMC-Core" }
            }
            body {
                h2 { +"Status - RemoteMC-Core" }

                h3 {
                    +"Connections:"
                }

                h4 {
                    +"Minecraft Servers"
                }
                h5 {
                    +"Online: "
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
                                MinecraftServers.getDefaultServer()?.equals(minecraftServer) == true
                            ) {
                                i {
                                    +" (default)"
                                }
                            }
                        }
                    }
                }
                h5 {
                    +"Offline: "
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
                    +"QQ Chat Bots"
                }
                h5 {
                    +"Online: "
                }
                ol {
                    val onlineBots = QQBots.getOnlineBots()
                    for (qqBot in onlineBots) {
                        li {
                            b {
                                +"${qqBot.host} : ${qqBot.port}"
                            }
                            +" - ${qqBot.groupName} (${qqBot.groupCode})"
                            if (qqBot.default &&
                                QQBots.getDefaultBot()?.equals(qqBot) == true
                            ) {
                                i {
                                    +" (default)"
                                }
                            }
                        }
                    }
                }
                h5 {
                    +"Offline: "
                }
                ol {
                    val offlineBots = QQBots.getOfflineBots()
                    for (qqBot in offlineBots) {
                        li {
                            b {
                                +"${qqBot.host} : ${qqBot.port}"
                            }
                            +" - ${qqBot.groupName} (${qqBot.groupCode})"
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
                    +"Powered by "
                    a(href = "https://ixor.tech") { +"iXOR Technology" }
                    +" with 💗."
                }
            }
        }
    }
}
