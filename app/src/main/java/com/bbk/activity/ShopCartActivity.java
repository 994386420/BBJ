package com.bbk.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bbk.adapter.ShopCartGridAdapter;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.util.ImmersedStatusbarUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;

public class ShopCartActivity extends BaseActivity implements ResultEvent{

	private GridView mgridview;
	private List<Map<String, String>> list;
	private DataFlow dataFlow;
	private ShopCartGridAdapter adapter;
	private ImageButton topbar_goback_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_cart);
		View topView = findViewById(R.id.topbar_layout);
		// 实现沉浸式状态栏
		ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
		dataFlow = new DataFlow(this);
		initView();
		initData();
	}

	private void initView() {
		list = new ArrayList<>();
		mgridview = (GridView)findViewById(R.id.mgridview);
		topbar_goback_btn = (ImageButton) findViewById(R.id.topbar_goback_btn);
		topbar_goback_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		adapter = new ShopCartGridAdapter(list, this);
		mgridview.setAdapter(adapter);
		mgridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent(ShopCartActivity.this, WebViewActivity.class);
				intent.putExtra("url", list.get(arg2).get("url"));
				startActivity(intent);
			}
		});
		
	}

	private void initData() {
		Map<String, String> params = new HashMap<>();
		dataFlow.requestData(1, "newService/queryGouwuche", params, this);
	}

	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
		try {
			JSONArray array = new JSONArray(content);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				Map<String, String> map = new HashMap<>();
				map.put("img", object.optString("img"));
				map.put("url", object.optString("url"));
				list.add(map);
			}
			adapter.notifyDataSetChanged();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
