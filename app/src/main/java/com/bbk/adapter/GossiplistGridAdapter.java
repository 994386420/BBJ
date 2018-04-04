package com.bbk.adapter;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import com.bbk.activity.CompareActivity;
import com.bbk.activity.IntentActivity;
import com.bbk.activity.R;
import com.bbk.adapter.ListViewAdapter2.ViewHolder;
import com.bbk.util.DensityUtil;
import com.bbk.view.BaseViewHolder;
import com.bumptech.glide.Glide;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GossiplistGridAdapter extends BaseAdapter{
	private List<String> list;
	private Context context;
	public GossiplistGridAdapter(List<String> list,Context context){
		this.list = list;
		this.context =context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		final ViewHolder vh;		
			if (convertView == null) {
				vh = new ViewHolder();
				convertView = View.inflate(context, R.layout.gossip_list_gridview, null);
				vh.img = (ImageView) convertView.findViewById(R.id.mimg);
				convertView.setTag(vh);
			}else{
				vh = (ViewHolder) convertView.getTag();
			}
			String img = list.get(position);
	        try {
				Glide.with(context)
				.load(img)
				.into(vh.img);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return convertView;
	}
	class ViewHolder {
		ImageView img;
	}
}
