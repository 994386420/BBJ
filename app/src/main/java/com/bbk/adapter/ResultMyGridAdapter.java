package com.bbk.adapter;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import com.bbk.Bean.SearchResultBean;
import com.bbk.activity.BidActivity;
import com.bbk.activity.CompareActivity;
import com.bbk.activity.IntentActivity;
import com.bbk.activity.R;
import com.bbk.activity.ResultDialogActivity;
import com.bbk.activity.WebViewActivity;
import com.bbk.adapter.ListViewAdapter2.ViewHolder;
import com.bbk.component.HomeAllComponent;
import com.bbk.component.HomeAllComponent5;
import com.bbk.component.SearchComponent;
import com.bbk.component.SimpleComponent;
import com.bbk.dialog.ResultDialog;
import com.bbk.util.DensityUtil;
import com.bbk.util.JumpIntentUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.BaseViewHolder;
import com.blog.www.guideview.Guide;
import com.blog.www.guideview.GuideBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResultMyGridAdapter extends RecyclerView.Adapter{
	private Activity context;
	private List<SearchResultBean> searchResultBeans;
	private int showTimes = 0;
	public ResultMyGridAdapter( List<SearchResultBean> searchResultBeans,Activity context){
		this.searchResultBeans = searchResultBeans;
		this.context =context;
	}
	public void notifyData(List<SearchResultBean> beans){
		if (beans != null && beans.size() > 0){
			this.searchResultBeans.addAll(beans);
			notifyDataSetChanged();
		}
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		ViewHolder ViewHolder = new ViewHolder(
				LayoutInflater.from(context).inflate(R.layout.listview_item_result4, parent, false));
		return ViewHolder;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		try {
			ViewHolder viewHolder = (ViewHolder) holder;
			init(viewHolder,position);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public int getItemCount() {
		return searchResultBeans.size();
	}


	class ViewHolder extends RecyclerView.ViewHolder {
		ImageView img;
		TextView title,item_offer,mbigprice,mlittleprice,juan;
		LinearLayout domainLayout;
		RelativeLayout intentbuy,mmoredomain;
		RelativeLayout itemlayout;
		public ViewHolder(View mView) {
			super(mView);
			img = (ImageView) mView.findViewById(R.id.item_img);
			title = (TextView) mView.findViewById(R.id.item_title);
			item_offer = (TextView) mView.findViewById(R.id.item_offer);
			mlittleprice = (TextView) mView.findViewById(R.id.mlittleprice);
			mbigprice = (TextView) mView.findViewById(R.id.mbigprice);
			juan = (TextView) mView.findViewById(R.id.juan);
			domainLayout = (LinearLayout) mView.findViewById(R.id.domain_layout);
			mmoredomain = (RelativeLayout) mView.findViewById(R.id.mmoredomain);
			intentbuy = (RelativeLayout)mView.findViewById(R.id.intentbuy);
			itemlayout = mView.findViewById(R.id.result_item);
		}
	}

	private void init(final ViewHolder vh, final int position) {
		try {
			WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			int width = wm.getDefaultDisplay().getWidth();
			LayoutParams params = vh.img.getLayoutParams();
			params.height = width/2;
			vh.img.setLayoutParams(params);
			final SearchResultBean dataSet = searchResultBeans.get(position);
			String title = dataSet.getTitle();
			String img = dataSet.getImgUrl();
			String price = dataSet.getPrice();
			String hnumber = dataSet.getComnum();
			vh.title.setText(title);
			String bigprice;
			String littleprice;
			/**
			 * 引导图层
			 */
			if (position == 0 && showTimes == 0) {
				final View finalView = vh.intentbuy;
				vh.intentbuy.post(new Runnable() {
					@Override public void run() {
						String isFirstResultUse = SharedPreferencesUtil.getSharedData(context,"isFirstUse", "isFirstResultUse");
						if (TextUtils.isEmpty(isFirstResultUse)) {
							isFirstResultUse = "yes";
						}
						if (isFirstResultUse.equals("yes")) {
							SharedPreferencesUtil.putSharedData(context, "isFirstUse","isFirstResultUse", "no");
							showGuideView(finalView);
						}
					}
				});
			}
			if (price.contains(".")) {
				int end = price.indexOf(".");
				bigprice = price.substring(0, end);
				littleprice = price.substring(end, price.length());
			}else{
				bigprice = price;
				littleprice = ".0";
			}
			vh.mbigprice.setText(bigprice);
			vh.mlittleprice.setText(littleprice);
			if (dataSet.getTarr() != null) {
				JSONArray array = new JSONArray(dataSet.getTarr());
				for (int i = 0; i < array.length(); i++) {
					JSONObject object = array.getJSONObject(i);
					String price1 = object.optString("price");
					if (price1.contains(".")) {
						int end = price1.indexOf(".");
						bigprice = price1.substring(0, end);
						littleprice = price1.substring(end, price1.length());
					}else{
						bigprice = price1;
						littleprice = ".0";
					}
					vh.mbigprice.setText(bigprice);
					vh.mlittleprice.setText(littleprice);
				}
			}
			if (Integer.valueOf(hnumber)>10000) {
				if (Integer.valueOf(hnumber)>100000000) {
					DecimalFormat df = new DecimalFormat("###.0");
					String num = df.format(Double.valueOf(hnumber)/100000000);
					vh.item_offer.setText("全网总评"+num+"亿条  ");
				}else{
					DecimalFormat df = new DecimalFormat("###.0");
					String num = df.format(Double.valueOf(hnumber)/10000);
					vh.item_offer.setText("全网总评"+num+"万条  ");
				}
			}else{
				vh.item_offer.setText("全网总评"+hnumber+"条  ");
			}
//				if ("1".equals(dataSet.get("isxianshi"))) {
			final String groupRowKey = dataSet.getGroupRowkey();
			vh.mmoredomain.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(context, ResultDialogActivity.class);
//						intent.putExtra("tarr",dataSet.get("tarr").toString() );
					intent.putExtra("keyword",dataSet.getKeyword());
					intent.putExtra("rowkey",groupRowKey );
					context.startActivity(intent);
				}
			});
			vh.intentbuy.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
//						Intent intent = new Intent(context, CompareActivity.class);
//						intent.putExtra("rowkey",groupRowKey );
					//二级页面去发标
					Intent intent = new Intent(context, BidActivity.class);
					intent.putExtra("rowkey",groupRowKey);
					intent.putExtra("type","1");
					context.startActivity(intent);
				}
			});

			if (dataSet.getYjson() != null){
				if ("-1".equals(dataSet.getSaleinfo())){
					vh.juan.setVisibility(View.GONE);
				}else {
					vh.juan.setVisibility(View.VISIBLE);
					vh.juan.setText("劵");
				}
			}else {
				try {
					if (dataSet.getYjson() != null) {
						vh.juan.setVisibility(View.VISIBLE);
						String yjson = dataSet.getYjson();
						final JSONObject object = new JSONObject(yjson);
						vh.juan.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								new ResultDialog(context).buildDiloag(object, v, context);
							}
						});
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			Glide.with(context)
					.load(img)
					.placeholder(R.mipmap.zw_img_300)
					.priority(Priority.HIGH)
					.into(vh.img);
			vh.itemlayout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					notifyDataSetChanged();
					Intent intent;
					if (JumpIntentUtil.isJump4(searchResultBeans, position)) {
						intent = new Intent(context, IntentActivity.class);
						if (searchResultBeans.get(position).getAndroidurl() != null) {
							intent.putExtra("url", searchResultBeans.get(position).getUrl());
						} else {
							intent.putExtra("url", searchResultBeans.get(position).getAndroidurl());
						}
						if (searchResultBeans.get(position).getTitle() != null) {
							intent.putExtra("title", searchResultBeans.get(position).getTitle());
						}
						if (searchResultBeans.get(position).getDomain() != null) {
							intent.putExtra("domain", searchResultBeans.get(position).getDomain());
						}
						if (searchResultBeans.get(position).getGroupRowkey() != null) {
							intent.putExtra("groupRowKey", searchResultBeans.get(position).getGroupRowkey());
						}
					} else {
						intent = new Intent(context, WebViewActivity.class);
						if (searchResultBeans.get(position).getAndroidurl() != null) {
							intent.putExtra("url", searchResultBeans.get(position).getUrl());
						} else {
							intent.putExtra("url", searchResultBeans.get(position).getUrl());
						}
						if (searchResultBeans.get(position).getGroupRowkey() != null) {
							intent.putExtra("groupRowKey", searchResultBeans.get(position).getGroupRowkey());
						}
					}
					context.startActivity(intent);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 首页引导图层
	 * @param targetView
	 */
	public void showGuideView(View targetView) {
		showTimes++;
		GuideBuilder builder = new GuideBuilder();
		builder.setTargetView(targetView)
//                .setFullingViewId(R.id.ll_view_group)
				.setAlpha(150)
				.setHighTargetCorner(20)
				.setHighTargetPaddingBottom(40)
				.setOverlayTarget(false)
				.setOutsideTouchable(false);
		builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
			@Override public void onShown() {
			}

			@Override public void onDismiss() {

			}
		});

		builder.addComponent(new SearchComponent()).addComponent(new HomeAllComponent5());
		Guide guide = builder.createGuide();
		guide.setShouldCheckLocInWindow(true);
		guide.show((Activity) context);
	}
}
