package com.bbk.adapter;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import com.bbk.Bean.SearchResultBean;
import com.bbk.activity.BidActivity;
import com.bbk.activity.CompareActivity;
import com.bbk.activity.IntentActivity;
import com.bbk.activity.R;
import com.bbk.activity.ResultDialogActivity;
import com.bbk.activity.WebViewActivity;
import com.bbk.adapter.ListViewAdapter2.ViewHolder;
import com.bbk.dialog.ResultDialog;
import com.bbk.util.DensityUtil;
import com.bbk.util.JumpIntentUtil;
import com.bbk.view.BaseViewHolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResultMyGridAdapter extends BaseAdapter{
//	private List<Map<String, Object>> list;
	private Activity context;
	ViewHolder vh;
	private List<SearchResultBean> searchResultBeans;
	public ResultMyGridAdapter( List<SearchResultBean> searchResultBeans,Activity context){
		this.searchResultBeans = searchResultBeans;
		this.context =context;
	}
	public void notifyData(List<SearchResultBean> beans){
		this.searchResultBeans.addAll(beans);
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return searchResultBeans.size();
	}

	@Override
	public Object getItem(int arg0) {
		return searchResultBeans.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
			if (convertView == null) {
				vh = new ViewHolder();
				convertView = View.inflate(context, R.layout.listview_item_result4, null);
				vh.img = (ImageView) convertView.findViewById(R.id.item_img);
				vh.title = (TextView) convertView.findViewById(R.id.item_title);
				vh.item_offer = (TextView) convertView.findViewById(R.id.item_offer);
				vh.mlittleprice = (TextView) convertView.findViewById(R.id.mlittleprice);
				vh.mbigprice = (TextView) convertView.findViewById(R.id.mbigprice);
				vh.juan = (TextView) convertView.findViewById(R.id.juan);
				vh.domainLayout = (LinearLayout) convertView.findViewById(R.id.domain_layout);
				vh.mmoredomain = (RelativeLayout) convertView.findViewById(R.id.mmoredomain);
				vh.intentbuy = (RelativeLayout) convertView.findViewById(R.id.intentbuy);
				vh.itemlayout =  convertView.findViewById(R.id.result_item);
				convertView.setTag(vh);
			}else{
				vh = (ViewHolder) convertView.getTag();
			}
			try {
				WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
				int width = wm.getDefaultDisplay().getWidth();
				LayoutParams params = vh.img.getLayoutParams();
				params.height = width/2;
				vh.img.setLayoutParams(params);
				final SearchResultBean dataSet = searchResultBeans.get(position);
//	        final String url = dataSet.get("url").toString();
				String title = dataSet.getTitle();
				String img = dataSet.getImgUrl();
				String price = dataSet.getPrice();
				String hnumber = dataSet.getComnum();
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
				if (dataSet.getTarr() != null) {
					JSONArray array = new JSONArray(dataSet.getTarr());
					addList(array);
				}
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
//				if ("1".equals(dataSet.get("isxianshi"))) {
					final String groupRowKey = dataSet.getGroupRowkey();
					vh.mmoredomain.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Intent intent = new Intent(context, ResultDialogActivity.class);
//						intent.putExtra("tarr",dataSet.get("tarr").toString() );
							intent.putExtra("keyword",dataSet.getKeyword());
							intent.putExtra("rowkey",groupRowKey );
							context.startActivity(intent);
						}
					});
					vh.intentbuy.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
//						Intent intent = new Intent(context, CompareActivity.class);
//						intent.putExtra("rowkey",groupRowKey );
							//二级页面去发标
							Intent intent = new Intent(context, BidActivity.class);
							intent.putExtra("rowkey",groupRowKey);
							intent.putExtra("type","1");
							context.startActivity(intent);
						}
					});

//			}else{
//					vh.mmoredomain.setVisibility(View.GONE);
//					vh.intentbuy.setVisibility(View.GONE);
//				}
				if (dataSet.getYjson() != null){
					if ("-1".equals(dataSet.getSaleinfo())){
						vh.juan.setVisibility(View.GONE);
					}else {
						vh.juan.setVisibility(View.VISIBLE);
						vh.juan.setText("劵");
					}
				}else {
					try {
						if (dataSet.getYjson() != null) {
							vh.juan.setVisibility(View.VISIBLE);
							String yjson = dataSet.getYjson();
							final JSONObject object = new JSONObject(yjson);
							vh.juan.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									new ResultDialog(context).buildDiloag(object, v, context);
								}
							});
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				Glide.with(context)
						.load(img)
						.placeholder(R.mipmap.zw_img_300)
						.priority(Priority.HIGH)
						.into(vh.img);
				vh.itemlayout.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						notifyDataSetChanged();
						Intent intent;
						if (JumpIntentUtil.isJump4(searchResultBeans, position)) {
							intent = new Intent(context, IntentActivity.class);
							if (searchResultBeans.get(position).getAndroidurl() != null) {
								intent.putExtra("url", searchResultBeans.get(position).getUrl());
							} else {
								intent.putExtra("url", searchResultBeans.get(position).getAndroidurl());
							}
							if (searchResultBeans.get(position).getTitle() != null) {
								intent.putExtra("title", searchResultBeans.get(position).getTitle());
							}
							if (searchResultBeans.get(position).getDomain() != null) {
								intent.putExtra("domain", searchResultBeans.get(position).getDomain());
							}
							if (searchResultBeans.get(position).getGroupRowkey() != null) {
								intent.putExtra("groupRowKey", searchResultBeans.get(position).getGroupRowkey());
							}
						} else {
							intent = new Intent(context, WebViewActivity.class);
							if (searchResultBeans.get(position).getAndroidurl() != null) {
								intent.putExtra("url", searchResultBeans.get(position).getUrl());
							} else {
								intent.putExtra("url", searchResultBeans.get(position).getUrl());
							}
							if (searchResultBeans.get(position).getGroupRowkey() != null) {
								intent.putExtra("groupRowKey", searchResultBeans.get(position).getGroupRowkey());
							}
						}
						context.startActivity(intent);
					}
			});
			}catch (Exception e){
				e.printStackTrace();
			}
		return convertView;
	}
	class ViewHolder {
		ImageView img;
		TextView title,item_offer,mbigprice,mlittleprice,juan;
		LinearLayout domainLayout;
		RelativeLayout intentbuy,mmoredomain;
		RelativeLayout itemlayout;
	}
	public void addList(JSONArray array) throws JSONException {
		for (int i = 0; i < array.length(); i++) {
			JSONObject object = array.getJSONObject(i);
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
