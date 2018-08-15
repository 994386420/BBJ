package com.bbk.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.OrderItemBeanList;
import com.bbk.Bean.OrderItembean;
import com.bbk.Bean.ShopOrderBean;
import com.bbk.activity.R;
import com.bbk.dialog.AlertDialog;
import com.bbk.shopcar.ShopOrderDetailActivity;
import com.bbk.shopcar.WuLiuActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rtj on 2018/2/27.
 */

public class ShopOrderWaiCengAdapter extends BaseAdapter {
    private Context context;
    private List<ShopOrderBean> shopOrderBeans;

    public ShopOrderWaiCengAdapter(Context context, List<ShopOrderBean> list) {
        this.context = context;
        this.shopOrderBeans = list;
    }

    public void notifyData(List<ShopOrderBean> beans) {
        this.shopOrderBeans.addAll(beans);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return shopOrderBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return shopOrderBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.shop_order_waiceng_item, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        try {
            final ShopOrderBean shopOrderBean = shopOrderBeans.get(position);
            vh.recyclerviewOrderItem.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,
                    LinearLayoutManager.VERTICAL, false) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }

            };
            vh.recyclerviewOrderItem.setLayoutManager(linearLayoutManager);
            List<OrderItemBeanList> orderItemBeanLists = JSON.parseArray(shopOrderBean.getArr1(),OrderItemBeanList.class);
            vh.recyclerviewOrderItem.setAdapter(new ShopOrderAdapter(context,orderItemBeanLists,shopOrderBean.getOrdernum()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.recyclerview_order_item)
        RecyclerView recyclerviewOrderItem;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
