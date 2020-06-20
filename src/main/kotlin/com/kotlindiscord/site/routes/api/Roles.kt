package com.kotlindiscord.site.routes.api

import com.kotlindiscord.api.client.models.RoleModel
import com.kotlindiscord.database.Role
import com.kotlindiscord.database.getOrNull
import com.kotlindiscord.site.components.apiRoute
import com.kotlindiscord.site.models.fromDB
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.util.getOrFail
import org.jetbrains.exposed.dao.exceptions.EntityNotFoundException
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction


val apiRolesDelete = apiRoute {
    newSuspendedTransaction {
        val id = call.parameters.getOrFail("id").toLong()
        val role = Role.getOrNull(id) ?: return@newSuspendedTransaction call.respond(HttpStatusCode.NotFound)

        role.delete()
    }

    null
}

val apiRolesGet = apiRoute {
    newSuspendedTransaction {
        Role.all().map { fromDB(it) }
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
    }

    null
}
