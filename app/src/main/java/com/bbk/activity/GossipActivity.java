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

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshView.XRefreshViewListener;
import com.bbk.PhotoPicker.PhotoPicker;
import com.bbk.adapter.GossipAdapter;
import com.bbk.dialog.ActionSheetDialog;
import com.bbk.dialog.AlertDialog;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.resource.Constants;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.MyFootView;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GossipActivity extends BaseActivity implements OnClickListener,ResultEvent,OnItemClickListener{
	private XRefreshView xrefresh;
	private boolean isclear = false;
	//当前选择（全部，待审核，通过审核，未通过审核）
	private int curclick = 0;
	private ListView mlistview;
	private List<Map<String, String>> list;
	private TextView mydraft;
	private ImageButton topbar_goback_btn;
	private int page=1;
	private DataFlow dataFlow;
	private String userID;
	private GossipAdapter adapter;
	private TabLayout tablayout;
	private int curposition = -1;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gossip);
		View topView = findViewById(R.id.layout);
		// 实现沉浸式状态栏
		ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
		dataFlow = new DataFlow(this);
		initVeiw();
		initData();
	}


	private void initVeiw() {
		list = new ArrayList<>();
		userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
		
		topbar_goback_btn = (ImageButton) findViewById(R.id.topbar_goback_btn);
		mlistview = (ListView) findViewById(R.id.mlistview);
		mydraft = (TextView) findViewById(R.id.mydraft);
		tablayout = (TabLayout) findViewById(R.id.tablayout);
		xrefresh = (XRefreshView) findViewById(R.id.xrefresh);
		tablayout.addTab(tablayout.newTab().setText("全部"));
		tablayout.addTab(tablayout.newTab().setText("待审核"));
		tablayout.addTab(tablayout.newTab().setText("通过审核"));
		tablayout.addTab(tablayout.newTab().setText("未通过审核"));
		tablayout.setTabMode(TabLayout.MODE_FIXED);
		refreshAndloda();
		tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				int j = tab.getPosition();
				if (j==0){
					curclick = 0;
				}else if(j==1){
					curclick = 1;
				}else if(j==2){
					curclick = 2;
				}else if(j==3){
					curclick = 3;
				}
				isclear = true;
				refresh();
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {

			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {

			}
		});

		topbar_goback_btn.setOnClickListener(this);
		mydraft.setOnClickListener(this);

		
	}

	private void initData() {
		adapter = new GossipAdapter(list, this);
		mlistview.setAdapter(adapter);
		if (getIntent().getStringExtra("type")!=null) {
			String type1 = getIntent().getStringExtra("type");
			if (type1.equals("1")) {
//				loadData("0");
				TabLayout.Tab tabAt = tablayout.getTabAt(1);
				tabAt.select();
			}else{
				loadData(null);
			}
		}
	}
