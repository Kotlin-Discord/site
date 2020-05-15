package com.kotlindiscord.site.components

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.websocket.WebSockets

fun installWebsockets(app: Application) {
    app.install(WebSockets) {
        // Don't think we need to change any of the settings
    }
}
