package com.bbk.adapter;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import com.bbk.activity.CompareActivity;
import com.bbk.activity.IntentActivity;
import com.bbk.activity.R;
import com.bbk.activity.ResultDialogActivity;
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

public class ShopCartGridAdapter extends BaseAdapter{
	private List<Map<String, String>> list;
	private Context context;
	public ShopCartGridAdapter(List<Map<String, String>> list,Context context){
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
				convertView = View.inflate(context, R.layout.shopcart_gridview, null);
				vh.img = (ImageView) convertView.findViewById(R.id.mimg);
				convertView.setTag(vh);
			}else{
				vh = (ViewHolder) convertView.getTag();
			}
			Map<String, String> map = list.get(position);
			String img = map.get("img");
	        Glide.with(context)
			.load(img)
			.placeholder(R.mipmap.zhanwei_04)
			.into(vh.img);
		return convertView;
	}
	class ViewHolder {
		ImageView img;
	}
}
