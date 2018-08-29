package com.bbk.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbk.Bean.PuDaoBean;
import com.bbk.activity.BaseActivity;
import com.bbk.activity.BidBillDetailActivity;
import com.bbk.activity.BidDetailActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.util.SharedPreferencesUtil;
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
    private List<PuDaoBean> puDaoBeans;
    private Context context;
    public BidAcceptanceAdapter(Context context,List<PuDaoBean> list){
        this.puDaoBeans = list;
        this.context = context;
    }
    @Override
    public int getCount() {
        return puDaoBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return puDaoBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void notifyData(List<PuDaoBean> List){
        this.puDaoBeans.addAll(List);
        notifyDataSetChanged();
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
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
                viewHolder.itemlayout = convertView.findViewById(R.id.result_item);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
          try {
            final PuDaoBean puDaoBean = puDaoBeans.get(position);
            String endtime = puDaoBean.getEndtime();
            String img = puDaoBean.getImg();
            String id = puDaoBean.getId();
            String title = puDaoBean.getTitle();
            String price = puDaoBean.getPrice();
            String bidprice = puDaoBean.getBidprice();
            String number = puDaoBean.getNumber();
            String type = puDaoBean.getType();
            String url = puDaoBean.getUrl();
            String status = puDaoBean.getStatus();
            final String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
            if (status != null){
                if (status.equals("1")){
                viewHolder.mStatus.setText("接单");
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
            viewHolder.item_title.setText(title);
            viewHolder.mbidprice.setText("接单价  "+bidprice);
            viewHolder.mprice.setText("¥"+price);
            viewHolder.mcount.setText("x"+number);
            Glide.with(context)
                    .load(img)
                    .priority(Priority.HIGH)
                    .placeholder(R.mipmap.zw_img_300)
                    .into(viewHolder.item_img);
              viewHolder.itemlayout.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      try {
                          if (puDaoBean.getUserid().equals(userID)){
                              Intent intent = new Intent(context,BidBillDetailActivity.class);
                              intent.putExtra("fbid",puDaoBean.getId());
                              context.startActivity(intent);
                          }else {
                              Intent intent = new Intent(context, BidDetailActivity.class);
                              intent.putExtra("id",puDaoBean.getId());
                              intent.putExtra("status",puDaoBean.getStatus());
                              context.startActivity(intent);
                          }
                      }catch (Exception e){
                          e.printStackTrace();
                      }
                  }
              });
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
        LinearLayout itemlayout;
    }

}
