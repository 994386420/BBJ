package com.bbk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bbk.Bean.BrokerageDetailBean;
import com.bbk.Bean.ShopDianpuBean;
import com.bbk.activity.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rtj on 2018/3/7.
 */

public class BrokerageDetailAdapter extends RecyclerView.Adapter {
    private List<BrokerageDetailBean> brokerageDetailBeans;
    private Context context;

    public BrokerageDetailAdapter(Context context, List<BrokerageDetailBean> brokerageDetailBeans) {
        this.brokerageDetailBeans = brokerageDetailBeans;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder ViewHolder = new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.brokeragedetail_item_layout, parent, false));
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
    public void notifyData(List<BrokerageDetailBean> beans) {
        this.brokerageDetailBeans.addAll(beans);
        notifyDataSetChanged();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return brokerageDetailBeans.size();
    }


    private void initTop(final ViewHolder viewHolder, final int position) {
        try {
            viewHolder.tvDdh.setText(brokerageDetailBeans.get(position).getDdh());
            viewHolder.tvUsername.setText(brokerageDetailBeans.get(position).getUsername());
            viewHolder.tvMoney.setText(brokerageDetailBeans.get(position).getYongjin());
            viewHolder.tvTime.setText(brokerageDetailBeans.get(position).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_username)
        TextView tvUsername;
        @BindView(R.id.tv_ddh)
        TextView tvDdh;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_money)
        TextView tvMoney;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
