import com.apadmi.mockzilla.codegen.exampleResponses
import com.apadmi.mockzilla.codegen.generateFromSchema
import io.swagger.parser.OpenAPIParser
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.parser.core.models.ParseOptions
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals

private fun parseSpec(yaml: String): OpenAPI {
    val options = ParseOptions().apply { isResolve = true; isResolveFully = true }
    val result = OpenAPIParser().readContents(yaml.trimIndent(), null, options)
    return result.openAPI ?: error("Test fixture failed to parse: ${result.messages}")
}

private fun targetSchema(componentsYaml: String): Schema<*> {
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
                      schema:
                        ${'$'}ref: '#/components/schemas/Target'
        components:
          schemas:
        $componentsYaml
        """
    )
    return spec.paths["/widgets"]!!.get.responses["200"]!!.content["application/json"]!!.schema
}

class SchemaExamplesTest {
    @Test
    fun `boolean without an example defaults to true`() {
        val schema = targetSchema(
            """
                Target:
                  type: boolean
            """
        )
        assertEquals(true,
            generateFromSchema(schema)
        )
    }

    @Test
    fun `boolean prefers its own example over the placeholder`() {
        val schema = targetSchema(
            """
                Target:
                  type: boolean
                  example: false
            """
        )
        assertEquals(false,
            generateFromSchema(schema)
        )
    }

    @Test
    fun `string without an example defaults to the literal placeholder`() {
        val schema = targetSchema(
            """
                Target:
                  type: string
            """
        )
        assertEquals("string",
            generateFromSchema(schema)
        )
    }

    @Test
    fun `integer without an example prefers maximum over minimum`() {
        val schema = targetSchema(
            """
                Target:
                  type: integer
                  minimum: 1
                  maximum: 99
            """
        )
        assertEquals(BigDecimal.valueOf(99),
            generateFromSchema(schema)
        )
    }

    @Test
    fun `integer with neither example nor maximum falls back to minimum`() {
        val schema = targetSchema(
            """
                Target:
                  type: integer
                  minimum: 5
            """
        )
        assertEquals(BigDecimal.valueOf(5),
            generateFromSchema(schema)
        )
    }

    @Test
    fun `integer with no example minimum or maximum defaults to zero`() {
        val schema = targetSchema(
            """
                Target:
                  type: integer
            """
        )
        assertEquals(0,
            generateFromSchema(schema)
        )
    }

    @Test
    fun `number without an example defaults to zero`() {
        val schema = targetSchema(
            """
                Target:
                  type: number
            """
        )
        assertEquals(0.0,
            generateFromSchema(schema)
        )
    }

    @Test
    fun `enum uses its first declared value over the generic placeholder`() {
        val schema = targetSchema(
            """
                Target:
                  type: string
                  enum: [ACTIVE, INACTIVE]
            """
        )
        assertEquals("ACTIVE",
            generateFromSchema(schema)
        )
    }

    @Test
    fun `enum with its own example still wins over the first declared value`() {
        val schema = targetSchema(
            """
                Target:
                  type: string
                  enum: [ACTIVE, INACTIVE]
                  example: INACTIVE
            """
        )
        assertEquals("INACTIVE",
            generateFromSchema(schema)
        )
    }

    @Test
    fun `enum precedence applies regardless of the underlying base type`() {
        val schema = targetSchema(
            """
                Target:
                  type: integer
                  enum: [1, 2, 3]
            """
        )
        assertEquals(1,
            generateFromSchema(schema)
        )
    }

    @Test
    fun `array produces a single-item list from its items schema`() {
        val schema = targetSchema(
            """
                Target:
                  type: array
                  items:
                    type: integer
            """
        )
        assertEquals(listOf(0),
            generateFromSchema(schema)
        )
    }

    @Test
    fun `map with a value schema produces one representative entry`() {
        val schema = targetSchema(
            """
                Target:
                  type: object
                  additionalProperties:
                    type: string
            """
        )
        assertEquals(mapOf("key" to "string"),
            generateFromSchema(schema)
        )
    }

    @Test
    fun `object recurses over each declared property`() {
        val schema = targetSchema(
            """
                Target:
                  type: object
                  properties:
                    name:
                      type: string
                    age:
                      type: integer
            """
        )
        assertEquals(mapOf("name" to "string", "age" to 0),
            generateFromSchema(schema)
        )
    }

    @Test
    fun `object with no declared properties produces an empty map rather than crashing`() {
        val schema = targetSchema(
            """
                Target:
                  type: object
            """
        )
        assertEquals(emptyMap<String, Any?>(),
            generateFromSchema(schema)
        )
    }

    @Test
    fun `allOf merges every member and this schema's own properties win on collision`() {
        val schema = targetSchema(
            """
                Base:
                  type: object
                  properties:
                    id: {type: integer}
                    name: {type: string}
                Target:
                  allOf:
                    - ${'$'}ref: '#/components/schemas/Base'
                    - type: object
                      properties:
                        name: {type: string, example: overridden}
                        extra: {type: boolean}
            """
        )
        assertEquals(mapOf("id" to 0, "name" to "overridden", "extra" to true),
            generateFromSchema(schema)
        )
    }

    @Test
    fun `a pure oneOf with no allOf or properties returns the first branch directly, unwrapped`() {
        val schema = targetSchema(
            """
                Target:
                  oneOf:
                    - type: string
                    - type: integer
            """
        )
        // Not modeling the union — always pick one representative shape, returned
        // as-is rather than wrapped in a map, since the branches need not be objects.
        assertEquals("string",
            generateFromSchema(schema)
        )
    }

    @Test
    fun `a $ref property nested inside an object resolves to the real referenced schema`() {
        val schema = targetSchema(
            """
                Address:
                  type: object
                  properties:
                    city: {type: string, example: London}
                Target:
                  type: object
                  properties:
                    address:
                      ${'$'}ref: '#/components/schemas/Address'
            """
        )
        assertEquals(mapOf("address" to mapOf("city" to "London")),
            generateFromSchema(schema)
        )
    }

    // --- exampleResponses: the response -> ExampleResponse(s) flattening ---

    private fun responseFor(yaml: String, statusKey: String = "200") =
        exampleResponses(
            statusKey,
            parseSpec(yaml).paths["/widgets"]!!.get.responses[statusKey]!!
        )

    @Test
    fun `a response with no application-json content degrades to a single empty-body variant`() {
        val result = responseFor(
            """
            openapi: 3.0.3
            info: {title: Test, version: '1.0'}
            paths:
              /widgets:
                get:
                  responses:
                    '200':
                      description: ok response
            """
        )

        assertEquals(1, result.size)
        assertEquals(200, result[0].statusCode)
        assertEquals("Ok response", result[0].name)
        assertEquals("", result[0].bodyValue)
    }

    @Test
    fun `named examples on one response each become their own variant, none discarded`() {
        val result = responseFor(
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
                          schema: {type: string}
                          examples:
                            empty: {value: []}
                            oneItem: {value: ["a"]}
            """
        )

        assertEquals(2, result.size)
        assertEquals(setOf("Empty", "OneItem"), result.map { it.name }.toSet())
        assertEquals(setOf("[]", "[\"a\"]"), result.map { it.bodyValue }.toSet())
    }

    @Test
    fun `the default single-example branch synthesizes from the schema when no example is set`() {
        val result = responseFor(
            """
            openapi: 3.0.3
            info: {title: Test, version: '1.0'}
            paths:
              /widgets:
                get:
                  responses:
                    '200':
                      description: A widget
                      content:
                        application/json:
                          schema:
                            type: object
                            properties:
                              id: {type: integer}
            """
        )

        assertEquals(1, result.size)
        assertEquals("A widget", result[0].name)
        assertEquals("{\"id\":0}", result[0].bodyValue)
    }

    @Test
    fun `status key 'default' maps to 200`() {
        val result = responseFor(
            """
            openapi: 3.0.3
            info: {title: Test, version: '1.0'}
            paths:
              /widgets:
                get:
                  responses:
                    default:
                      description: fallback
            """,
            statusKey = "default",
        )
        assertEquals(200, result[0].statusCode)
    }

    @Test
    fun `a wildcard range key like 4XX maps to its base status code`() {
        val result = responseFor(
            """
            openapi: 3.0.3
            info: {title: Test, version: '1.0'}
            paths:
              /widgets:
                get:
                  responses:
                    4XX:
                      description: client error
            """,
            statusKey = "4XX",
        )
        assertEquals(400, result[0].statusCode)
    }
}
