# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# signingConfig, minifyEnabled and shrinkResources flags in build.gradle.

-keep class org.example.pasienapp.data.** { *; }
-keep class org.example.pasienapp.api.** { *; }
-keepclassmembers class org.example.pasienapp.data.** { *; }

-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn retrofit2.**
-dontwarn javax.annotation.**

-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
