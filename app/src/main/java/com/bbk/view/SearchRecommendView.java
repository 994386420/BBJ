package com.bbk.view;

import java.util.List;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.ResultMainActivity;
import com.bbk.activity.ResultMainActivity;
import com.bbk.dao.SearchHistoryDao;
import com.bbk.fragment.SearchFragment;
import com.bbk.util.BaseTools;
import com.bbk.util.DensityUtil;
import com.bbk.util.SharedPreferencesUtil;

public class SearchRecommendView extends LinearLayout {
	
	private Context mContext;
	private SearchHistoryDao dao;
	private int screenWidth = 0;
	
	private LinearLayout recommendLayout;
	private LinearLayout hotWordsLayout;
	
	private LinearLayout historyLayout;
	private TextView cleanHistoryBtn;
	
	private List<String> historyList;
	
	private OnPlusClickListener mOnPlusClickListener;

	public SearchRecommendView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mContext = context;
		dao = new SearchHistoryDao(context);
		historyList = dao.findAllHistories();
		screenWidth = BaseTools.getWindowsWidth((Activity) context);
		View mView = inflate(context, R.layout.view_search_recommend);
		
		recommendLayout = (LinearLayout) mView.findViewById(R.id.recommend_layout);
		hotWordsLayout = (LinearLayout) mView.findViewById(R.id.hot_search_words_layout);
		
		historyLayout = (LinearLayout) mView.findViewById(R.id.search_history_layout);
		cleanHistoryBtn = (TextView) mView.findViewById(R.id.clean_search_history_btn);
		cleanHistoryBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dao.deleteAllHistory();
				historyLayout.removeViews(0, historyLayout.getChildCount() - 2);
				historyLayout.setVisibility(View.GONE);
			}
		});
		
		
		try {
			String hotKeyword = SharedPreferencesUtil.getSharedData(context, "hotKeyword", "hotKeyword");
			initTag(new JSONArray(hotKeyword));
			initHistoryList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public View inflate(Context context, int id) {
		return View.inflate(context, id, this);
	}
	
	public View inflate(int id) {
		return ((Activity) mContext).getLayoutInflater().inflate(id, null, false);
	}
	
	public void initTag(JSONArray jsonArr) {
		hotWordsLayout.removeAllViews();
		
		if(jsonArr.length() == 0) {
			recommendLayout.setVisibility(View.GONE);
			return;
		} else {
			recommendLayout.setVisibility(View.VISIBLE);
		}
		
		Typeface face = ((MyApplication)mContext.getApplicationContext()).getFontFace();
		
		int i = 0;
		int length = jsonArr.length();
		int marginLength = DensityUtil.dip2px(mContext, 10);
		int paddingLength = DensityUtil.dip2px(mContext, 10);
		int paddingLength1 = DensityUtil.dip2px(mContext, 8);
		while(i < length) {
			LinearLayout linearLayout = new LinearLayout(mContext);
			linearLayout.setLayoutParams(new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
			linearLayout.setOrientation(LinearLayout.HORIZONTAL);
			
			for(; i < length; i ++) {
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				params.rightMargin = marginLength;
				params.bottomMargin = marginLength;
				
				TextView localTextView = new TextView(mContext);
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
						Intent intent = new Intent(mContext, ResultMainActivity.class);
						intent.putExtra("keyword", str);
						intent.putExtra("addition", v.getTag().toString());
						mContext.startActivity(intent);
					}
				});
				
				if( (getViewWidth(linearLayout) + marginLength + getViewWidth(localTextView)) <= (screenWidth - DensityUtil.dip2px(mContext, 32)) ) {
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
	
	public void initHistoryList() {
		historyLayout.removeViews(0, historyLayout.getChildCount() - 2);
		int length = historyList.size();
		if(length > 0) {
			historyLayout.setVisibility(View.VISIBLE);
		} else {
			historyLayout.setVisibility(View.GONE);
			return;
		}
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(mContext, 40));
		
		for(int i = length - 1; i >= 0; i --) {
			View itemView = inflate(R.layout.item_search_history);
			itemView.setTag(historyList.get(i));
			
			TextView itemHistoryTv = (TextView) itemView.findViewById(R.id.item_history);
			ImageView itemPlusBtn = (ImageView) itemView.findViewById(R.id.item_plus);
			itemHistoryTv.setText(historyList.get(i));
			
			
			itemView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {  
					Intent intent = new Intent(mContext, ResultMainActivity.class);
					intent.putExtra("stype", SearchFragment.stypeWay);
					intent.putExtra("keyword", v.getTag().toString());
					mContext.startActivity(intent);
				}
			});
			
			if(mOnPlusClickListener != null) {
				itemPlusBtn.setTag(historyList.get(i));
				itemPlusBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mOnPlusClickListener.onPlusClickListener(v, v.getTag().toString());
					}
				});
			}
			
			
			historyLayout.addView(itemView, 0, params);
			
		}
		
		
	}
	
	public void notifyData() {
		historyList = dao.findAllHistories();
		initHistoryList();
	}
	
	public interface OnPlusClickListener {
		void onPlusClickListener(View view, String str);
	}
	
	public void setOnPlusClickListener(OnPlusClickListener onPlusClickListener) {
		mOnPlusClickListener = onPlusClickListener;
	}

}
