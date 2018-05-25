package com.bbk.adapter;

import java.util.List;
import java.util.Map;

import com.bbk.activity.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OrderListAdapter extends BaseAdapter{
	private List<Map<String, String>> list;
	private Context context;
	public static int mPosition;
	
	public OrderListAdapter(List<Map<String, String>> list,Context context){
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
			convertView = View.inflate(context, R.layout.orderlist_listview, null);
			vh.mdomain = (TextView) convertView.findViewById(R.id.mdomain);
			vh.mtime = (TextView) convertView.findViewById(R.id.mtime);
			vh.mselectimg = (ImageView) convertView.findViewById(R.id.mselectimg);
			vh.mtimeimg = (ImageView) convertView.findViewById(R.id.mtimeimg);
			vh.top = (RelativeLayout) convertView.findViewById(R.id.top);
			vh.mxian = convertView.findViewById(R.id.mxian);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		if (position == 0) {
			vh.mtimeimg.setVisibility(View.VISIBLE);
		}
		Map<String, String> map = list.get(position);
		String domain = map.get("domainCh");
		String time = map.get("time");
		String isselect = map.get("isselect");
		String isbianji = map.get("isbianji");
		if (isselect.equals("0")) {
			vh.mselectimg.setImageResource(R.mipmap.weixuanzhongyuan);
		}else{
			vh.mselectimg.setImageResource(R.mipmap.xuanzhongyuan);
		}
		if (isbianji.equals("0")) {
			vh.mselectimg.setVisibility(View.GONE);
			vh.top.setVisibility(View.VISIBLE);
			vh.mxian.setVisibility(View.VISIBLE);
			vh.mtime.setVisibility(View.VISIBLE);
		}else{
			vh.mselectimg.setVisibility(View.VISIBLE);
			vh.top.setVisibility(View.GONE);
			vh.mxian.setVisibility(View.GONE);
			vh.mtime.setVisibility(View.GONE);
		}
		vh.mdomain.setText(domain);
		vh.mtime.setText(time);
		return convertView;
	}
	class ViewHolder {
		TextView mdomain,mtime;
		ImageView mselectimg,mtimeimg;
		View mxian;
		RelativeLayout top;
	}

}

