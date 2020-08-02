package com.kotlindiscord.site.components

import com.kotlindiscord.site.templates.KotDisExtension
import com.mitchellbosecke.pebble.loader.ClasspathLoader
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.pebble.Pebble

fun installPebble(app: Application) {
    app.install(Pebble) {
        loader(
            ClasspathLoader().apply {
                prefix = "templates/"
                extension(KotDisExtension())
            }
        )
    }
}
