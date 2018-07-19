package com.bbk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbk.Bean.InvitedBean;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.AdaptionSizeTextView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rtj on 2018/3/7.
 */

public class FenSiInvitedAdapter extends RecyclerView.Adapter {
    private List<InvitedBean> fensiBeans;
    private Context context;

    public FenSiInvitedAdapter(Context context, List<InvitedBean> fensiBeans) {
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

    public void notifyData(List<InvitedBean> beans) {
        this.fensiBeans.addAll(beans);
        notifyDataSetChanged();
    }


    private void initTop(final ViewHolder viewHolder, final int position) {
        try {
            final InvitedBean fensiBean = fensiBeans.get(position);
            //将position保存在itemView的Tag中，以便点击时进行获取
            viewHolder.itemView.setTag(position);
            viewHolder.tvName.setText(fensiBean.getNickname());
            viewHolder.tvQiandao.setText(fensiBean.getMsg());
            viewHolder.tvQiandao.setTextColor(context.getResources().getColor(R.color.tuiguang_color1));
            viewHolder.ivHongbao.setVisibility(View.GONE);
            if (fensiBean.getStatus().equals("0")) {
                viewHolder.tvLingqu.setVisibility(View.GONE);
                viewHolder.tvNotice.setVisibility(View.VISIBLE);
                viewHolder.tvNotice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        noticeInvitedUserSign(fensiBean);
                    }
                });
            } else {
                viewHolder.tvLingqu.setVisibility(View.VISIBLE);
                viewHolder.tvNotice.setVisibility(View.GONE);
                viewHolder.tvLingqu.setText("今日已提醒");
                viewHolder.tvLingqu.setTextColor(context.getResources().getColor(R.color.tuiguang_color4));
            }
            viewHolder.llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_qiandao)
        AdaptionSizeTextView tvQiandao;
        @BindView(R.id.iv_hongbao)
        ImageView ivHongbao;
        @BindView(R.id.tv_lingqu)
        AdaptionSizeTextView tvLingqu;
        @BindView(R.id.tv_notice)
        AdaptionSizeTextView tvNotice;
        @BindView(R.id.ll_item)
        LinearLayout llItem;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 提醒
     */
    private void noticeInvitedUserSign(final InvitedBean fensiBean) {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        maps.put("inviteduserid", fensiBean.getInviteduserid());
        RetrofitClient.getInstance(context).createBaseApi().noticeInvitedUserSign(
                maps, new BaseObserver<String>(context) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                String status = fensiBean.getStatus();
                                Integer i = Integer.valueOf(status);
                                fensiBean.setStatus((i + 1) + "");
                                notifyDataSetChanged();
                                StringUtil.showToast(context, "提醒成功");
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
                        StringUtil.showToast(context, e.message);
                    }
                });
    }
}
