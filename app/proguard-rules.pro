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
-keepattributes Exceptions, Signature, InnerClasses


-dontwarn android.support.**
-keep class android.support.** { *;}
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *;}
-dontwarn org.apache.http.entity.mime.**
-keep class org.apache.http.entity.mime.** { *;}
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }


-keep class com.miqian.mq.activity.WebActivity { *;}
-keep class com.miqian.mq.activity.WebHFActivity { *;}

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

##-------------------Begin proguard configuration for ShareSdk-------------------------
-libraryjars ../ShareSDK/libs/MobCommons-2016.0707.1708.jar
-libraryjars ../ShareSDK/libs/MobTools-2016.0707.1708.jar
-libraryjars ../ShareSDK/libs/ShareSDK-Core-2.7.4.jar
-libraryjars ../ShareSDK/libs/ShareSDK-QQ-2.7.4.jar
-libraryjars ../ShareSDK/libs/ShareSDK-QZone-2.7.4.jar
-libraryjars ../ShareSDK/libs/ShareSDK-ShortMessage-2.7.4.jar
-libraryjars ../ShareSDK/libs/ShareSDK-SinaWeibo-2.7.4.jar
-libraryjars ../ShareSDK/libs/ShareSDK-Wechat-2.7.4.jar
-libraryjars ../ShareSDK/libs/ShareSDK-Wechat-Core-2.7.4.jar
-libraryjars ../ShareSDK/libs/ShareSDK-Wechat-Moments-2.7.4.jar
-libraryjars ../ShareSDK/libs/ShareSDK-Email-2.7.4.jar

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


##-------------------Begin proguard configuration for GrowingIO-------------------------
-keep class com.growingio.android.sdk.** {
    public *;
}
##--------------------End proguard configuration for GrowingIO------------------------

##-------------------Begin proguard configuration for udesk-------------------------
-libraryjars ../Udesk_Separate_UI/libs/android-async-http-1.4.6.jar
-libraryjars ../Udesk_Separate_UI/libs/qiniu-android-sdk-7.0.1.jar
-libraryjars ../Udesk_Separate_UI/libs/udesk_sdk_3.2.0.jar
-libraryjars ../Udesk_Separate_UI/libs/bugly_crash_release__2.1.jar

-keep class udesk.**{*; }
-keep class cn.udesk.**{*; }
-keep class com.qiniu.android.**{*; }
-keep class com.tencent.bugly.**{*; }
-keep class com.nostra13.universalimageloader.**{*; }
-keep class com.loopj.android.http.**{*; }
-keep class rx.internal.**{*; }

-keep class com.kenai.jbosh.** {*; }
-keep class com.novell.sasl.client.** {*; }
-keep class de.measite.smack.** {*; }
-keep class org.** {*; }
-keepclasseswithmembernames class udesk.core.**  {*; }
-keepclasseswithmembernames class cn.udesk.**  {*; }

-keep class de.measite.smack.AndroidDebugger { *; }
-keep class * implements org.jivesoftware.smack.initializer.SmackInitializer
-keep class * implements org.jivesoftware.smack.provider.IQProvider
-keep class * implements org.jivesoftware.smack.provider.PacketExtensionProvider
-keep class * extends org.jivesoftware.smack.packet.Packet
-keep class org.jivesoftware.smack.XMPPConnection
-keep class org.jivesoftware.smack.ReconnectionManager
-keep class org.jivesoftware.smack.CustomSmackConfiguration
-keep class org.jivesoftware.smackx.disco.ServiceDiscoveryManager
-keep class org.jivesoftware.smackx.xhtmlim.XHTMLManager
-keep class org.jivesoftware.smackx.muc.MultiUserChat
-keep class org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager
-keep class org.jivesoftware.smackx.bytestreams.socks5.Socks5BytestreamManager
-keep class org.jivesoftware.smackx.filetransfer.FileTransferManager
-keep class org.jivesoftware.smackx.iqlast.LastActivityManager
-keep class org.jivesoftware.smackx.commands.AdHocCommandManager
-keep class org.jivesoftware.smackx.ping.PingManager
-keep class org.jivesoftware.smackx.privacy.PrivacyListManager
-keep class org.jivesoftware.smackx.time.EntityTimeManager
-keep class org.jivesoftware.smackx.vcardtemp.VCardManager
##--------------------End proguard configuration for udesk------------------------