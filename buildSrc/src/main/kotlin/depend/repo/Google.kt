package depend.repo

class Google {
  val gradleVersion = "com.android.tools.build:gradle:7.0.1"
  val compileSdkVersion = 31
  val buildToolsVersion = "30.0.2"
  val minSdk = 21
  val targetSdk = 30

  val annotation = "androidx.annotation:annotation:1.1.0"
  val core = "androidx.core:core:1.0.2"
  val coreKtx = "androidx.core:core-ktx:1.7.0"
  val fragment = "androidx.fragment:fragment:1.2.4"
  val palette = "androidx.palette:palette:1.0.0"

  val appcompat = "androidx.appcompat:appcompat:1.4.2"
  val material = "com.google.android.material:material:1.6.1"
  val constraintlayout = "androidx.constraintlayout:constraintlayout:2.1.4"
  val viewpager = "androidx.viewpager:viewpager:1.0.0"
  val viewpager2 = "androidx.viewpager2:viewpager2:1.0.0"
  val recyclerview = "androidx.recyclerview:recyclerview:1.1.0"
  val cardview = "androidx.cardview:cardview:1.0.0"
  val swiperefreshlayout = "androidx.swiperefreshlayout:swiperefreshlayout:1.0.0"
  val coordinatorlayout = "androidx.coordinatorlayout:coordinatorlayout:1.2.0"
  val flexbox = "com.google.android.flexbox:flexbox:3.0.0"

  val lifecycle = Lifecycle()

  class Lifecycle {
    private val lifecycleVersion = "2.2.0"
    val process = "androidx.lifecycle:lifecycle-process:$lifecycleVersion"
    val viewmodelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion"
    val runtimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion"
  }

  val paging = Paging()

  class Paging {
    private val pagingVersion = "3.1.1"
    val runtime = "androidx.paging:paging-runtime:$pagingVersion"
  }
}