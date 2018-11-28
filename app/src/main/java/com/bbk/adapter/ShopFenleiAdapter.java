package com.bbk.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.ShopFenLeiBean;
import com.bbk.activity.R;
import com.bbk.shopcar.NewDianpuHomeActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rtj on 2018/3/7.
 */

public class ShopFenleiAdapter extends RecyclerView.Adapter {
    private Context context;
    List<ShopFenLeiBean> shopFenLeiBeans;
    List<ShopFenLeiBean.ListBean> listBeans;

    public ShopFenleiAdapter(Context context, List<ShopFenLeiBean> shopFenLeiBeans) {
        this.context = context;
        this.shopFenLeiBeans = shopFenLeiBeans;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder ViewHolder = new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.shopfenlei_layout, parent, false));
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
        if (shopFenLeiBeans != null && shopFenLeiBeans.size() > 0) {
            return shopFenLeiBeans.size();
        } else {
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.recycler_fenlei)
        RecyclerView recyclerFenlei;

        public ViewHolder(View mView) {
            super(mView);
            ButterKnife.bind(this, mView);
        }
    }

    private void initTop(final ViewHolder viewHolder, final int position) {
        try {
            final ShopFenLeiBean shopFenLeiBean = shopFenLeiBeans.get(position);
            viewHolder.tvType.setText(shopFenLeiBean.getName());
            listBeans = JSON.parseArray(shopFenLeiBean.getList(),ShopFenLeiBean.ListBean.class);
            LinearLayoutManager linearLayoutManager = new GridLayoutManager(context,5);
            viewHolder.recyclerFenlei.setLayoutManager(linearLayoutManager);
            viewHolder.recyclerFenlei.setAdapter(new FenleiTypeAdapter(context,listBeans));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
