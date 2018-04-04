package com.bbk.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbk.activity.DetailsMainActivity22;
import com.bbk.activity.R;
import com.bbk.util.LoadImgUtil;

public class BrowseListAdapterDelegate implements AdapterDelegate<ArrayList<HashMap<String, Object>>> {
	private int viewType;
	private Context mContext;
	
	public BrowseListAdapterDelegate(int viewType) {
		this.viewType = viewType;
	}
	
	public BrowseListAdapterDelegate(Context context, int viewType) {
		mContext = context;
		this.viewType = viewType;
	}
	
	@Override
	public int getItemViewType() {
		return viewType;
	}
	
	@Override
	public boolean isForViewType(ArrayList<HashMap<String, Object>> items,
			int position) {
		return items.get(position).get("itemType").toString().equals("1") ? true : false;
	}

	@Override
	public void onBindViewHolder(ArrayList<HashMap<String, Object>> items,
			int position, ViewHolder holder) {
		
		BrowseListViewHolder browseHolder = (BrowseListViewHolder) holder;
		
		final HashMap<String, Object> itemMap = items.get(position);
		
		LoadImgUtil.loadImg(mContext, browseHolder.itemImg, itemMap.get("itemImg").toString());
		browseHolder.itemTitle.setText(itemMap.get("itemTitle").toString());
		browseHolder.itemPrice.setText("￥" + itemMap.get("itemPrice").toString());

		String isCollected = itemMap.get("itemIsCollected").toString();
		if("0".equals(isCollected)) {
			LoadImgUtil.loadImg(mContext, browseHolder.itemIsCollected, R.mipmap.icon_collection_heart_normal);
		}
		if("1".equals(isCollected)) {
			LoadImgUtil.loadImg(mContext, browseHolder.itemIsCollected, R.mipmap.icon_collection_heart_selected);
		}
		browseHolder.itemView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String groupRowkey = itemMap.get("itemGroupRowkey").toString();
				Intent intent = new Intent(mContext, DetailsMainActivity22.class);
				intent.putExtra("groupRowKey", groupRowkey);
				mContext.startActivity(intent);
			}
		});
		
	}

	@Override
	@NonNull
	public ViewHolder onCreateViewHolder(ViewGroup parent) {
		return new BrowseListViewHolder(
				((Activity)mContext).getLayoutInflater()
				.inflate(R.layout.item_browse_list, parent, false));
	}
	
	public class BrowseListViewHolder extends ViewHolder {
		
		ImageView itemImg;
		TextView itemTitle;
		TextView itemPrice;
		
		ImageView itemIsCollected;

		public BrowseListViewHolder(View arg0) {
			super(arg0);
			itemImg = (ImageView) arg0.findViewById(R.id.item_img);
			itemTitle = (TextView) arg0.findViewById(R.id.item_title);
			itemPrice = (TextView) arg0.findViewById(R.id.item_price);
			itemIsCollected = (ImageView) arg0.findViewById(R.id.item_is_collected);
		}

	}
}
