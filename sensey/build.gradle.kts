/*
 * Copyright (C) 2016 Nishant Srivastava
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

    alias(libs.plugins.maven.publish)
}

android {
    namespace = "com.github.nisrulz." + LibraryInfo.POM_ARTIFACT_ID

    compileSdk = BuildSdkInfo.COMPILE_SDK_VERSION

    defaultConfig {
        minSdk = BuildSdkInfo.MIN_SDK_VERSION

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(BuildSdkInfo.JVM_TARGET))
        }
    }

    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    implementation(libs.core.ktx)

    // Testing
    testImplementation(libs.androidx.test.ext.junit)
    testImplementation(libs.junit)
    testImplementation(libs.androidx.core)
    testImplementation(libs.androidx.runner)
    testImplementation(libs.androidx.rules)
    testImplementation(libs.mockito.core)
    testImplementation(libs.robolectric)
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
