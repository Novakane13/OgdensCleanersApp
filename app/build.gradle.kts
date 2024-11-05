// app/build.gradle.kts

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.10"
    id("com.google.gms.google-services") // Make sure this is applied here
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.ogdenscleaners.ogdenscleanersapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ogdenscleaners.ogdenscleanersapp"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    packaging {
        resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
    }
}

dependencies {
    // Core Libraries
    implementation("org.jetbrains.kotlin:kotlin-bom")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("androidx.core:core-ktx:1.12.0")

    // AndroidX Libraries
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.recyclerview:recyclerview:1.3.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.6.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.6.0")

    // Material Components
    implementation("com.google.android.material:material:1.9.0")

    // Jetpack Compose BOM
    implementation(platform("androidx.compose:compose-bom:2024.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // Retrofit and Gson
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.10")

    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.9.3")

    // Google Play Services
    implementation("com.snap.adkit:adkit:2.3.4")
    implementation("com.google.android.gms:play-services-maps:18.0.2")

    // Stripe SDK
    implementation("com.stripe:stripe-android:20.11.0")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:30.3.1"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-database-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    // Hilt for Dependency Injection
    implementation("com.google.dagger:hilt-android:2.47")
    implementation(libs.volley)
    kapt("com.google.dagger:hilt-compiler:2.47")

    // Core Desugaring
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

    // Testing Dependencies
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.10.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation("junit:junit:1.15.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Debugging Tools
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")


    implementation("androidx.databinding:databinding-runtime:3.5.0") // For Data Binding



}

kapt {
    correctErrorTypes = true
}
