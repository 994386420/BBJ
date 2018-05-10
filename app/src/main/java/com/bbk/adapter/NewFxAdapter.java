package com.bbk.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.bbk.Decoration.TwoDecoration;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.WebViewActivity;
import com.bbk.activity.WebViewWZActivity;
import com.bbk.dialog.HomeAlertDialog;
import com.bbk.flow.DataFlow;
import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.util.EventIdIntentUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.selecttableview.SelectableTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rtj on 2018/3/7.
 */

public class NewFxAdapter extends RecyclerView.Adapter implements ResultEvent{
    private List<Map<String,String>> list;
    private Context context;
    private String wztitle = "";
    private DataFlow6 dataFlow;
    public NewFxAdapter(Context context, List<Map<String,String>> list){
        this.list = list;
        this.context = context;
        this.dataFlow = new DataFlow6(context);
    }
//    @Override
//    public int getCount() {
//        return list.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return list.get(position);
//    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NewFxAdapter.ViewHolder ViewHolder = new NewFxAdapter.ViewHolder(
                LayoutInflater.from(context).inflate(R.layout. fx_item_layout, parent, false));
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            NewFxAdapter.ViewHolder viewHolder = (NewFxAdapter.ViewHolder) holder;
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
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder viewHolder;
//        if (convertView == null) {
//            viewHolder = new ViewHolder();
//            convertView = View.inflate(context, R.layout.
//                    fx_item_layout, null);
//            viewHolder.item_img = convertView.findViewById(R.id.item_img);
//            viewHolder.item_title = convertView.findViewById(R.id.item_title);
//            viewHolder.content = convertView.findViewById(R.id.content);
//            viewHolder.time = convertView.findViewById(R.id.bl_time);
//            viewHolder.mlike = convertView.findViewById(R.id.mlike);
//            viewHolder.mcomment = convertView.findViewById(R.id.mcomment);
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//        try {
//            Map<String,String> map = list.get(position);
//            String content = map.get("content");
//            String img = map.get("img");
//            String time = map.get("atime");
//            String title = map.get("title");
//            String count = map.get("count");
//            String zan = map.get("zan");
//            viewHolder.item_title.setText(title);
//            viewHolder.time.setText(time);
//            viewHolder.content.setText(content);
//            viewHolder.mlike.setText(zan);
//            viewHolder.mcomment.setText(count);
//            Glide.with(context)
//                    .load(img)
//                    .priority(Priority.HIGH)
//                    .placeholder(R.mipmap.zw_img_300)
//                    .into(viewHolder.item_img);
//        }catch (Exception e){
//
//        }
//        return convertView;
//    }

    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
        switch (requestCode){
            case 4:
                Intent intent = new Intent(context, WebViewWZActivity.class);
                intent.putExtra("url", content);
                intent.putExtra("title", wztitle);
                context.startActivity(intent);
                break;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView item_img;
        TextView item_title,mbidprice,content,time,mlike,mcomment;
        LinearLayout itemlayout;
        public ViewHolder(View mView) {
            super(mView);
            item_img = mView.findViewById(R.id.item_img);
           item_title = mView.findViewById(R.id.item_title);
           content = mView.findViewById(R.id.content);
           time = mView.findViewById(R.id.bl_time);
           mlike = mView.findViewById(R.id.mlike);
            mcomment = mView.findViewById(R.id.mcomment);
            itemlayout = mView.findViewById(R.id.result_item);
        }
    }
    private void initTop(NewFxAdapter.ViewHolder viewHolder, final int position) {
        try {
            Map<String,String> map = list.get(position);
            String content = map.get("content");
            String img = map.get("img");
            String time = map.get("atime");
            String title = map.get("title");
            String count = map.get("count");
            String zan = map.get("zan");
            viewHolder.item_title.setText(title);
            viewHolder.time.setText(time);
            viewHolder.content.setText(content);
            viewHolder.mlike.setText(zan);
            viewHolder.mcomment.setText(count);
            Glide.with(context)
                    .load(img)
                    .priority(Priority.HIGH)
                    .placeholder(R.mipmap.zw_img_300)
                    .into(viewHolder.item_img);
            viewHolder.itemlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        insertWenzhangGuanzhu(position);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertWenzhangGuanzhu(int position) {
        try {
            wztitle  = list.get(position).get("title");
            String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
            String token = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "token");
            if (!TextUtils.isEmpty(userID)) {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("userid", userID);
                params.put("wzid",  list.get(position).get("id"));
                params.put("token", token);
                params.put("type", "2");
                dataFlow.requestData(4, "newService/insertWenzhangGuanzhu", params, this);
            } else {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("userid", "-1");
                params.put("token", token);
                params.put("wzid",  list.get(position).get("id"));
                params.put("type", "2");
                dataFlow.requestData(4, "newService/insertWenzhangGuanzhu", params, this);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
