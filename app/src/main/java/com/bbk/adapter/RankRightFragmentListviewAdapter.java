package com.bbk.adapter;

import java.util.ArrayList;
import android.widget.AdapterView.OnItemClickListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bbk.activity.DetailsMainActivity22;
import com.bbk.activity.R;
import com.bbk.activity.RankActivity;
import com.bbk.activity.ResultMainActivity;
import com.bbk.view.MyGridView;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RankRightFragmentListviewAdapter extends BaseAdapter{
	private List<Map<String, String>> list;
	private List<Map<String, Object>> mdata;
	private ArrayList<String> listname,listaddtion,tlistname,tlistaddtion;
	private Context context;
	private RankListViewGridAdapter adapter;
	private String type;
	public RankRightFragmentListviewAdapter(List<Map<String, String>> list,Context context,String type){
		this.list = list;
		this.context =context;
		this.type = type;
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
			vh.rank_more = (RelativeLayout) convertView.findViewById(R.id.rank_more);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		vh.rank_more.setVisibility(View.GONE);
		Map<String, String> map = list.get(position);
		final String tongjilist = map.get("tongjilist");
		//如果tongjilist不为空,则为为你推荐，显示更多
		if (tongjilist!= "" && tongjilist!=null) {
			vh.rank_more.setVisibility(View.VISIBLE);
			vh.rank_more.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					JSONArray jsonArray;
					try {
					jsonArray = new JSONArray(tongjilist);
					tlistname =new ArrayList<String>();
					tlistaddtion =new ArrayList<String>();
					for (int i = 0; i < jsonArray.length(); i++) {

						JSONObject object2 = jsonArray.getJSONObject(i);
						String addtion = object2.optString("addtion");
						String name = object2.optString("name");
						tlistname.add(name);
						tlistaddtion.add(addtion);
					}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Intent intent = new Intent(context, RankActivity.class);
					intent.putExtra("position", "0");
					intent.putExtra("rankIndex", type);
					intent.putStringArrayListExtra("listname",tlistname);
					intent.putStringArrayListExtra("listaddtion",tlistaddtion);
					context.startActivity(intent);
				}
			});
		}
		String name = map.get("name");
		String content = map.get("content");
		vh.mtext.setText(name);
		final JSONArray array;
		try {
		array = new JSONArray(content);
		mdata = new ArrayList<>();
		Map<String, Object> map1 = null;
		for (int i = 0; i < array.length(); i++) {
			JSONObject object = array.getJSONObject(i);
			map1 = new HashMap<>();
			//type为三是品牌榜，取品牌，否则取名字
			if (type.equals("3")) {
				if (object.optString("brand")!= "") {
					String text = object.optString("brand");
					map1.put("text", text);
				}else{
					String text = object.optString("name");
					map1.put("text", text);
				}
			}else{
				String text = object.optString("name");
				map1.put("text", text);
			}
			String imageUrl = object.optString("img");
			String price = object.optString("price");
			map1.put("imageUrl", imageUrl);
			map1.put("price", price);
			mdata.add(map1);
		}
		if (mdata!= null) {
			if (type.equals("3") && tongjilist!= "") {
				adapter = new RankListViewGridAdapter(context, mdata,1);
			}else{
				adapter = new RankListViewGridAdapter(context, mdata, 0);
			}
			vh.mgridview.setAdapter(adapter);
			vh.mgridview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position1, long arg3) {
					try {
						if (type.equals("3") && tongjilist!= "") {
							Map<String, String> map = list.get(position);
							String brand = array.getJSONObject(position1).optString("brand");
							String addition = array.getJSONObject(position1).optString("addition");
							Intent intent = new Intent(context, ResultMainActivity.class);
							intent.putExtra("brand", brand);
							intent.putExtra("addition", addition);
							context.startActivity(intent);
							
						}else{
							JSONObject object = array.getJSONObject(position1);
							if (object.optString("price")!= "") {
								String groupRowkey = object.optString("rowkey");
								Intent intent1 = new Intent(context,DetailsMainActivity22.class);
								intent1.putExtra("groupRowKey", groupRowkey);
								context.startActivity(intent1);
							}else{
								listname =new ArrayList<String>();
								listaddtion =new ArrayList<String>();
								for (int i = 0; i < array.length(); i++) {
									JSONObject object2 = array.getJSONObject(i);
									String text = object2.optString("name");
									String addtion = object2.optString("addtion");
									listaddtion.add(addtion);
									listname.add(text);
								}
								Intent intent = new Intent(context, RankActivity.class);
								intent.putExtra("position", String.valueOf(position1));
								intent.putExtra("rankIndex", type);
								intent.putStringArrayListExtra("listname",listname);
								intent.putStringArrayListExtra("listaddtion",listaddtion);
								context.startActivity(intent);
							}
						}
						
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
		RelativeLayout rank_more;
	}
}
