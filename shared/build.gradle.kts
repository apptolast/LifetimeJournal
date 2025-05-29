@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.gradleBuildConfig)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.kotlinCocoapods)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    targets.configureEach {
        compilations.configureEach {
            compileTaskProvider.get().compilerOptions {
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }
        }
    }
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    jvm()

    sourceSets {
        androidMain.dependencies {
            implementation(libs.googleIdIdentity)
        }

        commonMain.dependencies {
            // Gitlive (Firebase)
            implementation(libs.firebase.gitlive.auth)
            implementation(libs.firebase.gitlive.firestore)
            implementation(libs.firebase.gitlive.common)

            // Room
            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)

            implementation(libs.kotlinx.serialization.json)

            implementation(libs.androidx.coroutines.core)
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")

            // Koin
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
        }
    }

    cocoapods {
        summary = "Some description for a Kotlin/Native module"
        homepage = "Link to a Kotlin/Native module homepage"
        version = "1.0"
        ios.deploymentTarget = "16.0"
        podfile = project.file("../iosApp/Podfile")

        // Optional properties
        // Configure the Pod name here instead of changing the Gradle project name
//        name = "shared"

        framework {
            // Required properties
            // Framework name configuration. Use this property instead of deprecated 'frameworkName'
            baseName = "shared"

            // Optional properties
            // Specify the framework linking type. It's dynamic by default.
            isStatic = true
            // Dependency export
            // Uncomment and specify another project module if you have one:
            // export(project(":<your other KMP module>"))
//            transitiveExport = false // This is default.
        }

        // Maps custom Xcode configuration to NativeBuildType
        xcodeConfigurationToNativeBuildType["CUSTOM_DEBUG"] = NativeBuildType.DEBUG
        xcodeConfigurationToNativeBuildType["CUSTOM_RELEASE"] = NativeBuildType.RELEASE

        pod("GoogleSignIn") {
            extraOpts += listOf("-compiler-option", "-fmodules")
        }
        pod("Firebase") {
            extraOpts += listOf("-compiler-option", "-fmodules")
            linkOnly = true
        }
        pod("FirebaseCore") {
            extraOpts += listOf("-compiler-option", "-fmodules")
            linkOnly = true
        }
        pod("FirebaseAuth") {
            extraOpts += listOf("-compiler-option", "-fmodules")
            linkOnly = true
        }
        pod("FirebaseFirestore") {
            extraOpts += listOf("-compiler-option", "-fmodules")
            linkOnly = true
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

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.common.ktx)
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
    generateKotlin = true
}

ksp {
    arg("KOIN_DEFAULT_MODULE", "true")
    arg("room.schemaLocation", "$projectDir/schemas")
}
