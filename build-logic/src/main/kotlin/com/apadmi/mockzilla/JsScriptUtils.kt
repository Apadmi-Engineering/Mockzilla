package com.apadmi.mockzilla

import org.gradle.api.Project
import java.io.File

private const val serviceWorkerFileName = "mockServiceWorker.js"
fun Project.prepareKarmaWithServiceWorkerAndGetConfigPath(): String {
    File(rootProject.rootDir, "js-scripts/$serviceWorkerFileName")
        .copyTo(
            File(projectDir, "src/jsTest/resources/$serviceWorkerFileName"),
            overwrite = true
        )
    return prepareKarmaFileAndGetPath()
}

private fun Project.prepareKarmaFileAndGetPath(): String {
    val dir = File(projectDir, "karma.config.d/").apply { mkdirs() }
    val file = File(dir, "karma.conf.js")
    val serviceWorkerPath =
        "${rootProject.rootDir.absolutePath.removeSuffix("/")}/build/js/packages/lib-${name}-test/kotlin/${serviceWorkerFileName}"
    file.writeText("""
        config.files.push({ pattern: '${serviceWorkerPath}', served: true, watched: false, included: false });
        config.proxies['/${serviceWorkerFileName}'] = "/base/kotlin/${serviceWorkerFileName}";
    """.trimIndent())
    return dir.absolutePath
}
