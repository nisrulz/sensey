plugins {
    alias(libs.plugins.android.application) apply false

    alias(libs.plugins.android.library) apply false


    alias(libs.plugins.maven.publish) apply false

    alias(libs.plugins.ktlint) apply false
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}

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
