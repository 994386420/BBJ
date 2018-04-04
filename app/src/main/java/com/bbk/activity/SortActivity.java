package com.bbk.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bbk.activity.R;
import com.bbk.activity.SearchMainActivity;
import com.bbk.adapter.ListViewAdapter;
import com.bbk.adapter.RankRightFragmentListviewAdapter;
import com.bbk.adapter.SortRightFragmentListviewAdapter;
import com.bbk.flow.DataFlow;
import com.bbk.flow.DataFlow2;
import com.bbk.flow.ResultEvent;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;

public class SortActivity extends BaseActivity implements ResultEvent,OnItemClickListener{
	private View rank_head;
	private ListView mlistView,mlistViewRight;
	//分类数组
	private String[] str = {"为你推荐","服饰","鞋靴/箱包","美妆/个护 ","母婴","玩具","手机/数码","电脑/办公","家电/厨具","酒水/食品","钟表/珠宝","户外",
			"家装/建材","保健","汽车"};
	private ListViewAdapter adapter;
	private List<Map<String, String>> listright;
	private SortRightFragmentListviewAdapter adapterright;
	private RelativeLayout msearchall;
	private int curselect = 0;
	private DataFlow2 dataFlow;
	//分类数组对应的addition
	private String[] str2 = {"","24","20|21","05|10","17","19","01|07","02|08","03|15","04|09","22|25","16","12|14","23","18"};
	public static int mPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sort_rank);
		rank_head = findViewById(R.id.rank_head);
		rank_head.setVisibility(View.VISIBLE);
		dataFlow = new DataFlow2(this);
		mPosition = 0;
		ImmersedStatusbarUtils.FlymeSetStatusBarLightMode(getWindow(),true);
		ImmersedStatusbarUtils.MIUISetStatusBarLightMode(this,true);
		initstateView();
		initView();
		initData();
	}
	private void initData() {
		HashMap<String, String> params = new HashMap<String, String>();
		String token = SharedPreferencesUtil.getSharedData(this, "userInfor", "token");
		params.put("addtion", "");
		params.put("token", token);
		dataFlow.requestData(1, "newApp/queryCatagTree", params, this);
	}

	private void initView() {
		listright = new ArrayList<>();
		mlistView = (ListView) findViewById(R.id.mlistview);
		mlistViewRight = (ListView) findViewById(R.id.mlistviewright);
		msearchall = (RelativeLayout)findViewById(R.id.msearchall);
	
		adapter = new ListViewAdapter(str, this);
		mlistView.setAdapter(adapter);
		mlistView.setOnItemClickListener(this);
		msearchall.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(SortActivity.this, SearchMainActivity.class);
				startActivity(intent);
			}
		});
		
		
	}

	//状态栏高度
	private int getStatusBarHeight() {  
        Class<?> c = null;  
  
        Object obj = null;  
  
        Field field = null;  
  
        int x = 0, sbar = 0;  
  
        try {  
  
            c = Class.forName("com.android.internal.R$dimen");  
  
            obj = c.newInstance();  
  
            field = c.getField("status_bar_height");  
  
            x = Integer.parseInt(field.get(obj).toString());  
  
            sbar = getResources().getDimensionPixelSize(x);  
  
        } catch (Exception e1) {  
  
            e1.printStackTrace();  
  
        }  
  
        return sbar;  
    } 
	//沉浸式状态栏
	private void initstateView() {
		if (Build.VERSION.SDK_INT >=19) {
			rank_head.setVisibility(View.VISIBLE);
		}
		int result = getStatusBarHeight();
		LayoutParams layoutParams = rank_head.getLayoutParams();
		layoutParams.height = result;
		rank_head.setLayoutParams(layoutParams);
	}



	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
		try {
		switch (requestCode) {
		case 1:
			
			JSONArray array = new JSONArray(content);
			Map<String, String> map = null;
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				String name = object.optString("name");
				String chid = object.optString("chid");
				String tongjilist = object.optString("tongjilist");
				map = new HashMap<>();
				map.put("name", name);
				map.put("content", chid);
				map.put("tongjilist", tongjilist);
				listright.add(map);
				
			}
		if (listright!= null) {
			adapterright = new SortRightFragmentListviewAdapter(listright, this);
			mlistViewRight.setAdapter(adapterright);
		}
		DialogSingleUtil.dismiss(500);
			break;
		case 2:
			
			listright.clear();
			JSONArray array1 = new JSONArray(content);
			Map<String, String> map1 = null;
			for (int i = 0; i < array1.length(); i++) {
				JSONObject object = array1.getJSONObject(i);
				String name = object.optString("name");
				String chid = object.optString("chid");
				String tongjilist = object.optString("tongjilist");
				map1 = new HashMap<>();
				map1.put("name", name);
				map1.put("content", chid);
				map1.put("tongjilist", tongjilist);
				listright.add(map1);
			}
			if (adapterright == null) {
				adapterright = new SortRightFragmentListviewAdapter(listright, this);
				mlistViewRight.setAdapter(adapterright);
			}else{
				adapterright.notifyDataSetChanged();
			}
			DialogSingleUtil.dismiss(500);
			break;
		default:
			break;
		}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (curselect!= position) {
			curselect = position;
			mPosition =position;
			adapter.notifyDataSetChanged();
			listright.clear();
			String addtion = str2[position];
			HashMap<String, String> params = new HashMap<String, String>();
			String token = SharedPreferencesUtil.getSharedData(this, "userInfor", "token");
			params.put("addtion", addtion);
			params.put("token", token);
			dataFlow.requestData(2, "newApp/queryCatagTree", params, this);
		}
	}

}
