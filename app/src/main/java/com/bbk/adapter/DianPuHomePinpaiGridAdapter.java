package com.bbk.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbk.Bean.PinpaiBean;
import com.bbk.Bean.ShopDianpuBean;
import com.bbk.activity.R;
import com.bbk.activity.ShopDetailActivty;
import com.bbk.util.EventIdIntentUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rtj on 2018/3/7.
 */

public class DianPuHomePinpaiGridAdapter extends RecyclerView.Adapter {
    //    private List<Map<String,String>> list;
    private Context context;
    List<PinpaiBean> pinpaiBeans;

    public DianPuHomePinpaiGridAdapter(Context context,List<PinpaiBean> pinpaiBeans) {
//        this.list = list;
        this.context = context;
        this.pinpaiBeans = pinpaiBeans;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder ViewHolder = new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.dianpu_pinpa_item, parent, false));
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
        return pinpaiBeans.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView item_img;
        LinearLayout itemlayout;

        public ViewHolder(View mView) {
            super(mView);
            ButterKnife.bind(this, mView);
            item_img = mView.findViewById(R.id.item_img);
            itemlayout = mView.findViewById(R.id.result_item);
        }
    }

    private void initTop(final ViewHolder viewHolder, final int position) {
        try {
            final PinpaiBean pinpaiBean = pinpaiBeans.get(position);
            Glide.with(context)
                    .load(pinpaiBean.getImg())
                    .priority(Priority.HIGH)
                    .placeholder(R.mipmap.zw_img_300)
                    .into(viewHolder.item_img);
            viewHolder.itemlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventIdIntentUtil.EventIdIntentDianpu(context,pinpaiBean.getEventId(),pinpaiBean.getKeyword());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
