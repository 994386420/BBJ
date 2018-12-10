package com.bbk.adapter;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbk.Bean.ZeroBuyBean;
import com.bbk.activity.IntentActivity;
import com.bbk.activity.R;
import com.bbk.activity.ZiYingZeroBuyDetailActivty;
import com.bbk.model.view.CustomProgressBar;
import com.bbk.resource.NewConstants;
import com.bbk.util.StringUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rtj on 2018/3/7.
 */

public class ZeroBuyForOlderAdapter extends RecyclerView.Adapter {
    private Context context;
    List<ZeroBuyBean> zeroBuyBeans;


    public ZeroBuyForOlderAdapter(Context context, List<ZeroBuyBean> zeroBuyBeans) {
        this.context = context;
        this.zeroBuyBeans = zeroBuyBeans;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder ViewHolder = new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.zero_buy_item_layout, parent, false));
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
        if (zeroBuyBeans != null && zeroBuyBeans.size() > 0) {
            return zeroBuyBeans.size();
        } else {
            return 0;
        }
    }

    public void notifyData(List<ZeroBuyBean> beans) {
        this.zeroBuyBeans.addAll(beans);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView item_img;
        TextView mbidprice, mprice, youhui;
        TextView item_title, copy_title, copy_url;
        LinearLayout itemlayout, mCopyLayout;
        @BindView(R.id.dianpu_text)
        TextView dianpuText;
        @BindView(R.id.quan)
        TextView quan;
        @BindView(R.id.zuan)
        TextView zuan;
        @BindView(R.id.price1)
        TextView price1;
        @BindView(R.id.bprice1)
        TextView bprice1;
        @BindView(R.id.tv_mall)
        TextView tvMall;
        @BindView(R.id.tv_sale)
        TextView tvSale;
        @BindView(R.id.ll_quan)
        LinearLayout llQuan;
        @BindView(R.id.cpb_progresbar)
        CustomProgressBar cpbProgresbar;
        @BindView(R.id.tv_pintuan)
        TextView tvPintuan;
        @BindView(R.id.ll_pintuan)
        LinearLayout llPintuan;
        @BindView(R.id.ll_price)
        LinearLayout llPrice;
        @BindView(R.id.ll_price1)
        LinearLayout llPrice1;
        @BindView(R.id.tv_zerobuy)
        TextView tvZerobuy;
        @BindView(R.id.tv_salebi)
        TextView tvSalebi;
        @BindView(R.id.rmb1)
        TextView rmb1;

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
            final ZeroBuyBean zeroBuyBean = zeroBuyBeans.get(position);
            String img = zeroBuyBean.getImg();
            final String title = zeroBuyBean.getTitle();
            viewHolder.tvSale.setVisibility(View.VISIBLE);
            viewHolder.item_title.setText(title);
            viewHolder.tvMall.setVisibility(View.GONE);
            viewHolder.rmb1.setText("原价 ¥");
            viewHolder.price1.setText(zeroBuyBean.getBprice());
            viewHolder.tvSalebi.setVisibility(View.GONE);
            viewHolder.cpbProgresbar.setVisibility(View.GONE);
            viewHolder.bprice1.setVisibility(View.GONE);
            viewHolder.tvZerobuy.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.tvZerobuy.setBackgroundResource(R.drawable.bg_czg5);
            viewHolder.tvZerobuy.setText(zeroBuyBean.getNeedfensi() + "粉丝购买");
            if (zeroBuyBean.getQuan() != null && !zeroBuyBean.getQuan().equals("")) {
                viewHolder.llQuan.setVisibility(View.GONE);
                viewHolder.quan.setText(zeroBuyBean.getQuan());
            } else {
                viewHolder.llQuan.setVisibility(View.GONE);
            }
            if (zeroBuyBean.getZuan() != null) {
                viewHolder.zuan.setVisibility(View.GONE);
                viewHolder.zuan.setText(zeroBuyBean.getZuan());
            } else {
                viewHolder.zuan.setVisibility(View.GONE);
            }
            Glide.with(context)
                    .load(img)
                    .priority(Priority.HIGH)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .placeholder(R.mipmap.zw_img_300)
                    .into(viewHolder.item_img);
            viewHolder.llPrice1.setVisibility(View.VISIBLE);
            viewHolder.llPrice.setVisibility(View.GONE);
            viewHolder.itemlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;
                    intent = new Intent(context, ZiYingZeroBuyDetailActivty.class);
                    intent.putExtra("gid", zeroBuyBean.getGid());
                    intent.putExtra("id", zeroBuyBean.getId());
                    intent.putExtra("isOlder", "yes");
                    context.startActivity(intent);
                }
            });
            viewHolder.itemlayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    viewHolder.mCopyLayout.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            viewHolder.mCopyLayout.setVisibility(View.GONE);
                        }
                    }, 2500);
                    return true;
                }
            });
            viewHolder.mCopyLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewHolder.mCopyLayout.setVisibility(View.GONE);
                }
            });
            viewHolder.copy_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(title);
                    StringUtil.showToast(context, "复制成功");
                    viewHolder.mCopyLayout.setVisibility(View.GONE);
                }
            });
            viewHolder.copy_url.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(zeroBuyBean.getImg());
                    StringUtil.showToast(context, "复制成功");
                    viewHolder.mCopyLayout.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}