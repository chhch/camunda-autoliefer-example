package io.github.chhch.autoliefer.tasks.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class ProcessDefinition(var key: String, var name: String, var resource: String)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MessageBody(
        val messageName: String,
        val processVariables: Map<String, TypedVariable>,
        val correlationKeys: Map<String, TypedVariable> = emptyMap()
)

data class TypedVariable(val value: String, val type: String)