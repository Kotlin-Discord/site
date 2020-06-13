package com.kotlindiscord.site.models

import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    val id: Long,

    val username: String,
    val discriminator: String,
    val avatarUrl: String
)
