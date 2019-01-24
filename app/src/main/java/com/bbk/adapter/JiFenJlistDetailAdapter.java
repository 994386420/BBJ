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

import com.bbk.Bean.JiFenJlistBean;
import com.bbk.activity.R;
import com.bbk.shopcar.ShopOrderActivity;
import com.bbk.shopcar.ShopOrderDetailActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rtj on 2018/3/7.
 */

public class JiFenJlistDetailAdapter extends RecyclerView.Adapter {
    private List<JiFenJlistBean> jiFenJlistBeans;
    private Context context;

    public JiFenJlistDetailAdapter(Context context, List<JiFenJlistBean> jiFenJlistBeans) {
        this.jiFenJlistBeans = jiFenJlistBeans;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder ViewHolder = new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.jifen_jlist_layout, parent, false));
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

    public void notifyData(List<JiFenJlistBean> beans) {
        this.jiFenJlistBeans.addAll(beans);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return jiFenJlistBeans.size();
    }


    private void initTop(final ViewHolder viewHolder, final int position) {
        try {
            final JiFenJlistBean jiFenJlistBean = jiFenJlistBeans.get(position);
            viewHolder.tvShopName.setText(jiFenJlistBean.getTitle());
            viewHolder.tvTime.setText(jiFenJlistBean.getSdt());
            viewHolder.tvUseJifen.setText(jiFenJlistBean.getJifen());
            Glide.with(context).load(jiFenJlistBean.getImgurl()).priority(Priority.HIGH)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .placeholder(R.mipmap.zw_img_300).into(viewHolder.imgUseJifen);
            viewHolder.llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShopOrderDetailActivity.class);
                    intent.putExtra("orderid", jiFenJlistBean.getOrdernum());
                    intent.putExtra("dianpuid",jiFenJlistBean.getDianpuid());
                    intent.putExtra("state", jiFenJlistBean.getState());
                    context.startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_use_jifen)
        ImageView imgUseJifen;
        @BindView(R.id.tv_shop_name)
        TextView tvShopName;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_use_jifen)
        TextView tvUseJifen;
        @BindView(R.id.ll_item)
        LinearLayout llItem;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
