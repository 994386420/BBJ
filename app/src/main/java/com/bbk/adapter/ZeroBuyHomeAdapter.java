package com.bbk.adapter;

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
import com.bbk.Bean.ShopDianpuBean;
import com.bbk.Bean.ZeroBuyBean;
import com.bbk.activity.IntentActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.ShopDetailActivty;
import com.bbk.activity.WebViewActivity;
import com.bbk.activity.ZiYingZeroBuyDetailActivty;
import com.bbk.adapter.FenXiangListAdapter;
import com.bbk.resource.NewConstants;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rtj on 2018/3/7.
 */

public class ZeroBuyHomeAdapter extends RecyclerView.Adapter {
    //    private List<Map<String,String>> list;
    private Context context;
    List<ZeroBuyBean> zeroBuyBeans;
    private ClipboardManager clipboardManager;
    private LogInterface logInterface;

    public ZeroBuyHomeAdapter(Context context, List<ZeroBuyBean> zeroBuyBeans) {
//        this.list = list;
        this.context = context;
        this.zeroBuyBeans = zeroBuyBeans;
    }
    public LogInterface getLogInterface() {
        return logInterface;
    }

    public void setLogInterface(LogInterface logInterface) {
        this.logInterface = logInterface;
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
        if (zeroBuyBeans != null && zeroBuyBeans.size() > 0) {
            return zeroBuyBeans.size();
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
            final ZeroBuyBean miaoShaBean = zeroBuyBeans.get(position);
            viewHolder.itemTitle.setText(miaoShaBean.getTitle());
            viewHolder.bprice.setVisibility(View.VISIBLE);
            viewHolder.bprice.setText(miaoShaBean.getBprice());
            viewHolder.bprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰
            viewHolder.price.setText(miaoShaBean.getPrice());
            if (miaoShaBean.getQuan() != null && !miaoShaBean.getQuan().equals("0") && !miaoShaBean.getQuan().equals("")){
                viewHolder.quan.setText("券"+miaoShaBean.getQuan());
                viewHolder.llQuan.setVisibility(View.GONE);
            }else {
                viewHolder.llQuan.setVisibility(View.GONE);
            }
            if (miaoShaBean.getZuan() != null){
                viewHolder.zuan.setText(miaoShaBean.getZuan().replace("预估",""));
                viewHolder.zuan.setVisibility(View.GONE);
            }else {
                viewHolder.zuan.setVisibility(View.GONE);
            }
            Glide.with(context)
                    .load(miaoShaBean.getImg())
                    .priority(Priority.HIGH)
                    .placeholder(R.mipmap.zw_img_300)
                    .into(viewHolder.itemImg);
            if (miaoShaBean.getBili().equals("100")){
                viewHolder.resultItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        StringUtil.showToast(context,"已经抢完了！");
                    }
                });
            }else {
                viewHolder.resultItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent;
                        NewConstants.showdialogFlg = "1";
                        if (miaoShaBean.getZeroBuyDomain() != null){
                            if (miaoShaBean.getZeroBuyDomain().equals("taobao")){
                                intent = new Intent(context, IntentActivity.class);
                                if (miaoShaBean.getTitle() != null) {
                                    intent.putExtra("title", miaoShaBean.getTitle());
                                }
                                if (miaoShaBean.getRowkey() != null) {
                                    intent.putExtra("groupRowKey", miaoShaBean.getRowkey());
                                }
                                intent.putExtra("isczg", "0");
                                intent.putExtra("tljid", miaoShaBean.getId());
                                if (miaoShaBean.getBprice() != null) {
                                    intent.putExtra("bprice", miaoShaBean.getBprice());
                                }
                                context.startActivity(intent);
                            }else {
                                intent = new Intent(context, ZiYingZeroBuyDetailActivty.class);
                                intent.putExtra("gid", miaoShaBean.getGid());
                                intent.putExtra("id", miaoShaBean.getId());
                                intent.putExtra("isOlder", "no");
                                context.startActivity(intent);
                            }
                        }else {
                            intent = new Intent(context, IntentActivity.class);
                            if (miaoShaBean.getTitle() != null) {
                                intent.putExtra("title", miaoShaBean.getTitle());
                            }
                            if (miaoShaBean.getRowkey() != null) {
                                intent.putExtra("groupRowKey", miaoShaBean.getRowkey());
                            }
                            intent.putExtra("isczg", "0");
                            intent.putExtra("tljid", miaoShaBean.getId());
                            if (miaoShaBean.getBprice() != null) {
                                intent.putExtra("bprice", miaoShaBean.getBprice());
                            }
                            context.startActivity(intent);
                        }
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface LogInterface {
        void IntentLog(String url,String title,String domain,String isczg,String bprice,String quan,String zuan,String type);
    }
}
