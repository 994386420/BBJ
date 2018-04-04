package com.bbk.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bbk.activity.R;
import com.bbk.util.DensityUtil;
import com.bbk.view.BaseViewHolder;
import com.bbk.view.DCGridView;

/**
 * @Description:gridview的Adapter
 */
public class DCGridAdapter extends BaseAdapter {
	private DCGridView mDCGridView;
	private Context mContext;
	private List<Map<String, Object>> mData;
	private String[] mTitle;
	public DCGridAdapter(Context mContext, List<Map<String, Object>> mData,String[] mTitle,DCGridView dCGridView) {
		super();
		this.mContext = mContext;
		this.mData = mData;
		this.mTitle = mTitle;
		this.mDCGridView = dCGridView;
	}

	@Override
	public int getCount() {
		return mTitle.length+mData.size();
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
		String title = null;
		if(position<mTitle.length){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item_title_dc, parent, false);
			title = mTitle[position];
		}else{
			convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item_dc, parent, false);
			Map<String, Object> map = mData.get(position - mTitle.length);
			title = map.get("title").toString();
		}
		TextView textView = BaseViewHolder.get(convertView, R.id.textView);
		textView.setText(title);
		//设置不重叠边框
		if(position < 2){
			convertView.setBackgroundResource(R.drawable.bg_dc_table_top_bottom_left_border);
		}else if(position == 2){
			convertView.setBackgroundResource(R.drawable.bg_dc_table_top_bottom_left_right_border);
		}else if((position+1)%3 == 0){
			convertView.setBackgroundResource(R.drawable.bg_dc_table_bottom_left_right_border);
		}else{
			convertView.setBackgroundResource(R.drawable.bg_dc_table_bottom_left_border);
		}
		return convertView;
	}
	public void notifyDataSetChanged(String[] mTitle) {
		this.mTitle = mTitle;
		super.notifyDataSetChanged();
		if(mData.size() > 0){
			//每一行单元格高度不一样会产生类似高度过高的问题
			int left = mDCGridView.getPaddingLeft();
			int top = mDCGridView.getPaddingTop();
			int right = mDCGridView.getPaddingRight();
			 
			
			//取得title的高度
			int titleH = DensityUtil.dip2px(mContext, 30)+DensityUtil.sp2px(mContext,14);
			//取得不是title的高度
			int contentH = DensityUtil.dip2px(mContext, 24)+DensityUtil.sp2px(mContext,12);
			//有多少误差行
			int row = mData.size() / 3;
			
			//内容的每一行要产生多少误差
			int errorH = titleH - contentH;
			mDCGridView.setPadding(left, top, right, -(errorH * row));
		}
	}

}
