package com.kotlindiscord.site.routes.api

import com.kotlindiscord.database.Role
import com.kotlindiscord.site.components.apiRoute
import com.kotlindiscord.site.components.route
import com.kotlindiscord.site.models.RoleModel
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.util.getOrFail
import org.jetbrains.exposed.dao.exceptions.EntityNotFoundException
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction


val apiRolesDelete = route {
    newSuspendedTransaction {
        val id = call.parameters.getOrFail("id").toLong()

        val role = try {
            Role[id]
        } catch (e: EntityNotFoundException) {
            return@newSuspendedTransaction call.respond(HttpStatusCode.NotFound)
        }

        role.delete()
    }

    call.respond(HttpStatusCode.OK)
}

val apiRolesGet = apiRoute {
    newSuspendedTransaction {
        Role.all().map { RoleModel.fromDB(it) }
    }
}

val apiRolesPost = apiRoute {
    val model = call.receive<RoleModel>()

    newSuspendedTransaction {
        try {
            val role = Role[model.id]

            role.name = model.name
            role.colour = model.colour
        } catch (e: EntityNotFoundException) {
            Role.new(model.id) {
                name = model.name
                colour = model.colour
            }
        }

        call.respond(HttpStatusCode.OK)
    }
}
