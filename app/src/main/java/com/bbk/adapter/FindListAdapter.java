package com.bbk.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bbk.Bean.BiaoLiaoBean;
import com.bbk.Bean.NewFxBean;
import com.bbk.activity.GossipPiazzaDetailActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.SortActivity;
import com.bbk.activity.WebViewWZActivity;
import com.bbk.flow.DataFlow;
import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.util.SharedPreferencesUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

public class FindListAdapter extends BaseAdapter implements ResultEvent{
	private List<NewFxBean> fxBeans;
	private Context context;
	public static int mPosition;
	private String wztitle = "";
	private DataFlow dataFlow;
	
	public FindListAdapter(List<NewFxBean> fxBeans, Context context){
		this.fxBeans = fxBeans;
		this.context =context;
		this.dataFlow = new DataFlow(context);
	}
	public void notifyData(List<NewFxBean> fxBeans){
		if (fxBeans != null){
			this.fxBeans.addAll(fxBeans);
			notifyDataSetChanged();
		}
	}
	@Override
	public int getCount() {
		return fxBeans.size();
	}

	@Override
	public Object getItem(int position) {
		return fxBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		ViewHolder vh;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = View.inflate(context, R.layout.find_listview, null);
			vh.mimg = (ImageView) convertView.findViewById(R.id.mimg);
			vh.img1 = (ImageView) convertView.findViewById(R.id.img1);
			vh.mlike = (TextView) convertView.findViewById(R.id.mlike);
			vh.mcomment = (TextView) convertView.findViewById(R.id.mcomment);
			vh.mauthor = (TextView) convertView.findViewById(R.id.mauthor);
			vh.mtitle = (TextView) convertView.findViewById(R.id.mtitle);
			vh.itemlayout = convertView.findViewById(R.id.result_item);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
			NewFxBean fxBean = fxBeans.get(position);
			String author = fxBean.getAuthor();
			String title = fxBean.getTitle();
			String img = fxBean.getImg();
			String zan = fxBean.getZan();
			String type = fxBean.getType();
			String count = fxBean.getCount();
			vh.mlike.setText(zan);
			vh.mauthor.setText(author);
			vh.mcomment.setText(count);
			vh.mtitle.setText(title);
			WindowManager wm = ((Activity) context).getWindowManager();
			@SuppressWarnings("deprecation")
			int width = wm.getDefaultDisplay().getWidth();
			LayoutParams params = vh.mimg.getLayoutParams();
			params.height = width*500/1190;
			vh.mimg.setLayoutParams(params);
			Glide.with(context).load(img)
					.placeholder(R.mipmap.zhanwei_01).into(vh.mimg);
		    vh.itemlayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				insertWenzhangGuanzhu(position);
			}
		});
		return convertView;
	}


	class ViewHolder {
		TextView mlike,mcomment,mauthor,mtitle;
		ImageView mimg,img1;
		LinearLayout itemlayout;
	}
	private void insertWenzhangGuanzhu(int position) {
		wztitle  = fxBeans.get(position).getTitle();
		String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
		String token = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "token");
		if (!TextUtils.isEmpty(userID)) {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("userid", userID);
			params.put("wzid", fxBeans.get(position).getId());
			params.put("token", token);
			params.put("type", "2");
			dataFlow.requestData(1, "newService/insertWenzhangGuanzhu", params, this,false);
		} else {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("userid", "-1");
			params.put("token", token);
			params.put("wzid", fxBeans.get(position).getId());
			params.put("type", "2");
			dataFlow.requestData(1, "newService/insertWenzhangGuanzhu", params, this,false);
		}

	}
	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
		Intent intent = new Intent(context, WebViewWZActivity.class);
		intent.putExtra("url", content);
		intent.putExtra("title", wztitle);
		context.startActivity(intent);
	}
}
