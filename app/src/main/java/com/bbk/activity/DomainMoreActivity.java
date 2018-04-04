package com.bbk.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bbk.adapter.DomainMoreListAdapter;
import com.bbk.adapter.DomainMoreListAdapter.setMyOnClickListener;
import com.bbk.adapter.DomainMoreListAdapter2;
import com.bbk.adapter.DomainMoreListAdapter2.setMyOnClickListener2;
import com.bbk.flow.DataFlow;
import com.bbk.flow.DataFlow3;
import com.bbk.flow.ResultEvent;
import com.bbk.util.BaseTools;
import com.bbk.util.SharedPreferencesUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class DomainMoreActivity extends BaseActivity implements ResultEvent,OnClickListener{
	private int currentIndex = 0;
	private HorizontalScrollView sbox;
	private LinearLayout mbox;
	private DataFlow dataFlow;
	private DataFlow3 dataFlow1;
	private List<Map<String, String>> list;
	private List<Map<String, String>> list1;
	private DomainMoreListAdapter adapter;
	private ListView mlistview;
	private ListView mlistview1;
	private DomainMoreListAdapter2 adapter1;
	private String token;
	private ImageView topbar_goback_btn;
	private LinearLayout msearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_domain_more);
		token = SharedPreferencesUtil.getSharedData(DomainMoreActivity.this, "userInfor", "token");
		dataFlow = new DataFlow(this);
		dataFlow1 = new DataFlow3(this);
		initView();
		initData();
	}
	
	
	
	private void initData() {
		Map<String, String> paramsMap = new HashMap<String, String>();
		
		paramsMap.put("token",token);
		dataFlow.requestData(1, "newService/queryDomainUrlMorenByToken", paramsMap, this);
	}
	private void getHttpdata(String type) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("type",type);
		paramsMap.put("token",token);
		dataFlow.requestData(2, "newService/queryDomainUrlMorenByType", paramsMap, this);
	}


	private void initView() {
		list = new ArrayList<>();
		list1 = new ArrayList<>();
		
		topbar_goback_btn = (ImageView) findViewById(R.id.topbar_goback_btn);
		sbox = (HorizontalScrollView) findViewById(R.id.sbox);
		mbox = (LinearLayout) findViewById(R.id.mbox);
		msearch = (LinearLayout) findViewById(R.id.msearch);
		mlistview = (ListView) findViewById(R.id.mlistview);
		mlistview1 = (ListView) findViewById(R.id.mlistview1);
		topbar_goback_btn.setOnClickListener(this);
		msearch.setOnClickListener(this);
	}
	private void insertGZ(String id){
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("token",token);
		paramsMap.put("gzid",id);
		dataFlow.requestData(3, "newService/insertGZ", paramsMap, this);
	}
	private void deleteGZ(String id){
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("token",token);
		paramsMap.put("gzid",id);
		dataFlow.requestData(3, "newService/deleteGZ", paramsMap, this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topbar_goback_btn:
			finish();
			break;
		case R.id.msearch:
			Intent intent = new Intent(this, SearchMainActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
	//一级菜单一
		private void addtitle(final String text, final int i) {
			LayoutInflater inflater = LayoutInflater.from(DomainMoreActivity.this);
			View view = inflater.inflate(R.layout.super_item_title, null);
			final TextView title = (TextView) view.findViewById(R.id.item_title);
			final View henggang = view.findViewById(R.id.bottom_view);
			title.setText(text);
			title.setTextColor(Color.parseColor("#666666"));
			henggang.setBackgroundColor(Color.parseColor("#ffffff"));
			view.setPadding(BaseTools.getPixelsFromDp(this, 10), 0, BaseTools.getPixelsFromDp(this, 10), 0);
			if (i == 0) {
				title.setTextColor(Color.parseColor("#ff7d41"));
				henggang.setBackgroundColor(Color.parseColor("#ff7d41"));
			}
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					if (i!= currentIndex) {
						 
						View view2 = mbox.getChildAt(i);
						TextView title1 = (TextView) view2.findViewById(R.id.item_title);
						View henggang1 = view2.findViewById(R.id.bottom_view);
						title1.setTextColor(Color.parseColor("#ff7d41"));
						henggang1.setBackgroundColor(Color.parseColor("#ff7d41"));
						
						View view4 = mbox.getChildAt(currentIndex);
						TextView title3 = (TextView) view4.findViewById(R.id.item_title);
						View henggang3 = view4.findViewById(R.id.bottom_view);
						title3.setTextColor(Color.parseColor("#666666"));
						henggang3.setBackgroundColor(Color.parseColor("#ffffff"));
						
						currentIndex = i;
						if (i==0) {
							mlistview.setVisibility(View.VISIBLE);
							mlistview1.setVisibility(View.GONE);
						}else{
							getHttpdata(text);
							mlistview.setVisibility(View.GONE);
							mlistview1.setVisibility(View.VISIBLE);
						}
	
					}
				}


			});
			mbox.addView(view);
		}

	private void loadList(JSONArray arr, List<Map<String, String>> list12){
		for (int i = 0; i < arr.length(); i++) {
			JSONObject jsonObject;
			try {
				jsonObject = arr.getJSONObject(i);
			Map<String, String> map = new HashMap<>();
			String id = jsonObject.optString("id");
			String img = jsonObject.optString("img");
			String name1 = jsonObject.optString("name1");
			String name2 = jsonObject.optString("name2");
			String url = jsonObject.optString("url");
			String isgz = jsonObject.optString("isgz");
			map.put("isgz", isgz);
			map.put("id", id);
			map.put("img", img);
			map.put("name1", name1);
			map.put("name2", name2);
			map.put("url", url);
			list12.add(map);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
		switch (requestCode) {
		case 1:
			try {
				JSONObject object = new JSONObject(content);
				JSONArray typelist = object.getJSONArray("typelist");
				addtitle("我的关注", 0);
				for (int i = 0; i < typelist.length(); i++) {
					JSONObject object2 = typelist.getJSONObject(i);
					String name = object2.optString("name");
					addtitle(name, i+1);
				}
				JSONArray morenlist = object.getJSONArray("morenlist");
				loadList(morenlist, list);
				adapter = new DomainMoreListAdapter(list, this);
				mlistview.setAdapter(adapter);
				mlistview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						String url = list.get(arg2).get("url");
						Intent intent = new Intent(DomainMoreActivity.this,WebViewActivity.class);
						intent.putExtra("url", url);
						startActivity(intent);
					}
				});
				adapter.setOnClickListener(new setMyOnClickListener() {
					
					@Override
					public void onClick(int positon) {
						deleteGZ(list.get(positon).get("id"));
						list.remove(positon);
						adapter.notifyDataSetChanged();
					}
				});
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 2:
			try {
				list1.clear();
				JSONArray arr = new JSONArray(content);
				loadList(arr,list1);
				adapter1 = new DomainMoreListAdapter2(list1, this);
				mlistview1.setAdapter(adapter1);
				mlistview1.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						String url = list1.get(arg2).get("url");
						Intent intent = new Intent(DomainMoreActivity.this,WebViewActivity.class);
						intent.putExtra("url", url);
						startActivity(intent);
					}
				});
				adapter1.setOnClickListener(new setMyOnClickListener2() {

					@Override
					public void onClick(View v, int positon) {
						TextView cancle = (TextView) v.findViewById(R.id.cancle);
						if (cancle.getText().toString().equals("取消关注")) {
							cancle.setText("+ 关注");
							cancle.setTextColor(Color.parseColor("#ff7d41"));
							deleteGZ(list1.get(positon).get("id"));
							Map<String, String> map = list1.get(positon);
							map.put("isgz", "0");
							for (int i = 0; i < list.size(); i++) {
								String id = list.get(i).get("id");
								if (id.equals(map.get("id"))) {
									list.remove(i);
									break;
								}
							}
							adapter.notifyDataSetChanged();
						}else{
							cancle.setText("取消关注");
							cancle.setTextColor(Color.parseColor("#999999"));
							insertGZ(list1.get(positon).get("id"));
							Map<String, String> map = list1.get(positon);
							map.put("isgz", "1");
							list.add(map);
							adapter.notifyDataSetChanged();
							
						}
					}
					
				});
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}



}
