package com.bbk.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
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

import com.alibaba.fastjson.JSON;
import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshView.XRefreshViewListener;
import com.bbk.Bean.BiaoLiaoBean;
import com.bbk.Bean.NewFxBean;
import com.bbk.activity.GossipPiazzaDetailActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.WebViewWZActivity;
import com.bbk.adapter.FindListAdapter;
import com.bbk.adapter.GossipPiazzaAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.util.BaseTools;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.util.SystemBarTintManager;
import com.bbk.view.HeaderView;
import com.bbk.view.MyFootView;
import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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
public class NewRankFragment extends BaseViewPagerFragment {
	private View rank_head, mView;
	private DataFlow dataFlow;
	private ListView mlistView;
	private LinearLayout mbox;
	private int x= 1;
	private FindListAdapter listadapter;
	private SmartRefreshLayout xrefresh;
	private int topicpage = 1;
	private List<NewFxBean> fxBeans;


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
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("page", topicpage+"");
		maps.put("type", "比比鲸原创");
		RetrofitClient.getInstance(getActivity()).createBaseApi().queryArticleByType(
				maps, new BaseObserver<String>(getActivity()) {
					@Override
					public void onNext(String s) {
						try {
							JSONObject jsonObject = new JSONObject(s);
							if (jsonObject.optString("status").equals("1")) {
								xrefresh.finishLoadMore();
								xrefresh.finishRefresh();
								fxBeans = JSON.parseArray(jsonObject.optString("content"), NewFxBean.class);
								if (x == 1) {
									listadapter = new FindListAdapter(fxBeans, getActivity());
									mlistView.setAdapter(listadapter);
								} else if (x == 2) {
									if (jsonObject.optString("content") != null && !jsonObject.optString("content").toString().equals("[]")) {
										listadapter.notifyData(fxBeans);
									} else {
										StringUtil.showToast(getActivity(), "没有更多了");
									}
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					@Override
					protected void hideDialog() {
						DialogSingleUtil.dismiss(0);
					}

					@Override
					protected void showDialog() {
						DialogSingleUtil.show(getActivity());
					}

					@Override
					public void onError(ExceptionHandle.ResponeThrowable e) {
						Log.e("Exception", e.getMessage());
						StringUtil.showToast(getActivity(), "网络异常");
					}
				});
	}

	private void initView() {
		xrefresh =  mView.findViewById(R.id.xrefresh);
		refreshAndloda();
		mlistView =  mView.findViewById(R.id.mlistview);
		mbox = mView.findViewById(R.id.mbox);

	}
	private void refreshAndloda() {
		xrefresh.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(final RefreshLayout refreshlayout) {
					topicpage = 1;
					x = 1;
					initData(true);
			}
		});
		xrefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore(RefreshLayout refreshlayout) {
				topicpage++;
				x = 2;
				initData(true);
			}
		});
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


}
