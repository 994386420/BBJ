package com.bbk.adapter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.alibaba.baichuan.android.trade.callback.AlibcLoginCallback;
import com.bbk.Bean.NewHomeCzgBean;
import com.bbk.activity.IntentActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.WebViewActivity;
import com.bbk.resource.NewConstants;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.JumpIntentUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.logg.Logg;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rtj on 2018/3/7.
 */

public class NewCzgAdapter extends RecyclerView.Adapter {
    //    private List<Map<String,String>> list;
    private Context context;
    List<NewHomeCzgBean> newHomeCzgBean;
    private ClipboardManager clipboardManager;
    private int drawS;

    public NewCzgAdapter(Context context, List<NewHomeCzgBean> newHomeCzgBean) {
//        this.list = list;
        this.context = context;
        this.newHomeCzgBean = newHomeCzgBean;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder ViewHolder = new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.czg_item_layout, parent, false));
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
        if (newHomeCzgBean != null && newHomeCzgBean.size() > 0) {
            return newHomeCzgBean.size();
        }else {
            return 0;
        }
    }

    public void notifyData(List<NewHomeCzgBean> beans) {
        this.newHomeCzgBean.addAll(beans);
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
            String img = newHomeCzgBean.get(position).getImgurl();
            final String title = newHomeCzgBean.get(position).getTitle();
            String price = newHomeCzgBean.get(position).getPrice();
            String dianpu = newHomeCzgBean.get(position).getDianpu();
            String youhui = newHomeCzgBean.get(position).getYouhui();
            String mbidprice = newHomeCzgBean.get(position).getHislowprice();//最低价
            viewHolder.tvSale.setVisibility(View.VISIBLE);
            viewHolder.tvSale.setText(newHomeCzgBean.get(position).getSale() + "人付款");
            viewHolder.item_title.setText("      " + title);
            try {
                if (mbidprice != null) {
                    viewHolder.mbidprice.setText("最低价 " + mbidprice);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            String domin = newHomeCzgBean.get(position).getDomain();
            if (domin != null) {
                if (domin.equals("taobao")) {
//                    viewHolder.tvMall.setText("淘宝");
                    drawS = context.getResources().getIdentifier("home_logo_03", "mipmap", context.getPackageName());
                } else if (domin.equals("tmall")) {
//                    viewHolder.tvMall.setText("天猫");
                    drawS = context.getResources().getIdentifier("home_logo_01", "mipmap", context.getPackageName());
                } else {
//                    viewHolder.tvMall.setText("京东");
                    drawS = context.getResources().getIdentifier("home_logo_02", "mipmap", context.getPackageName());
                }
                viewHolder.tvMall.setBackgroundResource(drawS);
            } else {
                viewHolder.tvMall.setVisibility(View.GONE);
            }
            if (newHomeCzgBean.get(position).getBprice() != null && !newHomeCzgBean.get(position).getBprice().equals("")) {
                viewHolder.bprice.setText("¥" + newHomeCzgBean.get(position).getBprice());
                viewHolder.bprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰
            }else {
                viewHolder.bprice.setVisibility(View.GONE);
            }
            viewHolder.price.setText(price);
            if (newHomeCzgBean.get(position).getQuan1() !=null){
                viewHolder.llQuan.setVisibility(View.VISIBLE);
                viewHolder.quan.setText(newHomeCzgBean.get(position).getQuan1());
            }else {
                viewHolder.llQuan.setVisibility(View.GONE);
            }
            if (newHomeCzgBean.get(position).getZuan() != null) {
                viewHolder.zuan.setVisibility(View.VISIBLE);
                viewHolder.zuan.setText(newHomeCzgBean.get(position).getZuan());
            } else {
                viewHolder.zuan.setVisibility(View.GONE);
            }
            viewHolder.dianpuText.setText(dianpu);
            viewHolder.mprice.setText("¥" + price);
            viewHolder.youhui.setText(youhui);
            Glide.with(context)
                    .load(img)
                    .priority(Priority.HIGH)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .placeholder(R.mipmap.zw_img_300)
                    .into(viewHolder.item_img);
            viewHolder.itemlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notifyDataSetChanged();
                    Intent intent;
                    try {
//                        if (AlibcLogin.getInstance().isLogin() == true) {
                        if (JumpIntentUtil.isJump5(newHomeCzgBean, position)) {
                            clipboardManager = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                            clipboardManager.setPrimaryClip(ClipData.newPlainText(null, ""));
                            NewConstants.showdialogFlg = "1";
                            intent = new Intent(context, IntentActivity.class);
                            if (newHomeCzgBean.get(position).getRequestUrl() != null) {
                                intent.putExtra("url", newHomeCzgBean.get(position).getRequestUrl());
                            }
                            if (newHomeCzgBean.get(position).getTitle() != null) {
                                intent.putExtra("title", newHomeCzgBean.get(position).getTitle());
                            }
                            if (newHomeCzgBean.get(position).getDomain() != null) {
                                intent.putExtra("domain", newHomeCzgBean.get(position).getDomain());
                            }
                            if (newHomeCzgBean.get(position).getRowkey() != null) {
                                intent.putExtra("groupRowKey", newHomeCzgBean.get(position).getRowkey());
                            }
                            intent.putExtra("isczg", "1");
                            if (newHomeCzgBean.get(position).getBprice() != null) {
                                intent.putExtra("bprice", newHomeCzgBean.get(position).getBprice());
                            }
                        } else {
                            intent = new Intent(context, WebViewActivity.class);
                            intent.putExtra("url", newHomeCzgBean.get(position).getUrl());
                            intent.putExtra("title", newHomeCzgBean.get(position).getTitle());
                        }
                        context.startActivity(intent);
//                        } else {
//                            DialogSingleUtil.show(context, "授权中...");
//                            TaoBaoLoginandLogout();//淘宝授权登陆
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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
                    cm.setText(newHomeCzgBean.get(position).getRequestUrl());
                    StringUtil.showToast(context, "复制成功");
                    viewHolder.mCopyLayout.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 淘宝授权登录
     */
    private void TaoBaoLoginandLogout() {
        final AlibcLogin alibcLogin = AlibcLogin.getInstance();
        alibcLogin.showLogin((Activity) context, new AlibcLoginCallback() {

            @Override
            public void onSuccess() {
                DialogSingleUtil.dismiss(0);
                StringUtil.showToast(context, "登录成功 ");
                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String date = sDateFormat.format(new Date());
                SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "taobao", "taobaodata", date);
            }

            @Override
            public void onFailure(int code, String msg) {
                DialogSingleUtil.dismiss(0);
                StringUtil.showToast(context, "登录失败 ");
            }
        });
        DialogSingleUtil.dismiss(0);
    }
}
