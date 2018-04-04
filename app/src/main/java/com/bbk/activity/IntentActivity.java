package com.bbk.activity;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.alibaba.baichuan.android.trade.callback.AlibcLoginCallback;
import com.bbk.flow.DataFlow3;
import com.bbk.flow.ResultEvent;
import com.bbk.util.EventIdIntentUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class IntentActivity extends BaseActivity implements ResultEvent{

	private DataFlow3 dataFlow;
	private String title;
	private String url;
	private String domain;
	private ImageView img3;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intent);
		dataFlow = new DataFlow3(this);
		title = getIntent().getStringExtra("title");
		url = getIntent().getStringExtra("url");
		domain = getIntent().getStringExtra("domain");
		rowkey = getIntent().getStringExtra("groupRowKey");
		initView();
		initData();
	}

	private void initView() {
		
		img3 =(ImageView)findViewById(R.id.img3);
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
		int drawS = getResources().getIdentifier(domain,"mipmap", getPackageName());
		img3.setImageResource(drawS);
	}
	@Override
	protected void onDestroy() {
		handler.removeCallbacksAndMessages(null);  
		super.onDestroy();
		
	}
	private void initData() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("title", title);
		params.put("url", url);
		params.put("domain", domain);
		params.put("client", "andorid");
		dataFlow.requestData(1, "newApp/getJumpUrl", params, this);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(5000);
					if (isintent) {
						Intent intent = new Intent(IntentActivity.this,WebViewActivity.class);
						intent.putExtra("url", url);
						startActivity(intent);
						finish();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
		try {
			isintent = false;
			JSONObject obj = new JSONObject(content);
			String type = obj.optString("type");
			if (type.equals("0")) {
				intentbuy.setVisibility(View.VISIBLE);
				onclickthis.setVisibility(View.VISIBLE);
				url = obj.optString("url");
				if (!obj.optString("domainCh").isEmpty()) {
					String domainCh = obj.optString("domainCh");
					text1.setText("此商品已下架，将跳转至"+domainCh+"搜索页");
				}else{
					text1.setText("此商品已下架，将跳转至该商城搜索页");
				}	
			}else{
				url = obj.optString("url");
				if (obj.has("desc")){
					String desc = obj.optString("desc");
					if (!"".equals(desc)){
						text1.setText(desc);
						taobaoLogin(this,url);
					}else {
						handler.postDelayed(runa, 0);
					}
				}else {
					handler.postDelayed(runa, 0);
				}
//				Log.e("=============",url+"");

			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void  taobaoLogin(final Context context, final String url){
		if (AlibcLogin.getInstance().getSession()!= null){
			String nick = AlibcLogin.getInstance().getSession().nick;
			if (nick!= null && !"".equals(nick)) {
				handler.postDelayed(runa, 0);
			}else {
				AlibcLogin alibcLogin = AlibcLogin.getInstance();

				alibcLogin.showLogin((Activity) context, new AlibcLoginCallback() {

					@Override
					public void onSuccess() {
						Toast.makeText(context, "登录成功 ",
								Toast.LENGTH_LONG).show();
						handler.postDelayed(runa, 0);
					}
					@Override
					public void onFailure(int code, String msg) {
						Toast.makeText(context, "登录失败 ",
								Toast.LENGTH_LONG).show();
						handler.postDelayed(runa, 0);
					}
				});
		}

		}else {
			handler.postDelayed(runa, 0);
		}

	}
}
