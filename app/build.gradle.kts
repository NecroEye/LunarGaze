plugins {
    id("com.android.application")
    id("androidx.navigation.safeargs")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.muratcangzm.lunargaze"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.muratcangzm.lunargaze"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {

        release {
            isMinifyEnabled = true
            isDebuggable = false
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            isMinifyEnabled = false
            isDebuggable = true
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }


    buildFeatures{
        viewBinding = true
    }

}

dependencies {

    val nav_version = "2.7.6"
    val lifecycle_version = "2.7.0"
    val room_version = "2.6.1"

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")


    //LifeCycle & LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")

    //Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    //RxJava
    implementation ("io.reactivex.rxjava3:rxjava:3.0.0")
    implementation ("io.reactivex.rxjava3:rxandroid:3.0.0")

    //Room
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-rxjava3:$room_version")
    kapt("androidx.room:room-compiler:$room_version")

    //Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.48.1")
    kapt("com.google.dagger:hilt-android-compiler:2.48.1")

    //UI
    implementation("com.github.bumptech.glide:glide:4.16.0")

    //Testing
    androidTestImplementation("androidx.navigation:navigation-testing:$nav_version")
    implementation("com.google.truth:truth:1.1.4")
    testImplementation("org.mockito:mockito-core:4.7.0")

    //Android Testing
    androidTestImplementation("androidx.navigation:navigation-testing:$nav_version")
    androidTestImplementation("org.mockito:mockito-android:4.7.0")
    androidTestImplementation("org.mockito:mockito-core:4.7.0")
    androidTestImplementation("com.google.truth:truth:1.1.4")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.44")

}

kapt {
    correctErrorTypes = true
}