package com.bbk.adapter;

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
import com.bbk.Bean.TaoBaoCarBean;
import com.bbk.Bean.TaobaoCarListBean;
import com.bbk.Bean.ZiYingCarListBean;
import com.bbk.activity.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



public class TaoBaoAdapter extends RecyclerView.Adapter {
    private Context context;
    List<TaoBaoCarBean> taoBaoCarBeans;
    private String domain;
    List<ZiYingCarListBean> ziYingCarListBeans;
    List<TaobaoCarListBean> taobaoCarListBeans;
    private int allMun;

    public TaoBaoAdapter(Context context, List<TaoBaoCarBean> taoBaoCarBeans, String domain, int allMun) {
        this.context = context;
        this.taoBaoCarBeans = taoBaoCarBeans;
        this.domain = domain;
        this.allMun = allMun;
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
        @BindView(R.id.img_tishi)
        ImageView imgTishi;

        public ViewHolder(View mView) {
            super(mView);
            ButterKnife.bind(this, mView);
        }
    }

    private void initTop(final ViewHolder viewHolder, final int position) {
        try {
            final TaoBaoCarBean taoBaoCarBean = taoBaoCarBeans.get(position);
            if (position == 0){
                viewHolder.imgTishi.setVisibility(View.VISIBLE);
            }else {
                viewHolder.imgTishi.setVisibility(View.GONE);
            }
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
            viewHolder.recyclerviewTaobaoItem.setFocusable(false);
//            if (taoBaoCarBean.getName().equals("京东自营")){
//                ziYingCarListBeans = JSON.parseArray(taoBaoCarBean.getList(), ZiYingCarListBean.class);
//            }else {
            taobaoCarListBeans = JSON.parseArray(taoBaoCarBean.getList(), TaobaoCarListBean.class);
//            }
//            Logg.e(taobaoCarListBeans.size()+"===============>>>");
            viewHolder.recyclerviewTaobaoItem.setAdapter(new TaoBaoListAdapter(context, taobaoCarListBeans, allMun, domain, taoBaoCarBean.getName(), taoBaoCarBean.getList(),taoBaoCarBean.getDianpuurl()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
