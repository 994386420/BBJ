package com.bbk.server;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.bbk.activity.MyApplication;
import com.bbk.activity.WelcomeActivity;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ClipboardManager;
import android.content.ClipboardManager.OnPrimaryClipChangedListener;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class FloatingWindowService extends Service implements ResultEvent {

	private ClipboardManager clipboardManager;
	private DataFlow dataFlow;

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
					String text = clipboardManager.getText().toString();
					if (text != null) {
							SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "clipchange", "cm", text);
//							Map<String, String> paramsMap = new HashMap<String, String>();
//							paramsMap.put("url", text);
//							dataFlow.requestData(1, "newService/checkExsistProduct", paramsMap,FloatingWindowService.this,false);
						checkExsistProduct(text);
				}
				// //获得当前activity的名字
				// Intent intent = new Intent(getApplicationContext(),
				// ResultDialogActivity.class);
				// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// startActivity(intent);

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
						StringUtil.showToast(FloatingWindowService.this, e.message);
					}
				});

	}
}