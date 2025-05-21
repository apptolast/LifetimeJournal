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
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

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

            implementation(libs.kmauth.google)

            // Koin
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
        }
    }

    cocoapods {
        // Required properties
        // Specify the required Pod version here
        // Otherwise, the Gradle project version is used
        version = "1.0"
//        HtmlStyle.summary = "Some description for a Kotlin/Native module"
//        homepage = "Link to a Kotlin/Native module homepage"

        // Optional properties
        // Configure the Pod name here instead of changing the Gradle project name
        name = "MyCocoaPod"

        framework {
            // Required properties
            // Framework name configuration. Use this property instead of deprecated 'frameworkName'
            baseName = "MyFramework"

            // Optional properties
            // Specify the framework linking type. It's dynamic by default.
            isStatic = false
            // Dependency export
            // Uncomment and specify another project module if you have one:
            // export(project(":<your other KMP module>"))
            transitiveExport = false // This is default.
        }

        // Maps custom Xcode configuration to NativeBuildType
        xcodeConfigurationToNativeBuildType["CUSTOM_DEBUG"] = NativeBuildType.DEBUG
        xcodeConfigurationToNativeBuildType["CUSTOM_RELEASE"] = NativeBuildType.RELEASE
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
}
