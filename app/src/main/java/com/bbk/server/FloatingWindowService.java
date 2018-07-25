package com.bbk.server;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.CheckBean;
import com.bbk.activity.IntentActivity;
import com.bbk.activity.JumpDetailActivty;
import com.bbk.activity.MyApplication;
import com.bbk.activity.WelcomeActivity;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.dialog.AlertDialog;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.resource.NewConstants;
import com.bbk.util.CheckYouhuiAlertDialog;
import com.bbk.util.DialogCheckYouhuiUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.logg.Logg;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ClipboardManager;
import android.content.ClipboardManager.OnPrimaryClipChangedListener;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

public class FloatingWindowService extends Service implements ResultEvent {

	private ClipboardManager clipboardManager;
	private DataFlow dataFlow;
	private CheckBean checkBean;
	private long previousTime = 0;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		dataFlow = new DataFlow(this);
		clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		clipboardManager.addPrimaryClipChangedListener(new OnPrimaryClipChangedListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onPrimaryClipChanged() {
				    long now = System.currentTimeMillis();
				    if (now - previousTime < 200){
				    	previousTime = now;
				    	return;
					}
//				    Logg.e("===============>>>","执行了几次");
					previousTime = now;
//				    Logg.e(clipboardManager);
					String text = clipboardManager.getText().toString();
					Logg.e(text);
					if (text != null && !text.equals("") && !text.equals("null")) {
									if (text.contains("bbj")) {
										NewConstants.copyText = text;
									}
//						https://item.taobao.com/item.htm?id=552855465528
//							Map<String, String> paramsMap = new HashMap<String, String>();
//							paramsMap.put("url", text);
//							dataFlow.requestData(1, "newService/checkExsistProduct", paramsMap,FloatingWindowService.this,false);
						// //获得当前activity的名字
//						if (!text.contains("标题:")) {
//							SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "clipchange", "cm", text);
//							Logg.e("======>>>",text);
//							if (text.contains("http")&&text.contains("jd") || text.contains("https" )&& text.contains("jd") || text.contains("http")&&text.contains("taobao") || text.contains("http")&&text.contains("tmall") ||
//									text.contains("http")&&text.contains("zmnxbc") || text.contains("http")&&text.contains("点击链接") || text.contains("http")&&text.contains("喵口令")|| text.contains("https")&&text.contains("taobao")
//									||text.contains("https")&&text.contains("tmall") || text.contains("https")&&text.contains("zmnxbc") || text.contains("https")&&text.contains("点击链接") || text.contains("https")&&text.contains("喵口令") ) {
//								checkExsistProduct(text);
////								Intent intent = new Intent(getApplicationContext(),
////										JumpDetailActivty.class);
//							}
//						}
					}
				}


		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@SuppressLint("NewApi")
	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
		switch (requestCode) {
		case 1:
			if (!content.isEmpty()) {
				try {
					JSONObject jsonObject = new JSONObject(content);
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
//								checkBean = JSON.parseObject(content,CheckBean.class);
//								if (checkBean.getHasCps() != null) {
//									if (checkBean.getHasCps().equals("1")) {
//										Intent intent = new Intent(FloatingWindowService.this, IntentActivity.class);
//										if (checkBean.getUrl() != null && !checkBean.getUrl().equals("")) {
//											intent.putExtra("url", checkBean.getUrl());
//										}
////														if (title != null && !title.equals("")) {
////															intent.putExtra("title", title);
////														}
//										if (checkBean.getDomain() != null && !checkBean.getDomain().equals("")) {
//											intent.putExtra("domain", checkBean.getDomain());
//										}
//										if (checkBean.getRowkey()!= null && !checkBean.getRowkey().equals("")) {
//											intent.putExtra("groupRowKey", checkBean.getRowkey());
//										}
//										if (checkBean.getPrice() != null && !checkBean.getPrice().equals("")) {
//											intent.putExtra("bprice", checkBean.getPrice());
//										}
//										DialogCheckYouhuiUtil.dismiss(2000);
//										intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//										startActivity(intent);
//									}
//								}
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
//						DialogCheckYouhuiUtil.show(getApplicationContext());
					}

					@Override
					public void onError(ExceptionHandle.ResponeThrowable e) {
//						StringUtil.showToast(FloatingWindowService.this, e.message);
					}
				});

	}
}