package com.bbk.adapter;

import java.util.List;
import java.util.Map;

import com.bbk.activity.R;
import com.bbk.util.BaseTools;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeGuessLikeAadapter extends BaseAdapter{
	private List<Map<String, String>> list;
	private Activity context;
	public HomeGuessLikeAadapter(List<Map<String, String>> list,Activity context){
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
			convertView = View.inflate(context, R.layout.home_guess_like, null);
			vh.mimg = (ImageView) convertView.findViewById(R.id.mimg);
			vh.title = (TextView) convertView.findViewById(R.id.title);
			vh.mbigprice = (TextView) convertView.findViewById(R.id.mbigprice);
			vh.mlittleprice = (TextView) convertView.findViewById(R.id.mlittleprice);
			vh.msale = (TextView) convertView.findViewById(R.id.msale);
			vh.mdate = (TextView) convertView.findViewById(R.id.mdate);
			vh.mcoupon = (TextView) convertView.findViewById(R.id.mcoupon);
			
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}
	
		final Map<String, String> map = list.get(position);		
        String title = map.get("title");
        String price = map.get("price");
        String img = map.get("img");
        String youhui = map.get("youhui");
        String st = map.get("st");
        String et = map.get("et");
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
        
        vh.title.setText(title);
        vh.mbigprice.setText(bigprice);
        vh.mlittleprice.setText(littleprice);
        vh.msale.setText("月销"+sale);
        vh.mdate.setText("( "+st+"~"+et+" )");
        vh.mcoupon.setText(youhui);
        WindowManager wm = context.getWindowManager();
        @SuppressWarnings("deprecation")
		int width = wm.getDefaultDisplay().getWidth();
        LayoutParams params = vh.mimg.getLayoutParams();
        params.height = (width-BaseTools.getPixelsFromDp(context, 4))/2;
        vh.mimg.setLayoutParams(params);
        Glide.with(context)
		.load(img)
		.into(vh.mimg);
        
		return convertView;
	}
	class ViewHolder {
		ImageView mimg;
		TextView title,mbigprice,mlittleprice,msale,mdate,mcoupon;

	}
}
