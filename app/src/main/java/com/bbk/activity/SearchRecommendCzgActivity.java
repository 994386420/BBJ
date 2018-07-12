package com.bbk.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bbk.dao.SearchHistoryDao;
import com.bbk.resource.NewConstants;
import com.bbk.util.BaseTools;
import com.bbk.util.DensityUtil;
import com.bbk.util.SharedPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class SearchRecommendCzgActivity extends BaseActivity implements OnClickListener, OnItemClickListener {

	private final static int LOAD_OVER = 1;
	private ListView historyListView;
	private TextView cleanHistoryBtn;
	private SearchHistoryDao dao;
	private SimpleAdapter listViewAdapter;
	private ArrayList<HashMap<String, Object>> itemList;
	
	private LinearLayout hotWordsLayout;
	
	private int screenWidth = 0;
	private String[] hotWordsArr = {"iphone 6s", "ipad pro", "外套", "长袖衬衣", "婴儿尿布", "哆啦咪", "巴拉巴拉小魔仙"};
	public static String ACTION_NAME = "SearchRecommendCzgActivity";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_recommend);
//		MyApplication.getInstance().addActivity(this);
		
		screenWidth = BaseTools.getWindowsWidth(this);
		initView();
		initData();
	}

	private void initView() {
		historyListView = (ListView) findViewById(R.id.search_history_list);
		cleanHistoryBtn = (TextView) findViewById(R.id.clean_history_btn);
		cleanHistoryBtn.setOnClickListener(this);
		
		historyListView.setOnItemClickListener(this);
		
		hotWordsLayout = (LinearLayout) findViewById(R.id.hot_words_layout);
	}

	private void initData() {
		
		try {
			initTag(new JSONArray(SharedPreferencesUtil.getSharedData(getApplicationContext(), "hotCzgKeyword", "hotCzgKeyword")));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		dao = new SearchHistoryDao(this);
		List<String> historyList = dao.findAllHistories();
		int length = historyList.size();
		if(length > 0) {
			cleanHistoryBtn.setVisibility(View.VISIBLE);
		} else {
			cleanHistoryBtn.setVisibility(View.GONE);
			return;
		}
		
		itemList = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < length; i++) {
			HashMap<String, Object> itemMap = new LinkedHashMap<String, Object>();
			itemMap.put("item_history", historyList.get(i));
			itemList.add(itemMap);
		}

		listViewAdapter = new SimpleAdapter(this, itemList,
				R.layout.listview_item_history, 
				new String[] {"item_history"}, new int[] {R.id.item_history});

		historyListView.setAdapter(listViewAdapter);
		
//		loadData();
	}

	/*private void loadData() {
		Thread loadDataThread = new Thread(new Runnable() {
			@Override
			public void run() {

				Map<String, String> paramsMap = new HashMap<String, String>();
				paramsMap.put("number", "10");
				String result = HttpUtil.getHttp(paramsMap, Constants.MAIN_URL_MOBILE + "getSearchHotWord",SearchRecommendActivity.this);
				Message msg = Message.obtain();
				msg.what = LOAD_OVER;
				msg.obj = result;
				mHandler.sendMessageDelayed(msg, 0);
			}
		});
		loadDataThread.start();
	}
	*/
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(isFinishing()) {
				return;
			}
			switch (msg.what) {
			case LOAD_OVER:
				String result = msg.obj.toString();
				try {
					if(!TextUtils.isEmpty(result)) {
						initTag(new JSONArray(result));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
		}
	};
	
	public void initTag(JSONArray jsonArr) {
		hotWordsLayout.removeAllViews();
		
		Typeface face = getApp().getFontFace();
		
		int i = 0;
		int length = jsonArr.length();
		int marginLength = DensityUtil.dip2px(this, 10);
		int paddingLength = DensityUtil.dip2px(this, 10);
		int paddingLength1 = DensityUtil.dip2px(this, 8);
		while(i < length) {
			LinearLayout linearLayout = new LinearLayout(this);
			linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			linearLayout.setOrientation(LinearLayout.HORIZONTAL);
			
			for(; i < length; i ++) {
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.rightMargin = marginLength;
				params.bottomMargin = marginLength;
				
				final TextView localTextView = new TextView(this);
				localTextView.setBackgroundResource(R.drawable.bg_textview_recommend);
				localTextView.setGravity(Gravity.CENTER);
				localTextView.setPadding(paddingLength, paddingLength1, paddingLength, paddingLength1);
				localTextView.setId(i);
				localTextView.setTypeface(face);
				
				localTextView.setText(jsonArr.optJSONObject(i).optString("name"));
//				String productType = jsonArr.optJSONObject(i).optString("productType");
//				localTextView.setTag(productType);
				localTextView.setTextColor(Color.parseColor("#464646"));
				localTextView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						String str = ((TextView) v).getText().toString();
						
						if (!dao.exsistHistory(str)) {
							dao.addHistory(str);
						}
						if(SearchMainActivity.instance != null && !SearchMainActivity.instance.isFinishing()){
							SearchMainActivity.instance.finish();
						}
//						finish();
						final InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(localTextView.getWindowToken(), 0);
						Intent intent = new Intent(ACTION_NAME);
						intent.putExtra("keyword", str);
						SharedPreferencesUtil.putSharedData(SearchRecommendCzgActivity.this, "shaixuan", "shaixuan", "yes");
						NewConstants.clickpositionFenlei = 5200;
						NewConstants.clickpositionDianpu = 5200;
						NewConstants.clickpositionMall = 5200;
						sendBroadcast(intent);
					}
				});
				
				if( (getViewWidth(linearLayout) + marginLength + getViewWidth(localTextView)) <= (screenWidth - DensityUtil.dip2px(this, 32)) ) {
					linearLayout.addView(localTextView, params);
				} else {
					break;
				}
			}
			
			hotWordsLayout.addView(linearLayout);
		}
	}
	
	public int getViewWidth(View v) {
		int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(w, h);
		return v.getMeasuredWidth();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.clean_history_btn:
			dao.deleteAllHistory();
			itemList.clear();
			listViewAdapter.notifyDataSetChanged();
			cleanHistoryBtn.setVisibility(View.GONE);
			break;

		default:
			break;
		}
	}
	
	public void textViewOnClick(View v) {
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map = (HashMap<String, Object>) parent.getItemAtPosition(position);
		String keyword = (String) map.get("item_history");
		if(SearchMainActivity.instance != null && !SearchMainActivity.instance.isFinishing()){
			SearchMainActivity.instance.finish();
		}
//						finish();
		final InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(historyListView.getWindowToken(), 0);
		Intent intent = new Intent(ACTION_NAME);
		intent.putExtra("keyword", keyword);
		SharedPreferencesUtil.putSharedData(SearchRecommendCzgActivity.this, "shaixuan", "shaixuan", "yes");
		NewConstants.clickpositionFenlei = 5200;
		NewConstants.clickpositionDianpu = 5200;
		NewConstants.clickpositionMall = 5200;
		sendBroadcast(intent);
	}
	
	@Override
	protected void onDestroy() {
		setContentView(R.layout.view_null);
		System.gc();
		super.onDestroy();
	}
}
