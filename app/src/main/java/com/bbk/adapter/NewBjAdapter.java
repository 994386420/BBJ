package com.bbk.adapter;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bbk.Bean.NewHomeCzgBean;
import com.bbk.Bean.NewHomePubaBean;
import com.bbk.activity.BidBillDetailActivity;
import com.bbk.activity.BidDetailActivity;
import com.bbk.activity.BidHomeActivity;
import com.bbk.activity.HomeActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.QueryHistoryActivity;
import com.bbk.activity.R;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
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
    List<NewHomePubaBean> newHomePubaBeans;
    public NewBjAdapter(Context context, List<NewHomePubaBean> pubaBeans){
        this.list = list;
        this.context = context;
        this.newHomePubaBeans = pubaBeans;
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
        return newHomePubaBeans.size();
    }

    public void notifyData(List<NewHomePubaBean> beans){
        this.newHomePubaBeans.addAll(beans);
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView item_img;
        TextView item_title,mbidprice,mcount,mprice,copy_title;
        RushBuyCountDownTimerView mtime;
        LinearLayout itemlayout,mCopyLayout;
        public ViewHolder(View mView) {
            super(mView);
            item_img = (ImageView)mView.findViewById(R.id.item_img);
            item_title = (TextView)mView.findViewById(R.id.item_title);
            mbidprice = (TextView)mView.findViewById(R.id.mbidprice);
            mcount = (TextView)mView.findViewById(R.id.mcount);
            mprice = (TextView)mView.findViewById(R.id.mprice);
            mtime = (RushBuyCountDownTimerView)mView.findViewById(R.id.mtime);
            itemlayout = mView.findViewById(R.id.result_item);
            mCopyLayout = mView.findViewById(R.id.copy_layout);
            copy_title = mView.findViewById(R.id.copy_title);
        }
    }
    private void initTop(final NewBjAdapter.ViewHolder viewHolder, final int position) {
        try {
//            Map<String,String> map = list.get(position);
            String endtime = newHomePubaBeans.get(position).getEndtime();
            String img = newHomePubaBeans.get(position).getImg();
            String id = newHomePubaBeans.get(position).getId();
            final String title =newHomePubaBeans.get(position).getTitle();
            String price = newHomePubaBeans.get(position).getPrice();
//            String bidprice = map.get("bidprice");
            String number = newHomePubaBeans.get(position).getNumber();
            String type = newHomePubaBeans.get(position).getType();
//            String url = map.get("url");
            viewHolder.mtime.friendly_time(endtime,"#999999");
//            viewHolder.mtime.start();
            viewHolder.item_title.setText(title);
            viewHolder.mbidprice.setVisibility(View.GONE);
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
                    notifyDataSetChanged();
                    final String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
                    try {
                        if (newHomePubaBeans.get(position).getUserid().equals(userID)) {
                            Intent intent = new Intent(context, BidBillDetailActivity.class);
                            intent.putExtra("fbid", newHomePubaBeans.get(position).getId());
                            context.startActivity(intent);
                        } else {
                            Intent intent = new Intent(context, BidDetailActivity.class);
                            intent.putExtra("id", newHomePubaBeans.get(position).getId());
                            context.startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            viewHolder.itemlayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    viewHolder.mCopyLayout.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            viewHolder.mCopyLayout.setVisibility(View.GONE);
                        }
                    }, 2500);
                    return true;
                }
            });
            viewHolder.mCopyLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewHolder.mCopyLayout.setVisibility(View.GONE);
                }
            });
            viewHolder.copy_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(title);
                    StringUtil.showToast(context,"复制成功");
                    viewHolder.mCopyLayout.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
