package com.bbk.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bbk.adapter.ListViewAdapter;
import com.bbk.adapter.ListViewAdapter2;
import com.bbk.adapter.RankRightFragmentListviewAdapter;
import com.bbk.adapter.SortRightFragmentListviewAdapter;
import com.bbk.flow.DataFlow;
import com.bbk.flow.DataFlow2;
import com.bbk.flow.ResultEvent;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.DialogUtil;
import com.bbk.util.SharedPreferencesUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.EditText;
import android.widget.ImageButton;

public class RankCategoryActivity extends BaseActivity implements ResultEvent,OnItemClickListener{
	private View mView;
	private ListView mlistView,mlistViewRight;
	private String[] str = {"为你推荐","服饰","鞋靴/箱包","美妆/个护 ","母婴","玩具","手机/数码","电脑/办公","家电/厨具","酒水/食品","钟表/珠宝","户外",
			"家装/建材","保健","汽车"};
	private ListViewAdapter2 adapter;
	private List<Map<String, String>> listright;
	private RankRightFragmentListviewAdapter adapterright;
	private DataFlow2 dataFlow;
	private String[] str2 = {"","24","20|21","05|10","17","19","01|07","02|08","03|15","04|09","22|25","16","12|14","23","18"};
	private String type;
	private TextView topbar_title;
	private EditText topbar_search_et;
	private ImageButton topbar_goback_btn;
	private int curselect = 0;
	public static int mPosition;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rank_category);
		type = getIntent().getStringExtra("type");
		dataFlow = new DataFlow2(this);
		mPosition = 0;
		initView();
		initData();
	}
	private void initData() {
		HashMap<String, String> params = new HashMap<String, String>();
		String token = SharedPreferencesUtil.getSharedData(this, "userInfor", "token");
		params.put("addtion", "");
		params.put("token", token);
		params.put("type", type);
		dataFlow.requestData(1, "newApp/querySortCatagTree", params, this);
	}

	private void initView() {
		listright = new ArrayList<>();
		topbar_goback_btn = (ImageButton) findViewById(R.id.topbar_goback_btn);
		topbar_goback_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		mlistView = (ListView) findViewById(R.id.mlistview);
		mlistViewRight = (ListView) findViewById(R.id.mlistviewright);
		topbar_title = (TextView) findViewById(R.id.topbar_title);
		topbar_search_et = (EditText) findViewById(R.id.topbar_search_et);
		topbar_search_et.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				if (arg1 == EditorInfo.IME_ACTION_SEARCH){ 
					String keyword = topbar_search_et.getText().toString();
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);    
			        imm.hideSoftInputFromWindow(topbar_search_et.getWindowToken(), 0) ;  
		            Intent intent = new Intent(RankCategoryActivity.this, RankActivity.class);
		            intent.putExtra("keyword", keyword);
		            intent.putExtra("rankIndex", type);
		            startActivity(intent);
		            return true;  
		        }  
				return false;
			}
		});
		topbar_search_et.addTextChangedListener(new TextWatcher() {
			
			@Override 
			public void beforeTextChanged(CharSequence s, int start, int count, 
			int after) { 
			// TODO Auto-generated method stub 
			} 
			@Override 
			public void onTextChanged(CharSequence s, int start, int before, int count) { 
			// TODO Auto-generated method stub 
			} 
			@Override 
			public void afterTextChanged(Editable s) { 
			// TODO Auto-generated method stub 
			int lines = topbar_search_et.getLineCount(); 
			// 限制最大输入行数 
			if (lines > 1) { 
			String str = s.toString(); 
			int cursorStart = topbar_search_et.getSelectionStart(); 
			int cursorEnd = topbar_search_et.getSelectionEnd(); 
			if (cursorStart == cursorEnd && cursorStart < str.length() && cursorStart >= 1) { 
			str = str.substring(0, cursorStart-1) + str.substring(cursorStart); 
			} else { 
			str = str.substring(0, s.length()-1); 
			} 
			// setText会触发afterTextChanged的递归 
			topbar_search_et.setText(str);	
			// setSelection用的索引不能使用str.length()否则会越界 
			topbar_search_et.setSelection(topbar_search_et.getText().length()); 
			} 
			}
		});
		
		inittitle();
		adapter = new ListViewAdapter2(str ,this);
		mlistView.setAdapter(adapter);
		mlistView.setOnItemClickListener(this);
		
		
	}


	private void inittitle() {
		switch (type) {
		case "3":
			topbar_title.setText("热品牌");
			topbar_search_et.setHint("查询商品品牌榜");
			break;
		case "5":
			topbar_title.setText("最好货");
			topbar_search_et.setHint("查询商品好评榜");
			break;
		case "2":
			topbar_title.setText("便宜货");
			topbar_search_et.setHint("查询商品降价榜");
			break;
		case "6":
			topbar_title.setText("超好卖");
			topbar_search_et.setHint("查询商品销量榜");
			break;

		default:
			topbar_title.setText("最好货");
			topbar_search_et.setHint("查询商品好评榜");
			break;
		}
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
			adapterright = new RankRightFragmentListviewAdapter(listright, this,type);
			mlistViewRight.setAdapter(adapterright);
		}
		DialogSingleUtil.dismiss(300);
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
				adapterright = new RankRightFragmentListviewAdapter(listright, this,type);
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
			if (position == 0) {
				HashMap<String, String> params = new HashMap<String, String>();
				String token = SharedPreferencesUtil.getSharedData(this, "userInfor", "token");
				params.put("addtion", "");
				params.put("token", token);
				params.put("type", type);
				dataFlow.requestData(1, "newApp/querySortCatagTree", params, this);
			}else{
				String addtion = str2[position];
				HashMap<String, String> params = new HashMap<String, String>();
				String token = SharedPreferencesUtil.getSharedData(this, "userInfor", "token");
				params.put("addtion", addtion);
				params.put("token", token);
				params.put("type ", type);
				dataFlow.requestData(2, "newApp/querySortCatagTree", params, this);
			}
			
		}
	}

}
