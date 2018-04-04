package com.bbk.adapter;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bbk.activity.R;
import com.bbk.util.BaseTools;
import com.bbk.util.DateUtil;
import com.bbk.util.DensityUtil;
import com.bbk.util.NumberUtil;
import com.bbk.view.BaseViewHolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class PromoteResultAdapter extends SimpleAdapter {
	
	private int[] mTo;
    private String[] mFrom;

    private List<? extends Map<String, ?>> mData;

    private int mResource;
    private LayoutInflater mInflater;
    private Context mContext;
	
	public PromoteResultAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
		mData = data;
        mResource = resource;
        mFrom = from;
        mTo = to;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
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
        Object startDateObject = mData.get(position).get("startdate");
        if(startDateObject != null) {
        	String startDate = startDateObject.toString();
        	if(DateUtil.compareDateSS(startDate, DateUtil.getDate()) > 0)
        		v.setBackgroundColor(Color.parseColor("#90DDBA"));
        }
        
        bindView(position, v);

        return v;
    }
	
	private void bindView(int position, View view) {
        final Map<String, ?> dataSet = mData.get(position);
        if (dataSet == null) {
            return;
        }
        if(dataSet.containsKey("CategoryActivityHasData")) {
        	String str = dataSet.get("CategoryActivityHasData").toString();
        	if(NumberUtil.parseInt(str, 0) > 0) {
        		view.setBackgroundColor(Color.parseColor("#FFFFFF"));
        	} else {
        		view.setBackgroundColor(Color.parseColor("#EFEFEF"));
        	}
        }
        
        for (int i = 0; i < mFrom.length; i++) {
            final View v = view.findViewById(mTo[i]);
            if (v != null) {
                final Object data = dataSet.get(mFrom[i]);
                String text = data == null ? "" : data.toString();
                if (text == null) {
                    text = "";
                }
                if (v instanceof Checkable) {
                    if (data instanceof Boolean) {
                        ((Checkable) v).setChecked((Boolean) data);
                    } else if (v instanceof TextView) {
                        setViewText((TextView) v, text);
                    } else {
                        throw new IllegalStateException(v.getClass().getName() +
                                " should be bound to a Boolean, not a " +
                                (data == null ? "<unknown type>" : data.getClass()));
                    }
                } else if (v instanceof TextView) {
                    setViewText((TextView) v, text);
                } else if (v instanceof ImageView) {
                    if (data instanceof Integer) {
                    	v.setVisibility(View.VISIBLE);
                        setViewImage((ImageView) v, (Integer) data);                            
                    } else {
                    	if(TextUtils.isEmpty(text)) {
                    		text = "http://www.bibijing.com/";
                    	}
                    	if("view.gone".equals(text)) {
                    		v.setVisibility(View.GONE);
                    	} else {
                    		v.setVisibility(View.VISIBLE);
                    		LayoutParams para;
                    		para = v.getLayoutParams();
                    		int pxX = para.width;
                    		int pxY = para.height;
                    		Glide.with(mContext)
                    		.load(text)
                    		.override(pxX, pxY)
                    		.placeholder(R.mipmap.default_img)
                    		.diskCacheStrategy(DiskCacheStrategy.NONE)
                    		.dontAnimate()
                    		.into((ImageView) v);
                    	}
                    }
                } else if(v instanceof LinearLayout) {
                	((LinearLayout) v).removeAllViews();
                	
                	String[] imgUrlArr = text.split("\\|");
                	int screenLength = BaseTools.getWindowsWidth((Activity) mContext) - DensityUtil.dip2px(mContext, 56);
                	int length = imgUrlArr.length > 3 ? 3 : imgUrlArr.length;
                	for(int k = 0; k < length; k ++) {
                		ImageView imgView = new ImageView(mContext);
                		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenLength / 3, screenLength / 3);
                		Glide.with(mContext)
        				.load(imgUrlArr[k])
        				.placeholder(R.mipmap.default_img)
        				.diskCacheStrategy(DiskCacheStrategy.NONE)
        				.dontAnimate()
        				.into(imgView);
                		((LinearLayout) v).addView(imgView, params);
                		if(k != length - 1) {
                			View dividerView = new View(mContext);
                			LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(DensityUtil.dip2px(mContext, 4), screenLength / 3);
                			((LinearLayout) v).addView(dividerView, dividerParams);
                		}
                	}
                	
                } else {
                    throw new IllegalStateException(v.getClass().getName() + " is not a " +
                            " view that can be bounds by this SimpleAdapter");
                }
            }
        }
    }
}
