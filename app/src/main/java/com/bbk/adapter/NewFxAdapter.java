package com.bbk.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbk.activity.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import java.util.List;
import java.util.Map;

/**
 * Created by rtj on 2018/3/7.
 */

public class NewFxAdapter extends BaseAdapter{
    private List<Map<String,String>> list;
    private Context context;
    public NewFxAdapter(Context context, List<Map<String,String>> list){
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.
                    fx_item_layout, null);
            viewHolder.item_img = convertView.findViewById(R.id.item_img);
            viewHolder.item_title = convertView.findViewById(R.id.item_title);
            viewHolder.content = convertView.findViewById(R.id.content);
            viewHolder.time = convertView.findViewById(R.id.bl_time);
            viewHolder.mlike = convertView.findViewById(R.id.mlike);
            viewHolder.mcomment = convertView.findViewById(R.id.mcomment);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
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
        return convertView;
    }
    class ViewHolder{
        ImageView item_img;
        TextView item_title,mbidprice,content,time,mlike,mcomment;
    }

}
