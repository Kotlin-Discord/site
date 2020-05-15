@file:Suppress("MatchingDeclarationName")  // In this case, it doesn't make anything easier to understand

package com.kotlindiscord.site.components

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.*
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.location
import io.ktor.locations.url
import io.ktor.routing.param
import io.ktor.routing.routing

@KtorExperimentalLocationsAPI
@Location("/login") class LoginLocation

val discordProvider = OAuthServerSettings.OAuth2ServerSettings(
    name = "discord",

    authorizeUrl = "https://discord.com/api/oauth2/authorize",
    accessTokenUrl = "https://discord.com/api/oauth2/token",

    clientId = System.getenv("DISCORD_CLIENT_ID"),
    clientSecret = System.getenv("DISCORD_CLIENT_SECRET"),

    defaultScopes = listOf("identify")
)

@KtorExperimentalLocationsAPI
fun installAuth(app: Application) {
    app.install(Authentication) {
        oauth("discord") {
            client = HttpClient(Apache)
            providerLookup = { discordProvider }
            urlProvider = { url(LoginLocation()) }
        }
    }

    app.routing {
        authenticate("discord") {
            location<LoginLocation>() {
                param("error") {
                    handle {
                        // TODO: Error ??????????????????????????
                    }
                }

                handle {
                    val principal = call.authentication.principal<OAuthAccessTokenResponse.OAuth2>()

                    if (principal != null) {
                        // TODO: Success ????????????????
                    } else {
                        // TODO: Render login page ??????????
                    }
                }
            }
        }
    }
}
