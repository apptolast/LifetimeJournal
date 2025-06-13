plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinSerialization)
    application
}

group = "com.apptolast.lifetimejournal"
version = "0.1.0"
application {
    mainClass.set("com.apptolast.lifetimejournal.ApplicationKt")
    applicationDefaultJvmArgs =
        listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

ktor {
    fatJar {
        archiveFileName.set("lifetime-journal.jar")
    }
    docker {
        jreVersion.set(JavaVersion.VERSION_17)

        localImageName.set("lifetime-journal-docker-image")
        imageTag.set("0.0.1-preview")

    }
}

dependencies {
    //implementation(projects.shared)
    // Cliente Redis - Lettuce (recomendado)
    implementation(libs.lettuce.core)
    // Inyección de dependencias
    implementation(libs.koin.ktor)
    // Serialización
    implementation(libs.ktor.simple.cache)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.simple.redis.cache)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.h2)
    implementation(libs.ktor.server.host.common)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)
    implementation(libs.ktor.server.config.yaml)
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
}
