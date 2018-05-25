package com.bbk.adapter;

import java.util.List;
import java.util.Map;

import com.bbk.activity.R;
import com.bbk.activity.SortActivity;
import com.bbk.adapter.DomainMoreListAdapter.setMyOnClickListener;
import com.bbk.adapter.MesageCListSendAdapter.ViewHolder;

import com.bumptech.glide.Glide;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MesageCListReceiveAdapter extends BaseAdapter {
	private List<Map<String, String>> list;
	private Context context;
	private MyClickListener mListener;
	private int position1;

    //写一个设置接口监听的方法
    public void setOnClickListener(MyClickListener listener) {
        mListener = listener;
    }
	public MesageCListReceiveAdapter(List<Map<String, String>> list, Context context) {
		this.list = list;
		this.context = context;
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
			convertView = View.inflate(context, R.layout.mesage_listview_receive, null);
			vh.mimg = (ImageView) convertView.findViewById(R.id.mimg);
			vh.mtime = (TextView) convertView.findViewById(R.id.mtime);
			vh.mcontent = (TextView) convertView.findViewById(R.id.mcontent);
			vh.mtitle = (TextView) convertView.findViewById(R.id.mtitle);
			vh.mname = (TextView) convertView.findViewById(R.id.mname);
			vh.mreply = (TextView) convertView.findViewById(R.id.mreply);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		Map<String, String> map = list.get(position);
		vh.mname.setText(map.get("nickname"));
		vh.mtime.setText(map.get("dt"));
		vh.mtitle.setText(map.get("title"));
		vh.mcontent.setText(map.get("content"));
		position1 = position;
		vh.mreply.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mListener.onClick(position1);
			}
		});
		Glide.with(context).load(map.get("imgurl")).into(vh.mimg);
		return convertView;
	}

	class ViewHolder {
		TextView mtime, mcontent, mtitle, mreply, mname;
		ImageView mimg;
	}
	  //自定义接口，用于回调按钮点击事件到Activity
	  public interface MyClickListener{
	      public void onClick(int position);
	  }

	
}