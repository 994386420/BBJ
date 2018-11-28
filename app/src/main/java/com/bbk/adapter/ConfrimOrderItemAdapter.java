package com.bbk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbk.Bean.ConfrimOredetItemBean;
import com.bbk.activity.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.logg.Logg;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rtj on 2018/3/7.
 */

public class ConfrimOrderItemAdapter extends RecyclerView.Adapter {
    private Context context;
    List<ConfrimOredetItemBean> confrimOredetItemBeans;
    private String orderid, state;

    public ConfrimOrderItemAdapter(Context context, List<ConfrimOredetItemBean> confrimOredetItemBeans) {
//        this.list = list;
        this.context = context;
        this.confrimOredetItemBeans = confrimOredetItemBeans;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder ViewHolder = new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.order_item_recyc_layout, parent, false));
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
        return confrimOredetItemBeans.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_img)
        ImageView itemImg;
        @BindView(R.id.item_title)
        TextView itemTitle;
        @BindView(R.id.tv_shop_price)
        TextView tvShopPrice;
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.tv_shop_guige)
        TextView tvShopGuige;
        @BindView(R.id.item_order)
        LinearLayout itemOrder;
        @BindView(R.id.tv_money)
        TextView tvMoney;
        @BindView(R.id.tv_number)
        TextView tvNumber;
        @BindView(R.id.ll_price)
        LinearLayout llPrice;
        @BindView(R.id.ll_price1)
        LinearLayout llPrice1;

        public ViewHolder(View mView) {
            super(mView);
            ButterKnife.bind(this, mView);
        }
    }

    private void initTop(final ViewHolder vh, final int position) {
        try {
            final ConfrimOredetItemBean confrimOredetItemBean = confrimOredetItemBeans.get(position);
            vh.itemTitle.setText(confrimOredetItemBean.getTitle());
            vh.tvNum.setText("x" + confrimOredetItemBean.getNumber());
            vh.tvShopPrice.setText("￥" + confrimOredetItemBean.getPrice());
//            Logg.e(confrimOredetItemBean.getGuige());
            vh.tvShopGuige.setText(confrimOredetItemBean.getGuige());
            vh.tvMoney.setText(confrimOredetItemBean.getPrice());
            vh.tvNumber.setText("x" + confrimOredetItemBean.getNumber());
            vh.llPrice.setVisibility(View.VISIBLE);
            vh.llPrice1.setVisibility(View.GONE);
            Glide.with(context)
                    .load(confrimOredetItemBean.getImgurl())
                    .priority(Priority.HIGH)
                    .placeholder(R.mipmap.zw_img_300)
                    .into(vh.itemImg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
