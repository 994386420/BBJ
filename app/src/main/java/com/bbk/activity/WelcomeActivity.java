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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.entity.XGMessageEntity;
import com.bbk.resource.Constants;
import com.bbk.resource.NewConstants;
import com.bbk.server.FloatingWindowService;
import com.bbk.util.EventIdIntentUtil;
import com.bbk.util.SchemeIntentUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

import static com.bbk.model.MainActivity.consultService;


public class WelcomeActivity extends BaseActivity2{
	String customContent;
	private XGMessageEntity xgMessage;
	private TextView mbtn;
	private final static String ALBUM_PATH
			= Environment.getExternalStorageDirectory() + "/download_image/";
	private ImageView mimg;
	protected String url = Constants.MAIN_BASE_URL_MOBILE+ "apiService/queryAppGuanggao" ;
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
			getWindow().setBackgroundDrawableResource(R.color.white);
			mbtn.setVisibility(View.VISIBLE);
			mbtn.setText("跳过"+curTime+"秒");
			curTime--;

			if (curTime!=-1) {
				handler2.sendEmptyMessageDelayed(curTime, 1000);
			}else{
				Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
				if(xgMessage != null){
					intent.putExtra("xgMessage", new Gson().toJson(xgMessage).toString());
				}
				intent.putExtra("customContent", customContent);
				startActivity(intent);
				finish();
			}
		};
	};
	private int time = 3;
	private static final int REQUEST_EXTERNAL_STORAGE = 1;
	private static String[] PERMISSIONS_STORAGE = {
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE ,
			Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS};

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
		parseIntent();
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

		//启动后台服务
		Intent service=new Intent(this, FloatingWindowService.class);
		startService(service);
		SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "userInfor", "token", token);
		init();
		NewConstants.yingdaoFlag = "0";
		Context context = getApplicationContext();
//		XGPushConfig.setAccessId(context, 2100196420);
//		XGPushConfig.setAccessKey(context, "AUTV25N58F3Z");


