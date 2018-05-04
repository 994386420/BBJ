package com.bbk.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshView.XRefreshViewListener;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.WebViewWZActivity;
import com.bbk.adapter.FindListAdapter;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.util.BaseTools;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.SystemBarTintManager;
import com.bbk.view.HeaderView;
import com.bbk.view.MyFootView;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 新版发现页面
 */
public class NewRankFragment extends BaseViewPagerFragment implements ResultEvent {
	private View rank_head, mView;
	private DataFlow dataFlow;
	private ListView mlistView;
	private LinearLayout mbox;
	private int currentIndex = 1;
	private List<Map<String, String>> titlelist;
	private List<Map<String, String>> datalist;
	private FindListAdapter listadapter;
	private boolean isclear = false;
	private String wztitle = "";
	private XRefreshView xrefresh;
	private int topicpage = 1;
	private int typepage = 1;
	private boolean isloadmore = false;


	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
		// return super.onCreateView(inflater, container, savedInstanceState);
		if (null == mView) {
			mView = inflater.inflate(R.layout.fragment_rank, null);
			rank_head = mView.findViewById(R.id.rank_head);
			dataFlow = new DataFlow(getActivity());
			ImmersedStatusbarUtils.FlymeSetStatusBarLightMode(getActivity().getWindow(),true);
			ImmersedStatusbarUtils.MIUISetStatusBarLightMode(getActivity(),true);
			initstateView();
			initView();
		}

