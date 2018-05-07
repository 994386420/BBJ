package com.bbk.adapter;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bbk.activity.BidActivity;
import com.bbk.activity.BidFbActivity;
import com.bbk.activity.CompareActivity;
import com.bbk.activity.R;
import com.bbk.activity.ResultDialogActivity;
import com.bbk.activity.WebViewActivity;
import com.bbk.dialog.ResultDialog;
import com.bbk.view.MyListView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResultMyListAdapter extends BaseAdapter{
	private List<Map<String, Object>> list;
	private List<Map<String, String>> list1;
	private Activity context;
	ViewHolder vh;
	public ResultMyListAdapter(List<Map<String, Object>> list,Activity context){
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
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = View.inflate(context, R.layout.listview_result, null);
			vh.img = (ImageView) convertView.findViewById(R.id.item_img);
			vh.mcouponimg = (ImageView) convertView.findViewById(R.id.mcouponimg);
			vh.title = (TextView) convertView.findViewById(R.id.item_title);
			vh.mlingjuan = (TextView) convertView.findViewById(R.id.mlingjuan);
			vh.item_offer = (TextView) convertView.findViewById(R.id.item_offer);
			vh.mcoupontext = (TextView) convertView.findViewById(R.id.mcoupontext);
			vh.mlittleprice = (TextView) convertView.findViewById(R.id.mlittleprice);
			vh.mbigprice = (TextView) convertView.findViewById(R.id.mbigprice);
			vh.myouhuitext = (TextView) convertView.findViewById(R.id.myouhuitext);
			vh.domainLayout = (LinearLayout) convertView.findViewById(R.id.domain_layout);
			vh.mcoupon = (LinearLayout) convertView.findViewById(R.id.mcoupon);
			vh.findsimilar = (RelativeLayout) convertView.findViewById(R.id.findsimilar);
			vh.intentbuy = (RelativeLayout) convertView.findViewById(R.id.intentbuy);
			vh.mfengexian = convertView.findViewById(R.id.mfengexian);
			vh.lingjuanzhanwei = convertView.findViewById(R.id.lingjuanzhanwei);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}
		try {
			final Map<String, Object> dataSet = list.get(position);
			//Log.e("====",position+"   "+dataSet);
			final String title = dataSet.get("title").toString();
			String img = dataSet.get("img").toString();
//			String price = dataSet.get("price").toString();
			String hnumber = dataSet.get("hnumber").toString();
			vh.title.setText(title);
			JSONArray array = new JSONArray(dataSet.get("tarr").toString());
			addList(array);
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
				final String domain1 = dataSet.get("domain1").toString();
				final String rowkey = dataSet.get("groupRowKey").toString();
				vh.intentbuy.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(context, ResultDialogActivity.class);
//    				intent.putExtra("tarr",dataSet.get("tarr").toString() );
						intent.putExtra("keyword",dataSet.get("keyword").toString() );
						intent.putExtra("rowkey",rowkey );
						context.startActivity(intent);

					}
				});
				vh.findsimilar.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
