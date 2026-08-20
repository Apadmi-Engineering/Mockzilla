import com.apadmi.mockzilla.codegen.generateMockzillaConfig
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

private const val SWAGGER_SPEC = """
openapi: 3.0.3
info: {title: Contract Test, version: '1.0'}
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
                oneItem: {value: ["a widget"]}
        '404':
          description: "Can't find that widget"
          content:
            application/json:
              schema: {type: object, properties: {message: {type: string, example: "not found"}}}
        '500':
          description: |-
            Server exploded.
            Try again later.
          content:
            application/json:
              schema: {type: object}
              example: {"error": "it's ${'$'}broken", "retryable": true}
    post:
      responses:
        '201':
          description: created
          content:
            application/json:
              schema:
                type: object
                properties:
                  id: {type: integer, example: 42}
                  name: {type: string, example: "New Widget"}
                  inStock: {type: boolean}
                  tags: {type: array, items: {type: string}}
        '400':
          description: bad request
  /widgets/{id}:
    put:
      responses:
        '200':
          description: updated
        '404':
          description: not found
        '409':
          description: conflict
    delete:
      responses:
        '204':
          description: no content
        '404':
          description: not found
  /users/{id}:
    patch:
      responses:
        '200':
          description: updated
        '422':
          description: unprocessable
        '500':
          description: server error
  /users:
    head:
      responses:
        '200':
          description: ok
    options:
      responses:
        '200':
          description: ok
"""

class ConfigGeneratorTest {
    @Test
    fun `generateMockzillaConfig writes a file whose contents pass dart analyze against the real mockzilla package`() {
        val tempOutputPath = "../FlutterMockzilla/mockzilla/temp_file.dart"
        val outputFile = File(tempOutputPath)

        try {
            val specFile = File("spec.yaml").apply { writeText(SWAGGER_SPEC.trimIndent()) }

            generateMockzillaConfig(specFile.path, tempOutputPath)


            assertTrue(outputFile.exists(), "Expected $outputFile to have been written.")
            val source = outputFile.readText()
            assertTrue(source.contains("import 'package:mockzilla/mockzilla.dart';"))
            assertTrue(source.contains("final mockzillaConfig = MockzillaConfig("))

            assertEquals(7, Regex("EndpointConfig\\(").findAll(source).count())
            assertEquals(16, Regex("DashboardOverridePreset\\(").findAll(source).count())
            listOf("get", "head", "post", "put", "delete", "options", "patch").forEach { method ->
                assertTrue(
                    source.contains("HttpMethod.$method"),
                    "Expected generated source to reference HttpMethod.$method",
                )
            }

            // dart format (inside generateMockzillaConfig) already proves this is valid Dart.
            // dart analyze additionally resolves it against the real MockzillaConfig.
            val process = ProcessBuilder("dart", "analyze", outputFile.path)
                .inheritIO()
                .start()
            assertEquals(0, process.waitFor(), "dart analyze reported issues with the generated config.")
            specFile.delete()
        } finally {
            outputFile.delete()
        }

        assertFalse(outputFile.exists(), "Generated contract-check file should have been deleted.")
    }
}
