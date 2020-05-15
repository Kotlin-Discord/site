package com.kotlindiscord.site.routes

import com.kotlindiscord.site.components.route
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.pebble.PebbleContent
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing

fun register(app: Application) {
    app.routing {
        get("/", indexGet)
    }
}

val indexGet = route {
    call.respond(PebbleContent("index.html.peb", mapOf("who" to "world")))
}
