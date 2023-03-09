package tech.ixor.routes.web

import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.http.*
import io.ktor.server.routing.*
import kotlinx.html.*
import tech.ixor.I18N
import tech.ixor.entity.MinecraftServers
import tech.ixor.entity.QQBot
import tech.ixor.job.MinecraftServerAliveMonitor
import tech.ixor.job.QQBotAliveMonitor

fun Route.status() {
    get("/status") {
        call.respondHtml(HttpStatusCode.OK) {
            val minecraftServerAliveMonitor = MinecraftServerAliveMonitor()
            minecraftServerAliveMonitor.forceUpdate()
            val qqBotAliveMonitor = QQBotAliveMonitor()
            qqBotAliveMonitor.forceUpdate()
            htmlPageHead("${I18N.websiteStatus}")
            body {
                pageWrapper("${I18N.websiteStatus}") {
                    h2 {
                        b("h1a") {
                            +I18N.connections()
                        }
                    }

                    h3 {
                        +I18N.minecraftServer()
                    }
                    if (MinecraftServers.getOnlineServers().isNotEmpty()) {
                        h4 {
                            +I18N.online()
                        }
                    }
                    ol {
                        val onlineServers = MinecraftServers.getOnlineServers()
                        for (minecraftServer in onlineServers) {
                            li {
                                if (minecraftServer.getCompatibilityStatus()) {
                                    i {
                                        attributes["style"] = "color: green;"
                                        +"[Compatible] "
                                    }
                                } else {
                                    i {
                                        attributes["style"] = "color: red;"
                                        +"[Incompatible] "
                                    }
                                }
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
                    if (MinecraftServers.getOfflineServers().isNotEmpty()) {
                        h4 {
                            +I18N.offline()
                        }
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

                    h3 {
                        +I18N.qqChatBot()
                    }
                    val qqBot = QQBot.getBot()
                    p {
                        if (qqBot.getCompatibilityStatus()) {
                            i {
                                attributes["style"] = "color: green;"
                                +"[Compatible] "
                            }
                        } else {
                            if (qqBot.checkOnlineStatus()) {
                                i {
                                    attributes["style"] = "color: red;"
                                    +"[Incompatible] "
                                }
                            }
                        }
                        +"${qqBot.host} : ${qqBot.port} - "
                        b {
                            if (qqBot.checkOnlineStatus()) {
                                +I18N.isOnline()
                            } else {
                                +I18N.isOffline()
                            }
                        }
                    }
                    h4 {
                        +I18N.qqGroups()
                    }
                    ol {
                        val qqGroups = qqBot.getQQGroups()
                        for (qqGroup in qqGroups) {
                            li {
                                b {
                                    +qqGroup.groupName
                                }
                                +" - ${qqGroup.groupCode}"
                            }
                        }
                    }
                }
            }
        }
    }
}
