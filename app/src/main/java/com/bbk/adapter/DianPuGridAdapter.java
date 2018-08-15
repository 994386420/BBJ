package com.bbk.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbk.Bean.ShopDianpuBean;
import com.bbk.Bean.ShopTuijianBean;
import com.bbk.activity.R;
import com.bbk.activity.ShopDetailActivty;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rtj on 2018/3/7.
 */

public class DianPuGridAdapter extends RecyclerView.Adapter {
    //    private List<Map<String,String>> list;
    private Context context;
    List<ShopDianpuBean> shopDianpuBeans;

    public DianPuGridAdapter(Context context,List<ShopDianpuBean> shopDianpuBeans) {
//        this.list = list;
        this.context = context;
        this.shopDianpuBeans = shopDianpuBeans;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder ViewHolder = new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.shop_grid_item_layout, parent, false));
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
        return shopDianpuBeans.size();
    }

    public void notifyData(List<ShopDianpuBean> beans) {
        this.shopDianpuBeans.addAll(beans);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView item_img;
        TextView mbidprice, mprice, youhui;
        TextView item_title, copy_title, copy_url;
        LinearLayout itemlayout, mCopyLayout;
        @BindView(R.id.quan)
        TextView quan;
        @BindView(R.id.zuan)
        TextView zuan;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.bprice)
        TextView bprice;
        @BindView(R.id.ll_quan)
        LinearLayout llQuan;
        @BindView(R.id.tv_sale)
        TextView tvSale;
        @BindView(R.id.tv_sale1)
        TextView tvSale1;

        public ViewHolder(View mView) {
            super(mView);
            ButterKnife.bind(this, mView);
            item_img = mView.findViewById(R.id.item_img);
            item_title = mView.findViewById(R.id.item_title);
            mbidprice = mView.findViewById(R.id.mbidprice);
            mprice = mView.findViewById(R.id.mprice);
            youhui = mView.findViewById(R.id.youhui_text);
            itemlayout = mView.findViewById(R.id.result_item);
            mCopyLayout = mView.findViewById(R.id.copy_layout);
            copy_title = mView.findViewById(R.id.copy_title);
            copy_url = mView.findViewById(R.id.copy_url);
        }
    }

    private void initTop(final ViewHolder viewHolder, final int position) {
        try {
            final ShopDianpuBean shopTuijianBean = shopDianpuBeans.get(position);
            viewHolder.item_title.setText(shopTuijianBean.getTitle());
            viewHolder.tvSale.setVisibility(View.GONE);
            viewHolder.bprice.setText(shopTuijianBean.getBprice());
            viewHolder.bprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰
            viewHolder.price.setText(shopTuijianBean.getPrice());
            viewHolder.llQuan.setVisibility(View.GONE);
            viewHolder.zuan.setVisibility(View.GONE);
            viewHolder.tvSale1.setVisibility(View.VISIBLE);
            viewHolder.tvSale1.setText(shopTuijianBean.getSale() + "人已买");
            Glide.with(context)
                    .load(shopTuijianBean.getImgurl())
                    .priority(Priority.HIGH)
                    .placeholder(R.mipmap.zw_img_300)
                    .into(viewHolder.item_img);
            viewHolder.itemlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notifyDataSetChanged();
                    Intent intent;
                    intent = new Intent(context, ShopDetailActivty.class);
                    intent.putExtra("id", shopTuijianBean.getId());
                    context.startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
