package com.kotlindiscord.site.components

import com.kotlindiscord.site.routes.indexGet
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.util.pipeline.PipelineContext

fun route(body: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit):
        suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = body

fun installRouting(app: Application) {
    app.routing {
        get("/", indexGet)
    }
}
