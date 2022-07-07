# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Develop\Tools\android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Metadata created via reflection
-keepclassmembers public class * extends com.genexus.android.core.base.metadata.theme.ThemeClassDefinition {
   public <init>(...);
}
-keepclassmembers public class * extends com.genexus.android.core.base.metadata.rules.RuleDefinition {
   public <init>(...);
}

# Constructors of user controls
-keepclassmembers class * extends android.view.View {
	public <init>(android.content.Context, com.genexus.android.core.base.metadata.layout.LayoutItemDefinition);
	public <init>(android.content.Context, com.genexus.android.core.ui.Coordinator, com.genexus.android.core.base.metadata.layout.LayoutItemDefinition);
	public <init>(android.content.Context, com.genexus.android.core.ui.Coordinator, com.genexus.android.core.base.metadata.layout.GridDefinition);
}

# Functions used by expression evaluator
-keepclassmembers class com.genexus.GXutil {
    public <methods>;
}
-keepclassmembers class com.genexus.android.core.base.metadata.expressions.GXutilPlus {
    public <methods>;
}
-keepclassmembers class com.genexus.GxRegex {
    public <methods>;
}
-keepclassmembers class com.genexus.LocalUtil {
    public <methods>;
}
-keepclassmembers class com.genexus.PrivateUtilities {
    public <methods>;
}
-dontnote com.genexus.GXutil

# Classes for offline apps (reorganization, business components, procedures, &c)
-keep public class * extends com.genexus.GXReorganization {
    public <init>(...);
}
-keep public class * extends com.artech.base.synchronization.GXOfflineDatabase {
    public <init>(int);
}
-keep public class * extends com.genexus.GXProcedure {
    public <init>(int);
    # Dynamic combos, autocomplete, input descriptions.
    *** entity_*(...);
    *** dyn_entity_*(...);
}
-keep public class * extends com.genexus.GxSilentTrnSdt {
    public <init>(int);
}
-keep public class * extends com.genexus.xml.GXXMLSerializable {
    public *** getgxTv_*();
    public void setgxTv_*(...);
}
-dontnote com.genexus.GXBaseCollection
-dontnote com.genexus.GXSimpleCollection

# Misc stuff that we reference via reflection
-keepclassmembernames class androidx.appcompat.widget.Toolbar {
    private android.widget.TextView mTitleTextView;
}
-keep public class com.genexus.db.SQLAndroidBlobFileHelper {
    public <methods>;
}
-keep class org.sqldroid.SQLDroidDriver
-dontnote com.genexus.db.driver.GXConnection
-keep class HTTPClient.*Module

# Picasso uses OkHttp by reflection, but it works fine without it.
-dontnote com.squareup.picasso.**
-dontwarn com.squareup.picasso.**

# Disable misc Android warnings. We assume they know what they're doing.
-dontnote androidx.**
-dontnote com.google.**

# SimpleXML supports Java Streams, but it's not available on Android
-dontwarn javax.xml.stream.**

# Ignore duplicate classes added by ProGuard without regard to the useLibrary 'org.apache.http.legacy' directive.
# See issue https://code.google.com/p/android/issues/detail?id=194513.
-dontnote android.net.http.*
-dontnote org.apache.http.**

# Ignore warnings about obfuscated parameters in kept methods (mostly view property setters).
-dontnote com.genexus.android.core.base.metadata.**
-dontnote com.genexus.android.core.controls.**
-dontnote com.genexus.android.core.fragments.**
-dontnote com.genexus.controls.**
-dontnote com.artech.extendedcontrols.**
-dontnote com.artech.android.layout.*
-dontnote com.extensions.controls.sparkline.*
-dontnote com.artech.base.services.IPropertiesObject
-dontnote com.genexus.android.core.base.model.PropertiesObject

# Temporary, to allow us to proceed. These shouldn't be ignored, actually.
-dontwarn com.genexus.cryptography.**
-dontwarn HTTPClient.JSSESSLConnection
-dontwarn com.genexus.GXGeospatial


# OkHttp ProGuard configuration (as of 4.9)
# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*
# OkHttp platform used only on JVM and when Conscrypt and other security providers are available.
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