//		XGPushManager.registerPush(this, new XGIOperateCallback() {
//			@Override
//			public void onSuccess(Object data, int flag) {
//				Log.e("TPush", "注册成功，设备token为：" + data);
//			}
//			@Override
//			public void onFail(Object data, int errCode, String msg) {
//				Log.e("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
//			}
//		});
		//外部应用打开APP传递参数
		Intent intent = getIntent();
		String scheme = intent.getScheme();
		Uri uri = intent.getData();
		if (uri!= null){
			SchemeIntentUtil.intent(uri,this);
			finish();
		}
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
//					    checkExsistProduct(text);
//						dataFlow.requestData(1, "newService/checkExsistProduct", paramsMap, WelcomeActivity.this,false);
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

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		parseIntent();
	}

	private void parseIntent() {
		Intent intent = getIntent();
//		if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
//			consultService(this, null, null, null);
//			// 最好将intent清掉，以免从堆栈恢复时又打开客服窗口
//			setIntent(new Intent());
//		}
	}

	private void init() {
		if (getIntent().getStringExtra("customContent") != null) {
			customContent = getIntent().getStringExtra("customContent");
		}
		HomeActivity.position = 0;
		mbtn=(TextView) findViewById(R.id.mbtn);
		mbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
				if(xgMessage != null){
					intent.putExtra("xgMessage", new Gson().toJson(xgMessage).toString());
				}
				intent.putExtra("customContent", customContent);
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
			Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
			startActivity(intent);
			finish();
		}else{
			getGuangGao();
		}
	}

	private void getGuangGao(){
		RetrofitClient.getInstance(this).createBaseApi().queryAppGuanggao(
				map, new BaseObserver<String>(this) {
					@Override
					public void onNext(String s) {
						try {
							JSONObject jsonObject = new JSONObject(s);
							if (jsonObject.optString("status").equals("1")) {
								final JSONObject content = jsonObject.getJSONObject("content");
								String img = content.optString("img");
									Glide.with(getApplicationContext())
											.load(img)
											.diskCacheStrategy(DiskCacheStrategy.SOURCE)
											.listener(new RequestListener<String, GlideDrawable>() {
												@Override
												public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
													return false;
												}

												@Override
												public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
													if (mimg == null) {
														return false;
													}
													if (mimg.getScaleType() != ImageView.ScaleType.FIT_XY) {
														mimg.setScaleType(ImageView.ScaleType.FIT_XY);
													}
													ViewGroup.LayoutParams params = mimg.getLayoutParams();
													int vw = mimg.getWidth() - mimg.getPaddingLeft() - mimg.getPaddingRight();
													float scale = (float) vw / (float) resource.getIntrinsicWidth();
													int vh = Math.round(resource.getIntrinsicHeight() * scale);
													params.height = vh + mimg.getPaddingTop() + mimg.getPaddingBottom();
													mimg.setLayoutParams(params);
													return false;
												}
											})
											.into(mimg);
								handler2.sendEmptyMessage(time);
								final String eventId = content.optString("eventId");
								//点击跳转活动页
								mimg.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View arg0) {
										Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
										intent.putExtra("content", content.toString());
										SharedPreferencesUtil.putSharedData(WelcomeActivity.this, "shaixuan", "shaixuan", "yes");
										NewConstants.clickpositionFenlei = 5200;
										NewConstants.clickpositionDianpu = 5200;
										NewConstants.clickpositionMall = 5200;
										startActivity(intent);
										finish();
									}
								});
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					@Override
					protected void hideDialog() {
					}

					@Override
					protected void showDialog() {
					}

					@Override
					public void onError(ExceptionHandle.ResponeThrowable e) {
						StringUtil.showToast(WelcomeActivity.this, e.message);
						Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
						startActivity(intent);
						finish();
					}
				});
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
//					Log.i("=============start",obj+"===========");
//					xgMessage.setStartType(obj.getString("startType"));
//					xgMessage.setUrl(obj.getString("url"));
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
	}


	//是否跳转引导页
	public void startHome() {
		String isFirstUse = SharedPreferencesUtil.getSharedData(this,"isFirstStartUse", "isFirstStartUse");
		if (TextUtils.isEmpty(isFirstUse)) {
			isFirstUse = "yes";
		}
		if (isFirstUse.equals("yes")) {
			SharedPreferencesUtil.putSharedData(this, "isFirstStartUse","isFirstStartUse", "no");
			startActivity(new Intent(this, WelcomeGuideActivity.class));
			finish();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		XGPushManager.unregisterPush(this);
	}
	/**
	 * 初始化分类数据和热度搜索词
	 */
	private void loadData() {
		loadhotKeyword("2");
	}

	/**
	 * 搜索关键词加载
	 * @param type
	 */
	private void loadhotKeyword(final String type) {
		String hotKeyword = SharedPreferencesUtil.getSharedData(getApplicationContext(), "hotKeyword", "hotKeyword");
		if (hotKeyword == null || "".equals(hotKeyword)) {
			hotKeyword = "[{\"name\":\"iphone se\",\"productType\":\"\"},{\"name\":\"衬衫\",\"productType\":\"240201\"},{\"name\":\"电炖锅\",\"productType\":\"030313\"},{\"name\":\"情侣睡衣\",\"productType\":\"240313\"},{\"name\":\"空调\",\"productType\":\"030102\"},{\"name\":\"风扇\",\"productType\":\"030201\"},{\"name\":\"T恤\",\"productType\":\"160209\"},{\"name\":\"奶粉\",\"productType\":\"1701\"}]";
			SharedPreferencesUtil.putSharedData(getApplicationContext(),
					"hotKeyword", "hotKeyword", hotKeyword);
		} else {
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("type",type);
			RetrofitClient.getInstance(this).createBaseApi().getSearchHotWord(
					maps, new BaseObserver<String>(this) {
						@Override
						public void onNext(String s) {
							try {
								JSONObject jsonObject = new JSONObject(s);
								if (jsonObject.optString("status").equals("1")) {
									String content = jsonObject.optString("content");
									if (content!= null && !"".equals(content)) {
											if (type.equals("1")) {
												SharedPreferencesUtil.putSharedData(
														getApplicationContext(), "hotKeyword",
														"hotKeyword", content);
											}
									}
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						@Override
						protected void hideDialog() {

						}

						@Override
						protected void showDialog() {

						}

						@Override
						public void onError(ExceptionHandle.ResponeThrowable e) {
							StringUtil.showToast(WelcomeActivity.this, e.message);
						}
			});
		}
	}
}
