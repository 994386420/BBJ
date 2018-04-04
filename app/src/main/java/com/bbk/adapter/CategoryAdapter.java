package com.bbk.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bbk.activity.R;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
	private ArrayList<HashMap<String, Object>> mData;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
	
	public CategoryAdapter(Context context, ArrayList<HashMap<String, Object>> data) {
		mData = data;
        mContext = context;
	}
	
	@Override
	public int getItemCount() {
		return mData.size();
	}
	
	class CategoryViewHolder extends ViewHolder {
		TextView itemTitle;
		
		
		public CategoryViewHolder(View itemView) {
			super(itemView);
			itemTitle = (TextView) itemView.findViewById(R.id.item_title);
			
		}
	}
	
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		return new CategoryViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_left_category, arg0, false));	
	}
	

	@Override
	public void onBindViewHolder(ViewHolder vh, int index) {
		final CategoryViewHolder viewHolder = (CategoryViewHolder) vh;
		
		viewHolder.itemTitle.setText(mData.get(index).get("itemTitle").toString());
		
		String type =  mData.get(index).get("itemType").toString();
		if("selected".equals(type)) {
//			viewHolder.itemView.setBackgroundColor(Color.parseColor("#F5F5F5"));
			viewHolder.itemTitle.setTextColor(Color.parseColor("#0C9CFD"));
		}
		if("normal".equals(type)) {
//			viewHolder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
			viewHolder.itemTitle.setTextColor(Color.parseColor("#323232"));
		}
		
		if (mOnItemClickListener != null) {
			vh.itemView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int pos = viewHolder.getLayoutPosition();
					mOnItemClickListener.onItemClick(viewHolder.itemView, pos);
				}
			});
		}
	}
	
	public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
		this.mOnItemClickListener = mOnItemClickListener;
	}
	
	public interface OnItemClickListener {
		void onItemClick(View view, int position);
	}
	
}
