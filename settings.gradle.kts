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

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        // Try mavenLocal only if the dependency cannot
        // be found on Maven Central
        mavenLocal()
    }
}

// For accessing modules as type safe values
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "SenseyProject"
include(":sample")
include(":sensey")