package com.bbk.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshView.XRefreshViewListener;
import com.bbk.adapter.MesageCListReceiveAdapter;
import com.bbk.adapter.MesageCListReceiveAdapter.MyClickListener;
import com.bbk.adapter.MesageCListSendAdapter;
import com.bbk.adapter.MesageCListSysAdapter;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.resource.Constants;
import com.bbk.util.EventIdIntentUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.HeaderView;
import com.bbk.view.MyFootView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MesageCenterActivity extends BaseActivity implements OnClickListener, ResultEvent, OnLayoutChangeListener {

	private DataFlow dataFlow;
	private ImageButton topbar_goback_btn;
	private List<Map<String, String>> systemlist, sendlist, receivelist;
	private MesageCListSysAdapter systemadapter;
	private MesageCListSendAdapter sendadapter;
	private MesageCListReceiveAdapter receiveadapter;
	private int curclick = 0;
	private XRefreshView xrefresh;
	private boolean isclear = false;
	private EditText msgEdittext;
	private int systempage = 1;
	private int sendpage = 1;
	private int receivepage = 1;
	private boolean systemLoadmore = false;
	private boolean sendLoadmore = false;
	private boolean receiveLoadmore = false;
	private int screenHeight;
	private int keyHeight;
	private RelativeLayout parentview;
	private String userID, msgreid, msgplid, msgwenzhangid;
	private Toast toast;
	private LinearLayout mzhanwei, sendtext;
	private String type = "0";
	private String wztitle = "";
	private TextView mhuifusend;
	private ListView mlistview;
	private TabLayout tablayout;
	private View sendhenggang;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mesage_center);
		View topView = findViewById(R.id.parentview);
		// 实现沉浸式状态栏
		ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
				| WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		if (getIntent().getStringExtra("type") != null) {
			type = getIntent().getStringExtra("type");
		}
		dataFlow = new DataFlow(this);
		initView();
		initData(type);
	}

	private void initData(String type2) {
		curclick = Integer.valueOf(type2);
		TabLayout.Tab tabAt;
		switch (type2) {
		case "0":
			initsystemData();
			break;
		case "1":
			tabAt = tablayout.getTabAt(1);
			tabAt.select();
			break;
		case "2":
			tabAt = tablayout.getTabAt(2);
			tabAt.select();
			break;

		default:
			break;
		}
	}

	private void initView() {
		systemlist = new ArrayList<>();
		sendlist = new ArrayList<>();
		receivelist = new ArrayList<>();
		topbar_goback_btn = (ImageButton) findViewById(R.id.topbar_goback_btn);
		parentview = (RelativeLayout) findViewById(R.id.parentview);
		mzhanwei = (LinearLayout) findViewById(R.id.mzhanwei);
		sendtext = (LinearLayout) findViewById(R.id.sendtext);
		msgEdittext = (EditText) findViewById(R.id.msgEdittext);
		sendhenggang = findViewById(R.id.sendhenggang);
		mhuifusend = (TextView) findViewById(R.id.mhuifusend);
		userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
		// 获取屏幕高度
		screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
		// 阀值设置为屏幕高度的1/3
		keyHeight = screenHeight / 3;
		mlistview = (ListView) findViewById(R.id.mlistview);
		tablayout = (TabLayout) findViewById(R.id.tablayout);
		xrefresh = (XRefreshView) findViewById(R.id.xrefresh);
		tablayout.addTab(tablayout.newTab().setText("消息通知"));
		tablayout.addTab(tablayout.newTab().setText("发出的评论"));
		tablayout.addTab(tablayout.newTab().setText("收到的评论"));
		tablayout.setTabMode(TabLayout.MODE_FIXED);
		xrefresh.setCustomHeaderView(new HeaderView(this));
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
				xrefresh.setVisibility(View.GONE);
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
		parentview.setOnClickListener(this);
		mhuifusend.setOnClickListener(this);

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
				switch (curclick) {
				case 0:
					systempage = 1;
					initsystemData();
					break;
				case 1:
					sendpage = 1;
					initsendData();
					break;
				case 2:
					receivepage = 1;
					initreceiveData();
					break;

				default:
					break;
				}
			}

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLoadMore(boolean isSilence) {

				switch (curclick) {
				case 0:

					systempage++;
					initsystemData();
					break;
				case 1:
					sendpage++;
					initsendData();
					break;
				case 2:
					receivepage++;
					initreceiveData();
					break;

				default:
					break;
				}
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
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(msgEdittext.getWindowToken(), 0);
		sendtext.setVisibility(View.GONE);
		switch (curclick) {
			case 0:
				initsystemData();
				break;
			case 1:
				initsendData();
				break;
			case 2:
				initreceiveData();
				break;

		}
	}
	private void initsystemData() {
		Map<String, String> params = new HashMap<>();
		params.put("userid", userID);
		params.put("page", String.valueOf(systempage));
		dataFlow.requestData(1, "newService/querySysTMessage", params, this);
	}

	private void initsendData() {
		Map<String, String> params = new HashMap<>();
		params.put("userid", userID);
		params.put("page", String.valueOf(sendpage));
		dataFlow.requestData(2, "newService/queryPLMyRe", params, this);
	}

	private void initreceiveData() {
		Map<String, String> params = new HashMap<>();
		params.put("userid", userID);
		params.put("page", String.valueOf(receivepage));
		dataFlow.requestData(3, "newService/queryPLOtherRe", params, this);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
			send();
			return true;
		}
		// return true;
		return super.dispatchKeyEvent(event);
		// 必不可少，否则所有的组件都不会有TouchEvent了
	}
	private void send(){
		if (msgEdittext.getText().toString().isEmpty()) {
			if (toast != null) {
				toast.cancel();
			}
			toast = Toast.makeText(this, "评论不能为空", Toast.LENGTH_SHORT);
			toast.show();
		} else {
			/* 隐藏软键盘 */
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			if (inputMethodManager.isActive()) {
				inputMethodManager
						.hideSoftInputFromWindow(MesageCenterActivity.this.getCurrentFocus().getWindowToken(), 0);
			}
			sendtext.setVisibility(View.GONE);
			sendhenggang.setVisibility(View.GONE);

			Map<String, String> params = new HashMap<>();
			params.put("userid", userID);
			params.put("reid", msgreid);
			params.put("plid", msgplid);
			params.put("wenzhangid", msgwenzhangid);
			params.put("content", msgEdittext.getText().toString());
			dataFlow.requestData(4, "newService/insertPL", params, this);
		}
	}
	@Override
	public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight,
			int oldBottom) {

		// old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值

		// 现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
		if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {

		} else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
			sendtext.setVisibility(View.GONE);
			sendhenggang.setVisibility(View.GONE);

		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topbar_goback_btn:
			finish();
			break;
		case R.id.parentview:
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			sendtext.setVisibility(View.GONE);
			sendhenggang.setVisibility(View.GONE);
			break;
		case R.id.mhuifusend:
			send();
			break;
		default:
			break;
		}
	}

	/**
	 * 系统消息
	 * 
	 */
	private void loadSystemtMsg(final JSONArray arr) throws JSONException {
		if (arr.length() < 10) {
			systemLoadmore = false;
			xrefresh.setLoadComplete(true);
		} else {
			systemLoadmore = true;
			xrefresh.setAutoLoadMore(true);
		}
		for (int i = 0; i < arr.length(); i++) {
			JSONObject object = arr.getJSONObject(i);
			Map<String, String> map = new HashMap<>();
			map.put("message", object.optString("message").toString());
			map.put("dtime", object.optString("dtime").toString());
			map.put("isread", object.optString("isread").toString());
			map.put("mid", object.optString("mid").toString());
			if (object.optString("eventId")!= null) {
				map.put("eventId", object.optString("eventId").toString());
			}else{
				map.put("eventId", "-1");
			}
			systemlist.add(map);
		}
		systemsetadapter();


	}
	public void systemsetadapter(){
		systemadapter = new MesageCListSysAdapter(systemlist, this);
		mlistview.setAdapter(systemadapter);
		mlistview.setFocusable(true);
		mlistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (!systemlist.get(arg2).get("isread").equals("1")) {
					systemlist.get(arg2).put("isread", "1");
					systemadapter.notifyDataSetChanged();
					Map<String, String> params = new HashMap<>();
					params.put("userid", userID);
					params.put("mid", systemlist.get(arg2).get("mid"));
					dataFlow.requestData(10, "newService/insertMessageRead", params, MesageCenterActivity.this);
				}
				if (systemlist.get(arg2).get("eventId").equals("101")) {
					if (receiveLoadmore) {
						xrefresh.setAutoLoadMore(true);
					} else {
						xrefresh.setLoadComplete(true);
					}
					TabLayout.Tab tabAt = tablayout.getTabAt(2);
					tabAt.select();
				}
			}
		});
	}

	/**
	 * 发出的评论
	 * 
	 */
	private void loadSendMsg(final JSONArray arr) throws JSONException {
		for (int i = 0; i < arr.length(); i++) {
			JSONObject object = arr.getJSONObject(i);
			Map<String, String> map = new HashMap<>();
			map.put("content", object.optString("content").toString());
			map.put("title", object.optString("title").toString());
			map.put("wzid", object.optString("wzid").toString());
			map.put("dt", object.optString("dt").toString());
			if (!object.optString("nickname").isEmpty()) {
				map.put("nickname", object.optString("nickname").toString());
			}
			map.put("imgurl", object.optString("imgurl").toString());
			map.put("htmlid", object.optString("htmlid").toString());
			sendlist.add(map);
		}
		sendsetadapter();

	}
	public void sendsetadapter(){
		sendadapter = new MesageCListSendAdapter(sendlist, this);
		mlistview.setAdapter(sendadapter);
		mlistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				wztitle = sendlist.get(position).get("title");
				String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor",
						"userID");
				String token = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor",
						"token");
				String wz = sendlist.get(position).get("wzid");
				if (wz.contains("B")){
					String blid = wz.substring(1, wz.length());
					String url = Constants.MAIN_BASE_URL_MOBILE +"mobile/blbar?blid="+blid+"&userid="+userID;
					Intent intent = new Intent(MesageCenterActivity.this, WebViewWZActivity.class);
					intent.putExtra("url",url);
					intent.putExtra("title",wztitle);
					startActivity(intent);
				}else {
					HashMap<String, String> params = new HashMap<String, String>();
					params.put("userid", userID);
					params.put("wzid", wz);
					params.put("token", token);
					params.put("type", "2");
					dataFlow.requestData(5, "newService/insertWenzhangGuanzhu", params, MesageCenterActivity.this);
				}
			}
		});
	}

	/**
	 * 收到的评论
	 * 
	 */
	private void loadReceiveMsg(final JSONArray arr) throws JSONException {
		if (arr.length() < 10) {
			receiveLoadmore = false;
			xrefresh.setLoadComplete(true);
		} else {
			receiveLoadmore = true;
			xrefresh.setAutoLoadMore(true);
		}
		for (int i = 0; i < arr.length(); i++) {
			JSONObject object = arr.getJSONObject(i);
			Map<String, String> map = new HashMap<>();
			map.put("content", object.optString("content").toString());
			map.put("title", object.optString("title").toString());
			map.put("dt", object.optString("dt").toString());
			map.put("nickname", object.optString("nickname").toString());
			map.put("imgurl", object.optString("imgurl").toString());
			map.put("uid", object.optString("uid").toString());
			map.put("plid", object.optString("plid").toString());
			map.put("wenzhangid", object.optString("wenzhangid").toString());
			map.put("htmlid", object.optString("htmlid").toString());
			receivelist.add(map);
		}
		recieversetadapter();

	}
	public void recieversetadapter(){
		receiveadapter = new MesageCListReceiveAdapter(receivelist, this);
		mlistview.setAdapter(receiveadapter);
		mlistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				wztitle = receivelist.get(position).get("title");
				String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor",
						"userID");
				String token = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor",
						"token");
				if (receivelist.get(position).get("wenzhangid").contains("B")){
					String wenzhangid = sendlist.get(position).get("wenzhangid");
					String blid = wenzhangid.substring(1, wenzhangid.length());
					String url = Constants.MAIN_BASE_URL_MOBILE +"mobile/blbar?blid="+blid+"&userid="+userID;
					Intent intent = new Intent(MesageCenterActivity.this, WebViewWZActivity.class);
					intent.putExtra("url",url);
					intent.putExtra("title",wztitle);
					startActivity(intent);
				}else {
					HashMap<String, String> params = new HashMap<String, String>();
					params.put("userid", userID);
					params.put("wzid", receivelist.get(position).get("wenzhangid"));
					params.put("token", token);
					params.put("type", "2");
					dataFlow.requestData(5, "newService/insertWenzhangGuanzhu", params, MesageCenterActivity.this);
				}
			}
		});
		receiveadapter.setOnClickListener(new MyClickListener() {

			@Override
			public void onClick(int position) {
				msgwenzhangid = receivelist.get(position).get("wenzhangid");
				msgreid = receivelist.get(position).get("uid");
				msgplid = receivelist.get(position).get("plid");
				sendtext.setVisibility(View.VISIBLE);
				sendhenggang.setVisibility(View.VISIBLE);
				msgEdittext.requestFocus();
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);// 开启或者关闭软键盘
				imm.showSoftInput(msgEdittext, InputMethodManager.SHOW_FORCED);// 弹出软键盘时，焦点定位在输入框中

			}
		});
	}
	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
		xrefresh.stopRefresh();
		xrefresh.stopLoadMore();
		switch (requestCode) {
		case 1:
			try {
				if (isclear) {
					systemlist.clear();
				}
				JSONArray arr = new JSONArray(content);
				if (arr.length() == 0) {
					systemsetadapter();
					mzhanwei.setVisibility(View.VISIBLE);
				} else {
					mzhanwei.setVisibility(View.GONE);
					loadSystemtMsg(arr);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 2:
			try {
				if (isclear) {
					sendlist.clear();
				}
				JSONArray arr = new JSONArray(content);
				if (arr.length() == 0) {
					sendsetadapter();
					mzhanwei.setVisibility(View.VISIBLE);
				} else {
					mzhanwei.setVisibility(View.GONE);
					loadSendMsg(arr);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 3:
			try {
				if (isclear) {
					receivelist.clear();
				}
				JSONArray arr = new JSONArray(content);
				if (arr.length() == 0) {
					recieversetadapter();
					mzhanwei.setVisibility(View.VISIBLE);
				} else {
					mzhanwei.setVisibility(View.GONE);
					loadReceiveMsg(arr);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 4:
			msgEdittext.setText("");
			TabLayout.Tab tabAt = tablayout.getTabAt(1);
			tabAt.select();
			isclear = true;
			sendpage = 1;
			initsendData();
			break;
		case 5:
			Intent intent = new Intent(this, WebViewWZActivity.class);
			intent.putExtra("title", wztitle);
			intent.putExtra("url", content);
			startActivity(intent);
			break;

		case 10:
			break;
		default:
			break;
		}
		xrefresh.setVisibility(View.VISIBLE);
	}

}
