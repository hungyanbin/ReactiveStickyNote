object Dependencies {
    object Android {
        const val coreKtx = "androidx.core:core-ktx:1.5.0"
        const val appCompat = "androidx.appcompat:appcompat:1.3.0"
        const val material = "com.google.android.material:material:1.3.0"

        const val composeUI = "androidx.compose.ui:ui:${Version.compose}"
        const val composeUITooling = "androidx.compose.ui:ui-tooling:${Version.compose}"
        const val composeMaterial = "androidx.compose.material:material:${Version.compose}"
        const val rxJava3 = "androidx.compose.runtime:runtime-rxjava3:${Version.compose}"
        const val composeActivity = "androidx.activity:activity-compose:1.3.0-beta01"
        const val navigation = "androidx.navigation:navigation-compose:2.4.0-alpha02"

        const val lifecycleKts = "androidx.lifecycle:lifecycle-runtime-ktx:2.3.1"
    }

    object Koin {
        const val android = "io.insert-koin:koin-android:${Version.koin}"
    }

    object RxJava {
        const val rxJava = "io.reactivex.rxjava3:rxjava:3.0.12"
        const val rxAndroid = "io.reactivex.rxjava3:rxandroid:3.0.0"
        const val rxKotlin = "io.reactivex.rxjava3:rxkotlin:3.0.0"
    }

    object Firebase {
        const val bom = "com.google.firebase:firebase-bom:28.0.1"
        const val firestore = "com.google.firebase:firebase-firestore"
    }

    object Test {
        const val jUnit4 = "junit:junit:4.+"
        const val jUnit5 = "org.junit.jupiter:junit-jupiter-api:${Version.jUnit5}"
        const val jUnit5Engine = "org.junit.jupiter:junit-jupiter-engine:${Version.jUnit5}"
    }
}

object Version {
    const val kotlin = "1.5.10"
    const val jUnit5 = "5.3.1"
    const val compose = "1.0.0-beta08"
    const val koin = "3.0.2"

    object Android {
        const val buildTool = "30.0.3"
        const val minSdk = 23
        const val targetSdk = 30
        const val compileSdk = 30
    }
}
