package com.kotlindiscord.site.routes.api

import com.kotlindiscord.database.Role
import com.kotlindiscord.database.User
import com.kotlindiscord.site.components.apiRoute
import com.kotlindiscord.site.components.route
import com.kotlindiscord.site.models.UserModel
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.util.getOrFail
import org.jetbrains.exposed.dao.exceptions.EntityNotFoundException
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction


val apiUsersGet = apiRoute {
    newSuspendedTransaction {
        User.all().map { UserModel.fromDB(it) }
    }
}

val apiUsersGetSingle = apiRoute {
    newSuspendedTransaction {
        val id = call.parameters.getOrFail("id").toLong()

        val user = try {
            User[id]
        } catch (e: EntityNotFoundException) {
            return@newSuspendedTransaction call.respond(HttpStatusCode.NotFound)
        }

        UserModel.fromDB(user)
    }
}

val apiUsersPost = route {  // This one doesn't return any actual data
    val model = call.receive<UserModel>()

    newSuspendedTransaction {
        try {
            val user = User[model.id]

            user.userName = model.username
            user.discriminator = model.discriminator
            user.avatarUrl = model.avatarUrl

            val givenRoles = user.roles.map { it.id.value }
            val roles = user.roles.filter { model.roles.contains(it.id.value) }.toMutableList()

            for (roleId in model.roles - givenRoles) {
                try {
                    roles.add(Role[roleId])
                } catch (e: EntityNotFoundException) {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Unknown role ID: $roleId"))

                    return@newSuspendedTransaction
                }
            }

            user.roles = SizedCollection(roles)
        } catch (e: EntityNotFoundException) {
            val roles = mutableListOf<Role>()

            for (roleId in model.roles) {
                try {
                    roles.add(Role[roleId])
                } catch (e: EntityNotFoundException) {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Unknown role ID: $roleId"))

                    return@newSuspendedTransaction
                }
            }

            User.new(model.id) {
                userName = model.username
                discriminator = model.discriminator
                avatarUrl = model.avatarUrl

                this.roles = SizedCollection(roles)
            }
        }

        if (call.response.status() == null) call.respond(HttpStatusCode.OK)
    }
}
