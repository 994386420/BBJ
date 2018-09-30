package com.bbk.adapter;

import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.ConfrimOredetItemBean;
import com.bbk.Bean.TaoBaoCarBean;
import com.bbk.Bean.TaobaoCarListBean;
import com.bbk.activity.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rtj on 2018/3/7.
 */

public class TaoBaoAdapter extends RecyclerView.Adapter {
    private Context context;
    List<TaoBaoCarBean> taoBaoCarBeans;

    public TaoBaoAdapter(Context context, List<TaoBaoCarBean> taoBaoCarBeans) {
        this.context = context;
        this.taoBaoCarBeans = taoBaoCarBeans;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder ViewHolder = new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.taobao_car_item, parent, false));
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
        if (taoBaoCarBeans != null && taoBaoCarBeans.size() > 0) {
            return taoBaoCarBeans.size();
        } else {
            return 0;
        }
    }

    public void notifyData(List<TaoBaoCarBean> beans) {
        this.taoBaoCarBeans.addAll(beans);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img)
        ImageView img;
        @BindView(R.id.mdianpu)
        TextView mdianpu;
        @BindView(R.id.mtypetext)
        TextView mtypetext;
        @BindView(R.id.recyclerview_taobao_item)
        RecyclerView recyclerviewTaobaoItem;
        @BindView(R.id.result_item)
        LinearLayout resultItem;

        public ViewHolder(View mView) {
            super(mView);
            ButterKnife.bind(this, mView);
        }
    }

    private void initTop(final ViewHolder viewHolder, final int position) {
        try {
            final TaoBaoCarBean taoBaoCarBean = taoBaoCarBeans.get(position);
            viewHolder.mdianpu.setText(taoBaoCarBean.getName());
//            Glide.with(context)
//                    .load(taoBaoCarBean.getDpimgurl())
//                    .priority(Priority.HIGH)
//                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
//                    .placeholder(R.mipmap.zw_img_300)
//                    .into(viewHolder.img);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,
                    LinearLayoutManager.VERTICAL, false) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }

            };
            viewHolder.recyclerviewTaobaoItem.setLayoutManager(linearLayoutManager);
            List<TaobaoCarListBean> taobaoCarListBeans = JSON.parseArray(taoBaoCarBean.getList(), TaobaoCarListBean.class);
            viewHolder.recyclerviewTaobaoItem.setAdapter(new TaoBaoListAdapter(context, taobaoCarListBeans));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
