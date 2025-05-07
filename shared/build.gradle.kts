import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.gradleBuildConfig)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    jvm()

    sourceSets {
        commonMain.dependencies {
//            // Gitlive (Firebase)
//            implementation(libs.firebase.gitlive.auth)
//            implementation(libs.firebase.gitlive.common)
//            implementation(libs.firebase.gitlive.auth)

            // Room
            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)

            implementation(libs.kotlinx.serialization.json)

            implementation(libs.androidx.coroutines.core)
            implementation(libs.kmauth.google)
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")

            // Koin
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)


        }
    }
}

android {
    namespace = "com.apptolast.lifetimejournal.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

dependencies {
    ksp(libs.koin.ksp.compiler)
    ksp(libs.room.compiler)
}

buildConfig {
    packageName("com.apptolast.lifetimejournal")

    useKotlinOutput() // forces the outputType to 'kotlin', generating an `object`
//    useKotlinOutput {
//        topLevelConstants = true
//    }    // forces the outputType to 'kotlin', generating top-level declarations
    useKotlinOutput { internalVisibility = false } // makes `BuildConfig` class `public` (defaults to `internal`)

    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").reader())
    val kotzillaApiKey = properties.getProperty("KOTZILLA_API_KEY")
    val webClientId = properties.getProperty("WEB_ID_CLIENT")

    buildConfigField("KOTZILLA_API_KEY", kotzillaApiKey)
    buildConfigField("WEB_ID_CLIENT", webClientId)
}

room {
    schemaDirectory("$projectDir/schemas")
}
