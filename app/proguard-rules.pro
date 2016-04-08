-ignorewarnings

-dontshrink
-keepattributes Signature
-keepattributes *Annotation*
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontoptimize
-verbose
-dontwarn
-dontskipnonpubliclibraryclassmembers
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference


-dontwarn android.support.**
-keep class android.support.** { *;}
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *;}
-dontwarn org.apache.http.entity.mime.**
-keep class org.apache.http.entity.mime.** { *;}
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }


-keep class com.miqian.mq.activity.WebActivity { *;}

-keep class com.miqian.mq.entity.** { *;}

-keepclasseswithmembernames class * { 
    native <methods>; 
} 
-keepclasseswithmembernames class * { 
    public <init>(android.content.Context, android.util.AttributeSet); 
} 
-keepclasseswithmembernames class * { 
    public <init>(android.content.Context, android.util.AttributeSet, int); 
} 
-keepclassmembers enum * { 
    public static **[] values(); 
    public static ** valueOf(java.lang.String); 
} 
-keepclassmembers class * implements java.io.Serializable { 
    static final long serialVersionUID; 
    private static final java.io.ObjectStreamField[] serialPersistentFields; 
    private void writeObject(java.io.ObjectOutputStream); 
    private void readObject(java.io.ObjectInputStream); 
    java.lang.Object writeReplace(); 
    java.lang.Object readResolve(); 
} 
-keep class  * implements java.io.Serializable {
    public <methods>;
}
-keep class * implements android.os.Parcelable { 
  public static final android.os.Parcelable$Creator *; 
} 

##-------------------Begin proguard configuration for UMENG-------------------------
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

-keep public class com.miqian.mq.R$*{
public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
##--------------------End proguard configuration for UMENG------------------------

-dontwarn rx.**
-dontwarn okio.**
-dontwarn com.squareup.okhttp.*
-dontwarn retrofit.appengine.UrlFetchClient

-keep class com.umeng.message.* {#友盟推送
            public <fields>;
            public <methods>;
    }

    -keep class com.umeng.message.protobuffer.* {
            public <fields>;
            public <methods>;
    }

    -keep class com.squareup.wire.* {
            public <fields>;
            public <methods>;
    }

    -keep class com.umeng.message.local.* {
            public <fields>;
            public <methods>;
    }
    -keep class org.android.agoo.impl.*{
            public <fields>;
            public <methods>;
    }

    -keep class org.android.agoo.service.* {*;}

    -keep class org.android.spdy.**{*;}

    -keep public class [com.xiaobai.mizar].R$*{
        public static final int *;
    }
    #友盟分享
-dontwarn com.tencent.weibo.sdk -dontwarn com.tencent.**

##-------------------Begin proguard configuration for 连连支付-------------------------
-keep public class com.yintong.pay.utils.** {
    <fields>;
    <methods>;
}
-keep class com.yintong.secure.activityproxy.PayIntro$LLJavascriptInterface{*;}

-keep public class com.yintong.** {
    <fields>;
    <methods>;
}
-dontwarn com.yintong.**
-keep public class com.yintong.** {*;
}
##-------------------Begin proguard configuration for 连连支付-------------------------


##-------------------Begin proguard configuration for ShareSdk-------------------------
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}
-dontwarn cn.sharesdk.**
-dontwarn **.R$*
-keep class m.framework.**{*;}
# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
##--------------------End proguard configuration for ShareSdk------------------------

-dontwarn com.mob.tools.**
-keep class com.mob.tools.**{*;}

##------------------- for 友盟在线参数-------------------------
-keep class com.umeng.onlineconfig.OnlineConfigAgent {
        public <fields>;
        public <methods>;

}

-keep class com.umeng.onlineconfig.OnlineConfigLog {
        public <fields>;
        public <methods>;
}

-keep interface com.umeng.onlineconfig.UmengOnlineConfigureListener {
        public <methods>;
}

##-------------------Begin proguard configuration for MAA-------------------------
-keep class com.mato.** { *; }
-dontwarn com.mato.**
-keepattributes Exceptions, Signature, InnerClasses
##--------------------End proguard configuration for MAA------------------------

##-------------------Begin proguard configuration for GrowingIO-------------------------
-keep class com.growingio.android.sdk.** {
    public *;
}
##--------------------End proguard configuration for GrowingIO------------------------

##-------------------Begin proguard configuration for udesk-------------------------
-libraryjars libs/asmack-android-8-4.0.6.jar
-libraryjars libs/android-async-http.jar
-libraryjars libs/android-support-v4.jar
-libraryjars libs/qiniu-android-sdk-7.0.1.jar
-libraryjars libs/udesk_sdkui.2.3.0.jar
-libraryjars libs/universal-image-loader-1.9.4.jar

-keep class com.kenai.jbosh.** {*; }
-keep class com.novell.sasl.client.** {*; }
-keep class de.measite.smack.** {*; }
-keep class org.** {*; }
##--------------------End proguard configuration for udesk------------------------