pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
    }
}

rootProject.name = "segmented-control"

include(":segmented-control")
include(":sample:composeApp")
include(":sample:androidApp")
include(":sample:desktopApp")
include(":sample:webApp")
// The iOS sample is a standalone Xcode project under sample/iosApp that consumes
// the SampleShared framework produced by :sample:composeApp — it is not a Gradle module.
