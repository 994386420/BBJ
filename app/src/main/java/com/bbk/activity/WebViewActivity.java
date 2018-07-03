package com.bbk.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;
import com.bbk.dialog.WebViewAlertDialog;
import com.bbk.flow.DataFlow4;
import com.bbk.flow.ResultEvent;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.X5WebView;
import com.tamic.jswebview.view.NumberProgressBar;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebViewClient;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.tencent.smtt.sdk.WebView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class WebViewActivity extends BaseActivity implements OnClickListener, ResultEvent{

	private X5WebView webViewLayout;
	private String url = "";
	protected NumberProgressBar bar;
	private LinearLayout gongnenglan, home, shoucang, lookhistory, lookcompare;
	private DataFlow4 dataFlow;
	private String weburl = "";
	private String webtitle = "";
	private boolean isintent = false;
	private ImageView topbar_goback_btn, collectimg;
	private TextView mtitle;
	private String token,domain;
	private String content1 = "";
	private String rowkey = "";
	private String srowkey = "";
	private String hrowkey = "";
	private String reurl = "";
	private String reurl1 = "";
	private boolean ishis = true;
//	private int rowkeynum = 0;
	public static Activity instance = null;
//	private AlibcShowParams alibcShowParams;//页面打开方式，默认，H5，Native
//	private Map<String, String> exParams;//yhhpass参数
   private ProgressBar progressbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_view_activity);
		dataFlow = new DataFlow4(this);
		instance = this;
		getWindow().setBackgroundDrawable(null);
		View topView = findViewById(R.id.topbar_layout);
		// 实现沉浸式状态栏
		ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
		// MyApplication.getInstance().addActivity(this);
		if (getIntent().getStringExtra("rowkey") != null) {
			rowkey = getIntent().getStringExtra("rowkey");
			srowkey = getIntent().getStringExtra("rowkey");
			hrowkey = getIntent().getStringExtra("rowkey");
		}
		url = getIntent().getStringExtra("url");
//		domain = getIntent().getStringExtra("domain");
//		if (domain != null && url != null){
//			if (domain.equals("jd")){
//				// 通过url呼京东主站
//                // url 通过url呼京东主站的地址
//				// mKeplerAttachParameter 存储第三方传入参数
//                // mOpenAppAction  呼京东主站回调
//				KeplerApiManager.getWebViewService().openAppWebViewPage(this,
//						url,
//						mKeplerAttachParameter,
//						mOpenAppAction);
//			}else if (domain.equals("tmall") || domain.equals("taobao")){
//				if(StringUtil.checkPackage(this,"com.taobao.taobao")) {
//					showUrl();
//				}else {
//					StringUtil.showToast(WebViewActivity.this,"未安装淘宝app");
//				}
//			}
//		}
		// url = "https://pages.tmall.com/wow/rais/act/tmall-choice";
		if (url != null){
		if (url.contains("item.jd.com")) {// 京东
			url = url.replace("item.jd.com", "item.m.jd.com/product");
		} /*
			 * else if(url.contains("product.suning.com")) {// 苏宁 String id =
			 * url.replace("http://product.suning.com/", "").replace(".html",
			 * ""); if(id.contains("-")) { id = id.substring(0,
			 * id.indexOf("-")); } url = "http://m.suning.com/product/" + id +
			 * ".html"; }
			 */ else if (url.contains("item.yhd.com")) {// 一号店
			url = url.replace("item.yhd.com", "item.m.yhd.com");
		} else if (url.contains("www.newegg.cn")) {// 新蛋
			url = url.replace("www.newegg.cn", "m.newegg.cn");
		} else if (url.contains("item.yixun.com")) {// 易迅
			url = "http://m.yixun.com/t/detail/index.html?pid="
					+ url.replace("http://item.yixun.com/item-", "").replace(".html", "");
		} else if (url.contains("item.gome.com.cn")) {// 国美
			url = url.replace("item.gome.com.cn/", "m.gome.com.cn/product-");
		}
		}
		initView();

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

		webViewLayout.addView(progressbar);
		WebChromeClient wvcc = new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				webtitle = title;
				mtitle.setText(title);

				if (!reurl1.equals(weburl)) {
					if (!hrowkey.isEmpty()) {
						if (!weburl.isEmpty()) {
							queryCompareByUrl();
							reurl1 = weburl;
						} else {
							gongnenglan.setVisibility(GONE);
						}
					} else {
						if (weburl.contains("product") || weburl.contains("Product") || weburl.contains("detail")
								|| weburl.contains("item") || weburl.contains("gp/aw") || weburl.contains("style/index")
								|| weburl.contains("sku-") || weburl.contains("goods") || weburl.contains("Detail")
								|| weburl.contains("proDetail") || weburl.contains("Goods")) {
							if (!weburl.isEmpty()) {
								queryCompareByUrl();
								reurl1 = weburl;
							} else {
								gongnenglan.setVisibility(GONE);
							}

						} else {
							gongnenglan.setVisibility(GONE);
						}
					}
//
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
		webViewLayout.setWebChromeClient(wvcc);
	}
