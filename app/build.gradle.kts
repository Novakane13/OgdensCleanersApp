plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.0"
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
        viewBinding = true // Proper place for enabling view binding
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


dependencies {
    // Kotlin BOM
    implementation(libs.kotlin.bom)

    // Kotlin Standard Library
    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.kotlinx.serialization.json)

    // AndroidX Core
    implementation(libs.androidx.core.ktx.v1120)

    // Lifecycle and Activity KTX
    implementation(libs.androidx.lifecycle.runtime.ktx.v261)
    implementation(libs.androidx.activity.compose.v180)

    // Compose BOM
    implementation(platform(libs.androidx.compose.bom.v20230901))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)

    // Retrofit and Gson
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.gson)

    // Google Play
    implementation(libs.play.services.ads.v2330)

    // Stripe
    implementation(libs.stripe.android)
    implementation(libs.stripe.android.v20151)
    implementation(libs.stripe.android.vpaymentcompose)
    implementation (libs.stripe.android)



    // Firebase Services
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.database.ktx)
    implementation(libs.firebase.messaging.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.dataconnect)
    implementation(libs.firebase.core)

    // Other dependencies
    implementation(libs.androidx.appcompat.v161)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.material)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.maps.v1802)
    implementation(libs.android.maps.utils)
    implementation(libs.cronet.embedded)
    implementation(libs.androidx.mediarouter)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.material.v190)
    implementation(libs.firebase.inappmessaging)
    implementation (libs.android.maps.utils.v223)
    implementation (libs.retrofit.v290)
    implementation (libs.converter.gson.v290)
    implementation(libs.volley)
    implementation(libs.play.services.wallet)
    implementation(libs.play.services.ads)

    // OkHttp
    implementation (libs.okhttp) // Or latest version
    implementation (libs.okhttp.v493)

    // Coroutines
    implementation (libs.kotlinx.coroutines.core)
    implementation (libs.kotlinx.coroutines.android)



    // Core Library Desugaring
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom.v20241000))
    androidTestImplementation(libs.ui.test.junit4)
    androidTestImplementation(libs.androidx.junit.v115)
    androidTestImplementation(libs.androidx.espresso.core.v351)

    // Debug dependencies
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}
