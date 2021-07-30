## ----------------------------------
##     PictureSelector 2.0
## ----------------------------------
-keep class com.luck.picture.lib.** { *; }
#Ucrop
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }
#Okio
-dontwarn org.codehaus.mojo.animal_sniffer.*
#------------------------------------------------

## ----------------------------------
##     xxpermissions
## ----------------------------------
-dontwarn com.hjq.permissions.**

#androidx包使用混淆 app项目下直接用就可
#-keep class com.google.android.material.** {*;}
#-keep class androidx.** {*;}
#-keep public class * extends androidx.**
#-keep interface androidx.** {*;}
#-dontwarn com.google.android.material.**
#-dontnote com.google.android.material.**
#-dontwarn androidx.**

## ----------------------------------
##     room
## ----------------------------------
-dontwarn android.arch.util.paging.CountedDataSource
-dontwarn android.arch.persistence.room.paging.LimitOffsetDataSource

## ----------------------------------
##     viewmodel
## ----------------------------------
-keep class * extends androidx.lifecycle.ViewModel {
    <init>();
}
-keep class * extends androidx.lifecycle.AndroidViewModel {
    <init>(android.app.Application);
}
## ----------------------------------
##     livedata
## ----------------------------------
-keepclassmembers public class * extends androidx.lifecycle.LiveData {
    public <init>(...);
}
