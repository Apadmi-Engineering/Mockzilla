package com.apadmi.mockzilla.codegen

import com.apadmi.mockzilla.codegen.data.ExampleResponse
import io.swagger.v3.core.util.Json
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.media.*

fun exampleResponses(statusKey: String, response: ApiResponse): Array<ExampleResponse> {
    val descStart = if (statusKey == "default") "Default: " else ""
    val description = descStart + response.description
    val statusCode = statusCodeFrom(statusKey)
    val mediaType = response.content?.get("application/json") ?: return arrayOf(
        ExampleResponse(
            statusCode,
            description.toTitleCase(),
            ""
        )
    )

    if (!mediaType.examples.isNullOrEmpty()) {
        return mediaType.examples.entries.map { entry ->
            val example = entry.value
            ExampleResponse(
                statusCode,
                entry.key.toTitleCase(),
                Json.mapper().writeValueAsString(example.value)
            )
        }.toTypedArray()
    }

    val bodyValue = Json.mapper().writeValueAsString(mediaType.example ?: generateFromSchema(mediaType.schema))

    return arrayOf(
        ExampleResponse(
            statusCode,
            description,
            bodyValue
        )
    )
}

fun generateFromSchema(schema: Schema<*>?): Any? {
    if (schema == null) return null

    val enumValues = schema.enum
    if (!enumValues.isNullOrEmpty()) {
        return schema.example ?: enumValues.first() ?: "string"
    }

    return when (schema) {
        is BooleanSchema -> schema.example ?: true
        is IntegerSchema -> schema.example ?: schema.maximum ?: schema.minimum ?: 0
        is NumberSchema -> schema.example ?: schema.maximum ?: schema.minimum ?: 0.0
        is StringSchema, is BinarySchema, is FileSchema -> schema.example ?: "string"
        is UUIDSchema -> schema.example ?: "3fa85f64-5717-4562-b3fc-2c963f66afa6"
        is DateSchema -> schema.example ?: "2024-01-01"
        is DateTimeSchema -> schema.example ?: "2024-01-01T00:00:00Z"
        is EmailSchema -> schema.example ?: "user@example.com"
        is PasswordSchema -> schema.example ?: "password"
        is ByteArraySchema -> schema.example ?: "U3dhZ2dlciByb2Nrcw=="
        is ArraySchema -> schema.example ?: listOf(generateFromSchema(schema.items))
        is MapSchema ->
            if (schema.example == null) {
                val valueSchema = schema.additionalProperties as? Schema<*>
                if (valueSchema == null) emptyMap() else mapOf("key" to generateFromSchema(valueSchema))
            } else { schema.example }
        is ObjectSchema ->
            if (schema.example == null) {
                val result = mutableMapOf<String, Any?>()
                schema.properties?.forEach { (name, propertySchema) -> result[name] = generateFromSchema(propertySchema) }
                result
            } else { schema.example }
        is ArbitrarySchema, is JsonSchema -> genericFallback(schema)
        is ComposedSchema -> {
            if (!schema.allOf.isNullOrEmpty() || !schema.properties.isNullOrEmpty()) {
                val merged = mutableMapOf<String, Any?>()
                // The cast below is unchecked (JVM erasure can't verify a Map's key
                // type at runtime) but sound in practice: every Map this function
                // ever returns is built with String keys (see MapSchema/ObjectSchema/
                // genericFallback above, and this same merged map). When a member
                // is a non-object schema (a primitive/array allOf member, or a
                // oneOf/anyOf branch that isn't Map-shaped), the safe cast (`as?`)
                // yields null and `putAll` is simply skipped — never a crash.
                @Suppress("UNCHECKED_CAST")
                schema.allOf?.forEach { sub -> (generateFromSchema(sub) as? Map<String, Any?>)?.let { merged.putAll(it) } }
                @Suppress("UNCHECKED_CAST")
                schema.oneOf?.firstOrNull()?.let { (generateFromSchema(it) as? Map<String, Any?>)?.let(merged::putAll) }
                @Suppress("UNCHECKED_CAST")
                schema.anyOf?.firstOrNull()?.let { (generateFromSchema(it) as? Map<String, Any?>)?.let(merged::putAll) }
                schema.properties?.forEach { (name, propertySchema) ->
                    merged[name] = generateFromSchema(propertySchema)
                }
                merged
            } else {
                (schema.oneOf?.firstOrNull() ?: schema.anyOf?.firstOrNull())?.let { generateFromSchema(it) }
            }
        }
        else -> genericFallback(schema)
    }
}

// todo check this
private fun genericFallback(schema: Schema<*>): Any? {
    val valueSchema = schema.additionalProperties as? Schema<*>
    return when {
        valueSchema != null -> mapOf("key" to generateFromSchema(valueSchema))
        !schema.properties.isNullOrEmpty() -> {
            val result = mutableMapOf<String, Any?>()
            schema.properties.forEach { (name, propertySchema) -> result[name] = generateFromSchema(propertySchema) }
            result
        }

        else -> schema.example
    }
}

private fun statusCodeFrom(key: String): Int {
    if (key == "default") return 200;
    val normalized = key.uppercase().replace('X', '0')
    return normalized.toIntOrNull() ?: 200;
}

private fun String.toTitleCase(): String {
    return "${this[0].uppercaseChar()}${substring(1)}";
}