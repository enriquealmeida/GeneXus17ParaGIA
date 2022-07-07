# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Rules for BaiduMaps see
#    https://programmersought.com/article/36655549123/
#  and http://lbsyun.baidu.com/index.php?title=androidsdk/guide/create-project/androidstudio
#

-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-keep class com.baidu.vi.** {*;}
-keep class mapsdkvi.com.** {*;}
-dontwarn com.baidu.**

