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
    implementation(libs.logback)
    // Cliente Redis - Lettuce (recomendado)
    implementation(libs.lettuce.core)
    // Inyección de dependencias
    implementation(libs.koin.ktor)
    // Serialización
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test.junit)
}
