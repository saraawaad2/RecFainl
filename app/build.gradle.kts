plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.recipe"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.recipe"
        minSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // ✅ Firebase BoM to avoid version conflicts
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))

    // ✅ Firebase libraries (no versions needed when using BoM)
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-storage")

    // FirebaseUI needs specific version
    implementation("com.firebaseui:firebase-ui-firestore:8.0.2")

    // Core AndroidX
    implementation("androidx.appcompat:appcompat:1.7.0-alpha03")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.drawerlayout:drawerlayout:1.2.0")

    // Navigation
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")

    // Google Play Services
    implementation("com.google.android.gms:play-services-location:21.2.0")

    // CameraX
    implementation("androidx.camera:camera-core:1.4.0-alpha04")
    implementation("androidx.camera:camera-camera2:1.4.0-alpha04")

    // Glide (image loader)
    implementation("com.github.bumptech.glide:glide:4.15.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.0")

    // Picasso (alternative image loader)
    implementation("com.squareup.picasso:picasso:2.71828")

    // Signature pad
    implementation("com.github.gcacace:signature-pad:1.3.1")

    // AndroidX activity alias
    implementation(libs.activity)

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