//    				Intent intent = new Intent(context, CompareActivity.class);
						//二级页面去发标
						Intent intent = new Intent(context, BidActivity.class);
						intent.putExtra("rowkey",rowkey);
						intent.putExtra("type","1");
						context.startActivity(intent);
					}
				});
			}else{
				vh.intentbuy.setVisibility(View.GONE);
				vh.findsimilar.setVisibility(View.GONE);
			}
			if ("0".equals(dataSet.get("yjson"))){
//				vh.mcouponimg.setVisibility(View.VISIBLE);
//				vh.myouhuitext.setVisibility(View.VISIBLE);
//				vh.lingjuanzhanwei.setVisibility(View.VISIBLE);
//				Log.i("newsalefino",dataSet.get("newsaleinfo").toString()+"=====");
				//新增nessaleinfo字段，当url存在时直接跳转，不存在则隐藏跳转只显示优惠信息
				if ("-1".equals(dataSet.get("newsaleinfo"))){
					vh.mcoupon.setVisibility(View.GONE);
					vh.myouhuitext.setVisibility(View.GONE);
					vh.lingjuanzhanwei.setVisibility(View.GONE);
					vh.mcouponimg.setVisibility(View.GONE);
					vh.myouhuitext.setVisibility(View.GONE);
				}else {
					String newsaleinfo = dataSet.get("newsaleinfo").toString();
					JSONObject object = new JSONObject(newsaleinfo);
					String desc = object.optString("desc");
					final String url = object.optString("url");
					if (!"".equals(url)){
						vh.mcoupon.setVisibility(View.VISIBLE);
						vh.myouhuitext.setVisibility(View.VISIBLE);
						vh.mlingjuan.setVisibility(View.VISIBLE);
						vh.mcouponimg.setVisibility(View.VISIBLE);
						vh.lingjuanzhanwei.setVisibility(View.VISIBLE);
//						Log.i("newsalefino",desc+"====="+url);
						vh.mcoupontext.setText(desc);
						vh.mcoupon.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(context, WebViewActivity.class);
								intent.putExtra("url",url);
								context.startActivity(intent);
							}
						});
					}else {
						vh.mcoupon.setVisibility(View.VISIBLE);
						vh.myouhuitext.setVisibility(View.VISIBLE);
						vh.mcoupontext.setText(desc);
						vh.lingjuanzhanwei.setVisibility(View.GONE);
						vh.mlingjuan.setVisibility(View.GONE);
						vh.mcouponimg.setVisibility(View.GONE);
					}
				}
//				if ("-1".equals(dataSet.get("saleinfo"))){
//					vh.mcoupon.setVisibility(View.GONE);
//					vh.myouhuitext.setVisibility(View.GONE);
//					vh.lingjuanzhanwei.setVisibility(View.GONE);
//				}else {
//					if (!"".equals(dataSet.get("saleinfo"))){
//						vh.mcoupon.setVisibility(View.VISIBLE);
//						vh.mlingjuan.setVisibility(View.GONE);
//						vh.mcouponimg.setVisibility(View.GONE);
//						vh.mcoupontext.setText(dataSet.get("saleinfo").toString());
//						vh.mcoupon.setOnClickListener(new OnClickListener() {
//							@Override
//							public void onClick(View v) {
//
//							}
//						});
//					}else {
//						vh.mcoupon.setVisibility(View.GONE);
//						vh.myouhuitext.setVisibility(View.GONE);
//						vh.lingjuanzhanwei.setVisibility(View.GONE);
//					}
//
//				}

			}else {
				try {
					vh.mcouponimg.setVisibility(View.VISIBLE);
					vh.mcoupon.setVisibility(View.VISIBLE);
					vh.myouhuitext.setVisibility(View.VISIBLE);
					vh.lingjuanzhanwei.setVisibility(View.VISIBLE);
					String yjson = dataSet.get("yjson").toString();
					final JSONObject object = new JSONObject(yjson);
					String desc = object.getJSONArray("ylist").getJSONObject(0).optString("desc");
					vh.mcoupontext.setText(desc);
					vh.mcoupon.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							new ResultDialog(context).buildDiloag(object,v,context);
						}
					});
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			Glide.with(context)
					.load(img)
					.priority(Priority.HIGH)
					.placeholder(R.mipmap.zw_img_300)
					.into(vh.img);

		}catch (Exception e){
			e.printStackTrace();
		}
		return convertView;
	}
	class ViewHolder {
		ImageView img,mcouponimg;
		TextView title,item_offer,mbigprice,mlittleprice,mcoupontext,mlingjuan,myouhuitext;
		LinearLayout domainLayout,mcoupon;
		RelativeLayout intentbuy,findsimilar;
		View mfengexian,lingjuanzhanwei;

	}

	public void addList(JSONArray array) throws JSONException {
		for (int i = 0; i < array.length(); i++) {
			JSONObject object = array.getJSONObject(i);
//			Map<String,String> map = new HashMap<>();
//			map.put("price",object.optString("price"));
//			list1.add(map);
			String price = object.optString("price");
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
//			Log.i("-----",bigprice+"---------"+littleprice);
			vh.mbigprice.setText(bigprice);
			vh.mlittleprice.setText(littleprice);
		}
	}
}
