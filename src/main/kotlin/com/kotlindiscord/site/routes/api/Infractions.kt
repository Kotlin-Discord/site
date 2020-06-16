package com.kotlindiscord.site.routes.api

import com.kotlindiscord.database.Infraction
import com.kotlindiscord.database.InfractionTypes
import com.kotlindiscord.database.Infractions
import com.kotlindiscord.database.User
import com.kotlindiscord.site.components.route
import com.kotlindiscord.site.models.InfractionFilterModel
import com.kotlindiscord.site.models.InfractionModel
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import org.jetbrains.exposed.dao.exceptions.EntityNotFoundException
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.Instant
import java.time.LocalDateTime

val apiInfractionsGet = route {
    val model = call.receive<InfractionFilterModel>()

    if (model.createdAfter != null && model.createdBefore != null) return@route call.respond(
        HttpStatusCode.NotAcceptable,
        mapOf("error" to "Specify one of createdAfter and createdBefore, not both.")
    )

    newSuspendedTransaction {
        val query = Infractions.selectAll()

        if (model.id != null) query.andWhere { Infractions.id eq model.id }

        if (model.reason != null) query.andWhere { Infractions.reason like model.reason }
        if (model.type != null) query.andWhere { Infractions.type eq InfractionTypes.valueOf(model.type.type) }
        if (model.createdAfter != null) query.andWhere { Infractions.created lessEq model.createdAfter }
        if (model.createdBefore != null) query.andWhere { Infractions.created greaterEq model.createdBefore }

        if (model.infractor != null) {
            val infractor = getUser(model.infractor) ?: return@newSuspendedTransaction call.respond(
                HttpStatusCode.NotFound,
                mapOf("error" to "Unknown infractor ID: ${model.infractor}")
            )

            query.andWhere { Infractions.infractor eq infractor.id }
        }

        if (model.user != null) {
            val user = getUser(model.user) ?: return@newSuspendedTransaction call.respond(
                HttpStatusCode.NotFound,
                mapOf("error" to "Unknown user ID: ${model.user}")
            )

            query.andWhere { Infractions.infractor eq user.id }
        }

        if (model.active != null) {
            if (model.active) {
                query.andWhere {
                    Infractions.expires.isNotNull() and (Infractions.expires greater Instant.now())
                }
            } else {
                query.andWhere {
                    Infractions.expires lessEq Instant.now() or Infractions.expires.isNull()
                }
            }
        }

        call.respond(HttpStatusCode.OK, query.toList())
    }
}

val apiInfractionsPost = route {
    val model = call.receive<InfractionModel>()

    val infractor = getUser(model.infractor) ?: return@route call.respond(
        HttpStatusCode.NotFound,
        mapOf("error" to "Unknown infractor ID: ${model.infractor}")
    )

    val user = getUser(model.user) ?: return@route call.respond(
        HttpStatusCode.NotFound,
        mapOf("error" to "Unknown user ID: ${model.user}")
    )

    Infraction.new(model.id) {
        created = LocalDateTime.from(model.created)
        expires = LocalDateTime.from(model.expires)

        reason = model.reason
        type = InfractionTypes.valueOf(model.type.type)

        this.infractor = infractor
        this.user = user
    }

    call.respond(HttpStatusCode.OK)
}

private suspend fun getUser(id: Long): User? = newSuspendedTransaction {
    try {
        User[id]
    } catch (e: EntityNotFoundException) {
        null
    }
}
