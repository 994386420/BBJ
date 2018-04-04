package com.bbk.adapter;

import java.util.List;
import java.util.Map;

import com.bbk.activity.R;
import com.bbk.view.BaseViewHolder;
import com.bumptech.glide.Glide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SuperMarketListAdapter extends BaseAdapter{
	private Context mContext;
	private List<Map<String, Object>> mData;

	public SuperMarketListAdapter(Context mContext, List<Map<String, Object>> mData) {
		super();
		this.mContext = mContext;
		this.mData = mData;
	}

	@Override
	public int getCount() {
		return mData.size();
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
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.supermarketlist,parent,false);
		}
		Map<String, Object> map = mData.get(position);
		TextView mtitle = BaseViewHolder.get(convertView, R.id.mtitle);
		TextView mquote = BaseViewHolder.get(convertView, R.id.mquote);
		TextView mgood = BaseViewHolder.get(convertView, R.id.mgood);
		TextView mbigprice = BaseViewHolder.get(convertView, R.id.mbigprice);
		TextView mlittleprice = BaseViewHolder.get(convertView, R.id.mlittleprice);
		ImageView imageView = BaseViewHolder.get(convertView, R.id.iv_gridView_item);
		String imageUrl = map.get("imageUrl").toString();
		String title = map.get("title").toString();
		String price = map.get("price").toString();
		String bigprice;
        String littleprice;
        if (price.contains(".")) {
        	int end = price.indexOf(".");
    		bigprice = price.substring(0, end);
    		littleprice = price.substring(end, price.length());
		}else{
			bigprice = price;
			littleprice = ".0";
		}
		String quote = map.get("quote").toString()+"条报价";
		String good = map.get("good").toString()+"条好评";
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
	    int width = wm.getDefaultDisplay().getWidth();
	    LayoutParams params = imageView.getLayoutParams();
	    params.height = width/2;
	    imageView.setLayoutParams(params);
		mtitle.setText(title);
		mquote.setText(quote);
		mgood.setText(good);
		mbigprice.setText(bigprice);
		mlittleprice.setText(littleprice);
		Glide.with(mContext)
		.load(imageUrl)
		.skipMemoryCache(true)
		.placeholder(R.mipmap.zw_img_300)
		.dontAnimate()
		.thumbnail(0.5f)
		.into(imageView);
		return convertView;
	}

}
