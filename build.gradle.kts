import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("org.jetbrains.intellij.platform") version "2.10.2"
    kotlin("jvm") version "2.2.20"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2025.2.4")
        testFramework(TestFrameworkType.Starter)
    }

    // JUnit 5
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")

    // The tests didn't run without this dependency. Didn't investigate further.
    testImplementation("junit:junit:4.13.2")

    // Dependency Injection for Starter configuration
    testImplementation("org.kodein.di:kodein-di-jvm:7.20.2")
}

intellijPlatform {
    buildSearchableOptions = false
}

tasks.test {
    useJUnitPlatform()

    reports {
        html.required.set(true)
    }

    doLast {
        val reportFilePath = "file://${layout.buildDirectory.get().asFile.absolutePath}/reports/tests/test/index.html"
        println("Test report: $reportFilePath")
    }

    // Set properties to avoid shutdown errors
    systemProperty("idea.home.path", layout.buildDirectory.dir("idea-sandbox").get().asFile.absolutePath)
    systemProperty("idea.log.path", layout.buildDirectory.dir("logs").get().asFile.absolutePath)
    systemProperty("java.awt.headless", "true")

    // For debugging
//    systemProperty("org.slf4j.simpleLogger.defaultLogLevel", "info")
//    systemProperty("org.slf4j.simpleLogger.log.com.intellij.driver.sdk", "debug")
//    testLogging {
//        events("passed", "skipped", "failed", "standardOut", "standardError")
//        showStandardStreams = true
//        showExceptions = true
//        showCauses = true
//        showStackTraces = true
//    }
}

kotlin {
    jvmToolchain(17)
}