		return mView;
	}
	private void initData(boolean is) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("page", topicpage+"");
		params.put("type", "比比鲸原创");
		dataFlow.requestData(1, "newService/queryArticleByType", params, this,is);
	}

	private void loadData() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("type", "比比鲸原创");
		params.put("page", typepage+"");
		dataFlow.requestData(2, "newService/queryArticleByType", params, this);
	}

	private void insertWenzhangGuanzhu(int position) {
		wztitle  = datalist.get(position).get("title");
		String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
		String token = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "token");
		if (!TextUtils.isEmpty(userID)) {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("userid", userID);
			params.put("wzid", datalist.get(position).get("id"));
			params.put("token", token);
			params.put("type", "2");
			dataFlow.requestData(4, "newService/insertWenzhangGuanzhu", params, this);
		} else {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("userid", "-1");
			params.put("token", token);
			params.put("wzid", datalist.get(position).get("id"));
			params.put("type", "2");
			dataFlow.requestData(4, "newService/insertWenzhangGuanzhu", params, this);
		}

	}

	private void initView() {
		titlelist = new ArrayList<>();
		datalist = new ArrayList<>();
		xrefresh =  mView.findViewById(R.id.xrefresh);
		xrefresh.setCustomHeaderView(new HeaderView(getActivity()));
		refreshAndloda();
		mlistView =  mView.findViewById(R.id.mlistview);
		mlistView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
				case OnScrollListener.SCROLL_STATE_FLING:
					// 当ListView处于滑动状态时，停止加载图片，保证操作界面流畅
					Glide.with(getActivity()).pauseRequests();
					break;
				case OnScrollListener.SCROLL_STATE_IDLE:
					// 当ListView处于静止状态时，继续加载图片
					Glide.with(getActivity()).resumeRequests();
					break;
				}
			}
			
			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
		});
		mbox = mView.findViewById(R.id.mbox);

	}
	private void refreshAndloda() {
		xrefresh.setXRefreshViewListener(new XRefreshViewListener() {
			
			@Override
			public void onRelease(float direction) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onRefresh(boolean isPullDown) {
				if (currentIndex == 1) {
					isclear = true;
					topicpage = 1;
					initData(true);
				}else if(currentIndex > 1){
					isclear = true;
					typepage = 1;
					loadData();
				}
			}
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadMore(boolean isSilence) {
				if (currentIndex == 1) {
					topicpage++;
					initData(true);
				}else if(currentIndex > 1){
					typepage++;
					loadData();
				}
			}
			
			@Override
			public void onHeaderMove(double headerMovePercent, int offsetY) {
				// TODO Auto-generated method stub
				
			}
		});
		MyFootView footView = new MyFootView(getActivity());
		xrefresh.setCustomFooterView(footView);
	}


	// 状态栏高度
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

			sbar = getContext().getResources().getDimensionPixelSize(x);

		} catch (Exception e1) {

			e1.printStackTrace();

		}

		return sbar;
	}

	// 沉浸式状态栏
	private void initstateView() {
		if (Build.VERSION.SDK_INT >= 19) {
			rank_head.setVisibility(View.VISIBLE);
		}
		int result = getStatusBarHeight();
		LayoutParams layoutParams = rank_head.getLayoutParams();
		layoutParams.height = result;
		rank_head.setLayoutParams(layoutParams);

	}


	@Override
	protected void loadLazyData() {
		initData(true);
	}


	private void loadListData(JSONArray moren) throws JSONException {
		if (moren.length()<10) {
			isloadmore = false;
			xrefresh.setPullLoadEnable(false);
		}else{
			isloadmore = true;
			xrefresh.setPullLoadEnable(true);
		}
		for (int i = 0; i < moren.length(); i++) {
			JSONObject object = moren.getJSONObject(i);
			Map<String, String> map = new HashMap<>();
			map.put("author", object.optString("author"));
			map.put("title", object.optString("title"));
			map.put("img", object.optString("img"));
			map.put("zan", object.optString("zan"));
			map.put("count", object.optString("count"));
			map.put("htmlid", object.optString("htmlid"));
			map.put("id", object.optString("id"));
			map.put("type", object.optString("type"));
			datalist.add(map);
		}
		if (listadapter != null) {
			listadapter.notifyDataSetChanged();
		} else {
			listadapter = new FindListAdapter(datalist, getActivity());
			mlistView.setAdapter(listadapter);
			mlistView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					insertWenzhangGuanzhu(arg2);

				}
			});
		}
		isclear =false;
	}

	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
		xrefresh.stopLoadMore();
		xrefresh.stopRefresh();
		switch (requestCode) {
		case 1:
			try {
					if (mbox != null) {
						mbox.removeAllViews();
					}
					if (isclear) {
						datalist.clear();
					}
					JSONArray jsonArray = new JSONArray(content);
					loadListData(jsonArray);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 2:
			if (isclear) {
				datalist.clear();
			}
			JSONArray jsonArray;
			try {
				jsonArray = new JSONArray(content);
				loadListData(jsonArray);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;
		case 3:
			try {
				if (isclear) {
					datalist.clear();
				}
				mbox.getChildAt(0).setVisibility(View.VISIBLE);
				
				titlelist.get(currentIndex).put("isselect", "0");
				titlelist.get(0).put("isselect", "1");
				View view = mbox.getChildAt(0);
				TextView title1 = (TextView) view.findViewById(R.id.item_title);
				View henggang1 = view.findViewById(R.id.bottom_view);
				title1.setTextColor(Color.parseColor("#ff7d41"));
				henggang1.setBackgroundColor(Color.parseColor("#ff7d41"));

				View view4 = mbox.getChildAt(currentIndex);
				TextView title3 = (TextView) view4.findViewById(R.id.item_title);
				View henggang3 = view4.findViewById(R.id.bottom_view);
				title3.setTextColor(Color.parseColor("#666666"));
				henggang3.setBackgroundColor(Color.parseColor("#ffffff"));
				// mhscrollview.scrollTo(view.getLeft() - 200, 0);
				currentIndex = 0;
				JSONArray jsonObject1 = new JSONArray(content);
				loadListData(jsonObject1);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 4:
			Intent intent = new Intent(getActivity(), WebViewWZActivity.class);
			intent.putExtra("url", content);
			intent.putExtra("title", wztitle);
			startActivity(intent);
			break;
		default:
			break;
		}
	}


}
