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

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.NewFxBean;
import com.bbk.adapter.FindListAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
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
import com.bbk.util.StringUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.google.gson.Gson;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;


public class WelcomeActivity extends BaseActivity2{
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
					    checkExsistProduct(text);
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
						StringUtil.showToast(WelcomeActivity.this, "网络异常");
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
		XGPushManager.unregisterPush(this);
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
		XGPushManager.unregisterPush(this);
	}
	/**
	 * 初始化分类数据和热度搜索词
	 */
	private void loadData() {
		TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		String token = TelephonyMgr.getDeviceId();
		queryIndexSeeByToken(token);
		queryIndexTuijianByToken(token);
		loadhotKeyword("1");
	}
	private void queryIndexSeeByToken(String token) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", token);
		params.put("page", "1");
		RetrofitClient.getInstance(this).createBaseApi().queryIndexSeeByToken(
				params, new BaseObserver<String>(this) {
					@Override
					public void onNext(String s) {
						try {
							JSONObject jsonObject = new JSONObject(s);
							if (jsonObject.optString("status").equals("1")) {
								String content = jsonObject.optString("content");
								if (content!= null && !"".equals(content)) {
									SharedPreferencesUtil.putSharedData(
											getApplicationContext(), "homedata",
											"seelike", content);
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
						StringUtil.showToast(WelcomeActivity.this, "网络异常");
					}
				});
	}
	private void queryIndexTuijianByToken(String token) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", token);
		RetrofitClient.getInstance(this).createBaseApi().queryIndexTuijianByToken(
				params, new BaseObserver<String>(this) {
					@Override
					public void onNext(String s) {
						try {
							JSONObject jsonObject = new JSONObject(s);
							if (jsonObject.optString("status").equals("1")) {
								String content = jsonObject.optString("content");
								if (content!= null && !"".equals(content)) {
										SharedPreferencesUtil.putSharedData(
												getApplicationContext(), "homedata",
												"hometuijian", content);
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
						StringUtil.showToast(WelcomeActivity.this, "网络异常");
					}
				});

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
							StringUtil.showToast(WelcomeActivity.this, "网络异常");
						}
			});
		}
	}

	private void checkExsistProduct(String text) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("url", text);
		RetrofitClient.getInstance(this).createBaseApi().checkExsistProduct(
				paramsMap, new BaseObserver<String>(this) {
					@Override
					public void onNext(String s) {
						try {
							JSONObject jsonObject = new JSONObject(s);
							if (jsonObject.optString("status").equals("1")) {
								String content = jsonObject.optString("content");
								JSONObject json = new JSONObject(content);
//						Log.i("网络请求返回数据===dataflow：",jsonObject+"------------------------");
								if (!json.optString("rowkey").isEmpty()) {
									SharedPreferencesUtil.putSharedData(getApplicationContext(), "clipchange", "clipchange", "1");
									SharedPreferencesUtil.putSharedData(getApplicationContext(), "clipchange", "object", content);
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
						StringUtil.showToast(WelcomeActivity.this, "网络异常");
					}
				});

	}

}
