package com.bbk.adapter;

import java.util.List;
import java.util.Map;

import com.bbk.activity.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyCoinAdapter extends BaseAdapter{
	private List<Map<String, Object>> list;
	private Context context;
	public static int mPosition;
	private String yaoqingma;
	
	public MyCoinAdapter(List<Map<String, Object>> list,String yaoqingma,Context context){
		this.list = list;
		this.context =context;
		this.yaoqingma = yaoqingma;
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
			convertView = View.inflate(context, R.layout.mycoin_listview, null);
			vh.mstr1 = (TextView) convertView.findViewById(R.id.mstr1);
			vh.mstr2 = (TextView) convertView.findViewById(R.id.mstr2);
			vh.mstr3 = (TextView) convertView.findViewById(R.id.mstr3);
			vh.mgo = (TextView) convertView.findViewById(R.id.mgo);
			vh.mimg = (ImageView) convertView.findViewById(R.id.mimg);
			vh.mbiankuang = (RelativeLayout) convertView.findViewById(R.id.mbiankuang);
			vh.mhenggong = convertView.findViewById(R.id.mhenggong);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		Map<String, Object> map = list.get(position);
		String str1 = map.get("str1").toString();
		String str2 = map.get("str2").toString();
		String str3 = map.get("str3").toString();
		String isgo = map.get("isgo").toString();
//		Log.i("isgo",isgo+"---------------------");
		int drawable = (int) map.get("drawable");
		vh.mstr1.setText(str1);
		vh.mstr2.setText(str2);
		vh.mstr3.setText(str3);
		if (isgo.equals("0")) {
			vh.mgo.setText("去完成");
			vh.mbiankuang.setBackgroundResource(R.drawable.text_circle_orange_white_more);
			vh.mgo.setTextColor(Color.parseColor("#ff7d41"));
		}else{
			vh.mgo.setText("已完成");
			vh.mgo.setTextColor(Color.parseColor("#999999"));
			vh.mbiankuang.setBackgroundResource(R.drawable.text_circle_gray_white_more);
		}
		vh.mimg.setBackgroundResource(drawable);
		if (position == 8) {
			vh.mhenggong.setVisibility(View.GONE);
		}
		if (yaoqingma != null && !yaoqingma.equals("")) {
			if (position == 3) {
				vh.mgo.setVisibility(View.GONE);
				vh.mbiankuang.setVisibility(View.GONE);
			} else {
				vh.mgo.setVisibility(View.VISIBLE);
				vh.mbiankuang.setVisibility(View.VISIBLE);
			}
		}else {
			if (position == 2) {
				vh.mgo.setVisibility(View.GONE);
				vh.mbiankuang.setVisibility(View.GONE);
			} else {
				vh.mgo.setVisibility(View.VISIBLE);
				vh.mbiankuang.setVisibility(View.VISIBLE);
			}
		}
		if (position == 0){
			vh.mgo.setText("邀请好友");
			vh.mbiankuang.setBackgroundResource(R.drawable.text_circle_orange_white_more);
			vh.mgo.setTextColor(Color.parseColor("#ff7d41"));
		}
		return convertView;
	}
	class ViewHolder {
		TextView mstr1,mstr2,mstr3,mgo;
		ImageView mimg;
		RelativeLayout mbiankuang;
		View mhenggong;
	}

}

