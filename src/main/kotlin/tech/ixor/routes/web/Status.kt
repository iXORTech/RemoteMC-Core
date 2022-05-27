package tech.ixor.routes.web

import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.http.*
import io.ktor.server.routing.*
import kotlinx.html.*
import tech.ixor.I18N
import tech.ixor.entity.MinecraftServers
import tech.ixor.entity.QQBots
import tech.ixor.job.MinecraftServerAliveMonitor

fun Route.status() {
    get("/status") {
        call.respondHtml(HttpStatusCode.OK) {
            val minecraftServerAliveMonitor = MinecraftServerAliveMonitor()
            minecraftServerAliveMonitor.forceUpdate()
            head {
                title { +"${I18N.website_status}" }
            }
            body {
                h2 { +"${I18N.website_status}" }

                h3 {
                    +"${I18N.connections}"
                }

                h4 {
                    +"${I18N.minecraft_server}"
                }
                h5 {
                    +"${I18N.online} "
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
                                    +" ${I18N.default}"
                                }
                            }
                        }
                    }
                }
                h5 {
                    +"${I18N.offline}"
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
                    +"${I18N.qq_chat_bots}"
                }
                h5 {
                    +"${I18N.online} "
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
                                    +" ${I18N.default}"
                                }
                            }
                        }
                    }
                }
                h5 {
                    +"${I18N.offline}"
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
                        +"${I18N.reportBug}"
                    }
                }

                hr {}

                p {
                    +"${I18N.poweredBy} "
                    a(href = "https://ixor.tech") { +"iXOR Technology" }
                    +" ${I18N.withLove}"
                }
            }
        }
    }
}
