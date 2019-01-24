package com.bbk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.JiFenListBean;
import com.bbk.Bean.JiFenOlistBean;
import com.bbk.activity.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rtj on 2018/3/7.
 */

public class JiFenDetailAdapter extends RecyclerView.Adapter {
    private List<JiFenListBean> jiFenListBeans;
    private Context context;

    public JiFenDetailAdapter(Context context, List<JiFenListBean> jiFenListBeans) {
        this.jiFenListBeans = jiFenListBeans;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder ViewHolder = new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.jifen_list_layout, parent, false));
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

    public void notifyData(List<JiFenListBean> beans) {
        this.jiFenListBeans.addAll(beans);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return jiFenListBeans.size();
    }


    private void initTop(final ViewHolder viewHolder, final int position) {
        try {
            JiFenListBean jiFenListBean = jiFenListBeans.get(position);
            viewHolder.tvYuefen.setText(jiFenListBean.getKey());
            JiFenOlistBean jiFenOlistBean = JSON.parseObject(jiFenListBean.getList(), JiFenOlistBean.class);
            if (jiFenOlistBean.getGetMsg() != null && jiFenOlistBean.getGetNum() != null&& !jiFenOlistBean.getGetNum().equals("0")){
                    viewHolder.llGetJifen.setVisibility(View.VISIBLE);
            }else {
                viewHolder.llGetJifen.setVisibility(View.GONE);
            }
            if (jiFenOlistBean.getUseMsg() != null && jiFenOlistBean.getUseNum() != null && !jiFenOlistBean.getUseNum().equals("0")){
                viewHolder.llUseJifen.setVisibility(View.VISIBLE);
            }else {
                viewHolder.llUseJifen.setVisibility(View.GONE);
            }
            if (jiFenOlistBean.getGetMsg() != null) {
                viewHolder.tvGetJifen.setVisibility(View.VISIBLE);
                viewHolder.tvGetJifen.setText(jiFenOlistBean.getGetMsg());
            } else {
                viewHolder.tvGetJifen.setVisibility(View.GONE);
            }
            if (jiFenOlistBean.getGetNum() != null) {
                viewHolder.tvGetnum.setVisibility(View.VISIBLE);
                viewHolder.tvGetnum.setText("+" + jiFenOlistBean.getGetNum());
            } else {
                viewHolder.tvGetnum.setVisibility(View.GONE);
            }

            if (jiFenOlistBean.getUseMsg() != null) {
                viewHolder.tvUseJifen.setVisibility(View.VISIBLE);
                viewHolder.tvUseJifen.setText(jiFenOlistBean.getUseMsg());
            } else {
                viewHolder.tvUseJifen.setVisibility(View.GONE);
            }

            if (jiFenOlistBean.getUseNum() != null) {
                viewHolder.tvUsenum.setVisibility(View.VISIBLE);
                viewHolder.tvUsenum.setText("-" + jiFenOlistBean.getUseNum());
            } else {
                viewHolder.tvUsenum.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_yuefen)
        TextView tvYuefen;
        @BindView(R.id.tv_get_jifen)
        TextView tvGetJifen;
        @BindView(R.id.tv_getnum)
        TextView tvGetnum;
        @BindView(R.id.tv_use_jifen)
        TextView tvUseJifen;
        @BindView(R.id.tv_usenum)
        TextView tvUsenum;
        @BindView(R.id.ll_get_jifen)
        LinearLayout llGetJifen;
        @BindView(R.id.ll_use_jifen)
        LinearLayout llUseJifen;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
