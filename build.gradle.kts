

buildscript{
    repositories{
        google()
        mavenCentral()
        maven { url = uri("https://www.jitpack.io" ) }
    }
    dependencies{
        classpath("com.google.gms:google-services:4.4.1")
        val nav_version = "2.7.7"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
    }
}

plugins {
    id("com.android.application") version "8.4.0-rc02" apply false
    id("com.android.library") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.7.0" apply false
    id("com.google.dagger.hilt.android") version "2.48.1" apply false
}