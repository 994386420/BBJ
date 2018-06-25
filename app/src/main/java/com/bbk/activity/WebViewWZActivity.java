package com.bbk.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.resource.Constants;
import com.bbk.util.EventIdIntentUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.ShareUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.SoftHideKeyBoardUtil;
import com.bbk.view.MyWebView;
import com.bbk.view.X5WebView;
import com.tamic.jswebview.view.NumberProgressBar;
import com.tamic.jswebview.view.ProgressBarWebView;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import com.tencent.smtt.sdk.WebView;

import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class WebViewWZActivity extends BaseActivity implements OnClickListener, ResultEvent,IUiListener {

	private X5WebView mPbWebview;
	private String url = "";
	protected NumberProgressBar bar;
	private DataFlow dataFlow;
	private String weburl;
	private String webtitle;
	private boolean isintent = false;
	private ImageView topbar_goback_btn,fengxiang;
	private TextView mtitle;
	private String token;
	private String content1 = "";
	private String title = "";
	private String reurl = "";
	private Tencent mTencent;
	private ProgressBar progressbar;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_view_activity);
		SoftHideKeyBoardUtil.assistActivity(this,getStatusBarHeight(WebViewWZActivity.this));
		mTencent = Tencent.createInstance(Constants.QQ_APP_ID, WebViewWZActivity.this);
		dataFlow = new DataFlow(this);
		// MyApplication.getInstance().addActivity(this);
		title = getIntent().getStringExtra("title");
		url = getIntent().getStringExtra("url");
		getWindow().setBackgroundDrawable(null);
		View topView = findViewById(R.id.topbar_layout);
		// 实现沉浸式状态栏
		ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
		initView();
		initData();

		progressbar = new android.widget.ProgressBar(this, null,
				android.R.attr.progressBarStyleHorizontal);
		// 设置进度条的大小
		progressbar.setLayoutParams(new AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.FILL_PARENT,
				5, 0, 0));
		// 可以改变颜色
		ClipDrawable d = new ClipDrawable(new ColorDrawable(getResources().getColor(R.color.tuiguang_color5)),
				Gravity.LEFT, ClipDrawable.HORIZONTAL);
		progressbar.setProgressDrawable(d);
		progressbar.setBackgroundColor(getResources().getColor(R.color.transparent));

		// progressbar.setProgressDrawable(context.getResources().getDrawable(
		// R.drawable.barbgimg));

		mPbWebview.addView(progressbar);
		WebChromeClient wvcc = new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				webtitle = title;
				mtitle.setText(title);
				String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor",
						"userID");
				String token = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "token");
				// 如果Url包含这些字段就显示下方按钮
				if (url.contains("product") || url.contains("Product") || url.contains("detail") || url.contains("item")
						|| url.contains("gp/aw") || url.contains("style/index") || url.contains("sku-") || url.contains("goods")
						|| url.contains("Detail") || url.contains("proDetail") || url.contains("Goods")) {
					Map<String, String> params = new HashMap<>();
					params.put("title", webtitle);
					params.put("url", weburl);
					params.put("userid", token);
					params.put("type", "2");
					dataFlow.requestData(1, "newService/insertFootPrintOrCollect", params, WebViewWZActivity.this, false);
				}
				super.onReceivedTitle(view, title);
			}
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					progressbar.setVisibility(GONE);
				} else {
					if (progressbar.getVisibility() == GONE)
						progressbar.setVisibility(VISIBLE);
					progressbar.setProgress(newProgress);
				}
				super.onProgressChanged(view, newProgress);
			}
		};
		// 设置setWebChromeClient对象
		mPbWebview.setWebChromeClient(wvcc);
	}
	private int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	private void initView() {
		mPbWebview = $(R.id.web_view_layout);
		mtitle = $(R.id.title);
		fengxiang = $(R.id.fengxiang);
		fengxiang.setVisibility(View.VISIBLE);
		topbar_goback_btn = $(R.id.topbar_goback_btn);
		topbar_goback_btn.setOnClickListener(this);
		bar = $(R.id.mProgressBar);
		fengxiang.setOnClickListener(this);
		loadWebPage(url);
	}

	private void loadWebPage(String pageUrl) {
		if (TextUtils.isEmpty(pageUrl)) {
			return;
		}
		mPbWebview.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
				if (!isintent) {
					if (url.contains("bbjtech://")) {
						Uri uri = Uri.parse(url);

						try {
							JSONObject jsonObject = new JSONObject();
							// String host = uri.getHost();
							// String dataString = intent.getDataString();
							String eventId = uri.getQueryParameter("eventId");
							jsonObject.put("eventId", eventId);
							if (uri.getQueryParameter("htmlUrl") != null) {
								String htmlUrl = uri.getQueryParameter("htmlUrl");
								jsonObject.put("htmlUrl", htmlUrl);
							}
							if (uri.getQueryParameter("groupRowkey") != null) {
								String groupRowkey = uri.getQueryParameter("groupRowkey");
								jsonObject.put("groupRowkey", groupRowkey);
							}
							if (uri.getQueryParameter("rankType") != null) {
								String rankType = uri.getQueryParameter("rankType");
								jsonObject.put("rankType", rankType);
							}
							if (uri.getQueryParameter("keyword") != null) {
								String keyword = uri.getQueryParameter("keyword");
								jsonObject.put("keyword", keyword);
							}
							if (uri.getQueryParameter("url")!=null) {
								String url2 = uri.getQueryParameter("url");
								jsonObject.put("url", url2);

							}
							EventIdIntentUtil.EventIdIntent(WebViewWZActivity.this,jsonObject);
//							EventIdIntent(jsonObject);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}
				if (!url.startsWith("http:") && !url.startsWith("https:")) {
					return true;
				}
				weburl = url;
				return super.shouldOverrideUrlLoading(webView, url);
			}

			@Override
			public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
				super.onPageStarted(webView, s, bitmap);
			}

			@Override
			public void onPageFinished(WebView webView, String s) {
				if (url.startsWith("http:") || url.startsWith("https:")) {
					weburl = url;
				}
				if (!url.equals(reurl)) {
					if (url.startsWith("http:") || url.startsWith("https:")) {
						String token = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "token");
						// 如果Url包含这些字段就显示下方按钮
						if (url.contains("product") || url.contains("Product") || url.contains("detail") || url.contains("item")
								|| url.contains("gp/aw") || url.contains("style/index") || url.contains("sku-") || url.contains("goods")
								|| url.contains("Detail") || url.contains("proDetail") || url.contains("Goods")) {
							weburl = url;
							if (!webtitle.isEmpty()) {
								Map<String, String> params = new HashMap<>();
								params.put("title", webtitle);
								params.put("url", weburl);
								params.put("userid", token);
								params.put("type", "2");
								dataFlow.requestData(1, "newService/insertFootPrintOrCollect", params, WebViewWZActivity.this, false);
								reurl = url;
							}

						}
					}
				}
				super.onPageFinished(webView, s);
			}

			@Override
			public void onReceivedError(WebView webView,  int errorCode, String description, String failingUrl) {
								Toast.makeText(WebViewWZActivity.this, description, Toast.LENGTH_SHORT).show();
				 super.onReceivedError(webView, errorCode, description,
				 failingUrl);
			}
		});
