package com.bbk.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
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
import com.bbk.Bean.PinTuanBean;
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

public class NewPinTuanAdapter extends RecyclerView.Adapter {

    //    private List<Map<String,String>> list;
    private Context context;
    List<PinTuanBean> newHomeCzgBean;
    private ClipboardManager clipboardManager;
    private int drawS;
    private LogPinTuanInterface logPinTuanInterface;

    public NewPinTuanAdapter(Context context, List<PinTuanBean> newHomeCzgBean) {
//        this.list = list;
        this.context = context;
        this.newHomeCzgBean = newHomeCzgBean;
    }

    public LogPinTuanInterface getLogPinTuanInterface() {
        return logPinTuanInterface;
    }

    public void setLogPinTuanInterface(LogPinTuanInterface logPinTuanInterface) {
        this.logPinTuanInterface = logPinTuanInterface;
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
        if (newHomeCzgBean != null && newHomeCzgBean.size() > 0) {
            return newHomeCzgBean.size();
        } else {
            return 0;
        }
    }

    public void notifyData(List<PinTuanBean> beans) {
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
        @BindView(R.id.price1)
        TextView price;
        @BindView(R.id.bprice1)
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
            final PinTuanBean pinTuanBean = newHomeCzgBean.get(position);
            String img = newHomeCzgBean.get(position).getImgurl();
            final String title = newHomeCzgBean.get(position).getTitle();
            String price = newHomeCzgBean.get(position).getPrice();
            String mbidprice = newHomeCzgBean.get(position).getBprice();//最低价
            viewHolder.tvSale.setVisibility(View.GONE);
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
            } else {
                viewHolder.bprice.setVisibility(View.GONE);
            }
            viewHolder.price.setText(price);
            if (newHomeCzgBean.get(position).getQuan() != null) {
                viewHolder.llQuan.setVisibility(View.VISIBLE);
                viewHolder.quan.setText(newHomeCzgBean.get(position).getQuan());
            } else {
                viewHolder.llQuan.setVisibility(View.GONE);
            }
            if (newHomeCzgBean.get(position).getZuan() != null) {
                viewHolder.zuan.setVisibility(View.VISIBLE);
                viewHolder.zuan.setText(newHomeCzgBean.get(position).getZuan());
            } else {
                viewHolder.zuan.setVisibility(View.GONE);
            }
//            viewHolder.dianpuText.setText(dianpu);
            viewHolder.mprice.setText("¥" + price);
//            viewHolder.youhui.setText(youhui);
            Glide.with(context)
                    .load(img)
                    .priority(Priority.HIGH)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .placeholder(R.mipmap.zw_img_300)
                    .into(viewHolder.item_img);


            if (pinTuanBean.getYipin() != null) {
                viewHolder.llPintuan.setVisibility(View.VISIBLE);
                viewHolder.tvPintuan.setText(pinTuanBean.getYipin() + "人已拼团");
            } else {
                viewHolder.llPintuan.setVisibility(View.GONE);
            }

            viewHolder.llPrice1.setVisibility(View.VISIBLE);
            viewHolder.llPrice.setVisibility(View.GONE);


            viewHolder.itemlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    notifyDataSetChanged();
                    Intent intent;
                    String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
                    if (TextUtils.isEmpty(userID)) {
                        logPinTuanInterface.IntentPinTuanLog(pinTuanBean.getUrl(), pinTuanBean.getTitle(), pinTuanBean.getDomain(), "0", pinTuanBean.getBprice(), pinTuanBean.getQuan(),pinTuanBean.getZuan() ,"miaosha");
                    } else {
                        if ("beibei".equals(pinTuanBean.getDomain())
                                || "jd".equals(pinTuanBean.getDomain()) || "taobao".equals(pinTuanBean.getDomain())
                                || "tmall".equals(pinTuanBean.getDomain()) || "suning".equals(pinTuanBean.getDomain())) {
                            clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                            clipboardManager.setPrimaryClip(ClipData.newPlainText(null, ""));
                            intent = new Intent(context, IntentActivity.class);
                            if (pinTuanBean.getUrl() != null) {
                                intent.putExtra("url", pinTuanBean.getUrl());
                            }
                            if (pinTuanBean.getTitle() != null) {
                                intent.putExtra("title", pinTuanBean.getTitle());
                            }
                            if (pinTuanBean.getDomain() != null) {
                                intent.putExtra("domain", pinTuanBean.getDomain());
                            }
                            intent.putExtra("isczg", "0");
                            if (pinTuanBean.getBprice() != null) {
                                intent.putExtra("bprice", pinTuanBean.getBprice());
                            }
                            if (pinTuanBean.getQuan() != null && !pinTuanBean.getQuan().equals("0")) {
                                intent.putExtra("quan", pinTuanBean.getQuan());
                            }
                            if (pinTuanBean.getZuan() != null) {
                                intent.putExtra("zuan", pinTuanBean.getZuan());
                            }
                            intent.putExtra("type", "miaosha");
                        } else {
                            intent = new Intent(context, WebViewActivity.class);
                            intent.putExtra("url", pinTuanBean.getUrl());
                            intent.putExtra("title", pinTuanBean.getTitle());
                        }
                        context.startActivity(intent);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface LogPinTuanInterface {
        void IntentPinTuanLog(String url,String title,String domain,String isczg,String bprice,String quan,String zuan ,String type);
    }
}
