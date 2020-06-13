@file:UseSerializers(InstantSerialiser::class)

package com.kotlindiscord.site.models

import com.kotlindiscord.site.serialisers.InstantSerialiser
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Instant

@Serializable
data class InfractionModel(
    val id: Long,

    val infractor: UserModel,
    val user: UserModel,
    val reason: String,
    val type: String,

    val expires: Instant,
    val created: Instant
)
