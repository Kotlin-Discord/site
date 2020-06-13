package com.kotlindiscord.site.models

import kotlinx.serialization.Serializable

@Serializable
data class RoleModel(
    val id: Long,

    val name: String,
    val colour: Int
)
