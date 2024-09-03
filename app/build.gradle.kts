plugins {
    id ("com.android.application")
    id ("org.jetbrains.kotlin.android")
    id ("com.google.devtools.ksp")
    id ("com.google.dagger.hilt.android")
    id ("org.jetbrains.kotlin.plugin.compose")
    id ("androidx.room")
}

android {

    room {
        schemaDirectory("$projectDir/schemas")
    }

    compileSdk = 34

    defaultConfig {
        applicationId = "com.appspell.sportintervaltimer"
        minSdk = 30
        targetSdk = 34
        versionCode = 10000001
        versionName = "1.0.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    namespace = "com.appspell.sportintervaltimer"
}

dependencies {

    // General compose dependencies
    implementation ("androidx.activity:activity-compose:1.9.1")
    implementation ("androidx.compose.ui:ui-tooling-preview:1.6.8")

    // Other compose dependencies
    implementation ("androidx.constraintlayout:constraintlayout-compose:1.0.1")

    // Compose for Wear OS Dependencies
    implementation ("androidx.wear.compose:compose-material:1.3.1")

    // Foundation is additive, so you can use the mobile version in your Wear OS app.
    implementation ("androidx.wear.compose:compose-foundation:1.3.1")

    // Wear specific Compose Dependencies
    implementation ("androidx.wear.compose:compose-material:1.3.1")
    implementation ("androidx.wear.compose:compose-foundation:1.3.1")

    // For navigation within your app...
    implementation ("androidx.wear.compose:compose-navigation:1.3.1")
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.7")

    // Hilt
    implementation ("com.google.dagger:hilt-android:2.51")
    implementation ("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation ("androidx.startup:startup-runtime:1.1.1")
    ksp ("com.google.dagger:hilt-compiler:2.51")

    // Room
    implementation ("androidx.room:room-runtime:2.6.1")
    ksp ("androidx.room:room-compiler:2.6.1")

    // Time
    implementation ("net.danlew:android.joda:2.12.0")

    implementation ("androidx.core:core-ktx:1.13.1")
    implementation ("com.google.android.gms:play-services-wearable:18.2.0")
    implementation ("androidx.legacy:legacy-support-v4:1.0.0")
    implementation ("androidx.wear:wear:1.3.0")

    // Testing
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.2.1")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation ("androidx.compose.ui:ui-test-junit4:1.6.8")
    debugImplementation ("androidx.compose.ui:ui-tooling:1.6.8")
}