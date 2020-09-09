package com.kotlindiscord.site.routes.api

import com.kotlindiscord.api.client.models.RoleModel
import com.kotlindiscord.database.Role
import com.kotlindiscord.database.getOrNull
import com.kotlindiscord.site.components.apiRoute
import com.kotlindiscord.site.models.fromDB
import com.kotlindiscord.site.rolesMutex
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.util.*
import kotlinx.coroutines.sync.withLock
import mu.KotlinLogging
import org.jetbrains.exposed.dao.exceptions.EntityNotFoundException
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

private val logger = KotlinLogging.logger {}


val apiRolesDelete = apiRoute {
    newSuspendedTransaction {
        rolesMutex.withLock {
            val id = call.parameters.getOrFail("id").toLong()
            val role = Role.getOrNull(id) ?: return@newSuspendedTransaction call.respond(HttpStatusCode.NotFound)

            role.holders.forEach {
                it.roles = SizedCollection(it.roles.toList().filter { entry -> entry.id != role.id })
            }
            role.delete()
        }
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
        rolesMutex.withLock {
            try {
                val role = Role[model.id]

                role.name = model.name
                role.colour = model.colour
            } catch (e: EntityNotFoundException) {
                logger.info(e) { "Entity not found: ${model.id}" }

                Role.new(model.id) {
                    name = model.name
                    colour = model.colour
                }
            }
        }
    }

    null
}
