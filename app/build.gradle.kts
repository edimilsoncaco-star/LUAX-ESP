plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
kotlin {
    jvmToolchain(17)
}
android {
    namespace = "com.luax.esp"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.luax.esp"
        minSdk = 23
        targetSdk = 28
        versionCode = 3
        versionName = "3.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
