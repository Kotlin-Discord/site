package com.kotlindiscord.site.templates

import com.mitchellbosecke.pebble.extension.Function
import com.mitchellbosecke.pebble.extension.escaper.SafeString
import com.mitchellbosecke.pebble.template.EvaluationContext
import com.mitchellbosecke.pebble.template.PebbleTemplate

private val template = """
    <span class="ui text color-{CLASS}">@{ROLE}</span>
""".trimIndent()

class RoleFunction : Function {
    override fun getArgumentNames(): MutableList<String> =mutableListOf("role")

    override fun execute(
        args: MutableMap<String, Any>?,
        self: PebbleTemplate?,
        context: EvaluationContext?,
        lineNumber: Int
    ): Any {
        val role = args!!["role"] as String

        return SafeString(
            template
                .replace("{CLASS}", role.replace(' ', '-').toLowerCase())
                .replace("{ROLE}", role.capitalize())
        )
    }
}
