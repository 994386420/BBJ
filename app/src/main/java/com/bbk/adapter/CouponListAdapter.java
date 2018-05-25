package com.bbk.adapter;

import java.util.List;
import java.util.Map;

import com.bbk.activity.R;
import com.bbk.activity.SortActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CouponListAdapter extends BaseAdapter{
	private List<Map<String, String>> list;
	private Context context;
	public static int mPosition;
	
	public CouponListAdapter(List<Map<String, String>> list,Context context){
		this.list = list;
		this.context =context;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder vh;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = View.inflate(context, R.layout.coupon_fragment_listview, null);
			vh.mimg = (ImageView) convertView.findViewById(R.id.mimg);
			vh.mcouponnum = (TextView) convertView.findViewById(R.id.mcouponnum);
			vh.moldprice = (TextView) convertView.findViewById(R.id.moldprice);
			vh.mlittleprice = (TextView) convertView.findViewById(R.id.mlittleprice);
			vh.mbigprice = (TextView) convertView.findViewById(R.id.mbigprice);
			vh.msale = (TextView) convertView.findViewById(R.id.msale);
			vh.mtitle = (TextView) convertView.findViewById(R.id.mtitle);
			vh.mcoupon = (LinearLayout) convertView.findViewById(R.id.mcoupon);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		Map<String, String> map = list.get(position);
		String title = map.get("title");
		String price = map.get("price");
		String img = map.get("img");
		String sale = map.get("sale");
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
		vh.msale.setText("已售"+sale+"件");
		vh.mtitle.setText(title);
		if (map.get("down")!= null) {
			String down = map.get("down");
			String bprice = map.get("bprice");
			String bbprice = "￥"+bprice;
			SpannableString span = new SpannableString(bbprice);
			span.setSpan(new StrikethroughSpan(), 0, bbprice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			vh.moldprice.setText(span);
			vh.mcouponnum.setText("领"+down+"元劵");
			vh.moldprice.setVisibility(View.VISIBLE);
			vh.mcoupon.setVisibility(View.VISIBLE);
		}else{
			vh.moldprice.setVisibility(View.GONE);
			vh.mcoupon.setVisibility(View.GONE);
		}
		

		Glide.with(context).load(img)
		.placeholder(R.mipmap.zw_img_300).into(vh.mimg);
		return convertView;
	}
	class ViewHolder {
		TextView mcouponnum,moldprice,mlittleprice,mbigprice,msale,mtitle;
		ImageView mimg;
		LinearLayout mcoupon;
	}

}
