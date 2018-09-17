package com.bbk.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbk.Bean.MiaoShaBean;
import com.bbk.activity.IntentActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.WebViewActivity;
import com.bbk.model.view.CustomProgressBar;
import com.bbk.util.SharedPreferencesUtil;
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

public class NewMIaoShaAdapter extends RecyclerView.Adapter {
    //    private List<Map<String,String>> list;
    private Context context;
    List<MiaoShaBean> miaoShaBeans;
    private ClipboardManager clipboardManager;
    private String state;
    private String TIME;
    private LogMiaoShaInterface logMiaoShaInterface;

    public NewMIaoShaAdapter(Context context, List<MiaoShaBean> miaoShaBeans, String state,String time) {
//        this.list = list;
        this.context = context;
        this.miaoShaBeans = miaoShaBeans;
        this.state = state;
        this.TIME = time;
    }

    public LogMiaoShaInterface getLogMiaoShaInterface() {
        return logMiaoShaInterface;
    }

    public void setLogMiaoShaInterface(LogMiaoShaInterface logMiaoShaInterface) {
        this.logMiaoShaInterface = logMiaoShaInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder ViewHolder = new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.new_czg_item_layout, parent, false));
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
        if (miaoShaBeans != null && miaoShaBeans.size() > 0) {
            return miaoShaBeans.size();
        } else {
            return 0;
        }
    }

    public void notifyData(List<MiaoShaBean> beans) {
        this.miaoShaBeans.addAll(beans);
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
        @BindView(R.id.ll_progresbar)
        LinearLayout llProgresbar;
        @BindView(R.id.tv_salebi)
        TextView tvSalebi;
        @BindView(R.id.tv_go_buy)
        TextView tvGoBuy;
        @BindView(R.id.rmb)
        TextView rmb;

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
            final MiaoShaBean miaoShaBean = miaoShaBeans.get(position);
            String img = miaoShaBean.getImgurl();
            final String title = miaoShaBean.getTitle();
            String price = miaoShaBean.getPrice();
            String mbidprice = miaoShaBean.getBprice();//最低价
            viewHolder.tvMall.setVisibility(View.GONE);
            if (state.equals("1")) {
                if (miaoShaBean.getSalebilv() != null) {
                    viewHolder.llProgresbar.setVisibility(View.VISIBLE);
                    viewHolder.cpbProgresbar.setMaxProgress(100);
                    viewHolder.cpbProgresbar.setProgressColor(Color.parseColor("#FF8A83"));
                    viewHolder.cpbProgresbar.setCurProgress(Integer.parseInt(miaoShaBean.getSalebilv()), 2000);
                    viewHolder.tvSalebi.setText("已售" + miaoShaBean.getSalebilv() + "%");
                }
                viewHolder.tvGoBuy.setVisibility(View.VISIBLE);
                if (miaoShaBean.getSalebilv().equals("100")) {
                    viewHolder.tvGoBuy.setBackgroundResource(R.drawable.bg_czg6);
                    viewHolder.tvGoBuy.setText("抢完了");
                    viewHolder.tvGoBuy.setTextColor(context.getResources().getColor(R.color.tuiguang_color4));
                } else {
                    viewHolder.tvGoBuy.setTextColor(context.getResources().getColor(R.color.white));
                    viewHolder.tvGoBuy.setBackgroundResource(R.drawable.bg_czg5);
                    viewHolder.tvGoBuy.setText("去抢购");
                    if (mbidprice != null && !mbidprice.equals("")) {
                        viewHolder.bprice.setText("¥" + mbidprice);
                        viewHolder.bprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰
                    } else {
                        viewHolder.bprice.setVisibility(View.GONE);
                    }
                    viewHolder.price.setText(price);
                    viewHolder.rmb.setTextColor(context.getResources().getColor(R.color.tuiguang_color1));
                    viewHolder.price.setTextColor(context.getResources().getColor(R.color.tuiguang_color1));
                    viewHolder.tvSale.setVisibility(View.GONE);
                }
            } else {
                viewHolder.llProgresbar.setVisibility(View.GONE);
                viewHolder.tvGoBuy.setVisibility(View.GONE);
                viewHolder.rmb.setTextColor(context.getResources().getColor(R.color.tuiguang_color9));
                viewHolder.price.setTextColor(context.getResources().getColor(R.color.tuiguang_color9));
                viewHolder.tvSale.setVisibility(View.VISIBLE);
                viewHolder.tvSale.setTextColor(context.getResources().getColor(R.color.tuiguang_color9));
                viewHolder.tvSale.setText("今天"+TIME+"开抢");
                if (mbidprice != null && !mbidprice.equals("")) {
                    viewHolder.bprice.setText("¥" + mbidprice);
                    viewHolder.bprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰
                } else {
                    viewHolder.bprice.setVisibility(View.GONE);
                }
                viewHolder.price.setText(price);
                viewHolder.itemlayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
            }
                viewHolder.itemlayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent;
                        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
                        if (TextUtils.isEmpty(userID)) {
                            logMiaoShaInterface.IntentMiaoShaLog(miaoShaBean.getUrl(), miaoShaBean.getTitle(),miaoShaBean.getDomain(),"0",miaoShaBean.getBprice(),miaoShaBean.getQuan(),miaoShaBean.getZuan(),"miaosha");
                        } else {
                            if ("beibei".equals(miaoShaBean.getDomain())
                                    || "jd".equals(miaoShaBean.getDomain()) || "taobao".equals(miaoShaBean.getDomain())
                                    || "tmall".equals(miaoShaBean.getDomain()) || "suning".equals(miaoShaBean.getDomain())) {
                                clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                clipboardManager.setPrimaryClip(ClipData.newPlainText(null, ""));
                                intent = new Intent(context, IntentActivity.class);
                                if (miaoShaBean.getUrl() != null) {
                                    intent.putExtra("url", miaoShaBean.getUrl());
                                }
                                if (miaoShaBean.getTitle() != null) {
                                    intent.putExtra("title", miaoShaBean.getTitle());
                                }
                                if (miaoShaBean.getDomain() != null) {
                                    intent.putExtra("domain", miaoShaBean.getDomain());
                                }
                                intent.putExtra("isczg", "0");
                                if (miaoShaBean.getBprice() != null) {
                                    intent.putExtra("bprice", miaoShaBean.getBprice());
                                }
                                if (miaoShaBean.getQuan() != null && !miaoShaBean.getQuan().equals("0")) {
                                    intent.putExtra("quan", miaoShaBean.getQuan());
                                }
                                if (miaoShaBean.getQuan() != null) {
                                    intent.putExtra("zuan", miaoShaBean.getZuan());
                                }
                                intent.putExtra("type", "miaosha");
                            } else {
                                intent = new Intent(context, WebViewActivity.class);
                                intent.putExtra("url", miaoShaBean.getUrl());
                                intent.putExtra("title", miaoShaBean.getTitle());
                            }
                            context.startActivity(intent);
                        }
                    }
                });
