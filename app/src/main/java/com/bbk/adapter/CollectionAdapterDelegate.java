package com.bbk.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbk.activity.R;
import com.bbk.adapter.CollectionAdapter.OnItemClickLitener;
import com.bbk.util.LoadImgUtil;

public class CollectionAdapterDelegate implements AdapterDelegate<ArrayList<HashMap<String, Object>>> {
	private int viewType;
	private Context mContext;
	private OnItemClickLitener mOnItemClickLitener;
	
	public CollectionAdapterDelegate(int viewType) {
		this.viewType = viewType;
	}
	
	public CollectionAdapterDelegate(Context context, int viewType) {
		mContext = context;
		this.viewType = viewType;
	}
	
	public CollectionAdapterDelegate(Context context, int viewType, OnItemClickLitener itemClickLitener) {
		mContext = context;
		this.viewType = viewType;
		mOnItemClickLitener = itemClickLitener;
	}
	
	public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
		this.mOnItemClickLitener = mOnItemClickLitener;
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
		
		final CollectionViewHolder collectionHolder = (CollectionViewHolder) holder;
		
		HashMap<String, Object> itemMap = items.get(position);
		
		collectionHolder.itemCategory.setText(itemMap.get("itemCategory").toString());
		LoadImgUtil.loadImg(mContext, collectionHolder.itemImg, itemMap.get("itemImg").toString());
		collectionHolder.itemTitle.setText(itemMap.get("itemTitle").toString());
		collectionHolder.itemPrice.setText("￥" + itemMap.get("itemPrice").toString());
		collectionHolder.itemCollectedDate.setText(itemMap.get("itemCollectedDate").toString());
		String notice = itemMap.get("itemNotice").toString();

		if (mOnItemClickLitener != null) {
			collectionHolder.itemView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int pos = collectionHolder.getLayoutPosition();
					mOnItemClickLitener.onItemClick(collectionHolder.itemView, pos);
				}
			});
			
			collectionHolder.itemView.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					int pos = collectionHolder.getLayoutPosition();
					mOnItemClickLitener.onItemLongClick(collectionHolder.itemView, pos);
					return false;
				}
			});
		}
	}

	@Override
	@NonNull
	public ViewHolder onCreateViewHolder(ViewGroup parent) {
		return new CollectionViewHolder(
				((Activity)mContext).getLayoutInflater()
				.inflate(R.layout.item_collection, parent, false));
	}
	
	public class CollectionViewHolder extends ViewHolder {
		TextView itemCategory;
		ImageView itemImg;
		TextView itemTitle;
		TextView itemPrice;
		TextView itemCollectedDate;


		public CollectionViewHolder(View arg0) {
			super(arg0);
			itemCategory = (TextView) arg0.findViewById(R.id.item_category);
			itemImg = (ImageView) arg0.findViewById(R.id.item_img);
			itemTitle = (TextView) arg0.findViewById(R.id.item_title);
			itemPrice = (TextView) arg0.findViewById(R.id.item_price);
			itemCollectedDate = (TextView) arg0.findViewById(R.id.item_collected_date);

		}

	}
}
