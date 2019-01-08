package com.bbk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbk.Bean.DiscountPersonBean;
import com.bbk.activity.R;
import com.bbk.view.AdaptionSizeTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiscountPersonAdapter extends RecyclerView.Adapter {
    private Context context;
    List<DiscountPersonBean> discountBeans;
    private String state;


    public DiscountPersonAdapter(Context context, List<DiscountPersonBean> discountBeans, String state) {
        this.context = context;
        this.discountBeans = discountBeans;
        this.state = state;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder ViewHolder = new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.youhuiquan_item_layout, parent, false));
        return ViewHolder;
    }

    public void notifyData(List<DiscountPersonBean> beans) {
        this.discountBeans.addAll(beans);
        notifyDataSetChanged();
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
        if (discountBeans != null && discountBeans.size() > 0) {
            return discountBeans.size();
        } else {
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_discount_money)
        AdaptionSizeTextView tvDiscountMoney;
        @BindView(R.id.tv_discount_message)
        AdaptionSizeTextView tvDiscountMessage;
        @BindView(R.id.tv_discount_time)
        AdaptionSizeTextView tvDiscountTime;
        @BindView(R.id.tv_ok)
        TextView tvOk;
        @BindView(R.id.ll_discount)
        LinearLayout llDiscount;
        @BindView(R.id.tv_quan_msg)
        TextView tvQuanMsg;
        @BindView(R.id.img_has_lingqu)
        ImageView imgHasLingqu;
        @BindView(R.id.tv_ch2)
        AdaptionSizeTextView tvCh2;
        @BindView(R.id.img_kuaiguoqi)
        ImageView imgKuaiguoqi;

        public ViewHolder(View mView) {
            super(mView);
            ButterKnife.bind(this, mView);
        }
    }

    private void initTop(final ViewHolder viewHolder, final int position) {
        try {
            final DiscountPersonBean discountBean = discountBeans.get(position);
            viewHolder.tvQuanMsg.setText(discountBean.getTypeCh1());
            viewHolder.tvDiscountMoney.setText(discountBean.getMjmoneyprice());
            viewHolder.tvDiscountMessage.setText(discountBean.getMjmoneyCh());
            viewHolder.tvDiscountTime.setText(discountBean.getBegin() + " - " + discountBean.getEnd());
            viewHolder.tvCh2.setText(discountBean.getTypeCh2());
            viewHolder.tvCh2.setVisibility(View.VISIBLE);
            if (state != null) {
                switch (state) {
                    case "1":
                        viewHolder.tvOk.setText("立即使用");
                        viewHolder.tvOk.setVisibility(View.VISIBLE);
                        viewHolder.imgHasLingqu.setVisibility(View.GONE);
                        viewHolder.llDiscount.setBackgroundResource(R.mipmap.discount_02);
                        if (discountBean.getLater().equals("1")) {
                            viewHolder.imgKuaiguoqi.setVisibility(View.VISIBLE);
                        } else {
                            viewHolder.imgKuaiguoqi.setVisibility(View.GONE);
                        }
                        break;
                    case "-1":
                        viewHolder.tvOk.setVisibility(View.GONE);
                        viewHolder.imgHasLingqu.setVisibility(View.VISIBLE);
                        viewHolder.llDiscount.setBackgroundResource(R.mipmap.discount_02);
                        viewHolder.imgHasLingqu.setBackgroundResource(R.mipmap.discount_08);
                        break;
                    case "0":
                        viewHolder.tvOk.setVisibility(View.GONE);
                        viewHolder.imgHasLingqu.setVisibility(View.VISIBLE);
                        viewHolder.llDiscount.setBackgroundResource(R.mipmap.discount_01);
                        viewHolder.imgHasLingqu.setBackgroundResource(R.mipmap.discount_09);
                        break;
                }
            }
            viewHolder.tvOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (discountBean.getId() != null) {
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}