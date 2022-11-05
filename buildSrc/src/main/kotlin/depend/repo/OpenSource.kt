package depend.repo

class OpenSource {

  val fastjson = "com.alibaba:fastjson:1.1.71.android"
  val gson = "com.google.code.gson:gson:2.8.6"
  val exoplayer = "com.google.android.exoplayer:exoplayer-core:2.12.1"
  val playService = "com.google.android.gms:play-services-places:12.0.1"
  val work = "android.arch.work:work-runtime:1.0.0-alpha06"
  val firebaseJobDispatcher = "com.firebase:firebase-jobdispatcher:0.8.5"

  val thinDownload = "com.mani:ThinDownloadManager:1.2.5"
  val eventbus = "org.greenrobot:eventbus:3.1.1"
  val greendao = "org.greenrobot:greendao:3.2.0"

  val debugLeakCanary = "com.squareup.leakcanary:leakcanary-android:2.9.1"


  val rx = Rx()

  class Rx {
    val rxjava = "io.reactivex.rxjava2:rxjava:2.1.0"
    val rxandroid = "io.reactivex.rxjava2:rxandroid:2.1.0"

    val rxpermissions = "com.github.tbruyelle:rxpermissions:0.10.2"

    val rxlifecycle = "com.trello.rxlifecycle3:rxlifecycle-android:3.1.0"
    val rxlifecycle_component = "com.trello.rxlifecycle3:rxlifecycle-components:3.1.0"
  }

  val retrofit = Retrofit()

  class Retrofit {
    private val retrofitVersion = "2.6.1"
    val runtime = "com.squareup.retrofit2:retrofit:$retrofitVersion"
    val gson = "com.squareup.retrofit2:converter-gson:$retrofitVersion"
    val rxjava2 = "com.squareup.retrofit2:adapter-rxjava2:$retrofitVersion"
    val scalars = "com.squareup.retrofit2:converter-scalars:$retrofitVersion"
  }

  val facebook = Facebook()

  class Facebook {
    private val frescoVersion = "2.3.0"
    val fresco = "com.facebook.fresco:fresco:$frescoVersion"
    val webp = "com.facebook.fresco:webpsupport:$frescoVersion"
    val infer = "com.facebook.infer.annotation:infer-annotation:0.18.0"
    val animatedWebp = "com.facebook.fresco:animated-webp:$frescoVersion"
    val animatedGif = "com.facebook.fresco:animated-gif:$frescoVersion"
    val animatedGifLite = "com.facebook.fresco:animated-gif-lite:$frescoVersion"
    val animatedBase = "com.facebook.fresco:animated-base:$frescoVersion"
    val uiCommon = "com.facebook.fresco:ui-common:$frescoVersion"
    val rebound = "com.facebook.rebound:rebound:0.3.8"
  }

  val glide = Glide()

  class Glide {
    private val glideVersion = "4.11.0"
    val runtime = "com.github.bumptech.glide:glide:$glideVersion"
    val compiler = "com.github.bumptech.glide:compiler:$glideVersion"
  }

  val lottie = "com.airbnb.android:lottie:5.2.0"
}