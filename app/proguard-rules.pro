#打开project.properties文件中的proguard.config。
#-------------------------------------------基本不用动区域--------------------------------------------
#---------------------------------基本指令区----------------------------------
-optimizationpasses 5                                                # 指定代码的压缩级别
-dontusemixedcaseclassnames                                          # 是否使用大小写混合
-dontskipnonpubliclibraryclasses                                     # 是否混淆第三方jar
-dontpreverify                                                       # 混淆时是否做预校验
-verbose                                                             # 混淆时是否记录日志
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*      # 混淆时所采用的算法


#---------------------------------默认保留区---------------------------------
#保持一些基本的不会混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keepclasseswithmembernames class * {                     # 保持 native 方法不被混淆
    native <methods>;
}
-keepclasseswithmembers class * {                          # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {                          # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity {    # 保持自定义控件类不被混淆
    public void *(android.view.View);
}
-keepclassmembers enum * {                                   # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {             # 保持 Parcelable 不被混淆
    public static final android.os.Parcelable$Creator *;
}

#自己写的自定义控件不要混淆
-keep public class * extends android.view.View { *; }
#adapter也不能混淆
-keep public class * extends android.widget.BaseAdapter { *; }
#如果你使用了RecyclerView.Adapter 添加下面这行
-keep public class * extends android.support.v7.widget.RecyclerView.Adapter{ *; }
#数据模型不要混淆
-keepnames class * implements java.io.Serializable
#如果项目中用到了反射，则涉及到反射的类不能混淆(比如，transforms包下的所有类不混淆)
-keep class com.example.newsdemo.app.banner.transforms.**{ *; }

#----------------------------------------------------------------------------

#---------------------------------webview------------------------------------
-keepclassmembers class fqcn.of.javascript.interface.for.Webview {
   public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, jav.lang.String);
}
#----------------------------------------------------------------------------

#第三方类库(有多少加多少)
#-libraryjars libs/activation.jar
#-libraryjars libs/additionnal.jar
#-libraryjars libs/Baidu_Mtj_3.7.2.0.jar
#-libraryjars libs/glide-3.7.0.jar
#-libraryjars libs/glide-okhttp3-integration-1.4.0.jar
#-libraryjars libs/glide-okhttp-integration-1.4.0.jar
#-libraryjars libs/jd_crash_lib_release_210.jar
#-libraryjars libs/JDSDK_h.jar
#-libraryjars libs/mail.jar
#-libraryjars libs/mhvp-core.jar
#-libraryjars libs/Msc.jar
#-libraryjars libs/open_sdk.jar
#-libraryjars libs/nineoldandroids.jar
#-libraryjars libs/weibosdk.jar
#-libraryjars libs/weiboSDKCore_3.1.2.jar
#-libraryjars libs/wup-1.0.0.E-SNAPSHOT.jar
#-libraryjars libs/Xg_sdk_v3.0_20170301_1733.jar

#保持第三方库，有的需要-dontwarn,Android Private Librarys里面的全部keep,keep的时候需要注意class,有的还需要dontwarn。
#---------------------------------第三方依赖库-------------------------------
# 信鸽
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep class com.tencent.android.tpush.** {* ;}
-keep class com.tencent.mid.** {* ;}
-keep class com.qq.taf.jce.** {*;}

-keep class tencent.**{*;}
-dontwarn tencent.**

-keep class qalsdk.**{*;}
-dontwarn qalsdk.**


-keep class android.** { *; }
-keep class com.umeng.** { *; }
-keep class com.tencent.** { *; }
-dontwarn com.tencent.**
-keep class com.baidu.** { *; }
-dontwarn  com.baidu.**
-keep class com.hyphenate.** {*;}
-dontwarn  com.hyphenate.**

-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
-keep class android.support.**{*;}

