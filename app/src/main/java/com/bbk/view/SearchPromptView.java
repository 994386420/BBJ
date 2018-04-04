package com.bbk.view;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bbk.activity.R;
import com.bbk.activity.ResultMainActivity;
import com.bbk.dao.SearchHistoryDao;
import com.bbk.util.HttpUtil;

public class SearchPromptView extends LinearLayout {
	
	private Context mContext;
	private SearchHistoryDao dao;
	
	private ListView promptListView;
	private SimpleAdapter mAdapter;
	private ArrayList<HashMap<String, Object>> itemList = new ArrayList<HashMap<String, Object>>();

	public SearchPromptView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mContext = context;
		dao = new SearchHistoryDao(context);
		
		View mView = inflate(context, R.layout.view_search_prompt);
		
		promptListView = (ListView) mView.findViewById(R.id.search_prompt_list);
		
		promptListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map = (HashMap<String, Object>) parent.getItemAtPosition(position);
				String keyword = (String) map.get("item_prompt");
				
				if (!dao.exsistHistory(keyword)) {
					dao.addHistory(keyword);
				}
				
				Intent intent = new Intent(mContext, ResultMainActivity.class);
				intent.putExtra("keyword", keyword);
				mContext.startActivity(intent);
			}
		});
		
		initListData();
	}
	
	public View inflate(Context context, int id) {
		return View.inflate(context, id, this);
	}
	
	private void initListData() {
		mAdapter = new SimpleAdapter(mContext, itemList,
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
		
		itemList.clear();
		mAdapter.notifyDataSetChanged();

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("keyword", key);
		
		HttpUtil.loadData(getContext(),params, "http://125.67.237.37:8080/data-service/searchAutoService/searchAuto", mHandler, what);
	}
	
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
//			if(mContext.isFinishing()) {
//				return;
//			}
			
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
	
}
