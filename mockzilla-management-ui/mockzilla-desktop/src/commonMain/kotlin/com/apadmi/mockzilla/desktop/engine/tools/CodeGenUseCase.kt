package com.apadmi.mockzilla.desktop.engine.tools

import com.apadmi.mockzilla.codegen.generateMockzillaConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal interface CodeGenUseCase {
    suspend fun generateConfig(inputPath: String, outputPath: String): Result<Unit>
}

internal class CodeGenUseCaseImpl : CodeGenUseCase {
    override suspend fun generateConfig(inputPath: String, outputPath: String) =
        withContext(Dispatchers.IO) {
            runCatching { generateMockzillaConfig(inputPath, outputPath) }
        }
}