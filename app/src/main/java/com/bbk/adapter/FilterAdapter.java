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
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bbk.activity.R;
import com.bbk.util.DensityUtil;

public class FilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private ArrayList<HashMap<String, Object>> mData;
	private Context mContext;
	private OnItemClickLitener mOnItemClickLitener;
	
	private int lastPostion = -1;
	
	public FilterAdapter() {
		super();
	}
	
	public FilterAdapter(Context context, ArrayList<HashMap<String, Object>> data) {
		mData = data;
		mContext = context;
	}

	public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
		this.mOnItemClickLitener = mOnItemClickLitener;
	}
	
	@Override
	public int getItemCount() {
		return mData.size();
	}

	@Override
	public void onBindViewHolder(final RecyclerView.ViewHolder arg0, final int arg1) {
		try {
			final FilterViewHolder cheapViewHolder = (FilterViewHolder) arg0;
			
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) cheapViewHolder.itemDivider.getLayoutParams();
			params.setMargins(DensityUtil.dip2px(mContext, 8), 0, DensityUtil.dip2px(mContext, 8), 0);
			cheapViewHolder.itemDivider.setLayoutParams(params);
			
			cheapViewHolder.itemText.setText(mData.get(arg1).get("item_text").toString());
			if("visible".equals(mData.get(arg1).get("item_img").toString())) {
				if("third".equals(mData.get(arg1).get("item_type").toString())) {
					cheapViewHolder.itemImg.setVisibility(View.VISIBLE);
				}
				lastPostion = arg1;
				
				cheapViewHolder.itemText.setTextColor(Color.parseColor("#EE240E"));
			} else {
				cheapViewHolder.itemImg.setVisibility(View.GONE);
				cheapViewHolder.itemText.setTextColor(Color.parseColor("#464646"));
			}
			
//			arg0.itemView.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					int pos = cheapViewHolder.getLayoutPosition();
//					
//					if(pos != lastPostion) {
//						((HashMap<String,Object>)mData.get(pos)).put("item_img", "visible");
//						if(lastPostion != -1) {
//							((HashMap<String,Object>)mData.get(lastPostion)).put("item_img", "gone");
//							notifyItemChanged(lastPostion);
//						}
//						notifyItemChanged(pos);
//						lastPostion = pos;
//					} else {
//						((HashMap<String,Object>)mData.get(pos)).put("item_img", "gone");
//						notifyItemChanged(pos);
//						lastPostion = -1;
//					}
//				}
//			});
			
			if (mOnItemClickLitener != null) {
				arg0.itemView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						int pos = cheapViewHolder.getLayoutPosition();
						
						if(pos != lastPostion) {
							mData.get(pos).put("item_img", "visible");
							if(lastPostion != -1) {
								mData.get(lastPostion).put("item_img", "gone");
								notifyItemChanged(lastPostion);
							}
							notifyItemChanged(pos);
							lastPostion = pos;
						} else {
							if("third".equals(mData.get(arg1).get("item_type").toString())) {
								mData.get(pos).put("item_img", "gone");
								notifyItemChanged(pos);
								lastPostion = -1;
							}
						}
						
						mOnItemClickLitener.onItemClick(cheapViewHolder.itemView, pos);
					}
				});
				
				arg0.itemView.setOnLongClickListener(new OnLongClickListener()
				{
					@Override
					public boolean onLongClick(View v)
					{
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
		return new FilterViewHolder(LayoutInflater.from(mContext).inflate(R.layout.listview_item_filter, arg0, false));
	}
	
	public interface OnItemClickLitener {
		void onItemClick(View view, int position);
		void onItemLongClick(View view, int position);
	}

	class FilterViewHolder extends ViewHolder {
		ImageView itemImg;
		TextView itemText;
		View itemDivider;
		public FilterViewHolder(View arg0) {
			super(arg0);
			itemText = (TextView) arg0.findViewById(R.id.item_text);
			itemImg = (ImageView) arg0.findViewById(R.id.item_img);
			itemDivider = arg0.findViewById(R.id.item_divider);
		}
	}
	
	public int getSelectedItemPostion() {
		return lastPostion;
	}
	public void setSelectedItemPostion(int lastPostion) {
		this.lastPostion = lastPostion;
	}
}
