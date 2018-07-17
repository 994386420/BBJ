package com.bbk.activity;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeInitCallback;
import com.bbk.chat.utils.Foreground;
import com.bbk.util.CrashHandler;
import com.kepler.jd.Listener.AsyncInitListener;
import com.kepler.jd.login.KeplerApiManager;
import com.logg.Logg;
import com.logg.config.LoggConfiguration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.imsdk.TIMGroupReceiveMessageOpt;
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMOfflinePushListener;
import com.tencent.imsdk.TIMOfflinePushNotification;
import com.tencent.imsdk.TIMSdkConfig;
import com.tencent.qalsdk.sdk.MsfSdkUtils;
import com.tencent.qcloud.sdk.Constant;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.commonsdk.UMConfigure;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;


public class MyApplication extends Application {
	/**
	 * 应用启动后是否是第一次打开排行榜单页面
	 */
	private boolean firstStartActivityRank = true;
	private static MyApplication app;
	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		app = this;

		//解决android N（>=24）系统以上分享 路径为file://时的 android.os.FileUriExposedException异常
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
			StrictMode.setVmPolicy(builder.build());
		}
		/**
		 * 打印注册
		 */
		LoggConfiguration configuration = new LoggConfiguration.Buidler()
				.setDebug(true)
//                .setTag("test")// 自定义全局Tag
				.build();
		Logg.init(configuration);

		initTXYun();
		initX5();
		Foreground.init(this);
		context = getApplicationContext();
		//设置全局的Header构建器
		SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
			@Override
			public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
				layout.setPrimaryColorsId(R.color.white, android.R.color.black);//全局设置主题颜色
				return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
			}
		});
		//设置全局的Footer构建器
		SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
			@Override
			public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
				//指定为经典Footer，默认是 BallPulseFooter
				return new ClassicsFooter(context).setDrawableSize(20);
			}
		});

		KeplerApiManager.asyncInitSdk(this, "581b75d36bd0443cb50b68ae316c7e93", "6938c582f809437dacd59c286c3191a6",
				new AsyncInitListener() {
					@Override
					public void onSuccess() {
                        // TODO Auto-generated method stub
						Log.e("Kepler", "Kepler asyncInitSdk onSuccess ");
					}
					@Override
					public void onFailure() {
                        // TODO Auto-generated method stub
						Log.e("Kepler",
								"Kepler asyncInitSdk 授权失败，请检查lib 工程资源引用；包名,签名证书是否和注册一致");

					}
				});
        /**
         * 友盟统计初始化
         */
        //设置LOG开关，默认为false
        UMConfigure.setLogEnabled(true);
        //初始化组件化基础库, 统计SDK/推送SDK/分享SDK都必须调用此初始化接口
        UMConfigure.init(this, "5ac45e89f43e4844fc000370","bbj", UMConfigure.DEVICE_TYPE_PHONE, null);

		if(MsfSdkUtils.isMainProcess(this)) {
			TIMManager.getInstance().setOfflinePushListener(new TIMOfflinePushListener() {
				@Override
				public void handleNotification(TIMOfflinePushNotification notification) {
					if (notification.getGroupReceiveMsgOpt() == TIMGroupReceiveMessageOpt.ReceiveAndNotify){
						//消息被设置为需要提醒
						notification.doNotify(getApplicationContext(), R.mipmap.logo);
					}
				}
			});
		}

		//解决 Android 7.0 后
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
			StrictMode.setVmPolicy(builder.build());
		}
//		getSignMd5Str();
		AlibcTradeSDK.asyncInit(this, new AlibcTradeInitCallback() {
			@Override
			public void onSuccess() {
				//初始化成功，设置相关的全局配置参数
				// ...
			}

			@Override
			public void onFailure(int code, String msg) {
				//初始化失败，可以根据code和msg判断失败原因，详情参见错误说明
			}
		});
		CrashHandler.getInstance().init(getApplicationContext());
	}
	public Typeface getFontFace(){
		return Typeface.createFromAsset (getAssets() , "fonts/PingFang Regular.ttf");
	}
	public boolean isFirstStartActivityRank() {
		return firstStartActivityRank;
	}
	public void setFirstStartActivityRank(boolean firstStartActivityRank) {
		this.firstStartActivityRank = firstStartActivityRank;
	}
	public static MyApplication getApplication(){
		return app;
	}
	public static Context getContext(){
		return app.getApplicationContext();
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
	/**
	 * 初始化X5
	 */
	private void initX5() {
		QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
			@Override
			public void onCoreInitFinished() {

			}

			@Override
			public void onViewInitFinished(boolean b) {

			}
		});
	}
	public void initTXYun(){
		//初始化SDK基本配置
		TIMSdkConfig config = new TIMSdkConfig(Constant.SDK_APPID)
				.enableCrashReport(false)
        		.enableLogPrint(true)
				.setLogLevel(TIMLogLevel.DEBUG)
				.setLogPath(Environment.getExternalStorageDirectory().getPath() + "/justfortest/");

		//初始化SDK
		TIMManager.getInstance().init(getApplicationContext(), config);
	}

	/**
	 * MD5加密
	 * @param byteStr 需要加密的内容
	 * @return 返回 byteStr的md5值
	 */
	public static String encryptionMD5(byte[] byteStr) {
		MessageDigest messageDigest = null;
		StringBuffer md5StrBuff = new StringBuffer();
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(byteStr);
			byte[] byteArray = messageDigest.digest();
//            return Base64.encodeToString(byteArray,Base64.NO_WRAP);
			for (int i = 0; i < byteArray.length; i++) {
				if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
					md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
				} else {
					md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
				}
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return md5StrBuff.toString();
	}

	/**
	 * 获取app签名md5值,与“keytool -list -keystore D:\Desktop\app_key”‘keytool -printcert     *file D:\Desktop\CERT.RSA’获取的md5值一样
	 */
	public String getSignMd5Str() {
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(
					getPackageName(), PackageManager.GET_SIGNATURES);
			Signature[] signs = packageInfo.signatures;
			Signature sign = signs[0];
			String signStr = encryptionMD5(sign.toByteArray());
			Log.i("=======",signStr);
			sHA1(context);
			return signStr;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String sHA1(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), PackageManager.GET_SIGNATURES);
			byte[] cert = info.signatures[0].toByteArray();
			MessageDigest md = MessageDigest.getInstance("SHA1");
			byte[] publicKey = md.digest(cert);
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < publicKey.length; i++) {
				String appendString = Integer.toHexString(0xFF & publicKey[i])
						.toUpperCase(Locale.US);
				if (appendString.length() == 1)
					hexString.append("0");
				hexString.append(appendString);
				hexString.append(":");
			}
			String result = hexString.toString();
			return result.substring(0, result.length()-1);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
