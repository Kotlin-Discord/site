package com.kotlindiscord.site.components

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import io.ktor.sessions.header

data class SessionApiKey(val token: String)
data class SessionLogin(val token: String)

fun installSessions(app: Application) {
    app.install(Sessions) {
        // TODO: There's almost certainly more stuff to do here, but let's leave it like this for now
        cookie<SessionLogin>("LOGIN")
        header<SessionApiKey>("API_KEY")
    }
}
