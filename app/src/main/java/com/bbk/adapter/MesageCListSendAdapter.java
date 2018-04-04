package com.bbk.adapter;

import java.util.List;
import java.util.Map;

import com.bbk.activity.R;
import com.bbk.activity.SortActivity;
import com.bbk.fragment.RankFragment;
import com.bumptech.glide.Glide;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MesageCListSendAdapter extends BaseAdapter{
	private List<Map<String, String>> list;
	private Context context;
	
	public MesageCListSendAdapter(List<Map<String, String>> list,Context context){
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
			convertView = View.inflate(context, R.layout.mesage_listview_send, null);
			vh.mimg = (ImageView) convertView.findViewById(R.id.mimg);
			vh.mtime = (TextView) convertView.findViewById(R.id.mtime);
			vh.mcontent = (TextView) convertView.findViewById(R.id.mcontent);
			vh.mtitle = (TextView) convertView.findViewById(R.id.mtitle);
			vh.mtext1 = (TextView) convertView.findViewById(R.id.mtext1);
			vh.mtext2 = (TextView) convertView.findViewById(R.id.mtext2);
			vh.mname = (TextView) convertView.findViewById(R.id.mname);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		Map<String, String> map = list.get(position);
		if (map.get("nickname")== null) {
			vh.mtext1.setVisibility(View.VISIBLE);
			vh.mtext2.setVisibility(View.GONE);
			vh.mname.setVisibility(View.GONE);
		}else{
			vh.mtext1.setVisibility(View.GONE);
			vh.mtext2.setVisibility(View.VISIBLE);
			vh.mname.setVisibility(View.VISIBLE);
			vh.mname.setText(map.get("nickname"));
		}
		vh.mtime.setText(map.get("dt"));
		vh.mtitle.setText(map.get("title"));
		vh.mcontent.setText(map.get("content"));
		Glide.with(context).load(map.get("imgurl")).into(vh.mimg);
		return convertView;
	}
	class ViewHolder {
		TextView mtime,mcontent,mtitle,mtext1,mtext2,mname;
		ImageView mimg;
	}

}
