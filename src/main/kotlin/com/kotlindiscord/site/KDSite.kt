package com.kotlindiscord.site

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.form

fun Application.main() {
    install(Authentication) {
        form("form") {

        }
    }
}
