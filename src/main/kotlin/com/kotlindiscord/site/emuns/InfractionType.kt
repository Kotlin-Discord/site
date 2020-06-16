package com.kotlindiscord.site.emuns

import javax.naming.InvalidNameException

enum class InfractionType(val type: String) {
    KICK("Kick"),
    BAN("Ban"),
    WARN("Warn");

    override fun toString(): String = this.type
}

fun String.toInfractionType(): InfractionType {
    val titleValue = this.capitalize()

    for (type in InfractionType.values()) {
        if (type.type == titleValue) {
            return type
        }
    }

    throw InvalidNameException("No such infraction type: $titleValue")
}
