# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/sufan/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html
#
# Add any project specific keep options here:
#
# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}



-libraryjars /Users/sufan/Library/Android/sdk/platforms/android-25/android.jar
-libraryjars /Users/sufan/Desktop/安卓jar包/proguard/rt.jar
-libraryjars /Users/sufan/Desktop/okhttp-3.5.0.jar
-libraryjars /Users/sufan/Desktop/retrofit-2.1.0.jar
-libraryjars /Users/sufan/Desktop/adapter-rxjava-2.1.0.jar
-libraryjars /Users/sufan/Desktop/logging-interceptor-3.5.0.jar
-libraryjars /Users/sufan/Desktop/rxjava-1.1.5.jar
-libraryjars /Users/sufan/Desktop/converter-gson-2.1.0.jar
-libraryjars /Users/sufan/Desktop/rxandroid-1.1.0.jar

-dontshrink
-dontoptimize
-dontusemixedcaseclassnames
-keepattributes *Annotation*
-keepattributes Signature,InnerClasses
-dontpreverify
-verbose

-dontwarn android.support.**
-keep class android.support.** {*;}


-dontwarn com.sf.libnet.control.**
-keep class com.sf.libnet.control.** { *;}


-keep public class com.google.vending.licensing.ILicensingService

-keep public class com.android.vending.licensing.ILicensingService

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
    void set*(***);
    *** get*();
}

# We want to keep methods in Activity that could be used in the XML attribute onClick
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}

-keepclassmembers class * extends android.os.Parcelable {
    public static final android.os.Parcelable$Creator CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keep class * extends android.app.Activity

-keep class * extends android.app.Service

-keep class * extends android.content.BroadcastReceiver

-keep class * extends android.content.ContentProvider

-keep class * extends android.content.ContentProvider


# Also keep - Enumerations. Keep the special static methods that are required in
# enumeration classes.
-keepclassmembers enum  * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep names - Native method names. Keep all native class/method names.
-keepclasseswithmembers,allowshrinking class * {
    native <methods>;
}
