package com.bbk.activity;

import com.bbk.entity.XGMessageEntity;
import com.bbk.update.UpdateChecker;
import com.google.gson.Gson;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PushWebViewActivity extends BaseActivity implements OnClickListener {
	
	private WebView webViewLayout;
	private String url = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.push_web_view_activity);
		
		url = getIntent().getStringExtra("url");
		initView();
		initData();	
	}

	
	private void initView(){
		webViewLayout = $(R.id.web_view_layout);
		$(R.id.topbar_goback_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				PushWebViewActivity.this.finish();
			}
		});
		loadWebPage(url);
	}
	
	private void loadWebPage(String pageUrl) {
		if(TextUtils.isEmpty(pageUrl)) {
			return ;
		}
		
		WebSettings wSet = webViewLayout.getSettings();
		wSet.setJavaScriptEnabled(true);
		
		webViewLayout.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
//				url = "openCustom:{'startType':'1'}";
				if(url.startsWith("openCustom:")){
					String xgString = url.substring(11);
					XGMessageEntity xgMessage = new Gson().fromJson(xgString, XGMessageEntity.class);
					Intent intent = null;
					switch (xgMessage.getStartType()) {
					case "update":
						UpdateChecker updateChecker = new UpdateChecker(PushWebViewActivity.this);
						updateChecker.checkForUpdates();
						break;
					case "openWeb":
						intent = new Intent(PushWebViewActivity.this,PushWebViewActivity.class);
						intent.putExtra("url", xgMessage.getUrl());
						startActivity(intent);
						break;
					default:
						Class<?> cls = xgMessage.getActivityClass();
						if(cls != null){
							intent = new Intent(PushWebViewActivity.this, cls);
							startActivity(intent);
						}
						break;
					}
					return true;
				}
				return super.shouldOverrideUrlLoading(view, url);
				
//				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//				startActivity(intent);
//				return true;
			}
		});
		
		webViewLayout.loadUrl(pageUrl);
	}
	
	private void initData(){
	}
	
	@Override
	public void onClick(View v) {
		finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(webViewLayout.canGoBack()) {
				webViewLayout.goBack();
				return true;
			} else {
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
		setContentView(R.layout.view_null);
		System.gc();
		super.onDestroy();
	}
	
}
