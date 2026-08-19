package com.apadmi.mockzilla.codegen

import com.apadmi.mockzilla.codegen.file_builders.dart.generateDartSource
import io.swagger.parser.OpenAPIParser
import io.swagger.v3.parser.core.models.ParseOptions
import java.io.File

fun generateMockzillaConfig(inputPath: String, outputPath: String) {
    val parseOptions = ParseOptions().apply { isResolve = true; isResolveFully = true }

    val spec = OpenAPIParser().readLocation(inputPath, null, parseOptions)

    spec.messages?.forEach { message -> println(message) }
    val openApi = spec.openAPI ?: error("Failed to parse $inputPath.")

    val endpoints = mapSpecToEndpoints(openApi)
    val source = generateDartSource(endpoints)

    val outputFile = File(outputPath)
    outputFile.parentFile?.mkdirs()
    outputFile.writeText(source)

    val process = ProcessBuilder("dart", "format", outputFile.path).inheritIO().start()
    if (process.waitFor() != 0) error("Dart format failed.")
}