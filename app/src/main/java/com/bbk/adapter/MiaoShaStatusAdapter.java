package com.bbk.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbk.Bean.ChaozhigouTypesBean;
import com.bbk.activity.R;
import com.logg.Logg;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MiaoShaStatusAdapter extends BaseAdapter {
    List<ChaozhigouTypesBean> chaozhigouTypesBeans;
    private Context context;
    private int clickTemp = -1;

    public MiaoShaStatusAdapter(Context context, List<ChaozhigouTypesBean> chaozhigouTypesBeans) {
        this.context = context;
        this.chaozhigouTypesBeans = chaozhigouTypesBeans;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 标识选择的Item
    public void setSeclection(int position) {
        clickTemp = position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.miaosha_states_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        try {
            final ChaozhigouTypesBean chaozhigouTypesBean = chaozhigouTypesBeans.get(position);
            viewHolder.miaoshaTime.setText(chaozhigouTypesBean.getName());
            viewHolder.miaoshaoStatus.setText(chaozhigouTypesBean.getDesc());
            if (position == clickTemp) {
                viewHolder.pointView.setVisibility(View.VISIBLE);
                viewHolder.llMiaosha.setBackgroundResource(R.color.tuiguang_color2);
                viewHolder.miaoshaoStatus.setTextColor(context.getResources().getColor(R.color.white));
                viewHolder.miaoshaTime.setTextColor(context.getResources().getColor(R.color.white));
            }else {
                viewHolder.pointView.setVisibility(View.INVISIBLE);
                viewHolder.llMiaosha.setBackgroundResource(R.color.tuiguang_color7);
                viewHolder.miaoshaoStatus.setTextColor(context.getResources().getColor(R.color.tuiguang_color8));
                viewHolder.miaoshaTime.setTextColor(context.getResources().getColor(R.color.tuiguang_color8));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.miaosha_time)
        TextView miaoshaTime;
        @BindView(R.id.miaoshao_status)
        TextView miaoshaoStatus;
        @BindView(R.id.ll_miaosha)
        LinearLayout llMiaosha;
        @BindView(R.id.point_view)
        ImageView pointView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
