package com.apadmi.mockzilla.codegen

import com.apadmi.mockzilla.codegen.file_builders.dart.generateDartSource
import com.apadmi.mockzilla.codegen.file_builders.kotlin.generateKotlinSource
import io.swagger.parser.OpenAPIParser
import io.swagger.v3.parser.core.models.ParseOptions
import org.jetbrains.kotlinx.ktfmt.format.FileType
import org.jetbrains.kotlinx.ktfmt.format.Formatter
import org.jetbrains.kotlinx.ktfmt.format.KotlinCode
import java.io.File


fun generateMockzillaConfig(inputPath: String, outputPath: String) {
    val parseOptions = ParseOptions().apply { isResolve = true; isResolveFully = true }

    val spec = OpenAPIParser().readLocation(inputPath, null, parseOptions)

    spec.messages?.forEach { message -> println(message) }
    val openApi = spec.openAPI
        ?: error("Failed to parse \"$inputPath\" (${spec.messages.joinToString(", ")}).")

    val endpoints = mapSpecToEndpoints(openApi)
    val tags = openApi.tags ?: emptyList()
    val source = if (outputPath.endsWith(".dart")) {
        generateDartSource(tags.map { it.name }, endpoints)
    } else {
        generateKotlinSource(tags.map { it.name }, endpoints)
    }

    val outputFile = File(outputPath)
    outputFile.parentFile?.mkdirs()
    outputFile.writeText(formatSource(outputPath, source))

    if (outputPath.endsWith(".dart")) tryDartFormat(outputFile)
}

internal fun formatSource(outputPath: String, source: String) =
    if (outputPath.endsWith(".kt")) {
        try {
            Formatter.format(Formatter.KOTLINLANG_FORMAT, KotlinCode(source, FileType.REGULAR))
        } catch (e: Exception) {
            // TODO: this should write the source and then rethrow
            println("Failed to format Kotlin source code: ${e.message}")
            source
        }
    } else {
        source
    }

private fun tryDartFormat(file: File) {
    runCatching {
        ProcessBuilder("dart", "format", file.path).inheritIO().start().waitFor()
    }
}
