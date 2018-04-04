package com.bbk.adapter;

import java.util.List;
import java.util.Map;

import com.bbk.activity.R;
import com.bbk.view.BaseViewHolder;
import com.bumptech.glide.Glide;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SecondAdapter5 extends BaseAdapter{
	private Context mContext;
	private List<Map<String, Object>> mData;

	public SecondAdapter5(Context mContext, List<Map<String, Object>> mData) {
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
					R.layout.gridview_home_text4,null);
		}
		
		Map<String, Object> map = mData.get(position);
		String text = (String) map.get("item_text");
		TextView textView = BaseViewHolder.get(convertView, R.id.tv);
		RelativeLayout biankuang = BaseViewHolder.get(convertView, R.id.biankuang);
		
		textView.setText(text);
		if ("yes".equals(map.get("item_selected").toString())) {
			textView.setTextColor(Color.parseColor("#0098ff"));
			biankuang.setBackgroundResource(R.drawable.shaixuan_textview2);
			
		}else{
			textView.setTextColor(Color.parseColor("#222222"));
			biankuang.setBackgroundResource(R.drawable.shaixuan_textview);
			
			
			
		}
		
		return convertView;
	}
}
