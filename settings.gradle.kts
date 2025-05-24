rootProject.name = "LifetimeJournal"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        // Kotzilla
        maven {
            name = "kotzilla"
            url = uri("https://repository.kotzilla.io/repository/kotzilla-platform/")
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        // Kotzilla
        maven {
            name = "kotzilla"
            url = uri("https://repository.kotzilla.io/repository/kotzilla-platform/")
        }
        mavenCentral()
    }
}

//include(":composeApp")
include(":server")
//include(":shared")
