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
import com.bbk.activity.ResultMainActivity;
import com.bbk.util.LoadImgUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class RankBrandAdapterDelegate implements AdapterDelegate<ArrayList<HashMap<String, Object>>> {
	private int viewType;
	private Context mContext;
	
	public RankBrandAdapterDelegate(int viewType) {
		this.viewType = viewType;
	}
	
	public RankBrandAdapterDelegate(Context context, int viewType) {
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
		return items.get(position).get("itemType").toString().equals("2") ? true : false;
	}
	/*public void loadImg(Context context, ImageView img, String url) {
		Glide.with(context)
		.load(url)
		.diskCacheStrategy(DiskCacheStrategy.NONE)
        .placeholder(R.drawable.default_img)
        .dontAnimate()
        .into(img);
	}*/

	@Override
	public void onBindViewHolder(final ArrayList<HashMap<String, Object>> items,
			final int position, ViewHolder holder) {
		
		final RankBrandViewHolder brandViewHolder = (RankBrandViewHolder) holder;
		
		HashMap<String, Object> itemMap = items.get(position);
		
//		loadImg(mContext, brandViewHolder.itemImg, itemMap.get("itemImg").toString());
		brandViewHolder.itemTitle.setText(itemMap.get("itemTitle").toString());
		brandViewHolder.itemNumber.setText(String.valueOf(position+1));
		if(position < 3){
			brandViewHolder.rankImg.setImageResource(R.mipmap.activity_rank_paihang1);
		}else{
			brandViewHolder.rankImg.setImageResource(R.mipmap.activity_rank_paihang2);
		}
		
		brandViewHolder.itemView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, ResultMainActivity.class);
				intent.putExtra("addition", items.get(position).get("itemAddtion").toString());
				intent.putExtra("brand", items.get(position).get("itemTitle").toString());
				mContext.startActivity(intent);
			}
		});
		
	}

	@Override
	@NonNull
	public ViewHolder onCreateViewHolder(ViewGroup parent) {
		return new RankBrandViewHolder(
				((Activity)mContext).getLayoutInflater()
				.inflate(R.layout.item_rank_brand, parent, false));
	}
	
	public class RankBrandViewHolder extends ViewHolder {
		ImageView itemImg;
		TextView itemTitle;
		ImageView rankImg;
		TextView itemNumber;

		public RankBrandViewHolder(View arg0) {
			super(arg0);
			itemImg = (ImageView) arg0.findViewById(R.id.item_img);
			itemTitle = (TextView) arg0.findViewById(R.id.item_title);
			rankImg = (ImageView) arg0.findViewById(R.id.rank_img);
			itemNumber = (TextView) arg0.findViewById(R.id.rank_number_tv);
		}

	}
}
