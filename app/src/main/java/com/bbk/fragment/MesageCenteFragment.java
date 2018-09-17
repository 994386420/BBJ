package com.bbk.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.ReceiceMsgBean;
import com.bbk.Bean.SendMsgBean;
import com.bbk.Bean.SystemMessageBean;
import com.bbk.activity.BaseActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.adapter.MesageCListReceiveAdapter;
import com.bbk.adapter.MesageCListReceiveAdapter.MyClickListener;
import com.bbk.adapter.MesageCListSendAdapter;
import com.bbk.adapter.MesageCListSysAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.flow.DataFlow;
import com.bbk.util.DialogMessageCenterUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.SoftHideKeyBoardUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.CommonLoadingView;
import com.logg.Logg;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class MesageCenteFragment extends BaseViewPagerFragment implements OnClickListener, OnLayoutChangeListener ,CommonLoadingView.LoadingHandler{

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
	private View mView;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (null == mView) {
			mView = inflater.inflate(R.layout.activity_mesage_center, null);
//			SoftHideKeyBoardUtil.assistActivity(getActivity(),getStatusBarHeight(getActivity()));
			getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
			View topView = mView.findViewById(R.id.parentview);
			// 实现沉浸式状态栏
			ImmersedStatusbarUtils.initAfterSetContentView(getActivity(), topView);
//			getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
//					| WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//			if (getIntent().getStringExtra("type") != null) {
//				type = getIntent().getStringExtra("type");
//			}
			dataFlow = new DataFlow(getActivity());
			initView();
		}
		return mView;
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
		zLoadingView = mView.findViewById(R.id.progress);
		zLoadingView.setLoadingHandler(this);
		systemlist = new ArrayList<>();
		sendlist = new ArrayList<>();
		receivelist = new ArrayList<>();
		topbar_goback_btn = (ImageButton) mView.findViewById(R.id.topbar_goback_btn);
		topbar_goback_btn.setVisibility(View.GONE);
		parentview = (RelativeLayout)mView. findViewById(R.id.parentview);
		sendtext = (LinearLayout) mView.findViewById(R.id.sendtext);
		msgEdittext = (EditText) mView.findViewById(R.id.msgEdittext);
		sendhenggang = mView.findViewById(R.id.sendhenggang);
		mhuifusend = (TextView) mView.findViewById(R.id.mhuifusend);
		// 获取屏幕高度
		screenHeight = getActivity().getWindowManager().getDefaultDisplay().getHeight();
		// 阀值设置为屏幕高度的1/3
		keyHeight = screenHeight / 3;
		mlistview = (ListView)mView. findViewById(R.id.mlistview);
		tablayout = (TabLayout)mView. findViewById(R.id.tablayout);
		xrefresh = (SmartRefreshLayout)mView. findViewById(R.id.xrefresh);
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
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(msgEdittext.getWindowToken(), 0);
		sendtext.setVisibility(View.GONE);
		switch (curclick) {
			case 0:
				systempage = 1;
				DialogMessageCenterUtil.show(getActivity());
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
		xrefresh.setNoMoreData(false);
		userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("userid", userID);
		maps.put("page", String.valueOf(systempage));
		RetrofitClient.getInstance(getActivity()).createBaseApi().querySysTMessage(
				maps, new BaseObserver<String>(getActivity()) {
					@Override
					public void onNext(String s) {
						try {
							JSONObject jsonObject = new JSONObject(s);
							String content = jsonObject.optString("content");
							if (jsonObject.optString("status").equals("1")) {
//								    Logg.json(jsonObject);
									systemMessageBeans = JSON.parseArray(content,SystemMessageBean.class);
									DialogMessageCenterUtil.dismiss(0);
								if (x == 1){
									if (systemMessageBeans != null && systemMessageBeans.size() > 0) {
										xrefresh.setEnableLoadMore(true);
										systemadapter = new MesageCListSysAdapter(systemMessageBeans, getActivity());
										mlistview.setAdapter(systemadapter);
										mlistview.setFocusable(true);
										mlistview.setVisibility(View.VISIBLE);
										zLoadingView.loadSuccess();
									}else {
										zLoadingView.setVisibility(View.VISIBLE);
										mlistview.setVisibility(View.GONE);
										zLoadingView.loadHomeSuccess(getActivity(),"登录后才能查看消息哦","去登录",true);
										xrefresh.setEnableLoadMore(false);
									}
								}else {
									mlistview.setVisibility(View.VISIBLE);
									zLoadingView.loadSuccess();
									if (systemMessageBeans != null && systemMessageBeans.size() > 0) {
										systemadapter.notifyData(systemMessageBeans);
									} else {
//										StringUtil.showToast(MesageCenterActivity.this, "没有更多了");
										xrefresh.finishLoadMoreWithNoMoreData();
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
//						zLoadingView.load();
					}

					@Override
					public void onError(ExceptionHandle.ResponeThrowable e) {
						DialogMessageCenterUtil.dismiss(0);
						zLoadingView.setVisibility(View.VISIBLE);
						zLoadingView.loadError();
						mlistview.setVisibility(View.GONE);
						xrefresh.finishLoadMore();
						xrefresh.finishRefresh();
						StringUtil.showToast(getActivity(), e.message);
					}
				});
	}
	/**
	 * 发出的消息
	 */
	private void initsendData() {
		xrefresh.setNoMoreData(false);
		userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("userid", userID);
		maps.put("page", String.valueOf(sendpage));
		RetrofitClient.getInstance(getActivity()).createBaseApi().queryPLMyRe(
				maps, new BaseObserver<String>(getActivity()) {
					@Override
					public void onNext(String s) {
						try {
							JSONObject jsonObject = new JSONObject(s);
							String content = jsonObject.optString("content");
							if (jsonObject.optString("status").equals("1")) {
								sendMsgBeans = JSON.parseArray(content,SendMsgBean.class);
								DialogMessageCenterUtil.dismiss(0);
								if (x == 1){
									if (sendMsgBeans != null && sendMsgBeans.size() > 0) {
										xrefresh.setEnableLoadMore(true);
										sendadapter = new MesageCListSendAdapter(sendMsgBeans, getActivity());
										mlistview.setAdapter(sendadapter);
										mlistview.setFocusable(true);
										mlistview.setVisibility(View.VISIBLE);
										zLoadingView.loadSuccess();
									}else {
										zLoadingView.setVisibility(View.VISIBLE);
										mlistview.setVisibility(View.GONE);
										zLoadingView.loadHomeSuccess(getActivity(),"登录后才能查看评论哦","去登录",true);
										xrefresh.setEnableLoadMore(false);
									}
								}else {
									mlistview.setVisibility(View.VISIBLE);
									zLoadingView.loadSuccess();
									if (sendMsgBeans != null && sendMsgBeans.size() > 0) {
										sendadapter.notifyData(sendMsgBeans);
									} else {
//										StringUtil.showToast(MesageCenterActivity.this, "没有更多了");
										xrefresh.finishLoadMoreWithNoMoreData();
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
						DialogMessageCenterUtil.show(getActivity());
					}

					@Override
					public void onError(ExceptionHandle.ResponeThrowable e) {
						DialogMessageCenterUtil.dismiss(0);
						zLoadingView.setVisibility(View.VISIBLE);
						zLoadingView.loadError();
						mlistview.setVisibility(View.GONE);
						xrefresh.finishLoadMore();
						xrefresh.finishRefresh();
						StringUtil.showToast(getActivity(), e.message);
					}
				});
	}

	/**
	 * 收到的消息
	 */
	private void initreceiveData() {
		xrefresh.setNoMoreData(false);
		userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("userid", userID);
		maps.put("page", String.valueOf(receivepage));
		RetrofitClient.getInstance(getActivity()).createBaseApi().queryPLOtherRe(
				maps, new BaseObserver<String>(getActivity()) {
					@Override
					public void onNext(String s) {
						try {
							JSONObject jsonObject = new JSONObject(s);
							String content = jsonObject.optString("content");
							if (jsonObject.optString("status").equals("1")) {
								receiceMsgBeans = JSON.parseArray(content,ReceiceMsgBean.class);
								DialogMessageCenterUtil.dismiss(0);
								if (x == 1){
									if (receiceMsgBeans != null && receiceMsgBeans.size() > 0) {
										xrefresh.setEnableLoadMore(true);
										receiveadapter = new MesageCListReceiveAdapter(receiceMsgBeans, getActivity());
										mlistview.setAdapter(receiveadapter);
										mlistview.setFocusable(true);
										mlistview.setVisibility(View.VISIBLE);
										zLoadingView.loadSuccess();
									}else {
										zLoadingView.setVisibility(View.VISIBLE);
										mlistview.setVisibility(View.GONE);
										zLoadingView.loadHomeSuccess(getActivity(),"登录后才能查看评论哦","去登录",true);
										xrefresh.setEnableLoadMore(false);
									}
								}else {
									mlistview.setVisibility(View.VISIBLE);
									zLoadingView.loadSuccess();
									if (receiceMsgBeans != null && receiceMsgBeans.size() > 0) {
										receiveadapter.notifyData(receiceMsgBeans);
									} else {
//										StringUtil.showToast(MesageCenterActivity.this, "没有更多了");
										xrefresh.finishLoadMoreWithNoMoreData();
									}
								}
								if (receiveadapter != null) {
									receiveadapter.setOnClickListener(new MyClickListener() {
										@Override
										public void onClick(int position,String wzid,String plid,String uid) {
												msgwenzhangid = wzid;
												msgreid = uid;
												msgplid = plid;
												sendtext.setVisibility(View.VISIBLE);
												sendhenggang.setVisibility(View.VISIBLE);
												msgEdittext.requestFocus();
												InputMethodManager imm = (InputMethodManager)getActivity(). getSystemService(INPUT_METHOD_SERVICE);
												imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);// 开启或者关闭软键盘
												imm.showSoftInput(msgEdittext, InputMethodManager.SHOW_FORCED);// 弹出软键盘时，焦点定位在输入框中
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
						DialogMessageCenterUtil.show(getActivity());
					}

					@Override
					public void onError(ExceptionHandle.ResponeThrowable e) {
						DialogMessageCenterUtil.dismiss(0);
						zLoadingView.setVisibility(View.VISIBLE);
						zLoadingView.loadError();
						mlistview.setVisibility(View.GONE);
						xrefresh.finishLoadMore();
						xrefresh.finishRefresh();
						StringUtil.showToast(getActivity(), e.message);
					}
				});
	}

//	@Override
//	public boolean dispatchKeyEvent(KeyEvent event) {
//		if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
//			send();
//			return true;
//		}
//		// return true;
//		return super.dispatchKeyEvent(event);
//		// 必不可少，否则所有的组件都不会有TouchEvent了
//	}
	private void send(){
		if (msgEdittext.getText().toString().isEmpty()) {
			StringUtil.showToast(getActivity(), "评论不能为空");
		} else {
			/* 隐藏软键盘 */
			InputMethodManager inputMethodManager = (InputMethodManager)getActivity(). getSystemService(INPUT_METHOD_SERVICE);
			if (inputMethodManager.isActive()) {
				inputMethodManager
						.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
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
		userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("userid", userID);
		maps.put("reid", msgreid);
		maps.put("plid", msgplid);
		maps.put("wenzhangid", msgwenzhangid);
		maps.put("content", msgEdittext.getText().toString());
		RetrofitClient.getInstance(getActivity()).createBaseApi().insertPL(
				maps, new BaseObserver<String>(getActivity()) {
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
						DialogMessageCenterUtil.show(getActivity());
					}

					@Override
					public void onError(ExceptionHandle.ResponeThrowable e) {
						DialogMessageCenterUtil.dismiss(0);
						zLoadingView.setVisibility(View.VISIBLE);
						zLoadingView.loadError();
						mlistview.setVisibility(View.GONE);
						StringUtil.showToast(getActivity(), e.message);
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
			break;
		case R.id.parentview:
			InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
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
		zLoadingView.setVisibility(View.GONE);
		switch (curclick) {
			case 0:
				systempage = 1;
				x = 1;
				DialogMessageCenterUtil.show(getActivity());
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

	@Override
	protected void loadLazyData() {
		switch (curclick) {
			case 0:
				systempage = 1;
				x= 1;
				DialogMessageCenterUtil.show(getActivity());
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

	@Override
	public void onResume() {
		super.onResume();
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
}
