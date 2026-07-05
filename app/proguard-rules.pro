# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# =========================================================
# 基础混淆规则
# =========================================================
# 保持注解不被混淆
-keepattributes *Annotation*
# 保持泛型不被混淆
-keepattributes Signature
# 保持内部类不被混淆
-keepattributes InnerClasses
# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable

# =========================================================
# 保持自定义 Bean 类不被混淆 (Gson 解析需要)
# =========================================================
-keep class com.app.video.user.core.bean.** { *; }

# =========================================================
# OkHttp 混淆规则
# =========================================================
# JSR 305 annotations are for optional nullability checks
-dontwarn javax.annotation.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*
# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform

# =========================================================
# Gson 混淆规则
# =========================================================
-keep class com.google.gson.** { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.reflect.** { *; }
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.internal.UnsafeAllocator { *; }
-keep class com.google.gson.internal.bind.TypeAdapter { *; }
-keep class com.google.gson.internal.bind.TypeAdapter$Factory { *; }
-dontwarn com.google.gson.**

# =========================================================
# EventBus 混淆规则
# =========================================================
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

# =========================================================
# Coroutines 混淆规则 (通常R8自带了，加上以防万一)
# =========================================================
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# =========================================================
# AndroidX & Material 混淆规则
# =========================================================
-dontwarn androidx.**
-keep class androidx.** { *; }
-keep interface androidx.** { *; }
-dontwarn com.google.android.material.**
-keep class com.google.android.material.** { *; }
-keep class androidx.lifecycle.** { *; }
-keep class androidx.annotation.** { *; }