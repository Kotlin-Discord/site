package com.kotlindiscord.site.templates

import com.mitchellbosecke.pebble.attributes.AttributeResolver
import com.mitchellbosecke.pebble.extension.*
import com.mitchellbosecke.pebble.extension.Function
import com.mitchellbosecke.pebble.operator.BinaryOperator
import com.mitchellbosecke.pebble.operator.UnaryOperator
import com.mitchellbosecke.pebble.tokenParser.TokenParser

class KotDisExtension : Extension {
    private val functions: MutableMap<String, Function> = mutableMapOf(
        "role" to RoleFunction()
    )

    override fun getBinaryOperators(): MutableList<BinaryOperator> = mutableListOf()
    override fun getGlobalVariables(): MutableMap<String, Any> = mutableMapOf()
    override fun getAttributeResolver(): MutableList<AttributeResolver> = mutableListOf()
    override fun getFilters(): MutableMap<String, Filter> = mutableMapOf()
    override fun getNodeVisitors(): MutableList<NodeVisitorFactory> = mutableListOf()
    override fun getFunctions(): MutableMap<String, Function> = functions
    override fun getTests(): MutableMap<String, Test> = mutableMapOf()
    override fun getTokenParsers(): MutableList<TokenParser> = mutableListOf()
    override fun getUnaryOperators(): MutableList<UnaryOperator> = mutableListOf()
}
