package com.bbk.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bbk.activity.R;

import java.util.List;
import java.util.Map;

/**
 * Created by rtj on 2018/3/7.
 */

public class BidMsgInformAdapter extends BaseAdapter{
    private List<Map<String,String>> list;
    private Context context;
    public BidMsgInformAdapter(Context context, List<Map<String,String>> list){
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(context, R.layout.fragment_bid_inform_listview, null);
                viewHolder.mtitle = (TextView)convertView.findViewById(R.id.mtitle);
                viewHolder.mtime = (TextView)convertView.findViewById(R.id.mtime);
                viewHolder.mcontent = (TextView)convertView.findViewById(R.id.mcontent);
                viewHolder.muserandprice = (TextView)convertView.findViewById(R.id.muserandprice);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Map<String,String> map = list.get(position);
            String id = map.get("id");
            String roledesc = map.get("roledesc");
            String title = map.get("title");
            String isread = map.get("isread");
            String extra = map.get("extra");
            String event = map.get("event");
            String userid = map.get("userid");
            String role = map.get("role");
            String fbid = map.get("fbid");
            String head = map.get("head");
            String dtimes = map.get("dtimes");
            viewHolder.mtitle.setText(head);
            viewHolder.mtime.setText(dtimes);
            viewHolder.mcontent.setText(roledesc+":"+title);
            viewHolder.muserandprice.setText(extra+",戳我查看详情~");
//            if (isread.equals("0")){
//                viewHolder.mtitle.setTextColor(context.getResources().getColor(R.color.biao_color));
//                viewHolder.mtime.setTextColor(context.getResources().getColor(R.color.biao_color));
//                viewHolder.mcontent.setTextColor(context.getResources().getColor(R.color.biao_color));
//                viewHolder.muserandprice.setTextColor(context.getResources().getColor(R.color.biao_color));
//            }else {
//                viewHolder.mtitle.setTextColor(context.getResources().getColor(R.color.black));
//                viewHolder.mtime.setTextColor(context.getResources().getColor(R.color.black));
//                viewHolder.mcontent.setTextColor(context.getResources().getColor(R.color.black));
//                viewHolder.muserandprice.setTextColor(context.getResources().getColor(R.color.black));
//            }
            return convertView;
        } catch (Exception e) {
            e.printStackTrace();
            return convertView;
        }
    }
    class ViewHolder{
        TextView mtitle,mtime,mcontent,muserandprice;
    }

}
