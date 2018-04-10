package com.bbk.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bbk.adapter.DraftAdapter;
import com.bbk.adapter.GossipAdapter;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyDraftActivity extends BaseActivity implements OnClickListener {
	private TextView mcompile;
	private List<Map<String, String>> list,datalist;
	private ListView mlistview;
	private RelativeLayout mselectanddelete;
	private LinearLayout mallselect, mzhanwei;
	private ImageView mallselectimg;
	private RelativeLayout mdelete;
	private ImageButton topbar_goback_btn;
	private boolean iscompile = false;
	private DraftAdapter adapter;
	private boolean isallselect = false;
	private JSONArray array;
	private RelativeLayout listviewbox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_draft);
		View topView = findViewById(R.id.topbar_layout);
		// 实现沉浸式状态栏
		ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
		initView();
		initData();
	}

	private void initView() {
		list = new ArrayList<>();
		datalist = new ArrayList<>();

		mlistview = (ListView) findViewById(R.id.mdraftlistview);
		mcompile = (TextView) findViewById(R.id.mcompile);
		mselectanddelete = (RelativeLayout) findViewById(R.id.mselectanddelete);
		mdelete = (RelativeLayout) findViewById(R.id.mdelete);
		mallselect = (LinearLayout) findViewById(R.id.mallselect);
		listviewbox = (RelativeLayout) findViewById(R.id.listviewbox);
		mzhanwei = (LinearLayout) findViewById(R.id.mzhanwei);
		mallselectimg = (ImageView) findViewById(R.id.mallselectimg);
		topbar_goback_btn = (ImageButton) findViewById(R.id.topbar_goback_btn);

		mcompile.setOnClickListener(this);
		mdelete.setOnClickListener(this);
		mallselect.setOnClickListener(this);
		topbar_goback_btn.setOnClickListener(this);
		

	}

	private void initData() {
		list.clear();
		String gossip = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "gossip", "gossip");
		try {
			if (!TextUtils.isEmpty(gossip)) {
				array = new JSONArray(gossip);
				if (array.length() > 0) {
					for (int i = array.length()-1; i >= 0; i--) {
						JSONObject object = array.getJSONObject(i);
						Map<String, String> map = new HashMap<>();
						map.put("content", object.optString("content"));
						map.put("title", object.optString("title"));
						map.put("dtime", object.optString("dtime"));
						map.put("url", object.optString("url"));
						if (object.has("vidioimg")){
							map.put("vidioimg", object.optString("vidioimg"));
						}else {
							map.put("vidioimg", object.optString("-1"));
						}
						map.put("typeCh","草稿");
						map.put("type", "2");
						map.put("isselect", "0");
						map.put("isbianji","0");
						map.put("imgs", object.getJSONArray("path").toString());
						list.add(map);
					}
					adapter = new DraftAdapter(list,this);
					mlistview.setAdapter(adapter);
					mlistview.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
							if (!iscompile) {
								Intent intent;
								Map<String, String> map = list.get(position);
								intent = new Intent(MyDraftActivity.this, MyGossipActivity.class);
								intent.putExtra("content", map.get("content"));
								intent.putExtra("title", map.get("title"));
								intent.putExtra("dtime", map.get("dtime"));
								intent.putExtra("imgs", map.get("imgs"));
								intent.putExtra("url", map.get("url"));
								intent.putExtra("position", position+"");
								startActivity(intent);
							}else{
								String str = list.get(position).get("isselect");
								if (str.equals("1")) {
									list.get(position).put("isselect", "0");
								}else{
									list.get(position).put("isselect", "1");
								}
								adapter.notifyDataSetChanged();
							}
						}
					});
					listviewbox.setVisibility(View.VISIBLE);
				}else {
					mzhanwei.setVisibility(View.VISIBLE);
					listviewbox.setVisibility(View.GONE);
					mcompile.setVisibility(View.GONE);
					mselectanddelete.setVisibility(View.GONE);
				}

			} else {
				mzhanwei.setVisibility(View.VISIBLE);
				listviewbox.setVisibility(View.GONE);
				mcompile.setVisibility(View.GONE);
				mselectanddelete.setVisibility(View.GONE);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*mlistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Log.e("====监听调用了=====", "监听调用了");
				if (!iscompile) {
					Intent intent;
					Map<String, String> map = list.get(position);
					intent = new Intent(MyDraftActivity.this, MyGossipActivity.class);
					intent.putExtra("content", map.get("content"));
					intent.putExtra("title", map.get("title"));
					intent.putExtra("dtime", map.get("dtime"));
					intent.putExtra("imgs", map.get("imgs"));
					intent.putExtra("id", "");
					startActivity(intent);
				}else{
					list.get(position).put("isselect", "1");
					adapter.notifyDataSetChanged();
				}
				
			}
		});*/
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mcompile:
			compile();
			break;
		case R.id.mdelete:
			try {
				Delete();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.mallselect:
			if (isallselect) {
				mallselectimg.setImageResource(R.mipmap.weixuanzhongyuan);
				allselect("0");
				isallselect = false;
			} else {
				mallselectimg.setImageResource(R.mipmap.xuanzhongyuan);
				allselect("1");
				isallselect = true;
			}

			break;
		case R.id.topbar_goback_btn:
			finish();
			break;
		default:
			break;
		}
	}

	@SuppressLint("NewApi")
	private void Delete() throws JSONException {
		datalist.clear();
		JSONArray array1 = new JSONArray();
		int length = list.size();
		for (int i = 0; i < length; i++) {
			Map<String, String> map = list.get(i);
			if (map.get("isselect").equals("0")) {
				datalist.add(map);
				JSONObject object = array.getJSONObject(length-i-1);
				array1.put(object);
			} 
		}
		JSONArray array2 = new JSONArray();
		for (int i = array1.length()-1; i >= 0; i--) {
			JSONObject object = array1.getJSONObject(i);
			array2.put(object);
		}
		list.clear();
		array = array2;
		list.addAll(datalist);
		adapter.notifyDataSetChanged();
		SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "gossip", "gossip", array.toString());
		if (list.size() == 0) {
			mzhanwei.setVisibility(View.VISIBLE);
			listviewbox.setVisibility(View.GONE);
			mcompile.setVisibility(View.GONE);
			mselectanddelete.setVisibility(View.GONE);
		}else{
			listviewbox.setVisibility(View.VISIBLE);
		}
	}

	private void allselect(String string) {
		for (int i = 0; i < list.size(); i++) {
			list.get(i).put("isselect", string);
		}
		adapter.notifyDataSetChanged();

	}

	private void compile() {
		if (iscompile) {
			mcompile.setText("编辑");
			mlistview.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
			mselectanddelete.setVisibility(View.GONE);
			for (int i = 0; i < list.size(); i++) {
				list.get(i).put("isbianji", "0");
			}
			adapter.notifyDataSetChanged();
			iscompile = false;
		} else {
			mcompile.setText("取消");
			mlistview.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
			mselectanddelete.setVisibility(View.VISIBLE);
			for (int i = 0; i < list.size(); i++) {
				list.get(i).put("isbianji", "1");
			}
			adapter.notifyDataSetChanged();
			iscompile = true;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}
}
