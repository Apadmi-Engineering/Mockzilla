import com.apadmi.mockzilla.codegen.data.EndpointSpec
import com.apadmi.mockzilla.codegen.data.ExampleResponse
import com.apadmi.mockzilla.codegen.file_builders.kotlin.endpointFragment
import com.apadmi.mockzilla.codegen.file_builders.kotlin.escapeForKotlinLiteral
import com.apadmi.mockzilla.codegen.file_builders.kotlin.escapeJsonForKotlinLiteral
import com.apadmi.mockzilla.codegen.file_builders.kotlin.generateKotlinSource
import com.apadmi.mockzilla.codegen.file_builders.kotlin.presetFragment
import com.apadmi.mockzilla.codegen.file_builders.kotlin.responseExpression
import io.swagger.models.HttpMethod
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

private fun endpoint(
    path: String = "/widgets",
    method: HttpMethod = HttpMethod.GET,
    success: ExampleResponse = ExampleResponse(200, "ok", "{\"id\":1}"),
    error: ExampleResponse = ExampleResponse(400, "error", ""),
    others: Array<ExampleResponse> = arrayOf(),
    tags: List<String> = emptyList(),
) = EndpointSpec(path, method, success, error, others, tags)

class KotlinCodeGeneratorTest {
    @Test
    fun `escapeForKotlinLiteral escapes backslash quote and dollar`() {
        assertEquals("a\\\\b\\\"c\\\$d", "a\\b\"c\$d".escapeForKotlinLiteral())
    }

    @Test
    fun `escapeForKotlinLiteral collapses a literal newline into a space`() {
        assertEquals("line one line two", "line one\nline two".escapeForKotlinLiteral())
    }

    @Test
    fun `escapeForKotlinLiteral escapes carriage return`() {
        assertEquals("a\\rb", "a\rb".escapeForKotlinLiteral())
    }

    @Test
    fun `escapeForKotlinLiteral escapes backslash before other characters, not after`() {
        assertEquals("\\\\\\\"", "\\\"".escapeForKotlinLiteral())
    }

    @Test
    fun `escapeJsonForKotlinLiteral escapes dollar for raw strings and leaves quotes`() {
        assertEquals("{\"a\":1,\${'\$'}b:2}", "{\"a\":1,\$b:2}".escapeJsonForKotlinLiteral())
    }

    @Test
    fun `escapeJsonForKotlinLiteral prevents closing the raw string`() {
        assertEquals("a\"\"\${'\"'}b", "a\"\"\"b".escapeJsonForKotlinLiteral())
    }

    @Test
    fun `escapeJsonForKotlinLiteral leaves backslashes and newlines alone`() {
        assertEquals("a\\nb\\\\c", "a\\nb\\\\c".escapeJsonForKotlinLiteral())
    }

    @Test
    fun `endpointFragment includes the escaped name, method, and matcher`() {
        val source = endpointFragment(endpoint(path = "/widgets", method = HttpMethod.POST))

        assertTrue(source.contains("name = \"POST widgets\""))
        assertTrue(source.contains("request.method == HttpMethod.post"))
        assertTrue(source.contains("request.uri.endsWith(\"/widgets\")"))
    }

    @Test
    fun `endpointFragment escapes a double quote in the path-derived name`() {
        val source = endpointFragment(endpoint(path = "/don\"t"))
        assertTrue(source.contains("name = \"GET don\\\"t\""))
    }

    @Test
    fun `endpointFragment embeds the default and error handlers`() {
        val source = endpointFragment(
            endpoint(
                success = ExampleResponse(200, "ok", "{\"a\":1}"),
                error = ExampleResponse(404, "missing", "testing testing"),
            )
        )

        assertTrue(
            source.contains(
                "defaultHandler = { request -> PartialMockzillaHttpResponse(statusCode = 200, body = \"{\\\"a\\\":1}\")"
            )
        )
        assertTrue(
            source.contains(
                "errorHandler = { request -> PartialMockzillaHttpResponse(statusCode = 404, body = \"testing testing\")"
            )
        )
    }

    @Test
    fun `endpointFragment joins multiple presets`() {
        val source = endpointFragment(
            endpoint(
                others = arrayOf(
                    ExampleResponse(404, "Not Found", "null"),
                    ExampleResponse(500, "Server Error", "null"),
                )
            )
        )

        assertEquals(2, Regex("DashboardOverridePreset\\(").findAll(source).count())
        assertTrue(source.contains("\"Not Found\""))
        assertTrue(source.contains("\"Server Error\""))
    }

