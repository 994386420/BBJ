package com.bbk.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bbk.activity.R;

public class RankSubCategoryAdapter extends SimpleAdapter{
	private String selectAddtion;
	private List<? extends Map<String, ?>> data;

	public RankSubCategoryAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		this.data = data;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		Map<String, ?> itemMap = data.get(position);
		String addtion = itemMap.get("itemAddtion").toString();
		TextView tv = (TextView)view.findViewById(R.id.item_text);
		if(addtion.equals(selectAddtion)){
			tv.setTextColor(Color.parseColor("#40B2FF"));
		}else{
			tv.setTextColor(Color.parseColor("#323232"));
		}
		return view;
	}
	public void setSelectAddtion(String selectAddtion){
		this.selectAddtion = selectAddtion;
	}

}
