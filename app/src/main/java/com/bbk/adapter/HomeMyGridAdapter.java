package com.bbk.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bbk.activity.CompareActivity;
import com.bbk.activity.R;
import com.bbk.activity.ResultDialogActivity;
import com.bbk.dialog.ResultDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

public class HomeMyGridAdapter extends BaseAdapter{
	private List<Map<String, Object>> list;
	private Activity context;
	public HomeMyGridAdapter(List<Map<String, Object>> list, Activity context){
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
		final ViewHolder vh;		
			if (convertView == null) {
				vh = new ViewHolder();
				convertView = View.inflate(context, R.layout.listview_item_result4, null);
				vh.img = (ImageView) convertView.findViewById(R.id.item_img);
				vh.title = (TextView) convertView.findViewById(R.id.item_title);
				vh.item_offer = (TextView) convertView.findViewById(R.id.item_offer);
				vh.mlittleprice = (TextView) convertView.findViewById(R.id.mlittleprice);
				vh.mbigprice = (TextView) convertView.findViewById(R.id.mbigprice);
				vh.domainLayout = (LinearLayout) convertView.findViewById(R.id.domain_layout);
				vh.mmoredomain = (RelativeLayout) convertView.findViewById(R.id.mmoredomain);
				vh.intentbuy = (RelativeLayout) convertView.findViewById(R.id.intentbuy);
				convertView.setTag(vh);
			}else{
				vh = (ViewHolder) convertView.getTag();
			}
			WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		    int width = wm.getDefaultDisplay().getWidth();
		    LayoutParams params = vh.img.getLayoutParams();
		    params.height = width/2;
		    vh.img.setLayoutParams(params);
			final Map<String, Object> dataSet = list.get(position);	        
//	        final String url = dataSet.get("url").toString();
			String title = dataSet.get("title").toString();
	        String img = dataSet.get("img").toString();
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
					vh.item_offer.setText("全网总评"+num+"亿条  ");
				}else{
					DecimalFormat df = new DecimalFormat("###.0");  
					String num = df.format(Double.valueOf(hnumber)/10000);
					vh.item_offer.setText("全网总评"+num+"万条  ");
				}
			}else{
				vh.item_offer.setText("全网总评"+hnumber+"条  ");
			}
	        if ("1".equals(dataSet.get("isxianshi"))) {
	        	vh.mmoredomain.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(context, ResultDialogActivity.class);
						intent.putExtra("tarr",dataSet.get("tarr").toString() );
						intent.putExtra("keyword",dataSet.get("keyword").toString() );
						context.startActivity(intent);
					}
				});
		        final String groupRowKey = dataSet.get("groupRowKey").toString();
				vh.intentbuy.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(context, CompareActivity.class);
						intent.putExtra("rowkey",groupRowKey );
						context.startActivity(intent);
					}
				});
	        }else{
	        	vh.mmoredomain.setVisibility(View.GONE);
	        	vh.intentbuy.setVisibility(View.GONE);
	        }
	        Glide.with(context)
			.load(img)
			.placeholder(R.mipmap.zw_img_300)
			.priority(Priority.HIGH)
			.into(vh.img);
		return convertView;
	}
	class ViewHolder {
		ImageView img;
		TextView title,item_offer,mbigprice,mlittleprice;
		LinearLayout domainLayout;
		RelativeLayout intentbuy,mmoredomain;
	}
}
