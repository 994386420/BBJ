package com.bbk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bbk.Bean.TypesChooseBean;
import com.bbk.activity.R;

import java.util.List;

public class SizeChooseAdapter extends BaseAdapter {

	private Context mContext;
	private List<String> titlelist;
	private List<String> listSize;
	private int clickTemp = -1;

	public SizeChooseAdapter(Context context,
                             List<String> titlelist,List<String> listSize) {
		this.titlelist = titlelist;
		this.mContext = context;
		this.listSize = listSize;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (titlelist != null && titlelist.size() > 0) {
			return titlelist.size();
		}else {
			return 0;
		}
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	// 标识选择的Item
	public void setSeclection(int position) {
		clickTemp = position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		ViewHolder mHolder = null;
		if (convertView == null) {
			convertView = inflater
					.inflate(R.layout.gridview_item, null);
			mHolder = new ViewHolder();
			mHolder.mTextView = (TextView) convertView
					.findViewById(R.id.mall_detail_item_text);

			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		if (clickTemp == position) {
			mHolder.mTextView
					.setBackgroundResource(R.drawable.bg_type);
//			mHolder.mTextView.setTextColor(mContext.getResources().getColor(
//					R.color.white));
			mHolder.mTextView.setTextColor(mContext.getResources().getColor(R.color.tuiguang_color5));
		} else {
			mHolder.mTextView
					.setBackgroundResource(R.drawable.bg_type1);
//			mHolder.mTextView.setTextColor(mContext.getResources().getColor(
//					R.color.back));
			mHolder.mTextView.setTextColor(mContext.getResources().getColor(R.color.tuiguang_color4));
		}
//		if (!Constants.isNull(mList.get(position).get("color"))
//				&& !Constants.isNull(mList.get(position).get("chima"))) {
//		if (position == 0){
//			mHolder.mTextView.setText("超值购");
//		}else {
			mHolder.mTextView.setText(titlelist.get(position)+"");
//		}
//		}
//		for (int i = 0;i<listSize.size();i++) {
//			try {
//				if (listSize.get(i).toString().equals(titlelist.get(i).toString())) {
//					mHolder.mTextView
//							.setBackgroundResource(R.drawable.bg_type);
//					mHolder.mTextView.setTextColor(mContext.getResources().getColor(R.color.tuiguang_color5));
//				} else {
//					mHolder.mTextView
//							.setBackgroundResource(R.drawable.bg_type1);
//					mHolder.mTextView.setTextColor(mContext.getResources().getColor(R.color.tuiguang_color4));
//				}
//			}catch (Exception e){
//				e.printStackTrace();
//			}
//		}
		return convertView;
	}

	class ViewHolder {
		private TextView mTextView;
	}

}
