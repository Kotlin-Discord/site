package com.kotlindiscord.site.routes.api

import com.kotlindiscord.api.client.models.InfractionFilterModel
import com.kotlindiscord.api.client.models.InfractionModel
import com.kotlindiscord.database.*
import com.kotlindiscord.site.components.apiRoute
import com.kotlindiscord.site.infractionsMutex
import com.kotlindiscord.site.models.fromDB
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import kotlinx.coroutines.sync.withLock
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.Instant
import java.time.LocalDateTime

val apiInfractionsGet = apiRoute {
    val model = call.receive<InfractionFilterModel>()

    return@apiRoute newSuspendedTransaction {
        val query = Infractions.selectAll()

        if (model.id != null) query.andWhere { Infractions.id eq model.id }

        if (model.reason != null) query.andWhere { Infractions.reason like model.reason!! }
        if (model.type != null) query.andWhere { Infractions.type eq InfractionTypes.valueOf(model.type!!.type) }

        if (model.createdAfter != null) query.andWhere {
            Infractions.created greaterEq LocalDateTime.from(model.createdAfter)
        }

        if (model.createdBefore != null) query.andWhere {
            Infractions.created lessEq LocalDateTime.from(model.createdBefore)
        }

        if (model.infractor != null) {
            val infractor = User.getOrNull(model.infractor!!) ?: return@newSuspendedTransaction call.respond(
                HttpStatusCode.NotFound,
                mapOf("error" to "Unknown infractor ID: ${model.infractor}")
            )

            query.andWhere { Infractions.infractor eq infractor.id }
        }

        if (model.user != null) {
            val user = User.getOrNull(model.user!!) ?: return@newSuspendedTransaction call.respond(
                HttpStatusCode.NotFound,
                mapOf("error" to "Unknown user ID: ${model.user}")
            )

            query.andWhere { Infractions.infractor eq user.id }
        }

        if (model.active != null) {
            if (model.active!!) {
                query.andWhere {
                    Infractions.expires.isNotNull() and (Infractions.expires greater LocalDateTime.from(Instant.now()))
                }
            } else {
                query.andWhere {
                    Infractions.expires.isNull() or (Infractions.expires lessEq LocalDateTime.from(Instant.now()))
                }
            }
        }

        query.map { fromDB(Infraction.wrapRow(it)) }
    }
}

val apiInfractionsPost = apiRoute {
    val model = call.receive<InfractionModel>()

    return@apiRoute newSuspendedTransaction {
        infractionsMutex.withLock {  // To prevent concurrent modifications
            val infractor = User.getOrNull(model.infractor) ?: return@newSuspendedTransaction call.respond(
                HttpStatusCode.NotFound,
                mapOf("error" to "Unknown infractor ID: ${model.infractor}")
            )

            val user = User.getOrNull(model.user) ?: return@newSuspendedTransaction call.respond(
                HttpStatusCode.NotFound,
                mapOf("error" to "Unknown user ID: ${model.user}")
            )

            if (model.id != null) {
                val infraction = Infraction.getOrNull(model.id!!) ?: return@newSuspendedTransaction call.respond(
                    HttpStatusCode.NotFound,
                    mapOf("error" to "Unknown infraction ID: ${model.infractor}")
                )

                infraction.created = model.created
                infraction.expires = model.expires

                infraction.reason = model.reason
                infraction.type = InfractionTypes.valueOf(model.type.type)

                infraction.infractor = infractor
                infraction.user = user
            } else {
                return@newSuspendedTransaction fromDB(
                    Infraction.new {
                        created = model.created
                        expires = model.expires

                        reason = model.reason
                        type = InfractionTypes.valueOf(model.type.type)

                        this.infractor = infractor
                        this.user = user
                    }
                )
            }
        }
    }
}
