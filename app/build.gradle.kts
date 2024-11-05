plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.0"
    id("com.google.gms.google-services")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt") // KAPT plugin for annotation processing (required for Hilt)
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

        // Add BuildConfig fields for API keys
        buildConfigField "String", "GOOGLE_MAPS_API_KEY", "\"${getApiKey("GOOGLE_MAPS_API_KEY")}\""
        buildConfigField "String", "STRIPE_PUBLISHABLE_KEY", "\"${getApiKey("STRIPE_PUBLISHABLE_KEY")}\""
    }

    buildFeatures {
        viewBinding = true // Enable view binding
        compose = true // Enable Jetpack Compose
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
        isCoreLibraryDesugaringEnabled = true // For Java 8+ APIs
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3" // Ensure this matches your Compose version
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

// Function to retrieve API keys from secrets.properties
def getApiKey(propName) {
    def propertiesFile = rootProject.file('secrets.properties')
    if (propertiesFile.exists()) {
        Properties properties = new Properties()
        properties.load(new FileInputStream(propertiesFile))
        return properties[propName]
    } else {
        throw new GradleException("Properties file not found. Make sure 'secrets.properties' exists.")
    }
}

dependencies {
    implementation(libs.androidx.espresso.core.v351)
    implementation(libs.androidx.espresso.core.v351)
    implementation(libs.androidx.espresso.core.v351)
    // Kotlin BOM
    implementation platform("org.jetbrains.kotlin:kotlin-bom")

    // Kotlin Standard Library
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk8"
    implementation "org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0"

    // AndroidX Core and Lifecycle
    implementation "androidx.core:core-ktx:1.12.0"
    implementation "androidx.lifecycle:lifecycle-runtime-ktx:2.6.1"
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.6.1"
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1"

    // AndroidX UI Libraries
    implementation "androidx.appcompat:appcompat:1.6.1"
    implementation "androidx.recyclerview:recyclerview:1.3.1"
    implementation "androidx.constraintlayout:constraintlayout:2.1.4"
    implementation "androidx.paging:paging-runtime:3.2.1"
    implementation "androidx.navigation:navigation-fragment-ktx:2.6.0"
    implementation "androidx.navigation:navigation-ui-ktx:2.6.0"

    // Material Design
    implementation "com.google.android.material:material:1.9.0"

    // Compose BOM
    implementation platform("androidx.compose:compose-bom:2023.09.01")
    implementation "androidx.compose.ui:ui"
    implementation "androidx.compose.ui:ui-graphics"
    implementation "androidx.compose.ui:ui-tooling-preview"
    implementation "androidx.compose.material3:material3"

    // Retrofit and Gson
    implementation "com.squareup.retrofit2:retrofit:2.9.0"
    implementation "com.squareup.retrofit2:converter-gson:2.9.0"
    implementation "com.google.code.gson:gson:2.10"

    // OkHttp
    implementation "com.squareup.okhttp3:okhttp:4.9.3"

    // Google Play Services
    implementation "com.google.android.gms:play-services-ads:23.3.0"
    implementation "com.google.android.gms:play-services-maps:18.0.2"

    // Stripe
    implementation "com.stripe:stripe-android:20.15.1"

    // Firebase Services
    implementation platform("com.google.firebase:firebase-bom:32.1.1")
    implementation "com.google.firebase:firebase-auth-ktx"
    implementation "com.google.firebase:firebase-database-ktx"
    implementation "com.google.firebase:firebase-messaging-ktx"
    implementation "com.google.firebase:firebase-firestore-ktx"
    implementation "com.google.firebase:firebase-analytics"

    // Coroutines
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4"
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4"

    // Dagger Hilt for Dependency Injection
    implementation "com.google.dagger:hilt-android:2.44"
    kapt "com.google.dagger:hilt-android-compiler:2.44"

    // Hilt for ViewModel
    implementation "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03"
    kapt "androidx.hilt:hilt-compiler:1.0.0"

    // Core Library Desugaring
    coreLibraryDesugaring "com.android.tools:desugar_jdk_libs:1.1.5"

    // Testing dependencies
    testImplementation "junit:junit:4.13.2"
    androidTestImplementation platform("androidx.compose:compose-bom:2024.10.00")
    androidTestImplementation "androidx.compose.ui:ui-test-junit4"
    androidTestImplementation "androidx.test.ext:junit:1.1.5"
    androidTestImplementation "androidx.test.espresso:espresso-core:3.5.1"

    // Debug dependencies
    debugImplementation "androidx.compose.ui:ui-tooling"
    debugImplementation "androidx.compose.ui:ui-test-manifest"
}

kapt {
    correctErrorTypes = true
}
}