package com.bbk.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bbk.activity.R;
import com.bbk.activity.ResultMainActivity;
import com.bbk.util.BaseTools;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BrandNavAdapter extends BaseAdapter {

	private Context context;
	private List<Map<String, Object>> data;

	public BrandNavAdapter(Context context, List<Map<String, Object>> data) {
		this.context = context;
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup vg) {
		ViewHolder vh;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.brand_nav, null);
			TextView navTv = (TextView) convertView.findViewById(R.id.brandNavTv);
			vh = new ViewHolder(navTv);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		String tag = data.get(position).get("brand").toString();
		vh.tv.setText(tag);
		if ("yes".equals(data.get(position).get("select").toString())) {
			vh.tv.setTextColor(Color.parseColor("#0098ff"));
		}else{
			vh.tv.setTextColor(Color.parseColor("#222222"));
		}
		
		return convertView;
	}

	class ViewHolder {
		TextView tv;

		public ViewHolder(TextView tv) {
			this.tv = tv;
		}
	}
}
