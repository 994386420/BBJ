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

import com.bbk.activity.BidDetailActivity;
import com.bbk.activity.DesPictureActivity;
import com.bbk.activity.R;
import com.bbk.view.CircleImageView1;
import com.bbk.view.MyGridView;
import com.bbk.view.RushBuyCountDownTimerView;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by rtj on 2018/3/7.
 */

public class BidAllHotAdapter extends BaseAdapter{
    private List<Map<String,String>> list;
    private Context context;
    public BidAllHotAdapter(Context context, List<Map<String,String>> list){
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
                convertView = View.inflate(context, R.layout.fragment_bidhome_hot, null);
                viewHolder.mtime = (RushBuyCountDownTimerView) convertView.findViewById(R.id.mtime);
                viewHolder.mprice = (TextView) convertView.findViewById(R.id.mprice);
                viewHolder.mextra = (TextView) convertView.findViewById(R.id.mextra);
                viewHolder.mtitle = (TextView) convertView.findViewById(R.id.mtitle);
                viewHolder.mimg1 = (ImageView) convertView.findViewById(R.id.mimg1);
                viewHolder.mimg2 = (ImageView) convertView.findViewById(R.id.mimg2);
                viewHolder.mimg3 = (ImageView) convertView.findViewById(R.id.mimg3);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Map<String,String> map = list.get(position);
            ImageView[] img = {viewHolder.mimg1,viewHolder.mimg2,viewHolder.mimg3};
            viewHolder.mtitle.setText(map.get("title"));
            viewHolder.mtime.addsum(map.get("endtime"),"#b40000");
            viewHolder.mtime.start();
            viewHolder.mprice.setText(map.get("price"));
            viewHolder.mextra.setText(map.get("extra"));
            JSONArray imgs = new JSONArray(map.get("imgs"));
            for (int j = 0; j < 3; j++) {
                Glide.with(context).load(imgs.getString(j)).into(img[j]);
            }

            return convertView;
        } catch (Exception e) {
            e.printStackTrace();
            return convertView;
        }
    }

    class ViewHolder{
        private TextView mprice,mtitle,mextra;
        private ImageView mimg1,mimg2,mimg3;
        private RushBuyCountDownTimerView mtime;
    }

}
