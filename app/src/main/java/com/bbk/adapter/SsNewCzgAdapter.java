package com.bbk.adapter;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rtj on 2018/3/7.
 */

public class SsNewCzgAdapter extends BaseAdapter {
    private List<NewHomeCzgBean> newHomeCzgBean;
    private Context context;
    private int drawS;

    public SsNewCzgAdapter(Context context, List<NewHomeCzgBean> list) {
        this.newHomeCzgBean = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return newHomeCzgBean.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void notifyData(List<NewHomeCzgBean> List){
        this.newHomeCzgBean.addAll(List);
        notifyDataSetChanged();
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.czg_item_layout, null);
            viewHolder = new ViewHolder(convertView);
            ButterKnife.bind(this, convertView);
            viewHolder.item_img = convertView.findViewById(R.id.item_img);
            viewHolder.item_title = convertView.findViewById(R.id.item_title);
            viewHolder.mbidprice = convertView.findViewById(R.id.mbidprice);
            viewHolder.dianpu = convertView.findViewById(R.id.dianpu_text);
            viewHolder.mprice = convertView.findViewById(R.id.mprice);
            viewHolder.youhui = convertView.findViewById(R.id.youhui_text);
            viewHolder.itemlayout = convertView.findViewById(R.id.result_item);
            viewHolder.mCopyLayout = convertView.findViewById(R.id.copy_layout);
            viewHolder.copy_title = convertView.findViewById(R.id.copy_title);
            viewHolder.copy_url = convertView.findViewById(R.id.copy_url);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        try {
            String img = newHomeCzgBean.get(position).getImgurl();
            final String title = newHomeCzgBean.get(position).getTitle();
            String price = newHomeCzgBean.get(position).getPrice();
            String dianpu = newHomeCzgBean.get(position).getDianpu();
            String youhui = newHomeCzgBean.get(position).getYouhui();
            String mbidprice = newHomeCzgBean.get(position).getHislowprice();//最低价
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
            if (newHomeCzgBean.get(position).getQuan1() != null && !newHomeCzgBean.get(position).getQuan1().equals("")) {
                viewHolder.llQuan.setVisibility(View.VISIBLE);
                viewHolder.quan.setText(newHomeCzgBean.get(position).getQuan1());
            } else {
                viewHolder.llQuan.setVisibility(View.GONE);
            }
            if (newHomeCzgBean.get(position).getZuan() != null && !newHomeCzgBean.get(position).getZuan().equals("")) {
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
                    cm.setText(newHomeCzgBean.get(position).getUrl());
                    StringUtil.showToast(context, "复制成功");
                    viewHolder.mCopyLayout.setVisibility(View.GONE);
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return convertView;
    }

//    static class ViewHolder {
//
//    }

    /**
     * 淘宝授权登录
     */
    private void TaoBaoLoginandLogout() {
        DialogSingleUtil.show(context, "授权中...");
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
    }

    class ViewHolder {
        ImageView item_img;
        TextView mbidprice, dianpu, mprice, youhui;
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

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
