package com.bbk.fragment;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bbk.activity.R;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.util.DensityUtil;
import com.bbk.util.LoadImgUtil;
import com.bbk.util.NumberUtil;

public class ProductReviewFragment extends Fragment implements OnClickListener,ResultEvent {
	
	private DataFlow dataFlow;
	private View mView;
	private String reviewStr = "";
	
	private LinearLayout reviewImpressionLayout;
	
	private LinearLayout reviewContentLayout;
	
	private RelativeLayout goodReviewLayout;
	private TextView goodReviewText;
	private ImageView goodReviewImg;
	
	private RelativeLayout badReviewLayout;
	private TextView badReviewText;
	private ImageView badReviewImg;
	
	private TextView loadMoreReviewBtn;
	
	private String reviewType = "1";
	private String rowkey = "";
	private int page = 1;
	
	private TextView noticeTv;
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		Bundle args = getArguments();
		reviewStr = args != null ? args.getString("reviewStr") : "";
		rowkey = args != null ? args.getString("rowkey") : "";
		super.onCreate(savedInstanceState);
	}
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mView = LayoutInflater.from(getActivity()).inflate(R.layout.product_review_fragment, null);
		return mView;
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		
		initView();
		initData();
		dataFlow = new DataFlow(getActivity());
		
		super.onActivityCreated(savedInstanceState);
	}
	
	public <T extends View> T $(int resId) {
		return (T) mView.findViewById(resId);
	}
	
	private void initView() {
		reviewImpressionLayout = $(R.id.review_impression_layout);
		reviewContentLayout = $(R.id.review_content_layout);
		
		goodReviewLayout = $(R.id.good_review_layout);
		goodReviewText = $(R.id.good_review_text);
		goodReviewImg = $(R.id.good_review_img);
		
		badReviewLayout = $(R.id.bad_review_layout);
		badReviewText = $(R.id.bad_review_text);
		badReviewImg = $(R.id.bad_review_img);
		
		loadMoreReviewBtn = $(R.id.load_more_review);
		loadMoreReviewBtn.setOnClickListener(this);
		
		goodReviewLayout.setOnClickListener(this);
		badReviewLayout.setOnClickListener(this);
		
		noticeTv = $(R.id.notice);
	}
	
	private void initData() {
		try {
			JSONObject jsonObj = new JSONObject(reviewStr);
			initReviewImpressionData(jsonObj.optString("impression"));
			initReviewContentData(jsonObj.optString("review"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getHttpReviewData(String key, String page, String type, int what) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("rowkey", key);
		params.put("page", page);
		params.put("type", type);
		
		dataFlow.requestData(what, "apiService/getDetailCommentByPage", params, this,false);
	}
	
	public View inflateView(int id) {
		return LayoutInflater.from(getActivity()).inflate(id, null);
	}
	
	public void initReviewImpressionData(String impression) {
		
		if(TextUtils.isEmpty(impression)) {
			reviewImpressionLayout.setVisibility(View.GONE);
			return ;
		}
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(getActivity(), 16));
		
		params.leftMargin = params.rightMargin = DensityUtil.dip2px(getActivity(), 16);
		params.topMargin = DensityUtil.dip2px(getActivity(), 8);
		String[] impressionArr = impression.split("\\|");
		
		int count = 0;
		for(int i = 0; i < impressionArr.length; i ++) {
			String[] itemArr = impressionArr[i].split("\\:");
			if(itemArr.length == 1) {
				count ++;
			} else {
				count += NumberUtil.parseInt(itemArr[1], 0);
			}
		}
		
		int length = impressionArr.length > 5 ? 5 : impressionArr.length;
		
		for(int i = 0; i < length; i ++) {
			
			View itemView = inflateView(R.layout.item_impression);
			TextView impressionTextTv = (TextView) itemView.findViewById(R.id.review_impression_text);
			View impressionRectangleView = itemView.findViewById(R.id.review_impression_rectangle);
			TextView impressionNumTv = (TextView) itemView.findViewById(R.id.review_impression_num);
			
			String[] itemArr = impressionArr[i].split("\\:");
			impressionTextTv.setText(itemArr[0]);
			android.view.ViewGroup.LayoutParams params2 = impressionRectangleView.getLayoutParams();
			
			if(itemArr.length == 1) {
				impressionNumTv.setText("1");
				params2.width = DensityUtil.dip2px(getActivity(), 
						160 * 1 / count);
			} else {
				impressionNumTv.setText(itemArr[1]);
				params2.width = DensityUtil.dip2px(getActivity(), 
						160 * NumberUtil.parseInt(itemArr[1], 0) / count);
			}
			impressionRectangleView.setLayoutParams(params2);
			
			
			reviewImpressionLayout.addView(itemView, i + 1, params);
		}
	}
	
	public void initReviewContentData(String review) {
		
		
		if(TextUtils.isEmpty(review) || "[]".equals(review)) {
			loadMoreReviewBtn.setVisibility(View.GONE);
			return;
		} else {
			loadMoreReviewBtn.setVisibility(View.VISIBLE);
		}
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		
		params.leftMargin = DensityUtil.dip2px(getActivity(), 8);
		params.rightMargin = params.leftMargin;
		params.topMargin = params.leftMargin * 2;
		
		try {
			JSONArray jsonArr = new JSONArray(review);
			
			int length = jsonArr.length();
			
			for(int i = 0; i < length; i ++) {
				View itemView = inflateView(R.layout.item_review);
				
				JSONObject jsonObj = jsonArr.optJSONObject(i);
				
				TextView itemPublisherTv = (TextView) itemView.findViewById(R.id.review_publisher);
				ImageView itemTypeImg = (ImageView) itemView.findViewById(R.id.review_type);
				TextView itemPublishDateTv = (TextView) itemView.findViewById(R.id.review_publisher_date);
				TextView itemContentTv = (TextView) itemView.findViewById(R.id.review_content);
				
				itemPublisherTv.setText(jsonObj.optString("name"));
				itemPublishDateTv.setText(jsonObj.optString("date"));
				itemContentTv.setText(jsonObj.optString("evaluation"));
				
				if("1".equals(reviewType)) {
					LoadImgUtil.loadImg(getActivity(), itemTypeImg, R.mipmap.icon_smile_blue);
				} else {
					LoadImgUtil.loadImg(getActivity(), itemTypeImg, R.mipmap.icon_cry_blue);
				}
				
				reviewContentLayout.addView(itemView, params);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.load_more_review:
			getHttpReviewData(rowkey, (++ page) + "", reviewType, 1);
			break;
		case R.id.good_review_layout:
			if("2".equals(reviewType)) {
				goodReviewLayout.setBackgroundResource(R.drawable.bg_review_sort_btn_selected);
				goodReviewText.setTextColor(Color.parseColor("#FFFFFF"));
				LoadImgUtil.loadImg(getActivity(), goodReviewImg, R.mipmap.icon_smile_white);
				
				badReviewLayout.setBackgroundResource(R.drawable.bg_review_sort_btn_normal);
				badReviewText.setTextColor(Color.parseColor("#0098FF"));
				LoadImgUtil.loadImg(getActivity(), badReviewImg, R.mipmap.icon_cry_blue);
				
				
				reviewContentLayout.removeAllViews();
				
				reviewType = "1";
				page = 1;
				
				getHttpReviewData(rowkey, page + "", reviewType, 1);
			}
			
			break;
		case R.id.bad_review_layout:
			if("1".equals(reviewType)) {
				badReviewLayout.setBackgroundResource(R.drawable.bg_review_sort_btn_selected);
				badReviewText.setTextColor(Color.parseColor("#FFFFFF"));
				LoadImgUtil.loadImg(getActivity(), badReviewImg, R.mipmap.icon_cry_white);
				
				goodReviewLayout.setBackgroundResource(R.drawable.bg_review_sort_btn_normal);
				goodReviewText.setTextColor(Color.parseColor("#0098FF"));
				LoadImgUtil.loadImg(getActivity(), goodReviewImg, R.mipmap.icon_smile_blue);
				
				reviewContentLayout.removeAllViews();
				
				reviewType = "2";
				page = 1;
				
				getHttpReviewData(rowkey, page + "", reviewType, 1);
			}
			
			break;
		default:
			break;
		}
	}

	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo,
			String content) {
		String resultStr = content;
		if(TextUtils.isEmpty(resultStr) || "{}".equals(resultStr) || "[]".equals(resultStr)) {
			noticeTv.setVisibility(View.VISIBLE);
			loadMoreReviewBtn.setVisibility(View.GONE);
			return;
		} else {
			noticeTv.setVisibility(View.GONE);
			loadMoreReviewBtn.setVisibility(View.VISIBLE);
		}
		switch (requestCode) {
		case 1:
			initReviewContentData(resultStr);
			break;
		default:
			break;
		}
		
	}
	
}
