package com.bbk.adapter;

import java.util.List;
import java.util.Map;

import com.bbk.activity.R;
import com.bbk.adapter.FindListAdapter.ViewHolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BrowseDomainAdapter extends BaseAdapter{
	private List<Map<String, String>> list;
	private Context context;
	
	public BrowseDomainAdapter(List<Map<String, String>> list,Context context){
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
			convertView = View.inflate(context, R.layout.collection_domain_listview, null);
			vh.mimg = (ImageView) convertView.findViewById(R.id.mimg);
			vh.mcomment = (TextView) convertView.findViewById(R.id.mcomment);
			vh.mtitle = (TextView) convertView.findViewById(R.id.mtitle);
			vh.mprice = (TextView) convertView.findViewById(R.id.mprice);
			vh.intentbuy = (RelativeLayout) convertView.findViewById(R.id.intentbuy);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		Map<String, String> map = list.get(position);
		String url = map.get("url");
		String title = map.get("title");
		if (map.get("price")!=null&&map.get("price")!="") {
			String price = map.get("price");
			String imgurl = map.get("imgurl");
			String sales = map.get("sales");
			vh.mprice.setVisibility(View.VISIBLE);
			vh.intentbuy.setVisibility(View.GONE);
			vh.mtitle.setText(title);
			vh.mcomment.setText("全网总评"+sales);
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
			vh.mprice.setText(Html.fromHtml(("￥" + "</big></big></big>"+bigprice+"</big></big></big>" +littleprice)));
			Glide.with(context).load(imgurl).placeholder(R.mipmap.default_img).diskCacheStrategy(DiskCacheStrategy.RESULT).into(vh.mimg);
		}else{
			vh.mtitle.setText(title);
			vh.mcomment.setText("尚未录取商品信息，小鲸尽快完善");
			Glide.with(context).load(R.mipmap.zw_img_300).into(vh.mimg);
			vh.mprice.setVisibility(View.GONE);
			vh.intentbuy.setVisibility(View.VISIBLE);
		}

		
		
		
		return convertView;
	}
	class ViewHolder {
		TextView mprice,mcomment,mtitle;
		ImageView mimg;
		RelativeLayout intentbuy;
	}
}
