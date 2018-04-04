package com.bbk.adapter;

import java.util.List;
import java.util.Map;

import com.bbk.activity.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class HotBrandAdapter extends RecyclerView.Adapter<HotBrandAdapter.ViewHolder>{ 
	private LayoutInflater mInflater; 
	private List<String> mDatas;
	private Context mContext;
	private MyItemClickListener mListener;
	
	public HotBrandAdapter(Context context, List<String> datats) 
	{ 
		mInflater = LayoutInflater.from(context); 
		mContext = context;
		mDatas = datats; 
	} 
	public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener
	{ 
		private MyItemClickListener mListener;
		public ViewHolder(View arg0,MyItemClickListener listener) { 
			super(arg0); 
			 this.mListener = listener;  
			 arg0.setOnClickListener(this);  
		}
		ImageView mImg; 

		@Override
		public void onClick(View v) {
			if(mListener != null){  
	            mListener.onItemClick(v,getPosition());  
	        }  
		} 
	} 
	@Override
	public int getItemCount() 
	{ 
		return mDatas.size(); 
	} 
	/** 
	 * 创建ViewHolder 
	 */
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) { 
	View view = mInflater.inflate(R.layout.activity_recycler_item2, viewGroup, false); 
	ViewHolder viewHolder = new ViewHolder(view,mListener); 
	viewHolder.mImg = (ImageView) view .findViewById(R.id.id_index_gallery_item_image); 
	return viewHolder; 
	} 
	   /** 
  * 设置Item点击监听 
  * @param listener 
  */  
		public void setOnItemClickListener(MyItemClickListener listener){  
			this.mListener = listener;  
		} 
	/** 
	 * 设置值 
	 */
	@Override
	public void onBindViewHolder(final ViewHolder viewHolder, final int i) 
	{ 
		String imageUrl = mDatas.get(i);
		Glide.with(mContext)
		.load(imageUrl)
		.skipMemoryCache(true)
		.diskCacheStrategy(DiskCacheStrategy.RESULT)
		.placeholder(R.mipmap.zw_img_332)
		.dontAnimate()
		.thumbnail(0.5f)
		.into(viewHolder.mImg);
	}
	public interface MyItemClickListener {  
	    public void onItemClick(View view,int postion);  
	}
 

} 
