package com.bbk.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bbk.activity.R;
import com.bbk.view.BaseViewHolder;

/**
 * @Description:gridview的Adapter
 * @author http://blog.csdn.net/finddreams
 */
public class DCServiceCountGridAdapter extends BaseAdapter {
	private Context mContext;
	private List<Map<String, Object>> mData;

	public DCServiceCountGridAdapter(Context mContext, List<Map<String, Object>> mData) {
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
					R.layout.grid_item_dc_service_count, parent, false);
		}
		Map<String, Object> map = mData.get(position);
		TextView textView = BaseViewHolder.get(convertView, R.id.textView);
		String domain = map.get("domain").toString();
		textView.setText(domain);
		View colorView = BaseViewHolder.get(convertView, R.id.colorView);
		GradientDrawable bgd = (GradientDrawable)colorView.getBackground();
		int color = (int) map.get("color");
		bgd.setColor(color);
		return convertView;
	}

}
