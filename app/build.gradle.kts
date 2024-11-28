plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.monkeypoxapp"
    compileSdk = 34


    defaultConfig {
        applicationId = "com.example.monkeypoxapp"
        minSdk = 27
        targetSdk = 34
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
    buildFeatures {
        viewBinding = true
        mlModelBinding = true

    }

    dependencies {

        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.appcompat)
        implementation(libs.material)
        implementation(libs.androidx.activity)
        implementation(libs.androidx.constraintlayout)
        implementation(libs.androidx.annotation)
        implementation(libs.androidx.lifecycle.livedata.ktx)
        implementation(libs.androidx.lifecycle.viewmodel.ktx)
        implementation(libs.firebase.firestore)
        implementation(libs.firebase.auth.ktx)
        implementation(libs.tensorflow.lite.support)
        implementation(libs.tensorflow.lite.metadata)
        implementation(libs.tensorflow.lite.gpu)
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
        implementation(platform("com.google.firebase:firebase-bom:33.3.0"))
        implementation("com.google.firebase:firebase-analytics-ktx")
        implementation("com.google.firebase:firebase-auth")
        implementation("com.google.firebase:firebase-firestore") // Import the GPU delegate plugin Library for GPU inference
    }
}