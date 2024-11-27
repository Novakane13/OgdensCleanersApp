plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0" // Compose Plugin for Kotlin 2.0
    id("com.google.gms.google-services")
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
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildFeatures {
        viewBinding = true
        compose = true
        dataBinding = true
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

    composeOptions {
        kotlinCompilerExtensionVersion = "2.0.0" // Upgraded to Compose Compiler 2.0.0
    }

    packaging {
        resources {
            excludes += setOf("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

val kotlinVersion = "2.0.0"
val hiltVersion = "2.52"
val serializationVersion = "1.7.3"
val composeVersion = "2024.11.00" // Update this to your Compose BOM version
val coroutineVersion = "1.9.0"
val lifecycleVersion = "2.8.7"
val navigationVersion = "2.8.4"

dependencies {
    // Kotlin Libraries
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")

    implementation("com.android.volley:volley:1.2.1")


    // Jetpack Compose (using BOM)
    implementation(platform("androidx.compose:compose-bom:$composeVersion"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // AndroidX Libraries
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")

    implementation("androidx.navigation:navigation-fragment-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navigationVersion")

    // Material Components
    implementation("com.google.android.material:material:1.12.0")

    // Retrofit and Gson
    implementation("com.squareup.retrofit2:retrofit:2.11.0") {
        exclude(group = "com.google.protobuf", module = "protobuf-java")
    }
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // Google Play Services
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation("com.google.maps.android:android-maps-utils:3.9.0")

    // Stripe SDK
    implementation("com.stripe:stripe-android:21.0.0")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation("com.google.firebase:firebase-auth-ktx") {
        exclude(group = "com.google.protobuf", module = "protobuf-java")
    }
    implementation("com.google.firebase:firebase-database-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")

    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineVersion")

    // Kotlin Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")

    // Hilt for Dependency Injection
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    implementation(libs.firebase.dataconnect)
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")

    // Include Tink library
    implementation("com.google.crypto.tink:tink-android:1.9.0")

    // Include gRPC dependencies
    implementation("io.grpc:grpc-protobuf-lite:1.62.2")
    implementation("io.grpc:grpc-stub:1.62.2")

    // Explicitly include protobuf-javalite
    implementation("com.google.protobuf:protobuf-javalite:3.25.5")

    // Core Library Desugaring
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.3")

    // Testing Dependencies
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    // Debugging Tools
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Exclusions
    implementation("com.google.dagger:dagger:$hiltVersion") {
        exclude(group = "com.google.protobuf", module = "protobuf-java")
    }
}

// Force protobuf-javalite version across all dependencies
configurations.all {
    resolutionStrategy {
        force("com.google.protobuf:protobuf-javalite:3.25.5")
    }
}

kapt {
    correctErrorTypes = true
}
