package com.bbk.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeInitCallback;
import com.appkefu.lib.interfaces.KFAPIs;
import com.bbk.chat.utils.Foreground;
import com.bbk.util.CrashHandler;
import com.bbk.util.EventIdIntentUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
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
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;


public class MyApplication extends Application {
	/**
	 * 应用启动后是否是第一次打开排行榜单页面
	 */
	private boolean firstStartActivityRank = true;
	private static MyApplication app;
	private static Context context;
	private static final String TAG = MyApplication.class.getName();
	public static final String UPDATE_STATUS_ACTION = "com.umeng.message.example.action.UPDATE_STATUS";
	private Handler handler;

	@Override
	public void onCreate() {
		super.onCreate();
		app = this;
		//默认关闭调试模式
		KFAPIs.DEBUG = false;
		//第一个参数默认设置为false, 即登录普通服务器, 如果设置为true, 则登录IP服务器,
		//注意: 当第一个参数设置为true的时候, 客服端需要选择登录ip服务器 才能够会话
		//正常情况下第一个参数请设置为false
		KFAPIs.enableIPServerMode(false, this);
		//第一种登录方式，推荐
		KFAPIs.visitorLogin(this);

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
				return new ClassicsFooter(context).setDrawableSize(15).setTextSizeTitle(15);
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
//        UMConfigure.setLogEnabled(true);
        //初始化组件化基础库, 统计SDK/推送SDK/分享SDK都必须调用此初始化接口
        UMConfigure.init(this, "59db08fcf29d985a31000021","bbj", UMConfigure.DEVICE_TYPE_PHONE, "2748cf7fcfa2f2cbb4c72943e9af435b");
        initUpush();
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

	private void initUpush() {
		final PushAgent mPushAgent = PushAgent.getInstance(this);
		handler = new Handler(getMainLooper());

		//sdk开启通知声音
		mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
		// sdk关闭通知声音
		// mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
		// 通知声音由服务端控制
		// mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SERVER);

		// mPushAgent.setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
		// mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);

		UmengMessageHandler messageHandler = new UmengMessageHandler() {

			/**
			 * 通知的回调方法（通知送达时会回调）
			 */
			@Override
			public void dealWithNotificationMessage(Context context, UMessage msg) {
				//调用super，会展示通知，不调用super，则不展示通知。
				super.dealWithNotificationMessage(context, msg);
			}

			/**
			 * 自定义消息的回调方法
			 */
			@Override
			public void dealWithCustomMessage(final Context context, final UMessage msg) {

				handler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						// 对自定义消息的处理方式，点击或者忽略
						boolean isClickOrDismissed = true;
						if (isClickOrDismissed) {
							//自定义消息的点击统计
							UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
						} else {
							//自定义消息的忽略统计
							UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
						}
						Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
					}
				});
			}

			/**
			 * 自定义通知栏样式的回调方法
			 */
			@Override
			public Notification getNotification(Context context, UMessage msg) {
				switch (msg.builder_id) {
					case 1:
//						Notification.Builder builder = new Notification.Builder(context);
//						RemoteViews myNotificationView = new RemoteViews(context.getPackageName(),
//								R.layout.notification_view);
//						myNotificationView.setTextViewText(R.id.notification_title, msg.title);
//						myNotificationView.setTextViewText(R.id.notification_text, msg.text);
//						myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
//						myNotificationView.setImageViewResource(R.id.notification_small_icon,
//								getSmallIconId(context, msg));
//						builder.setContent(myNotificationView)
//								.setSmallIcon(getSmallIconId(context, msg))
//								.setTicker(msg.ticker)
//								.setAutoCancel(true);
//
//						return builder.getNotification();
					default:
						//默认为0，若填写的builder_id并不存在，也使用默认。
						return super.getNotification(context, msg);
				}
			}
		};
		mPushAgent.setMessageHandler(messageHandler);
		/**
		 * 自定义行为的回调处理，参考文档：高级功能-通知的展示及提醒-自定义通知打开动作
		 * UmengNotificationClickHandler是在BroadcastReceiver中被调用，故
		 * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
		 * */
		UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {

			@Override
			public void launchApp(Context context, UMessage msg) {
				super.launchApp(context, msg);
			}

			@Override
			public void openUrl(Context context, UMessage msg) {
				super.openUrl(context, msg);
			}

			@Override
			public void openActivity(Context context, UMessage msg) {
				super.openActivity(context, msg);
			}

			@Override
			public void dealWithCustomAction(Context context, UMessage msg) {
//				Logg.e("APP" + msg.custom);
//				Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
//				StringUtil.showToast(context,"点击了"+msg.custom);
				SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "custom", "custom", msg.custom);
				if (isBackground(getApplicationContext())) {
					Logg.e("APP在后台"+isBackground(getApplicationContext()));
					Intent intent = new Intent(context, WelcomeActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("customContent", msg.custom);
					context.startActivity(intent);
				}else {
					Logg.e("APP在前台"+isBackground(getApplicationContext()));
					SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "custom", "custom", "");
					Intent intent = new Intent(context, EventIdIntentActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("customContent", msg.custom);
					context.startActivity(intent);
				}
			}
		};
		//使用自定义的NotificationHandler
		mPushAgent.setNotificationClickHandler(notificationClickHandler);
		mPushAgent.setDisplayNotificationNumber(0);
		final String userId= SharedPreferencesUtil.getSharedData(context, "userInfor", "userID");
		//注册推送服务 每次调用register都会回调该接口
		mPushAgent.register(new IUmengRegisterCallback() {
			@Override
			public void onSuccess(String deviceToken) {
				Log.i(TAG, "device token: " + deviceToken);
				mPushAgent.addAlias(userId, "BBJ", new UTrack.ICallBack() {
					@Override
					public void onMessage(boolean isSuccess, String message) {
						Logg.e("设置别名成功"+userId);
					}
				});
				sendBroadcast(new Intent(UPDATE_STATUS_ACTION));
			}

			@Override
			public void onFailure(String s, String s1) {
				Log.i(TAG, "register failed: " + s + " " + s1);
				sendBroadcast(new Intent(UPDATE_STATUS_ACTION));
			}
		});

		//使用完全自定义处理
		//mPushAgent.setPushIntentServiceClass(UmengNotificationService.class);

		//小米通道
		//MiPushRegistar.register(this, XIAOMI_ID, XIAOMI_KEY);
		//华为通道
		//HuaWeiRegister.register(this);
		//魅族通道
		//MeizuRegister.register(this, MEIZU_APPID, MEIZU_APPKEY);
	}


	public static boolean isBackground(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
                /*
                BACKGROUND=400 EMPTY=500 FOREGROUND=100
                GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
                 */
				Log.i(context.getPackageName(), "此appimportace ="
						+ appProcess.importance
						+ ",context.getClass().getName()="
						+ context.getClass().getName());
				if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
					Log.i(context.getPackageName(), "处于后台"
							+ appProcess.processName);
					return true;
				} else {
					Log.i(context.getPackageName(), "处于前台"
							+ appProcess.processName);
					return false;
				}
			}
		}
		return false;
	}
}
