plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.treasure.basic"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    api("com.google.android.material:material:1.9.0")
    api("androidx.appcompat:appcompat:1.6.1")
    api("androidx.constraintlayout:constraintlayout:2.1.4")
    api("androidx.core:core-ktx:1.10.1")

    api("org.jetbrains.kotlin:kotlin-reflect:1.8.20")

    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    api("androidx.activity:activity-ktx:1.6.1")
    api("androidx.fragment:fragment-ktx:1.5.7")
    api("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    api("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")

    // Room
    api("androidx.room:room-runtime:2.5.2")
    kapt("androidx.room:room-compiler:2.5.2")
    api("androidx.room:room-ktx:2.5.2")

    // Retrofit
    api("com.squareup.retrofit2:retrofit:2.9.0")
    api("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp
    api("com.squareup.okhttp3:okhttp:4.11.0")
    api("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // ok2curl
    api("com.github.mrmike:ok2curl:0.7.0") {
        exclude(
            group = "com.squareup.okhttp3",
            module = "okhttp"
        )
    }

    // ARouter
    // api("com.alibaba:arouter-api:1.5.2")
    // kapt("com.alibaba:arouter-compiler:1.5.2")

    // EventBus
    api("org.greenrobot:eventbus:3.2.0")

    // Material Dialogs
    api("com.afollestad.material-dialogs:core:3.3.0")
    api("com.afollestad.material-dialogs:lifecycle:3.3.0")

    // Glide
    api("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0")
}
