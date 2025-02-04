plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.example.amoz"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.amoz"
        minSdk = 33
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.example.amoz.CustomTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.hilt.android)
    implementation(libs.hilt.testing)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.runner)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation (libs.androidx.navigation.compose)
    implementation (libs.androidx.material.icons.extended)
    implementation (libs.coil.compose)
    implementation(libs.material3)
    implementation(libs.androidx.ui.text.google.fonts)


    implementation(libs.androidx.lifecycle.viewmodel.compose)

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore.preferences.rxjava2)
    implementation(libs.androidx.datastore.preferences.rxjava3)

    implementation(libs.androidx.datastore)
    implementation(libs.androidx.datastore.rxjava2)
    implementation(libs.androidx.datastore.rxjava3)

    implementation(libs.kotlinx.serialization.json.jvm)

    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    runtimeOnly("com.google.firebase:firebase-messaging-ktx:24.0.3")
    implementation("com.google.firebase:firebase-messaging:24.0.3")

    implementation(libs.kotlin.reflect)

    implementation(libs.retrofit)
    implementation(libs.converter.kotlinx.serialization)
    implementation(libs.play.services.auth)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.database)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.androidx.navigation.testing)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    testImplementation (libs.mockito.core)
    testImplementation (libs.mockito.kotlin)
    testImplementation (libs.robolectric)
    testImplementation (libs.junit)


    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
//    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.hilt.testing)
    androidTestImplementation(libs.mockito.android)

    kaptAndroidTest(libs.hilt.kotlin.testing)
    androidTestAnnotationProcessor(libs.hilt.kotlin.testing)

    debugImplementation(libs.androidx.monitor)
}
