package com.bbk.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bbk.Bean.SendMsgBean;
import com.bbk.Bean.SystemMessageBean;
import com.bbk.activity.MesageCenterActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.WebViewWZActivity;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.resource.Constants;
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
import android.widget.TextView;

import org.json.JSONObject;

public class MesageCListSendAdapter extends BaseAdapter{
	private List<SendMsgBean> sendMsgBeans;
	private Context context;
	private String wztitle = "";

	public MesageCListSendAdapter(List<SendMsgBean> list,Context context){
		this.sendMsgBeans = list;
		this.context =context;
	}
	public void notifyData(List<SendMsgBean> beans){
		this.sendMsgBeans.addAll(beans);
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return sendMsgBeans.size();
	}

	@Override
	public Object getItem(int position) {
		return sendMsgBeans.get(position);
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
			convertView = View.inflate(context, R.layout.mesage_listview_send, null);
			vh.mimg = (ImageView) convertView.findViewById(R.id.mimg);
			vh.mtime = (TextView) convertView.findViewById(R.id.mtime);
			vh.mcontent = (TextView) convertView.findViewById(R.id.mcontent);
			vh.mtitle = (TextView) convertView.findViewById(R.id.mtitle);
			vh.mtext1 = (TextView) convertView.findViewById(R.id.mtext1);
			vh.mtext2 = (TextView) convertView.findViewById(R.id.mtext2);
			vh.mname = (TextView) convertView.findViewById(R.id.mname);
			vh.itemlayout =  convertView.findViewById(R.id.result_item);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		final SendMsgBean sendMsgBean = sendMsgBeans.get(position);
		if (sendMsgBean.getNickname()== null) {
			vh.mtext1.setVisibility(View.VISIBLE);
			vh.mtext2.setVisibility(View.GONE);
			vh.mname.setVisibility(View.GONE);
		}else{
			vh.mtext1.setVisibility(View.GONE);
			vh.mtext2.setVisibility(View.VISIBLE);
			vh.mname.setVisibility(View.VISIBLE);
			vh.mname.setText(sendMsgBean.getNickname());
		}
		vh.mtime.setText(sendMsgBean.getDt());
		vh.mtitle.setText(sendMsgBean.getTitle());
		vh.mcontent.setText(sendMsgBean.getContent());
		Glide.with(context).load(sendMsgBean.getImgurl()).into(vh.mimg);
		vh.itemlayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				wztitle = sendMsgBean.getTitle();
				String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor",
						"userID");
				String token = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor",
						"token");
				String wz = sendMsgBean.getWzid();
				if (wz.contains("B")){
					String blid = wz.substring(1, wz.length());
					String url = Constants.MAIN_BASE_URL_MOBILE +"mobile/blbar?blid="+blid+"&userid="+userID;
					Intent intent = new Intent(context, WebViewWZActivity.class);
					intent.putExtra("url",url);
					intent.putExtra("title",wztitle);
					context.startActivity(intent);
				}else {
					insertWenzhangGuanzhu(userID,wz,token,wztitle);
				}
			}
		});
		return convertView;
	}
	class ViewHolder {
		TextView mtime,mcontent,mtitle,mtext1,mtext2,mname;
		ImageView mimg;
		LinearLayout itemlayout;
	}
	private void insertWenzhangGuanzhu(String userid, String wz, String token, final String wztitle) {
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("userid", userid);
		maps.put("wzid", wz);
		maps.put("token", token);
		maps.put("type", "2");
		RetrofitClient.getInstance(context).createBaseApi().insertWenzhangGuanzhu(
				maps, new BaseObserver<String>(context) {
					@Override
					public void onNext(String s) {
						Log.e("===",s);
						try {
							JSONObject jsonObject = new JSONObject(s);
							if (jsonObject.optString("status").equals("1")) {
								Intent intent = new Intent(context, WebViewWZActivity.class);
								intent.putExtra("title", wztitle);
								intent.putExtra("url", jsonObject.optString("content"));
								context.startActivity(intent);
							}
						}catch (Exception e){
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
						StringUtil.showToast(context, "网络异常");
					}
				});
	}
}
