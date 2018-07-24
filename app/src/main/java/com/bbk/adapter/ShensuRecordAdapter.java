package com.bbk.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbk.Bean.NewHomeCzgBean;
import com.bbk.Bean.ShensuBean;
import com.bbk.activity.FanLiOrderActivity;
import com.bbk.activity.R;
import com.bbk.view.AdaptionSizeTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rtj on 2018/3/7.
 */

public class ShensuRecordAdapter extends RecyclerView.Adapter {
    private List<ShensuBean> shensuBeans;
    private Context context;

    public ShensuRecordAdapter(Context context, List<ShensuBean> shensuBeans) {
        this.shensuBeans = shensuBeans;
        this.context = context;
    }

    public void notifyData(List<ShensuBean> beans) {
        this.shensuBeans.addAll(beans);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder ViewHolder = new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.shensu_item_layout, parent, false));
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
        return shensuBeans.size();
    }


    private void initTop(final ViewHolder viewHolder, final int position) {
        try {
            final ShensuBean shensuBean = shensuBeans.get(position);
            if (shensuBean.getDt() != null) {
                viewHolder.tvDt.setText(shensuBean.getDt());
            }
            if (shensuBean.getDescript() != null) {
                viewHolder.tvDescrip.setText(shensuBean.getDescript());
            }
            if (shensuBean.getReson() != null) {
                viewHolder.tvReson.setText(shensuBean.getReson());
            }
            if (shensuBean.getState() != null && shensuBean.getState().equals("1")){
                viewHolder.imgShensu.setVisibility(View.VISIBLE);
            }else {
                viewHolder.imgShensu.setVisibility(View.GONE);
            }
            viewHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (shensuBean.getState() != null && shensuBean.getState().equals("1")){
                        Intent intent = new Intent(context, FanLiOrderActivity.class);
                        context.startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_dt)
        TextView tvDt;
        @BindView(R.id.tv_descrip)
        TextView tvDescrip;
        @BindView(R.id.tv_reson)
        AdaptionSizeTextView tvReson;
        @BindView(R.id.img_shensu)
        ImageView imgShensu;
        @BindView(R.id.ll_shensu)
        LinearLayout llShensu;
        @BindView(R.id.item_layout)
        LinearLayout itemLayout;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
