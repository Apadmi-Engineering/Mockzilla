package com.apadmi.mockzilla

import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPom

fun Project.injectedVersion() = if (project.hasProperty("version")) properties["version"].toString()
    .takeUnless { it.isBlank() || it == "unspecified" } else null

fun Project.isSnapshot () = project.hasProperty("is_snapshot") && properties["is_snapshot"].toString().toBoolean()
fun Project.runNumber() = if (project.hasProperty("run_number")) properties["run_number"].toString()
    .takeUnless { it.isBlank() }?.toInt() else null

fun Project.githubToken() = if (hasProperty("github_token")) {
    property("github_token").toString()
} else {
    null
}

fun isSigningEnabled() = System.getenv("ORG_GRADLE_PROJECT_signingInMemoryKeyId") != null
fun isDevelopmentBuild() = !isSigningEnabled()

fun MavenPom.configureCommonProperties() {
    url.set("https://github.com/Apadmi-Engineering/Mockzilla")
    licenses {
        license {
            name.set("MIT")
            distribution.set("repo")
            url.set("https://opensource.org/licenses/MIT")
        }
    }

    developers {
        developer {
            id.set("samdc")
            name.set("Sam DC")
            email.set("samdc@apadmi.com")
        }
        developer {
            id.set("mattm")
            name.set("Matt M")
            email.set("mattm@apadmi.com")
        }
    }

    scm {
        connection.set("scm:git:ssh://github.com/Apadmi-Engineering/Mockzilla.git")
        developerConnection.set("scm:git:ssh://github.com/Apadmi-Engineering/Mockzilla.git")
        url.set("https://github.com/Apadmi-Engineering/Mockzilla")
    }
}