//	/**
//	 * 打开指定链接
//	 */
//	public void showUrl() {
//		String text = url;
//		if(TextUtils.isEmpty(text)) {
//			StringUtil.showToast(this, "URL为空");
//			return;
//		}
//		AlibcTrade.show(this, new AlibcPage(text), alibcShowParams, null, exParams , new DemoTradeCallback());
//	}
	private void queryCompareByUrl() {
		String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
		Map<String, String> params = new HashMap<>();
		params.put("title", webtitle);
		params.put("url", weburl);
		params.put("userid", userID);
		params.put("rowkey", hrowkey);
		dataFlow.requestData(3, "newService/queryCompareByUrl", params, WebViewActivity.this, false);
//		rowkeynum++;
//		if (rowkeynum>1) {
			hrowkey = "";
//		}
	}

	private void initView() {
		webViewLayout = $(R.id.web_view_layout);
		mtitle = $(R.id.title);
		collectimg = $(R.id.collectimg);
		topbar_goback_btn = $(R.id.topbar_goback_btn);
		topbar_goback_btn.setOnClickListener(this);
		bar = $(R.id.mProgressBar);
		gongnenglan = $(R.id.gongnenglan);
		home = $(R.id.home);
		shoucang = $(R.id.shoucang);
		lookcompare = $(R.id.lookcompare);
		lookhistory = $(R.id.lookhistory);
		String showis = SharedPreferencesUtil.getSharedData(this, "isshowhis", "showhis");
		if (!TextUtils.isEmpty(showis) && "1".equals(showis)) {
			lookhistory.setVisibility(VISIBLE);
		} else {
			lookhistory.setVisibility(GONE);
		}
		home.setOnClickListener(this);
		shoucang.setOnClickListener(this);
		lookhistory.setOnClickListener(this);
		lookcompare.setOnClickListener(this);
		// 支持JS
//		WebSettings webSettings = webViewLayout.getSettings();
//		webSettings.setUseWideViewPort(true);// 设置此属性，可任意比例缩放
//		webSettings.setLoadWithOverviewMode(true);
//		webSettings.setUseWideViewPort(true);
//		webSettings.setJavaScriptEnabled(true);
//		webSettings.setBuiltInZoomControls(true);
//		webSettings.setSupportZoom(true);
		// MyApplication application = (MyApplication) getApplicationContext();
		// int login_STATE = application.getLOGIN_STATE();
		// if (0 == login_STATE) {
		// Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
		// startActivity(new Intent(this, UserLoginNewActivity.class));
		// }else if(1 == login_STATE){
		// String password = SharedPreferencesUtil.getSharedData(this,
		// "userInfor", "password");
		// String userID = SharedPreferencesUtil.getSharedData(this,"userInfor",
		// "userID");
		// String md5 = MD5Util.Md5(password);
		// urlmd5=url+"&id="+userID+"&pass="+md5;
		// }else if(2 == login_STATE){
		// String openID = SharedPreferencesUtil.getSharedData(this,"userInfor",
		// "openID");
		// urlmd5=url+"&openID="+openID;
		// }
		loadWebPage(url);
	}

	private void loadWebPage(String pageUrl) {
		if (pageUrl != null){
			webViewLayout.loadUrl(pageUrl);
		}
		webViewLayout.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (!isintent) {
					if (url.contains("bbjtech://")) {
//						Log.i("===",url);
						if (url.contains("goJump")){
							String [] strings = url.split("=");
//							Log.i("===",strings[2]);
							Intent intent = new Intent(WebViewActivity.this, IntentActivity.class);
							intent.putExtra("groupRowKey", strings[2]);
							startActivity(intent);
						}
						//跳转到邀请好友页面
						if (url.contains("yaoqing")){
						Intent intent = new Intent(WebViewActivity.this, CoinGoGoGoActivity.class);
						intent.putExtra("type", "1");
						startActivity(intent);
					    }
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
							EventIdIntent(jsonObject);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}
				weburl = url;
//				view.loadUrl(url);
				if (!url.startsWith("http:") && !url.startsWith("https:")) {
					return true;
				}
				return super.shouldOverrideUrlLoading(view, url);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				weburl = url;
				if (!url.equals(reurl)) {
					if (url.contains("home.m.jd.com/newAllOrders/newAllOrders.action")) {
						insertOrderHistory("jd");
					}
					// https://h5.m.taobao.com/mlapp/cart.html?spm=a2141.7756461.3.1
					if (url.contains("h5.m.taobao.com/mlapp/olist.html")) {
						insertOrderHistory("taobao");
					}
					if (url.contains("res.m.suning.com/project/cart/orderList.html")) {
						insertOrderHistory("suning");
					}
					if (url.contains("my.m.yhd.com/myH5/h5Order/h5OrderList.do")) {
						insertOrderHistory("yhd");
					}
					if (url.contains("www.amazon.cn/gp/your-account/order-history/ref=ya_aw_converge")) {
						insertOrderHistory("amazon");
					}
					if (url.contains("u.m.gome.com.cn/my_order.html")) {
						insertOrderHistory("gome");
					}
					if (url.startsWith("http:") || url.startsWith("https:")) {
						String token = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor",
								"token");
						String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor",
								"userID");

						if (!reurl1.equals(weburl)) {
							if (!hrowkey.isEmpty()) {
								if (!weburl.isEmpty()) {
									queryCompareByUrl();
									reurl1 = weburl;
								} else {
									gongnenglan.setVisibility(GONE);
								}
							}else{

								if (weburl.contains("product") || weburl.contains("Product")
										|| weburl.contains("detail") || weburl.contains("item")
										|| weburl.contains("gp/aw") || weburl.contains("style/index")
										|| weburl.contains("sku-") || weburl.contains("goods")
										|| weburl.contains("Detail") || weburl.contains("proDetail")
										|| weburl.contains("Goods")) {
									if (!weburl.isEmpty()) {
										queryCompareByUrl();
										reurl1 = weburl;
									} else {
										gongnenglan.setVisibility(GONE);
									}

								} else {
									gongnenglan.setVisibility(GONE);
								}

							}
						}

						// 如果Url包含这些字段就显示下方按钮
						if (url.contains("product") || url.contains("Product") || url.contains("detail")
								|| url.contains("item") || url.contains("gp/aw") || url.contains("style/index")
								|| url.contains("sku-") || url.contains("goods") || url.contains("Detail")
								|| url.contains("proDetail") || url.contains("Goods")) {
							if (!webtitle.equals("")) {
								Map<String, String> params = new HashMap<>();
								params.put("title", webtitle);
								params.put("url", weburl);
								params.put("userid", token);
								params.put("rowkey", rowkey);
								params.put("type", "2");
								dataFlow.requestData(1, "newService/insertFootPrintOrCollect", params,
										WebViewActivity.this, false);
								rowkey = "";
								reurl = url;
							}

						}
					}
				}

			}
		});

	}

	private void insertOrderHistory(String domain) {
		token = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "token");
		Map<String, String> params = new HashMap<>();
		params.put("token", token);
		params.put("domain", domain);
		dataFlow.requestData(2, "newService/insertOrderHistory", params, WebViewActivity.this, false);
	}

	private void initData() {
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.home:
			SharedPreferencesUtil.putSharedData(getApplicationContext(), "homeactivty", "type", "0");
			intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
			break;
		case R.id.shoucang:
			String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
			if (!TextUtils.isEmpty(userID)) {
				Map<String, String> params = new HashMap<>();
				params.put("title", webtitle);
				params.put("url", weburl);
				params.put("userid", userID);
				params.put("rowkey", srowkey);
				params.put("type", "1");
				dataFlow.requestData(4, "newService/insertFootPrintOrCollect", params, WebViewActivity.this, false);
				srowkey = "";
			} else {
				Intent intent14 = new Intent(this, UserLoginNewActivity.class);
				startActivity(intent14);
				StringUtil.showToast(this, "请先登录！");
			}

			break;
		case R.id.lookcompare:
				new WebViewAlertDialog(this).builder(content1, "0",ishis,"webview").show();
			break;
		case R.id.lookhistory:
			new WebViewAlertDialog(this).builder(content1, "1",true,"webview").show();
			// intent = new Intent(this, WebViewDialogActivity.class);
			// intent.putExtra("content", content1);
			// intent.putExtra("type", "1");
			// startActivity(intent);
			break;
			case R.id.topbar_goback_btn:
				if(webViewLayout.canGoBack()){
					webViewLayout.goBack();
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
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK && webViewLayout.canGoBack()){
			webViewLayout.goBack();
			return true;
		}else{
			finish();
		}
		return true;
	}

	@Override
	protected void onDestroy() {
//		if (webViewLayout != null) {
//			webViewLayout.destroy();
////			mFl_web_view_layout.removeView(mWebView);
//			ViewParent parent = webViewLayout.getParent();
//			if (parent != null) {
//				((ViewGroup) parent).removeView(webViewLayout);
//			}
//			webViewLayout = null;
//		}

		if(null!=webViewLayout) {
			if (null != this.webViewLayout.getView()) {
				this.webViewLayout.getView().setVisibility(View.GONE);
				long  timeout
						= ViewConfiguration.getZoomControlsTimeout();

				new Timer().schedule(new TimerTask(){
					@Override
					public void run() {
						try {
							((WebView)webViewLayout.getView()).destroy();
						}catch (Exception e){

						}
					}
				}, timeout+1000L);
			}
		}
			// 如果先调用destroy()方法，则会命中if (isDestroyed())
			// return;这一行代码，需要先onDetachedFromWindow()，再
			// destory()
//			ViewParent parent = webViewLayout.getParent();
//			if (parent != null) {
//				((ViewGroup) parent).removeView(webViewLayout);
//			}
//			if (webViewLayout!= null){
//				webViewLayout.stopLoading();
//				// 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
//				webViewLayout.getSettings().setJavaScriptEnabled(false);
//				webViewLayout.clearHistory();
//				webViewLayout.clearView();
//				webViewLayout.removeAllViews();
//
//				try {
//					webViewLayout.destroy();
//					webViewLayout = null;
//				} catch (Throwable ex) {
//
//				}
//			}
//		}
		super.onDestroy();
	}
