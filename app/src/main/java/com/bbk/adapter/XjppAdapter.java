package com.bbk.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
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
import com.bbk.view.selecttableview.SelectableTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by rtj on 2018/3/7.
 */

public class XjppAdapter extends BaseAdapter{
    private List<Map<String,String>> list;
    private Context context;
    public XjppAdapter(Context context, List<Map<String,String>> list){
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
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.xjpp_layou, null);
            viewHolder.item_img = convertView.findViewById(R.id.item_img);
            viewHolder.mprice = convertView.findViewById(R.id.mprice);
            viewHolder.mtitle = convertView.findViewById(R.id.mTitle);
            viewHolder.itemlayout =  convertView.findViewById(R.id.result_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        try {
//            viewHolder.itemlayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                        Intent intent = new Intent(context, WebViewActivity.class);
//                        intent.putExtra("url", list.get(position).get("url"));
//                        intent.putExtra("groupRowKey", list.get(position).get("rowkey"));
//                        context.startActivity(intent);
//                }
//            });
        Map<String,String> map = list.get(position);
        String img = map.get("imgUrl");
        String price = map.get("price");
        String title = map.get("title");
        viewHolder.mprice.setText(price);
        viewHolder.mtitle.setText(title);
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
        TextView mprice,mtitle;
        LinearLayout itemlayout;
    }
}
