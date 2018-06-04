package com.bbk.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.content.ClipboardManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.alibaba.baichuan.android.trade.callback.AlibcLoginCallback;
import com.bbk.Bean.NewHomeCzgBean;
import com.bbk.activity.BidBillDetailActivity;
import com.bbk.activity.BidDetailActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.WebViewActivity;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.RushBuyCountDownTimerView;
import com.bbk.view.selecttableview.SelectableTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by rtj on 2018/3/7.
 */

public class NewCzgAdapter extends RecyclerView.Adapter{
//    private List<Map<String,String>> list;
    private Context context;
    List<NewHomeCzgBean> newHomeCzgBean;
    public NewCzgAdapter(Context context, List<NewHomeCzgBean> newHomeCzgBean){
//        this.list = list;
        this.context = context;
        this.newHomeCzgBean = newHomeCzgBean;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NewCzgAdapter.ViewHolder ViewHolder = new NewCzgAdapter.ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.czg_item_layout, parent, false));
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            NewCzgAdapter.ViewHolder viewHolder = (NewCzgAdapter.ViewHolder) holder;
            initTop(viewHolder,position);
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
        return newHomeCzgBean.size();
    }

    public void notifyData(List<NewHomeCzgBean> beans){
        this.newHomeCzgBean.addAll(beans);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView item_img;
        TextView mbidprice,dianpu,mprice,youhui;
        TextView item_title,copy_title,copy_url;
        LinearLayout itemlayout,mCopyLayout;
        public ViewHolder(View mView) {
            super(mView);
          item_img = mView.findViewById(R.id.item_img);
            item_title = mView.findViewById(R.id.item_title);
            mbidprice =mView.findViewById(R.id.mbidprice);
            dianpu = mView.findViewById(R.id.dianpu_text);
           mprice =mView.findViewById(R.id.mprice);
            youhui = mView.findViewById(R.id.youhui_text);
            itemlayout = mView.findViewById(R.id.result_item);
            mCopyLayout = mView.findViewById(R.id.copy_layout);
            copy_title = mView.findViewById(R.id.copy_title);
            copy_url = mView.findViewById(R.id.copy_url);
        }
    }
    private void initTop(final NewCzgAdapter.ViewHolder viewHolder, final int position) {
        try {
//            final Map<String,String> map = list.get(position);
//            Log.i("-------", newHomeCzgBean.get(position).getImgurl()+"=====");
            String img = newHomeCzgBean.get(position).getImgurl();
            final String title = newHomeCzgBean.get(position).getTitle();
            String price = newHomeCzgBean.get(position).getPrice();
//        String bidprice = map.get("bidprice");
            String dianpu = newHomeCzgBean.get(position).getDianpu();
            String youhui =newHomeCzgBean.get(position).getYouhui();
            String mbidprice = newHomeCzgBean.get(position).getHislowprice();//最低价
            viewHolder.item_title.setText(title);
            try {
                if (mbidprice != null){
                    viewHolder.mbidprice.setText("最低价 "+mbidprice);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            viewHolder.mprice.setText("¥"+price);
            viewHolder.dianpu.setText(dianpu);
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
                    try {
//                        Log.i("淘宝","=============="+ AlibcLogin.getInstance().isLogin());
                        if (AlibcLogin.getInstance().isLogin() == true){
                            Intent intent = new Intent(context,WebViewActivity.class);
                            intent.putExtra("url",  newHomeCzgBean.get(position).getUrl());
                            intent.putExtra("title", newHomeCzgBean.get(position).getTitle());
                            context.startActivity(intent);
                        }else {
                            TaoBaoLoginandLogout();//淘宝授权登陆
                            DialogSingleUtil.dismiss(0);
                        }
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
                    ClipboardManager cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(title);
                    StringUtil.showToast(context,"复制成功");
                    viewHolder.mCopyLayout.setVisibility(View.GONE);
                }
            });
            viewHolder.copy_url.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(newHomeCzgBean.get(position).getUrl());
                    StringUtil.showToast(context,"复制成功");
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
    private void TaoBaoLoginandLogout(){
        DialogSingleUtil.show(context,"授权中...");
        final AlibcLogin alibcLogin = AlibcLogin.getInstance();
        alibcLogin.showLogin((Activity) context, new AlibcLoginCallback() {

            @Override
            public void onSuccess() {
                DialogSingleUtil.dismiss(0);
                StringUtil.showToast(context, "登录成功 ");
                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String date = sDateFormat.format(new java.util.Date());
                SharedPreferencesUtil.putSharedData(MyApplication.getApplication(),"taobao","taobaodata",date);
            }
            @Override
            public void onFailure(int code, String msg) {
                DialogSingleUtil.dismiss(0);
                StringUtil.showToast(context, "登录失败 ");
            }
        });
    }
}
