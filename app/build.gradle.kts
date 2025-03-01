plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.foodorder"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.foodorder"
        minSdk = 24
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
    buildFeatures{
        viewBinding = true

    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    //    okhttp3
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.1.0")
// JSON Parsing
    implementation("com.squareup.retrofit2:converter-gson:2.1.0")
    implementation("com.squareup.picasso:picasso:2.8")

    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.github.bumptech.glide:glide:4.12.0")
    implementation("com.github.ismaeldivita:chip-navigation-bar:1.4.0")
    implementation("com.google.firebase:firebase-database:20.3.1")
    implementation("com.google.firebase:firebase-auth:21.0.1")
    implementation ("com.squareup.picasso:picasso:2.71828")

    implementation ("org.mindrot:jbcrypt:0.4")
    implementation ("com.github.mukeshsolanki:android-otpview-pinview:2.1.0")
    implementation ("com.google.firebase:firebase-functions:19.0.2")

    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation ("com.android.volley:volley:1.2.1")
    implementation ("com.google.firebase:firebase-firestore:23.0.1")



    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

