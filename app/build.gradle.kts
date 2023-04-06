plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    compileSdk = Version.Android.compileSdk
    buildToolsVersion = Version.Android.buildTool

    defaultConfig {
        applicationId = "com.yanbin.reactivestickynote"
        minSdk = Version.Android.minSdk
        targetSdk = Version.Android.targetSdk
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures.compose = true

    composeOptions {
        kotlinCompilerExtensionVersion = Version.compose
    }
}

dependencies {
    implementation(Dependencies.Android.coreKtx)
    implementation(Dependencies.Android.appCompat)
    implementation(Dependencies.Android.material)
    // Compose
    implementation(Dependencies.Android.composeUI)
    implementation(Dependencies.Android.composeMaterial)
    implementation(Dependencies.Android.composeUITooling)
    implementation(Dependencies.Android.rxJava3)
    implementation(Dependencies.Android.navigation)
    // Ktor
    implementation(Dependencies.Ktor.clientCore)
    implementation(Dependencies.Ktor.clientCio)
    implementation(Dependencies.Ktor.clientWebsocket)
    implementation(Dependencies.Ktor.serializationProtobuf)
    // Koin
    implementation(Dependencies.Koin.android)
    // RxJava
    implementation(Dependencies.RxJava.rxJava)
    implementation(Dependencies.RxJava.rxAndroid)
    implementation(Dependencies.RxJava.rxKotlin)
    implementation(Dependencies.RxJava.coroutineRx)
    // Firebase
    implementation(platform(Dependencies.Firebase.bom))
    implementation (Dependencies.Firebase.firestore)
    // Test
    testImplementation(Dependencies.Test.jUnit4)
    testImplementation(Dependencies.Test.mockk)

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

    implementation(Dependencies.Android.lifecycleKts)
    implementation(Dependencies.Android.composeActivity)
}