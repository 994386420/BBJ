package com.bbk.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bbk.adapter.BrandNavAdapter;
import com.bbk.util.BaseTools;
import com.bbk.view.FlowLayout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class ReslutBrandActivity extends BaseActivity {

	private String abcBrand;
	private String jsonObject;
	private String[] brandArr;
	private ListView mlistView;
	private FlowLayout brandsFl;
	private List<String> data;
	private List<Map<String, Object>> list;
	private int curposition = 0;
	private Button currentBrandButton=null;
	private String choseBrandName="";
	private List<List<String>> datas;
	private TextView okTv;
	private ImageButton cancelIb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_brands);
		jsonObject = getIntent().getStringExtra("jsonObject");
		abcBrand = getIntent().getStringExtra("abcBrand");
		brandArr = jsonObject.split("\\|");
		initView();
		initData();
		
	}

	private void initData() {
		for (int i = 0; i < 30; i++) {
			data.add(brandArr[i]);
		}
		datas.add(data);
		addBrandsToContainer(brandsFl, data);
		try {
			JSONObject object =new JSONObject(abcBrand);
			Iterator<String> keys = object.keys();
			while (keys.hasNext()) {
				String key = keys.next();
				JSONArray array = object.getJSONArray(key);
				List<String> lists = new ArrayList<>();
				for (int i = 0; i < array.length(); i++) {
					lists.add(array.optString(i));
				}
				datas.add(lists);
				Map<String, Object> map = new HashMap<>();
				map.put("select", "no");
				map.put("brand", key);
				list.add(map);
			}
			final BrandNavAdapter adapter =new BrandNavAdapter(this, list);
			mlistView.setAdapter(adapter);
			mlistView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					if (curposition!= position) {
						Map<String, Object> map2 = list.get(position);
						Map<String, Object> map3 = list.get(curposition);
						map2.put("select", "yes");
						map3.put("select", "no");
						adapter.notifyDataSetChanged();
						brandsFl.removeAllViews();
						addBrandsToContainer(brandsFl, datas.get(position));
						curposition=position;
					}
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(ReslutBrandActivity.this, ResultMainActivity.class);
		intent.putExtra("choseBrandName", "");
		setResult(1, intent); 
		finish();
		super.onBackPressed();
		
	}
	private void initView() {
		data = new ArrayList<>();
		list = new ArrayList<>();
		datas = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put("select", "yes");
		map.put("brand", "热门品牌");
		list.add(map);
		mlistView = (ListView) findViewById(R.id.mlistView);
		brandsFl = (FlowLayout) findViewById(R.id.brandsFl);
		okTv = (TextView) findViewById(R.id.okTv);
		okTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ReslutBrandActivity.this, ResultMainActivity.class);
				intent.putExtra("choseBrandName", choseBrandName);
				setResult(1, intent); 
				finish();
			}
		});
		cancelIb = (ImageButton) findViewById(R.id.cancelIb);
		cancelIb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ReslutBrandActivity.this, ResultMainActivity.class);
				intent.putExtra("choseBrandName", "");
				setResult(1, intent); 
				finish();
			}
		});
		
	}
	/**
	 * 增加List字符串数到Button并加入到对应容器,并为Button设置监听.
	 * 
	 * @param fl
	 * @param data
	 */
	private void addBrandsToContainer(final FlowLayout fl, List<String> data) {
		if (data == null || data.size() == 0) {
			return;
		}
		for (String brandName : data) {
			final Button brandBt = new Button(ReslutBrandActivity.this);
			brandBt.setText(brandName);
//			int min = 0xFF000000;
//			int max = 0xFFEEEEEE;
			// int randomColorInt = (int) (Math.random() * (max - min) + min);
			// int colorInt = 0xFFFFFFFF;
			// brandBt.setBackgroundColor(colorInt);
			brandBt.setBackgroundResource(R.drawable.bg_allbrand_normal);
			ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(BaseTools.getPixelsFromDp(this, 5), 0,BaseTools.getPixelsFromDp(this, 5), 0);
			// 设置被选中样式，以及将选中结果保存起来
			brandBt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (currentBrandButton == brandBt) {
								brandBt.setBackgroundResource(R.drawable.bg_allbrand_normal);
								currentBrandButton = null;
								// 取消选择
								choseBrandName = "";
								return;
							}
							// 清除之前被选中品牌,选中当前品牌
							if (currentBrandButton != null)
								currentBrandButton.setBackgroundResource(R.drawable.bg_allbrand_normal);
							brandBt.setBackgroundResource(R.drawable.bg_allbrand_selected);
							// 保存选择数据
							choseBrandName = brandBt.getText().toString();
							currentBrandButton = brandBt;
						}
					});
				}
			});
			fl.addView(brandBt,lp);
		}
	}
}
