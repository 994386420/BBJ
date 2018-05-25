package com.bbk.adapter;

import java.util.List;
import java.util.Map;

import com.bbk.activity.R;
import com.bumptech.glide.Glide;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MesageCListSysAdapter extends BaseAdapter{
	private List<Map<String, String>> list;
	private Context context;
	
	public MesageCListSysAdapter(List<Map<String, String>> list,Context context){
		this.list = list;
		this.context =context;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder vh;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = View.inflate(context, R.layout.mesage_listview_system, null);
			vh.mtime = (TextView) convertView.findViewById(R.id.mtime);
			vh.mcontent = (TextView) convertView.findViewById(R.id.mcontent);
			vh.notread = (RelativeLayout) convertView.findViewById(R.id.notread);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		Map<String, String> map = list.get(position);
		String message = map.get("message");
		String dtime = map.get("dtime");
		String isread = map.get("isread");
		if ("1".equals(isread)) {
			vh.notread.setVisibility(View.GONE);
		}else{
			vh.notread.setVisibility(View.VISIBLE);
		}
		vh.mtime.setText(dtime);
		vh.mcontent.setText(message);
		return convertView;
	}
	class ViewHolder {
		TextView mtime,mcontent;
		RelativeLayout notread;
	}

}
