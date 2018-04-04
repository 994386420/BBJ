package com.bbk.adapter;

import java.util.List;
import java.util.Map;

import com.bbk.activity.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ResultDialogAdapter1 extends BaseAdapter{
	private List<Map<String, Object>> list;
	private Context context;
	public ResultDialogAdapter1(List<Map<String, Object>> list,Context context){
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
			convertView = View.inflate(context, R.layout.result_dilog1, null);
			vh.mimg = (ImageView) convertView.findViewById(R.id.mimg);
			vh.mdomain = (TextView) convertView.findViewById(R.id.mdomain);
			vh.mlittleprice = (TextView) convertView.findViewById(R.id.mlittleprice);
			vh.mbigprice = (TextView) convertView.findViewById(R.id.mbigprice);
			vh.intentbuy = (RelativeLayout) convertView.findViewById(R.id.intentbuy);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}
		Map<String, Object> map = list.get(position);
		String domain = map.get("domain").toString();
		int drawS = context.getResources().getIdentifier(domain,"mipmap", context.getPackageName());
		vh.mimg.setBackgroundResource(drawS);
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
        vh.mbigprice.setText(bigprice);
        vh.mlittleprice.setText(littleprice);
		String domainCh = map.get("domainCh").toString();
		vh.mdomain.setText(domainCh);

        
		return convertView;
	}
	class ViewHolder {
		ImageView mimg;
		TextView mdomain,mbigprice,mlittleprice;
		RelativeLayout intentbuy;
	}
}
