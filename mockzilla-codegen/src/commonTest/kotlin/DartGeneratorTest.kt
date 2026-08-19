import com.apadmi.mockzilla.codegen.data.EndpointSpec
import com.apadmi.mockzilla.codegen.data.ExampleResponse
import com.apadmi.mockzilla.codegen.file_builders.dart.endpointFragment
import com.apadmi.mockzilla.codegen.file_builders.dart.escapeForDartLiteral
import com.apadmi.mockzilla.codegen.file_builders.dart.escapeJsonForDartLiteral
import com.apadmi.mockzilla.codegen.file_builders.dart.generateDartSource
import com.apadmi.mockzilla.codegen.file_builders.dart.presetFragment
import com.apadmi.mockzilla.codegen.file_builders.dart.responseExpression
import io.swagger.models.HttpMethod
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private fun endpoint(
    path: String = "/widgets",
    method: HttpMethod = HttpMethod.GET,
    success: ExampleResponse = ExampleResponse(200, "ok", "{\"id\":1}"),
    error: ExampleResponse = ExampleResponse(400, "error", ""),
    others: Array<ExampleResponse> = arrayOf(),
) = EndpointSpec(path, method, success, error, others)

class CodeGeneratorTest {
    @Test
    fun `escapeForDartLiteral escapes backslash quote and dollar`() {
        assertEquals("a\\\\b\\'c\\\$d", "a\\b'c\$d".escapeForDartLiteral())
    }

    @Test
    fun `escapeForDartLiteral collapses a literal newline into a space`() {
        assertEquals("line one line two", "line one\nline two".escapeForDartLiteral())
    }

    @Test
    fun `escapeForDartLiteral escapes carriage return`() {
        assertEquals("a\\rb", "a\rb".escapeForDartLiteral())
    }

    @Test
    fun `escapeForDartLiteral escapes backslash before other characters, not after`() {
        assertEquals("\\\\\\'", "\\'".escapeForDartLiteral())
    }
    
    @Test
    fun `escapeJsonForDartLiteral escapes quote and dollar`() {
        assertEquals("{\\'a\\':1,\\\$b:2}", "{'a':1,\$b:2}".escapeJsonForDartLiteral())
    }

    @Test
    fun `escapeJsonForDartLiteral escaped leaves backslashes and newlines alone`() {
        assertEquals("a\\nb\\\\c", "a\\nb\\\\c".escapeJsonForDartLiteral())
    }
    
    @Test
    fun `endpointFragment includes the escaped name, method, and matcher`() {
        val source = endpointFragment(endpoint(path = "/widgets", method = HttpMethod.POST))

        assertTrue(source.contains("name: 'POST widgets'"))
        assertTrue(source.contains("request.method == HttpMethod.post"))
        assertTrue(source.contains("request.uri.endsWith('/widgets')"))
    }

    @Test
    fun `endpointFragment escapes a single quote in the path-derived name`() {
        val source = endpointFragment(endpoint(path = "/don't"))
        assertTrue(source.contains("name: 'GET don\\'t'"))
    }

    @Test
    fun `endpointFragment embeds the default and error handlers`() {
        val source = endpointFragment(
            endpoint(
                success = ExampleResponse(200, "ok", "{\"a\":1}"),
                error = ExampleResponse(404, "missing", "testing testing"),
            )
        )

        assertTrue(source.contains("defaultHandler: (request) => MockzillaHttpResponse(statusCode: 200, body: '{\"a\":1}')"))
        assertTrue(source.contains("errorHandler: (request) => MockzillaHttpResponse(statusCode: 404, body: 'testing testing')"))
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
        assertTrue(source.contains("name: 'Not Found'"))
        assertTrue(source.contains("name: 'Server Error'"))
    }

    @Test
    fun `presetFragment includes the escaped name description and response`() {
        val source = presetFragment(ExampleResponse(404, "Not Found", "null"))

        assertTrue(source.contains("name: 'Not Found'"))
        assertTrue(source.contains("description: null"))
        assertTrue(source.contains("response: MockzillaHttpResponse(statusCode: 404, body: 'null')"))
    }

    @Test
    fun `responseExpression bakes the status code and escaped body into one line`() {
        val expression = responseExpression(ExampleResponse(200, "ok", "{\"a\":\"it's \$5\"}"))

        assertEquals(
            "MockzillaHttpResponse(statusCode: 200, body: '{\"a\":\"it\\'s \\\$5\"}')",
            expression,
        )
    }

    @Test
    fun `responseExpression preserves an already-empty body as an empty string literal`() {
        assertEquals(
            "MockzillaHttpResponse(statusCode: 204, body: '')",
            responseExpression(ExampleResponse(204, "no content", "")),
        )
    }

    @Test
    fun `generateSource produces one EndpointConfig per input endpoint`() {
        val source = generateDartSource(arrayOf(endpoint(path = "/a"), endpoint(path = "/b")))
        assertEquals(2, Regex("EndpointConfig\\(").findAll(source).count())
    }

    @Test
    fun `generateSource includes the generated-code header and import`() {
        val source = generateDartSource(arrayOf())

        assertTrue(source.contains("// GENERATED CODE"))
        assertTrue(source.contains("import 'package:mockzilla/mockzilla.dart';"))
        assertTrue(source.contains("final mockzillaConfig = MockzillaConfig("))
    }

    @Test
    fun `generateSource with no otherResponses produces an empty presets list`() {
        val source = generateDartSource(arrayOf(endpoint(others = arrayOf())))
        assertTrue(source.contains("presets: []"))
    }

    @Test
    fun `generateSource with an empty endpoint list still produces a valid-looking empty config`() {
        val source = generateDartSource(arrayOf())
        assertTrue(source.contains("endpoints: ["))
        assertTrue(source.contains("]);"))
    }
}
