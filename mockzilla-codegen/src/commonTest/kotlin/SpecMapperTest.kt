import com.apadmi.mockzilla.codegen.mapSpecToEndpoints
import io.swagger.models.HttpMethod
import io.swagger.parser.OpenAPIParser
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.parser.core.models.ParseOptions
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private fun parseSpec(yaml: String): OpenAPI {
    val options = ParseOptions().apply { isResolve = true; isResolveFully = true }
    val result = OpenAPIParser().readContents(yaml.trimIndent(), null, options)
    return result.openAPI ?: error("Test fixture failed to parse: ${result.messages}")
}

class SpecMapperTest {

    @Test
    fun `every declared operation on a path becomes its own EndpointSpec`() {
        val spec = parseSpec(
            """
            openapi: 3.0.3
            info: {title: Test, version: '1.0'}
            paths:
              /widgets:
                get:
                  responses:
                    '200': {description: ok}
                post:
                  responses:
                    '201': {description: created}
            """
        )

        val endpoints = mapSpecToEndpoints(spec)

        assertEquals(2, endpoints.size)
        assertTrue(endpoints.any { it.method == HttpMethod.GET })
        assertTrue(endpoints.any { it.method == HttpMethod.POST })
        assertTrue(endpoints.all { it.path == "/widgets" })
    }

    @Test
    fun `a trace-only operation is silently skipped, not crashed on`() {
        val spec = parseSpec(
            """
            openapi: 3.0.3
            info: {title: Test, version: '1.0'}
            paths:
              /widgets:
                trace:
                  responses:
                    '200': {description: ok}
            """
        )

        assertEquals(0, mapSpecToEndpoints(spec).size)
    }

    @Test
    fun `multiple paths each produce their own endpoint`() {
        val spec = parseSpec(
            """
            openapi: 3.0.3
            info: {title: Test, version: '1.0'}
            paths:
              /widgets:
                get:
                  responses:
                    '200': {description: ok}
              /widgets/{id}:
                get:
                  responses:
                    '200': {description: ok}
            """
        )

        val paths = mapSpecToEndpoints(spec).map { it.path }.toSet()
        assertEquals(setOf("/widgets", "/widgets/{id}"), paths)
    }

    @Test
    fun `the first documented 2xx becomes the success response, by declaration order`() {
        val spec = parseSpec(
            """
            openapi: 3.0.3
            info: {title: Test, version: '1.0'}
            paths:
              /widgets:
                get:
                  responses:
                    '201':
                      description: created
                      content:
                        application/json:
                          schema: {type: string, example: created}
                    '200':
                      description: ok
                      content:
                        application/json:
                          schema: {type: string, example: ok}
            """
        )

        val endpoint = mapSpecToEndpoints(spec).single()

        // Declaration order is 201 then 200, so 201 — the first 2xx encountered — wins.
        assertEquals(201, endpoint.successResponse.statusCode)
        assertEquals("\"created\"", endpoint.successResponse.bodyValue)
    }

    @Test
    fun `the first documented 4xx-5xx becomes the error response, by declaration order`() {
        val spec = parseSpec(
            """
            openapi: 3.0.3
            info: {title: Test, version: '1.0'}
            paths:
              /widgets:
                get:
                  responses:
                    '200': {description: ok}
                    '404':
                      description: not found
                      content:
                        application/json:
                          schema: {type: string, example: "not found"}
                    '409':
                      description: conflict
                      content:
                        application/json:
                          schema: {type: string, example: conflict}
            """
        )

        val endpoint = mapSpecToEndpoints(spec).single()

        assertEquals(404, endpoint.errorResponse.statusCode)
        assertEquals("\"not found\"", endpoint.errorResponse.bodyValue)
    }

    @Test
    fun `a lone 5xx with no 4xx present still becomes the error response`() {
        val spec = parseSpec(
            """
            openapi: 3.0.3
            info: {title: Test, version: '1.0'}
            paths:
              /widgets:
                get:
                  responses:
                    '200': {description: ok}
                    '500':
                      description: server error
                      content:
                        application/json:
                          schema: {type: string, example: "server error"}
            """
        )

        val endpoint = mapSpecToEndpoints(spec).single()

        assertEquals(500, endpoint.errorResponse.statusCode)
        assertEquals("\"server error\"", endpoint.errorResponse.bodyValue)
    }

    @Test
    fun `when no 2xx or 4xx-5xx are documented at all, defaults are used`() {
        val spec = parseSpec(
            """
            openapi: 3.0.3
            info: {title: Test, version: '1.0'}
            paths:
              /widgets:
                get:
                  responses:
                    '304': {description: not modified}
            """
        )

        val endpoint = mapSpecToEndpoints(spec).single()

        assertEquals(200, endpoint.successResponse.statusCode)
        assertEquals("Empty Success", endpoint.successResponse.name)
        assertEquals(400, endpoint.errorResponse.statusCode)
        assertEquals("Error", endpoint.errorResponse.name)
    }

    @Test
    fun `otherResponses contains every flattened response variant, unfiltered`() {
        val spec = parseSpec(
            """
            openapi: 3.0.3
            info: {title: Test, version: '1.0'}
            paths:
              /widgets:
                get:
                  responses:
                    '200': {description: ok}
                    '404': {description: not found}
            """
        )

        val endpoint = mapSpecToEndpoints(spec).single()

        assertEquals(2, endpoint.otherResponses.size)
        assertEquals(setOf(200, 404), endpoint.otherResponses.map { it.statusCode }.toSet())
    }

    @Test
    fun `multiple named examples on one status code all survive into otherResponses`() {
        val spec = parseSpec(
            """
            openapi: 3.0.3
            info: {title: Test, version: '1.0'}
            paths:
              /widgets:
                get:
                  responses:
                    '200':
                      description: ok
                      content:
                        application/json:
                          schema: {type: array, items: {type: string}}
                          examples:
                            empty: {value: []}
                            oneItem: {value: ["a"]}
            """
        )

        val endpoint = mapSpecToEndpoints(spec).single()

        assertEquals(2, endpoint.otherResponses.size)
        assertEquals(setOf("Empty", "OneItem"), endpoint.otherResponses.map { it.name }.toSet())
    }
}
