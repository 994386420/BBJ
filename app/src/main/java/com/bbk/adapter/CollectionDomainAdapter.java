package com.bbk.adapter;

import java.util.List;
import java.util.Map;

import com.bbk.activity.CompareActivity;
import com.bbk.activity.R;
import com.bbk.adapter.FindListAdapter.ViewHolder;
import com.bbk.adapter.MesageCListReceiveAdapter.MyClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CollectionDomainAdapter extends BaseAdapter {
	private List<Map<String, String>> list;
	private Context context;

	// private MyCollectionDomainClickListener mListener;
	// private int position1;
	//
	// //写一个设置接口监听的方法
	// public void setOnClickListener(MyCollectionDomainClickListener listener)
	// {
	// mListener = listener;
	// }
	public CollectionDomainAdapter(List<Map<String, String>> list, Context context) {
		this.list = list;
		this.context = context;
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
			vh.mselectimg = (ImageView) convertView.findViewById(R.id.mselectimg);
			vh.mcomment = (TextView) convertView.findViewById(R.id.mcomment);
			vh.mtitle = (TextView) convertView.findViewById(R.id.mtitle);
			vh.mprice = (TextView) convertView.findViewById(R.id.mprice);
			vh.intentbuy = (RelativeLayout) convertView.findViewById(R.id.intentbuy);
			vh.msimilar = (RelativeLayout) convertView.findViewById(R.id.msimilar);
			vh.mselect = (RelativeLayout) convertView.findViewById(R.id.mselect);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		Map<String, String> map = list.get(position);
		String url = map.get("url");
		String title = map.get("title");
		// position1 = position;
		// vh.mselect.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// mListener.onClick(position1);
		// }
		// });
		String isselect = map.get("isselect");
		String isbianji = map.get("isbianji");
		// 1为选中，0为未选中
		// 1为选中，0为未选中
		if (isselect.equals("1")) {
			vh.mselectimg.setImageResource(R.mipmap.xuanzhongyuan);
		} else {
			vh.mselectimg.setImageResource(R.mipmap.weixuanzhongyuan);
		}
		// 1为编辑状态，0为非编辑状态
		if (isbianji.equals("1")) {
			vh.mselect.setVisibility(View.VISIBLE);
		} else {
			vh.mselect.setVisibility(View.GONE);
		}

		if (map.get("price") != null && map.get("price") != "") {
			String price = map.get("price");
			String imgurl = map.get("imgurl");
			String sales = map.get("sales");
			final String rowkey = map.get("rowkey");

			vh.mprice.setVisibility(View.VISIBLE);
			vh.msimilar.setVisibility(View.VISIBLE);
			vh.intentbuy.setVisibility(View.GONE);
			vh.mtitle.setText(title);
			vh.mcomment.setText("全网总评" + sales);
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
			vh.msimilar.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(context, CompareActivity.class);
					intent.putExtra("rowkey",rowkey);
					context.startActivity(intent);
				}
			});
			Glide.with(context).load(imgurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(vh.mimg);
		} else {
			vh.mtitle.setText(title);
			vh.mcomment.setText("尚未录取商品信息，小鲸尽快完善");
			Glide.with(context).load(R.mipmap.zw_img_300).into(vh.mimg);
			vh.mprice.setVisibility(View.GONE);
			vh.msimilar.setVisibility(View.GONE);
			vh.intentbuy.setVisibility(View.VISIBLE);
			
		}

		return convertView;
	}

	class ViewHolder {
		TextView mprice, mcomment, mtitle;
		ImageView mimg, mselectimg;
		RelativeLayout  intentbuy, msimilar, mselect;
	}
	// //自定义接口，用于回调按钮点击事件到Activity
	// public interface MyCollectionDomainClickListener{
	// public void onClick(int position);
	// }
}