//		mPbWebview.setWebViewClient(new WebViewClient() {
//			@Override
//			public boolean shouldOverrideUrlLoading(WebView view, String url) {
//				if (!isintent) {
//					if (url.contains("bbjtech://")) {
//						Uri uri = Uri.parse(url);
//
//						try {
//							JSONObject jsonObject = new JSONObject();
//							// String host = uri.getHost();
//							// String dataString = intent.getDataString();
//							String eventId = uri.getQueryParameter("eventId");
//							jsonObject.put("eventId", eventId);
//							if (uri.getQueryParameter("htmlUrl") != null) {
//								String htmlUrl = uri.getQueryParameter("htmlUrl");
//								jsonObject.put("htmlUrl", htmlUrl);
//							}
//							if (uri.getQueryParameter("groupRowkey") != null) {
//								String groupRowkey = uri.getQueryParameter("groupRowkey");
//								jsonObject.put("groupRowkey", groupRowkey);
//							}
//							if (uri.getQueryParameter("rankType") != null) {
//								String rankType = uri.getQueryParameter("rankType");
//								jsonObject.put("rankType", rankType);
//							}
//							if (uri.getQueryParameter("keyword") != null) {
//								String keyword = uri.getQueryParameter("keyword");
//								jsonObject.put("keyword", keyword);
//							}
//							if (uri.getQueryParameter("url")!=null) {
//								String url2 = uri.getQueryParameter("url");
//								jsonObject.put("url", url2);
//
//							}
//							EventIdIntentUtil.EventIdIntent(WebViewWZActivity.this,jsonObject);
////							EventIdIntent(jsonObject);
//
//						} catch (JSONException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//
//				}
//				if (!url.startsWith("http:") && !url.startsWith("https:")) {
//					return true;
//				}
//				weburl = url;
//				return super.shouldOverrideUrlLoading(view, url);
//			}
//
//			@Override
//			public void onPageStarted(WebView view, String url, Bitmap favicon) {
//				super.onPageStarted(view, url, favicon);
//			}
//
//			@Override
//			public void onPageFinished(WebView view, String url) {
//				if (url.startsWith("http:") || url.startsWith("https:")) {
//					weburl = url;
//				}
//				if (!url.equals(reurl)) {
//					if (url.startsWith("http:") || url.startsWith("https:")) {
//						String token = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "token");
//						// 如果Url包含这些字段就显示下方按钮
//						if (url.contains("product") || url.contains("Product") || url.contains("detail") || url.contains("item")
//								|| url.contains("gp/aw") || url.contains("style/index") || url.contains("sku-") || url.contains("goods")
//								|| url.contains("Detail") || url.contains("proDetail") || url.contains("Goods")) {
//							weburl = url;
//							if (!webtitle.isEmpty()) {
//								Map<String, String> params = new HashMap<>();
//								params.put("title", webtitle);
//								params.put("url", weburl);
//								params.put("userid", token);
//								params.put("type", "2");
//								dataFlow.requestData(1, "newService/insertFootPrintOrCollect", params, WebViewWZActivity.this, false);
//								reurl = url;
//							}
//
//						}
//					}
//				}
//			}
//
//			@Override
//			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//				Toast.makeText(WebViewWZActivity.this, description, Toast.LENGTH_SHORT).show();
//				// super.onReceivedError(view, errorCode, description,
//				// failingUrl);
//			}
//
//		});

		mPbWebview.loadUrl(pageUrl);
	}
	private void initData() {
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.fengxiang:
			String useid = SharedPreferencesUtil.getSharedData(
					getApplicationContext(), "userInfor", "userID");
			if (!TextUtils.isEmpty(useid)) {
				String[] userids = url.split("&userid");
				String shareurl = userids[0];
				ShareUtil.showShareDialog(v, this, "专业的网购比价、导购平台", title, shareurl,"");
			}else{
				Intent intent14 = new Intent(this, UserLoginNewActivity.class);
				intent14.putExtra("iswebyanzheng", "yes");
				isintent = true;
				startActivityForResult(intent14, 1);
			}
			
			
			
			break;
			case R.id.topbar_goback_btn:
				if(mPbWebview.canGoBack()){
					mPbWebview.goBack();
				}else{
					finish();
				}
				break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK &&mPbWebview.canGoBack()){
			mPbWebview.goBack();
			return true;
		}else{
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	// 根据返回的 eventId 和参数判断 跳转
	public void EventIdIntent(JSONObject jo) {
		String eventId = jo.optString("eventId");
		switch (eventId) {
		case "5":
			String htmlUrl = jo.optString("htmlUrl");
			Intent intent4 = new Intent(this, WebViewXGActivity.class);
			intent4.putExtra("htmlUrl", htmlUrl);
			startActivity(intent4);
			break;
		case "6":
			String groupRowkey = jo.optString("groupRowkey");
			Intent intent5 = new Intent(this, DetailsMainActivity22.class);
			intent5.putExtra("groupRowKey", groupRowkey);
			startActivity(intent5);
			break;

		case "10":
			String htmlUrl1 = jo.optString("htmlUrl");
			Intent intent9 = new Intent(this, WebViewRechargeActivity.class);
			intent9.putExtra("htmlUrl", htmlUrl1);
			startActivity(intent9);
			break;
		case "11":
			String rankType = jo.optString("rankType");
			Intent intent10 = new Intent(this, RankCategoryActivity.class);
			intent10.putExtra("type", rankType);
			startActivity(intent10);
			break;
		case "12":
			String keyword = jo.optString("keyword");
			Intent intent11 = new Intent(this, ResultMainActivity.class);
			intent11.putExtra("keyword", keyword);
			startActivity(intent11);
			break;
		case "13":
			String htmlUrl13 = jo.optString("htmlUrl");
			url = htmlUrl13;
		case "14":
			Intent intent14 = new Intent(this, UserLoginNewActivity.class);
			intent14.putExtra("iswebyanzheng", "yes");
			isintent = true;
			startActivityForResult(intent14, 1);
			break;
		default:
			break;
		}
	}

	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 if (mTencent != null) {
	            Tencent.onActivityResultData(requestCode, resultCode, data, this);
	        }
		String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
		int i = url.indexOf("userid=");
		url = url.substring(0,i) + "userid=" + userID;
		isintent = false;
		mPbWebview.loadUrl(url);
//		mPbWebview.postDelayed(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//            	mPbWebview.clearHistory();
//            }
//        }, 1000);
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onDestroy() {
		//在Activity销毁的时候同时销毁WebView
		//如没有此操作，可能会出现，当你在网页上播放一个视频的时候，直接按home键退出应用，视频仍在播放
//		if (mPbWebview != null) {
//			mPbWebview.destroy();
////			mFl_web_view_layout.removeView(mWebView);
//			ViewParent parent = mPbWebview.getParent();
//            if (parent != null) {
//                ((ViewGroup) parent).removeView(mPbWebview);
//            }
//			mPbWebview = null;
//		}

		if(null!=mPbWebview) {
			if (null != this.mPbWebview.getView()) {
				this.mPbWebview.getView().setVisibility(View.GONE);
				long  timeout
						= ViewConfiguration.getZoomControlsTimeout();

				new Timer().schedule(new TimerTask(){
					@Override
					public void run() {
						try {
							((WebView)mPbWebview.getView()).destroy();
						}catch (Exception e){

						}
					}
				}, timeout+1000L);
			}
		}

//			if (mPbWebview.getWebView() != null) {
//				mPbWebview.setVisibility(View.GONE);
//				mPbWebview.getWebView().destroy();
//		}
//			mPbWebview.setVisibility(View.GONE);
//            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
//            // destory()
//            ViewParent parent = mPbWebview.getParent();
//            if (parent != null) {
//                ((ViewGroup) parent).removeView(mPbWebview);
//            }
//
//            mPbWebview.stopLoading();
//            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
//            webViewLayout.getSettings().setJavaScriptEnabled(false);
//            webViewLayout.clearHistory();
//            webViewLayout.clearView();
//            webViewLayout.removeAllViews();
//
//            try {
//            	webViewLayout.destroy();
//            } catch (Throwable ex) {
//
//            }
//        }
		super.onDestroy();
	}

	 /**
	  * QQ回调
	  */
	@Override
	public void onCancel() {

	}

	@Override
	public void onComplete(Object arg0) {
		Toast.makeText(this, "分享成功",
				Toast.LENGTH_LONG).show();
		loadData();
	}

	@Override
	public void onError(UiError arg0) {
		Toast.makeText(this, "分享取消",Toast.LENGTH_LONG).show();
	}
	private void loadData() {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("userid", SharedPreferencesUtil.getSharedData(getApplicationContext(), "userInfor", "userID"));
		dataFlow.requestData(1, "newService/checkIsShare", paramsMap, this, false);

	}
//	@Override
//	public void finish() {
//		ViewGroup view = (ViewGroup) getWindow().getDecorView();
//		view.removeAllViews();
//		super.finish();
//	}
}
