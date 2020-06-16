package com.kotlindiscord.site.models

import com.kotlindiscord.database.User
import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    val id: Long,

    val username: String,
    val discriminator: String,
    val avatarUrl: String,
    val roles: List<Long> = listOf()
) {
    companion object {
        fun fromDB(user: User): UserModel {
            return UserModel(
                id = user.id.value,
                username = user.userName,
                discriminator = user.discriminator,
                avatarUrl = user.avatarUrl,

                roles = user.roles.map { it.id.value }
            )
        }
    }
}
