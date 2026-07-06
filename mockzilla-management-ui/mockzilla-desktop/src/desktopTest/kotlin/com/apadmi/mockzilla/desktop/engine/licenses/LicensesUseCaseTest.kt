package com.apadmi.mockzilla.desktop.engine.licenses

import com.apadmi.mockzilla.testutils.CoroutineTest

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Suppress("MAGIC_NUMBER")
class LicensesUseCaseTest : CoroutineTest() {
    private fun sut(json: String) = LicensesUseCaseImpl { json }

    @Test
    fun `getLicenses - valid JSON - returns sorted library list`() = runBlockingTest {
        /* Setup */
        val json = """
            {
              "libraries": [
                {
                  "uniqueId": "org.foo:bar",
                  "name": "Zebra Library",
                  "artifactVersion": "2.0.0",
                  "licenses": ["MIT"]
                },
                {
                  "uniqueId": "org.foo:baz",
                  "name": "Alpha Library",
                  "artifactVersion": "1.0.0",
                  "licenses": ["Apache2"]
                }
              ],
              "licenses": {
                "MIT": { "name": "MIT License", "url": "https://opensource.org/licenses/MIT", "spdxId": "MIT" },
                "Apache2": { "name": "Apache Software License, Version 2.0", "url": "https://www.apache.org/licenses/LICENSE-2.0", "spdxId": "Apache-2.0" }
              }
            }
        """.trimIndent()

        /* Run */
        val result = sut(json).getLicenses()

        /* Verify */
        assertTrue(result.isSuccess)
        val libraries = result.getOrThrow()
        assertEquals(2, libraries.size)
        assertEquals("Alpha Library", libraries[0].name)
        assertEquals("Zebra Library", libraries[1].name)
    }

    @Test
    fun `getLicenses - library with multiple licenses - returns all license details`() = runBlockingTest {
        /* Setup */
        val json = """
            {
              "libraries": [
                {
                  "uniqueId": "org.foo:multi",
                  "name": "Multi-Licensed Lib",
                  "artifactVersion": "1.0.0",
                  "licenses": ["MIT", "Apache2"]
                }
              ],
              "licenses": {
                "MIT": { "name": "MIT License", "url": "https://opensource.org/licenses/MIT", "spdxId": "MIT" },
                "Apache2": { "name": "Apache Software License, Version 2.0", "url": "https://www.apache.org/licenses/LICENSE-2.0", "spdxId": "Apache-2.0" }
              }
            }
        """.trimIndent()

        /* Run */
        val result = sut(json).getLicenses()

        /* Verify */
        val library = result.getOrThrow().single()
        assertEquals(2, library.licenses.size)
        assertEquals("MIT License", library.licenses[0].name)
        assertEquals("MIT", library.licenses[0].spdxId)
        assertEquals("https://opensource.org/licenses/MIT", library.licenses[0].url)
        assertEquals("Apache Software License, Version 2.0", library.licenses[1].name)
    }

    @Test
    fun `getLicenses - library with unknown license ID - skips missing license`() = runBlockingTest {
        /* Setup */
        val json = """
            {
              "libraries": [
                {
                  "uniqueId": "org.foo:bar",
                  "name": "Some Library",
                  "licenses": ["UNKNOWN_ID"]
                }
              ],
              "licenses": {}
            }
        """.trimIndent()

        /* Run */
        val result = sut(json).getLicenses()

        /* Verify */
        val library = result.getOrThrow().single()
        assertEquals("Some Library", library.name)
        assertTrue(library.licenses.isEmpty())
    }

    @Test
    fun `getLicenses - library with no version - returns null version`() = runBlockingTest {
        /* Setup */
        val json = """
            {
              "libraries": [
                { "uniqueId": "org.foo:bar", "name": "No-Version Lib" }
              ],
              "licenses": {}
            }
        """.trimIndent()

        /* Run */
        val result = sut(json).getLicenses()

        /* Verify */
        val library = result.getOrThrow().single()
        assertEquals(null, library.version)
    }

    @Test
    fun `getLicenses - malformed JSON - returns failure`() = runBlockingTest {
        /* Run */
        val result = sut("not valid json").getLicenses()

        /* Verify */
        assertTrue(result.isFailure)
    }

    @Test
    fun `getLicenses - libraries sorted case-insensitively`() = runBlockingTest {
        /* Setup */
        val json = """
            {
              "libraries": [
                { "uniqueId": "a:c", "name": "zebra" },
                { "uniqueId": "a:b", "name": "Apple" },
                { "uniqueId": "a:a", "name": "mango" }
              ],
              "licenses": {}
            }
        """.trimIndent()

        /* Run */
        val names = sut(json).getLicenses().getOrThrow().map { it.name }

        /* Verify */
        assertEquals(listOf("Apple", "mango", "zebra"), names)
    }
}
