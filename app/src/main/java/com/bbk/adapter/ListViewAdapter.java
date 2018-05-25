package com.bbk.adapter;

import java.util.List;

import com.bbk.activity.R;
import com.bbk.activity.SortActivity;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter{
	private String[] str;
	private Context context;
	public static int mPosition;
	
	public ListViewAdapter(String[] str,Context context){
		this.str = str;
		this.context =context;
	}
	
	@Override
	public int getCount() {
		return str.length;
	}

	@Override
	public Object getItem(int position) {
		return str[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder vh;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.list_text, null);
			TextView mtext = (TextView) convertView.findViewById(R.id.mtext);
			View fenge = convertView.findViewById(R.id.fenge);
			vh = new ViewHolder(mtext,fenge);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		mPosition = position;
		vh.tv.setText(str[position]);
		if (position == SortActivity.mPosition) {
			convertView.setBackgroundColor(Color.parseColor("#f3f3f3"));
			vh.tv.setTextColor(Color.parseColor("#0098ff"));
			vh.fenge.setBackgroundColor(Color.parseColor("#f3f3f3"));
		} else {
			convertView.setBackgroundColor(Color.parseColor("#ffffff"));
			vh.tv.setTextColor(Color.parseColor("#333333"));
			vh.fenge.setBackgroundColor(Color.parseColor("#cbcbcb"));
		}
		return convertView;
	}
	class ViewHolder {
		TextView tv;
		View fenge;

		public ViewHolder(TextView tv,View fenge) {
			this.tv = tv;
			this.fenge = fenge;
		}
	}

}
