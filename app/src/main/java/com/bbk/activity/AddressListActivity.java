package com.bbk.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bbk.server.ActivityManager;
import com.bbk.util.AppJsonFileReader;
import com.bbk.util.SharedPreferencesUtil;

public class AddressListActivity extends BaseActivity implements OnClickListener, OnItemClickListener {

	private ImageButton goBackBtn; //AddSendAddressActivity
	private ListView addressListView;
	private ArrayList<HashMap<String, Object>> itemList;
	private SimpleAdapter listViewAdapter;
	
	private String index = "";
	private String province = "";
	private String city = "";
	private String area = "";
	private String sub = "";
	private String aa = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.address_list_layout);

//		MyApplication.getInstance().addActivity(this);
		
		ActivityManager.getInstance().pushActivity(this);

		initView();
		initData();
	}

	public void initView() {
		goBackBtn = $(R.id.topbar_goback_btn);
		goBackBtn.setOnClickListener(this);
		
		addressListView = $(R.id.address_listview);
		addressListView.setOnItemClickListener(this);
	}

	public void initData() {
		Intent intent = getIntent();
		index = intent.getStringExtra("index");
		province = intent.getStringExtra("province");
		city = intent.getStringExtra("city");
		sub = intent.getStringExtra("sub");
		
		aa = intent.getStringExtra("aa");
		
		initListView();
		loadListViewData();
	}
	
//	String jsonStr = AppJsonFileReader.getJson(getBaseContext(), "productType.json");
//	productTypeJSONArray = new JSONArray();
//	List<Map<String, String>> dataList = AppJsonFileReader.setListData(jsonStr);
	
	public void initListView() {
		itemList = new ArrayList<HashMap<String, Object>>();
		listViewAdapter = new SimpleAdapter(this, itemList,
				R.layout.listview_item_address, 
				new String[] { "item_title" }, new int[] { R.id.item_title });
		addressListView.setAdapter(listViewAdapter);
	}
	
	public void loadListViewData() {
		String jsonStr = "";
		if("1".equals(index)) {
			jsonStr = AppJsonFileReader.getJson(getBaseContext(), "address.json");
		} else {
			jsonStr = sub;
		}
		try {
			JSONArray addressJsonArr = new JSONArray(jsonStr);
			int length = addressJsonArr.length();
			for(int i = 0; i < length; i ++) {
				JSONObject  jsonObject = addressJsonArr.optJSONObject(i);
				String address = jsonObject.optString("name");
				
				if("请选择".equals(address) || "其他".equals(address)) {
					continue;
				}
				
				HashMap<String, Object> itemMap = new HashMap<String, Object>();
				itemMap.put("item_title", address);
				
				String sub = jsonObject.optString("sub");
				itemMap.put("sub", sub);
				
				itemList.add(itemMap);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		listViewAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topbar_goback_btn:
			ActivityManager.getInstance().popActivity();
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map = (HashMap<String, Object>) parent.getItemAtPosition(position);
		
		if("1".equals(index)) {
			
			Intent intent = new Intent(this, AddressListActivity.class);
			intent.putExtra("sub", (String) map.get("sub"));
			intent.putExtra("province", (String) map.get("item_title"));
			intent.putExtra("aa", aa);
			intent.putExtra("index","2");
			
			startActivity(intent);
		}
		if("2".equals(index)) {
			city = map.get("item_title").toString();
			String sub = map.get("sub").toString();
	        
	        if("addSendAddress".equals(aa)) {
	        	
	        	if(TextUtils.isEmpty(sub)) {
	        		SharedPreferencesUtil.putSharedData(getApplicationContext(), "sendAddress", "city", province);
	            	SharedPreferencesUtil.putSharedData(getApplicationContext(), "sendAddress", "area", city);
	            	ActivityManager.getInstance().popActivity();
	            	ActivityManager.getInstance().popActivity();
	        	} else {
	        		Intent intent = new Intent(this, AddressListActivity.class);
	        		intent.putExtra("sub", (String) map.get("sub"));
	        		intent.putExtra("province", province);
	        		intent.putExtra("city", city);
	        		intent.putExtra("index","3");
	        		startActivity(intent);
	        	}
	        	
	        } else {
	        	SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInforAddress", "province", province);
	        	SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInforAddress", "city", city);
	        	ActivityManager.getInstance().popActivity();
	        	ActivityManager.getInstance().popActivity();
	        }
			
		}
		
		if("3".equals(index)) {
			area = map.get("item_title").toString();
			SharedPreferencesUtil.putSharedData(getApplicationContext(), "sendAddress", "province", province);
        	SharedPreferencesUtil.putSharedData(getApplicationContext(), "sendAddress", "city", city);
        	SharedPreferencesUtil.putSharedData(getApplicationContext(), "sendAddress", "area", area);
        	ActivityManager.getInstance().popActivity();
        	ActivityManager.getInstance().popActivity();
        	ActivityManager.getInstance().popActivity();
			
		}
	}
	
	@Override
	protected void onDestroy() {
		setContentView(R.layout.view_null);
		System.gc();
		super.onDestroy();
	}

}
