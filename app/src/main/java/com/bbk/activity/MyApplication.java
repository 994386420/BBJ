package com.bbk.activity;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeInitCallback;
import com.bbk.chat.utils.Foreground;
import com.bbk.util.CrashHandler;
import com.tencent.imsdk.TIMGroupReceiveMessageOpt;
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMOfflinePushListener;
import com.tencent.imsdk.TIMOfflinePushNotification;
import com.tencent.imsdk.TIMSdkConfig;
import com.tencent.qalsdk.sdk.MsfSdkUtils;
import com.tencent.qcloud.sdk.Constant;
import com.umeng.commonsdk.UMConfigure;


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
		initTXYun();
		Foreground.init(this);
		context = getApplicationContext();
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
}
