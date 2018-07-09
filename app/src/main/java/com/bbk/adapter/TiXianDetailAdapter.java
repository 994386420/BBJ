package com.bbk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbk.Bean.TiXianDetailBean;
import com.bbk.activity.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rtj on 2018/3/7.
 */

public class TiXianDetailAdapter extends RecyclerView.Adapter {
    private List<TiXianDetailBean> tiXianDetailBeans;
    private Context context;

    public TiXianDetailAdapter(Context context, List<TiXianDetailBean> tiXianDetailBeans) {
        this.tiXianDetailBeans = tiXianDetailBeans;
        this.context = context;
    }

    public void notifyData(List<TiXianDetailBean> tiXianDetailBeans) {
        if (tiXianDetailBeans != null && tiXianDetailBeans.size() > 0) {
            this.tiXianDetailBeans.addAll(tiXianDetailBeans);
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder ViewHolder = new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.tixian_item_layout, parent, false));
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
        return tiXianDetailBeans.size();
    }


    private void initTop(final ViewHolder viewHolder, final int position) {
        try {
            viewHolder.tvDdh.setText(tiXianDetailBeans.get(position).getTime());
            viewHolder.tvUsername.setText(tiXianDetailBeans.get(position).getMessage());
            viewHolder.tvMoney.setText(tiXianDetailBeans.get(position).getJinbi());
            viewHolder.llTixian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_username)
        TextView tvUsername;
        @BindView(R.id.tv_ddh)
        TextView tvDdh;
        @BindView(R.id.tv_money)
        TextView tvMoney;
        @BindView(R.id.ll_tixian)
        LinearLayout llTixian;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
