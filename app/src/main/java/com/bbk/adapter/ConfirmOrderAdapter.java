package com.bbk.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.ConfrimOredetItemBean;
import com.bbk.Bean.GoodsBean;
import com.bbk.Bean.OrderItembean;
import com.bbk.activity.R;
import com.bbk.shopcar.Utils.ShopDialog;
import com.bbk.view.AdaptionSizeTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rtj on 2018/3/7.
 */

public class ConfirmOrderAdapter extends RecyclerView.Adapter {
    //    private List<Map<String,String>> list;
    private Context context;
    List<GoodsBean> goodsBeans;
    private ShopDialog shopDialog;

    public ConfirmOrderAdapter(Context context, List<GoodsBean> goodsBeans) {
//        this.list = list;
        this.context = context;
        this.goodsBeans = goodsBeans;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder ViewHolder = new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.confrim_order_item, parent, false));
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
        if (goodsBeans != null && goodsBeans.size() > 0) {
            return goodsBeans.size();
        }else {
            return 0;
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.mdianpu)
        TextView mdianpu;
        @BindView(R.id.recyclerview_order_item)
        RecyclerView recyclerviewOrderItem;
        @BindView(R.id.ll_fuwu)
        LinearLayout llFuwu;
        @BindView(R.id.tv_kuaidi)
        AdaptionSizeTextView tvKuaidi;
        @BindView(R.id.tv_num)
        AdaptionSizeTextView tvNum;
        @BindView(R.id.tv_subprice)
        AdaptionSizeTextView tvSubprice;
        @BindView(R.id.result_item)
        LinearLayout resultItem;

        public ViewHolder(View mView) {
            super(mView);
            ButterKnife.bind(this, mView);
        }
    }

    private void initTop(final ViewHolder viewHolder, final int position) {
        try {
            final GoodsBean goodsBean = goodsBeans.get(position);
            viewHolder.tvSubprice.setText(goodsBean.getSubprice());
            viewHolder.mdianpu.setText(goodsBean.getDianpu());
            viewHolder.tvKuaidi.setText(goodsBean.getWuliu());
            viewHolder.tvNum.setText("共"+goodsBean.getTotalnumber()+"件商品，小计");
            viewHolder.llFuwu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showBaozhangeDialog(context);
                }
            });
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,
                    LinearLayoutManager.VERTICAL, false) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }

            };
            viewHolder.recyclerviewOrderItem.setLayoutManager(linearLayoutManager);
            List<ConfrimOredetItemBean> confrimOredetItemBeans = JSON.parseArray(goodsBean.getList(),ConfrimOredetItemBean.class);
            viewHolder.recyclerviewOrderItem.setAdapter(new ConfrimOrderItemAdapter(context,confrimOredetItemBeans));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 商品基础保障
     *
     * @param context
     */
    public void showBaozhangeDialog(final Context context) {
        if (shopDialog == null || !shopDialog.isShowing()) {
            //初始化弹窗 布局 点击事件的id
            shopDialog = new ShopDialog(context, R.layout.shop_dialog_layout,
                    new int[]{R.id.tv_ok});
            shopDialog.show();
            shopDialog.setCanceledOnTouchOutside(true);
            TextView tv_update_gengxin = shopDialog.findViewById(R.id.tv_ok);
            TextView tv_title = shopDialog.findViewById(R.id.tv_title);
            tv_title.setText("基础保障");
            RecyclerView recyclerView = shopDialog.findViewById(R.id.recyclerview_shop_dialog);
            LinearLayout linearLayout = shopDialog.findViewById(R.id.ll_baozhang);
            linearLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            ImageView img_close = shopDialog.findViewById(R.id.img_close);
            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shopDialog.dismiss();
                }
            });
            tv_update_gengxin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shopDialog.dismiss();
                }
            });
        }
    }

}
