package com.bbk.adapter;

import java.util.List;
import java.util.Map;

import com.bbk.activity.R;
import com.bbk.adapter.SortListViewGridAdapter.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DetailListViewAdapter extends BaseAdapter{
	private Context mContext;
	private List<Map<String, String>> list;

	public DetailListViewAdapter(Context mContext, List<Map<String, String>> list) {
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
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.detail_listview,null);
			holder = new ViewHolder();
			holder.tv1 = (TextView) convertView.findViewById(R.id.tv111);
			holder.tv2 = (TextView) convertView.findViewById(R.id.tv222);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		Map<String, String> map = list.get(position);
		String text1 = map.get("text1");
		String text2 = map.get("text2");
		holder.tv1.setText(text1);
		holder.tv2.setText(text2);
		return convertView;
	}
	class ViewHolder{
		TextView tv1,tv2;
	}
}
