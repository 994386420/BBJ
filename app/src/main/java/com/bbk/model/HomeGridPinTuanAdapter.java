package com.bbk.model;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbk.Bean.MiaoShaBean;
import com.bbk.Bean.PinTuanBean;
import com.bbk.activity.IntentActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.WebViewActivity;
import com.bbk.resource.NewConstants;
import com.bbk.util.SharedPreferencesUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rtj on 2018/3/7.
 */

public class HomeGridPinTuanAdapter extends RecyclerView.Adapter {
    //    private List<Map<String,String>> list;
    private Context context;
    List<PinTuanBean> miaoShaBeans;
    private ClipboardManager clipboardManager;
    private LogPinTuanInterface logPinTuanInterface;

    public HomeGridPinTuanAdapter(Context context, List<PinTuanBean> miaoShaBeans) {
//        this.list = list;
        this.context = context;
        this.miaoShaBeans = miaoShaBeans;
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
                LayoutInflater.from(context).inflate(R.layout.home_list_item, parent, false));
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
        }else {
        return 0;
        }
    }
//
//    public void notifyData(List<ShopDianpuBean> beans) {
//        this.shopDianpuBeans.addAll(beans);
//        notifyDataSetChanged();
//    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_img)
        ImageView itemImg;
        @BindView(R.id.item_title)
        TextView itemTitle;
        @BindView(R.id.quan)
        TextView quan;
        @BindView(R.id.ll_quan)
        LinearLayout llQuan;
        @BindView(R.id.zuan)
        TextView zuan;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.bprice)
        TextView bprice;
        @BindView(R.id.result_item)
        LinearLayout resultItem;
        @BindView(R.id.copy_title)
        TextView copyTitle;
        @BindView(R.id.copy_url)
        TextView copyUrl;
        @BindView(R.id.copy_layout)
        LinearLayout copyLayout;

        public ViewHolder(View mView) {
            super(mView);
            ButterKnife.bind(this, mView);
        }
    }

    private void initTop(final ViewHolder viewHolder, final int position) {
        try {
            final PinTuanBean miaoShaBean = miaoShaBeans.get(position);
            viewHolder.itemTitle.setText(miaoShaBean.getTitle());
            viewHolder.bprice.setVisibility(View.VISIBLE);
            viewHolder.bprice.setText(miaoShaBean.getBprice());
            viewHolder.bprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰
            viewHolder.price.setText(miaoShaBean.getPrice());
            if (miaoShaBean.getQuan() != null && !miaoShaBean.getQuan().equals("0") && !miaoShaBean.getQuan().equals("")){
                viewHolder.quan.setText("券"+miaoShaBean.getQuan());
            }else {
                viewHolder.llQuan.setVisibility(View.GONE);
            }
            if (miaoShaBean.getZuan() != null){
                viewHolder.zuan.setText(miaoShaBean.getZuan().replace("预估",""));
            }else {
                viewHolder.zuan.setVisibility(View.GONE);
            }
            Glide.with(context)
                    .load(miaoShaBean.getImgurl())
                    .priority(Priority.HIGH)
                    .placeholder(R.mipmap.zw_img_300)
                    .into(viewHolder.itemImg);
            viewHolder.resultItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;
                    String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
                    if (TextUtils.isEmpty(userID)) {
                        logPinTuanInterface.IntentPinTuanLog(miaoShaBean.getUrl(), miaoShaBean.getTitle(),miaoShaBean.getDomain(),"0",miaoShaBean.getBprice(),miaoShaBean.getQuan(),miaoShaBean.getZuan(),"miaosha");
                    } else {
                        if ("beibei".equals(miaoShaBean.getDomain())
                                || "jd".equals(miaoShaBean.getDomain()) || "taobao".equals(miaoShaBean.getDomain())
                                || "tmall".equals(miaoShaBean.getDomain()) || "suning".equals(miaoShaBean.getDomain())) {
                            clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                            clipboardManager.setPrimaryClip(ClipData.newPlainText(null, ""));
//                        NewConstants.showdialogFlg = "1";
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
                            if (miaoShaBean.getZuan() != null) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface LogPinTuanInterface {
        void IntentPinTuanLog(String url,String title,String domain,String isczg,String bprice,String quan,String zuan,String type);
    }
}
