package com.bbk.activity;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.fragment.SearchFragment;
import com.bbk.util.BaseTools;
import com.bbk.util.DensityUtil;
import com.bbk.util.LoadImgUtil;
import com.bbk.util.SharedPreferencesUtil;

public class SubCategoryActivity extends BaseFragmentActivity implements OnClickListener,ResultEvent {
	
	private DataFlow dataFlow;
	private static final int HTTP_LOAD_OVER = 1;
	
	private int index = 0;
	private int screenWidth = 0;
	private String categoryStr = "";
	private String lastAddtion = "-1";
	private int lastIndex = -1; //上次点击位置
	private String title = "";
	
	private ImageButton goBackBtn;
	private TextView topTitle;
	
	private LinearLayout leftCategoryListLayout;
	private ScrollView rightLayout;
	
	private LinearLayout hotSubCategoryLayout;
	private LinearLayout hotSubCategoryListLayout;
	
	private LinearLayout hotBrandLayout;
	private LinearLayout hotBrandListLayout1;
	private LinearLayout hotBrandListLayout2;
	
	private LinearLayout experienceLayout;
	private LinearLayout experienceListLayout;
	
	private LinearLayout categoryListLayout;
	
	//默认选中子类
	private String subTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_sub_category);
		dataFlow = new DataFlow(this);
		
		screenWidth = BaseTools.getWindowsWidth(SubCategoryActivity.this);
		index = getIntent().getIntExtra("index", 0);
		categoryStr = SharedPreferencesUtil.getSharedData(SubCategoryActivity.this, "categoryType", "categoryType");
		title = getIntent().getStringExtra("title");
		
		initView();
		initData();
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	
	@Override
	public <T extends View> T $(int resId) {
		return (T) super.findViewById(resId);
	}
	
	public void initView() {
		goBackBtn = $(R.id.topbar_goback_btn);
		goBackBtn.setOnClickListener(this);
		
		topTitle = $(R.id.topbar_title);
		topTitle.setText(title);
		
		leftCategoryListLayout = $(R.id.left_category_list_layout);
		
		rightLayout = $(R.id.right_layout);
		
		hotSubCategoryLayout = $(R.id.hot_sub_category_layout);
		hotSubCategoryListLayout = $(R.id.hot_sub_category_list);
		
		hotBrandLayout = $(R.id.hot_brand_layout);
		hotBrandListLayout1 = $(R.id.hot_brand_list_1);
		hotBrandListLayout2 = $(R.id.hot_brand_list_2);
		
		experienceLayout = $(R.id.experience_layout);
		experienceListLayout = $(R.id.experience_list);
		
		categoryListLayout = $(R.id.sub_category_list_layout);
	}
	
	public void initData() {
		
		try {
			JSONArray categoryJsonArr = new JSONArray(categoryStr);
			initLeftCategoryList(categoryJsonArr.optJSONObject(index));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public View inflate(int id) {
		return getLayoutInflater().inflate(id, null, false);
	}
	
	public void initLeftCategoryList(JSONObject jsonObj) {

		if(TextUtils.isEmpty(jsonObj.toString()) || "{}".equals(jsonObj.toString())) {
			return;
		}
		
		try {
			JSONArray jsonArr = jsonObj.optJSONArray("chid");
			int length = jsonArr.length();
			LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, DensityUtil.dip2px(SubCategoryActivity.this, 56));
			
			View startClickView = null;
			subTitle = getIntent().getStringExtra("subTitle");
			
			for(int i = 0; i < length; i ++) {
				JSONObject itemJsonObj = jsonArr.optJSONObject(i);
				String name = itemJsonObj.optString("name");
				final String addtion = itemJsonObj.optString("number");
				View itemView = inflate(R.layout.item_left_category);
				TextView itemTitle = (TextView) itemView.findViewById(R.id.item_title);
				itemTitle.setText(name);
				itemView.setTag(i);
				itemView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if(!lastAddtion.equals(addtion)) {
							clickedStyle(v);
							if(lastIndex != -1){
								normalStyle(leftCategoryListLayout.getChildAt(lastIndex));
							}
							lastIndex = (int) v.getTag();
							lastAddtion = addtion;
							getHttpData(addtion, HTTP_LOAD_OVER);
						}
					}
				});
				if(subTitle != null){
					if(name.equals(subTitle)){
						startClickView = itemView;
					}
				}else if(i == 0) {
					startClickView = itemView;
				}
				leftCategoryListLayout.addView(itemView, params);
			}
			if(startClickView != null){
				startClickView.performClick();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void clickedStyle(View v) {
		TextView itemTitle = (TextView) v.findViewById(R.id.item_title);
		itemTitle.setTextColor(Color.parseColor("#0098FF"));
		v.setBackgroundColor(Color.parseColor("#F5F5F5"));
	}
	
	public void normalStyle(View v) {
		TextView itemTitle = (TextView) v.findViewById(R.id.item_title);
		itemTitle.setTextColor(Color.parseColor("#2D2D2D"));
		v.setBackgroundColor(Color.parseColor("#FFFFFF"));
	}
	
	public void getHttpData(String addtion, int what) {
		rightLayout.setVisibility(View.GONE);
		HashMap<String, String> params = new HashMap<String, String>();
		
		params.put("addition", addtion);
		params.put("typeNum", "4");
		params.put("brandNum", "6");
		params.put("itemNum", "10");
		
		dataFlow.requestData(what, "apiService/getHotInfo", params, this);
		
	}
	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo,
			String content) {
		rightLayout.setVisibility(View.VISIBLE);
		
		if(isFinishing()) {
			return;
		}
		
		String resultStr =content;

		if(TextUtils.isEmpty(resultStr)){
			Toast.makeText(SubCategoryActivity.this, "亲，网络出问题了，请稍后再试！", Toast.LENGTH_SHORT).show();
			return;
		}
		if("[]".equals(resultStr) || "{}".equals(resultStr)) {
			return ;
		}
		
		switch (requestCode) {
		case HTTP_LOAD_OVER:
			try {
				JSONObject jsonObj = new JSONObject(resultStr);
				initHotSubCategory(jsonObj.optString("hotType"));
				initHotBrand(jsonObj.optString("hotBrand"));
				initExperience(jsonObj.optString("article"));
				initCategory(jsonObj.optString("subtypes"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			break;
		default:
			break;
		}
		
	}
	
	public void initHotSubCategory(String str) {
		
		hotSubCategoryListLayout.removeAllViews();
		
		if(TextUtils.isEmpty(str) || "[]".equals(str)) {
			hotSubCategoryLayout.setVisibility(View.GONE);
			return ;
		} else {
			hotSubCategoryLayout.setVisibility(View.VISIBLE);
		}
		
		try {
			JSONArray jsonArr = new JSONArray(str);
			int length = jsonArr.length();
			int divider = DensityUtil.dip2px(SubCategoryActivity.this, 8);
			int width = (screenWidth - DensityUtil.dip2px(SubCategoryActivity.this, 88) - 4 * divider) / 3 ;
			int height = width + DensityUtil.dip2px(SubCategoryActivity.this, 28) + divider;
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
			
			params.leftMargin = divider;
			params.topMargin = divider;
			params.bottomMargin = divider;
			
			
			for(int i = 0; i < 3; i ++) {
				JSONObject itemJsonObj = jsonArr.optJSONObject(i);
				View itemView = inflate(R.layout.item_hot_category);
				
				ImageView itemImg = (ImageView) itemView.findViewById(R.id.item_img);
				
				RelativeLayout.LayoutParams imgParams = (RelativeLayout.LayoutParams) itemImg.getLayoutParams();
				imgParams.width = width;
				imgParams.height = width;
				imgParams.topMargin = divider;
				itemImg.setLayoutParams(imgParams);
				
				TextView itemTitle = (TextView) itemView.findViewById(R.id.item_title);
				
				LoadImgUtil.loadImg(SubCategoryActivity.this, itemImg, itemJsonObj.optString("typeurl"));
				itemTitle.setText(itemJsonObj.optString("typeCh"));
				
				final String title = itemJsonObj.optString("typeCh");
				final String addtion = itemJsonObj.optString("type");
				itemView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(SubCategoryActivity.this, ResultMainActivity.class);
						SearchFragment.stypeWay = 0;
						intent.putExtra("addition", addtion);
			    		intent.putExtra("keyword", title);
			    		startActivity(intent);
					}
				});
				
				hotSubCategoryListLayout.addView(itemView, params);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initHotBrand(String str) {
		hotBrandListLayout1.removeAllViews();
		hotBrandListLayout2.removeAllViews();
		
		if(TextUtils.isEmpty(str) || "[]".equals(str)) {
			hotBrandLayout.setVisibility(View.GONE);
			return ;
		} else {
			hotBrandLayout.setVisibility(View.VISIBLE);
		}
		
		try {
			JSONArray jsonArr = new JSONArray(str);
			int length = jsonArr.length();
			
			int divider = DensityUtil.dip2px(SubCategoryActivity.this, 8);
			int width = (screenWidth - DensityUtil.dip2px(SubCategoryActivity.this, 88) - 4 * divider) / 3;
			int height = width * 2 / 5;
			
			LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(width, height);
			params1.leftMargin = divider;
			params1.topMargin = divider;
			params1.bottomMargin = divider;
			params1.gravity = Gravity.CENTER;
			
			for(int i = 0; i < 3; i ++) {
				JSONObject itemJsonObj = jsonArr.optJSONObject(i);
				String brand = itemJsonObj.optString("brand");
				String brandUrl = itemJsonObj.optString("brandurl");
				View itemView;
				if(TextUtils.isEmpty(brandUrl)) {
					itemView = inflate(R.layout.item_hot_brand_text);
					TextView itemTitle = (TextView) itemView.findViewById(R.id.item_title);
					itemTitle.setText(brand);
				} else {
					itemView = inflate(R.layout.item_hot_brand_img);
					ImageView itemImg = (ImageView) itemView.findViewById(R.id.item_img);
					LoadImgUtil.loadImg(SubCategoryActivity.this, itemImg, brandUrl);
				}
				itemView.setTag(brand);
				itemView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(SubCategoryActivity.this, ResultMainActivity.class);
						intent.putExtra("addition", lastAddtion);
						intent.putExtra("brand", v.getTag().toString());
//						intent.putExtra("keyword", v.getTag().toString());
						startActivity(intent);
					}
				});
				
				hotBrandListLayout1.addView(itemView, params1);
			}
			
			LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(width, height);
			params2.leftMargin = divider;
			params2.topMargin = divider;
			params2.bottomMargin = divider;
			params2.gravity = Gravity.CENTER;
			for(int i = 3; i < length; i ++) {
				JSONObject itemJsonObj = jsonArr.optJSONObject(i);
				String brand = itemJsonObj.optString("brand");
				String brandUrl = itemJsonObj.optString("brandurl");
				View itemView;
				if(TextUtils.isEmpty(brandUrl)) {
					itemView = inflate(R.layout.item_hot_brand_text);
					TextView itemTitle = (TextView) itemView.findViewById(R.id.item_title);
					itemTitle.setText(brand);
				} else {
					itemView = inflate(R.layout.item_hot_brand_img);
					ImageView itemImg = (ImageView) itemView.findViewById(R.id.item_img);
					LoadImgUtil.loadImg(SubCategoryActivity.this, itemImg, brandUrl);
				}
				itemView.setTag(brand);
				itemView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(SubCategoryActivity.this, ResultMainActivity.class);
						intent.putExtra("addition", lastAddtion);
						intent.putExtra("brand", v.getTag().toString());
						startActivity(intent);
					}
				});
				hotBrandListLayout2.addView(itemView, params2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initExperience(String str) {
		experienceListLayout.removeAllViews();
		
		if(TextUtils.isEmpty(str) || "[]".equals(str)) {
			experienceLayout.setVisibility(View.GONE);
			return ;
		} else {
			experienceLayout.setVisibility(View.VISIBLE);
		}
		try {
			JSONArray jsonArr = new JSONArray(str);
			int length = jsonArr.length();
			
			for(int i = 0; i < length; i ++) {
				JSONObject itemJsonObj = jsonArr.optJSONObject(i);
				View itemView = inflate(R.layout.item_experience);
				ImageView itemImg = (ImageView) itemView.findViewById(R.id.item_img);
				TextView itemTitle = (TextView) itemView.findViewById(R.id.item_title);
				TextView itemContent = (TextView) itemView.findViewById(R.id.item_content);
				
				final String title = itemJsonObj.optString("title");
				final String content = itemJsonObj.optString("content");
				final String date = itemJsonObj.optString("date");
				
				LoadImgUtil.loadImg(SubCategoryActivity.this, itemImg, itemJsonObj.optString("thumb"));
				itemTitle.setText(title);
				itemContent.setText(itemJsonObj.optString("abstract"));
				
				itemView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(SubCategoryActivity.this, ContentActivity.class);

						intent.putExtra("title", title);
						intent.putExtra("content", content);
						intent.putExtra("date", date);

						startActivity(intent);
					}
				});
				
				experienceListLayout.addView(itemView);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initCategory(String str) {
		categoryListLayout.removeAllViews();
		if(TextUtils.isEmpty(str) || "{}".equals(str) || "[]".equals(str)) {
			
			return;
		}
		try {
			JSONArray jsonArr = new JSONArray(str);
			int length = jsonArr.length();
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, DensityUtil.dip2px(SubCategoryActivity.this, 48));
			for(int i = 0; i < length; i ++) {
				JSONObject itemJsonObj = jsonArr.optJSONObject(i);
				View itemView = inflate(R.layout.item_sub_category);
				TextView itemTitle = (TextView) itemView.findViewById(R.id.item_title);
				String number = itemJsonObj.optString("number");
				
				final String title = itemJsonObj.optString("name");
				final String addtion = itemJsonObj.optString("addtion");
				itemTitle.setText(title);
				
				
				if("0".equals(number)) {
					itemView.setBackgroundColor(Color.parseColor("#DDDDDD"));
					itemView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Toast.makeText(SubCategoryActivity.this, "分类数据完善中", Toast.LENGTH_SHORT).show();
						}
					});
				} else {
					itemView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(SubCategoryActivity.this, ResultMainActivity.class);
							intent.putExtra("addition", addtion);
				    		intent.putExtra("keyword", title);
				    		startActivity(intent);
						}
					});
				}
				
				categoryListLayout.addView(itemView, params);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topbar_goback_btn:
			finish();
			break;

		default:
			break;
		}
	}

}