//	private void initallData() {
//		HashMap<String, String> params = new HashMap<String, String>();
//		params.put("userid", userID);
//		params.put("page", String.valueOf(page));
//		dataFlow.requestData(1, "newService/queryBaoliaoByUserid", params, GossipActivity.this);
//	}
//	private void inittoData() {
//		HashMap<String, String> params = new HashMap<String, String>();
//		params.put("userid", userID);
//		params.put("type", "0");
//		params.put("page", String.valueOf(page));
//		dataFlow.requestData(2, "newService/queryBaoliaoByUserid", params, GossipActivity.this);
//	}
//	private void initpassData() {
//		HashMap<String, String> params = new HashMap<String, String>();
//		params.put("type", "1");
//		params.put("userid", userID);
//		params.put("page", String.valueOf(page));
//		dataFlow.requestData(3, "newService/queryBaoliaoByUserid", params, GossipActivity.this);
//	}
//	private void initnotpassData() {
//		HashMap<String, String> params = new HashMap<String, String>();
//		params.put("type", "-1");
//		params.put("userid", userID);
//		params.put("page", String.valueOf(page));
//		dataFlow.requestData(4, "newService/queryBaoliaoByUserid", params, GossipActivity.this);
//	}
	private void loadData(String type) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("type", type);
		params.put("userid", userID);
		params.put("page", String.valueOf(page));
		dataFlow.requestData(1, "newService/queryBaoliaoByUserid", params, GossipActivity.this);
	}

	private void refreshAndloda() {
		xrefresh.setXRefreshViewListener(new XRefreshViewListener() {


			@Override
			public void onRelease(float direction) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onRefresh(boolean isPullDown) {
				isclear = true;
				page = 1;
				refresh();

			}

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLoadMore(boolean isSilence) {
				page++;
				refresh();
			}

			@Override
			public void onHeaderMove(double headerMovePercent, int offsetY) {
				// TODO Auto-generated method stub

			}
		});
		MyFootView footView = new MyFootView(this);
		xrefresh.setCustomFooterView(footView);
	}

	private void refresh(){
		switch (curclick) {
			case 0:
				loadData(null);
				break;
			case 1:
				loadData("0");
				break;
			case 2:
				loadData("1");
				break;
			case 3:
				loadData("-1");
				break;
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topbar_goback_btn:
			finish();
			break;
		case R.id.mydraft:
			Intent intent = new Intent(this, MyDraftActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	private void allselect(String string) {
		for (int i = 0; i < list.size(); i++) {
			list.get(i).put("isselect", string);
		}
		adapter.notifyDataSetChanged();

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
		if ("1".equals(list.get(position).get("type"))) {
			String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
			String url = Constants.MAIN_BASE_URL_MOBILE +"mobile/blbar?blid="+list.get(position).get("id")+"&userid="+userID;
			Intent intent = new Intent(this, WebViewWZActivity.class);
			intent.putExtra("url",url);
			intent.putExtra("title",list.get(position).get("title"));
			startActivity(intent);
		}else if ("-1".equals(list.get(position).get("type"))){
			Map<String, String> map = list.get(position);
			Intent intent = new Intent(this, MyGossipActivity.class);
			intent.putExtra("content", map.get("content"));
			intent.putExtra("title", map.get("title"));
			intent.putExtra("dtime", map.get("dtime"));
			intent.putExtra("imgs", map.get("imgs"));
			intent.putExtra("url", map.get("url"));
			intent.putExtra("position", "审核未通过");
			startActivity(intent);
		}
	}

	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
		xrefresh.stopRefresh();
		xrefresh.stopLoadMore();
		switch (requestCode){
			case 1:
				if (isclear) {
					list.clear();
				}
				try {
					JSONArray arr = new JSONArray(content);
					if (arr.length() < 10) {
						xrefresh.setLoadComplete(true);
					} else {
						xrefresh.setAutoLoadMore(true);
					}
					for (int i = 0; i < arr.length(); i++) {
						JSONObject object = arr.getJSONObject(i);
						Map<String, String> map = new HashMap<>();
						map.put("content", object.optString("content"));
						map.put("id", object.optString("id"));
						map.put("title", object.optString("title"));
						map.put("dtime", object.optString("dtime"));
						map.put("typeCh", object.optString("typeCh"));
						map.put("type", object.optString("type"));
						map.put("url", object.optString("url"));
						if (object.has("video")){
							map.put("video", object.optString("video"));
						}else {
							map.put("video", object.optString("0"));
						}
						if (object.has("reason")){
							map.put("reason", object.optString("reason"));
						}else {
							map.put("reason", object.optString(""));
						}
						map.put("isselect", "0");
						map.put("isbianji", "0");
						map.put("imgs", object.getJSONArray("imgs").toString());
						list.add(map);
					}
					adapter.notifyDataSetChanged();
					adapter.setOnMylongClickListener(new GossipAdapter.OnMylongclickListent() {
						@Override
						public void OnlongClick(View view,final int position) {
							if (curclick == 3){
								new AlertDialog(GossipActivity.this).builder().setTitle("提示").setMsg("是否删除该条爆料？")
										.setPositiveButton("删除", new View.OnClickListener() {
											@Override
											public void onClick(View v) {
												Map<String, String> map = new HashMap<>();
												map.put("ids",list.get(position).get("id"));
												curposition = position;
												dataFlow.requestData(2,"newService/delMyBaoliaoById",map,GossipActivity.this);
											}
										}).setNegativeButton("取消", new View.OnClickListener() {
									@Override
									public void onClick(View v) {

									}
								}).show();
							}
						}
					});
					isclear = false;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 2:
				if ("1".equals(dataJo.optString("status"))){
					if (curposition!= -1){
						list.remove(curposition);
						adapter.notifyDataSetChanged();
						Toast.makeText(getApplicationContext(), "删除成功！",
								Toast.LENGTH_SHORT).show();
					}
				}else {
					Toast.makeText(getApplicationContext(), dataJo.optString("errmsg"),
							Toast.LENGTH_SHORT).show();
				}
				break;
		}

	}





}
