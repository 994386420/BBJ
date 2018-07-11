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
import com.andview.refreshview.XScrollView;
import com.andview.refreshview.XScrollView.OnScrollListener;
import com.andview.refreshview.XRefreshView.XRefreshViewListener;
import com.bbk.Bean.NewHomeCzgBean;
import com.bbk.adapter.BrowseDomainAdapter;
import com.bbk.adapter.BrowseWenzhangAdapter;
import com.bbk.adapter.CollectionWenzhangAdapter;
import com.bbk.adapter.SsNewCzgAdapter;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.resource.Constants;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.HeaderView;
import com.bbk.view.MyFootView;
import com.bbk.view.MyListView;
import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


public class BrowseActivity extends BaseFragmentActivity implements ResultEvent,OnClickListener{
	private ImageButton topbar_goback_btn;
	private TextView mcompile,mtext2,mtext1,topbar_title_iv;
	private RelativeLayout mtopic,mdomain;
	private View henggang1,henggang2;
	private MyListView topiclistview,domainlistview;
	private List<Map<String, String>> topiclist,domainlist;
	private DataFlow dataFlow;
	private int topicpage=1,domainpage=1;
	private String token;
	private String topiccount="",domaincount="";
	private BrowseWenzhangAdapter topicadapter;
	private BrowseDomainAdapter domainadapter;
	private int curclick = 0;
	private List<TextView> tlist = new ArrayList<>();
	private List<View> vlist = new ArrayList<>();
	private LinearLayout mzhanwei;
	private SmartRefreshLayout xrefresh;
	private boolean isclear = true;
	private boolean topicloadmore = false;
	private boolean domainloadmore = false;
	private String wztitle = "";
	private XScrollView mscroll;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blank);
		View topView = findViewById(R.id.parentview);
		// 实现沉浸式状态栏
		ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
		dataFlow = new DataFlow(this);
		
		initView();
		initTopicData(true);
	}
	
	public void initView() {
		token = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "token");
		topiclist = new ArrayList<>();
		domainlist = new ArrayList<>();
		topbar_goback_btn = (ImageButton) findViewById(R.id.topbar_goback_btn);
		topbar_title_iv = (TextView) findViewById(R.id.topbar_title_iv);
		topbar_title_iv.setText("我的足迹");
		mcompile = (TextView) findViewById(R.id.mcompile);
		mtopic = (RelativeLayout) findViewById(R.id.mtopic);
		mdomain = (RelativeLayout) findViewById(R.id.mdomain);
		topiclistview = (MyListView) findViewById(R.id.topiclistview);
		domainlistview = (MyListView) findViewById(R.id.domainlistview);
//		mscroll = (XScrollView) findViewById(R.id.mscroll);
		mzhanwei = (LinearLayout) findViewById(R.id.mzhanwei);
		xrefresh = (SmartRefreshLayout) findViewById(R.id.xrefresh);
		refreshAndloda();
		mtext1 = (TextView) findViewById(R.id.mtext1);
		mtext2 = (TextView) findViewById(R.id.mtext2);
		henggang1 = findViewById(R.id.henggang1);
		henggang2 = findViewById(R.id.henggang2);
		tlist.add(mtext1);
		tlist.add(mtext2);
		vlist.add(henggang1);
		vlist.add(henggang2);
		
		
		topbar_goback_btn.setOnClickListener(this);
		mtopic.setOnClickListener(this);
		mdomain.setOnClickListener(this);
		
	}
	
	public void initTopicData(boolean is) {
		Map<String, String> params = new HashMap<>();
		params.put("userid", token);
		params.put("type", "2");
		params.put("page", String.valueOf(topicpage));
		dataFlow.requestData(1, "newService/queryArticlesFootAndCollect", params, this,is);
	}
	public void initDomainData(boolean is) {
		String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
		Map<String, String> params = new HashMap<>();
		params.put("userid", userID);
//		params.put("type", "2");
//		params.put("page", String.valueOf(domainpage));
		dataFlow.requestData(2, "newService/queryFootPrintByUserid", params, this,is);
	}
	private void refreshAndloda() {
		xrefresh.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(final RefreshLayout refreshlayout) {
				isclear = true;
				switch (curclick) {
					case 0:
						topicpage = 1;
						initTopicData(false);
						break;
					case 1:
						domainpage = 1;
						initDomainData(false);
						break;
				}
			}
		});
		xrefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore(RefreshLayout refreshlayout) {
				switch (curclick) {
					case 0:
						if (topicloadmore) {
							topicpage++;
							initTopicData(false);
						}
						break;
					case 1:
						if (domainloadmore) {
							domainpage++;
							initDomainData(false);
						}
						break;

				}
			}
		});
