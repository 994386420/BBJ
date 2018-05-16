package com.bbk.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbk.activity.BaseActivity;
import com.bbk.activity.R;
import com.bbk.view.RushBuyCountDownTimerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rtj on 2018/3/7.
 */

public class BidAcceptanceAdapter extends BaseAdapter{
    private List<Map<String,String>> list;
    private Context context;
    public BidAcceptanceAdapter(Context context,List<Map<String,String>> list){
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

    public void notifyData(List<Map<String,String>> List){
        this.list.addAll(List);
        notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(context, R.layout.bid_acceptance_listview, null);
                viewHolder.item_img = (ImageView)convertView.findViewById(R.id.item_img);
                viewHolder.item_title = (TextView)convertView.findViewById(R.id.item_title);
                viewHolder.mbidprice = (TextView)convertView.findViewById(R.id.mbidprice);
                viewHolder.mcount = (TextView)convertView.findViewById(R.id.mcount);
                viewHolder.mprice = (TextView)convertView.findViewById(R.id.mprice);
                viewHolder.mtime = (RushBuyCountDownTimerView)convertView.findViewById(R.id.mtime);
                viewHolder.mbackground = convertView.findViewById(R.id.item_image_background);
                viewHolder.mStatus = convertView.findViewById(R.id.status_text);
                viewHolder.mTimeStatus = convertView.findViewById(R.id.time_status_text);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
          try {
            Map<String,String> map = list.get(position);
            String endtime = map.get("endtime");
            String img = map.get("img");
            String id = map.get("id");
            String title = map.get("title");
            String price = map.get("price");
            String bidprice = map.get("bidprice");
            String number = map.get("number");
            String type = map.get("type");
            String url = map.get("url");
            String status = map.get("status");
            if (status != null){
                if (status.equals("1")){
                viewHolder.mStatus.setText("扑倒");
                viewHolder.mbackground.setVisibility(View.GONE);
                viewHolder.mTimeStatus.setText("距结束");
                viewHolder.mtime.setVisibility(View.VISIBLE);
                viewHolder.mtime.friendly_time(endtime,"#999999");
                }else {
                viewHolder.mbackground.setVisibility(View.VISIBLE);
                viewHolder.mStatus.setText("完成");
                viewHolder.mTimeStatus.setText("已结束");
                viewHolder.mtime.setVisibility(View.GONE);
                }
            }
//            viewHolder.mtime.start();
            viewHolder.item_title.setText(title);
            viewHolder.mbidprice.setText("扑倒价  "+bidprice);
            viewHolder.mprice.setText(price);
            viewHolder.mcount.setText("x"+number);
            Glide.with(context)
                    .load(img)
                    .priority(Priority.HIGH)
                    .placeholder(R.mipmap.zw_img_300)
                    .into(viewHolder.item_img);
            return convertView;
        } catch (Exception e) {
            e.printStackTrace();
            return convertView;
        }
    }
    class ViewHolder{
        ImageView item_img,mbackground;
        TextView item_title,mbidprice,mcount,mprice,mStatus;
        RushBuyCountDownTimerView mtime;
        TextView mTimeStatus;//根据时间是否完成显示状态
    }

}
