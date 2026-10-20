import net.ltgt.gradle.errorprone.errorprone
import org.gradle.external.javadoc.CoreJavadocOptions

plugins {
    id("java-library")
    id("maven-publish")
    id("signing")
    id("net.ltgt.errorprone") version "5.1.1"
    jacoco
}

group = "io.github.anatolii-sennikov"
version = providers.gradleProperty("version").get()

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
    withSourcesJar()
    withJavadocJar()
}

dependencies {
    implementation("tools.jackson.core:jackson-databind:3.2.3")

    errorprone("com.google.errorprone:error_prone_core:2.50.0")
    errorprone("com.uber.nullaway:nullaway:0.14.2")

    api("org.jspecify:jspecify:1.0.1")
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.27.7")
    testImplementation("org.wiremock:wiremock:3.13.2")
    testImplementation("org.mockito:mockito-core:5.23.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.23.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.errorprone {
        error("NullAway")
        option("NullAway:AnnotatedPackages", "io.github.anatoliisennikov.rtkey")
    }
}

tasks.test {
    useJUnitPlatform()
    defaultCharacterEncoding = "UTF-8"
}

jacoco {
    toolVersion = "0.8.15"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.test)
    violationRules {
        rule {
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.90".toBigDecimal()
            }
            limit {
                counter = "BRANCH"
                value = "COVEREDRATIO"
                minimum = "0.85".toBigDecimal()
            }
        }
    }
}

tasks.check {
    dependsOn(tasks.jacocoTestReport, tasks.jacocoTestCoverageVerification)
}

tasks.javadoc {
    exclude("io/github/anatoliisennikov/rtkey/internal/**")
    options.encoding = "UTF-8"
    (options as CoreJavadocOptions).apply {
        addBooleanOption("Xdoclint:all,-missing", true)
        addBooleanOption("quiet", true)
    }
}

tasks.jar {
    manifest {
        attributes("Automatic-Module-Name" to "io.github.anatoliisennikov.rtkey")
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            pom {
                name.set("rt-key-client")
                description.set("Java client library for the RT Key smart intercom and access control platform.")
                url.set("https://github.com/anatolii-sennikov/rt-key-client")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                developers {
                    developer {
                        id.set("anatolii-sennikov")
                        name.set("Anatolii Sennikov")
                        email.set("anatolii.n.sennikov@gmail.com")
                        url.set("https://github.com/anatolii-sennikov")
                    }
                }

                scm {
                    url.set("https://github.com/anatolii-sennikov/rt-key-client")
                    connection.set("scm:git:https://github.com/anatolii-sennikov/rt-key-client.git")
                    developerConnection.set("scm:git:ssh://git@github.com/anatolii-sennikov/rt-key-client.git")
                }
            }
        }
    }
}

val signingKey = providers.gradleProperty("signingKey").filter { it.isNotBlank() }
val signingPassword = providers.gradleProperty("signingPassword")

signing {
    if (signingKey.isPresent) {
        require(signingPassword.isPresent && signingPassword.get().isNotEmpty()) {
            "signingPassword is required for the protected PGP key"
        }
        useInMemoryPgpKeys(signingKey.get().replace("\\n", "\n"), signingPassword.get())
        sign(publishing.publications["mavenJava"])
    }
}

tasks.matching { it.name.endsWith("ToNmcpRepository") }.configureEach {
    doFirst {
        check(signingKey.isPresent) {
            "signingKey is required to publish to Maven Central"
        }
    }
}
