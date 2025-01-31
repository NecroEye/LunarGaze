plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.safe.navigation)
}

android {
    namespace = "com.muratcangzm.model"
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        minSdk = ProjectConfig.minSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    @Suppress("UnstableApiUsage")
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_20
        targetCompatibility = JavaVersion.VERSION_20
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_20.toString()
    }

    tasks.withType<JavaCompile>().configureEach {
        options.compilerArgs.add("--enable-preview")
    }

    tasks.withType<Test>().configureEach {
        jvmArgs("--enable-preview")
    }

    tasks.withType<JavaExec>().configureEach {
        jvmArgs("--enable-preview")
    }
}

dependencies {

    implementation(libs.bundles.androidx)
    testImplementation(libs.arch.core)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.room.testing)
    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.mockito.core)
    androidTestImplementation(libs.mockito.android)
    ksp(libs.androidx.room.compiler)
    ksp(libs.hilt.compiler)
    implementation(libs.dagger.hilt)
    implementation(libs.bundles.room)
    implementation(libs.converter.gson)
    implementation(libs.bundles.rxjava3)
    implementation(libs.bundles.navigation)
}