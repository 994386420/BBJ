package com.bbk.adapter;

import java.util.List;
import java.util.Map;

import com.bbk.activity.R;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.BaseViewHolder;
import com.bumptech.glide.Glide;

import android.R.string;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RankListViewGridAdapter extends BaseAdapter{
	private Context mContext;
	private List<Map<String, Object>> mData;
	private int i;

	public RankListViewGridAdapter(Context mContext, List<Map<String, Object>> mData,int i) {
		super();
		this.mContext = mContext;
		this.mData = mData;
		this.i = i;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.rank_gridview,null);
			viewHolder = new ViewHolder();
			viewHolder.textView = BaseViewHolder.get(convertView, R.id.tv_gridView_item);
			viewHolder.imageView = BaseViewHolder.get(convertView, R.id.iv_gridView_img);
			viewHolder.rank_number_tv = BaseViewHolder.get(convertView, R.id.rank_number_tv);
			viewHolder.rank_img = BaseViewHolder.get(convertView, R.id.rank_img);
			viewHolder.rank = BaseViewHolder.get(convertView, R.id.rank);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Map<String, Object> map = mData.get(position);
		//判断是否为为你推荐（品牌榜除外）
		if (map.get("text") != "") {
			String text = map.get("text").toString();
			viewHolder.textView.setText(text);
		}else{
			String price = map.get("price").toString();
			viewHolder.textView.setText("￥"+price);
			viewHolder.textView.setTextColor(Color.parseColor("#c80000"));
			viewHolder.rank.setVisibility(View.VISIBLE);
			viewHolder.rank_number_tv.setText(String.valueOf(position+1));
			if(position < 3){
				viewHolder.rank_img.setImageResource(R.mipmap.top01);
			}else{
				viewHolder.rank_img.setImageResource(R.mipmap.top02);
			}
			
		}
		String imageUrl = map.get("imageUrl").toString();
		if (i == 1) {
			Glide.with(mContext)
			.load(imageUrl)
			.placeholder(R.mipmap.zw_img_138)
			.into(viewHolder.imageView);
		}else{
			Glide.with(mContext)
			.load(imageUrl)
			.placeholder(R.mipmap.zw_img_300)
			.into(viewHolder.imageView);
		}
		return convertView;
	}
	class ViewHolder{
		TextView textView,rank_number_tv;
		ImageView imageView,rank_img;
		RelativeLayout rank;
		
	}
}
