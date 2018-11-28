package com.bbk.activity;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.alibaba.baichuan.android.trade.AlibcTrade;
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
import com.bbk.util.UpdataDialog;
import com.bbk.view.AdaptionSizeTextView;
import com.bbk.view.RoundImageView;
import com.bumptech.glide.Glide;
import com.kepler.jd.login.KeplerApiManager;
import com.kepler.jd.sdk.bean.KeplerAttachParameter;
import com.kepler.jd.sdk.exception.KeplerBufferOverflowException;
import com.logg.Logg;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class IntentActivity extends BaseActivity {

	private DataFlow3 dataFlow;
	private String title;
	private String url;
	private String domain,isczg,bprice,type,quan,zuan,tljid;
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
			DialogSingleUtil.dismiss(0);
			finish();
		}
	};
	private RelativeLayout intentbuy;
	private String rowkey;
	String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
	private AlibcShowParams alibcShowParams;//页面打开方式，默认，H5，Native
	private Map<String, String> exParams;//yhhpass参数
	private UpdataDialog updataDialog;
	private boolean cancleJump = true;

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
		quan = getIntent().getStringExtra("quan");
		type = getIntent().getStringExtra("type");
		zuan = getIntent().getStringExtra("zuan");
		if (getIntent().getStringExtra("tljid") != null) {
			tljid = getIntent().getStringExtra("tljid");
		}
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
								Logg.json(jsonObject);
								final JumpBean jumpBean = JSON.parseObject(content,JumpBean.class);
								if (type != null) {
									if (type.equals("miaosha")) {
										handler.postDelayed(new Runnable() {
											@Override
											public void run() {
												if (cancleJump) {
													IntentThirdApp(domain, jumpBean.getUrl());
												}
											}
										}, 2000);
									}
								}else {
									isintent = false;
									if (jumpBean.getJumpThirdPage() != null && jumpBean.getJumpThirdPage().equals("1")) {
										DialogSingleUtil.dismiss(0);
										Intent intent = new Intent(IntentActivity.this, JumpDetailActivty.class);
										intent.putExtra("content", content);
										intent.putExtra("isczg", isczg);
										intent.putExtra("tljid",tljid);
										startActivity(intent);
										finish();
									} else {
										//jumpThirdPage 为0
										if (jumpBean.getUrl() != null) {
											if (jumpBean.getUrl().contains("jd")) {
												DialogSingleUtil.dismiss(0);
												finish();
											} else if (jumpBean.getUrl().contains("taobao") || jumpBean.getUrl().contains("tmall")) {
												alibcShowParams = new AlibcShowParams(OpenType.Native, false);
												alibcShowParams.setClientType("taobao_scheme");
												exParams = new HashMap<>();
												exParams.put("isv_code", "appisvcode");
												exParams.put("alibaba", "阿里巴巴");//自定义参数部分，可任意增删改
												showUrl(jumpBean.getUrl());
												DialogSingleUtil.dismiss(0);
												finish();
											} else {
												handler.postDelayed(runa, 0);
											}
										}
									}
								}

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
						if (type != null) {
							if (type.equals("miaosha")) {
								handler.postDelayed(new Runnable() {
									@Override
									public void run() {
											updataDialog.dismiss();
									}
								}, 2000);
							}
						}
					}

					@Override
					protected void showDialog() {
						if (NewConstants.showdialogFlg.equals("1")) {
							DialogSingleUtil.show(IntentActivity.this);
						}
						if (type != null) {
							if (type.equals("miaosha")) {
								showLoadingDialog(IntentActivity.this, domain, quan,zuan);
							}
						}
					}

					@Override
					public void onError(ExceptionHandle.ResponeThrowable e) {
						if (type != null) {
							if (type.equals("miaosha")) {
								updataDialog.dismiss();
							}
						}
						DialogSingleUtil.dismiss(0);
						finish();
						StringUtil.showToast(IntentActivity.this, e.message);
					}
				});
	}
	/**
	 * 打开指定链接
	 */
	public void showUrl(String url) {
		String text = url;
		if(TextUtils.isEmpty(text)) {
			StringUtil.showToast(this, "URL为空");
			return;
		}
		AlibcTrade.show(this, new AlibcPage(text), alibcShowParams, null, exParams , new DemoTradeCallback());
	}

	private KeplerAttachParameter mKeplerAttachParameter = new KeplerAttachParameter();

	public void showUrll(String url) {
		final String text = url;
		if (TextUtils.isEmpty(text)) {
			StringUtil.showToast(this, "URL为空");
			return;
		}
		if (cancleJump) {
			updataDialog.dismiss();
			AlibcTrade.show(IntentActivity.this, new AlibcPage(text), alibcShowParams, null, exParams, new DemoTradeCallback());
			finish();
		}
	}
	public void IntentThirdApp(String domain,final String url){
		cancleJump = true;
		alibcShowParams = new AlibcShowParams(OpenType.Native, false);
		alibcShowParams.setClientType("taobao_scheme");
		exParams = new HashMap<>();
		exParams.put("isv_code", "appisvcode");
		exParams.put("alibaba", "阿里巴巴");//自定义参数部分，可任意增删改
		if (domain != null) {
			if (domain.equals("tmall") || domain.equals("taobao")) {
				showUrll(url);
			} else if (domain.equals("jd")) {
						if (cancleJump) {
							updataDialog.dismiss();
							try {
								KeplerApiManager.getWebViewService().openJDUrlPage(url, mKeplerAttachParameter,IntentActivity.this, mOpenAppAction, 1500);
							} catch (KeplerBufferOverflowException e) {
								e.printStackTrace();
							} catch (JSONException e) {
								e.printStackTrace();
							}
							finish();
						}
			} else {
				if (cancleJump) {
					Intent intent = new Intent(IntentActivity.this, WebViewActivity.class);
					if (url != null) {
						intent.putExtra("url", url);
					}
					if (rowkey != null) {
						intent.putExtra("rowkey", rowkey);
					}
					updataDialog.dismiss();
					finish();
					startActivity(intent);
				}
			}
		}
	}
	public void showLoadingDialog(final Context context,String jumpdomain,String quans,String zuan) {
		Logg.e(zuan);
		if(updataDialog == null || !updataDialog.isShowing()) {
			//初始化弹窗 布局 点击事件的id
			updataDialog = new UpdataDialog(context, R.layout.disanfang_dialog,
					new int[]{R.id.ll_close});
			updataDialog.show();
			LinearLayout img_close = updataDialog.findViewById(R.id.ll_close);
			ImageView imgLoading = updataDialog.findViewById(R.id.img_loading);
			ImageView imageView = updataDialog.findViewById(R.id.img_app);
			AdaptionSizeTextView adaptionSizeTextViewQuan = updataDialog.findViewById(R.id.quan);
			AdaptionSizeTextView adaptionSizeTextViewQuan1 = updataDialog.findViewById(R.id.quan1);
			if (domain.equals("jd")){
				jumpdomain = "jumpjd";
			}else if (domain.equals("tmall")){
				jumpdomain = "jumptmall";
			}else if (domain.equals("taobao")){
				jumpdomain = "jumptaobao";
			}
			if (quans != null && !quans.equals("") && !quans.equals("0")) {
				adaptionSizeTextViewQuan1.setVisibility(View.VISIBLE);
				adaptionSizeTextViewQuan1.setText("领券减"+quans+"元");
			} else {
				adaptionSizeTextViewQuan1.setVisibility(View.INVISIBLE);
			}

			if (zuan != null && !zuan.equals("") && !zuan.equals("0")) {
				adaptionSizeTextViewQuan.setVisibility(View.VISIBLE);
				adaptionSizeTextViewQuan.setText("本商品"+zuan.replace("预估","")+"元");
			} else {
				adaptionSizeTextViewQuan.setVisibility(View.INVISIBLE);
			}
			int drawS = getResources().getIdentifier(jumpdomain,"mipmap", getPackageName());
			imageView.setImageResource(drawS);
			Glide.with(context).load(R.drawable.tuiguang_d05).into(imgLoading);
			img_close.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					updataDialog.dismiss();
					cancleJump = false;
					finish();
				}
			});
		}
	}

}
