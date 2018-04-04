package com.bbk.adapter;

import java.util.List;
import java.util.Map;

import com.bbk.activity.R;
import com.bbk.view.BaseViewHolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeFirstGridAdapter extends BaseAdapter{
	private Context mContext;
	private List<Map<String, String>> mData;

	public HomeFirstGridAdapter(Context mContext, List<Map<String, String>> mData) {
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
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.gridview_home2,null);
		}
		Map<String, String> map = mData.get(position);
		TextView textView = BaseViewHolder.get(convertView, R.id.tv_gridView_item);
		String text = map.get("text").toString();
		textView.setText(text);
		TextPaint tp = textView.getPaint(); 
		tp.setFakeBoldText(true);
		ImageView imageView = BaseViewHolder.get(convertView, R.id.iv_gridView_item);
		String imageUrl = map.get("imageUrl").toString();
		Glide.with(mContext)
		.load(imageUrl)
		.skipMemoryCache(true)
		.diskCacheStrategy(DiskCacheStrategy.RESULT)
		.placeholder(R.mipmap.zw_img_160)
		.thumbnail(0.5f)
		.into(imageView);
		System.gc();
		return convertView;
	}

}
