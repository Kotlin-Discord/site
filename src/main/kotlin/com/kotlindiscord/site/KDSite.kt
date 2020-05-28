package com.kotlindiscord.site

import com.kotlindiscord.site.components.*
import io.ktor.application.Application
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.sentry.Sentry
import mu.KotlinLogging

@KtorExperimentalLocationsAPI
fun Application.main() {
    val logger = KotlinLogging.logger {}

    if (System.getenv().getOrDefault("SENTRY_DSN", null) != null) {
        val sentry = Sentry.init()
        sentry.release = buildInfo.version
    }

    logger.info { "Starting KDSite version ${buildInfo.version}." }

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
