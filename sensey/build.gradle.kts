

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.compiler)

    alias(libs.plugins.maven.publish)
}

android {
    namespace = "com.github.nisrulz." + LibraryInfo.POM_ARTIFACT_ID

    compileSdk = BuildSdkInfo.COMPILE_SDK_VERSION

    defaultConfig {
        minSdk = BuildSdkInfo.MIN_SDK_VERSION

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        compose = true
    }

    buildTypes {
        release {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(BuildSdkInfo.JVM_TARGET)
        targetCompatibility = JavaVersion.toVersion(BuildSdkInfo.JVM_TARGET)
    }

    sourceSets {
        getByName("test") {
            java.srcDirs("src/test/java", "src/test/kotlin")
        }
    }

    testOptions.unitTests.isIncludeAndroidResources = true

    lint {
        abortOnError = true
    }
}

val libraryVersion by extra(LibraryInfo.POM_VERSION)

dependencies {
    // ── Lifecycle (compileOnly: consumer provides it) ──────────────────────────
    compileOnly(libs.androidx.lifecycle.common)

    // ── Coroutines (compileOnly: consumer provides if using Flow/SenseyFlow) ──
    compileOnly(libs.kotlinx.coroutines.core)

    // ── Compose (compileOnly: consumer provides if using touch-based plugins) ──
    compileOnly(platform(libs.androidx.compose.bom))
    compileOnly(libs.androidx.ui)
    compileOnly(libs.androidx.compose.foundation)

    // ── Test ───────────────────────────────────────────────────────────────────
    testImplementation(libs.bundles.testing)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.lifecycle.common)
    testImplementation(libs.androidx.lifecycle.runtime)
    testImplementation(platform(libs.androidx.compose.bom))
    testImplementation(libs.androidx.ui)
    testImplementation(libs.androidx.compose.foundation)
}

//region Maven Publishing
mavenPublishing {
    coordinates(artifactId = LibraryInfo.POM_ARTIFACT_ID, version = LibraryInfo.POM_VERSION)

    pom {
        name.set(LibraryInfo.POM_NAME)
        description.set(LibraryInfo.POM_DESCRIPTION)
        inceptionYear.set(LibraryInfo.POM_INCEPTION_YEAR)
        url.set(LibraryInfo.POM_URL)
        scm {
            url.set(LibraryInfo.POM_SCM_URL)
            connection.set(LibraryInfo.POM_SCM_CONNECTION)
            developerConnection.set(LibraryInfo.POM_SCM_DEV_CONNECTION)
        }
    }
}

//endregion
