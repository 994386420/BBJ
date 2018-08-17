package com.bbk.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue.IdleHandler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.fastjson.JSON;
import com.baidu.mobstat.StatService;
import com.bbk.Bean.CheckBean;
import com.bbk.Bean.DemoTradeCallback;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.fragment.NewHomeFragment;
import com.bbk.model.MainActivity;
import com.bbk.resource.NewConstants;
import com.bbk.util.ClipDialogUtil;
import com.bbk.util.DensityUtils;
import com.bbk.util.DialogCheckYouhuiUtil;
import com.bbk.util.SchemeIntentUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.util.UpdataDialog;
import com.kepler.jd.Listener.OpenAppAction;
import com.kepler.jd.login.KeplerApiManager;
import com.kepler.jd.sdk.bean.KeplerAttachParameter;
import com.logg.Logg;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BaseFragmentActivity extends FragmentActivity {
	private ClipDialogUtil clipDialogUtil;
	private ClipboardManager clipboardManager;
	private long previousTime = 0;
	private CheckBean checkBean;
	private static AlibcShowParams alibcShowParams;//页面打开方式，默认，H5，Native
	private static Map<String, String> exParams;//yhhpass参数
	private static Handler mHandler = new Handler();
	private static UpdataDialog updataDialog;
	public static boolean cancelCheck = true;// 是否取消查询
	private String copytext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		connectivities();
		clipDialogUtil = new ClipDialogUtil(this);
		Looper.myQueue().addIdleHandler(new IdleHandler() {
            @Override
            public boolean queueIdle() {
//                Log.i("IdleHandler","queueIdle");
                onInit();
                return false; //false 表示只监听一次IDLE事件,之后就不会再执行这个函数了.
            }
        });
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		DialogCheckYouhuiUtil.dismiss(0);
	}

	@Override
	protected void onStart() {
		super.onStart();
		NewConstants.showdialogFlg = "0";
		cancelCheck = true;
		clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//		Logg.e(clipboardManager.getPrimaryClip());
		if ( clipboardManager.getText() != null){
			if (MainActivity.isShowCheck == false){
//				Logg.e(NewHomeFragment.isShowCheck);
		String text =clipboardManager.getText().toString();
		if (text != null && !text.equals("") && !text.equals("null")) {
			copytext = text;
			if (text.contains("bbj")) {
				NewConstants.copyText = text;
			}
			// //获得当前activity的名字
			if (!text.contains("标题:")) {//https://item.taobao.com/item.htm?id=552530797708
				SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "clipchange", "cm", text);
				if (text.contains("http") && text.contains("jd") || text.contains("https") && text.contains("jd") || text.contains("http") && text.contains("taobao") || text.contains("http") && text.contains("tmall") ||
						text.contains("http") && text.contains("zmnxbc") || text.contains("http") && text.contains("淘") || text.contains("http") && text.contains("喵口令") || text.contains("https") && text.contains("taobao")
						|| text.contains("https") && text.contains("tmall") || text.contains("https") && text.contains("zmnxbc") || text.contains("https") && text.contains("淘") || text.contains("https") && text.contains("喵口令")) {
					String cliptext = SharedPreferencesUtil.getSharedData(this, "copyText", "copyText");
					Logg.e(text+"======="+cliptext);
					if (!text.equals(cliptext)) {
						checkExsistProduct(text);
					}
				}
			}
		}
		}
		}
	}

	//子类重写此函数即可,而不需要在onCreate()中去初始化.
    protected void onInit() {
		// 设置为U-APP场景
		MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);
		//友盟错误统计
		MobclickAgent.setCatchUncaughtExceptions(true);
    }

	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(this);
		MobclickAgent.onResume(this);
//		String clipchange = SharedPreferencesUtil.getSharedData(this, "clipchange", "clipchange");
//		if (clipchange.equals("1")) {
//			if (BaseFragmentActivity.this != null) {
//				ClipDialogUtil.creatDialog(this);
//				SharedPreferencesUtil.putSharedData(getApplicationContext(), "clipchange", "clipchange", "0");
//			}
//		}

//		clipboardManager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
//
//			@SuppressWarnings("deprecation")
//			@Override
//			public void onPrimaryClipChanged() {
//				long now = System.currentTimeMillis();
//				if (now - previousTime < 200){
//					previousTime = now;
//					return;
//				}
////				    Logg.e("===============>>>","执行了几次");
//				previousTime = now;
//
//			}
//
//
//		});
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
//						Log.i("checkprice：",jsonObject+"------------------------");
//								if (!json.optString("rowkey").isEmpty()) {
								SharedPreferencesUtil.putSharedData(getApplicationContext(), "clipchange", "clipchange", "1");
								SharedPreferencesUtil.putSharedData(getApplicationContext(), "clipchange", "object", content);
