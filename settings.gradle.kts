// settings.gradle.kts

pluginManagement {
    repositories {
        gradlePluginPortal() // General plugins repository.
        google() // Google repository for Android and Google Services plugins.
        mavenCentral() // General-purpose repository.
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
    }
}

// Set the root project name
rootProject.name = "OgdensCleanersApp"

// Include the app module
include(":app")
