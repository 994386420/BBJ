package com.bbk.adapter;

import java.util.List;
import java.util.Map;

import com.bbk.Bean.BiaoLiaoBean;
import com.bbk.Bean.NewFxBean;
import com.bbk.activity.R;
import com.bbk.activity.SortActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FindListAdapter extends BaseAdapter{
	private List<NewFxBean> fxBeans;
	private Context context;
	public static int mPosition;
	
	public FindListAdapter(List<NewFxBean> fxBeans, Context context){
		this.fxBeans = fxBeans;
		this.context =context;
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
	public View getView(int position, View convertView, ViewGroup arg2) {
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
		return convertView;
	}
	class ViewHolder {
		TextView mlike,mcomment,mauthor,mtitle;
		ImageView mimg,img1;
	}

}
