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
                meta { charset = "utf-8" }
                link( rel = "stylesheet", href = "/assets/style.css", type = "text/css")
                meta { name = "viewport"; content = "width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" }
                script { src = "https://unpkg.com/feather-icons" }
            }
            body {
                div {
                    id = "page-wrapper"
                    div("index-container") {
                        div("content") {
                            h2 { +"${I18N.website_status}" }

                            br { }

                            h2 {
                                b ("h1a") {
                                    +"${I18N.connections}"
                                }
                            }

                            h3 {
                                +"${I18N.minecraft_server}"
                            }
                            h4 {
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
                            h4 {
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

                            h3 {
                                +"${I18N.qq_chat_bots}"
                            }
                            h4 {
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
                                    }
                                }
                            }
                            h4 {
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

                            footer {
                                hr {}
                                a (href = "https://github.com/iXORTech/RemoteMC-Core/issues") {
                                    +"${I18N.reportBug}"
                                }
                                hr {}
                                a (href = "https://github.com/iXORTech") {
                                    i { attributes["data-feather"] = "github" }
                                }
                                +" | ${I18N.poweredBy} "
                                a (href = "https://ixor.tech") { +"iXOR Technology" }
                                +" ${I18N.withLove}"
                                br {}
                                + "${I18N.htmlThemeDesigned0}"
                                a (href = "https://github.com/athul/archie") { +"Archie Theme" }
                                + "${I18N.htmlThemeDesigned1}"
                                a (href = "https://github.com/KevinZonda") { +"@KevinZonda" }
                                + "${I18N.htmlThemeDesigned2}"
                            }
                        }
                    }
                }
            }
        }
    }
}