//            viewHolder.tvSale.setText(newHomeCzgBean.get(position).getSale() + "人付款");
            viewHolder.item_title.setText(title);
            if (mbidprice != null) {
                viewHolder.mbidprice.setText("最低价 " + mbidprice);
            }

            if (miaoShaBean.getQuan() != null && !miaoShaBean.getQuan().equals("0") && !miaoShaBean.getQuan().equals("")) {
                viewHolder.llQuan.setVisibility(View.VISIBLE);
                viewHolder.quan.setText(miaoShaBean.getQuan());
            } else {
                viewHolder.llQuan.setVisibility(View.GONE);
            }
            if (miaoShaBean.getZuan() != null) {
                viewHolder.zuan.setVisibility(View.VISIBLE);
                viewHolder.zuan.setBackgroundResource(R.drawable.bg_czg4);
                viewHolder.zuan.setTextColor(context.getResources().getColor(R.color.tuiguang_color1));
                viewHolder.zuan.setText(miaoShaBean.getZuan());
            } else {
                viewHolder.zuan.setVisibility(View.GONE);
            }
            viewHolder.mprice.setText("¥" + price);
            Glide.with(context)
                    .load(img)
                    .priority(Priority.HIGH)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .placeholder(R.mipmap.zw_img_300)
                    .into(viewHolder.item_img);

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
                    cm.setText(miaoShaBean.getUrl());
                    StringUtil.showToast(context, "复制成功");
                    viewHolder.mCopyLayout.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public interface LogMiaoShaInterface {
        void IntentMiaoShaLog(String url,String title,String domain,String isczg,String bprice,String quan,String zuan,String type);
    }
}
