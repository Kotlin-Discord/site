package com.kotlindiscord.site.components

import com.kotlindiscord.site.routes.api.*
import com.kotlindiscord.site.routes.rolesCss
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.pebble.PebbleContent
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.util.pipeline.PipelineContext

private val API_KEY = System.getenv("API_KEY")

fun redirect(url: String, permanent: Boolean = false): suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit {
    return {
        call.respondRedirect(url, permanent)
    }
}

fun route(body: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit):
        suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = body

fun apiRoute(body: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Any?):
        suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
    // TODO: Richer API auth
    // BODY: Once we have some kind of API auth validation, we should action it here.
    // BODY: We should also pass the auth details along, so the route itself can check perms and such.

    val key = call.request.headers.get("X-Api-Key")

    if (key == null || key != API_KEY) {
        call.respond(HttpStatusCode.Forbidden)
    } else {
        val resp = body.invoke(this, subject)

        when {
            resp != null && call.response.status() == null -> call.respond(HttpStatusCode.OK, resp)
            resp != null -> call.respond(resp)

            else -> call.respond(HttpStatusCode.OK)
        }
    }
}

fun template(
    path: String,
    data: Map<String, Any> = mapOf()
): suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit {
    return {
        call.respond(PebbleContent(path, data))
    }
}

suspend fun template(
    path: String,
    data: Map<String, Any> = mapOf(),
    contentType: ContentType = ContentType.Text.Html,
    call: ApplicationCall
) {
    call.respond(PebbleContent(path, data, contentType = contentType))
}


fun installRouting(app: Application) {
    app.routing {
        static("static") {
            resources("static")
        }

        get("/", template("pages/index.html.peb"))
        get("/generated/roles.css", rolesCss)

        get("/api", apiIndex)

        get("/api/infractions", apiInfractionsGet)
        post("/api/infractions", apiInfractionsPost)

        delete("/api/roles/{id}", apiRolesDelete)
        get("/api/roles", apiRolesGet)
        post("/api/roles", apiRolesPost)

        get("/api/users", apiUsersGet)
        get("/api/users/{id}", apiUsersGetSingle)
        post("/api/users", apiUsersPost)

        get("/docs", redirect("/"))
        get("/resources", redirect("/"))

        get("/docs/code-of-conduct", template("pages/docs/code-of-conduct.html.peb"))
        get("/docs/privacy", template("pages/docs/privacy.html.peb"))
        get("/docs/rules", template("pages/docs/rules.html.peb"))

        get("/resources/tools", template("pages/resources/tools.html.peb"))
        get("/resources/tutorials", template("pages/resources/tutorials.html.peb"))
    }
}
