package com.kotlindiscord.site.components

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.serialization.json

fun installJson(app: Application) {
    app.install(ContentNegotiation) {
        json()
    }
}
