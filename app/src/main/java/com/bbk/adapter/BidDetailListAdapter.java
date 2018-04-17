package com.bbk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.CircleImageView1;
import com.bbk.view.RushBuyCountDownTimerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Created by rtj on 2018/3/7.
 */

public class BidDetailListAdapter extends BaseAdapter{
    private List<Map<String,String>> list;
    private Context context;
    public BidDetailListAdapter(Context context, List<Map<String,String>> list){
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
                convertView = View.inflate(context, R.layout.bid_detail_listview, null);
                viewHolder.mbidtime = (TextView)convertView.findViewById(R.id.mbidtime);
                viewHolder.mbidprice = (TextView)convertView.findViewById(R.id.mbidprice);
                viewHolder.muser = (TextView)convertView.findViewById(R.id.muser);
                viewHolder.mimg = convertView.findViewById(R.id.mimg);
                viewHolder.mBid = convertView.findViewById(R.id.mbidtext);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Map<String,String> map = list.get(position);
            String bidid = map.get("bidid");
            String biduser = map.get("biduser");
            String biddesc = map.get("biddesc");
            String bidprice = map.get("bidprice");
            String bidtime = map.get("bidtime");
            String biduserid = map.get("biduserid");
            viewHolder.mbidprice.setText("￥"+bidprice);
            viewHolder.mbidtime.setText(bidtime);
            viewHolder.muser.setText(biduser);
            if (position == 0){

            }
            String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
            if (biduserid.equals(userID)){
                viewHolder.mbidprice.setTextColor(Color.parseColor("#1a9afc"));
                viewHolder.mbidtime.setTextColor(Color.parseColor("#1a9afc"));
                viewHolder.muser.setTextColor(Color.parseColor("#1a9afc"));
                viewHolder.mBid.setTextColor(Color.parseColor("#1a9afc"));
                String imgUrl = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "imgUrl");
                CircleImageView1.getImg(context,imgUrl,viewHolder.mimg);

            }
            switch (position){
                case 0:
                    viewHolder.mbidprice.setTextColor(Color.parseColor("#b40000"));
                    viewHolder.mbidtime.setTextColor(Color.parseColor("#b40000"));
                    viewHolder.muser.setTextColor(Color.parseColor("#b40000"));
                    viewHolder.mBid.setTextColor(Color.parseColor("#b40000"));
                    viewHolder.mimg.setBackgroundResource(R.mipmap.bj_05);
                    break;
                case 1:
                    viewHolder.mbidprice.setTextColor(Color.parseColor("#666666"));
                    viewHolder.mbidtime.setTextColor(Color.parseColor("#666666"));
                    viewHolder.muser.setTextColor(Color.parseColor("#666666"));
                    viewHolder.mBid.setTextColor(Color.parseColor("#666666"));
                    viewHolder.mimg.setBackgroundResource(R.mipmap.bj_06);
                    break;
                default:
                    viewHolder.mbidprice.setTextColor(Color.parseColor("#999999"));
                    viewHolder.mbidtime.setTextColor(Color.parseColor("#999999"));
                    viewHolder.muser.setTextColor(Color.parseColor("#999999"));
                    viewHolder.mBid.setTextColor(Color.parseColor("#999999"));
                    viewHolder.mimg.setBackgroundResource(R.mipmap.bj_07);
                    break;
            }
            return convertView;
        } catch (Exception e) {
            e.printStackTrace();
            return convertView;
        }
    }
    class ViewHolder{
        TextView mbidprice,mbidtime,muser,mBid;
        ImageView mimg;
    }

}
