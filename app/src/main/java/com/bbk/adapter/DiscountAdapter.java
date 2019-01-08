package com.bbk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbk.Bean.DiscountBean;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.AdaptionSizeTextView;
import com.logg.Logg;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiscountAdapter extends RecyclerView.Adapter {
    private Context context;
    List<DiscountBean> discountBeans;


    public DiscountAdapter(Context context, List<DiscountBean> discountBeans) {
        this.context = context;
        this.discountBeans = discountBeans;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder ViewHolder = new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.youhuiquan_item_layout, parent, false));
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
        @BindView(R.id.tv_discount_money)
        AdaptionSizeTextView tvDiscountMoney;
        @BindView(R.id.tv_discount_message)
        AdaptionSizeTextView tvDiscountMessage;
        @BindView(R.id.tv_discount_time)
        AdaptionSizeTextView tvDiscountTime;
        @BindView(R.id.tv_ok)
        TextView tvOk;
        @BindView(R.id.ll_discount)
        LinearLayout llDiscount;
        @BindView(R.id.tv_quan_msg)
        TextView tvQuanMsg;
        @BindView(R.id.img_has_lingqu)
        ImageView imgHasLingqu;

        public ViewHolder(View mView) {
            super(mView);
            ButterKnife.bind(this, mView);
        }
    }

    private void initTop(final ViewHolder viewHolder, final int position) {
        try {
            final DiscountBean discountBean = discountBeans.get(position);
            viewHolder.tvQuanMsg.setText(discountBean.getMsg());
            viewHolder.tvDiscountMoney.setText(discountBean.getMjmoneyprice());
            viewHolder.tvDiscountMessage.setText(discountBean.getMjmoney());
            viewHolder.tvDiscountTime.setText(discountBean.getBegin() + " - " + discountBean.getEnd());
            if (discountBean.getState() != null) {
                if (discountBean.getState().equals("")) {
                    viewHolder.tvOk.setText("立即领取");
                    viewHolder.imgHasLingqu.setVisibility(View.GONE);
                } else {
                    viewHolder.tvOk.setText("去使用");
                    viewHolder.imgHasLingqu.setVisibility(View.VISIBLE);
                }
            }
            viewHolder.tvOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (discountBean.getId() != null) {
                        insertCouponsByUserid(discountBean.getId(), discountBean);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 领取优惠券
     *
     * @param jid 劵id
     */
    private void insertCouponsByUserid(String jid, final DiscountBean discountBean) {
        Map<String, String> maps = new HashMap<String, String>();
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        maps.put("userid", userID);
        maps.put("jid", jid);
        RetrofitClient.getInstance(context).createBaseApi().insertCouponsByUserid(
                maps, new BaseObserver<String>(context) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String message = "";
                            Logg.json(jsonObject);
                            /**
                             * -1未查找到优惠券	1成功	0失败	-2已经领取过优惠券
                             */
                            if (jsonObject.optString("status").equals("1")) {
                                discountBean.setState("已领取");
                                message = "领取成功";
                                notifyDataSetChanged();
                                StringUtil.showToast(context, message);
                            } else {
                                switch (jsonObject.optString("content")) {
                                    case "-1":
                                        message = "未查找到优惠券";
                                        break;
                                    case "0":
                                        message = "领取失败";
                                        break;
                                    case "-2":
                                        message = "已经领取过优惠券";
                                        break;
                                }
                                notifyDataSetChanged();
                                StringUtil.showToast(context, message);
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