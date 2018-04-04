package com.bbk.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.mobstat.StatService;
import com.bbk.resource.Constants;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.MD5Util;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.MyWebView;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.tauth.Tencent;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * B端数据加载WebView视图
 */
public class DataWebViewActivity extends BaseActivity{
	

	private ProgressBar bar;
	private MyWebView webViewLayout;
	private String htmlUrl = "";
	private ImageButton topbar_goback_btn;
	private TextView title;
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_xg_activity);
		htmlUrl = getIntent().getStringExtra("url");
		
		initView();
		initData();
	}

	
	private void initView(){
		webViewLayout = $(R.id.web_view_layout);
		bar = $(R.id.mProgressBar);
		title = $(R.id.title);
		title.setText("大数据分析");
		TextPaint tp = title.getPaint(); 
		tp.setFakeBoldText(true);
		topbar_goback_btn = (ImageButton) findViewById(R.id.topbar_goback_btn);
		topbar_goback_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 if (webViewLayout.canGoBack()) { 
					 webViewLayout.goBack(); 
				   } 
				   else{
				         finish();
				  }
			}
		});
		//支持JS
        WebSettings settings = webViewLayout.getSettings();
        settings.setJavaScriptEnabled(true);
        //支持屏幕缩放
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
      //不显示webview缩放按钮
        settings.setDisplayZoomControls(false);
		loadWebPage(htmlUrl);
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
				
				if(!url.startsWith("http:") && !url.startsWith("https:")) {
					return true;
				}
				return super.shouldOverrideUrlLoading(view, url);
				
//				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//				startActivity(intent);
//				return true;
			}
		});
		webViewLayout.setWebChromeClient(new WebChromeClient() {
	          @Override
	          public void onProgressChanged(WebView view, int newProgress) {
	              if (newProgress == 100) {
	                  bar.setVisibility(View.GONE);
	              } else {
	                  if (View.GONE == bar.getVisibility()) {
	                      bar.setVisibility(View.VISIBLE);
	                  }
	                  bar.setProgress(newProgress);
	              }
	              super.onProgressChanged(view, newProgress);
	          }
	          
	      });
		webViewLayout.loadUrl(pageUrl);
	}
	
	private void initData(){
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
	protected void onResume() {
		super.onResume();
		XGPushClickedResult click = XGPushManager.onActivityStarted(this);
		if (click != null) { // 判断是否来自信鸽的打开方式
			String customContent = click.getCustomContent();
			if (customContent != null && customContent.length() != 0) {
				try {
					JSONObject obj = new JSONObject(customContent);
					if (!obj.isNull("htmlUrl")) {
						
						String value = obj.getString("htmlUrl");
						htmlUrl = value;
						initView();
						Log.e("===htmlUrl====", htmlUrl+"");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}
	@Override
	protected void onPause() {
		super.onPause();
		XGPushManager.onActivityStoped(this);
	}
	@Override
	protected void onDestroy() {
		setContentView(R.layout.view_null);
		System.gc();
		super.onDestroy();
	}
	@Override
	public void finish() {
		ViewGroup view = (ViewGroup) getWindow().getDecorView();
		view.removeAllViews();
		super.finish();
	}
	
}
