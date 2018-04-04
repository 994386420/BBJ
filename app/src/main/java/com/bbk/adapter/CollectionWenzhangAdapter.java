package com.bbk.adapter;

import java.util.List;
import java.util.Map;

import com.bbk.activity.R;
import com.bbk.adapter.FindListAdapter.ViewHolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CollectionWenzhangAdapter extends BaseAdapter{
	private List<Map<String, String>> list;
	private Context context;
	public static int mPosition;
	
	public CollectionWenzhangAdapter(List<Map<String, String>> list,Context context){
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
			convertView = View.inflate(context, R.layout.collection_wenzhang_listview, null);
			vh.mimg = (ImageView) convertView.findViewById(R.id.mimg);
			vh.mimg1 = (ImageView) convertView.findViewById(R.id.mimg1);
			vh.mimg2 = (ImageView) convertView.findViewById(R.id.mimg2);
			vh.mlike = (TextView) convertView.findViewById(R.id.mlike);
			vh.mcomment = (TextView) convertView.findViewById(R.id.mcomment);
			vh.mauthor = (TextView) convertView.findViewById(R.id.mauthor);
			vh.mtitle = (TextView) convertView.findViewById(R.id.mtitle);
			vh.mselect = (RelativeLayout) convertView.findViewById(R.id.mselect);
			vh.mselectimg = (ImageView) convertView.findViewById(R.id.mselectimg);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		vh.mimg1.setVisibility(View.GONE);
		vh.mimg2.setVisibility(View.GONE);
		vh.mcomment.setVisibility(View.GONE);
		vh.mlike.setVisibility(View.GONE);
		Map<String, String> map = list.get(position);
		
		String isselect = map.get("isselect");
		String isbianji = map.get("isbianji");
		//1为选中，0为未选中
		if (isselect.equals("1")) {
			vh.mselectimg.setImageResource(R.mipmap.xuanzhongyuan);
		}else{
			vh.mselectimg.setImageResource(R.mipmap.weixuanzhongyuan);
		}
		//1为编辑状态，0为非编辑状态
		if (isbianji.equals("1")) {
			vh.mselect.setVisibility(View.VISIBLE);
		}else{
			vh.mselect.setVisibility(View.GONE);
		}
		
		String author = map.get("author");
		String title = map.get("title");
		String img = map.get("img");
//		String zan = map.get("zan");
//		String count = map.get("count");
//		vh.mlike.setText(zan);
//		vh.mcomment.setText(count);
		vh.mauthor.setText(author);
		vh.mtitle.setText(title);
		if (!img.equals("1")) {
			Glide.with(context).load(img).placeholder(R.mipmap.fx_img)
			.diskCacheStrategy(DiskCacheStrategy.RESULT).into(vh.mimg);
		}
		return convertView;
	}
	class ViewHolder {
		TextView mlike,mcomment,mauthor,mtitle;
		ImageView mimg,mimg1,mimg2,mselectimg;
		RelativeLayout mselect;
	}
}
