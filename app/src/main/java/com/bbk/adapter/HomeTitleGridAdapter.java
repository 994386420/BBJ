package com.bbk.adapter;

import java.util.List;
import java.util.Map;

import com.bbk.activity.R;
import com.bbk.adapter.ResultMyGridAdapter.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HomeTitleGridAdapter extends BaseAdapter{

	private Context context;
	private List<Map<String, String>> mlist;

	public HomeTitleGridAdapter(Context mContext, List<Map<String, String>> mData) {
		super();
		this.context = mContext;
		this.mlist = mData;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlist.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mlist.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		final ViewHolder vh;		
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = View.inflate(context, R.layout.home_title_gridview, null);
			vh.mbackground = (RelativeLayout) convertView.findViewById(R.id.mbackground);
			vh.mtext = (TextView) convertView.findViewById(R.id.mtext);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}
		Map<String, String> map = mlist.get(position);
		String name = map.get("name");
		String isselect = map.get("isselect");
		vh.mtext.setText(name);
		if (isselect.equals("1")) {
			vh.mbackground.setBackgroundResource(R.drawable.text_result_orange);
			vh.mtext.setTextColor(Color.parseColor("#ffffff"));
		}else{
			vh.mbackground.setBackgroundResource(R.drawable.text_result_gray);
			vh.mtext.setTextColor(Color.parseColor("#333333"));
		}
		
		return convertView;
	}
	class ViewHolder {
		RelativeLayout mbackground;
		TextView mtext;
	}
}
