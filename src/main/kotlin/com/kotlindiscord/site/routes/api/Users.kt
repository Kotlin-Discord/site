package com.kotlindiscord.site.routes.api

import com.kotlindiscord.api.client.models.UserModel
import com.kotlindiscord.database.Role
import com.kotlindiscord.database.User
import com.kotlindiscord.database.getOrNull
import com.kotlindiscord.site.components.apiRoute
import com.kotlindiscord.site.models.fromDB
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
        User.all().map { fromDB(it) }
    }
}

val apiUsersGetSingle = apiRoute {
    newSuspendedTransaction {
        val id = call.parameters.getOrFail("id").toLong()
        val user = User.getOrNull(id) ?: return@newSuspendedTransaction call.respond(HttpStatusCode.NotFound)

        fromDB(user)
    }
}

val apiUsersPost = apiRoute {
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
                val role = Role.getOrNull(roleId) ?: return@newSuspendedTransaction call.respond(
                    HttpStatusCode.NotFound,
                    mapOf("error" to "Unknown role ID: $roleId")
                )

                roles.add(role)
            }

            user.roles = SizedCollection(roles)
        } catch (e: EntityNotFoundException) {
            val roles = mutableListOf<Role>()

            for (roleId in model.roles) {
                val role = Role.getOrNull(roleId) ?: return@newSuspendedTransaction call.respond(
                    HttpStatusCode.NotFound,
                    mapOf("error" to "Unknown role ID: $roleId")
                )

                roles.add(role)
            }

            User.new(model.id) {
                userName = model.username
                discriminator = model.discriminator
                avatarUrl = model.avatarUrl

                this.roles = SizedCollection(roles)
            }
        }
    }

    null
}
