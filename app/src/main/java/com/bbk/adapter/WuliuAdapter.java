package com.bbk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbk.Bean.WuliuItemBean;
import com.bbk.activity.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 追踪物流列表的适配器
 */

public class WuliuAdapter extends RecyclerView.Adapter<WuliuAdapter.TraceViewHolder> {

    private static final int TYPE_CURR = 0; //历史记录
    private static final int TYPE_NORMAL = 1; //当前
    private Context mContext;
    private List<WuliuItemBean> mList;
    private LayoutInflater inflater;

    public WuliuAdapter(Context mContext, List<WuliuItemBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemCount() {
        if (mList != null && mList.size() > 0) {
            return mList.size();
        } else {
            return 0;
        }
    }

    @Override
    public TraceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TraceViewHolder(inflater.inflate(R.layout.item_wuliu, parent, false));
    }

    @Override
    public void onBindViewHolder(TraceViewHolder holder, int position) {
        WuliuItemBean trace = mList.get(position);
        int type = Integer.parseInt(trace.getType());
        if (type == TYPE_CURR) {
            holder.tvAddress.setTextColor(mContext.getResources().getColor(R.color.tuiguang_color4));
            holder.dotIv.setImageResource(R.mipmap.dot_black);
            holder.dotIvNew.setVisibility(View.GONE);
        } else if (type == TYPE_NORMAL) {
            holder.tvAddress.setTextColor(mContext.getResources().getColor(R.color.tuiguang_color3));
            holder.dotIv.setVisibility(View.GONE);
            holder.dotIvNew.setVisibility(View.VISIBLE);
        }
        holder.tvTime.setText(trace.getStime());
        holder.tvAddress.setText(trace.getAddress());
        holder.tvDate.setText(trace.getSdate());
        if (position == mList.size() - 1) {
            //最后一条数据，隐藏时间轴的竖线和水平的分割线
            holder.timeLineView.setVisibility(View.INVISIBLE);
        }
        if (position == 0) {
            holder.timeLineViewTop.setVisibility(View.INVISIBLE);
        }
    }

    static class TraceViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.dot_iv)
        ImageView dotIv;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.time_line_view_top)
        View timeLineViewTop;
        @BindView(R.id.time_line_view)
        View timeLineView;
        @BindView(R.id.dot_iv_new)
        ImageView dotIvNew;

        public TraceViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
