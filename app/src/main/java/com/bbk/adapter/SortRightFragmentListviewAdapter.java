package com.bbk.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bbk.activity.R;
import com.bbk.activity.ResultMainActivity;
import com.bbk.activity.SearchMainActivity;
import com.bbk.activity.SearchRecommendCzgActivity;
import com.bbk.resource.NewConstants;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.MyGridView;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SortRightFragmentListviewAdapter extends BaseAdapter{
	private List<Map<String, String>> list;
	private List<Map<String, Object>> mdata;
	private ArrayList<String> listname,listaddtion;
	private Context context;
	private SortListViewGridAdapter adapter;
	
	public SortRightFragmentListviewAdapter(List<Map<String, String>> list,Context context){
		this.list = list;
		this.context =context;
		
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		ViewHolder vh;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.rank_rightfragment_listview, null);
			vh = new ViewHolder();
			vh.mtext = (TextView) convertView.findViewById(R.id.mText);
			vh.mgridview = (MyGridView) convertView.findViewById(R.id.mGridView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		Map<String, String> map = list.get(position);
		final String tongjilist = map.get("tongjilist");
		String name = map.get("name");
		final String content = map.get("content");
		vh.mtext.setText(name);
		final JSONArray array;
		try {
			array = new JSONArray(content);
		mdata = new ArrayList<>();
		Map<String, Object> map1 = null;
		for (int i = 0; i < array.length(); i++) {
			JSONObject object = array.getJSONObject(i);
			map1 = new HashMap<>();
				String text = object.optString("name");
				map1.put("text", text);
			String imageUrl = object.optString("img");
			String price = object.optString("price");
			map1.put("imageUrl", imageUrl);
			map1.put("price", price);
			mdata.add(map1);
		}
		if (mdata!= null) {
			adapter = new SortListViewGridAdapter(context, mdata);
			vh.mgridview.setAdapter(adapter);
			vh.mgridview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position1, long arg3) {
					try {
						JSONObject object = array.getJSONObject(position1);
						String text = object.optString("name");
							Intent intent = new Intent(context, SearchMainActivity.class);
							intent.putExtra("keyword", text);
						SharedPreferencesUtil.putSharedData(context, "shaixuan", "shaixuan", "yes");
						NewConstants.clickpositionFenlei = 5200;
						NewConstants.clickpositionDianpu = 5200;
						NewConstants.clickpositionMall = 5200;
							context.startActivity(intent);
	
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}
	class ViewHolder {
		TextView mtext;
		MyGridView mgridview;
//		RelativeLayout rank_more;
	}
}
