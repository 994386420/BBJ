package com.bbk.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbk.activity.R;
import com.bbk.view.BaseViewHolder;
import com.bumptech.glide.Glide;

/**
 * @Description:gridview的Adapter
 * @author http://blog.csdn.net/finddreams
 */
public class DCBrandGridAdapter extends BaseAdapter {
	private Context mContext;
	private List<Map<String, Object>> mData;

	public DCBrandGridAdapter(Context mContext, List<Map<String, Object>> mData) {
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
		return position;
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
					R.layout.grid_item_dc_brand, parent, false);
		}
		Map<String, Object> map = mData.get(position);
		TextView textView = BaseViewHolder.get(convertView, R.id.item_text);
		String brand = map.get("brand").toString();
		textView.setText(brand);
		ImageView imageView = BaseViewHolder.get(convertView, R.id.item_img);
		String brandImg = map.get("brandImg").toString();
		Glide.with(mContext)
		.load(brandImg)
		.skipMemoryCache(true)
		.placeholder(R.mipmap.default_img)
		.dontAnimate()
		.into(imageView);
		return convertView;
	}

}
