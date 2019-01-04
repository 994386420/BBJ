package com.bbk.server;

import org.json.JSONException;
import org.json.JSONObject;
import com.bbk.flow.ResultEvent;
import com.bbk.resource.NewConstants;
import com.bbk.util.SharedPreferencesUtil;
import com.logg.Logg;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ClipboardManager;
import android.content.ClipboardManager.OnPrimaryClipChangedListener;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class FloatingWindowService extends Service implements ResultEvent {

	private ClipboardManager clipboardManager;
	private long previousTime = 0;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
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
					previousTime = now;
					String text = clipboardManager.getText().toString();
					Logg.e(text);
					if (text != null && !text.equals("") && !text.equals("null")) {
									if (text.contains("bbj")) {
										NewConstants.copyText = text;
									}
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
}