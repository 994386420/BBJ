package com.bbk.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class MyViewPagerAdapter extends PagerAdapter{
	private List<Map<String, String>> list;
	private Context context;
	
	public MyViewPagerAdapter(Context mContext, List<Map<String, String>> mData) {
		super();
		this.context = mContext;
		this.list = mData;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 ==arg1;
	}
	
	@Override
	public void destroyItem(View container, int position, Object object) {
		// TODO Auto-generated method stub
//		super.destroyItem(container, position, object);
	}
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
	
		return container;
	}

}
