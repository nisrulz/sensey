plugins {
    alias(libs.plugins.android.application) apply false

    alias(libs.plugins.android.library) apply false


    alias(libs.plugins.maven.publish) apply false

    alias(libs.plugins.ktlint) apply false
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}

//region Git Hooks
tasks.register("installGitHooks") {
    doLast {
        ProcessBuilder("git", "config", "core.hooksPath", ".githooks")
            .inheritIO()
            .start()
            .waitFor()
        logger.lifecycle("Git hooks installed (.githooks/pre-commit runs ktlintFormat)")
    }
}
//endregion

//region Publishing Tasks
tasks.register("releaseToMavenLocal") {
    val moduleName = "sensey"
    doLast {
        project.extensions.getByType(ExecOperations::class.java).exec {
            commandLine =
                listOf(
                    "./gradlew",
                    ":$moduleName:assembleRelease",
                    ":$moduleName:publishToMavenLocal",
                    "--no-configuration-cache",
                )
        }
    }
}

tasks.register("releaseToMavenCentral") {
    val moduleName = "sensey"
    doLast {
        project.extensions.getByType(ExecOperations::class.java).exec {
            commandLine =
                listOf(
                    "./gradlew",
                    ":$moduleName:assembleRelease",
                    ":$moduleName:publishToMavenCentral",
                    "--no-configuration-cache",
                )
        }
    }
}
//endregion

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
