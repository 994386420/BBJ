package com.bbk.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;

import com.bbk.activity.DetailsMainActivity22;
import com.bbk.activity.R;
import com.bbk.adapter.PromoteResultAdapter;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.refresh.PullableListView;
import com.bbk.resource.Constants;
import com.bbk.util.HttpUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;


public class PromoteResultFragment extends BaseViewPagerFragment implements ResultEvent {
	public final static int LOAD_MORE = 1;
	public final static int RELOAD = 2;
	private DataFlow dataFlow;
	private View mView;
	private PullToRefreshListView ptrl;

	
	private int type;
	private String topCategory;
	private String category;
	
	private int curPage = 1;
	private boolean morePage = true;
	private ImageButton toTopBtn;
	
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		dataFlow = new DataFlow(getActivity());
		super.onCreate(savedInstanceState);
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if(null == mView) {
			mView = inflater.inflate(R.layout.fragment_promote_result, null);
			initView();
		}
		return mView;
	}
	
//	public void setOnRefreshListener(
//			PullToRefreshListView.OnRefreshListener onRefreshListener) {
//		this.onRefreshListener = onRefreshListener;
//	}
	
	public void initFilter(int type,String topCategory,String category) {
		this.type = type;
		this.topCategory = topCategory;
		this.category = category;
	}
	public void setCategory(String category) {
		this.category = category;
	}

	public void initView() {
		initResultItem();
	}
	private List<Map<String, Object>> resultData = new ArrayList<Map<String, Object>>();
	private PromoteResultAdapter resultAdapter;
	private PullableListView listView;
	private boolean isListNotCanPullDown;
	private void initResultItem() {
		ptrl = (PullToRefreshListView) (mView.findViewById(R.id.pull_refresh_list));
		ptrl.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.BOTH);

		
		
//  		listView = (PullableListView) mView.findViewById(R.id.result_list);
		resultAdapter = new PromoteResultAdapter(getActivity(), resultData,
				R.layout.listview_promote_item_result,
				new String[] { "item_img",
				"item_title", "item_price", "item_old_price","item_offer" }, new int[] {
				R.id.item_img, R.id.item_title, R.id.item_price,R.id.item_old_price,
				R.id.item_offer });
		ptrl.setAdapter(resultAdapter);
		ptrl.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				Intent intent = new Intent(getActivity(), DetailsMainActivity22.class);
				intent.putExtra("groupRowKey", resultData.get(pos).get("groupRowKey").toString());
				getActivity().startActivity(intent);
			}
		});
		
		ptrl.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				curPage=1;
				lazyLoad();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				curPage ++;
	    		getHttpDataList(false,LOAD_MORE);
			}
		});
	
		
		ptrl.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				showToTopBtn(firstVisibleItem);
			}
		});
		toTopBtn = (ImageButton) mView.findViewById(R.id.to_top_btn);
		toTopBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ptrl.getRefreshableView().setSelection(0);
			}
		});
		if(isListNotCanPullDown){
			ptrl.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.PULL_FROM_START);
		}
	}
	public void setListNotCanPullDown(){
		isListNotCanPullDown = true;
	}
	private Handler showHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				toTopBtn.setVisibility(View.VISIBLE);
				break;
			case 2:
				toTopBtn.setVisibility(View.INVISIBLE);
				break;
			default:
				break;
			}
		};
	};

	private void showToTopBtn(int firstVisibleItem) {
		Message msg = Message.obtain();
		if (firstVisibleItem >= 5) {
			msg.what = 1;
		} else {
			msg.what = 2;
		}
		showHandler.sendMessage(msg);
	}
	
	private void loadResultItem(String str) {
		try {
			JSONArray ja = new JSONArray(str);
			for (int i = 0; i < ja.length(); i++) {
				Map<String, Object> itemMap = new HashMap<String, Object>();
				JSONObject dJo = ja.getJSONObject(i).getJSONObject("detailInfoEntity");
				itemMap.put("item_img", dJo.optString("img"));
				itemMap.put("item_title", dJo.optString("title"));
				itemMap.put("item_price", dJo.optString("price"));
				itemMap.put("item_old_price", "原价:"+dJo.optString("oldprice"));
				itemMap.put("item_offer","来自" + dJo.optString("domainCount") + "个商城，" + dJo.optString("numberCount") + "条报价");
				itemMap.put("groupRowKey",dJo.optString("groupRowKey"));
				itemMap.put("quote", dJo.optString("numberCount"));
				itemMap.put("allDomain", dJo.optString("alldomain"));
				resultData.add(itemMap);
			}
			if(ja.length() < 10){
				morePage = false;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		resultAdapter.notifyDataSetChanged();
		if (curPage==1) {
			ptrl.getRefreshableView().setSelection(0);
		}
//		ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
	}
	public void getHttpDataList(boolean isShow,int loadType){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("cattop", topCategory);
		params.put("cat", category);
		params.put("type", String.valueOf(type));
		params.put("page", String.valueOf(curPage));
		MyParams myParams = new MyParams(isShow, loadType, params);
		new GetData().execute(myParams);
		
	}
	
	@Override
	public void lazyLoad() {
		getHttpDataList(true,RELOAD);
	}

	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo,
			String content) {
//		if(requestCode == RELOAD){
//			resultData.clear();
//		}
//		
	}


//	@Override
//	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
//		if(onRefreshListener != null){
//			onRefreshListener.onRefresh(pullToRefreshLayout);
//		}
//	}
//	@Override
//	public void onLoadMore(PullToRefreshListView pullToRefreshListView) {
//		if(morePage == false){
//			PullToRefreshListView.loadmoreFinish(PullToRefreshLayout.NOT_MORE);
//    	}else{
//    		curPage ++;
//    		getHttpDataList(false,LOAD_MORE);
//    	}
//	}
	public class MyParams{
		boolean isShow;
		int loadType;
		HashMap<String, String> params;
		public MyParams(boolean isShow, int loadType, HashMap<String, String> params) {
			super();
			this.isShow = isShow;
			this.loadType = loadType;
			this.params = params;
		}
		
	}
	
	private class GetData extends AsyncTask<MyParams, Void, String>{
		private Context context;
		private String content;
	@Override
	protected String doInBackground(MyParams... arg0) {
		boolean isShow=arg0[0].isShow;
		int loadType=arg0[0].loadType;
		HashMap<String, String> params=arg0[0].params;
		if (loadType == RELOAD) {
			resultData.clear();
		}
		//dataFlow.requestData(loadType, "apiService/queryActivityList",params, PromoteResultFragment.this,isShow);
		final String url = Constants.MAIN_BASE_URL_MOBILE+"apiService/queryActivityList";
		String dataStr = HttpUtil.getHttp(params,url,context);
		try {
			JSONObject dataJo = new JSONObject(dataStr);
			content = dataJo.optString("content");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}
		
		@Override
		protected void onPostExecute(String result) {
			if (result!=null) {
				loadResultItem(result);
			}
			
			ptrl.onRefreshComplete();
			super.onPostExecute(result);
		}
		
	}
	
		public void cut(){
			ptrl.onRefreshComplete();
			resultData.clear();
			curPage=1;
		}

}
