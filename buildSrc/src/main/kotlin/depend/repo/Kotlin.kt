package depend.repo

class Kotlin {
  private val kotlinVersion = "1.5.0"
  private val coroutinesVersion = "1.4.3"

  val gradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
  val lib = "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion"
  val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
  val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"
}