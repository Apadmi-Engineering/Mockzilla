package com.apadmi.mockzilla

import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register
import java.io.File

const val serviceWorkerFileName = "mockServiceWorker.js"
const val karmaDirName = "karma.config.d/"

fun Project.prepareKarmaFile() {
    val dir = File(projectDir, karmaDirName).apply { mkdirs() }
    val file = File(dir, "karma.conf.js")
    val serviceWorkerPath =
        "${rootProject.rootDir.absolutePath.removeSuffix("/")}/build/js/packages/lib-${name}-test/kotlin/${serviceWorkerFileName}"
    file.writeText("""
        config.files.push({ pattern: '${serviceWorkerPath}', served: true, watched: false, included: false });
        config.proxies['/${serviceWorkerFileName}'] = "/base/kotlin/${serviceWorkerFileName}";
    """.trimIndent())
}