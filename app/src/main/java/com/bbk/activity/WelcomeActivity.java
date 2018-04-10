package com.bbk.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bbk.entity.XGMessageEntity;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.fragment.GossipPiazzaFragment;
import com.bbk.resource.Constants;
import com.bbk.server.FloatingWindowService;
import com.bbk.server.GrayService;
import com.bbk.util.HttpUtil;
import com.bbk.util.NetworkUtils;
import com.bbk.util.SchemeIntentUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;


public class WelcomeActivity extends BaseActivity2 implements ResultEvent{
	private XGMessageEntity xgMessage;
	private TextView mbtn;
	private final static String ALBUM_PATH
			= Environment.getExternalStorageDirectory() + "/download_image/";
	private ImageView mimg;
	private DataFlow dataFlow;
	protected String url = Constants.MAIN_BASE_URL_MOBILE+ "apiService/queryAppGuanggao" ;
	@SuppressLint("StaticFieldLeak")
	public static Activity instance = null;
	protected Map<String, String> map = new HashMap<>();
	//倒计时
	@SuppressLint("HandlerLeak")
	private Handler handler2 = new Handler(){
		public void handleMessage(Message msg) {
			if (WelcomeActivity.this.isFinishing() || null == this) {
				return;
			}
			int curTime = msg.what;
			mbtn.setText("跳过"+curTime+"秒");
			curTime--;

			if (curTime!=-1) {
				handler2.sendEmptyMessageDelayed(curTime, 1000);
			}else{
				Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
				if(xgMessage != null){
					intent.putExtra("xgMessage", new Gson().toJson(xgMessage).toString());
				}
				startActivity(intent);
				finish();
			}
		};
	};
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case 0:
					String dataStr = (String) msg.obj;
					try {
						final JSONObject data = new JSONObject(dataStr);
						final JSONObject content = data.getJSONObject("content");
						String img = content.optString("img");
						Glide.with(WelcomeActivity.this)
								.load(img)
								.placeholder(R.mipmap.qidong)
								.into(mimg);
						final String eventId = content.optString("eventId");
						//点击跳转活动页
						mimg.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
								intent.putExtra("content", content.toString());
								startActivity(intent);
								finish();
							}
						});
					} catch (Exception e) {
						e.printStackTrace();
					}


					break;

				default:
					break;
			}
		};
	};
	private int time = 3;
	private Object htmlUrl;
	// Storage Permissions
	private static final int REQUEST_EXTERNAL_STORAGE = 1;
	private static String[] PERMISSIONS_STORAGE = {
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE ,
			Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS};
	private Thread thread;

	/**
	 * Checks if the app has permission to write to device storage
	 * If the app does not has permission then the user will be prompted to
	 * grant permissions
	 * @param activity
	 */
	public static void verifyStoragePermissions(Activity activity) {
// Check if we have write permission
		int permission = ActivityCompat.checkSelfPermission(activity,
				Manifest.permission.WRITE_EXTERNAL_STORAGE);

		if (permission != PackageManager.PERMISSION_GRANTED) {
// We don't have permission so prompt the user
			ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
					REQUEST_EXTERNAL_STORAGE);
		}
		if (permission != PackageManager.PERMISSION_GRANTED) {
// We don't have permission so prompt the user
			ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
					REQUEST_EXTERNAL_STORAGE);
		}
	}
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		/*
		 * Bundle bundle = getIntent().getExtras(); if (bundle != null) {
		 * Set<String> keys = bundle.keySet(); for (String key : keys) {
		 * Log.e(key, bundle.get(key).toString()); } }
		 */
		super.onCreate(savedInstanceState);
		instance = this;
		//友盟统计
//		MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
		verifyStoragePermissions(this);
		startHome();
		/**全屏设置，隐藏窗口所有装饰**/
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		/**标题是属于View的，所以窗口所有的修饰部分被隐藏后标题依然有效,需要去掉标题**/
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);
		TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		String token;
		if (TelephonyMgr.getDeviceId() != null) {
			token = TelephonyMgr.getDeviceId();
		} else {
			//android.provider.Settings;
			token = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
		}
