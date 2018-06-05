package com.bbk.adapter;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.alibaba.baichuan.android.trade.callback.AlibcLoginCallback;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.WebViewActivity;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
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

public class SsNewCzgAdapter extends BaseAdapter{
    private List<Map<String,String>> list;
    private Context context;
    public SsNewCzgAdapter(Context context, List<Map<String,String>> list){
        this.list = list;
        this.context = context;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void notifyData(List<Map<String,String>> List){
        this.list.addAll(List);
        notifyDataSetChanged();
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.czg_item_layout, null);
            viewHolder.item_img = convertView.findViewById(R.id.item_img);
            viewHolder.item_title = convertView.findViewById(R.id.item_title);
            viewHolder.mbidprice =convertView.findViewById(R.id.mbidprice);
            viewHolder.dianpu = convertView.findViewById(R.id.dianpu_text);
            viewHolder.mprice = convertView.findViewById(R.id.mprice);
            viewHolder.youhui = convertView.findViewById(R.id.youhui_text);
            viewHolder.itemlayout =  convertView.findViewById(R.id.result_item);
            viewHolder. mCopyLayout =convertView.findViewById(R.id.copy_layout);
            viewHolder.copy_title = convertView.findViewById(R.id.copy_title);
            viewHolder.copy_url = convertView.findViewById(R.id.copy_url);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        try {
        final Map<String,String> map = list.get(position);
        String img = map.get("imgurl");
        final String title = map.get("title");
        String price = map.get("price");
//        String bidprice = map.get("bidprice");
        String dianpu = map.get("dianpu");
        String youhui = map.get("youhui");
        String mbidprice = map.get("hislowprice");
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
            viewHolder.itemlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notifyDataSetChanged();
                    if (AlibcLogin.getInstance().isLogin() == true){
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("url", list.get(position).get("url"));
                        intent.putExtra("title", list.get(position).get("title"));
                        context.startActivity(intent);
                    } else {
                        TaoBaoLoginandLogout();//淘宝授权登陆
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
            viewHolder.copy_title.setBackgroundResource(R.drawable.bid_circle);
            viewHolder.copy_url.setBackgroundResource(R.drawable.bid_circle);
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
                    cm.setText(map.get("url"));
                    StringUtil.showToast(context,"复制成功");
                    viewHolder.mCopyLayout.setVisibility(View.GONE);
                }
            });
        Glide.with(context)
                    .load(img)
                    .priority(Priority.HIGH)
                    .placeholder(R.mipmap.zw_img_300)
                    .into(viewHolder.item_img);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return convertView;
    }
    class ViewHolder{
        ImageView item_img;
        TextView mbidprice,dianpu,mprice,youhui;
        TextView item_title,copy_title,copy_url;
        LinearLayout itemlayout,mCopyLayout;
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
