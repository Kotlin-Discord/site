@file:UseSerializers(InstantSerialiser::class, InfractionTypeSerialiser::class)

package com.kotlindiscord.site.models

import com.kotlindiscord.site.emuns.InfractionType
import com.kotlindiscord.site.serialisers.InfractionTypeSerialiser
import com.kotlindiscord.site.serialisers.InstantSerialiser
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Instant

@Serializable
data class InfractionFilterModel(
    val id: Long?,

    val infractor: Long?,
    val user: Long?,

    val reason: String?,
    val type: InfractionType?,

    val createdAfter: Instant?,
    val createdBefore: Instant?,

    val active: Boolean?
)
