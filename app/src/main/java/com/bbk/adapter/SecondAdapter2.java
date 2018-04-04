package com.bbk.adapter;

import java.util.List;
import java.util.Map;

import com.bbk.activity.R;
import com.bbk.view.BaseViewHolder;
import com.bumptech.glide.Glide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SecondAdapter2 extends BaseAdapter{
	private Context mContext;
	private List<String> mData;
	private int type;

	public SecondAdapter2(Context mContext, List<String> mData,int type) {
		super();
		this.mContext = mContext;
		this.mData = mData;
		this.type = type;
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
					R.layout.gridview_supermarket_text,null);
			View biankuang = convertView.findViewById(R.id.biankuang);
			if (type == 1) {
				if (position == 0) {
					biankuang.setBackgroundResource(R.drawable.text_view_border2);
				}
			} else if(type == 2){
				if (position == 0) {
					biankuang.setBackgroundResource(R.drawable.text_view_border3);
				}
			}else if(type == 3) {
				if (position == 0) {
					biankuang.setBackgroundResource(R.drawable.text_view_border4);
				}
			}

		}
		
		String text = mData.get(position);
		TextView textView = BaseViewHolder.get(convertView, R.id.tv);
		textView.setText(text);
		return convertView;
	}
}
