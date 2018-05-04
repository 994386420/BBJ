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

import com.bbk.activity.R;
import com.bbk.activity.RankActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class RankProductAdapterDelegate implements AdapterDelegate<ArrayList<HashMap<String, Object>>> {
	private int viewType;
	private Context mContext;
	
	public RankProductAdapterDelegate(int viewType) {
		this.viewType = viewType;
	}
	
	public RankProductAdapterDelegate(Context context, int viewType) {
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
	public void loadImg(Context context, ImageView img, String url) {
		Glide.with(context)
		.load(url)
		.diskCacheStrategy(DiskCacheStrategy.NONE)
        .placeholder(R.mipmap.default_img)
        .dontAnimate()
        .into(img);
	}
	
	@Override
	public void onBindViewHolder(final ArrayList<HashMap<String, Object>> items,
			final int position, ViewHolder holder) {
		
		final RankProductViewHolder productViewHolder = (RankProductViewHolder) holder;
		
		HashMap<String, Object> itemMap = items.get(position);
		
		loadImg(mContext, productViewHolder.itemImg, itemMap.get("itemImg").toString());
		
		
		productViewHolder.itemTitle.setText(itemMap.get("itemTitle").toString());
		productViewHolder.itemTitle.setLines(RankActivity.productLines);
		productViewHolder.itemPrice.setText("￥" + itemMap.get("itemPrice").toString());
		
		Object oldPrice = itemMap.get("itemOldPrice");
//		if(oldPrice != null){
//			productViewHolder.itemOldPrice.setText(oldPrice.toString());
//			productViewHolder.itemOldPrice.setVisibility(View.VISIBLE);
//		}else{
//			productViewHolder.itemOldPrice.setVisibility(View.GONE);
//		}
		
		productViewHolder.itemText.setText(itemMap.get("itemText").toString());
		productViewHolder.itemNumber.setText(String.valueOf(position+1));
		if(position < 3){
			productViewHolder.rankImg.setImageResource(R.mipmap.activity_rank_paihang1);
		}else{
			productViewHolder.rankImg.setImageResource(R.mipmap.activity_rank_paihang2);
		}
		Object details = itemMap.get("itemDetails");
		if(details != null){
			productViewHolder.itemDetails.setText(details.toString());
			productViewHolder.itemDetails.setVisibility(View.VISIBLE);
		}else{
			productViewHolder.itemDetails.setVisibility(View.INVISIBLE);
		}
		
		
//		productViewHolder.itemView.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(mContext, DetailsMainActivity22.class);
//				intent.putExtra("groupRowKey", items.get(position).get("itemGroupRowkey").toString());
//				mContext.startActivity(intent);
//			}
//		});
	}

	@Override
	@NonNull
	public ViewHolder onCreateViewHolder(ViewGroup parent) {
		return new RankProductViewHolder(
				((Activity)mContext).getLayoutInflater()
				.inflate(R.layout.item_rank_product, parent, false));
	}
	
	public class RankProductViewHolder extends ViewHolder {
		ImageView itemImg;
		ImageView rankImg;
		TextView itemTitle;
		TextView itemPrice;
		TextView itemText;
		TextView itemNumber;
		TextView itemDetails;
//		TextView itemOldPrice;

		public RankProductViewHolder(View view) {
			super(view);
			rankImg = (ImageView) view.findViewById(R.id.rank_img);
			itemImg = (ImageView) view.findViewById(R.id.item_img);
			itemTitle = (TextView) view.findViewById(R.id.item_title);
			itemPrice = (TextView) view.findViewById(R.id.item_price);
			itemText = (TextView) view.findViewById(R.id.item_text);
			itemNumber = (TextView) view.findViewById(R.id.rank_number_tv);
//			itemOldPrice = (TextView) view.findViewById(R.id.item_old_price);
		}

	}
}
