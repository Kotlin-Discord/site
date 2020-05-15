package com.kotlindiscord.site.routes

import com.kotlindiscord.site.components.route
import io.ktor.application.call
import io.ktor.pebble.PebbleContent
import io.ktor.response.respond

val indexGet = route {
    call.respond(PebbleContent("pages/index.html.peb", mapOf("who" to "world")))
}
