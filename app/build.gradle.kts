plugins {
    id("com.android.application") version "8.1.1"
    id("org.jetbrains.kotlin.android") version "1.9.10"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.10"
    id("com.google.gms.google-services") version "4.4.2"
    id("com.google.dagger.hilt.android") version "2.44"
    id("org.jetbrains.kotlin.kapt") // Make sure it’s `org.jetbrains.kotlin.kapt`
}

android {
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

        // Add BuildConfig fields for API keys from secrets.properties
        buildConfigField("String", "GOOGLE_MAPS_API_KEY", "\"${System.getenv("GOOGLE_MAPS_API_KEY") ?: ""}\"")
        buildConfigField("String", "STRIPE_PUBLISHABLE_KEY", "\"${System.getenv("STRIPE_PUBLISHABLE_KEY") ?: ""}\"")
    }

    buildFeatures {
        viewBinding = true
        compose = true
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
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    // Core libraries
    implementation("androidx.core:core-ktx:${libs.versions.coreKtx.get()}")

    // AndroidX Libraries
    implementation("androidx.appcompat:appcompat:${libs.versions.appcompat.get()}")
    implementation("androidx.constraintlayout:constraintlayout:${libs.versions.constraintlayout.get()}")
    implementation("androidx.recyclerview:recyclerview:${libs.versions.recyclerview.get()}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${libs.versions.lifecycleRuntimeKtx.get()}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${libs.versions.lifecycleLivedataKtx.get()}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${libs.versions.lifecycleViewmodelKtx.get()}")
    implementation("androidx.navigation:navigation-fragment-ktx:${libs.versions.navigationFragmentKtx.get()}")
    implementation("androidx.navigation:navigation-ui-ktx:${libs.versions.navigationUiKtx.get()}")

    // Material Components
    implementation("com.google.android.material:material:${libs.versions.material.get()}")

    // Jetpack Compose BOM
    implementation(platform("androidx.compose:compose-bom:${libs.versions.composeBom.get()}"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // Retrofit and Gson
    implementation("com.squareup.retrofit2:retrofit:${libs.versions.retrofit.get()}")
    implementation("com.squareup.retrofit2:converter-gson:${libs.versions.converterGson.get()}")

    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:${libs.versions.okhttp.get()}")

    // Google Play Services
    implementation("com.google.android.gms:play-services-ads:${libs.versions.playServicesAds.get()}")
    implementation("com.google.android.gms:play-services-maps:${libs.versions.playServicesMaps.get()}")

    // Stripe SDK
    implementation("com.stripe:stripe-android:${libs.versions.stripeAndroid.get()}")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:${libs.versions.firebaseBom.get()}"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-database-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${libs.versions.kotlinxCoroutinesCore.get()}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${libs.versions.kotlinxCoroutinesAndroid.get()}")

    // Hilt for Dependency Injection
    implementation("com.google.dagger:hilt-android:${libs.versions.hiltAndroid.get()}")
    kapt("com.google.dagger:hilt-android-compiler:${libs.versions.hiltAndroidCompiler.get()}")

    // Core Desugaring
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:${libs.versions.desugarJdkLibs.get()}")

    // Testing Dependencies
    testImplementation("junit:junit:${libs.versions.junit.get()}")
    androidTestImplementation(platform("androidx.compose:compose-bom:${libs.versions.composeBom.get()}"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation("androidx.test.ext:junit:${libs.versions.androidxJunit.get()}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${libs.versions.espressoCore.get()}")

    // Debugging Tools
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
