plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
}

android {
    namespace = "es.jac.roncafit"
    compileSdk = 34

    defaultConfig {
        applicationId = "es.jac.roncafit"
        minSdk = 23
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    //ADDED BY JOEL ALACREU
    implementation ("androidx.fragment:fragment-ktx:1.6.1")//Cambiar de fragment
    implementation("com.squareup.retrofit2:retrofit:2.9.0")//Retrofit (Gestion API)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")//Retrofit (Gestion API)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation( "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("me.relex:circleindicator:2.1.6")//Carrusel de imagenes
    implementation("org.imaginativeworld.whynotimagecarousel:whynotimagecarousel:2.1.0")//Carrusel de imagenes
    implementation("com.google.zxing:core:3.5.0")//Código QR
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")//Código QR
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))//Firebase
    implementation("com.google.firebase:firebase-firestore-ktx")//Firestore
    implementation("androidx.activity:activity-ktx:1.8.2")//CallBackFlow
    //implementation("com.google.android.gms:play-services-base:18.5.0")
    //implementation("androidx.activity:activity-ktx")//CallBackFlow
    //implementation("com.google.firebase:firebase-analytics-ktx:21.2.2")
    //implementation("com.google.firebase:firebase-crashlytics-ktx:18.3.7")




    //END

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}