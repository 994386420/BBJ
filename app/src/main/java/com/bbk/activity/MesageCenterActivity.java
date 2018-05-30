package com.bbk.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshView.XRefreshViewListener;
import com.bbk.Bean.ReceiceMsgBean;
import com.bbk.Bean.SendMsgBean;
import com.bbk.Bean.SystemMessageBean;
import com.bbk.Bean.WoYaoBean;
import com.bbk.adapter.BidListDetailAdapter;
import com.bbk.adapter.MesageCListReceiveAdapter;
import com.bbk.adapter.MesageCListReceiveAdapter.MyClickListener;
import com.bbk.adapter.MesageCListSendAdapter;
import com.bbk.adapter.MesageCListSysAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.SoftHideKeyBoardUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.CommonLoadingView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MesageCenterActivity extends BaseActivity implements OnClickListener, OnLayoutChangeListener ,CommonLoadingView.LoadingHandler{

	private DataFlow dataFlow;
	private ImageButton topbar_goback_btn;
	private List<Map<String, String>> systemlist, sendlist, receivelist;
	private MesageCListSysAdapter systemadapter;
	private MesageCListSendAdapter sendadapter;
	private MesageCListReceiveAdapter receiveadapter;
	private int curclick = 0;
	private SmartRefreshLayout xrefresh;
	private EditText msgEdittext;
	private int systempage = 1,x = 1;
	private int sendpage = 1;
	private int receivepage = 1;
	private int screenHeight;
	private int keyHeight;
	private RelativeLayout parentview;
	private String userID, msgreid, msgplid, msgwenzhangid;
	private Toast toast;
	private LinearLayout sendtext;
	private String type = "0";
	private String wztitle = "";
	private TextView mhuifusend;
	private ListView mlistview;
	private TabLayout tablayout;
	private View sendhenggang;
	private CommonLoadingView zLoadingView;//加载框
	private List<SystemMessageBean> systemMessageBeans;
	private List<SendMsgBean> sendMsgBeans;
	private List<ReceiceMsgBean> receiceMsgBeans;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mesage_center);
		SoftHideKeyBoardUtil.assistActivity(this,getStatusBarHeight(this));
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
	private int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
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
		zLoadingView = findViewById(R.id.progress);
		zLoadingView.setLoadingHandler(this);
		systemlist = new ArrayList<>();
		sendlist = new ArrayList<>();
		receivelist = new ArrayList<>();
		topbar_goback_btn = (ImageButton) findViewById(R.id.topbar_goback_btn);
		parentview = (RelativeLayout) findViewById(R.id.parentview);
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
		xrefresh = (SmartRefreshLayout) findViewById(R.id.xrefresh);
		tablayout.addTab(tablayout.newTab().setText("消息通知"));
		tablayout.addTab(tablayout.newTab().setText("发出的评论"));
		tablayout.addTab(tablayout.newTab().setText("收到的评论"));
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
				x = 1;
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
		xrefresh.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(final RefreshLayout refreshlayout) {
				switch (curclick) {
					case 0:
						systempage = 1;
						x= 1;
						initsystemData();
						break;
					case 1:
						sendpage = 1;
						x= 1;
						initsendData();
						break;
					case 2:
						receivepage = 1;
						x= 1;
						initreceiveData();
						break;

					default:
						break;
				}
			}
		});
		xrefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore(RefreshLayout refreshlayout) {
				switch (curclick) {
					case 0:
						systempage++;
						x= 2;
						initsystemData();
						break;
					case 1:
						sendpage++;
						x= 2;
						initsendData();
						break;
					case 2:
						receivepage++;
						x= 2;
						initreceiveData();
						break;

					default:
						break;
				}
			}
		});

	}
	private void refresh(){
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(msgEdittext.getWindowToken(), 0);
		sendtext.setVisibility(View.GONE);
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

		}
	}

	/**
	 * 系统消息
	 */
	private void initsystemData() {
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("userid", userID);
		maps.put("page", String.valueOf(systempage));
		RetrofitClient.getInstance(this).createBaseApi().querySysTMessage(
				maps, new BaseObserver<String>(this) {
					@Override
					public void onNext(String s) {
						try {
							JSONObject jsonObject = new JSONObject(s);
							String content = jsonObject.optString("content");
							if (jsonObject.optString("status").equals("1")) {
									systemMessageBeans = JSON.parseArray(content,SystemMessageBean.class);
								if (x == 1){
									if (systemMessageBeans != null && systemMessageBeans.size() > 0) {
										xrefresh.setEnableLoadMore(true);
										systemadapter = new MesageCListSysAdapter(systemMessageBeans, MesageCenterActivity.this);
										mlistview.setAdapter(systemadapter);
										mlistview.setFocusable(true);
										mlistview.setVisibility(View.VISIBLE);
										zLoadingView.loadSuccess();
									}else {
										mlistview.setVisibility(View.GONE);
										zLoadingView.loadSuccess(true);
										xrefresh.setEnableLoadMore(false);
									}
								}else {
									mlistview.setVisibility(View.VISIBLE);
									zLoadingView.loadSuccess();
									if (systemMessageBeans != null && systemMessageBeans.size() > 0) {
										systemadapter.notifyData(systemMessageBeans);
									} else {
										StringUtil.showToast(MesageCenterActivity.this, "没有更多了");
									}
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					@Override
					protected void hideDialog() {
						xrefresh.finishLoadMore();
						xrefresh.finishRefresh();
					}

					@Override
					protected void showDialog() {
						zLoadingView.load();
					}

					@Override
					public void onError(ExceptionHandle.ResponeThrowable e) {
						zLoadingView.loadError();
						mlistview.setVisibility(View.GONE);
						xrefresh.finishLoadMore();
						xrefresh.finishRefresh();
						StringUtil.showToast(MesageCenterActivity.this, "网络异常");
					}
				});
	}
	/**
	 * 发出的消息
	 */
	private void initsendData() {
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("userid", userID);
		maps.put("page", String.valueOf(sendpage));
		RetrofitClient.getInstance(this).createBaseApi().queryPLMyRe(
				maps, new BaseObserver<String>(this) {
					@Override
					public void onNext(String s) {
						try {
							JSONObject jsonObject = new JSONObject(s);
							String content = jsonObject.optString("content");
							if (jsonObject.optString("status").equals("1")) {
								sendMsgBeans = JSON.parseArray(content,SendMsgBean.class);
								if (x == 1){
									if (sendMsgBeans != null && sendMsgBeans.size() > 0) {
										xrefresh.setEnableLoadMore(true);
										sendadapter = new MesageCListSendAdapter(sendMsgBeans, MesageCenterActivity.this);
										mlistview.setAdapter(sendadapter);
										mlistview.setFocusable(true);
										mlistview.setVisibility(View.VISIBLE);
										zLoadingView.loadSuccess();
									}else {
										mlistview.setVisibility(View.GONE);
										zLoadingView.loadSuccess(true);
										xrefresh.setEnableLoadMore(false);
									}
								}else {
									mlistview.setVisibility(View.VISIBLE);
									zLoadingView.loadSuccess();
									if (sendMsgBeans != null && sendMsgBeans.size() > 0) {
										sendadapter.notifyData(sendMsgBeans);
									} else {
										StringUtil.showToast(MesageCenterActivity.this, "没有更多了");
									}
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					@Override
					protected void hideDialog() {
						xrefresh.finishLoadMore();
						xrefresh.finishRefresh();
					}

					@Override
					protected void showDialog() {
						zLoadingView.load();
					}

					@Override
					public void onError(ExceptionHandle.ResponeThrowable e) {
						zLoadingView.loadError();
						mlistview.setVisibility(View.GONE);
						xrefresh.finishLoadMore();
						xrefresh.finishRefresh();
						StringUtil.showToast(MesageCenterActivity.this, "网络异常");
					}
				});
	}

	/**
	 * 收到的消息
	 */
	private void initreceiveData() {
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("userid", userID);
		maps.put("page", String.valueOf(receivepage));
		RetrofitClient.getInstance(this).createBaseApi().queryPLOtherRe(
				maps, new BaseObserver<String>(this) {
					@Override
					public void onNext(String s) {
						try {
							JSONObject jsonObject = new JSONObject(s);
							String content = jsonObject.optString("content");
							if (jsonObject.optString("status").equals("1")) {
								receiceMsgBeans = JSON.parseArray(content,ReceiceMsgBean.class);
								if (x == 1){
									if (receiceMsgBeans != null && receiceMsgBeans.size() > 0) {
										xrefresh.setEnableLoadMore(true);
										receiveadapter = new MesageCListReceiveAdapter(receiceMsgBeans, MesageCenterActivity.this);
										mlistview.setAdapter(receiveadapter);
										mlistview.setFocusable(true);
										mlistview.setVisibility(View.VISIBLE);
										zLoadingView.loadSuccess();
									}else {
										mlistview.setVisibility(View.GONE);
										zLoadingView.loadSuccess(true);
										xrefresh.setEnableLoadMore(false);
									}
								}else {
									mlistview.setVisibility(View.VISIBLE);
									zLoadingView.loadSuccess();
									if (receiceMsgBeans != null && receiceMsgBeans.size() > 0) {
										receiveadapter.notifyData(receiceMsgBeans);
									} else {
										StringUtil.showToast(MesageCenterActivity.this, "没有更多了");
									}
								}
								if (receiveadapter != null) {
									receiceMsgBeans.addAll(receiceMsgBeans);
									receiveadapter.setOnClickListener(new MyClickListener() {
										@Override
										public void onClick(int position) {
											if (receiceMsgBeans != null) {
												msgwenzhangid = receiceMsgBeans.get(position).getWenzhangid();
												msgreid = receiceMsgBeans.get(position).getUid();
												msgplid = receiceMsgBeans.get(position).getPlid();
												sendtext.setVisibility(View.VISIBLE);
												sendhenggang.setVisibility(View.VISIBLE);
												msgEdittext.requestFocus();
												InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
												imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);// 开启或者关闭软键盘
												imm.showSoftInput(msgEdittext, InputMethodManager.SHOW_FORCED);// 弹出软键盘时，焦点定位在输入框中
											}
										}
									});
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					@Override
					protected void hideDialog() {
						xrefresh.finishLoadMore();
						xrefresh.finishRefresh();
					}

					@Override
					protected void showDialog() {
						zLoadingView.load();
					}

					@Override
					public void onError(ExceptionHandle.ResponeThrowable e) {
						zLoadingView.loadError();
						mlistview.setVisibility(View.GONE);
						xrefresh.finishLoadMore();
						xrefresh.finishRefresh();
						StringUtil.showToast(MesageCenterActivity.this, "网络异常");
					}
				});
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
			StringUtil.showToast(this, "评论不能为空");
		} else {
			/* 隐藏软键盘 */
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			if (inputMethodManager.isActive()) {
				inputMethodManager
						.hideSoftInputFromWindow(MesageCenterActivity.this.getCurrentFocus().getWindowToken(), 0);
			}
			sendtext.setVisibility(View.GONE);
			sendhenggang.setVisibility(View.GONE);
			insertPL();
		}
	}

	/**
	 * 插入评论
	 */
	private void insertPL() {
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("userid", userID);
		maps.put("reid", msgreid);
		maps.put("plid", msgplid);
		maps.put("wenzhangid", msgwenzhangid);
		maps.put("content", msgEdittext.getText().toString());
		RetrofitClient.getInstance(this).createBaseApi().insertPL(
				maps, new BaseObserver<String>(this) {
					@Override
					public void onNext(String s) {
						try {
							JSONObject jsonObject = new JSONObject(s);
							if (jsonObject.optString("status").equals("1")) {
								msgEdittext.setText("");
								TabLayout.Tab tabAt = tablayout.getTabAt(1);
								tabAt.select();
								sendpage = 1;
								initsendData();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					@Override
					protected void hideDialog() {
						zLoadingView.loadSuccess();
					}

					@Override
					protected void showDialog() {
						zLoadingView.load();
					}

					@Override
					public void onError(ExceptionHandle.ResponeThrowable e) {
						zLoadingView.loadError();
						mlistview.setVisibility(View.GONE);
						StringUtil.showToast(MesageCenterActivity.this, "网络异常");
					}
				});
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

//	public void systemsetadapter(){
////		systemadapter = new MesageCListSysAdapter(systemlist, this);
//		mlistview.setAdapter(systemadapter);
//		mlistview.setFocusable(true);
//		mlistview.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//				if (!systemlist.get(arg2).get("isread").equals("1")) {
//					systemlist.get(arg2).put("isread", "1");
//					systemadapter.notifyDataSetChanged();
//					Map<String, String> params = new HashMap<>();
//					params.put("userid", userID);
//					params.put("mid", systemlist.get(arg2).get("mid"));
//					dataFlow.requestData(10, "newService/insertMessageRead", params, MesageCenterActivity.this);
//				}
//				if (systemlist.get(arg2).get("eventId").equals("101")) {
////					if (receiveLoadmore) {
////						xrefresh.setAutoLoadMore(true);
////					} else {
////						xrefresh.setLoadComplete(true);
////					}
//					TabLayout.Tab tabAt = tablayout.getTabAt(2);
//					tabAt.select();
//				}
//			}
//		});
//	}

	@Override
	public void doRequestData() {
		switch (curclick) {
			case 0:
				systempage = 1;
				x = 1;
				initsystemData();
				break;
			case 1:
				sendpage = 1;
				x = 1;
				initsendData();
				break;
			case 2:
				receivepage = 1;
				x = 1;
				initreceiveData();
				break;
		}
	}
}
