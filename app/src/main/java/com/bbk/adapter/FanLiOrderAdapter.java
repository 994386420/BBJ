package com.bbk.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bbk.Bean.NewHomeCzgBean;
import com.bbk.Bean.OrderListBean;
import com.bbk.activity.FanLiOrderDetailActivity;
import com.bbk.activity.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rtj on 2018/3/7.
 */

public class FanLiOrderAdapter extends RecyclerView.Adapter {
    private Context context;
    List<OrderListBean> orderListBeans;

    public FanLiOrderAdapter(Context context, List<OrderListBean> orderListBeans) {
        this.context = context;
        this.orderListBeans = orderListBeans;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder ViewHolder = new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.order_item_layout, parent, false));
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            ViewHolder viewHolder = (ViewHolder) holder;
            initTop(viewHolder, position);
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
        return orderListBeans.size();
    }

    public void notifyData(List<OrderListBean> beans) {
        this.orderListBeans.addAll(beans);
        notifyDataSetChanged();
    }


    private void initTop(final ViewHolder viewHolder, final int position) {
        try {
            final OrderListBean orderListBean = orderListBeans.get(position);
            if (orderListBean.getStatus() != null && !orderListBean.getStatus().equals("")) {
                viewHolder.orderStatus.setVisibility(View.VISIBLE);
                viewHolder.orderStatus.setText(orderListBean.getStatus());
            }
            if (orderListBean.getTitle() != null) {
                viewHolder.orderTitle.setText(orderListBean.getTitle());
            }
            if (orderListBean.getDesc() != null) {
                viewHolder.orderTime.setText(orderListBean.getDesc());
            }
            if (orderListBean.getYj() != null) {
                viewHolder.orderYongjin.setText(orderListBean.getYj());
            }
            if (orderListBean.getYjmoney() != null&& !orderListBean.getYjmoney().equals("")) {
                viewHolder.orderYongjinMoney.setText("¥" + orderListBean.getYjmoney());
            }
            if (orderListBean.getImgurl() != null){
                Glide.with(context)
                        .load(orderListBean.getImgurl())
                        .priority(Priority.HIGH)
                        .placeholder(R.mipmap.zw_img_300)
                        .into(viewHolder.itemImg);
            }

                viewHolder.resultItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!orderListBean.getState().equals("4")) {
                            Intent intent = new Intent(context, FanLiOrderDetailActivity.class);
                            intent.putExtra("ddh", orderListBean.getDdh());
                            intent.putExtra("spid", orderListBean.getSpid());
                            intent.putExtra("domain", orderListBean.getDomain());
                            intent.putExtra("timeArr", orderListBean.getTimeArr());
                            intent.putExtra("imgurl", orderListBean.getImgurl());
                            intent.putExtra("yj", orderListBean.getYj());
                            intent.putExtra("yjmoney", orderListBean.getYjmoney());
                            intent.putExtra("money", orderListBean.getMoney());
                            intent.putExtra("dianpu", orderListBean.getDianpu());
                            intent.putExtra("state", orderListBean.getState());
                            intent.putExtra("title", orderListBean.getTitle());
                            intent.putExtra("url", orderListBean.getUrl());
                            context.startActivity(intent);
                        }
                    }
                });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.item_img)
        ImageView itemImg;
        @BindView(R.id.order_status)
        TextView orderStatus;
        @BindView(R.id.order_title)
        TextView orderTitle;
        @BindView(R.id.ll_title)
        LinearLayout llTitle;
        @BindView(R.id.order_yongjin)
        TextView orderYongjin;
        @BindView(R.id.order_yongjin_money)
        TextView orderYongjinMoney;
        @BindView(R.id.order_time)
        TextView orderTime;
        @BindView(R.id.mfengexian)
        View mfengexian;
        @BindView(R.id.result_item)
        RelativeLayout resultItem;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
