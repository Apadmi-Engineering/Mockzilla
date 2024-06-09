
import java.util.Base64

plugins {
    id("org.gradle.maven-publish")
}

publishing {
    repositories.maven(mavenUrl()) {
        name = "OSSRH"

        credentials {
            username = System.getenv("OSSRH_USER")
            password = System.getenv("OSSRH_KEY")
        }
    }

    publications.withType<MavenPublication> {
        // Provide artifacts information required by Maven Central
        pom {
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
    }
}

val hasKey = System.getenv("GPG_KEY_ID") != null

if (hasKey) {
    apply(plugin = "signing")

    configure<SigningExtension> { 
        useGpgCmd()
        sign(publishing.publications)
    }

    val signingTasks = tasks.withType<Sign>()
    tasks.withType<AbstractPublishToMaven>().configureEach {
        dependsOn(signingTasks)
    }
}

fun mavenUrl() = if (version.toString().endsWith("-SNAPSHOT")) {
    "https://s01.oss.sonatype.org/content/repositories/snapshots/"
} else {
    "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
}