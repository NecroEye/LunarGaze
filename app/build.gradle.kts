import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("androidx.navigation.safeargs")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.muratcangzm.lunargaze"
    compileSdk = 34

    val localProperties = Properties().apply {
        load(FileInputStream(File(rootProject.rootDir, "local.properties")))
    }

    defaultConfig {
        applicationId = "com.muratcangzm.lunargaze"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = 34
        versionCode = 4
        versionName = "1.273642736427364"

        resourceConfigurations += listOf("tr", "ru", "fr", "de", "es")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        release {

            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false

            buildConfigField("String", "GIPHY_KEY", localProperties.getProperty("GIPHY_KEY"))
            buildConfigField("String", "TENOR_KEY", localProperties.getProperty("TENOR_KEY"))


            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

        }

        debug {

            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false

            buildConfigField("String", "GIPHY_KEY", localProperties.getProperty("GIPHY_KEY"))
            buildConfigField("String", "TENOR_KEY", localProperties.getProperty("TENOR_KEY"))

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
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }

    hilt {
        enableAggregatingTask = true
    }
}

dependencies {

    implementation(libs.bundles.androidx)
    implementation(libs.bundles.retrofit)
    implementation(libs.bundles.life.cycle)
    implementation(libs.bundles.navigation)
    implementation(libs.bundles.rxjava3)
    implementation(libs.bundles.room)
    annotationProcessor(libs.androidx.room.compiler)
    kapt(libs.androidx.room.compiler)
    implementation(libs.dagger.hilt)
    kapt(libs.hilt.compiler)
    implementation(libs.bundles.datastore)
    implementation(libs.firebase.analytics)
    implementation(libs.bundles.ui)
    implementation(libs.bundles.google.auth)
    implementation(libs.timber)
    implementation(libs.truth)
    testImplementation(libs.mockito.core)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.mockito.kotlin)
    debugImplementation(libs.fragment.testing)
    androidTestImplementation(libs.bundles.android.testing)

}

kapt {
    correctErrorTypes = true
}