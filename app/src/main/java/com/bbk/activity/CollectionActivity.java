package com.bbk.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XScrollView;
import com.andview.refreshview.XRefreshView.XRefreshViewListener;
import com.andview.refreshview.XScrollView.OnScrollListener;
import com.bbk.activity.R.id;
import com.bbk.adapter.BrowseDomainAdapter;
import com.bbk.adapter.CollectionDomainAdapter;
import com.bbk.adapter.CollectionWenzhangAdapter;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.resource.Constants;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.MyFootView;
import com.bbk.view.MyListView;
import com.bumptech.glide.Glide;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class CollectionActivity extends BaseFragmentActivity implements ResultEvent, OnClickListener {
	private ImageButton topbar_goback_btn;
	private TextView mcompile, mtext2, mtext1, topbar_title_iv;
	private RelativeLayout mtopic, mdomain, mselectanddelete, mdelete;
	private View henggang1, henggang2;
	private MyListView topiclistview, domainlistview;
	private List<Map<String, String>> topiclist, domainlist, datalist;
	private DataFlow dataFlow;
	private int topicpage = 1, domainpage = 1;
	private String userID;
	private String topiccount = "", domaincount = "";
	private CollectionWenzhangAdapter topicadapter;
	private CollectionDomainAdapter domainadapter;
	private int curclick = 0;
	private List<TextView> tlist = new ArrayList<>();
	private List<View> vlist = new ArrayList<>();
	private boolean iscompile = false;
	private boolean isallselect = false;
	private boolean isclear = true;
	private LinearLayout mallselect;
	private ImageView mallselectimg;
	private String ids = "";
	private XRefreshView xrefresh;
	private LinearLayout mzhanwei;
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
		if (getIntent().getStringExtra("type") != null) {
			initDomainData();
		} else {
			initTopicData();
		}

	}

	public void initView() {
		userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
		topiclist = new ArrayList<>();
		domainlist = new ArrayList<>();
		datalist = new ArrayList<>();
		topbar_goback_btn = (ImageButton) findViewById(R.id.topbar_goback_btn);
		topbar_title_iv = (TextView) findViewById(R.id.topbar_title_iv);
		mcompile = (TextView) findViewById(R.id.mcompile);
		mtopic = (RelativeLayout) findViewById(R.id.mtopic);
		mzhanwei = (LinearLayout) findViewById(R.id.mzhanwei);
		mdomain = (RelativeLayout) findViewById(R.id.mdomain);
		mselectanddelete = (RelativeLayout) findViewById(R.id.mselectanddelete);
		mdelete = (RelativeLayout) findViewById(R.id.mdelete);
		topiclistview = (MyListView) findViewById(R.id.topiclistview);
		domainlistview = (MyListView) findViewById(R.id.domainlistview);
		mscroll = (XScrollView) findViewById(R.id.mscroll);
		mallselectimg = (ImageView) findViewById(R.id.mallselectimg);
		mallselect = (LinearLayout) findViewById(R.id.mallselect);
		xrefresh = (XRefreshView) findViewById(R.id.xrefresh);
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
		mcompile.setOnClickListener(this);
		mdelete.setOnClickListener(this);
		mallselect.setOnClickListener(this);

	}

	public void initTopicData() {
		Map<String, String> params = new HashMap<>();
		params.put("userid", userID);
		params.put("type", "1");
		params.put("page", String.valueOf(topicpage));
		dataFlow.requestData(1, "newService/queryArticlesFootAndCollect", params, this);
	}

	public void initDomainData() {
		Map<String, String> params = new HashMap<>();
		params.put("userid", userID);
		params.put("type", "1");
		params.put("page", String.valueOf(domainpage));
		dataFlow.requestData(2, "newService/queryProductFootAndCollect", params, this);
	}

	public void deleteTopicData() {
		Map<String, String> params = new HashMap<>();
		params.put("userid", userID);
		params.put("ids", ids);
		dataFlow.requestData(3, "newService/deleteArticleCollect", params, this);
	}

	public void deleteDomainData() {
		Map<String, String> params = new HashMap<>();
		params.put("userid", userID);
		params.put("ids", ids);
		dataFlow.requestData(3, "newService/deleteProductCollect", params, this);
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
					topicpage = 1;
					initTopicData();
					break;
				case 1:
					domainpage = 1;
					initDomainData();
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
					topicpage++;
					initTopicData();
					break;
				case 1:
					domainpage++;
					initDomainData();
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
		mscroll.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(ScrollView view, int scrollState, boolean arriveBottom) {
				switch (scrollState) {
				case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
				case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
					// 当ListView处于滑动状态时，停止加载图片，保证操作界面流畅
					Glide.with(CollectionActivity.this).pauseRequests();
					break;
				case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
					// 当ListView处于静止状态时，继续加载图片
					Glide.with(CollectionActivity.this).resumeRequests();
					break;
				}
			}

			@Override
			public void onScroll(int l, int t, int oldl, int oldt) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topbar_goback_btn:
			finish();
			break;
		case R.id.mtopic:
			if (!iscompile) {
				clickTabtitle(0);
			}
			break;
		case R.id.mdomain:
			if (!iscompile) {
				clickTabtitle(1);
			}
			break;
		case R.id.mcompile:
			switch (curclick) {
			case 0:
				compile(topiclist, topicadapter);
				break;
			case 1:
				compile(domainlist, domainadapter);
				break;
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
		case R.id.mdelete:
			switch (curclick) {
			case 0:
				Delete(topiclist, topicadapter);
				if (ids != "") {
					ids = ids.substring(0, ids.length() - 1);
					deleteTopicData();
					ids = "";
				}
				break;
			case 1:
				Delete(domainlist, domainadapter);
				if (ids != "") {
					ids = ids.substring(0, ids.length() - 1);
					deleteDomainData();
					ids = "";
				}
				break;
			}
			if (isallselect) {
				mallselectimg.setImageResource(R.mipmap.weixuanzhongyuan);
				isallselect = false;
			}
			mcompile.setText("编辑");
			mselectanddelete.setVisibility(View.GONE);
			iscompile = false;
			break;
		default:
			break;
		}

	}

	private void allselect(String string) {
		switch (curclick) {
		case 0:
			for (int i = 0; i < topiclist.size(); i++) {
				topiclist.get(i).put("isselect", string);
			}
			topicadapter.notifyDataSetChanged();
			break;
		case 1:
			for (int i = 0; i < domainlist.size(); i++) {
				domainlist.get(i).put("isselect", string);
			}
			domainadapter.notifyDataSetChanged();
			break;
		}
	}

	private void Delete(List<Map<String, String>> list, BaseAdapter adapter) {
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

	private void compile(List<Map<String, String>> list, BaseAdapter adapter) {
		if (iscompile) {
			mcompile.setText("编辑");
			mselectanddelete.setVisibility(View.GONE);
			for (int i = 0; i < list.size(); i++) {
				list.get(i).put("isbianji", "0");
			}
			adapter.notifyDataSetChanged();
			iscompile = false;
		} else {
			mcompile.setText("完成");
			mselectanddelete.setVisibility(View.VISIBLE);
			for (int i = 0; i < list.size(); i++) {
				list.get(i).put("isbianji", "1");
			}
			adapter.notifyDataSetChanged();
			iscompile = true;
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
				if (topiccount.isEmpty() || topiccount.equals("")) {
					mzhanwei.setVisibility(View.VISIBLE);
					mcompile.setVisibility(View.GONE);
					mtext1.setText("鲸话题");
				} else {
					mzhanwei.setVisibility(View.GONE);
					topiclistview.setVisibility(View.VISIBLE);
					mcompile.setVisibility(View.VISIBLE);
					mtext1.setText("鲸话题(" + topiccount + ")");
				}
				mtext2.setText("商品");
				domainlistview.setVisibility(View.GONE);
				curclick = 0;
				if (topicadapter == null) {
					isclear = true;
					initTopicData();
				}
				break;
			case 1:
				if (domaincount.isEmpty() || domaincount.equals("")) {
					mzhanwei.setVisibility(View.VISIBLE);
					mcompile.setVisibility(View.GONE);
					mtext2.setText("商品");
				} else {
					mzhanwei.setVisibility(View.GONE);
					domainlistview.setVisibility(View.VISIBLE);
					mcompile.setVisibility(View.VISIBLE);
					mtext2.setText("商品(" + domaincount + ")");
				}
				mtext1.setText("鲸话题");
				topiclistview.setVisibility(View.GONE);
				curclick = 1;
				if (domainadapter == null) {
					isclear = true;
					initDomainData();
				}
				break;
			default:
				break;
			}
		}

	}

	private void loadTopic(JSONObject object1) throws JSONException {
		JSONArray list = object1.getJSONArray("list");
		if (list.length() != 0) {
			mcompile.setVisibility(View.VISIBLE);
			mzhanwei.setVisibility(View.GONE);
			int j = Integer.valueOf(topiccount);
			if (list.length() < 10 || (Integer.valueOf(topiccount) % 10 == 0 && j / 10 == topicpage)) {
				topicloadmore = false;
				xrefresh.setLoadComplete(true);
			} else {
				topicloadmore = true;
				xrefresh.setAutoLoadMore(true);
			}
			for (int i = 0; i < list.length(); i++) {
				JSONObject object = list.getJSONObject(i);
				Map<String, String> map = new HashMap<>();
				map.put("author", object.optString("author"));
				map.put("title", object.optString("title"));
				map.put("isselect", "0");
				if (iscompile) {
					map.put("isbianji", "1");
				} else {
					map.put("isbianji", "0");
				}
				if (!object.optString("img").isEmpty()) {
					map.put("img", object.optString("img"));
				} else {
					map.put("img", "1");
				}
				map.put("zan", object.optString("zan"));
				map.put("count", object.optString("count"));
				map.put("htmlid", object.optString("htmlid"));
				map.put("id", object.optString("id"));
				topiclist.add(map);
			}
			if (topicadapter != null) {
				topicadapter.notifyDataSetChanged();
			} else {
				topicadapter = new CollectionWenzhangAdapter(topiclist, this);
				topiclistview.setAdapter(topicadapter);
				topiclistview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						if (iscompile) {
							// 编辑
							if (isallselect) {
								mallselectimg.setImageResource(R.mipmap.weixuanzhongyuan);
								isallselect = false;
							}
							String isselect = topiclist.get(arg2).get("isselect");
							if (isselect.equals("1")) {
								topiclist.get(arg2).put("isselect", "0");
							} else {
								topiclist.get(arg2).put("isselect", "1");
							}
							topicadapter.notifyDataSetChanged();
						} else {
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
									CollectionActivity.this);
						}
					}
				});
			}
			topiclistview.setVisibility(View.VISIBLE);
		} else {
			if (isclear) {
				topiclistview.setVisibility(View.GONE);
				mcompile.setVisibility(View.GONE);
			}
		}

	}

	private void loadDomain(JSONObject object1) throws JSONException {
		JSONArray list = object1.getJSONArray("list");
		if (list.length() != 0) {
			mcompile.setVisibility(View.VISIBLE);
			mzhanwei.setVisibility(View.GONE);
			int j = Integer.valueOf(domaincount);
			if (list.length() < 10 || (Integer.valueOf(domaincount) % 10 == 0 && j / 10 == domainpage)) {
				domainloadmore = false;
				xrefresh.setLoadComplete(true);
			} else {
				domainloadmore = true;
				xrefresh.setAutoLoadMore(true);
			}
			for (int i = 0; i < list.length(); i++) {
				JSONObject object = list.getJSONObject(i);
				Map<String, String> map = new HashMap<>();
				map.put("title", object.optString("title"));
				map.put("url", object.optString("url"));
				map.put("isselect", "0");
				if (iscompile) {
					map.put("isbianji", "1");
				} else {
					map.put("isbianji", "0");
				}
				map.put("id", object.optString("id"));
				if (!object.optString("price").isEmpty()) {
					map.put("price", object.optString("price"));
					map.put("imgurl", object.optString("imgurl"));
					map.put("sales", object.optString("sales"));
					map.put("producttype", object.optString("producttype"));
					map.put("rowkey", object.optString("rowkey"));
				}
				domainlist.add(map);
			}
			if (domainadapter != null) {
				domainadapter.notifyDataSetChanged();
			} else {
				domainadapter = new CollectionDomainAdapter(domainlist, this);
				domainlistview.setAdapter(domainadapter);
				domainlistview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						if (iscompile) {
							// 编辑
							if (isallselect) {
								mallselectimg.setImageResource(R.mipmap.weixuanzhongyuan);
								isallselect = false;
							}
							String isselect = domainlist.get(arg2).get("isselect");
							if (isselect.equals("1")) {
								domainlist.get(arg2).put("isselect", "0");
							} else {
								domainlist.get(arg2).put("isselect", "1");
							}
							domainadapter.notifyDataSetChanged();
						} else {
							// 跳转
							Intent intent = new Intent(CollectionActivity.this, WebViewActivity.class);
							intent.putExtra("url", domainlist.get(arg2).get("url"));
							if (domainlist.get(arg2).get("price") != null && domainlist.get(arg2).get("price") != "") {
								intent.putExtra("rowkey", domainlist.get(arg2).get("rowkey"));
							}
							startActivity(intent);
						}
					}
				});
			}
			domainlistview.setVisibility(View.VISIBLE);
		} else {
			if (isclear) {
				domainlistview.setVisibility(View.GONE);
				mcompile.setVisibility(View.GONE);
			}
		}

	}

	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
		xrefresh.stopLoadMore();
		xrefresh.stopRefresh();
		switch (requestCode) {
		case 1:

			try {
				JSONObject object = new JSONObject(content);
				if (isclear) {
					topiclist.clear();
					topiccount = object.optString("count");
					if (topiccount.isEmpty() || topiccount.equals("")) {
						mtext1.setText("鲸话题");
					} else {
						mtext1.setText("鲸话题(" + topiccount + ")");
					}
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
				JSONObject object = new JSONObject(content);
				if (isclear) {
					domainlist.clear();
					domaincount = object.optString("count");
					if (domaincount.isEmpty() || domaincount.equals("")) {
						mtext2.setText("商品");
					} else {
						mtext2.setText("商品(" + domaincount + ")");
					}
					mtext1.setText("鲸话题");

				}
				loadDomain(object);
				isclear = false;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 3:
			isclear = true;
			switch (curclick) {
			case 0:
				topicpage = 1;
				initTopicData();
				break;
			case 1:
				domainpage = 1;
				initDomainData();
				break;
			}
			break;
		case 4:
			Intent intent = new Intent(CollectionActivity.this, WebViewWZActivity.class);
			intent.putExtra("url", content);
			intent.putExtra("title", wztitle);
			startActivity(intent);
			break;
		default:
			break;
		}

	}

}
