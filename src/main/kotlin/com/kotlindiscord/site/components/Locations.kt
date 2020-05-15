package com.kotlindiscord.site.components

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.locations.Locations

fun installLocations(app: Application) {
    app.install(Locations)
}
