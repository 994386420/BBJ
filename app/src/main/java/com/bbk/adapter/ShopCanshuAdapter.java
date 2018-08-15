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

import com.bbk.Bean.CanshuBean;
import com.bbk.Bean.ShopTuijianBean;
import com.bbk.activity.R;
import com.bbk.activity.ShopDetailActivty;
import com.bbk.view.AdaptionSizeTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rtj on 2018/3/7.
 */

public class ShopCanshuAdapter extends RecyclerView.Adapter {
    //    private List<Map<String,String>> list;
    private Context context;
    List<CanshuBean> canshuBeans;

    public ShopCanshuAdapter(Context context, List<CanshuBean> canshuBeans) {
//        this.list = list;
        this.context = context;
        this.canshuBeans = canshuBeans;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder ViewHolder = new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.canshu_item_layout, parent, false));
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
        return canshuBeans.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_canshu1)
        TextView tvCanshu1;
        @BindView(R.id.tv_canshu2)
        AdaptionSizeTextView tvCanshu2;

        public ViewHolder(View mView) {
            super(mView);
            ButterKnife.bind(this, mView);
        }
    }

    private void initTop(final ViewHolder viewHolder, final int position) {
        try {
            final CanshuBean canshuBean = canshuBeans.get(position);
            viewHolder.tvCanshu1.setText(canshuBean.getName());
            viewHolder.tvCanshu2.setText(canshuBean.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
