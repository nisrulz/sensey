

plugins {
    alias(libs.plugins.android.application)

    // Compose
    alias(libs.plugins.compose.compiler)
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

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)

    debugImplementation(libs.bundles.compose.debug)
}
