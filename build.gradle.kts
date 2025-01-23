val koin_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "2.0.21"
    id("io.ktor.plugin") version "3.0.3"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21"
}
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(19))
    }
}

group = "com.betclic"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.cio.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")
    implementation("io.ktor:ktor-server-request-validation")
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-client-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("io.ktor:ktor-server-cio")
    implementation("io.ktor:ktor-server-status-pages")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:5.3.0")
    implementation("org.mongodb:bson-kotlinx:5.3.0")
    implementation("io.ktor:ktor-server-config-yaml")
    implementation("io.ktor:ktor-server-cors")
    testImplementation("io.ktor:ktor-server-test-host")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("io.mockk:mockk:1.13.16")
}


tasks.register("databaseInstance") {
    doLast {
        val command = arrayOf("docker-compose", "up", "-d")
        Runtime.getRuntime().exec(command)
    }
}

tasks.register("testDatabaseInstance") {
    doLast {
        val command = arrayOf("docker-compose", "-f", "docker-compose.test.yaml", "up", "-d")
        Runtime.getRuntime().exec(command)
    }
}

tasks.test {
    environment(
        "MONGODB_URI" to "mongodb://test:test@localhost:27018",
    )
}

tasks.test {
    dependsOn("testDatabaseInstance")
}

tasks.named("run") {
    dependsOn("databaseInstance")
}

