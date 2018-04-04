package com.bbk.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bbk.activity.R;
import com.bbk.view.HexagonImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class DCSimpleAdapter extends SimpleAdapter {
	
    private List<? extends Map<String, ?>> mData;

    private int mResource;
    private LayoutInflater mInflater;
    private Context mContext;
//	private Map<String,Bitmap> mBitmaps;
    
	public DCSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource,Map<String,Bitmap> bitmaps) {
		super(context, data, resource, null, null);
		mData = data;
        mResource = resource;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
//        mBitmaps = bitmaps;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent, mResource);
	}
	
	private View createViewFromResource(int position, View convertView,
            ViewGroup parent, int resource) {
        View v;
        if (convertView == null) {
            v = mInflater.inflate(resource, parent, false);
        } else {
            v = convertView;
        }
        bindView(position, v);
        return v;
    }
	
	private void bindView(int position, View view) {
        final Map<String,?> dataMap = mData.get(position);
        TextView textView = (TextView) view.findViewById(R.id.textView);
        String brand = (String) dataMap.get("brand");
        textView.setText(brand);
        String brandImg = (String) dataMap.get("brandImage");
        HexagonImageView imageView = (HexagonImageView) view.findViewById(R.id.imageView);
        /*Bitmap bit = mBitmaps.get(brandImg);
        if(bit != null){
        	imageView.setImageBitmap(bit);
        }*/
        
        Glide.with(mContext)
		.load(brandImg)
		.placeholder(R.mipmap.default_img)
		.diskCacheStrategy(DiskCacheStrategy.NONE)
		.dontAnimate()
		.into(imageView);
    }
}
