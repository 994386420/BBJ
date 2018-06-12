package com.bbk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbk.Bean.FensiBean;
import com.bbk.activity.R;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.StringUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rtj on 2018/3/7.
 */

public class FenSiAdapter extends RecyclerView.Adapter {
    private List<FensiBean> fensiBeans;
    private Context context;

    public FenSiAdapter(Context context, List<FensiBean> fensiBeans) {
        this.fensiBeans = fensiBeans;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder ViewHolder = new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.fensi_item_layout, parent, false));
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
        return fensiBeans.size();
    }

    public void notifyData(List<FensiBean> beans) {
        this.fensiBeans.addAll(beans);
        notifyDataSetChanged();
    }


    private void initTop(final ViewHolder viewHolder, final int position) {
        try {
            final FensiBean fensiBean = fensiBeans.get(position);
            //将position保存在itemView的Tag中，以便点击时进行获取
            viewHolder.itemView.setTag(position);
            viewHolder.tvName.setText(fensiBean.getInvitedname());
            viewHolder.tvQiandao.setText("今日已签到");
            if (fensiBean.getStatus().equals("0")){
                viewHolder.ivHongbao.setVisibility(View.VISIBLE);
                viewHolder.tvLingqu.setVisibility(View.GONE);
                Glide.with(context).load(R.drawable.tuiguang_d01).into(viewHolder.ivHongbao);
                viewHolder.ivHongbao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      getMoneySignFanLi(fensiBean);
                    }
                });
            }else {
                viewHolder.ivHongbao.setVisibility(View.GONE);
                viewHolder.tvLingqu.setVisibility(View.VISIBLE);
                viewHolder.tvLingqu.setText("已领取"+fensiBean.getMoney()+"个金币");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_qiandao)
        TextView tvQiandao;
        @BindView(R.id.iv_hongbao)
        ImageView ivHongbao;
        @BindView(R.id.tv_lingqu)
        TextView tvLingqu;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    /**
     *领取返利金币
     */
    private void getMoneySignFanLi(final FensiBean fensiBean) {
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("id", fensiBean.getId());
        RetrofitClient.getInstance(context).createBaseApi().getMoneySignFanLi(
                maps, new BaseObserver<String>(context) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                String status = fensiBean.getStatus();
                                Integer i = Integer.valueOf(status);
                                fensiBean.setStatus((i+1)+"");
                                notifyDataSetChanged();
                                StringUtil.showToast(context, "领取成功");
                            } else {
                                StringUtil.showToast(context, jsonObject.optString("errmsg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        DialogSingleUtil.dismiss(0);
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(context);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(context, "网络异常");
                    }
                });
    }
}
