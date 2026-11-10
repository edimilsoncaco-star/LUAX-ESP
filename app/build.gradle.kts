plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
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
}
