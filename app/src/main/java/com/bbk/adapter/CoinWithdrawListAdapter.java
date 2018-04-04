package com.bbk.adapter;

import java.util.List;
import java.util.Map;

import com.bbk.activity.R;
import com.bbk.activity.SortActivity;
import com.bbk.fragment.RankFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CoinWithdrawListAdapter extends BaseAdapter{
	private List<Map<String, String>> list;
	private Context context;
	public static int mPosition;
	
	public CoinWithdrawListAdapter(List<Map<String, String>> list,Context context){
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
			convertView = View.inflate(context, R.layout.everycoin_withdraw, null);
			vh.mtext1 = (TextView) convertView.findViewById(R.id.mtext1);
			vh.mtext2 = (TextView) convertView.findViewById(R.id.mtext2);
			vh.mtext3 = (TextView) convertView.findViewById(R.id.mtext3);

			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		Map<String, String> map = list.get(position);
		String jinbi = map.get("jinbi");
		String message = map.get("message");
		String time = map.get("time");
		String type = map.get("type");
		vh.mtext3.setText(time);
		vh.mtext2.setText(message);
		
		if (type.equals("0")) {
			vh.mtext1.setText("-"+jinbi+"鲸币");
			vh.mtext1.setTextColor(Color.parseColor("#46aa46"));
		}else{
			vh.mtext1.setText("+"+jinbi+"鲸币");
			vh.mtext1.setTextColor(Color.parseColor("#ff7d41"));
		}
		return convertView;
	}
	class ViewHolder {
		TextView mtext1,mtext2,mtext3;

	}

}

