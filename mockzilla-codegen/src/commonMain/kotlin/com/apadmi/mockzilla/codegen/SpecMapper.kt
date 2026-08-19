package com.apadmi.mockzilla.codegen

import com.apadmi.mockzilla.codegen.data.EndpointSpec
import com.apadmi.mockzilla.codegen.data.ExampleResponse
import io.swagger.models.HttpMethod
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
import kotlin.collections.plus

fun mapSpecToEndpoints(spec: OpenAPI): Array<EndpointSpec> {
    var endpointList: Array<EndpointSpec> = arrayOf()
    spec.paths.forEach { path ->
        for (method in HttpMethod.entries) {
            val op = getMethodOp(method, path.value)
            if (op != null) {
                endpointList += mapOperation(path.key, method, op)
            }
        }
    }
    return endpointList
}

fun getMethodOp(method: HttpMethod, item: PathItem): Operation? {
    return when (method) {
        HttpMethod.GET -> item.get
        HttpMethod.HEAD -> item.head
        HttpMethod.POST -> item.post
        HttpMethod.PUT -> item.put
        HttpMethod.DELETE -> item.delete
        HttpMethod.OPTIONS -> item.options
        HttpMethod.PATCH -> item.patch
    }
}

fun mapOperation(
    path: String,
    method: HttpMethod,
    operation: Operation,
): EndpointSpec {
    var examples: Array<ExampleResponse> = arrayOf()

    operation.responses?.forEach { (statusKey, response) ->
        examples += exampleResponses(statusKey, response)
    }

    val success = examples.firstOrNull { example ->
        example.statusCode in 200 until 300
    }
    val error = examples.firstOrNull { example ->
        example.statusCode in 400 until 600
    }

    return EndpointSpec(
        path,
        method,
        success ?: ExampleResponse(
            200,
            "Empty Success"
        ),
        error ?: ExampleResponse(
            400,
            "Error"
        ),
        examples.withUniqueNames()
    )
}

private fun Array<ExampleResponse>.withUniqueNames(): Array<ExampleResponse> {
    val counts = mutableMapOf<String, Int>()
    return map { example ->
        val count = counts.merge(example.name, 1) { old, _ -> old + 1 }
        if (count == 1) example else example.copy(name = "${example.name} ($count)")
    }.toTypedArray()
}