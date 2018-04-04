package com.bbk.adapter;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.bbk.activity.R;
import com.bbk.util.BaseTools;
import com.bbk.util.DensityUtil;

public class FilterCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private List<Map<String, Object>> mData;
	private Context mContext;
	private OnChangeLitener onChangeLitener;
	
	
	private int lastPostion = -1;
	
	public FilterCategoryAdapter() {
		super();
	}
	
	public FilterCategoryAdapter(Context context, List<Map<String, Object>> data) {
		mData = data;
		mContext = context;
	}

	public int getSelectedItemPostion() {
		return lastPostion;
	}
	
	public void setSelectedItemPostion(int lastPostion) {
		this.lastPostion = lastPostion;
	}
	
	@Override
	public int getItemCount() {
		return mData.size();
	}
	
	public void setOnChangeLitener(OnChangeLitener onChangeLitener) {
		this.onChangeLitener = onChangeLitener;
	}

	@Override
	public void onBindViewHolder(final RecyclerView.ViewHolder arg0, final int arg1) {
		try {
			
			CategoryViewHolder categoryViewHolder = (CategoryViewHolder) arg0;
			
			LayoutParams params = categoryViewHolder.itemText.getLayoutParams();
			params.width = (BaseTools.getWindowsWidth((Activity) mContext) - DensityUtil.dip2px(mContext, 64)) / 3;
			params.height = (int) (params.width*0.28);
			categoryViewHolder.itemText.setTextSize((float) (params.height/4.7));
			categoryViewHolder.itemText.setLayoutParams(params);
			categoryViewHolder.itemText.setBackgroundResource(R.drawable.bg_promote_item);
			
			if("yes".equals(mData.get(arg1).get("item_selected").toString())) {
				categoryViewHolder.itemText.setTextColor(Color.parseColor("#0098FF"));
				lastPostion = arg1;
			} else {
				categoryViewHolder.itemText.setTextColor(Color.parseColor("#323232"));
			}
			
			categoryViewHolder.itemText.setText(mData.get(arg1).get("item_text").toString());
			
			categoryViewHolder.itemView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int pos = arg0.getLayoutPosition();
					if(pos != lastPostion) {
						mData.get(pos).put("item_selected", "yes");
						if(lastPostion != -1 && lastPostion <= mData.size()) {
							mData.get(lastPostion).put("item_selected", "no");
							notifyItemChanged(lastPostion);
						}
						notifyItemChanged(pos);
						lastPostion = pos;
						if(onChangeLitener != null){
							onChangeLitener.onChange(v, pos);
						}
					}
				}
			});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		return new CategoryViewHolder(LayoutInflater.from(mContext).inflate(R.layout.popup_window_grid_item_brand_filter, arg0, false));
	}
	
	public interface OnChangeLitener {
		void onChange(View view, int position);
	}

	class CategoryViewHolder extends ViewHolder {
		TextView itemText;
		public CategoryViewHolder(View view) {
			super(view);
			itemText = (TextView) view.findViewById(R.id.item_text);
		}
	}
}
