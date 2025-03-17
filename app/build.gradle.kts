@file:Suppress("UnstableApiUsage")


plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    kotlin("android")
}

repositories{
    google()
}


android {
    namespace = "com.example.myfirstapp"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.example.myfirstapp"
        minSdk = 23
        targetSdk = 33
        versionCode = 1
        versionName = "1.0-${System.getenv("VERSION_SHA")}"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.core:core-ktx:1.12.0")
    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.7.4"))
    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.activity:activity:1.8.0")
    implementation("com.google.firebase:firebase-firestore-ktx:24.11.0")


    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("com.github.bumptech.glide:glide:4.12.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.gridlayout:gridlayout:1.0.0")
    implementation("com.google.firebase:firebase-database:20.3.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("com.google.firebase:firebase-storage-ktx:20.3.0")
    implementation("com.google.firebase:firebase-auth-ktx:22.3.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.3")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    // source:https://developer.android.com/topic/libraries/architecture/viewmodel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
}
