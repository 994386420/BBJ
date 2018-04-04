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
import android.widget.TextView;

public class BrowseWenzhangAdapter extends BaseAdapter{
	private List<Map<String, String>> list;
	private Context context;
	public static int mPosition;
	
	public BrowseWenzhangAdapter(List<Map<String, String>> list,Context context){
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
			vh.mlike = (TextView) convertView.findViewById(R.id.mlike);
			vh.mcomment = (TextView) convertView.findViewById(R.id.mcomment);
			vh.mauthor = (TextView) convertView.findViewById(R.id.mauthor);
			vh.mtitle = (TextView) convertView.findViewById(R.id.mtitle);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		Map<String, String> map = list.get(position);
		String author = map.get("author");
		String title = map.get("title");
		String img = map.get("img");
		String zan = map.get("zan");
		String count = map.get("count");
		vh.mlike.setText(zan);
		vh.mauthor.setText(author);
		vh.mcomment.setText(count);
		vh.mtitle.setText(title);
		if (!img.equals("1")) {
			Glide.with(context).load(img).placeholder(R.mipmap.fx_img)
			.diskCacheStrategy(DiskCacheStrategy.RESULT).into(vh.mimg);
		}
		return convertView;
	}
	class ViewHolder {
		TextView mlike,mcomment,mauthor,mtitle;
		ImageView mimg;
	}
}
