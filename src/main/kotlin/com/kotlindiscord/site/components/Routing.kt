package com.kotlindiscord.site.components

import com.kotlindiscord.site.routes.api.apiIndex
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

fun apiRoute(body: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Any?):
        suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
    // TODO: Validate API authentication
    // BODY: Once we have some kind of API auth validation, we should action it here.
    // BODY: We should also pass the auth details along, so the route itself can check perms and such.

    val resp = body.invoke(this, subject)

    if (resp != null) {
        call.respond(resp)
    }
}

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

        get("/api", apiIndex)

        get("/docs", redirect("/"))
        get("/resources", redirect("/"))

        get("/docs/code-of-conduct", template("pages/docs/code-of-conduct.html.peb"))
        get("/docs/privacy", template("pages/docs/privacy.html.peb"))
        get("/docs/rules", template("pages/docs/rules.html.peb"))

        get("/resources/tools", template("pages/resources/tools.html.peb"))
        get("/resources/tutorials", template("pages/resources/tutorials.html.peb"))
    }
}