-keepattributes Signature
    -keep class sun.misc.Unsafe { *; }
    -keep class com.taobao.** {*;}
    -keep class com.alibaba.** {*;}
    -keep class com.alipay.** {*;}
    -dontwarn com.taobao.**
    -dontwarn com.alibaba.**
    -dontwarn com.alipay.**
    -keep class com.ut.** {*;}
    -dontwarn com.ut.**
    -keep class com.ta.** {*;}
    -dontwarn com.ta.**
    -keep class org.json.** {*;}
    -keep class com.ali.auth.**  {*;}
    -dontwarn com.huawei.android.**
    -keep class com.huawei.android.** { *; }


    -dontwarn org.apache.harmony.**
    -keep class org.apache.harmony.** { *; }

    -dontwarn com.alibaba.baichuan.**
    -keep class com.alibaba.baichuan.** { *; }

    -dontwarn com.culiu.mhvp.**
    -keep class com.culiu.mhvp.** { *; }

    -dontwarn com.sina.weibo.**
    -keep class com.sina.weibo.** { *; }

    -dontwarn com.sun.mail.**
    -keep class com.sun.mail.** { *; }

    -dontwarn com.tencent.android.**
    -keep class com.tencent.android.** { *; }

    -dontwarn com.tencent.connect.**
    -keep class com.tencent.connect.** { *; }

    -dontwarn com.tencent.cos.**
    -keep class com.tencent.cos.** { *; }

    -dontwarn com.xiaomi.push.**
    -keep class com.xiaomi.push.** { *; }

    -dontwarn javax.activation.**
    -keep class javax.activation.** { *; }

    -dontwarn okio.**
    -keep class okio.** { *; }

    -dontwarn  org.apache.harmony.**
    -keep class org.apache.harmony.** { *; }


    -dontwarn retrofit2.**
    -keep class retrofit2.** { *; }
#-keep class com.iflytek.**{*;}

# ButterKnife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#Glide
-keep public class * implements  com.bumptech.glide.module.GlideModule

-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {

    **[] $VALUES;

    public *;

}
-keep class com.bumptech.** { *;}

# 微信支付
-dontwarn com.tencent.mm.**
-dontwarn com.tencent.wxop.stat.**
-keep class com.tencent.mm.** {*;}
-keep class com.tencent.wxop.stat.**{*;}
# 友盟统计分析
-keepclassmembers class * { public <init>(org.json.JSONObject); }
-keepclassmembers enum com.umeng.analytics.** {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 友盟自动更新
-keepclassmembers class * { public <init>(org.json.JSONObject); }
-keep public class cn.irains.parking.cloud.pub.R$*{ public static final int *; }
-keep public class * extends com.umeng.**
-keep class com.umeng.** { *; }

# 信鸽
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep class com.tencent.android.tpush.**  {* ;}
-keep class com.tencent.mid.**  {* ;}
-keepattributes *Annotation*

# 新浪微博
-keep class com.sina.weibo.sdk.* { *; }
-keep class android.support.v4.* { *; }
-keep class com.tencent.* { *; }
-keep class com.baidu.* { *; }
-keep class lombok.ast.ecj.* { *; }
-dontwarn android.support.v4.**
-dontwarn com.tencent.**s
-dontwarn com.baidu.**


#bug模式下都是可以的，打包需要加这一句
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
#当EventBus注解的函数找不到时，可以加以下
# EventBus 3.0
-keepclassmembers class ** {
    public void onEvent*(**);
}
# EventBus 3.0 annotation
-keepclassmembers class * {
    @de.greenrobot.event.Subscribe <methods>;
}


# 京东开普勒
-keep class com.kepler.**{*;}
-dontwarn com.kepler.**
-keep class com.jingdong.jdma.**{*;}
-dontwarn com.jingdong.jdma.**
-keep class com.jingdong.crash.**{*;}
-dontwarn com.jingdong.crash.**

#-keep public class * implements com.bumptech.glide.module.GlideModule

# 友盟消息推送
-dontwarn com.umeng.**
-dontwarn com.taobao.**
-dontwarn anet.channel.**
-dontwarn anetwork.channel.**
-dontwarn org.android.**
-dontwarn org.apache.thrift.**
-dontwarn com.xiaomi.**
-dontwarn com.huawei.**
-dontwarn com.meizu.**
-keepattributes *Annotation*
-keep class com.taobao.** {*;}
-keep class org.android.** {*;}
-keep class anet.channel.** {*;}
-keep class com.umeng.** {*;}
-keep class com.xiaomi.** {*;}
-keep class com.huawei.** {*;}
-keep class com.meizu.** {*;}
-keep class org.apache.thrift.** {*;}
-keep class com.alibaba.sdk.android.**{*;}
-keep class com.ut.**{*;}
-keep class com.ta.**{*;}
-keep class com.ta.**{*;}
-keep public class com.bbk.activity.R$*{
   public static final int *;
}


#---------------------------------实体类---------------------------------
-keep class com.bbk.Bean.** { *; }
-keep class com.bbk.shopcar.entity.** { *; }


