package com.bbk.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bbk.dao.SearchHistoryDao;
import com.bbk.util.HttpUtil;

public class SearchPromptActivity extends BaseActivity implements OnItemClickListener {

//	private String keyword = "";
	private ListView promptListView;
	private SimpleAdapter mAdapter;
	private ArrayList<HashMap<String, Object>> itemList = new ArrayList<HashMap<String, Object>>();
	
	private SearchHistoryDao dao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_prompt);
//		MyApplication.getInstance().addActivity(this);
		
		dao = new SearchHistoryDao(this);
		initView();
		initData();
	}

	/**
	 * 
	 */
	private void initView() {
		promptListView = (ListView) findViewById(R.id.search_prompt_list);
		promptListView.setOnItemClickListener(this);
	}

	/**
	 * 
	 */
	private void initData() {
		initListData();
//		getHttpData(keyword, 1);
	}

	private void initListData() {
		mAdapter = new SimpleAdapter(this, itemList,
				R.layout.listview_item_prompt, new String[] { "item_prompt" },
				new int[] { R.id.item_prompt });

		promptListView.setAdapter(mAdapter);
	}
	
	private void initListData(String str) {
		if(TextUtils.isEmpty(str) || "{}".equals(str) || "[]".equals(str)) {
			return;
		}
		
		try {
			JSONArray jsonArr = new JSONArray(str);
			int length = jsonArr.length();
			itemList.clear();
			mAdapter.notifyDataSetChanged();
			for(int i = 0; i < length; i ++) {
				JSONObject jsonObj = jsonArr.optJSONObject(i);
				HashMap<String, Object> itemMap = new HashMap<String, Object>();
				itemMap.put("item_prompt", jsonObj.optString("name"));
				itemList.add(itemMap);
			}
			mAdapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void getHttpData(String key, int what) {

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("keyword", key);
		
		HttpUtil.loadData(this,params, "http://125.67.237.37:8080/data-service/searchAutoService/searchAuto", mHandler, what);
	}
	
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			if(isFinishing()) {
				return;
			}
			
			String resultStr = msg.obj.toString();
			if(TextUtils.isEmpty(resultStr) || "{}".equals(resultStr) || "[]".equals(resultStr)) {
				return;
			}
			switch (msg.what) {
			case 1:
				initListData(resultStr);
				break;

			default:
				break;
			}
			
		}
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		map = (HashMap<String, Object>) parent.getItemAtPosition(position);
		String keyword = (String) map.get("item_prompt");
		
		if (!dao.exsistHistory(keyword)) {
			dao.addHistory(keyword);
		}
		if(ResultMainActivity.instance != null && !ResultMainActivity.instance.isFinishing()){
			ResultMainActivity.instance.finish();
		}
		finish();
		Intent intent = new Intent(this, ResultMainActivity.class);
		intent.putExtra("keyword", keyword);
		startActivity(intent);
	}
	
	@Override
	protected void onDestroy() {
		setContentView(R.layout.view_null);
		System.gc();
		super.onDestroy();
	}
}
