package com.bbk.adapter;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import com.bbk.activity.R;
import com.bbk.activity.RankActivity;
import com.bbk.adapter.RankRightFragmentListviewAdapter.ViewHolder;
import com.bbk.util.BaseTools;
import com.bbk.view.MyGridView;
import com.bumptech.glide.Glide;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RankActivityListViewAdapter extends BaseAdapter{
	private List<Map<String, Object>> list;
	private Context context;
	private String type;
	public RankActivityListViewAdapter(List<Map<String, Object>> list,Context context,String type){
		this.list = list;
		this.context =context;
		this.type = type;
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
			convertView = View.inflate(context, R.layout.item_rank_product, null);
			vh = new ViewHolder();
			vh.rank_img = (ImageView) convertView.findViewById(R.id.rank_img);
			vh.item_img = (ImageView) convertView.findViewById(R.id.item_img);
			vh.item_title = (TextView) convertView.findViewById(R.id.item_title);
			vh.item_text = (TextView) convertView.findViewById(R.id.item_text);
			vh.mbigprice = (TextView) convertView.findViewById(R.id.mbigprice);
			vh.mlittleprice = (TextView) convertView.findViewById(R.id.mlittleprice);
			vh.red_first = (TextView) convertView.findViewById(R.id.red_first);
			vh.mgood = (TextView) convertView.findViewById(R.id.mgood);
			vh.rank_number_tv = (TextView) convertView.findViewById(R.id.rank_number_tv);
			vh.oldprice = (TextView) convertView.findViewById(R.id.oldprice);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		Map<String, Object> map = list.get(position);
		String title = (String) map.get("title");
		String imgUrl = (String) map.get("imgUrl");
		String hnumber = (String) map.get("hnumber");
		String oldprice = (String) map.get("oldprice");
		String domaincount =  map.get("domaincount").toString();
		String quote = map.get("quote").toString();
		String price = (String) map.get("price");
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
		vh.rank_number_tv.setText(String.valueOf(position+1));
		if(position < 3){
			vh.rank_img.setImageResource(R.mipmap.top01);
		}else{
			vh.rank_img.setImageResource(R.mipmap.top02);
		}
		vh.item_title.setText(title);
		vh.item_text.setText(domaincount+"个商城   "+quote+"家报价");
		vh.mbigprice.setText(bigprice);
		vh.mlittleprice.setText(littleprice);
		if (type.equals("2")) {
			vh.red_first.setText("七天降价");
			vh.mgood.setText(hnumber+"%");
			SpannableString spannableString = new SpannableString(oldprice);
			spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#999999")), 0, oldprice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			//4.用删除线标记文本
			spannableString.setSpan(new StrikethroughSpan(), 0, oldprice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			vh.oldprice.setVisibility(View.VISIBLE);
			vh.oldprice.setText(spannableString);
		}else if(type.equals("6")){
			vh.red_first.setText("全网销量");
			if (Integer.valueOf(hnumber)>10000) {
				if (Integer.valueOf(hnumber)>100000000) {
					DecimalFormat df = new DecimalFormat("###.0");  
					String num = df.format(Double.valueOf(hnumber)/100000000);
					vh.mgood.setText(num+"亿+");
				}else{
					DecimalFormat df = new DecimalFormat("###.0");  
					String num = df.format(Double.valueOf(hnumber)/10000);
					vh.mgood.setText(num+"万+");
				}
			}else{
				vh.mgood.setText(hnumber);
			}
		}else{
			if (Integer.valueOf(hnumber)>10000) {
				if (Integer.valueOf(hnumber)>100000000) {
					DecimalFormat df = new DecimalFormat("###.0");  
					String num = df.format(Double.valueOf(hnumber)/100000000);
					vh.mgood.setText(num+"亿+");
				}else{
					DecimalFormat df = new DecimalFormat("###.0");  
					String num = df.format(Double.valueOf(hnumber)/10000);
					vh.mgood.setText(num+"万+");
				}
			}else{
				vh.mgood.setText(hnumber);
			}
		}
		
		Glide.with(context)
		.load(imgUrl)
		.into(vh.item_img);
		return convertView;
	}
	class ViewHolder {
		ImageView rank_img,item_img;
		TextView item_title;
		TextView item_text;
		TextView mbigprice;
		TextView mlittleprice;
		TextView red_first,mgood,rank_number_tv,oldprice;

	}
}
