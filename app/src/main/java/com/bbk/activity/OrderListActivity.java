package com.bbk.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bbk.adapter.OrderListAdapter;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.MyListView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OrderListActivity extends BaseActivity implements ResultEvent, OnClickListener {

	private ImageButton topbar_goback_btn;
	private MyListView mlistview;
	private List<Map<String, String>> list, datalist;
	private String token;
	private DataFlow dataFlow;
	private OrderListAdapter adapter;
	private TextView mcompile,mtime;
	private boolean iscompile = false;
	private boolean isallselect = false;
	private RelativeLayout mselectanddelete;
	private ImageView mallselectimg;
	private String ids = "";
	private RelativeLayout top;
	private RelativeLayout mdelete;
	private LinearLayout mallselect;
	private LinearLayout mmoretime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_list);
		dataFlow = new DataFlow(this);
		initView();
		initData();
	}

	private void initView() {
		list = new ArrayList<>();
		datalist = new ArrayList<>();
		mlistview = (MyListView) findViewById(R.id.mlistview);
		mcompile = (TextView) findViewById(R.id.mcompile);
		mtime = (TextView) findViewById(R.id.mtime);
		topbar_goback_btn = (ImageButton) findViewById(R.id.topbar_goback_btn);
		mallselectimg = (ImageView) findViewById(R.id.mallselectimg);
		mselectanddelete = (RelativeLayout) findViewById(R.id.mselectanddelete);
		mdelete = (RelativeLayout) findViewById(R.id.mdelete);
		mselectanddelete = (RelativeLayout) findViewById(R.id.mselectanddelete);
		top = (RelativeLayout) findViewById(R.id.top);
		mallselect = (LinearLayout) findViewById(R.id.mallselect);
		mmoretime = (LinearLayout) findViewById(R.id.mmoretime);
		topbar_goback_btn.setOnClickListener(this);
		mcompile.setOnClickListener(this);
		mdelete.setOnClickListener(this);
		mallselect.setOnClickListener(this);

	}

	private void initData() {
		token = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "token");
		Map<String, String> params = new HashMap<>();
		params.put("token", token);
		dataFlow.requestData(1, "newService/queryOrderListByToken", params, this);
	}

	private void deleteData() {
		Map<String, String> params = new HashMap<>();
		params.put("ids", ids);
		dataFlow.requestData(2, "newService/deleteOrderHistory", params, this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mcompile:
			if (iscompile) {
				mcompile.setText("编辑");
				compile("0");
				mselectanddelete.setVisibility(View.GONE);
				top.setVisibility(View.VISIBLE);
				mtime.setVisibility(View.VISIBLE);
				iscompile = false;
			} else {
				mcompile.setText("取消");
				compile("1");
				mselectanddelete.setVisibility(View.VISIBLE);
				top.setVisibility(View.GONE);
				mtime.setVisibility(View.GONE);
				iscompile = true;
			}
			break;
		case R.id.topbar_goback_btn:
			finish();
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
		case R.id.mdelete:
			Delete();
			if (ids != "") {
				ids = ids.substring(0, ids.length() - 1);
				deleteData();
				ids = "";
			}
			break;

		default:
			break;
		}
	}

	private void Delete() {
		datalist.clear();

		int length = list.size();
		for (int i = 0; i < length; i++) {
			Map<String, String> map = list.get(i);
			if (map.get("isselect").equals("0")) {
				datalist.add(map);
			} else {
				String id = map.get("id");
				ids = id + "," + ids;
			}
		}
		list.clear();
		list.addAll(datalist);
		adapter.notifyDataSetChanged();
	}

	private void allselect(String string) {
		for (int i = 0; i < list.size(); i++) {
			list.get(i).put("isselect", string);
		}
		adapter.notifyDataSetChanged();
	}

	private void compile(String str) {
		for (int i = 0; i < list.size(); i++) {
			list.get(i).put("isbianji", str);
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
		switch (requestCode) {
		case 1:
			list.clear();
			try {
				JSONArray array = new JSONArray(content);
				if (array.length()>0) {
					for (int i = 0; i < array.length(); i++) {
						JSONObject object = array.getJSONObject(i);
						Map<String, String> map = new HashMap<>();
						map.put("id", object.optString("id"));
						map.put("time", object.optString("time"));
						map.put("domain", object.optString("domain"));
						map.put("url", object.optString("url"));
						map.put("domainCh", object.optString("domainCh"));
						map.put("isselect", "0");
						map.put("isbianji", "0");
						list.add(map);
					}
					String time = array.getJSONObject(array.length()-1).optString("time");
					mtime.setText(time);
					if (adapter != null) {
						adapter.notifyDataSetChanged();
					} else {
						adapter = new OrderListAdapter(list, this);
						mlistview.setAdapter(adapter);
						mlistview.setOnItemClickListener(new OnItemClickListener() {
							
							@Override
							public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
								if (iscompile) {
									// 编辑
									if (isallselect) {
										mallselectimg.setImageResource(R.mipmap.weixuanzhongyuan);
										isallselect = false;
									}
									String isselect = list.get(arg2).get("isselect");
									if (isselect.equals("1")) {
										list.get(arg2).put("isselect", "0");
									} else {
										list.get(arg2).put("isselect", "1");
									}
									adapter.notifyDataSetChanged();
								} else {
									Intent intent = new Intent(OrderListActivity.this, WebViewActivity.class);
									intent.putExtra("url", list.get(arg2).get("url"));
									startActivity(intent);
								}
							}
						});
					}
				}else{
					mcompile.setVisibility(View.GONE);
					mmoretime.setVisibility(View.GONE);
					mdelete.setVisibility(View.GONE);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 2:
			initData();
			break;
		default:
			break;
		}
	}

}
