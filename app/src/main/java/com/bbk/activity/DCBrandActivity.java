package com.bbk.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bumptech.glide.Glide;

public class DCBrandActivity extends BaseFragmentActivity implements
		OnClickListener,ResultEvent {
	private DataFlow dataFlow;
	private static String currentProductType;// 当前请求到的类型
	private String producttype;
	private static JSONObject dataJoTotal = null;
	private LinearLayout leftCategoryListLayout;
	private String mAddtion = "";
	private int mIndex;
	private ListView brandList;
	private SimpleAdapter brandListAdapter;
	private List<Map<String, Object>> brandListData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dataFlow = new DataFlow(this);
		Glide.get(this).clearMemory();
		setContentView(R.layout.activity_data_count_brand);
		initView();
		initData();
	}

	public void initView() {
		ImageButton goBackBtn = (ImageButton) findViewById(R.id.topbar_goback_btn);
		goBackBtn.setOnClickListener(this);
		leftCategoryListLayout = (LinearLayout) findViewById(R.id.left_brand_list_layout);
		
		brandList = (ListView) findViewById(R.id.listview_brand);
		brandList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Map<String, Object> map = brandListData.get(position);
				Intent intent = new Intent();
				String brand = (String) map.get("brand");
				intent.putExtra("brand", brand);
				boolean hasSku = false;
				try {
					if(dataJoTotal.getInt("hasSku")>0){
						hasSku = true;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				intent.putExtra("hasSku", hasSku);
				setResult(3, intent);
				DCBrandActivity.this.finish();
			}
		});
		brandListData = new ArrayList<>();
		brandListAdapter = new SimpleAdapter(this, brandListData, R.layout.item_data_count_sku_or_brand, new String[]{"brand"}, new int[]{R.id.textView});
		brandList.setAdapter(brandListAdapter);
	}

	public void initData() {
		producttype = getIntent().getStringExtra("producttype");
		if (dataJoTotal == null || currentProductType == null || !currentProductType.equals(producttype)) {
			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("type", "3");
			paramsMap.put("producttype", producttype);
			dataFlow.requestData(0, "bdataService/getInitInfo", paramsMap, this);
		}else{
			onResultData(11, null, null, null);
		}
	}
	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo,
			String content) {
		if(requestCode == 0){
			try {
				dataJoTotal = new JSONObject(content);
				currentProductType = producttype;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		try {
			initLeftCategoryList();
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(DCBrandActivity.this, "亲，网络出问题了，请稍后再试！",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void initLeftCategoryList() throws JSONException {
		
		if (dataJoTotal == null) { 
			return; 
		}
		 
		Iterator<String> keys = dataJoTotal.getJSONObject("list").keys();
		List<String> list = new ArrayList<String>();
		while(keys.hasNext()){
			list.add(keys.next().toString());
		}
		
		Object[] ks = list.toArray(new Object[]{});
		Arrays.sort(ks);
		for (int i = 0; i < ks.length; i++) {
			final String name = ks[i].toString();
			View itemView = inflate(R.layout.item_left_brand_or_sku);
			TextView itemTitle = (TextView) itemView
					.findViewById(R.id.item_title);
			itemTitle.setText(name);
			itemView.setTag(i);
			itemView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(final View v) {
					if (!mAddtion.equals(name)) {
						clickedStyle(v);
						normalStyle(leftCategoryListLayout.getChildAt(mIndex));
						brandListData.clear();
						mIndex = (int) v.getTag();
						try {
							JSONArray ja = dataJoTotal.getJSONObject("list").getJSONArray(name);
							for (int j = 0; j < ja.length(); j++) {
								JSONObject jo = ja.getJSONObject(j);
								Map<String,Object> map = new HashMap<>();
								map.put("brand", jo.getString("brand"));
//								map.put("brandImg", jo.getString("brandimg"));
								brandListData.add(map);
							}
							brandListAdapter.notifyDataSetChanged();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
			});
			leftCategoryListLayout.addView(itemView);
			if(i==0){
				itemView.performClick();
			}
		}
	}

	public View inflate(int id) {
		return getLayoutInflater().inflate(id, null, false);
	}

	public void clickedStyle(View v) {
		TextView itemTitle = (TextView) v.findViewById(R.id.item_title);
		itemTitle.setTextColor(Color.parseColor("#0098FF"));
		v.setBackgroundColor(Color.parseColor("#F5F5F5"));
	}

	public void normalStyle(View v) {
		TextView itemTitle = (TextView) v.findViewById(R.id.item_title);
		itemTitle.setTextColor(Color.parseColor("#2D2D2D"));
		v.setBackgroundColor(Color.parseColor("#FFFFFF"));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topbar_goback_btn:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
