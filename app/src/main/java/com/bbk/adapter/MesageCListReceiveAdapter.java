package com.bbk.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bbk.Bean.ReceiceMsgBean;
import com.bbk.activity.MesageCenterActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.WebViewWZActivity;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.resource.Constants;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bumptech.glide.Glide;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

public class MesageCListReceiveAdapter extends BaseAdapter {
	private List<ReceiceMsgBean> receiceMsgBeans;
	private Context context;
	private MyClickListener mListener;
	private int position1;
	private String wztitle = "";

    //写一个设置接口监听的方法
    public void setOnClickListener(MyClickListener listener) {
        mListener = listener;
    }
	public MesageCListReceiveAdapter(List<ReceiceMsgBean> list, Context context) {
		this.receiceMsgBeans = list;
		this.context = context;
	}
	public void notifyData(List<ReceiceMsgBean> beans){
		this.receiceMsgBeans.addAll(beans);
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return receiceMsgBeans.size();
	}

	@Override
	public Object getItem(int position) {
		return receiceMsgBeans.get(position);
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
			convertView = View.inflate(context, R.layout.mesage_listview_receive, null);
			vh.mimg = (ImageView) convertView.findViewById(R.id.mimg);
			vh.mtime = (TextView) convertView.findViewById(R.id.mtime);
			vh.mcontent = (TextView) convertView.findViewById(R.id.mcontent);
			vh.mtitle = (TextView) convertView.findViewById(R.id.mtitle);
			vh.mname = (TextView) convertView.findViewById(R.id.mname);
			vh.mreply = (TextView) convertView.findViewById(R.id.mreply);
			vh.itemlayout =  convertView.findViewById(R.id.result_item);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		final ReceiceMsgBean receiceMsgBean = receiceMsgBeans.get(position);
		vh.mname.setText(receiceMsgBean.getNickname());
		vh.mtime.setText(receiceMsgBean.getDt());
		vh.mtitle.setText(receiceMsgBean.getTitle());
		vh.mcontent.setText(receiceMsgBean.getContent());
		position1 = position;
		vh.mreply.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mListener.onClick(position1,receiceMsgBean.getWenzhangid(),receiceMsgBean.getPlid(),receiceMsgBean.getUid());
			}
		});
		Glide.with(context).load(receiceMsgBean.getImgurl()).into(vh.mimg);
		vh.itemlayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				wztitle = receiceMsgBean.getTitle();
				String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor",
						"userID");
				String token = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor",
						"token");
				if (receiceMsgBean.getWenzhangid().contains("B")){
					String wenzhangid = receiceMsgBean.getWenzhangid();
					String blid = wenzhangid.substring(1, wenzhangid.length());
					String url = Constants.MAIN_BASE_URL_MOBILE +"mobile/blbar?blid="+blid+"&userid="+userID;
					Intent intent = new Intent(context, WebViewWZActivity.class);
					intent.putExtra("url",url);
					intent.putExtra("title",wztitle);
					context.startActivity(intent);
				}else {
					insertWenzhangGuanzhu(userID,receiceMsgBean.getWenzhangid(),token,wztitle);
				}
			}
		});
		return convertView;
	}

	class ViewHolder {
		TextView mtime, mcontent, mtitle, mreply, mname;
		ImageView mimg;
		LinearLayout itemlayout;
	}
	  //自定义接口，用于回调按钮点击事件到Activity
	  public interface MyClickListener{
	      public void onClick(int position,String weid,String plid,String uid);
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
						DialogSingleUtil.dismiss(0);
					}

					@Override
					protected void showDialog() {
						DialogSingleUtil.show(context);
					}
					@Override
					public void onError(ExceptionHandle.ResponeThrowable e) {
						DialogSingleUtil.dismiss(0);
						StringUtil.showToast(context, e.message);
					}
				});
	}
}