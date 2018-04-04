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

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder>{ 
	private LayoutInflater mInflater; 
	private List<Map<String, Object>> mDatas;
	private Context mContext;
	private MyItemClickListener1 mListener;
	
	public GalleryAdapter(Context context, List<Map<String, Object>> datats) 
	{ 
		mInflater = LayoutInflater.from(context); 
		mContext = context;
		mDatas = datats; 
	} 
	public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener
	{ 
		private MyItemClickListener1 mListener;
		public ViewHolder(View arg0,MyItemClickListener1 listener) { 
			super(arg0); 
			 this.mListener = listener;  
			 arg0.setOnClickListener(this);  
		}
		ImageView mImg; 
		TextView mgood; 
		TextView mprice; 
		TextView mquote; 

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
	View view = mInflater.inflate(R.layout.activity_recycler_item, viewGroup, false); 
	ViewHolder viewHolder = new ViewHolder(view,mListener); 
	viewHolder.mImg = (ImageView) view .findViewById(R.id.id_index_gallery_item_image); 
	viewHolder.mgood = (TextView) view .findViewById(R.id.mgood); 
	viewHolder.mprice = (TextView) view .findViewById(R.id.mprice); 
	viewHolder.mquote = (TextView) view .findViewById(R.id.mquote); 
	return viewHolder; 
	} 
	   /** 
  * 设置Item点击监听 
  * @param myItemClickListener 
  */  
		public void setOnItemClickListener(MyItemClickListener1 myItemClickListener){  
			this.mListener = myItemClickListener;  
		} 
	/** 
	 * 设置值 
	 */
	@Override
	public void onBindViewHolder(final ViewHolder viewHolder, final int i) 
	{ 
		Map<String, Object> map = mDatas.get(i);
		String good = map.get("good").toString();
		String price = map.get("price").toString();
		String quote = map.get("quote").toString()+"家报价";
		viewHolder.mgood.setText(good);
		viewHolder.mprice.setText(price);
		viewHolder.mquote.setText(quote);
		String imageUrl = map.get("imageUrl").toString();
		Glide.with(mContext)
		.load(imageUrl)
		.skipMemoryCache(true)
		.placeholder(R.mipmap.zw_img_300)
		.diskCacheStrategy(DiskCacheStrategy.RESULT)
		.dontAnimate()
		.into(viewHolder.mImg);
	}
	public interface MyItemClickListener1 {  
	    public void onItemClick(View view,int postion);  
	}
 

} 
