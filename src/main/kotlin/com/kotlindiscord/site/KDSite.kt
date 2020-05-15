package com.kotlindiscord.site

import com.kotlindiscord.site.components.*
import io.ktor.application.Application
import io.ktor.locations.KtorExperimentalLocationsAPI

@KtorExperimentalLocationsAPI
fun Application.main() {
    // Install Locations first since other components use it
    installLocations(this)

    // Install each component we need to use
    installAuth(this)
    installJson(this)
    installPebble(this)
    installSessions(this)
    installWebsockets(this)

    // Now, set up routing
    installRouting(this)
}
