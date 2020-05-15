package com.kotlindiscord.site

import com.kotlindiscord.site.components.*
import io.ktor.application.Application
import io.ktor.locations.KtorExperimentalLocationsAPI

@KtorExperimentalLocationsAPI
fun Application.main() {
    installAuth(this)
    installJson(this)
    installLocations(this)
    installSessions(this)
    installWebsockets(this)
}
