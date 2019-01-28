package com.bbk.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bbk.Bean.SystemMessageBean;
import com.bbk.Bean.WoYaoBean;
import com.bbk.activity.BidBillDetailActivity;
import com.bbk.activity.HomeActivity;
import com.bbk.activity.MesageCenterActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.resource.NewConstants;
import com.bbk.util.EventIdIntentUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bumptech.glide.Glide;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class MesageCListSysAdapter extends BaseAdapter{
	private List<SystemMessageBean> systemMessageBeans;
	private Context context;
	
	public MesageCListSysAdapter(List<SystemMessageBean> list, Context context){
		this.systemMessageBeans = list;
		this.context =context;
	}
	public void notifyData(List<SystemMessageBean> beans){
		this.systemMessageBeans.addAll(beans);
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return systemMessageBeans.size();
	}

	@Override
	public Object getItem(int position) {
		return systemMessageBeans.get(position);
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
			convertView = View.inflate(context, R.layout.mesage_listview_system, null);
			vh.mtime = (TextView) convertView.findViewById(R.id.mtime);
			vh.mcontent = (TextView) convertView.findViewById(R.id.mcontent);
			vh.notread = (RelativeLayout) convertView.findViewById(R.id.notread);
			vh.itemlayout =  convertView.findViewById(R.id.result_item);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		final SystemMessageBean systemMessageBean = systemMessageBeans.get(position);
		String message = systemMessageBean.getMessage();
		String dtime =systemMessageBean.getDtime();
		String isread = systemMessageBean.getIsread();
		if ("1".equals(isread)) {
			vh.notread.setVisibility(View.GONE);
		}else{
			vh.notread.setVisibility(View.VISIBLE);
		}
		vh.mtime.setText(dtime);
		vh.mcontent.setText(message);
		vh.itemlayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				EventIdIntentUtil.EventIdIntent(context,systemMessageBean.getEventId(),systemMessageBean.getHtmlUrl());
//				Log.i("============>>>",systemMessageBean.getEventId());
				if (!systemMessageBean.getIsread().equals("1")) {
					systemMessageBean.setIsread("1");
					notifyDataSetChanged();
					insertMessageRead(systemMessageBean.getMid());
				}
			}
		});
		return convertView;
	}
	class ViewHolder {
		TextView mtime,mcontent;
		RelativeLayout notread;
		LinearLayout itemlayout;
	}
	private void insertMessageRead(String mid) {
		String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("userid", userID);
		maps.put("mid", mid);
		RetrofitClient.getInstance(context).createBaseApi().insertMessageRead(
				maps, new BaseObserver<String>(context) {
					@Override
					public void onNext(String s) {
						Log.e("===",s);
						try {
							JSONObject jsonObject = new JSONObject(s);
							NewConstants.messages = jsonObject.optInt("content");
							if (HomeActivity.draggableflagview != null){
								if (NewConstants.messages == 0){
									HomeActivity.draggableflagview.setVisibility(View.GONE);
								}else {
									HomeActivity.draggableflagview.setVisibility(View.VISIBLE);
									HomeActivity.draggableflagview.setText(NewConstants.messages+"");
								}
						}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					@Override
					protected void hideDialog() {
					}

					@Override
					protected void showDialog() {
					}
					@Override
					public void onError(ExceptionHandle.ResponeThrowable e) {
						StringUtil.showToast(context, e.message);
					}
				});
	}
}
