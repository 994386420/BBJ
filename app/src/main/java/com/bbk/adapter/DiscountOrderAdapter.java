package com.bbk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.bbk.Bean.DiscountBean;
import com.bbk.activity.R;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 确认订单界面优惠信息adapter
 */
public class DiscountOrderAdapter extends RecyclerView.Adapter {
    private Context context;
    List<DiscountBean> discountBeans;


    public DiscountOrderAdapter(Context context, List<DiscountBean> discountBeans) {
        this.context = context;
        this.discountBeans = discountBeans;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder ViewHolder = new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.order_youhui_item, parent, false));
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            ViewHolder viewHolder = (ViewHolder) holder;
            initTop(viewHolder, position);
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
        if (discountBeans != null && discountBeans.size() > 0) {
            return discountBeans.size();
        } else {
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_youhui)
        TextView tvYouhui;
        @BindView(R.id.check_youhui)
        CheckBox checkYouhui;

        public ViewHolder(View mView) {
            super(mView);
            ButterKnife.bind(this, mView);
        }
    }
    //设置选中的位置，将其他位置设置为未选
    public void checkPosition(int position) {
        for (int i = 0; i < discountBeans.size(); i++) {
            if (position == i) {// 设置已选位置
                discountBeans.get(i).setCheck(true);
            } else {
                discountBeans.get(i).setCheck(false);
            }
        }
        notifyDataSetChanged();
    }


    private void initTop(final ViewHolder viewHolder, final int position) {
        try {
            final DiscountBean discountBean = discountBeans.get(position);
            viewHolder.tvYouhui.setText(discountBean.getMjmoneyCh2());
            viewHolder.checkYouhui.setChecked(discountBean.isCheck());
            viewHolder.checkYouhui
                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {//单击checkbox实现单选，根据状态变换进行单选设置

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {
                            // TODO Auto-generated method stub
                            if (isChecked) {
                                checkPosition(position);
                            } else {
                                discountBean.setCheck(false);//将已选择的位置设为选择
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}