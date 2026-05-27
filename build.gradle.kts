plugins {
    alias(libs.plugins.android.application) apply false

    alias(libs.plugins.android.library) apply false


    alias(libs.plugins.maven.publish) apply false

    alias(libs.plugins.ktlint) apply false

    alias(libs.plugins.dokka) apply false
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}

val dokkaOutputDir = rootProject.layout.buildDirectory.dir("dokka")
val docsLibraryVersion = LibraryInfo.POM_VERSION

tasks.register("publishDocs") {
    dependsOn(":sensey:dokkaGeneratePublicationHtml")
    doLast {
        val sourceDir = project(":sensey").layout.buildDirectory.dir("dokka/html").get().asFile
        if (!sourceDir.exists()) return@doLast

        val outputDir = dokkaOutputDir.get().asFile.resolve(docsLibraryVersion)
        outputDir.mkdirs()
        sourceDir.copyRecursively(outputDir, overwrite = true)

        val latestDir = dokkaOutputDir.get().asFile.resolve("latest")
        if (latestDir.exists()) latestDir.deleteRecursively()
        outputDir.copyRecursively(latestDir, overwrite = true)

        val indexHtml = dokkaOutputDir.get().asFile.resolve("index.html")
        indexHtml.writeText(
            """
            <!DOCTYPE html>
            <html>
            <head><meta http-equiv="refresh" content="0; url=./$docsLibraryVersion/index.html" /></head>
            <body><p>Redirecting to <a href="./$docsLibraryVersion/index.html">$docsLibraryVersion docs</a></p></body>
            </html>
            """.trimIndent(),
        )
    }
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
