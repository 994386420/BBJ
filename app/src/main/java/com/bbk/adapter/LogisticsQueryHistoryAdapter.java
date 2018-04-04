package com.bbk.adapter;

import java.util.List;
import java.util.Map;

import com.bbk.activity.R;
import com.bbk.activity.SortActivity;
import com.bbk.fragment.RankFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LogisticsQueryHistoryAdapter extends BaseAdapter{
	private List<Map<String, String>> list;
	private Context context;
	public static int mPosition;
	
	public LogisticsQueryHistoryAdapter(List<Map<String, String>> list,Context context){
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
			convertView = View.inflate(context, R.layout.logistics_query_history, null);
			vh.text = (TextView) convertView.findViewById(R.id.text);
			vh.time = (TextView) convertView.findViewById(R.id.time);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		Map<String, String> map = list.get(position);
		String number = map.get("number");
		String time = map.get("time");
		vh.text.setText(number);
		vh.time.setText(time);
		return convertView;
	}
	class ViewHolder {
		TextView text,time;
	}

}