//								}
								Logg.json(content);
								checkBean = JSON.parseObject(content,CheckBean.class);
								if (checkBean.getHasCps() != null) {
									if (checkBean.getHasCps().equals("1")) {
										DialogCheckYouhuiUtil.dismiss(2000);
										mHandler.postDelayed(new Runnable() {
											@Override
											public void run() {
												if (cancelCheck) {
													showYouhuiDialog(BaseFragmentActivity.this);
												}
											}
										}, 2000);
									}
								}else{
									DialogCheckYouhuiUtil.dismiss(2000);
									if (cancelCheck) {
									mHandler.postDelayed(new Runnable() {
										@Override
										public void run() {
												showMessageDialog(BaseFragmentActivity.this, checkBean.getUrl());
												;//耗时操作
										}
									}, 2000);
								}
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					@Override
					protected void hideDialog() {
//						clipboardManager.setPrimaryClip(ClipData.newPlainText(null, ""));
						SharedPreferencesUtil.putSharedData(BaseFragmentActivity.this, "copyText", "copyText", copytext);
					}

					@Override
					protected void showDialog() {
						if (BaseFragmentActivity.this != null){
							DialogCheckYouhuiUtil.show(BaseFragmentActivity.this);
					    }else {
						DialogCheckYouhuiUtil.dismiss(0);
					}
					}

					@Override
					public void onError(ExceptionHandle.ResponeThrowable e) {
//						StringUtil.showToast(FloatingWindowService.this, e.message);
						DialogCheckYouhuiUtil.dismiss(0);
					}
				});
