import com.android.build.api.dsl.ApplicationBuildFeatures
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.safe.navigation)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

android {
    namespace = "com.muratcangzm.lunargaze"
    compileSdk = ProjectConfig.compileSdk

    val localProperties = Properties().apply {
        load(FileInputStream(File(rootProject.rootDir, "local.properties")))
    }

    defaultConfig {
        applicationId = "com.muratcangzm.lunargaze"
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
        versionCode = 4
        versionName = "1.273642736427364"

        resourceConfigurations += listOf("tr", "ru", "fr", "de", "es")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false

            buildConfigField("String", "GIPHY_KEY", localProperties.getProperty("GIPHY_KEY"))
            buildConfigField("String", "TENOR_KEY", localProperties.getProperty("TENOR_KEY"))
            buildConfigField("String", "OPEN_AI_KEY", localProperties.getProperty("OPEN_AI_KEY"))

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false

            buildConfigField("String", "GIPHY_KEY", localProperties.getProperty("GIPHY_KEY"))
            buildConfigField("String", "TENOR_KEY", localProperties.getProperty("TENOR_KEY"))
            buildConfigField("String", "OPEN_AI_KEY", localProperties.getProperty("OPEN_AI_KEY"))

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_20
        targetCompatibility = JavaVersion.VERSION_20
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_20.toString()
    }

    buildFeatures(Action<ApplicationBuildFeatures> {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    })

    lint {
        disable.add("MissingTranslation")
    }

    hilt {
        enableAggregatingTask = true
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(20))
        }
    }
}

dependencies {

    implementation(project(":core:model"))

    implementation(libs.bundles.androidx)
    implementation(libs.bundles.retrofit)
    implementation(libs.bundles.life.cycle)
    implementation(libs.bundles.navigation)
    implementation(libs.bundles.rxjava3)
    implementation(libs.bundles.room)
    implementation(libs.dagger.hilt)
    ksp(libs.hilt.compiler)
    ksp(libs.androidx.room.compiler)
    annotationProcessor(libs.androidx.room.compiler)
    implementation(libs.bundles.datastore)
    implementation(libs.firebase.analytics)
    implementation(libs.bundles.ui)
    implementation(libs.bundles.google.auth)
    implementation(libs.timber)
    implementation(libs.truth)
    implementation(libs.androidx.security.crypto)
    testImplementation(libs.mockito.core)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.mockito.kotlin)
    debugImplementation(libs.fragment.testing)
    androidTestImplementation(libs.bundles.android.testing)

    coreLibraryDesugaring(libs.desugare)
}

ksp { arg("room.schemaLocation", "${projectDir}/schemas") }