package com.bbk.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbk.activity.R;
import com.bbk.view.RushBuyCountDownTimerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import org.json.JSONObject;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Created by rtj on 2018/3/7.
 */

public class NewCzgAdapter extends BaseAdapter{
    private List<Map<String,String>> list;
    private Context context;
    public NewCzgAdapter(Context context, List<Map<String,String>> list){
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
            convertView = View.inflate(context, R.layout.czg_item_layout, null);
            viewHolder.item_img = convertView.findViewById(R.id.item_img);
            viewHolder.item_title = convertView.findViewById(R.id.item_title);
            viewHolder.mbidprice =convertView.findViewById(R.id.mbidprice);
            viewHolder.dianpu = convertView.findViewById(R.id.dianpu_text);
            viewHolder.mprice = convertView.findViewById(R.id.mprice);
            viewHolder.youhui = convertView.findViewById(R.id.youhui_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        try {
        Map<String,String> map = list.get(position);
        String img = map.get("imgurl");
        String title = map.get("title");
        String price = map.get("price");
//        String bidprice = map.get("bidprice");
        String dianpu = map.get("dianpu");
        String youhui = map.get("youhui");
        String mbidprice = map.get("hislowprice");
        viewHolder.item_title.setText(title);
            try {
            if (!mbidprice.isEmpty()){
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
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return convertView;
    }
    class ViewHolder{
        ImageView item_img;
        TextView item_title,mbidprice,dianpu,mprice,youhui;
    }

}