    @Test
    fun `presetFragment includes the escaped name and response`() {
        val source = presetFragment(ExampleResponse(404, "Not Found", "null"))

        assertTrue(source.contains("\"Not Found\""))
        assertTrue(source.contains("null"))
        assertTrue(source.contains("response = PartialMockzillaHttpResponse(statusCode = 404, body = \"null\")"))
    }

    @Test
    fun `responseExpression bakes the status code and escaped body into one line`() {
        val expression = responseExpression(ExampleResponse(200, "ok", "{\"a\":\"it's \$5\"}"))

        assertEquals(
            "PartialMockzillaHttpResponse(statusCode = 200, body = \"{\\\"a\\\":\\\"it's \\\$5\\\"}\")",
            expression,
        )
    }

    @Test
    fun `responseExpression preserves an already-empty body as an empty string literal`() {
        assertEquals(
            "PartialMockzillaHttpResponse(statusCode = 204, body = \"\")",
            responseExpression(ExampleResponse(204, "no content", "")),
        )
    }

    @Test
    fun `generateSource produces one addEndpoint per input endpoint`() {
        val source = generateKotlinSource(emptyList(), arrayOf(endpoint(path = "/a"), endpoint(path = "/b")))
        assertEquals(2, Regex("\\.addEndpoint\\(").findAll(source).count())
    }

    @Test
    fun `generateSource includes the generated-code header, import and builder`() {
        val source = generateKotlinSource(emptyList(), arrayOf())

        assertTrue(source.contains("// GENERATED CODE"))
        assertTrue(source.contains("import com.apadmi.mockzilla"))
        assertTrue(source.contains("val config = MockzillaConfig.Builder()"))
        assertTrue(source.contains(".build()"))
    }

    @Test
    fun `generateSource with no otherResponses produces an empty presets list`() {
        val source = generateKotlinSource(emptyList(), arrayOf(endpoint(others = arrayOf())))
        assertTrue(source.contains("presets = listOf()"))
    }

    @Test
    fun `generateSource with no tags does not emit a tag comment but still emits every endpoint`() {
        val source = generateKotlinSource(emptyList(), arrayOf(endpoint(path = "/a"), endpoint(path = "/b")))

        assertFalse(source.contains("// Other Endpoints"))
        assertEquals(2, Regex("\\.addEndpoint\\(").findAll(source).count())
    }

    @Test
    fun `generateSource emits a comment header per tag`() {
        val source = generateKotlinSource(
            listOf("OTP", "Session"),
            arrayOf(
                endpoint(path = "/auth/otp/send", tags = listOf("OTP")),
                endpoint(path = "/auth/otp/verify", tags = listOf("OTP")),
                endpoint(path = "/auth/logout", tags = listOf("Session")),
            ),
        )

        assertTrue(source.contains("// OTP Endpoints"))
        assertTrue(source.contains("// Session Endpoints"))
        assertFalse(source.contains("// Other Endpoints"))
    }

    @Test
    fun `generateSource assigns each endpoint to only its own tag group`() {
        val source = generateKotlinSource(
            listOf("OTP", "Session"),
            arrayOf(
                endpoint(path = "/auth/otp/send", tags = listOf("OTP")),
                endpoint(path = "/auth/logout", tags = listOf("Session")),
            ),
        )

        val otpBlock = source.substringAfter("// OTP Endpoints").substringBefore("// Session Endpoints")
        val sessionBlock = source.substringAfter("// Session Endpoints")

        assertTrue(otpBlock.contains("/auth/otp/send"))
        assertFalse(otpBlock.contains("/auth/logout"))
        assertTrue(sessionBlock.contains("/auth/logout"))
        assertFalse(sessionBlock.contains("/auth/otp/send"))
    }

    @Test
    fun `generateSource groups untagged endpoints under an Other Endpoints comment when tags exist`() {
        val source = generateKotlinSource(
            listOf("OTP"),
            arrayOf(
                endpoint(path = "/auth/otp/send", tags = listOf("OTP")),
                endpoint(path = "/auth/logout", tags = emptyList()),
            ),
        )

        assertTrue(source.contains("// OTP Endpoints"))
        assertTrue(source.contains("// Other Endpoints"))
        val otherBlock = source.substringAfter("// Other Endpoints")
        assertTrue(otherBlock.contains("/auth/logout"))
        assertFalse(otherBlock.contains("/auth/otp/send"))
    }

    @Test
    fun `generateSource with an empty endpoint list still produces a valid-looking empty config`() {
        val source = generateKotlinSource(emptyList(), arrayOf())
        assertTrue(source.contains("val config = MockzillaConfig.Builder()"))
        assertTrue(source.contains(".build()"))
        assertFalse(source.contains(".addEndpoint("))
    }
}

