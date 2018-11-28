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

import com.bbk.Bean.ShopFenLeiBean;
import com.bbk.activity.R;
import com.bbk.model.DianpuSearchActivity;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FenleiTypeAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<ShopFenLeiBean.ListBean> listBeans;

    public FenleiTypeAdapter(Context mContext, List<ShopFenLeiBean.ListBean> listBeans) {
        super();
        this.mContext = mContext;
        this.listBeans = listBeans;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder ViewHolder = new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rank_gridview, parent, false));
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
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public int getItemCount() {
        if (listBeans != null && listBeans.size() > 0) {
            return listBeans.size();
        } else {
            return 0;
        }
    }

    private void initTop(final ViewHolder viewHolder, final int position) {
        try {
            final ShopFenLeiBean.ListBean listBean = listBeans.get(position);
            viewHolder.tvGridViewItem.setText(listBean.getName());
            viewHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, DianpuSearchActivity.class);
                    intent.putExtra("dianpuid", "");
                    intent.putExtra("producttype", listBean.getName());
                    intent.putExtra("plevel", "2");
                    intent.putExtra("keyword", "");
                    mContext.startActivity(intent);
                }
            });
            Glide.with(mContext).load(listBean.getImg()).skipMemoryCache(true)
                    .placeholder(R.mipmap.zw_img_300)
                    .into(viewHolder.ivGridViewImg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_gridView_img)
        ImageView ivGridViewImg;
        @BindView(R.id.tv_gridView_item)
        TextView tvGridViewItem;
        @BindView(R.id.item_layout)
        LinearLayout itemLayout;

        public ViewHolder(View mView) {
            super(mView);
            ButterKnife.bind(this, mView);
        }
    }
}
