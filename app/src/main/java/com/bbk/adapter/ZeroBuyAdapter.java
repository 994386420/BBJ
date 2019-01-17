package com.bbk.adapter;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.logg.Logg;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ZeroBuyAdapter extends RecyclerView.Adapter {
    private Context context;
    List<ZeroBuyBean> zeroBuyBeans;


    public ZeroBuyAdapter(Context context, List<ZeroBuyBean> zeroBuyBeans) {
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
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.bprice)
        TextView bprice;
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
        @BindView(R.id.item_img1)
        ImageView itemImg1;

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
            viewHolder.tvSale.setVisibility(View.GONE);
            viewHolder.item_title.setText(title);
            viewHolder.tvMall.setVisibility(View.GONE);
            viewHolder.price.setText(zeroBuyBean.getTlj());

            if (zeroBuyBean.getBprice() != null && !zeroBuyBean.getBprice().equals("")) {
                viewHolder.bprice.setText("原价¥" + zeroBuyBean.getBprice());
            } else {
                viewHolder.bprice.setVisibility(View.GONE);
            }
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

            if (zeroBuyBean.getBili() != null) {
                viewHolder.cpbProgresbar.setMaxProgress(100);
                viewHolder.cpbProgresbar.setProgressColor(Color.parseColor("#FF8A83"));
                viewHolder.cpbProgresbar.setCurProgress(Integer.parseInt(zeroBuyBean.getBili()), 2000);
                viewHolder.tvSalebi.setText("已售" + zeroBuyBean.getBili() + "%");
            }
            if (zeroBuyBean.getBili() != null) {
                if (zeroBuyBean.getBili().equals("100")) {
                    viewHolder.itemImg1.setVisibility(View.VISIBLE);
                    viewHolder.tvZerobuy.setBackgroundResource(R.drawable.bg_czg6);
                    viewHolder.tvZerobuy.setText("抢完了");
                    viewHolder.tvZerobuy.setTextColor(context.getResources().getColor(R.color.tuiguang_color4));
                } else {
                    viewHolder.itemImg1.setVisibility(View.GONE);
                    viewHolder.tvZerobuy.setTextColor(context.getResources().getColor(R.color.white));
                    viewHolder.tvZerobuy.setBackgroundResource(R.drawable.bg_czg5);
                    viewHolder.tvZerobuy.setText("0元购");
                }
            }
            Glide.with(context)
                    .load(img)
                    .priority(Priority.HIGH)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .placeholder(R.mipmap.zw_img_300)
                    .into(viewHolder.item_img);
            viewHolder.llPrice1.setVisibility(View.GONE);
            viewHolder.llPrice.setVisibility(View.VISIBLE);
            viewHolder.tvPintuan.setText("剩余" + zeroBuyBean.getNumber());
            viewHolder.itemlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;
                    NewConstants.showdialogFlg = "1";
                    if (zeroBuyBean.getBili().equals("100")) {
                        StringUtil.showToast(context,"已经抢完了！");
                    }else {
                        if (zeroBuyBean.getZeroBuyDomain() != null){
                            if (zeroBuyBean.getZeroBuyDomain().equals("taobao")){
                                intent = new Intent(context, IntentActivity.class);
                                if (zeroBuyBean.getTitle() != null) {
                                    intent.putExtra("title", zeroBuyBean.getTitle());
                                }
                                if (zeroBuyBean.getRowkey() != null) {
                                    intent.putExtra("groupRowKey", zeroBuyBean.getRowkey());
                                }
                                intent.putExtra("isczg", "0");
                                intent.putExtra("tljid", zeroBuyBean.getId());
                                if (zeroBuyBean.getBprice() != null) {
                                    intent.putExtra("bprice", zeroBuyBean.getBprice());
                                }
                                context.startActivity(intent);
                            }else {
                                intent = new Intent(context, ZiYingZeroBuyDetailActivty.class);
                                intent.putExtra("gid", zeroBuyBean.getGid());
                                intent.putExtra("id", zeroBuyBean.getId());
                                intent.putExtra("isOlder", "no");
                                context.startActivity(intent);
                            }
                        }else {
                            intent = new Intent(context, IntentActivity.class);
                            if (zeroBuyBean.getTitle() != null) {
                                intent.putExtra("title", zeroBuyBean.getTitle());
                            }
                            if (zeroBuyBean.getRowkey() != null) {
                                intent.putExtra("groupRowKey", zeroBuyBean.getRowkey());
                            }
                            intent.putExtra("isczg", "0");
                            intent.putExtra("tljid", zeroBuyBean.getId());
                            if (zeroBuyBean.getBprice() != null) {
                                intent.putExtra("bprice", zeroBuyBean.getBprice());
                            }
                            context.startActivity(intent);
                        }
                    }
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