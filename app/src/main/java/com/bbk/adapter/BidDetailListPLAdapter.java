package com.bbk.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbk.activity.DesPictureActivity;
import com.bbk.activity.R;
import com.bbk.view.CircleImageView1;
import com.bbk.view.MyGridView;
import com.bumptech.glide.Glide;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by rtj on 2018/3/7.
 */

public class BidDetailListPLAdapter extends BaseAdapter{
    private List<Map<String,String>> list;
    private Context context;
    public BidDetailListPLAdapter(Context context, List<Map<String,String>> list){
        this.list = list;
        this.context = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(context, R.layout.bid_detail_pl_listview, null);
                viewHolder.mname = (TextView)convertView.findViewById(R.id.mname);
                viewHolder.mcontent = (TextView)convertView.findViewById(R.id.mcontent);
                viewHolder.mtime = (TextView)convertView.findViewById(R.id.mtime);
                viewHolder.mimg = (ImageView) convertView.findViewById(R.id.mimg);
                viewHolder.mratingbar = (MaterialRatingBar) convertView.findViewById(R.id.mratingbar);
                viewHolder.mgridview = (MyGridView) convertView.findViewById(R.id.mgridview);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Map<String,String> map = list.get(position);
            String plcontent = map.get("plcontent");
            String plrole = map.get("plrole");
            String pluserid = map.get("pluserid");
            String plid = map.get("plid");
            String plstar = map.get("plstar");
            String fbid = map.get("fbid");
            String pldtime = map.get("pldtime");
            String plhead = map.get("plhead");
            String plusername = map.get("plusername");
            String plimgs = map.get("plimgs");
            if ("1".equals(plrole)){
                viewHolder.mname.setText("发标："+plusername);
            }else {
                viewHolder.mname.setText("接镖："+plusername);
            }
            viewHolder.mratingbar.setRating(Float.valueOf(plstar));
            viewHolder.mcontent.setText(plcontent);
            viewHolder.mtime.setText("评论日期："+pldtime);
            CircleImageView1.getImg(context,plhead,viewHolder.mimg);
            if ("0".equals(plimgs)){
                viewHolder.mgridview.setVisibility(View.GONE);
            }else {
                JSONArray array = new JSONArray(plimgs);
                if (array.length()!= 0){
                    final List<String> imglist = new ArrayList<>();
                    for (int i = 0; i <array.length() ; i++) {
                        String images = array.optString(i);
                        imglist.add(images);
                    }
                    List<String> videoimg = new ArrayList<String>();
                    recyGrid(viewHolder,imglist,videoimg);
                    viewHolder.mgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent Intent = new Intent(context, DesPictureActivity.class);
                            Intent.putStringArrayListExtra("list", (ArrayList<String>) imglist);
                            Intent.putExtra("position",position);
                            context.startActivity(Intent);

                        }
                    });
                }else {
                    viewHolder.mgridview.setVisibility(View.GONE);
                }
            }
            return convertView;
        } catch (Exception e) {
            e.printStackTrace();
            return convertView;
        }
    }
    private void recyGrid(ViewHolder viewHolder, List<String> imglist, List<String> videoimg){
        WindowManager wm = (WindowManager) context .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        ViewGroup.LayoutParams layoutParams = viewHolder.mgridview.getLayoutParams();
        //根据图片个数来设置布局
        if (imglist.size() == 1){
            viewHolder.mgridview.setNumColumns(1);
            layoutParams.width = width/2;
        }else if(imglist.size() == 2){
            viewHolder.mgridview.setNumColumns(2);
            layoutParams.width = GridLayoutManager.LayoutParams.MATCH_PARENT;
        }else if (imglist.size() == 4){
            viewHolder.mgridview.setNumColumns(2);
            layoutParams.width = width*2/3;
        }else {
            viewHolder.mgridview.setNumColumns(3);
            layoutParams.width = GridLayoutManager.LayoutParams.MATCH_PARENT;
        }
        viewHolder.mgridview.setLayoutParams(layoutParams);
        GossipPiazzaImageAdapter adapter = new GossipPiazzaImageAdapter(context,imglist,videoimg);
        viewHolder.mgridview.setAdapter(adapter);
    }
    class ViewHolder{
        TextView mname,mcontent,mtime;
        ImageView mimg;
        MaterialRatingBar mratingbar;
        MyGridView mgridview;
    }

}
