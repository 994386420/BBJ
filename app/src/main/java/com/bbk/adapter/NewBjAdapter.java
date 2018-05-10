package com.bbk.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bbk.activity.BidBillDetailActivity;
import com.bbk.activity.BidDetailActivity;
import com.bbk.activity.BidHomeActivity;
import com.bbk.activity.HomeActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.QueryHistoryActivity;
import com.bbk.activity.R;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.RushBuyCountDownTimerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Created by rtj on 2018/3/7.
 */

public class NewBjAdapter extends RecyclerView.Adapter{
    private List<Map<String,String>> list;
    private Context context;
    private int ITEM_TYPE_TOP = 1;
    public NewBjAdapter(Context context, List<Map<String,String>> list){
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NewBjAdapter.ViewHolder ViewHolder = new NewBjAdapter.ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.bid_acceptance_listview, parent, false));
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            ViewHolder viewHolder = (ViewHolder) holder;
            initTop(viewHolder,position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void notifyData(List<Map<String,String>> List){
        this.list.addAll(List);
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView item_img;
        TextView item_title,mbidprice,mcount,mprice;
        RushBuyCountDownTimerView mtime;
        LinearLayout itemlayout;
        public ViewHolder(View mView) {
            super(mView);
            item_img = (ImageView)mView.findViewById(R.id.item_img);
            item_title = (TextView)mView.findViewById(R.id.item_title);
            mbidprice = (TextView)mView.findViewById(R.id.mbidprice);
            mcount = (TextView)mView.findViewById(R.id.mcount);
            mprice = (TextView)mView.findViewById(R.id.mprice);
            mtime = (RushBuyCountDownTimerView)mView.findViewById(R.id.mtime);
            itemlayout = mView.findViewById(R.id.result_item);
        }
    }
    private void initTop(NewBjAdapter.ViewHolder viewHolder, final int position) {
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
            viewHolder.mtime.friendly_time(endtime,"#999999");
//            viewHolder.mtime.start();
            viewHolder.item_title.setText(title);
            viewHolder.mbidprice.setVisibility(View.GONE);
            viewHolder.mprice.setText(price);
            viewHolder.mcount.setText("x"+number);
            Glide.with(context)
                    .load(img)
                    .priority(Priority.HIGH)
                    .placeholder(R.mipmap.zw_img_300)
                    .into(viewHolder.item_img);
            viewHolder.itemlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
                    try {
                        if (list.get(position).get("userid").equals(userID)) {
                            Intent intent = new Intent(context, BidBillDetailActivity.class);
                            intent.putExtra("fbid", list.get(position).get("id"));
                            context.startActivity(intent);
                        } else {
                            Intent intent = new Intent(context, BidDetailActivity.class);
                            intent.putExtra("id", list.get(position).get("id"));
                            context.startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
