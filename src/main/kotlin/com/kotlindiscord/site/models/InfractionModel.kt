@file:UseSerializers(InstantSerialiser::class, InfractionTypeSerialiser::class)

package com.kotlindiscord.site.models

import com.kotlindiscord.database.Infraction
import com.kotlindiscord.site.emuns.InfractionType
import com.kotlindiscord.site.emuns.toInfractionType
import com.kotlindiscord.site.serialisers.InfractionTypeSerialiser
import com.kotlindiscord.site.serialisers.InstantSerialiser
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Instant
import java.time.ZoneOffset

@Serializable
data class InfractionModel(
    val id: Long,

    val infractor: Long,
    val user: Long,
    val reason: String,
    val type: InfractionType,

    val expires: Instant,
    val created: Instant
) {
    companion object {
        fun fromDB(inf: Infraction): InfractionModel {
            return InfractionModel(
                id = inf.id.value,

                reason = inf.reason,
                type = inf.type.name.toInfractionType(),
                expires = inf.expires.toInstant(ZoneOffset.UTC),
                created = inf.created.toInstant(ZoneOffset.UTC),

                infractor = inf.infractor.id.value,
                user = inf.user.id.value
            )
        }
    }
}