//	@Override
//	public void finish() {
//	    ViewGroup view = (ViewGroup) getWindow().getDecorView();
//	    view.removeAllViews();
//	    super.finish();
//	}
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
	protected void onResume() {
//		webViewLayout.reload();
		super.onResume();
	}

	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
		switch (requestCode) {
		case 3:
			try {
				if (dataJo.optInt("status") <= 0) {
					gongnenglan.setVisibility(GONE);
				} else {
					JSONObject jsonObject = new JSONObject(content);
					int iscollect = jsonObject.optInt("iscollect");
					if (!jsonObject.has("hisurl")) {
						lookhistory.setVisibility(GONE);
						ishis   = false;
					}else {
						lookhistory.setVisibility(VISIBLE);
						ishis   = true;
					}
					gongnenglan.setVisibility(VISIBLE);
					if (iscollect == 1) {
						collectimg.setImageResource(R.mipmap.three_05);
						shoucang.setEnabled(false);
					} else {
						collectimg.setImageResource(R.mipmap.three_04);
						shoucang.setEnabled(true);
					}
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			content1 = content;
			break;
		case 4:
			collectimg.setImageResource(R.mipmap.three_05);
			shoucang.setEnabled(false);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
		int i = url.indexOf("userid=");
		url = url.substring(0, i) + "userid=" + userID;
		isintent = false;
		loadWebPage(url);
		super.onActivityResult(requestCode, resultCode, data);
	}


//	private KeplerAttachParameter mKeplerAttachParameter = new KeplerAttachParameter();
//
//	OpenAppAction mOpenAppAction = new OpenAppAction() {
//		@Override
//		public void onStatus(final int status, final String url) {
////			mHandler.post(new Runnable() {
////				@Override
////				public void run() {
//					if (status == OpenAppAction.OpenAppAction_start) {//开始状态未必一定执行，
//						DialogSingleUtil.show(WebViewActivity.this);
//					}else {
////						mKelperTask = null;
//						DialogSingleUtil.dismiss(0);
//					}
//					if(status == OpenAppAction.OpenAppAction_result_NoJDAPP) {
//						StringUtil.showToast(WebViewActivity.this,"未安装京东");
//						//未安装京东
//					}else if(status == OpenAppAction.OpenAppAction_result_BlackUrl){
//						//不在白名单
//					}else if(status == OpenAppAction.OpenAppAction_result_ErrorScheme){
//						//协议错误
//					}else if(status == OpenAppAction.OpenAppAction_result_APP){
//						//呼京东成功
//					}else if(status == OpenAppAction.OpenAppAction_result_NetError){
//						//网络异常
//					}
////				}
////			});
//		}
//	};

}
