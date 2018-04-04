package com.bbk.adapter;

import java.util.List;
import java.util.Map;

import com.bbk.activity.R;
import com.bbk.adapter.RankListViewGridAdapter.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BrandRankAdapter extends BaseAdapter{
	private Context mContext;
	private List<String> list;

	public BrandRankAdapter(Context mContext, List<String> list) {
		super();
		this.mContext = mContext;
		this.list = list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.rank_brand_listview,null);
			viewHolder = new ViewHolder();
			viewHolder.number = (TextView) convertView.findViewById(R.id.number);
			viewHolder.brand = (TextView) convertView.findViewById(R.id.brand);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String brand = list.get(position);
		viewHolder.brand.setText(brand);
		viewHolder.number.setText(String.valueOf(position+4));
		return convertView;
	}
	class ViewHolder{
		TextView number,brand;
	
	}
}
