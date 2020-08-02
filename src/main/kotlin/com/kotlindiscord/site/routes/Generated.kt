package com.kotlindiscord.site.routes

import com.kotlindiscord.database.Role
import com.kotlindiscord.database.Roles
import com.kotlindiscord.site.components.route
import com.kotlindiscord.site.components.template
import com.kotlindiscord.site.models.fromDB
import io.ktor.application.call
import io.ktor.http.ContentType
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.Instant

private const val CACHE_SECONDS: Long = 60 * 30;  // 30 minutes

private var roleCache: Map<String, List<Map<String, String>>>? = null
private var roleCacheUpdated = Instant.now()

val rolesCss = route {
    val now = Instant.now()

    if (roleCache == null || now.minusSeconds(CACHE_SECONDS) > roleCacheUpdated) {
        val roles = newSuspendedTransaction {
            Role.all().orderBy(Roles.name to SortOrder.ASC).map { fromDB(it) }
        }

        @Suppress("MagicNumber")
        roleCache = mapOf("roles" to roles.map {
            mapOf(
                "name" to it.name.replace(' ', '-').toLowerCase(),
                "colour" to it.colour.toString(16)
            )
        })

        roleCacheUpdated = now
    }

    template(
        "css/roles.css.peb",
        roleCache!!,
        ContentType.Text.CSS,
        call = call
    )
}
