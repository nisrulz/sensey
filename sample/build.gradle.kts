

plugins {
    alias(libs.plugins.android.application)

    // Compose
    alias(libs.plugins.compose.compiler)

    // Kotlin Serialization (required by Navigation 3)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = ApplicationInfo.BASE_NAMESPACE

    compileSdk = BuildSdkInfo.COMPILE_SDK_VERSION

    defaultConfig {
        minSdk = BuildSdkInfo.MIN_SDK_VERSION

        targetSdk = BuildSdkInfo.TARGET_SDK_VERSION

        versionCode = ApplicationInfo.VERSION_CODE
        versionName = ApplicationInfo.VERSION_NAME

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isDebuggable = true
        }
        release {
            isMinifyEnabled = true
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

    lint {
        abortOnError = true
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // Module Dependency
    implementation(projects.sensey)

    // Support
    implementation(libs.androidx.appcompat)

    // Lifecycle (compileOnly in sensey — must be provided by the consumer)
    implementation(libs.androidx.lifecycle.common)

    // Coroutines (compileOnly in sensey — must be provided by the consumer)
    implementation(libs.kotlinx.coroutines.core)

    // Compose (compileOnly in sensey — must be provided by the consumer)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)

    // Material Icons Extended
    implementation(libs.androidx.material.icons.extended)

    // Navigation 3
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)

    // Material3 Adaptive (for NavigationSuiteScaffold)
    implementation(libs.androidx.adaptive)

    // Kotlin Serialization (for Nav3 routes)
    implementation(libs.kotlinx.serialization.json)

    // Lifecycle ViewModel Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    debugImplementation(libs.bundles.compose.debug)
}
