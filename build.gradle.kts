plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.google.gms.google.services) apply false
    // ✅ No duplicate plugins block allowed
}

buildscript {
    dependencies {
        // ✅ Firebase Google Services plugin
        classpath("com.google.gms:google-services:4.4.0")
    }
    repositories {
        google()
        mavenCentral()
    }
}
