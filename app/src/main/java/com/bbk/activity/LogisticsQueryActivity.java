package com.bbk.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bbk.adapter.LogisticsQueryHistoryAdapter;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.MyListView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class LogisticsQueryActivity extends BaseActivity {
	private EditText medittext;
	private ImageView msearch;
	private LinearLayout mdelete;
	private String url = "https://m.kuaidi100.com/result.jsp";
	private List<Map<String, String>> list;
	private LogisticsQueryHistoryAdapter adapter;
	private MyListView mlistview;
	private RelativeLayout mbackground;
	private JSONArray array;
	private ImageButton topbar_goback_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logistics_query);
		initView();
		initData();
	}

	private void initView() {
		list = new ArrayList<>();
		
		mbackground = (RelativeLayout) findViewById(R.id.mbackground);
		WindowManager wm = this.getWindowManager();
	    int width = wm.getDefaultDisplay().getWidth();
	    LayoutParams params = (LayoutParams) mbackground.getLayoutParams();
	    params.height = (width*311)/700;
	    mbackground.setLayoutParams(params);
		medittext = (EditText) findViewById(R.id.medittext);
		msearch = (ImageView) findViewById(R.id.msearch);
		topbar_goback_btn = (ImageButton) findViewById(R.id.topbar_goback_btn);
		topbar_goback_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		mdelete = (LinearLayout) findViewById(R.id.mdelete);
		mlistview = (MyListView) findViewById(R.id.mlistview);
		adapter = new LogisticsQueryHistoryAdapter(list, this);
		mlistview.setAdapter(adapter);
		mlistview.setOnItemClickListener(new OnItemClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				try {
					JSONObject jsonObject = array.getJSONObject(arg2);
					array.remove(arg2);
					array.put(0, jsonObject);
					SharedPreferencesUtil.cleanShareData(MyApplication.getApplication(), "LogisticsQuery");
					SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "LogisticsQuery",
							"LogisticsQuery", array.toString());
					Map<String, String> map = list.get(arg2);
					list.remove(arg2);
					list.add(0, map);
					adapter.notifyDataSetChanged();
					url = "https://m.kuaidi100.com/result.jsp?nu="+jsonObject.optString("number");
					Intent intent = new Intent(LogisticsQueryActivity.this, WebViewActivity.class);
					intent.putExtra("url", url);
					startActivity(intent);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		msearch.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {
				try {
					String LogisticsQuery = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),
							"LogisticsQuery", "LogisticsQuery");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
					String now = sdf.format(new Date());
					String time = now.substring(5, 10);
					JSONObject object = new JSONObject();
					object.put("number", medittext.getText().toString());
					object.put("time", time);
					if (!LogisticsQuery.isEmpty()) {
						array = new JSONArray(LogisticsQuery);
					}else{
						array = new JSONArray();
					}
					array.put(object);
					if (array.length()>10) {
						array.remove(0);
					}
					SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "LogisticsQuery",
							"LogisticsQuery", array.toString());
					Map<String, String> map = new HashMap<>();
					map.put("number", medittext.getText().toString());
					map.put("time", time);
					list.add(map);
					adapter.notifyDataSetChanged();
					mdelete.setVisibility(View.VISIBLE);
					url = "https://m.kuaidi100.com/result.jsp?nu="+medittext.getText().toString();
					Intent intent = new Intent(LogisticsQueryActivity.this, WebViewActivity.class);
					intent.putExtra("url", url);
					startActivity(intent);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		mdelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SharedPreferencesUtil.cleanShareData(MyApplication.getApplication(), "LogisticsQuery");
				list.clear();
				adapter.notifyDataSetChanged();
				mdelete.setVisibility(View.GONE);
			}
		});
	}

	private void initData() {
		String LogisticsQuery = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),
				"LogisticsQuery", "LogisticsQuery");
		if (!LogisticsQuery.isEmpty()) {
			try {
				array = new JSONArray(LogisticsQuery);
				for (int i = 0; i < array.length(); i++) {
					Map<String, String> map = new HashMap<>();
					JSONObject object = array.getJSONObject(i);
					String number = object.optString("number");
					String time = object.optString("time");
					map.put("number", number);
					map.put("time", time);
					list.add(map);
				}
				adapter.notifyDataSetChanged();
				mdelete.setVisibility(View.VISIBLE);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			mdelete.setVisibility(View.GONE);
		}
	}
}
