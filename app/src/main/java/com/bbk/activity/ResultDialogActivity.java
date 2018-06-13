package com.bbk.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.NewFxBean;
import com.bbk.adapter.FindListAdapter;
import com.bbk.adapter.ResultDialogAdapter1;
import com.bbk.adapter.ResultDialogAdapter2;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.flow.DataFlow3;
import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.resource.Constants;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.HttpUtil;
import com.bbk.util.JumpIntentUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.MyListView;
import com.tencent.qcloud.sdk.Constant;
import com.zyao89.view.zloading.ZLoadingView;
import com.zyao89.view.zloading.Z_TYPE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class ResultDialogActivity extends BaseActivity {
	private MyListView mlistview1,mlistview2;
	private ResultDialogAdapter1 adapter1;
	private ResultDialogAdapter2 adapter2;
	private List<Map<String, Object>> list1,list2,list;
	private ImageView mclose;
	private LinearLayout wantdomain;
	private View henggang;
	private RelativeLayout msize;
	private Thread thread;
	private boolean isrequest = true;
	private int requestnum = 0;
	private int removenum = 0;
	private String keyword,rowkey;
	private boolean isrun = true;
	private DataFlow3 dataFlow;
	private ZLoadingView mloadingView;
	private ScrollView sll_bijia;//比价弹窗布局
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result_dialog);
		dataFlow = new DataFlow3(this);
		keyword = getIntent().getStringExtra("keyword");
		rowkey = getIntent().getStringExtra("rowkey");
		initView();
		getBijiaArr();
	}

	private void initlist(JSONArray array) {
		list = new ArrayList<>();
		list1 = new ArrayList<>();
		list2 = new ArrayList<>();
		try {
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				if (object.optString("price").isEmpty()) {
					Map<String, Object> map = new HashMap<>();
					map.put("domain", object.optString("domain"));
					map.put("domainCh", object.optString("domainCh"));
					map.put("url", object.optString("url"));
					list2.add(map);
				}else{
					Map<String, Object> map = new HashMap<>();
					map.put("title", object.optString("title"));
					map.put("price", object.optString("price"));
					map.put("domain", object.optString("domain"));
					map.put("domainCh", object.optString("domainCh"));
					map.put("url", object.optString("url"));
					if (object.has("purl")) {
						map.put("purl", object.optString("purl"));
					}else{
						map.put("purl",  "0");
					}
					
					map.put("groupRowKey", object.optString("rowkey"));
					list1.add(map);
					list.add(map);
				}
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void getBijiaArr() {
		HashMap<String, String> paramsMap = new HashMap<>();
		paramsMap.put("rowkey",rowkey);
		RetrofitClient.getInstance(this).createBaseApi().getBijiaArr(
				paramsMap, new BaseObserver<String>(this) {
					@Override
					public void onNext(String s) {
						try {
							JSONObject jsonObject = new JSONObject(s);
							String content = jsonObject.optString("content");
							if (jsonObject.optString("status").equals("1")) {
								JSONArray array = new JSONArray(content);
								initlist(array);
								if (list2.isEmpty()) {
									wantdomain.setVisibility(View.GONE);
									henggang.setVisibility(View.GONE);
								}
								mclose = (ImageView) findViewById(R.id.mclose);
								mclose.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View arg0) {

										finish();
									}
								});
								adapter1 = new ResultDialogAdapter1(list1, ResultDialogActivity.this);
								adapter2 = new ResultDialogAdapter2(list2, ResultDialogActivity.this);
								mlistview1.setAdapter(adapter1);
								mlistview2.setAdapter(adapter2);

								mlistview1.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
										Map<String, Object> map = list1.get(arg2);
										final String url = map.get("url").toString();
										final String title = map.get("title").toString();
										final String domain1 = map.get("domain").toString();
										final String groupRowKey = map.get("groupRowKey").toString();
										Intent intent;
										if (JumpIntentUtil.isJump(list1,arg2,"domain")) {
											intent = new Intent(ResultDialogActivity.this,IntentActivity.class);
											intent.putExtra("title", title);
											intent.putExtra("domain", domain1);
											intent.putExtra("url", url);
											intent.putExtra("groupRowKey", groupRowKey);
										}else{
											intent = new Intent(ResultDialogActivity.this,WebViewActivity.class);
											if (null!= getIntent().getStringExtra("isweb")) {
												WebViewActivity.instance.finish();
											}
											intent.putExtra("url", url);
											intent.putExtra("groupRowKey", groupRowKey);
										}
										startActivity(intent);
										finish();
									}
								});
								mlistview2.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
										Map<String, Object> map = list2.get(arg2);
										final String url = map.get("url").toString();
										Intent intent = new Intent(ResultDialogActivity.this,WebViewActivity.class);
										if (null!= getIntent().getStringExtra("isweb")) {
											WebViewActivity.instance.finish();
										}
										intent.putExtra("url", url);
										startActivity(intent);
										finish();
									}
								});
								if (thread == null) {
									NowPrice();
								}
								mloadingView.setVisibility(View.GONE);
								sll_bijia.setVisibility(View.VISIBLE);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					@Override
					protected void hideDialog() {
					}

					@Override
					protected void showDialog(){
					}

					@Override
					public void onError(ExceptionHandle.ResponeThrowable e) {
						StringUtil.showToast(ResultDialogActivity.this, e.message);
					}
				});
	}

	private void initView() {
		mloadingView = findViewById(R.id.loading_view);
		sll_bijia = findViewById(R.id.bijia_scrollview);
		mloadingView.setLoadingBuilder(Z_TYPE.DOUBLE_CIRCLE,0.5);
		mlistview1 = (MyListView) findViewById(R.id.mlistview1);
		mlistview2 = (MyListView) findViewById(R.id.mlistview2);
		wantdomain = (LinearLayout) findViewById(R.id.wantdomain);
		msize =  findViewById(R.id.msize);
		WindowManager wm = this.getWindowManager();
	    int height = wm.getDefaultDisplay().getHeight();
	    LayoutParams params = msize.getLayoutParams();
	    params.height = (int) (height*0.8);
	    msize.setLayoutParams(params);
		henggang = findViewById(R.id.henggang);
	}
	private void NowPrice(){
    	thread = new Thread(new Runnable() {

			public void run() {
				while (isrun) {
					if (isrequest == true) {
						try {
							Map<String, String> params = new HashMap<>();
							if (!"0".equals(list.get(requestnum).get("purl"))) {
								String str;
								String content;
								params.put("domain", list.get(requestnum).get("domain").toString());
								params.put("rowkey", list.get(requestnum).get("groupRowKey").toString());
								params.put("fromwhere", "android"+keyword);
								if (list.get(requestnum).get("purl").toString().contains("||")) {
									String url = list.get(requestnum).get("purl").toString();
									String[] split = url.split("\\|\\|");
									String referrer=split[1];
									content = HttpUtil.getHttp1(params, split[0], ResultDialogActivity.this, referrer);
									params.put("pcontent", content);
									str = HttpUtil.getHttp(params, Constants.MAIN_BASE_URL_MOBILE+"checkService/checkProduct", ResultDialogActivity.this);
								}else{
									content = HttpUtil.getHttp1(params, list.get(requestnum).get("purl").toString(), ResultDialogActivity.this,null);
									params.put("pcontent", content);
									str = HttpUtil.getHttp(params, Constants.MAIN_BASE_URL_MOBILE+"checkService/checkProduct", ResultDialogActivity.this);
								}
								JSONObject object = new JSONObject(str);
								if ("3".equals(object.optString("type"))) {
									if ("".equals(object.optString("url"))) {
										content = HttpUtil.getHttp1(params, list.get(requestnum).get("url").toString(), ResultDialogActivity.this,null);
									}else{
										content = HttpUtil.getHttp1(params, object.optString("url"), ResultDialogActivity.this,null);
									}
									params.put("pcontent", content);
									String url = Constants.MAIN_BASE_URL_MOBILE+"checkService/checkProduct";
									str = HttpUtil.getHttp(params, url, ResultDialogActivity.this);
								}
								Message mes = handler.obtainMessage();
								mes.obj = str;
								mes.arg1 = requestnum;
								mes.what =0;
								handler.sendMessage(mes);
							}
							if (requestnum+1 >= list.size()) {
								isrequest = false;
							}
							requestnum++;
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		});
		thread.start();
    }
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				String str = msg.obj.toString();
				int i = msg.arg1;
				try {
					JSONObject object = new JSONObject(str);
					switch (object.optString("type")) {
					case "0":
						list1.remove(i-removenum);
						adapter1.notifyDataSetChanged();
						removenum++;
						break;
					case "1":
						String price = object.optString("price");
						list1.get(i-removenum).put("price", price);
						adapter1.notifyDataSetChanged();
						break;
					default:
						break;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
	@Override
	protected void onDestroy() {
		handler.removeCallbacks(thread);
		isrun = false;
		super.onDestroy();

	}
}
