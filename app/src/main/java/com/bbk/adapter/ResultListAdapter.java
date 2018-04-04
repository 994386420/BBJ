package com.bbk.adapter;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import com.bbk.activity.CompareActivity;
import com.bbk.activity.R;
import com.bbk.util.DensityUtil;
import com.bumptech.glide.Glide;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ResultListAdapter extends BaseAdapter{
	private List<Map<String, Object>> list;
	private Activity context;
	public ResultListAdapter(List<Map<String, Object>> list,Activity context){
		this.list = list;
		this.context =context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder vh;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = View.inflate(context, R.layout.listview_item_result2, null);
			vh.img = (ImageView) convertView.findViewById(R.id.item_img);
			vh.title = (TextView) convertView.findViewById(R.id.item_title);
			vh.item_offer = (TextView) convertView.findViewById(R.id.item_offer);
			vh.mlittleprice = (TextView) convertView.findViewById(R.id.mlittleprice);
			vh.mbigprice = (TextView) convertView.findViewById(R.id.mbigprice);
			vh.domainLayout = (LinearLayout) convertView.findViewById(R.id.domain_layout);
			vh.mcompare = (LinearLayout) convertView.findViewById(R.id.mcompare);
			vh.intentbuy1 = (RelativeLayout) convertView.findViewById(R.id.intentbuy1);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}
		
		if (vh.domainLayout!=null) {
			vh.domainLayout.removeAllViews();
		}
		Map<String, Object> dataSet = list.get(position);
		String hassimi = dataSet.get("hassimi").toString();
		
		final String rowkey = dataSet.get("groupRowKey").toString();
		if (hassimi.equals("1")) {
			vh.mcompare.setVisibility(View.VISIBLE);
			vh.intentbuy1.setVisibility(View.GONE);
			vh.mcompare.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(context, CompareActivity.class);
					intent.putExtra("rowkey", rowkey);
					context.startActivity(intent);
				}
			});
		}else{
			if (!dataSet.get("url").toString().equals("1")) {
				vh.mcompare.setVisibility(View.GONE);
				vh.intentbuy1.setVisibility(View.VISIBLE);
				final String url = dataSet.get("url").toString();
				final String title = dataSet.get("title").toString();
				final String domain1 = dataSet.get("domain1").toString();
//				vh.intentbuy1.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View arg0) {
//						Intent intent = new Intent(context,IntentActivity.class);
//						intent.putExtra("url", url);
//						intent.putExtra("title", title);
//						intent.putExtra("domain", domain1);
//						context.startActivity(intent);
//					}
//				});
			}
			
		}
		//Log.e("====",position+"   "+dataSet);
        Object allDomain = dataSet.get("allDomain");
        if(allDomain != null){
        	String[] domains = dataSet.get("allDomain").toString().split(" ");
            int maxLength = 5;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(DensityUtil.dip2px(context, 15), DensityUtil.dip2px(context, 15));
            for (int i=0;i<domains.length && i<maxLength;i++) {
    			ImageView imageView = new ImageView(context);
    			layoutParams.setMargins(0,0,DensityUtil.dip2px(context, 4),0);
    			imageView.setLayoutParams(layoutParams);
    	        int drawS = context.getResources().getIdentifier(domains[i],"domain", context.getPackageName());
    	        imageView.setBackgroundResource(drawS);
    	        vh.domainLayout.addView(imageView);
    		}
            if(domains.length > maxLength){
            	ImageView imageView = new ImageView(context);
            	imageView.setLayoutParams(layoutParams);
    	        imageView.setImageResource(R.mipmap.domain_more);
    	        vh.domainLayout.addView(imageView);
            }
        }
        String title = dataSet.get("title").toString();
        String img = dataSet.get("img").toString();
        String domainCount = dataSet.get("domainCount").toString();
        String quote = dataSet.get("quote").toString();
        String price = dataSet.get("price").toString();
        String hnumber = dataSet.get("hnumber").toString();
        vh.title.setText(title);
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
        vh.mbigprice.setText(bigprice);
        vh.mlittleprice.setText(littleprice);
        if (Integer.valueOf(hnumber)>10000) {
			if (Integer.valueOf(hnumber)>100000000) {
				DecimalFormat df = new DecimalFormat("###.0");  
				String num = df.format(Double.valueOf(hnumber)/100000000);
				vh.item_offer.setText("全网总评"+num+"亿条  "+quote+"条报价");
			}else{
				DecimalFormat df = new DecimalFormat("###.0");  
				String num = df.format(Double.valueOf(hnumber)/10000);
				vh.item_offer.setText("全网总评"+num+"万条  "+quote+"条报价");
			}
		}else{
			vh.item_offer.setText("全网总评"+hnumber+"条  "+quote+"条报价");
		}
        Glide.with(context)
		.load(img)
		.into(vh.img);
        
		return convertView;
	}
	class ViewHolder {
		ImageView img;
		TextView title,item_offer,mbigprice,mlittleprice;
		LinearLayout domainLayout,mcompare;
		RelativeLayout intentbuy1;
	}
}
