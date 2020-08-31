package com.kotlindiscord.site.components

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.*
import org.slf4j.event.Level

fun installLogging(app: Application) {
    app.install(CallLogging) {
        level = Level.INFO
    }
}
