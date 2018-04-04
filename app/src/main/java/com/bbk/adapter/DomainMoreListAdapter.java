package com.bbk.adapter;

import java.util.List;
import java.util.Map;

import com.bbk.activity.R;
import com.bumptech.glide.Glide;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DomainMoreListAdapter extends BaseAdapter{
	private List<Map<String, String>> list;
	private Context context;
	private setMyOnClickListener mListener;

    //写一个设置接口监听的方法
    public void setOnClickListener(setMyOnClickListener listener) {
        mListener = listener;
    }
	public DomainMoreListAdapter(List<Map<String, String>> list,Context context){
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
	public View getView(final int position, View convertView, ViewGroup arg2) {
		final ViewHolder vh;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = View.inflate(context, R.layout.domain_more_listview, null);
			vh.img = (ImageView) convertView.findViewById(R.id.img);
			vh.name1 = (TextView) convertView.findViewById(R.id.name1);
			vh.name2 = (TextView) convertView.findViewById(R.id.name2);
			vh.cancle = (TextView) convertView.findViewById(R.id.cancle);
			
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}
		
		vh.cancle.setOnClickListener(new OnClickListener() {
			
			private int positon1=position;
			@Override
			public void onClick(View arg0) {
				mListener.onClick(positon1);
			}
		});
		Map<String, String> map = list.get(position);
		String img = map.get("img");
		String name1 = map.get("name1");
		String name2 = map.get("name2");
		vh.name1.setText(name1);
		vh.name2.setText(name2);
		 Glide.with(context)
			.load(img)
			.into(vh.img);

        
		return convertView;
	}
	class ViewHolder {
		ImageView img;
		TextView name1,name2,cancle;
		
	}
	public interface setMyOnClickListener{
		public void onClick(int positon);
	}
}
