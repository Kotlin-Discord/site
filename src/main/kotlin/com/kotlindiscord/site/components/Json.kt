package com.kotlindiscord.site.components

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson

fun installJson(app: Application) {
    app.install(ContentNegotiation) {
        gson {
            disableHtmlEscaping()
            enableComplexMapKeySerialization()
            serializeNulls()
            serializeSpecialFloatingPointValues()
            setLenient()
        }
    }
}
