plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)

    id("com.google.gms.google-services") // 👈 Add this line for Firebase
}

android {
    namespace = "com.example.petconnectt"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.petconnectt"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // ✅ Firebase Auth dependency (manual, since it's not part of libs.versions.toml)
    implementation("com.google.firebase:firebase-auth:22.3.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // Google Maps SDK for Android
    implementation("com.google.android.gms:play-services-maps:18.2.0")

// Location Services for GPS
    implementation("com.google.android.gms:play-services-location:21.0.1")

// Places API (optional but recommended for place search)
    implementation("com.google.android.libraries.places:places:3.4.0")
    implementation("androidx.cardview:cardview:1.0.0")

    implementation("com.google.firebase:firebase-database-ktx:20.3.0")

    // ✅ Firebase Storage (Kotlin version)
    implementation("com.google.firebase:firebase-storage-ktx:20.3.0")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")


}
