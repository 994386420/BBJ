package com.bbk.adapter;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbk.activity.R;
import com.bbk.util.BaseTools;
import com.bbk.util.DensityUtil;

public class FilterCheckAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
	private List<Map<String, Object>> mData;
	private Context mContext;
	private OnItemClickLitener mOnItemClickLitener;
	private boolean isMore = false;
	private int lastPostion = -1;
	
	public FilterCheckAdapter() {
		super();
	}
	public FilterCheckAdapter(Context context, List<Map<String, Object>> data) {
		mData = data;
		mContext = context;
	}
	public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
		this.mOnItemClickLitener = mOnItemClickLitener;
	}
	
	public int getSelectedItemPostion() {
		return lastPostion;
	}
	
	public void setSelectedItemPostion(int lastPostion) {
		this.lastPostion = lastPostion;
	}
	
	public void setMore(boolean isMore) {
		this.isMore = isMore;
	}
	@Override
	public int getItemCount() {
		if(mData.size()>6&&!isMore){
			return 6;
		}
		return mData.size();
	}

	@Override
	public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
		try {
			
			final BrandViewHolder viewHolder = (BrandViewHolder) holder;
			
			LayoutParams params = viewHolder.itemText.getLayoutParams();
			params.width = (BaseTools.getWindowsWidth((Activity) mContext) - DensityUtil.dip2px(mContext, 64)) / 3;
			params.height = params.width * 2 / 5;
			viewHolder.itemText.setLayoutParams(params);
			
			if("yes".equals(mData.get(position).get("item_selected").toString())) {
				viewHolder.itemText.setBackgroundResource(R.drawable.bg_filter_brand_selected);
			} else {
				viewHolder.itemText.setBackgroundResource(R.drawable.bg_filter_brand_normal);
			}
			
			viewHolder.itemText.setText(mData.get(position).get("item_text").toString());
			
			viewHolder.itemView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Map<String, Object> map = mData.get(position);
					boolean selected = map.get("item_selected").equals("yes")?true:false;
					if(selected){
						viewHolder.itemText.setBackgroundResource(R.drawable.bg_filter_brand_normal);
						map.put("item_selected", "no");
					}else{
						viewHolder.itemText.setBackgroundResource(R.drawable.bg_filter_brand_selected);
						map.put("item_selected", "yes");
					}
				}
			});
			
			// 如果设置了回调，则设置点击事件
			if (mOnItemClickLitener != null) {
				holder.itemView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						int pos = holder.getLayoutPosition();
						mOnItemClickLitener.onItemClick(holder.itemView, pos);
					}
				});
				
				holder.itemView.setOnLongClickListener(new OnLongClickListener()
				{
					@Override
					public boolean onLongClick(View v)
					{
						int pos = holder.getLayoutPosition();
						mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
						return false;
					}
				});
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		return new BrandViewHolder(LayoutInflater.from(mContext).inflate(R.layout.popup_window_grid_item_brand_filter, arg0, false));
	}
	
	public interface OnItemClickLitener {
		void onItemClick(View view, int position);
		void onItemLongClick(View view, int position);
	}

	class BrandViewHolder extends ViewHolder {
		TextView itemText;
		public BrandViewHolder(View arg0) {
			super(arg0);
			itemText = (TextView) arg0.findViewById(R.id.item_text);
		}
	}
}
