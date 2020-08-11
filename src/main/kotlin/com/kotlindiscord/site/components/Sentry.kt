package com.kotlindiscord.site.components

import io.ktor.application.Application
import io.ktor.application.install
import se.zensum.ktorSentry.SentryFeature

fun installSentry(app: Application) {
    app.install(SentryFeature) {
        appEnv = System.getProperty("SENTRY_ENVIRONMENT", "dev")
    }
}
