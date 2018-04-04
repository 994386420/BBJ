package com.bbk.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bbk.adapter.HomeFirstGridAdapter;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

public class RecomendActivity extends BaseActivity implements ResultEvent{
	private ImageButton topbar_goback_btn;
	private TextView topbar_title_iv;
	private GridView mgridView;
	private DataFlow dataFlow;
	private String maintype;
	private List<Map<String, String>> list;
	private HomeFirstGridAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recomend);
		dataFlow = new DataFlow(this);
		maintype = getIntent().getStringExtra("maintype");
		initView();
		initData();
	}

	private void initData() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("maintype", maintype);
		dataFlow.requestData(1, "newApp/getSuperMarketAllTypes", params, this);
	}

	private void initView() {
		list = new ArrayList<>();
		topbar_goback_btn = (ImageButton) findViewById(R.id.topbar_goback_btn);
		topbar_goback_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		topbar_title_iv = (TextView) findViewById(R.id.topbar_title_iv);
		mgridView = (GridView) findViewById(R.id.mgridView);
		if (maintype.equals("2")) {
			topbar_title_iv.setText("服饰");
			topbar_title_iv.setTextColor(Color.parseColor("#235fb0"));
		}else if(maintype.equals("3")){
			topbar_title_iv.setText("全球购");
			topbar_title_iv.setTextColor(Color.parseColor("#f92d5a"));
		}
	}

	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
		switch (requestCode) {
		case 1:
			try {
				JSONObject object = new JSONObject(content);
				final JSONArray array = object.getJSONArray("alltypes");
				Map<String,String> map = null;
				for (int i = 0; i < array.length(); i++) {
					JSONObject object2 = array.getJSONObject(i);
					String name = object2.optString("name");
					String img = object2.optString("img");
					map = new HashMap<>();
					map.put("text", name);
					map.put("imageUrl", img);
					list.add(map);
				}
				if (list!= null) {
					adapter = new HomeFirstGridAdapter(RecomendActivity.this,list);
					mgridView.setAdapter(adapter);
					mgridView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
							try {
								JSONObject jsonObject = array.getJSONObject(arg2);
								String keyword = jsonObject.optString("keyword");
								Intent intent = new Intent(RecomendActivity.this, ResultMainActivity.class);
		                    	intent.putExtra("keyword", keyword);
		                    	startActivity(intent);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
				}
				
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