//		mscroll.setOnScrollListener(new OnScrollListener() {
//
//			@Override
//			public void onScrollStateChanged(ScrollView view, int scrollState, boolean arriveBottom) {
//				switch (scrollState) {
//				case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
//				case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
//					// 当ListView处于滑动状态时，停止加载图片，保证操作界面流畅
//					Glide.with(BrowseActivity.this).pauseRequests();
//					break;
//				case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
//					// 当ListView处于静止状态时，继续加载图片
//					Glide.with(BrowseActivity.this).resumeRequests();
//					break;
//				}
//			}
//
//			@Override
//			public void onScroll(int l, int t, int oldl, int oldt) {
//				// TODO Auto-generated method stub
//
//			}
//		});
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topbar_goback_btn:
			finish();
			break;
		case R.id.mtopic:
			clickTabtitle(0);
			
			break;
		case R.id.mdomain:
			clickTabtitle(1);
			
			break;

		default:
			break;
		}
	}
	private void clickTabtitle(int i) {
		if (curclick != i) {
			tlist.get(0).setTextColor(Color.parseColor("#333333"));
			tlist.get(1).setTextColor(Color.parseColor("#333333"));
			vlist.get(0).setBackgroundColor(Color.parseColor("#ffffff"));
			vlist.get(1).setBackgroundColor(Color.parseColor("#ffffff"));
			tlist.get(i).setTextColor(Color.parseColor("#ff7d41"));
			vlist.get(i).setBackgroundColor(Color.parseColor("#ff7d41"));
			switch (i) {
			case 0:
				xrefresh.setEnableLoadMore(true);
				domainlistview.setVisibility(View.GONE);
//				if (topiccount.isEmpty() || topiccount.equals("")) {
//					topiclistview.setVisibility(View.GONE);
					mtext1.setText("鲸话题");
//				}else{
					topiclistview.setVisibility(View.VISIBLE);
//					mtext1.setText("鲸话题("+topiccount+")");
//				}
				mtext2.setText("商品");
				curclick = 0;
				if (topicadapter == null) {
					isclear = true;
					initTopicData(true);
				}
				break;
			case 1:
				xrefresh.setEnableLoadMore(false);
				topiclistview.setVisibility(View.GONE);
//				if (domaincount.isEmpty() || domaincount.equals("")) {
//					domainlistview.setVisibility(View.GONE);
					mtext2.setText("商品");
//				}else{
					domainlistview.setVisibility(View.VISIBLE);
//					mtext2.setText("商品("+domaincount+")");
//				}
				mtext1.setText("鲸话题");
				curclick = 1;
				if (domainadapter == null) {
					isclear = true;
					initDomainData(true);
				}
				break;
			default:
				break;
			}
		}

	}
	private void loadTopic(JSONObject object1) throws JSONException {
		JSONArray list = object1.getJSONArray("list");
		if (list.length()!= 0) {
			int j = Integer.valueOf(topiccount);
			if (list.length()<10 || (Integer.valueOf(topiccount)%10 == 0 && j/10 == topicpage)) {
				topicloadmore = false;
				xrefresh.setEnableLoadMore(false);
			}else{
				topicloadmore = true;
				xrefresh.setEnableLoadMore(true);
			}
			for (int i = 0; i < list.length(); i++) {
				JSONObject object = list.getJSONObject(i);
				Map<String, String> map = new HashMap<>();
				map.put("author", object.optString("author"));
				map.put("title", object.optString("title"));
				if (!object.optString("img").isEmpty()) {
					map.put("img", object.optString("img"));
				}else{
					map.put("img", "1");
				}
				map.put("zan", object.optString("zan"));
				map.put("count", object.optString("count"));
				map.put("htmlid", object.optString("htmlid"));
				map.put("id", object.optString("id"));
				topiclist.add(map);
			}
			topicadapter = new BrowseWenzhangAdapter(topiclist, this);
			topiclistview.setAdapter(topicadapter);
			topiclistview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					wztitle = topiclist.get(arg2).get("title");
					String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),
							"userInfor", "userID");
					String token = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),
							"userInfor", "token");
					HashMap<String, String> params = new HashMap<String, String>();
					params.put("userid", userID);
					params.put("wzid", topiclist.get(arg2).get("id"));
					params.put("token", token);
					params.put("type", "2");
					dataFlow.requestData(4, "newService/insertWenzhangGuanzhu", params,
							BrowseActivity.this);
				}
			});
			topiclistview.setVisibility(View.VISIBLE);
		}else{
			if (isclear) {
				topiclistview.setVisibility(View.GONE);
			}
		}
		
	}

	private void loadDomain(JSONObject object1) throws JSONException {
		JSONArray list = object1.getJSONArray("list");
		if (list.length()!= 0) {
			int j = Integer.valueOf(domaincount);
			if (list.length()<10 || (Integer.valueOf(domaincount)%10 == 0 && j/10 == domainpage)) {
				
				domainloadmore = false;
				xrefresh.setEnableLoadMore(false);
			}else{
				domainloadmore = true;
				xrefresh.setEnableLoadMore(true);
			}
			for (int i = 0; i < list.length(); i++) {
				JSONObject object = list.getJSONObject(i);
				Map<String, String> map = new HashMap<>();
				map.put("title", object.optString("title"));
				map.put("url", object.optString("url"));
				if (!object.optString("price").isEmpty()) {
					map.put("price", object.optString("price"));
					map.put("imgurl", object.optString("imgurl"));
					map.put("sales", object.optString("sales"));
					map.put("producttype", object.optString("producttype"));
					map.put("rowkey", object.optString("rowkey"));
				}
				domainlist.add(map);
			}
			domainadapter = new BrowseDomainAdapter(domainlist, this);
			domainlistview.setAdapter(domainadapter);
			domainlistview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					Intent intent = new Intent(BrowseActivity.this, WebViewActivity.class);
					intent.putExtra("url", domainlist.get(arg2).get("url"));
					if (domainlist.get(arg2).get("price") != null && domainlist.get(arg2).get("price") != "") {
						intent.putExtra("rowkey", domainlist.get(arg2).get("rowkey"));
					}
					startActivity(intent);
				}
			});
			domainlistview.setVisibility(View.VISIBLE);
		}else{
			if (isclear) {
				domainlistview.setVisibility(View.GONE);
			}
		}
		
	}
	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
		xrefresh.finishLoadMore();
		xrefresh.finishRefresh();
		switch (requestCode) {
		case 1:
			
			try {
				JSONObject object = new JSONObject(content);
				if (isclear) {
					topiclist.clear();
					topiccount = object.optString("count");
//					if (topiccount.isEmpty() || topiccount.equals("")) {
						mtext1.setText("鲸话题");
//					}else{
//						mtext1.setText("鲸话题("+topiccount+")");
//					}
					mtext2.setText("商品");
				}
				loadTopic(object);
				isclear = false;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			break;
		case 2:
			try {
//				JSONObject object = new JSONObject(content);
//				if (isclear) {
//					domainlist.clear();
////					domaincount = object.optString("count");
//					if (domaincount.isEmpty() || domaincount.equals("")) {
//						mtext2.setText("商品");
//					}else{
//						mtext2.setText("商品("+domaincount+")");
//					}
//					mtext1.setText("鲸话题");
//				}
//				Log.i("===========",content);
				List<NewHomeCzgBean> newHomeCzgBean = JSON.parseArray(content,NewHomeCzgBean.class);
//				loadDomain(object);
				if (newHomeCzgBean != null && newHomeCzgBean.size() > 0) {
					SsNewCzgAdapter ssNewCzgAdapter = new SsNewCzgAdapter(this, newHomeCzgBean);
					domainlistview.setAdapter(ssNewCzgAdapter);
				}else {
					mzhanwei.setVisibility(View.VISIBLE);
					domainlistview.setVisibility(View.GONE);
				}
				isclear = false;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 4:
			Intent intent = new Intent(BrowseActivity.this, WebViewWZActivity.class);
			intent.putExtra("url", content);
			intent.putExtra("title", wztitle);
			startActivity(intent);
			break;
		default:
			break;
		}
		
	}




}
