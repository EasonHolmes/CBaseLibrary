#PictureSelector 2.0
-keep class com.luck.picture.lib.** { *; }
#Ucrop
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }
#Okio
-dontwarn org.codehaus.mojo.animal_sniffer.*
#------------------------------------------------

# OkHttp3 easyhttp9.6
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**
# 不混淆这个包下的类
-keep class com.xxx.xxx.xxx.xxx.** {
    <fields>;
}

#PictureSelector 2.0拍照图库选择照片
-keep class com.luck.picture.lib.** { *; }
#Ucrop
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }
#Okio
-dontwarn org.codehaus.mojo.animal_sniffer.*