import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * 小米开屏 需要增加首发图标
 * aapt2 此方法失效
 */
class SplashResReplacePlugin : Plugin<Project> {

  override fun apply(project: Project) {
    if (project.plugins.hasPlugin(AppPlugin::class.java)) {
      var appExtension = project.extensions.findByType(AppExtension::class.java)
      appExtension!!.applicationVariants.all { variant ->
        var buildType = variant.buildType.name
        var flavor = variant.flavorName
        println("name=${variant.name} buildType=$buildType flavor=$flavor")
        if ("xiaomi" == flavor) {
          variant.outputs.all { output ->
            output.processResources.doFirst {
              var rootPath = project.buildDir.path + "/intermediates/res/merged/${variant.flavorName}/${variant.buildType.name}"
              replaceXiaomiDrawable(rootPath)
            }
          }
        }
      }
    }
  }

  private fun replaceXiaomiDrawable(rootPath: String) {
    var file = File("$rootPath/drawable/layer_splash.xml")
    var xiaomiFile = File("$rootPath/drawable/layer_splash_xiaomi_first.xml")
    if (file.exists() && xiaomiFile.exists()) {
      println("小米首发资源替换开始...${file.path}")
      var fis = FileInputStream(xiaomiFile)
      var fos = FileOutputStream(file)
      val buffer = ByteArray(fis.available())
      fis.read(buffer)
      fos.write(buffer)
      fis.close()
      fos.close()
      println("小米首发资源替换结束...${xiaomiFile.path}")
    } else {
      println("小米首发替换的资源不存在...")
    }
    xiaomiFile.delete()
  }
}