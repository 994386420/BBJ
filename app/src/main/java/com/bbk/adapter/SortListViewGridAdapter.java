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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SortListViewGridAdapter extends BaseAdapter{
	private Context mContext;
	private List<Map<String, Object>> mData;

	public SortListViewGridAdapter(Context mContext, List<Map<String, Object>> mData) {
		super();
		this.mContext = mContext;
		this.mData = mData;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mData.get(position);
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
			String text = map.get("text").toString();
			viewHolder.textView.setText(text);
		String imageUrl = map.get("imageUrl").toString();
			Glide.with(mContext)
			.load(imageUrl)
			.skipMemoryCache(true)
			.placeholder(R.mipmap.zw_img_300)
			.into(viewHolder.imageView);
		return convertView;
	}
	class ViewHolder{
		TextView textView,rank_number_tv;
		ImageView imageView,rank_img;
		RelativeLayout rank;
		
	}
}