//		 = TelephonyMgr.getDeviceId();
		//启动后台服务
		Intent service=new Intent(this, FloatingWindowService.class);
		startService(service);
		dataFlow = new DataFlow(this);
		SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "userInfor", "token", token);
		init();


		// 开启logcat输出，方便debug，发布时请关闭
		// XGPushConfig.enableDebug(this, true);
		// 如果需要知道注册是否成功，请使用registerPush(getApplicationContext(),
		// XGIOperateCallback)带callback版本
		// 如果需要绑定账号，请使用registerPush(getApplicationContext(),account)版本
		// 具体可参考详细的开发指南
		// 传递的参数为ApplicationContext

		Context context = getApplicationContext();
		XGPushConfig.setAccessId(context, 2100196420);
		XGPushConfig.setAccessKey(context, "AUTV25N58F3Z");
		String userId=SharedPreferencesUtil.getSharedData(context, "userInfor", "userID");

		XGPushManager.registerPush(context, userId, new XGIOperateCallback() {

			@Override
			public void onSuccess(Object data, int arg1) {
				Log.e("TPush", "注册成功，设备token为：" + data);
			}

			@Override
			public void onFail(Object data, int errCode, String msg) {
				Log.e("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
			}
		});
		XGPushManager.registerPush(this, new XGIOperateCallback() {
			@Override
			public void onSuccess(Object data, int flag) {
				Log.e("TPush", "注册成功，设备token为：" + data);
			}
			@Override
			public void onFail(Object data, int errCode, String msg) {
				Log.e("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
			}
		});
//		if (TextUtils.isEmpty(userId)) {
//			XGPushManager.registerPush(context, userId,new XGIOperateCallback() {
//				@Override
//				public void onSuccess(Object data, int flag) {
//					Log.d("TPush", "注册成功，设备token为：" + data);
//					SharedPreferencesUtil.putSharedData(WelcomeActivity.this,
//							"deviceToken", "token", data.toString());
//				}
//				@Override
//				public void onFail(Object data, int errCode, String msg) {
//					Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
//				}
//			});
//		}
//			Log.e("====123=====","123");
		// 2.36（不包括）之前的版本需要调用以下2行代码
//		Intent service = new Intent(context, XGPushService.class);
//		context.startService(service);

		// 其它常用的API：
		// 绑定账号（别名）注册：registerPush(context,account)或registerPush(context,account,
		// XGIOperateCallback)，其中account为APP账号，可以为任意字符串（qq、openid或任意第三方），业务方一定要注意终端与后台保持一致。
		// 取消绑定账号（别名）：registerPush(context,"*")，即account="*"为取消绑定，解绑后，该针对该账号的推送将失效
		// 反注册（不再接收消息）：unregisterPush(context)
		// 设置标签：setTag(context, tagName)
		// 删除标签：deleteTag(context, tagName)

		// MyApplication.getInstance().addActivity(this);
		//外部应用打开APP传递参数
		Intent intent = getIntent();
		String scheme = intent.getScheme();
		Uri uri = intent.getData();
		if (uri!= null){
			SchemeIntentUtil.intent(uri,this);
			finish();
		}
		handler2.sendEmptyMessage(time);
		ClipboardManager cm = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
		if (cm.getText()!= null) {
			String text = cm.getText().toString();
			String clip = SharedPreferencesUtil.getSharedData(context, "clipchange", "cm");
			if (!clip.equals(text)) {
				if (text.startsWith("http:") || text.startsWith("https:")) {
					if (text.contains("product") || text.contains("Product") || text.contains("detail") || text.contains("item")
							|| text.contains("gp/aw") || text.contains("style/index") || text.contains("sku-") || text.contains("goods")
							|| text.contains("Detail") || text.contains("proDetail") || text.contains("Goods")) {
						SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "clipchange", "cm", text);
						Map<String, String> paramsMap = new HashMap<String, String>();
						paramsMap.put("url", text);
						dataFlow.requestData(1, "newService/checkExsistProduct", paramsMap, WelcomeActivity.this,false);
					}
				}
			}
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		for (int grantResult : grantResults) {
			if (grantResult == PackageManager.PERMISSION_DENIED) {
				finish();
			}
		}
	}

	private void init() {
		mbtn=(TextView) findViewById(R.id.mbtn);
		mbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
				if(xgMessage != null){
					intent.putExtra("xgMessage", new Gson().toJson(xgMessage).toString());
				}
				startActivity(intent);
				finish();
			}
		});
		mimg=(ImageView) findViewById(R.id.mimg);
		//判断有没有网络
		ConnectivityManager cm = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (null == networkInfo) {
			mimg.setBackgroundResource(R.mipmap.qidong);
		}else{
			thread = new Thread(new Runnable() {

				@Override
				public void run() {
					Looper.prepare();
					String dataStr = HttpUtil.getHttp(map, url, WelcomeActivity.this);
//					Log.e("====进入线程了=======", ""+dataStr);
					Message mes = handler.obtainMessage();
					mes.obj = dataStr;
					mes.what =0;
					handler.sendMessage(mes);
					Looper.loop();
				}
			});
			thread.start();
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		XGPushClickedResult click = XGPushManager.onActivityStarted(this);
		Log.d("TPush", "onResumeXGPushClickedResult:" + click);
		if (click != null) { // 判断是否来自信鸽的打开方式
			String customContent = click.getCustomContent();
			xgMessage = new XGMessageEntity(click.getTitle(),click.getContent());
			if (customContent != null && customContent.length() != 0) {
				try {
					JSONObject obj = new JSONObject(customContent);
					xgMessage.setStartType(obj.getString("startType"));
					xgMessage.setUrl(obj.getString("url"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}else{
			xgMessage = null;
		}
	}
	@Override
	protected void onInit() {
		super.onInit();
		new Thread(){
			@Override
			public void run() {
				loadData();
			};
		}.start();
		startHome();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		XGPushManager.onActivityStoped(this);
	}

	public static String getPicNameFromPath(String picturePath){
		String temp[] = picturePath.replaceAll("\\\\","/").split("/");
		String fileName = "";
		if(temp.length > 1){
			fileName = temp[temp.length - 1];
		}
		return fileName;
	}

	//是否跳转引导页
	public void startHome() {
		String isFirstUse = SharedPreferencesUtil.getSharedData(this,"isFirstUse", "isFirstUse");
		if (TextUtils.isEmpty(isFirstUse)) {
			isFirstUse = "yes";
		}
		if (isFirstUse.equals("yes")) {
			SharedPreferencesUtil.putSharedData(this, "isFirstUse","isFirstUse", "no");
			startActivity(new Intent(this, WelcomeGuideActivity.class));
			finish();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (thread!= null){
			handler.removeCallbacks(thread);
		}


	}
	/**
	 * 初始化分类数据和热度搜索词
	 */
	private void loadData() {
		TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		String token = TelephonyMgr.getDeviceId();
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", token);
		String hometuijian = HttpUtil.getHttp(params,
				Constants.MAIN_BASE_URL_MOBILE
						+ "newService/queryIndexTuijianByToken", null);
		if (hometuijian != null && !"".equals(hometuijian)) {
			SharedPreferencesUtil.putSharedData(
					getApplicationContext(), "homedata",
					"hometuijian", hometuijian);
		}
		Map<String, String> param = new HashMap<String, String>();
		param.put("token", token);
		param.put("page", "1");
		String seelike = HttpUtil.getHttp(param,
				Constants.MAIN_BASE_URL_MOBILE
						+ "newService/queryIndexSeeByToken", null);
		if (seelike != null && !"".equals(seelike)) {
			SharedPreferencesUtil.putSharedData(
					getApplicationContext(), "homedata",
					"seelike", seelike);
		}
		String hotKeyword = SharedPreferencesUtil.getSharedData(getApplicationContext(), "hotKeyword", "hotKeyword");
//		String categoryType = SharedPreferencesUtil.getSharedData(getApplicationContext(), "categoryType", "categoryType");
//		String pageInit = SharedPreferencesUtil.getSharedData(getApplicationContext(), "pageInit", "pageInit");
		if (hotKeyword == null || "".equals(hotKeyword)) {
			hotKeyword = "[{\"name\":\"iphone se\",\"productType\":\"\"},{\"name\":\"衬衫\",\"productType\":\"240201\"},{\"name\":\"电炖锅\",\"productType\":\"030313\"},{\"name\":\"情侣睡衣\",\"productType\":\"240313\"},{\"name\":\"空调\",\"productType\":\"030102\"},{\"name\":\"风扇\",\"productType\":\"030201\"},{\"name\":\"T恤\",\"productType\":\"160209\"},{\"name\":\"奶粉\",\"productType\":\"1701\"}]";
			SharedPreferencesUtil.putSharedData(getApplicationContext(),
					"hotKeyword", "hotKeyword", hotKeyword);
		} else {
			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("number", "10");
			String hKeyword = HttpUtil.getHttp(paramsMap,
					Constants.MAIN_BASE_URL_MOBILE
							+ "apiService/getSearchHotWord", null);
			if (hKeyword != null && !"".equals(hKeyword)) {
				try {
					JSONObject jo = new JSONObject(hKeyword);
					hKeyword = jo.getString("content");
					SharedPreferencesUtil.putSharedData(
							getApplicationContext(), "hotKeyword",
							"hotKeyword", hKeyword);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
//		if (categoryType == null || "".equals(categoryType)) {
//			categoryType = "[{\"name\":\"服饰\",\"chid\":[{\"hasdata\":0,\"name\":\"女装\","
//					+ "\"number\":\"2401\"},{\"hasdata\":0,\"name\":\"男装\",\"number\":\"2402\"},"
//					+ "{\"hasdata\":0,\"name\":\"内衣\",\"number\":\"2403\"},{\"hasdata\":0,\"name\":\"服饰配件\",\"number\":\"2404\"}]},{\"name\":\"鞋靴\\/箱包\",\"chid\":[{\"hasdata\":0,\"name\":\"流行男鞋\",\"number\":\"2001\"},{\"hasdata\":0,\"name\":\"时尚女鞋\",\"number\":\"2002\"},{\"hasdata\":0,\"name\":\"潮流女包\",\"number\":\"2101\"},{\"hasdata\":0,\"name\":\"精品男包\",\"number\":\"2102\"},{\"hasdata\":0,\"name\":\"功能箱包\",\"number\":\"2103\"},{\"hasdata\":0,\"name\":\"礼品\",\"number\":\"2104\"},{\"hasdata\":0,\"name\":\"奢侈品\",\"number\":\"2105\"},{\"hasdata\":0,\"name\":\"婚庆\",\"number\":\"2106\"}]},{\"name\":\"美妆\\/个护\",\"chid\":[{\"hasdata\":0,\"name\":\"香水彩妆\",\"number\":\"0501\"},{\"hasdata\":0,\"name\":\"洗发护发\",\"number\":\"0502\"},{\"hasdata\":0,\"name\":\"面部护肤\",\"number\":\"0503\"},{\"hasdata\":0,\"name\":\"身体护肤\",\"number\":\"0504\"},{\"hasdata\":0,\"name\":\"口腔护理\",\"number\":\"1005\"},{\"hasdata\":0,\"name\":\"女性护理\",\"number\":\"1006\"}]},{\"name\":\"母婴\",\"chid\":[{\"hasdata\":0,\"name\":\"奶粉\",\"number\":\"1701\"},{\"hasdata\":0,\"name\":\"营养辅食\",\"number\":\"1702\"},{\"hasdata\":0,\"name\":\"尿裤湿巾\",\"number\":\"1703\"},{\"hasdata\":0,\"name\":\"喂养用品\",\"number\":\"1704\"},{\"hasdata\":0,\"name\":\"洗护用品\",\"number\":\"1705\"},{\"hasdata\":0,\"name\":\"童车童床\",\"number\":\"1706\"},{\"hasdata\":0,\"name\":\"寝居服饰\",\"number\":\"1707\"},{\"hasdata\":0,\"name\":\"妈妈专区\",\"number\":\"1708\"},{\"hasdata\":0,\"name\":\"童装童鞋\",\"number\":\"1709\"},{\"hasdata\":0,\"name\":\"安全座椅\",\"number\":\"1710\"}]},{\"name\":\"玩具\",\"chid\":[{\"hasdata\":0,\"name\":\"适用年龄\",\"number\":\"1901\"},{\"hasdata\":0,\"name\":\"遥控\\/电动\",\"number\":\"1902\"},{\"hasdata\":0,\"name\":\"毛绒布艺\",\"number\":\"1903\"},{\"hasdata\":0,\"name\":\"娃娃玩具\",\"number\":\"1904\"},{\"hasdata\":0,\"name\":\"模型玩具\",\"number\":\"1905\"},{\"hasdata\":0,\"name\":\"健身玩具\",\"number\":\"1906\"},{\"hasdata\":0,\"name\":\"动漫玩具\",\"number\":\"1907\"},{\"hasdata\":0,\"name\":\"益智玩具\",\"number\":\"1908\"},{\"hasdata\":0,\"name\":\"积木拼插\",\"number\":\"1909\"},{\"hasdata\":0,\"name\":\"DIY玩具\",\"number\":\"1910\"},{\"hasdata\":0,\"name\":\"创意减压\",\"number\":\"1911\"},{\"hasdata\":0,\"name\":\"乐器相关\",\"number\":\"1912\"}]},{\"name\":\"手机\\/数码\",\"chid\":[{\"hasdata\":0,\"name\":\"手机配件\",\"number\":\"0101\"},{\"hasdata\":0,\"name\":\"手机通讯\",\"number\":\"0103\"},{\"hasdata\":0,\"name\":\"数码配件\",\"number\":\"0704\"},{\"hasdata\":0,\"name\":\"时尚影音\",\"number\":\"0705\"},{\"hasdata\":0,\"name\":\"智能设备\",\"number\":\"0706\"},{\"hasdata\":0,\"name\":\"电子教育\",\"number\":\"0707\"}]},{\"name\":\"电脑\\/办公\",\"chid\":[{\"hasdata\":0,\"name\":\"电脑整机\",\"number\":\"0201\"},{\"hasdata\":0,\"name\":\"电脑配件\",\"number\":\"0202\"},{\"hasdata\":0,\"name\":\"外设产品\",\"number\":\"0203\"},{\"hasdata\":0,\"name\":\"网络产品\",\"number\":\"0204\"},{\"hasdata\":0,\"name\":\"服务产品\",\"number\":\"0207\"},{\"hasdata\":0,\"name\":\"办公打印\",\"number\":\"0805\"},{\"hasdata\":0,\"name\":\"办公文仪\",\"number\":\"0806\"}]},{\"name\":\"家电\",\"chid\":[{\"hasdata\":0,\"name\":\"大家电\",\"number\":\"0301\"},{\"hasdata\":0,\"name\":\"生活电器\",\"number\":\"0302\"},{\"hasdata\":0,\"name\":\"厨房电器\",\"number\":\"0303\"},{\"hasdata\":0,\"name\":\"个护健康\",\"number\":\"0304\"},{\"hasdata\":0,\"name\":\"五金家装\",\"number\":\"0305\"}]},{\"name\":\"家具\\/厨具\",\"chid\":[{\"hasdata\":0,\"name\":\"卧室家具\",\"number\":\"1301\"},{\"hasdata\":0,\"name\":\"客厅家具\",\"number\":\"1302\"},{\"hasdata\":0,\"name\":\"餐厅家具\",\"number\":\"1303\"},{\"hasdata\":0,\"name\":\"书房家具\",\"number\":\"1304\"},{\"hasdata\":0,\"name\":\"储物家具\",\"number\":\"1305\"},{\"hasdata\":0,\"name\":\"阳台\\/户外\",\"number\":\"1306\"},{\"hasdata\":0,\"name\":\"商业办公\",\"number\":\"1307\"},{\"hasdata\":0,\"name\":\"儿童家具\",\"number\":\"1309\"},{\"hasdata\":0,\"name\":\"烹饪锅具\",\"number\":\"1501\"},{\"hasdata\":0,\"name\":\"刀剪菜板\",\"number\":\"1502\"},{\"hasdata\":0,\"name\":\"厨房配件\",\"number\":\"1503\"},{\"hasdata\":0,\"name\":\"水具酒具\",\"number\":\"1504\"},{\"hasdata\":0,\"name\":\"餐具\",\"number\":\"1505\"},{\"hasdata\":0,\"name\":\"茶具\\/咖啡具\",\"number\":\"1506\"}]},{\"name\":\"酒水\\/食品\",\"chid\":[{\"hasdata\":0,\"name\":\"酒类\",\"number\":\"0401\"},{\"hasdata\":0,\"name\":\"饮料\",\"number\":\"0409\"},{\"hasdata\":0,\"name\":\"茶叶\",\"number\":\"0902\"},{\"hasdata\":0,\"name\":\"进口食品\",\"number\":\"0903\"},{\"hasdata\":0,\"name\":\"休闲食品\",\"number\":\"0904\"},{\"hasdata\":0,\"name\":\"粮油调味\",\"number\":\"0905\"},{\"hasdata\":0,\"name\":\"饮料冲调\",\"number\":\"0906\"},{\"hasdata\":0,\"name\":\"地方特产\",\"number\":\"0907\"},{\"hasdata\":0,\"name\":\"生鲜食品\",\"number\":\"0908\"}]},{\"name\":\"钟表\\/珠宝\",\"chid\":[{\"hasdata\":0,\"name\":\"瑞士名表\",\"number\":\"2201\"},{\"hasdata\":0,\"name\":\"欧美大牌\",\"number\":\"2202\"},{\"hasdata\":0,\"name\":\"经典国产\",\"number\":\"2203\"},{\"hasdata\":0,\"name\":\"日韩港台\",\"number\":\"2204\"},{\"hasdata\":0,\"name\":\"时尚手表\",\"number\":\"2206\"},{\"hasdata\":0,\"name\":\"钟类\",\"number\":\"2207\"},{\"hasdata\":0,\"name\":\"时尚饰品\",\"number\":\"2501\"},{\"hasdata\":0,\"name\":\"纯金K金饰品\",\"number\":\"2502\"},{\"hasdata\":0,\"name\":\"金银投资\",\"number\":\"2503\"},{\"hasdata\":0,\"name\":\"银饰\",\"number\":\"2504\"},{\"hasdata\":0,\"name\":\"钻石\",\"number\":\"2505\"},{\"hasdata\":0,\"name\":\"翡翠玉石\",\"number\":\"2506\"},{\"hasdata\":0,\"name\":\"水晶玛瑙\",\"number\":\"2507\"},{\"hasdata\":0,\"name\":\"彩宝\",\"number\":\"2508\"},{\"hasdata\":0,\"name\":\"铂金\",\"number\":\"2509\"},{\"hasdata\":0,\"name\":\"天然木饰\",\"number\":\"2510\"},{\"hasdata\":0,\"name\":\"珍珠\",\"number\":\"2511\"}]},{\"name\":\"户外\",\"chid\":[{\"hasdata\":0,\"name\":\"运动鞋包\",\"number\":\"1601\"},{\"hasdata\":0,\"name\":\"运动服饰\",\"number\":\"1602\"},{\"hasdata\":0,\"name\":\"骑行运动\",\"number\":\"1603\"},{\"hasdata\":0,\"name\":\"垂钓用品\",\"number\":\"1604\"},{\"hasdata\":0,\"name\":\"游泳用品\",\"number\":\"1605\"},{\"hasdata\":0,\"name\":\"户外鞋服\",\"number\":\"1606\"},{\"hasdata\":0,\"name\":\"户外装备\",\"number\":\"1607\"},{\"hasdata\":0,\"name\":\"健身训练\",\"number\":\"1608\"},{\"hasdata\":0,\"name\":\"纤体瑜伽\",\"number\":\"1609\"},{\"hasdata\":0,\"name\":\"体育用品\",\"number\":\"1610\"}]},{\"name\":\"家装\\/建材\",\"chid\":[{\"hasdata\":0,\"name\":\"家纺\",\"number\":\"1201\"},{\"hasdata\":0,\"name\":\"灯具\",\"number\":\"1202\"},{\"hasdata\":0,\"name\":\"生活日用\",\"number\":\"1203\"},{\"hasdata\":0,\"name\":\"家装软饰\",\"number\":\"1204\"},{\"hasdata\":0,\"name\":\"清洁用品\",\"number\":\"1205\"},{\"hasdata\":0,\"name\":\"宠物生活\",\"number\":\"1206\"},{\"hasdata\":0,\"name\":\"灯饰照明\",\"number\":\"1401\"},{\"hasdata\":0,\"name\":\"厨房卫浴\",\"number\":\"1402\"},{\"hasdata\":0,\"name\":\"五金工具\",\"number\":\"1403\"},{\"hasdata\":0,\"name\":\"电工电料\",\"number\":\"1404\"},{\"hasdata\":0,\"name\":\"墙地面材料\",\"number\":\"1405\"},{\"hasdata\":0,\"name\":\"装饰材料\",\"number\":\"1406\"},{\"hasdata\":0,\"name\":\"装修服务\",\"number\":\"1407\"}]},{\"name\":\"保健\",\"chid\":[{\"hasdata\":0,\"name\":\"营养健康\",\"number\":\"2301\"},{\"hasdata\":0,\"name\":\"营养成分\",\"number\":\"2302\"},{\"hasdata\":0,\"name\":\"传统滋补\",\"number\":\"2303\"},{\"hasdata\":0,\"name\":\"成人用品\",\"number\":\"2304\"},{\"hasdata\":0,\"name\":\"保健器械\",\"number\":\"2305\"},{\"hasdata\":0,\"name\":\"急救卫生\",\"number\":\"2306\"},{\"hasdata\":0,\"name\":\"中医药品\",\"number\":\"2307\"}]},{\"name\":\"汽车\",\"chid\":[{\"hasdata\":0,\"name\":\"维修保养\",\"number\":\"1801\"},{\"hasdata\":0,\"name\":\"车载电器\",\"number\":\"1802\"},{\"hasdata\":0,\"name\":\"美容清洗\",\"number\":\"1803\"},{\"hasdata\":0,\"name\":\"汽车装饰\",\"number\":\"1804\"},{\"hasdata\":0,\"name\":\"安全自驾\",\"number\":\"1805\"}]}]";
//			SharedPreferencesUtil.putSharedData(getApplicationContext(),
//					"categoryType", "categoryType", categoryType);
//		} else {
//			Map<String, String> paramsMap = new HashMap<String, String>();
//			String cType = HttpUtil.getHttp(paramsMap,
//					Constants.MAIN_BASE_URL_MOBILE + "apiService/getTypeTree",
//					null);
//			if (cType != null && !"".equals(cType)) {
//				try {
//					JSONObject jo = new JSONObject(cType);
//					cType = jo.getString("content");
//					if (!"[]".equals(cType)) {
//						SharedPreferencesUtil.putSharedData(
//								getApplicationContext(), "categoryType",
//								"categoryType", cType);
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		if (pageInit == null || "".equals(pageInit)) {
//			pageInit = "{\"img2\":\"http://www.bibijing.com/images_dt/app/banner2.jpg\",\"img1\":\"http://www.bibijing.com/images_dt/app/banner1.jpg\",\"keyword\":\"裤子\"}";
//			SharedPreferencesUtil.putSharedData(getApplicationContext(),
//					"pageInit", "pageInit", pageInit);
//		} else {
//			Map<String, String> paramsMap = new HashMap<String, String>();
//			String pInit = HttpUtil.getHttp(paramsMap,
//					Constants.MAIN_BASE_URL_MOBILE + "apiService/getAppFirstPageInit",
//					null);
//			if (pInit != null && !"".equals(pInit)) {
//				try {
//					JSONObject jo = new JSONObject(pInit);
//					pInit = jo.getString("content");
//					if (!"{}".equals(pInit)) {
//						SharedPreferencesUtil.putSharedData(
//								getApplicationContext(), "pageInit",
//								"pageInit", pInit);
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//		}
	}
	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
		switch (requestCode) {
			case 1:
				if (!content.isEmpty()) {
					try {
						JSONObject jsonObject = new JSONObject(content);
						Log.i("网络请求返回数据===dataflow：",jsonObject+"------------------------");
						if (!jsonObject.optString("rowkey").isEmpty()) {
							SharedPreferencesUtil.putSharedData(getApplicationContext(), "clipchange", "clipchange", "1");
							SharedPreferencesUtil.putSharedData(getApplicationContext(), "clipchange", "object", content);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;

			default:
				break;
		}
	}


}
