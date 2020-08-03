package com.kotlindiscord.site.models

import com.kotlindiscord.api.client.enums.toInfractionType
import com.kotlindiscord.api.client.models.InfractionModel
import com.kotlindiscord.api.client.models.RoleModel
import com.kotlindiscord.api.client.models.UserModel
import com.kotlindiscord.database.Infraction
import com.kotlindiscord.database.Role
import com.kotlindiscord.database.User

fun fromDB(inf: Infraction): InfractionModel {
    return InfractionModel(
        id = inf.id.value,

        reason = inf.reason,
        type = inf.type.name.toInfractionType(),
        expires = inf.expires,
        created = inf.created,

        infractor = inf.infractor.id.value,
        user = inf.user.id.value
    )
}

fun fromDB(role: Role): RoleModel {
    return RoleModel(
        id = role.id.value,
        name = role.name,
        colour = role.colour
    )
}

fun fromDB(user: User): UserModel {
    return UserModel(
        id = user.id.value,
        username = user.userName,
        discriminator = user.discriminator,
        avatarUrl = user.avatarUrl,

        roles = user.roles.map { it.id.value }.toSet(),
        present = user.present
    )
}
