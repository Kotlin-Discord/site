package com.kotlindiscord.site.components

import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.pebble.PebbleContent
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.util.pipeline.PipelineContext

fun redirect(url: String, permanent: Boolean = false): suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit {
    return {
        call.respondRedirect(url, permanent)
    }
}

fun route(body: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit):
        suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = body

fun template(path: String): suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit {
    return {
        call.respond(PebbleContent(path, mapOf()))
    }
}

fun installRouting(app: Application) {
    app.routing {
        static("static") {
            resources("static")
        }

        get("/", template("pages/index.html.peb"))

        get("/community", redirect("/", true))
        get("/community/code-of-conduct", template("pages/community/code-of-conduct.html.peb"))
        get("/community/privacy", template("pages/community/privacy.html.peb"))
        get("/community/rules", template("pages/community/rules.html.peb"))
    }
}
