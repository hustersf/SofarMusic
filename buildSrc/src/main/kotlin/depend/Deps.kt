package depend

import depend.repo.Google
import depend.repo.Kotlin
import depend.repo.OpenSource
import depend.repo.Test

object Deps {

  val google = Google()

  val test = Test()

  val kotlin = Kotlin()

  val openSource = OpenSource()
}