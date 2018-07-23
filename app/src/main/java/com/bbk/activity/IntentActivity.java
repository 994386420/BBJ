package com.bbk.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.alibaba.baichuan.android.trade.callback.AlibcLoginCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.fastjson.JSON;
import com.bbk.Bean.DemoTradeCallback;
import com.bbk.Bean.JumpBean;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.flow.DataFlow3;
import com.bbk.resource.NewConstants;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.RoundImageView;
import com.kepler.jd.Listener.OpenAppAction;
import com.kepler.jd.login.KeplerApiManager;
import com.kepler.jd.sdk.bean.KeplerAttachParameter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class IntentActivity extends BaseActivity {

	private DataFlow3 dataFlow;
	private String title;
	private String url;
	private String domain,isczg,bprice;
	private RoundImageView img3;
	private RelativeLayout onclickthis;
	private TextView text1;
	private boolean isintent = true;
	private Handler handler = new Handler();
	private Runnable runa = new Runnable() {
		
		@Override
		public void run() {
			Intent intent = new Intent(IntentActivity.this,WebViewActivity.class);
			intent.putExtra("url", url);
			intent.putExtra("rowkey", rowkey);
			startActivity(intent);
			finish();
		}
	};
	private RelativeLayout intentbuy;
	private String rowkey;
	String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
	private AlibcShowParams alibcShowParams;//页面打开方式，默认，H5，Native
	private Map<String, String> exParams;//yhhpass参数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intent);
		View topView = findViewById(R.id.topbar_layout);
		// 实现沉浸式状态栏
		ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
		dataFlow = new DataFlow3(this);
		title = getIntent().getStringExtra("title");
		url = getIntent().getStringExtra("url");
		domain = getIntent().getStringExtra("domain");
		rowkey = getIntent().getStringExtra("groupRowKey");
		isczg = getIntent().getStringExtra("isczg");
		bprice = getIntent().getStringExtra("bprice");
		initView();
		getJumpUrl();
	}

	private void initView() {
		
		img3 = (RoundImageView) findViewById(R.id.img3);
		text1 =(TextView)findViewById(R.id.text1);
		onclickthis =(RelativeLayout)findViewById(R.id.onclickthis);
		intentbuy =(RelativeLayout)findViewById(R.id.intentbuy);
		intentbuy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(IntentActivity.this,WebViewActivity.class);
				intent.putExtra("url", url);
				startActivity(intent);
				finish();
				
			}
		});
		onclickthis.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				handler.removeCallbacks(runa);
				finish();
			}
		});
		try {
			if (url.contains("jd")){
				domain = "jd";
			}else if (url.contains("tmall")){
				domain = "tmall";
			}else if (url.contains("taobao")){
				domain = "taobao";
			}
			int drawS = getResources().getIdentifier(domain,"mipmap", getPackageName());
			img3.setImageResource(drawS);
		}catch (Exception E){
			E.printStackTrace();
		}
	}
	@Override
	protected void onDestroy() {
		handler.removeCallbacksAndMessages(null);  
		super.onDestroy();
		
	}

	/**
	 * 跳转页面
	 */
	private void getJumpUrl() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("userid", userID);
		if (title != null) {
			params.put("title", title);
		}
		if (url != null) {
			params.put("url", url);
		}
		if (domain != null) {
			params.put("domain", domain);
		}
		params.put("client", "andorid");
		if (isczg != null) {
			params.put("isczg", isczg);// 如果是从超值购请求的   新增参数  isczg=1,否则可以不传
		}
		if (bprice != null) {
			params.put("bprice", bprice);
		}
		if (rowkey != null){
			params.put("rowkey",rowkey);
		}
		params.put("client", "andorid");
		RetrofitClient.getInstance(this).createBaseApi().getJumpUrl(
				params, new BaseObserver<String>(this) {
					@Override
					public void onNext(String s) {
						try {
							JSONObject jsonObject = new JSONObject(s);
							String content = jsonObject.optString("content");
							if (jsonObject.optString("status").equals("1")) {
								    JumpBean jumpBean = JSON.parseObject(content,JumpBean.class);
									isintent = false;
//									String type = jumpBean.getType();
//									if (type.equals("0")) {
//										intentbuy.setVisibility(View.VISIBLE);
//										onclickthis.setVisibility(View.VISIBLE);
//										url = jumpBean.getUrl();
//										if (!jumpBean.getDomainCh().isEmpty()) {
//											String domainCh = jumpBean.getDomainCh();
//											text1.setText("此商品已下架，将跳转至"+domainCh+"搜索页");
//										}else{
//											text1.setText("此商品已下架，将跳转至该商城搜索页");
//										}
//									}else{
//										url = jumpBean.getUrl();
//										if (jumpBean.getDesc() != null){
//											String desc = jumpBean.getDesc();
//											if (!"".equals(desc)){
//												text1.setText(desc);
//												taobaoLogin(IntentActivity.this,url);
//											}else {
												if (jumpBean.getJumpThirdPage() != null && jumpBean.getJumpThirdPage().equals("1")){
													Intent intent = new Intent(IntentActivity.this,JumpDetailActivty.class);
													intent.putExtra("content", content);
													startActivity(intent);
													DialogSingleUtil.dismiss(0);
													finish();
												}else {
													//jumpThirdPage 为0
													if (jumpBean.getUrl() != null) {
														if (jumpBean.getUrl().contains("jd")) {
															// 通过url呼京东主站
															// url 通过url呼京东主站的地址
															// mKeplerAttachParameter 存储第三方传入参数
															// mOpenAppAction  呼京东主站回调
															KeplerApiManager.getWebViewService().openAppWebViewPage(IntentActivity.this,
																	url,
																	mKeplerAttachParameter,
																	mOpenAppAction);
															DialogSingleUtil.dismiss(0);
															finish();
														} else if (jumpBean.getUrl().contains("taobao") || jumpBean.getUrl().contains("tmall")) {
															alibcShowParams = new AlibcShowParams(OpenType.Native, false);
															alibcShowParams.setClientType("taobao_scheme");
															exParams = new HashMap<>();
															exParams.put("isv_code", "appisvcode");
															exParams.put("alibaba", "阿里巴巴");//自定义参数部分，可任意增删改
															showUrl();
															DialogSingleUtil.dismiss(0);
															finish();
														} else {
															handler.postDelayed(runa, 0);
														}
													}
												}
//											}
//										}else {
//											if (jumpBean.getJumpThirdPage() != null && jumpBean.getJumpThirdPage().equals("1")){
//												Intent intent = new Intent(IntentActivity.this,JumpDetailActivty.class);
//												intent.putExtra("content", content);
//												startActivity(intent);
//												finish();
//											}else {
//												handler.postDelayed(runa, 0);
//											}
//										}
//									}

							} else {
								StringUtil.showToast(IntentActivity.this, jsonObject.optString("errmsg"));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					@Override
					protected void hideDialog() {
						DialogSingleUtil.dismiss(0);
					}

					@Override
					protected void showDialog() {
						if (NewConstants.showdialogFlg.equals("1")) {
							DialogSingleUtil.show(IntentActivity.this);
						}
					}

					@Override
					public void onError(ExceptionHandle.ResponeThrowable e) {
//						DialogSingleUtil.dismiss(0);
						StringUtil.showToast(IntentActivity.this, e.message);
					}
				});
	}
//	private void initData() {
//		HashMap<String, String> params = new HashMap<String, String>();
//		params.put("title", title);
//		params.put("url", url);
//		params.put("domain", domain);
//		params.put("client", "andorid");
////		dataFlow.requestData(1, "newApp/getJumpUrl", params, this);
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
////				try {
////					Thread.sleep(5000);
////					if (isintent) {
////						Intent intent = new Intent(IntentActivity.this,WebViewActivity.class);
////						intent.putExtra("url", url);
////						startActivity(intent);
////						finish();
////					}
////				} catch (InterruptedException e) {
////					// TODO Auto-generated catch block
////					e.printStackTrace();
////				}
//			}
//		}).start();
//	}
//
//	public void  taobaoLogin(final Context context, final String url){
//		if (AlibcLogin.getInstance().getSession()!= null){
//			String nick = AlibcLogin.getInstance().getSession().nick;
//			if (nick!= null && !"".equals(nick)) {
//				handler.postDelayed(runa, 0);
//			}else {
//				AlibcLogin alibcLogin = AlibcLogin.getInstance();
//
//				alibcLogin.showLogin(IntentActivity.this, new AlibcLoginCallback() {
//
//
//					@Override
//					public void onSuccess() {
//
//					}
//
//					@Override
//					public void onFailure(int i, String s) {
//
//					}
//				});
//		}
//
//		}else {
//			handler.postDelayed(runa, 0);
//		}
//
//	}
	/**
	 * 打开指定链接
	 */
	public void showUrl() {
		String text = url;
		if(TextUtils.isEmpty(text)) {
			StringUtil.showToast(this, "URL为空");
			return;
		}
		AlibcTrade.show(this, new AlibcPage(text), alibcShowParams, null, exParams , new DemoTradeCallback());
	}

	private KeplerAttachParameter mKeplerAttachParameter = new KeplerAttachParameter();

	OpenAppAction mOpenAppAction = new OpenAppAction() {
		@Override
		public void onStatus(final int status, final String url) {
			Intent intent;
			if (status == OpenAppAction.OpenAppAction_start) {//开始状态未必一定执行，
				DialogSingleUtil.show(IntentActivity.this);
			}else {
				DialogSingleUtil.dismiss(0);
			}
			if(status == OpenAppAction.OpenAppAction_result_NoJDAPP) {
				StringUtil.showToast(IntentActivity.this,"未安装京东");
				intent = new Intent(IntentActivity.this, WebViewActivity.class);
				if (url != null) {
					intent.putExtra("url", url);
				}
				if (rowkey != null) {
					intent.putExtra("rowkey", rowkey);
				}
				startActivity(intent);
				//未安装京东
			}else if(status == OpenAppAction.OpenAppAction_result_BlackUrl){
				StringUtil.showToast(IntentActivity.this,"不在白名单");
				//不在白名单
			}else if(status == OpenAppAction.OpenAppAction_result_ErrorScheme){
				StringUtil.showToast(IntentActivity.this,"协议错误");
				//协议错误
			}else if(status == OpenAppAction.OpenAppAction_result_APP){
				//呼京东成功
			}else if(status == OpenAppAction.OpenAppAction_result_NetError){
				StringUtil.showToast(IntentActivity.this,"网络异常");
				//网络异常
			}
		}
	};
}
