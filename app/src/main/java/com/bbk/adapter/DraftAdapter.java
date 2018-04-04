package com.bbk.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bbk.activity.DesPictureActivity;
import com.bbk.activity.ImageLookActivity;
import com.bbk.activity.MyDraftActivity;
import com.bbk.activity.MyGossipActivity;
import com.bbk.activity.R;
import com.bbk.activity.SortActivity;
import com.bbk.fragment.RankFragment;
import com.bbk.view.ExpandableTextView;
import com.bbk.view.MyGridView;
import com.bumptech.glide.Glide;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DraftAdapter extends BaseAdapter{
	private List<Map<String, String>> list;
	private Context context;
	
	
	public DraftAdapter(List<Map<String, String>> list,Context context){
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
	public View getView(final int position, View convertView, ViewGroup arg2) {
		final ViewHolder vh;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = View.inflate(context, R.layout.draft_listview, null);
			vh.mtime = (TextView) convertView.findViewById(R.id.mtime);
			vh.mcontent = (TextView) convertView.findViewById(R.id.mcontent);
			vh.mtype = (TextView) convertView.findViewById(R.id.mtype);
			vh.mtitle = (TextView) convertView.findViewById(R.id.mtitle);
			vh.mselect = (RelativeLayout) convertView.findViewById(R.id.mselect);
			vh.mselectimg = (ImageView) convertView.findViewById(R.id.mselectimg);
			vh.mimggrid = (MyGridView) convertView.findViewById(R.id.mimggrid);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		final Map<String, String> map = list.get(position);
		final String content = map.get("content");
		String title = map.get("title");
		String dtime = map.get("dtime");
		String typeCh = map.get("typeCh");
		String imgs = map.get("imgs");
		
		vh.mtype.setText(typeCh);
		String isselect = map.get("isselect");
		final String isbianji = map.get("isbianji");
		//1为选中，0为未选中
		if (isselect.equals("1")) {
			vh.mselectimg.setImageResource(R.mipmap.xuanzhongyuan);
			vh.mimggrid.setFocusable(false);
		}else{
			vh.mselectimg.setImageResource(R.mipmap.weixuanzhongyuan);
			vh.mimggrid.setFocusable(true);
		}
		//1为编辑状态，0为非编辑状态
		if (isbianji.equals("1")) {
			vh.mselect.setVisibility(View.VISIBLE);
		}else{
			vh.mselect.setVisibility(View.GONE);
		}
		vh.mtime.setText(dtime);
		vh.mtitle.setText(title);
		vh.mcontent.setText(content);
		final List<String> imglist = new ArrayList<String>();
		try {
			JSONArray array = new JSONArray(imgs);
			for (int i = 0; i < array.length(); i++) {
				String img = array.getString(i);
				imglist.add(img);
			}
			List<String> videoimg = new ArrayList<String>();
			if (!"-1".equals(map.get("vidioimg"))){
				videoimg.add(map.get("vidioimg"));
			}
			recyGrid(vh,imglist,videoimg);
			vh.mimggrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent Intent = new Intent(context, DesPictureActivity.class);
					Intent.putStringArrayListExtra("list", (ArrayList<String>) imglist);
					Intent.putExtra("position",position);
					context.startActivity(Intent);

				}
			});
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if ("0".equals(isbianji)) {
						Intent intent;
						intent = new Intent(context, MyGossipActivity.class);
						intent.putExtra("content", map.get("content"));
						intent.putExtra("title", map.get("title"));
						intent.putExtra("dtime", map.get("dtime"));
						intent.putExtra("imgs", map.get("imgs"));
						intent.putExtra("url", map.get("url"));
						intent.putExtra("position", position+"");
						context.startActivity(intent);
					}else{
						String str = list.get(position).get("isselect");
						if (str.equals("1")) {
							list.get(position).put("isselect", "0");
						}else{
							list.get(position).put("isselect", "1");
						}
						notifyDataSetChanged();
					}
				}
			});
//			GossiplistGridAdapter adapter = new GossiplistGridAdapter(imglist, context);
//			vh.mimggrid.setAdapter(adapter);
//			vh.mimggrid.setFocusable(false);
//			vh.mimggrid.setOnItemClickListener(new OnItemClickListener() {
//
//				@Override
//				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//					Intent intent = new Intent(context,ImageLookActivity.class);
//					intent.putExtra("img", imglist.get(arg2));
//					context.startActivity(intent);
//				}
//			});
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return convertView;
	}
	private void recyGrid(ViewHolder viewHolder, List<String> imglist, List<String> videoimg){
		WindowManager wm = (WindowManager) context .getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		ViewGroup.LayoutParams layoutParams = viewHolder.mimggrid.getLayoutParams();
		//根据图片个数来设置布局
		if (imglist.size() == 1){
			viewHolder.mimggrid.setNumColumns(1);
			layoutParams.width = width/2;
		}else if(imglist.size() == 2){
			viewHolder.mimggrid.setNumColumns(2);
			layoutParams.width = GridLayoutManager.LayoutParams.MATCH_PARENT;
		}else if (imglist.size() == 4){
			viewHolder.mimggrid.setNumColumns(2);
			layoutParams.width = width*2/3;
		}else {
			viewHolder.mimggrid.setNumColumns(3);
			layoutParams.width = GridLayoutManager.LayoutParams.MATCH_PARENT;
		}
		viewHolder.mimggrid.setLayoutParams(layoutParams);
		GossipPiazzaImageAdapter adapter = new GossipPiazzaImageAdapter(context,imglist,videoimg);
		viewHolder.mimggrid.setAdapter(adapter);
	}
	class ViewHolder {
		TextView mtime,mtype,mtitle,mallshow,mcontent;
		RelativeLayout mselect;
		ImageView mselectimg;
		MyGridView mimggrid;
	}

}
