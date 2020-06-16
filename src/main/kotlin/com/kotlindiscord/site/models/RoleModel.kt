package com.kotlindiscord.site.models

import com.kotlindiscord.database.Role
import kotlinx.serialization.Serializable

@Serializable
data class RoleModel(
    val id: Long,

    val name: String,
    val colour: Int
) {
    companion object {
        fun fromDB(role: Role): RoleModel {
            return RoleModel(
                id = role.id.value,
                name = role.name,
                colour = role.colour
            )
        }
    }
}
