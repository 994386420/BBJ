package com.bbk.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbk.activity.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderDetailStatusAdapter extends BaseAdapter {
    List<Object> timeList;
    private Context context;
    private String time,state;

    public OrderDetailStatusAdapter(Context context, List<Object> timeList,String state) {
        this.context = context;
        this.timeList = timeList;
        this.state = state;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.order_detail_status_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        try {
            switch (position){
                case 0:
                    viewHolder.orderDetailStatus.setText("从比比鲸到商家");
                    viewHolder.view1.setVisibility(View.INVISIBLE);
                    viewHolder.view3.setVisibility(View.INVISIBLE);
                    viewHolder.llLine2.setVisibility(View.GONE);
                    viewHolder.llLine1.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    viewHolder.orderDetailStatus.setText("在商家下单");
                    if (state.equals("3")){
                        viewHolder.llLine2.setVisibility(View.GONE);
                        viewHolder.llLine1.setVisibility(View.VISIBLE);
                    }else if (state.equals("2")){
                        viewHolder.llLine2.setVisibility(View.GONE);
                        viewHolder.llLine1.setVisibility(View.VISIBLE);
                    } else if (state.equals("1")){
                        viewHolder.llLine2.setVisibility(View.GONE);
                        viewHolder.llLine1.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.llLine2.setVisibility(View.VISIBLE);
                        viewHolder.llLine1.setVisibility(View.GONE);
                    }
                    break;
                case 2:
                    viewHolder.orderDetailStatus.setText("确认收货");
                    if (state.equals("3")){
                        viewHolder.llLine2.setVisibility(View.GONE);
                        viewHolder.llLine1.setVisibility(View.VISIBLE);
                    }else if (state.equals("2")){
                        viewHolder.llLine2.setVisibility(View.GONE);
                        viewHolder.llLine1.setVisibility(View.VISIBLE);
                    } else if (state.equals("1")){
                        viewHolder.llLine2.setVisibility(View.VISIBLE);
                        viewHolder.llLine1.setVisibility(View.GONE);
                    } else {
                        viewHolder.llLine2.setVisibility(View.VISIBLE);
                        viewHolder.llLine1.setVisibility(View.GONE);
                    }
                    break;
                case 3:
                    viewHolder.orderDetailStatus.setText("发放佣金");
                    viewHolder.view2.setVisibility(View.INVISIBLE);
                    viewHolder.view4.setVisibility(View.INVISIBLE);
                    if (state.equals("3")){
                        viewHolder.llLine2.setVisibility(View.GONE);
                        viewHolder.llLine1.setVisibility(View.VISIBLE);
                    }else {
                        viewHolder.llLine2.setVisibility(View.VISIBLE);
                        viewHolder.llLine1.setVisibility(View.GONE);
                    }
                    break;
               }
               viewHolder.orderDetailStatusTime.setText(timeList.get(position).toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.order_detail_status)
        TextView orderDetailStatus;
        @BindView(R.id.ll_line1)
        LinearLayout llLine1;
        @BindView(R.id.ll_line2)
        LinearLayout llLine2;
        @BindView(R.id.order_detail_status_time)
        TextView orderDetailStatusTime;
        @BindView(R.id.view1)
        View view1;
        @BindView(R.id.view2)
        View view2;
        @BindView(R.id.view3)
        View view3;
        @BindView(R.id.view4)
        View view4;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
