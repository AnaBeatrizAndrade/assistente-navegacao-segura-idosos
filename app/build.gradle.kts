plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "br.ufms.assistente.navegacao"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "br.ufms.assistente.navegacao"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

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

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)

    implementation("com.google.android.material:material:1.11.0")
    implementation("com.squareup.okhttp3:okhttp:5.5.0")
    implementation("androidx.work:work-runtime:2.9.0")
    implementation("androidx.security:security-crypto:1.1.0")
    implementation("androidx.lifecycle:lifecycle-service:2.11.0")
}