package com.bbk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbk.activity.GossipCommentDetailActivity;
import com.bbk.activity.R;
import com.bbk.view.CircleImageView1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by rtj on 2018/2/27.
 */

public class GossipCommentListViewAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String,String>> list;



    public GossipCommentListViewAdapter(Context context, List<Map<String,String>> list){
        this.context = context;
        this.list = list;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = View.inflate(context, R.layout.gossip_comment_listview, null);
            vh.mimg = (ImageView) convertView.findViewById(R.id.mimg);
            vh.mname = (TextView) convertView.findViewById(R.id.mname);
            vh.mtime = (TextView) convertView.findViewById(R.id.mtime);
            vh.mcontent = (TextView) convertView.findViewById(R.id.mcontent);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        if (list.size()>0){
            Map<String, String> map = list.get(position);
            String content = map.get("content");
            String time = map.get("time");
            String rename = map.get("rename");
            String name = map.get("name");
            String img = map.get("img");
            vh.mtime.setText(time);
            vh.mname.setText(name);
            if (rename!= null){
                int length = rename.length();
                SpannableString spannableString = new SpannableString("回复 "+rename+"： "+content);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#0099EE"));
                spannableString.setSpan(colorSpan, 3, 3+length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                vh.mcontent.setText(spannableString);
            }else {
                vh.mcontent.setText(content);
            }

            CircleImageView1.getImg(context,img,vh.mimg);
        }
        return convertView;
    }
    class ViewHolder {
        TextView mname,mtime,mcontent;
        ImageView mimg;
    }
}
