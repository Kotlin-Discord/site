package com.kotlindiscord.site

import com.kotlindiscord.database.connectToDb
import com.kotlindiscord.database.migrator.MigrationsManager
import com.kotlindiscord.site.components.*
import io.ktor.application.Application
import io.ktor.locations.KtorExperimentalLocationsAPI
import mu.KotlinLogging

@KtorExperimentalLocationsAPI
fun Application.main() {
    val logger = KotlinLogging.logger {}

    if (System.getenv().getOrDefault("SENTRY_DSN", null) != null) {
        // Install Sentry early here for safety
        installSentry(this)
    }

    logger.info { "Starting KDSite version ${buildInfo.version}." }

    // Connect to the database
    connectToDb()

    logger.info { "Connected to database. Applying migrations..." }

    // Apply migrations
    try {
        MigrationsManager().migrateAll()

        logger.info { "Database migrated successfully." }
    } catch (e: IllegalStateException) {
        logger.info { "Database already migrated." }
    }

    // Install Locations first since other components use it
    installLocations(this)

    // Install each component we need to use
    installAuth(this)
    installJson(this)
    installLogging(this)
    installPebble(this)
    installSessions(this)
    installWebsockets(this)

    // Now, set up routing
    installRouting(this)

    logger.info { "Setup completed." }
}
