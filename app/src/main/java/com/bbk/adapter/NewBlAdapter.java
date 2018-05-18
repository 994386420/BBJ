package com.bbk.adapter;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.bbk.activity.GossipPiazzaDetailActivity;
import com.bbk.activity.R;
import com.bbk.activity.WebViewActivity;
import com.bbk.util.StringUtil;
import com.bbk.view.RushBuyCountDownTimerView;
import com.bbk.view.selecttableview.SelectableTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Created by rtj on 2018/3/7.
 */

public class NewBlAdapter extends RecyclerView.Adapter{
    private List<Map<String,String>> list;
    private Context context;
    public NewBlAdapter(Context context, List<Map<String,String>> list){
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NewBlAdapter.ViewHolder ViewHolder = new NewBlAdapter.ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.bl_item_layout, parent, false));
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            NewBlAdapter.ViewHolder viewHolder = (NewBlAdapter.ViewHolder) holder;
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
        return list.size();
    }

    public void notifyData(List<Map<String,String>> List){
        this.list.addAll(List);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView item_img;
        TextView item_title,mbidprice,mExtr,time,mlike,mcomment;
        TextView copy_title,copy_url;
        LinearLayout itemlayout,mCopyLayout;
        public ViewHolder(View mView) {
            super(mView);
            item_img = (ImageView)mView.findViewById(R.id.item_img);
            item_title = (TextView)mView.findViewById(R.id.item_title);
            mExtr = (TextView)mView.findViewById(R.id.mExtra);
            time = (TextView)mView.findViewById(R.id.bl_time);
            mlike = mView.findViewById(R.id.mlike);
            mcomment = mView.findViewById(R.id.mcomment);
            itemlayout = mView.findViewById(R.id.result_item);
            mCopyLayout = mView.findViewById(R.id.copy_layout);
            copy_title = mView.findViewById(R.id.copy_title);
            copy_url = mView.findViewById(R.id.copy_url);
        }
    }
    private void initTop(final NewBlAdapter.ViewHolder viewHolder, final int position) {
        try {
            final Map<String,String> map = list.get(position);
            String mExtr = map.get("extra");
            String img = map.get("img");
            String time = map.get("dtime");
            final String title = map.get("title");
            String count = map.get("plnum");
            String readnum = map.get("readnum");//阅读数
            String zan = map.get("zannum");
            final String content = map.get("content");
            viewHolder.item_title.setText(content);
            viewHolder.time.setText(time);
            viewHolder.mExtr.setText("¥"+map.get("price")+", "+ mExtr);
            viewHolder.mlike.setText(zan);
            viewHolder.mcomment.setText(readnum);
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
                            Intent intent = new Intent(context, GossipPiazzaDetailActivity.class);
                            intent.putExtra("blid",list.get(position).get("blid"));
                            context.startActivity(intent);
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
                    cm.setText(map.get("url"));
                    StringUtil.showToast(context,"复制成功");
                    viewHolder.mCopyLayout.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