//		https://item.taobao.com/item.htm?id=43804811906
	}
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		//外部应用打开APP传递参数
//		Intent intent = getIntent();
//		String scheme = intent.getScheme();
//		Uri uri = intent.getData();
//		if (uri!= null){
//			SchemeIntentUtil.intent(uri,this);
//		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(this);
		MobclickAgent.onPause(this);
	}


	public void connectivities() {
		ConnectivityManager cm = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();

		if (null == networkInfo) {
			StringUtil.showToast(this, "没有网络连接");
		} else if (!wifiInfo.isConnected()) {
//			Toast toast = Toast.makeText(this, "您的WIFI未连接，正在使用流量",
//					Toast.LENGTH_LONG);
//			toast.show();
		}
	}
	@SuppressWarnings("unchecked")
	public <T extends View> T $(int resId) {
		return (T) super.findViewById(resId);
	}

	protected View inflateView(int id) {
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(id, null);
		return view;
	}
	protected MyApplication getApp(){
		return (MyApplication) getApplication();
	}

	/**
	 * 有优惠券弹窗
	 * @param context
	 */
	public void showYouhuiDialog(final Context context) {
		if (updataDialog == null || !updataDialog.isShowing()) {
			//初始化弹窗 布局 点击事件的id
			updataDialog = new UpdataDialog(context, R.layout.check_nomessage_dialog_layout,
					new int[]{R.id.tv_update_gengxin});
			updataDialog.show();
			updataDialog.setCanceledOnTouchOutside(true);
			LinearLayout llZuji = updataDialog.findViewById(R.id.ll_zuji);
			llZuji.setVisibility(View.VISIBLE);
			llZuji.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
					if (TextUtils.isEmpty(userID)) {
						JumpDetailActivty.Flag = "home";
						Intent intent = new Intent(context, UserLoginNewActivity.class);
						startActivityForResult(intent, 1);
					} else {
						Intent intent  = new Intent(context, BrowseActivity.class);
						updataDialog.dismiss();
						cancelCheck = false;
						startActivity(intent);
					}
				}
			});
			TextView tvZuan = updataDialog.findViewById(R.id.tv_zuan);
			TextView tvQuan = updataDialog.findViewById(R.id.tv_update);
			tvQuan.setTextColor(context.getResources().getColor(R.color.tuiguang_color2));
			tvZuan.setText(checkBean.getMessage1());
			tvQuan.setText(checkBean.getMessage2());
			TextView tv_update_gengxin = updataDialog.findViewById(R.id.tv_update_gengxin);
			tv_update_gengxin.setText("查看优惠");
			tv_update_gengxin.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					updataDialog.dismiss();
					Intent intent = new Intent(context, IntentActivity.class);
					if (checkBean.getUrl() != null && !checkBean.getUrl().equals("")) {
						intent.putExtra("url", checkBean.getUrl());
					}
					if (checkBean.getDomain() != null && !checkBean.getDomain().equals("")) {
						intent.putExtra("domain", checkBean.getDomain());
					}
					if (checkBean.getRowkey() != null && !checkBean.getRowkey().equals("")) {
						intent.putExtra("groupRowKey", checkBean.getRowkey());
					}
					if (checkBean.getPrice() != null && !checkBean.getPrice().equals("")) {
						intent.putExtra("bprice", checkBean.getPrice());
					}
					startActivity(intent);
				}
			});
			LinearLayout ll_close = updataDialog.findViewById(R.id.ll_close);
			ll_close.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					updataDialog.dismiss();
					cancelCheck = false;
				}
			});
		}
	}
	/**
	 *
	 * @param context
	 */
	public void showMessageDialog(final Context context, final String url) {
		if(updataDialog == null || !updataDialog.isShowing()) {
			//初始化弹窗 布局 点击事件的id
			updataDialog = new UpdataDialog(context, R.layout.check_nomessage_dialog_layout,
					new int[]{R.id.tv_update_gengxin});
			updataDialog.show();
			updataDialog.setCanceledOnTouchOutside(true);
			TextView tv_update_gengxin = updataDialog.findViewById(R.id.tv_update_gengxin);
			tv_update_gengxin.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					updataDialog.dismiss();
					jumpThirdApp(url);
				}
			});
			LinearLayout ll_close = updataDialog.findViewById(R.id.ll_close);
			ll_close.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					updataDialog.dismiss();
				}
			});
		}
	}



	public  void jumpThirdApp(String url){
//			DialogSingleUtil.show(context);
		alibcShowParams = new AlibcShowParams(OpenType.Native, false);
		alibcShowParams.setClientType("taobao_scheme");
		exParams = new HashMap<>();
		exParams.put("isv_code", "appisvcode");
		exParams.put("alibaba", "阿里巴巴");//自定义参数部分，可任意增删改
//			if (domain != null) {
		if (url.contains("tmall") || url.contains("taobao")) {
			showUrl(url);
		} else if (url.contains("jd")) {
			KeplerApiManager.getWebViewService().openAppWebViewPage(BaseFragmentActivity.this,
					url,
					mKeplerAttachParameter,
					mOpenAppAction);
//					DialogSingleUtil.dismiss(100);
		} else {
			Intent intent = new Intent(BaseFragmentActivity.this, WebViewActivity.class);
			if (url != null) {
				intent.putExtra("url", url);
			}
//					if (rowkey != null) {
//						intent.putExtra("rowkey", rowkey);
//					}
			startActivity(intent);
//					DialogSingleUtil.dismiss(50);
		}
//			}
	}

	/**
	 * 打开指定链接
	 */
	public  void showUrl(String url) {
		String text = url;
		if (TextUtils.isEmpty(text)) {
			StringUtil.showToast(BaseFragmentActivity.this, "URL为空");
			return;
		}
		AlibcTrade.show(BaseFragmentActivity.this, new AlibcPage(text), alibcShowParams, null, exParams, new DemoTradeCallback());
//		DialogSingleUtil.dismiss(100);
	}

	private static KeplerAttachParameter mKeplerAttachParameter = new KeplerAttachParameter();

	OpenAppAction mOpenAppAction = new OpenAppAction() {
		@Override
		public void onStatus(final int status, final String url) {
			Intent intent;
			if (status == OpenAppAction.OpenAppAction_start) {//开始状态未必一定执行，
//				DialogSingleUtil.show(context);
			} else {
//				DialogSingleUtil.dismiss(0);
			}
			if (status == OpenAppAction.OpenAppAction_result_NoJDAPP) {
				StringUtil.showToast(BaseFragmentActivity.this, "未安装京东");
				intent = new Intent(BaseFragmentActivity.this, WebViewActivity.class);
				if (url != null) {
					intent.putExtra("url", url);
				}
//				if (rowkey != null) {
//					intent.putExtra("rowkey", rowkey);
//				}
				startActivity(intent);
				//未安装京东
			} else if (status == OpenAppAction.OpenAppAction_result_BlackUrl) {
				StringUtil.showToast(BaseFragmentActivity.this, "不在白名单");
				//不在白名单
			} else if (status == OpenAppAction.OpenAppAction_result_ErrorScheme) {
				StringUtil.showToast(BaseFragmentActivity.this, "协议错误");
				//协议错误
			} else if (status == OpenAppAction.OpenAppAction_result_APP) {
				//呼京东成功
			} else if (status == OpenAppAction.OpenAppAction_result_NetError) {
				StringUtil.showToast(BaseFragmentActivity.this, "网络异常");
				//网络异常
			}
		}
	};
}
