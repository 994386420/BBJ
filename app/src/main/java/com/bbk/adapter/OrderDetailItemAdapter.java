package com.bbk.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bbk.Bean.OrderItembean;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.ShopDetailActivty;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.model.MainActivity;
import com.bbk.resource.NewConstants;
import com.bbk.shopcar.MyWantPLActivity;
import com.bbk.shopcar.TuiKuanDeatailActivity;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.HomeLoadUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.logg.Logg;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rtj on 2018/3/7.
 */

public class OrderDetailItemAdapter extends RecyclerView.Adapter {
    private Context context;
    List<OrderItembean> orderItembeans;
    private String state;
    private String orderid,dianpuid;
    private String ordernum;//订单号

    public OrderDetailItemAdapter(Context context, List<OrderItembean> orderItembeans, String state,String orderid,String dianpuid,String ordernum) {
        this.context = context;
        this.orderItembeans = orderItembeans;
        this.orderid = orderid;
        this.state = state;
        this.dianpuid = dianpuid;
        this.ordernum = ordernum;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder ViewHolder = new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.order_item_recyc_layout, parent, false));
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
        if (orderItembeans != null && orderItembeans.size() > 0) {
            return orderItembeans.size();
        } else {
            return 0;
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_img)
        ImageView itemImg;
        @BindView(R.id.item_title)
        TextView itemTitle;
        @BindView(R.id.tv_shop_price)
        TextView tvShopPrice;
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.tv_shop_guige)
        TextView tvShopGuige;
        @BindView(R.id.item_order)
        LinearLayout itemOrder;
        @BindView(R.id.ll_tuikuan)
        RelativeLayout llTuikuan;
        @BindView(R.id.tv_tuikuan)
        TextView tvTuikuan;
        @BindView(R.id.tv_tuikuan_state)
        TextView tvTuikuanState;
        @BindView(R.id.tv_pl)
        TextView tvPl;

        public ViewHolder(View mView) {
            super(mView);
            ButterKnife.bind(this, mView);
        }
    }

    private void initTop(final ViewHolder vh, final int position) {
        try {
            final OrderItembean orderItembean = orderItembeans.get(position);
            final String states = orderItembean.getSubstate();
            vh.itemTitle.setText(orderItembean.getTitle());
            vh.tvNum.setText("x" + orderItembean.getNumber());
            vh.tvShopPrice.setText("￥" + orderItembean.getPrice());
            vh.tvShopGuige.setText(orderItembean.getParam());
            vh.itemOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (orderItembean.getProductstate().equals("1")) {
                        Intent intent = new Intent(context, ShopDetailActivty.class);
                        intent.putExtra("id", orderItembean.getGoodsid());
                        context.startActivity(intent);
                        return;
                    }
                    StringUtil.showToast(context,"该商品已下架！");
                }
            });
            Glide.with(context)
                    .load(orderItembean.getImgurl())
                    .priority(Priority.HIGH)
                    .placeholder(R.mipmap.zw_img_300)
                    .into(vh.itemImg);

            if (orderItembean.getPlstate().equals("")&&state.equals("3")){
                vh.llTuikuan.setVisibility(View.VISIBLE);
                vh.tvPl.setVisibility(View.VISIBLE);
                vh.tvPl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, MyWantPLActivity.class);
                        intent.putExtra("id",orderItembean.getGoodsid());
                        intent.putExtra("img",orderItembean.getImgurl());
                        intent.putExtra("title",orderItembean.getTitle());
                        intent.putExtra("ordernum",orderid);
                        intent.putExtra("dianpuid",dianpuid);
                        context.startActivity(intent);
                    }
                });
            }else {
                vh.llTuikuan.setVisibility(View.GONE);
                vh.tvPl.setVisibility(View.GONE);
            }

            if (state != null){
                vh.llTuikuan.setVisibility(View.VISIBLE);
                if (state.equals("1") || state.equals("2")) {
                    vh.tvTuikuan.setVisibility(View.VISIBLE);
                    vh.tvTuikuanState.setVisibility(View.GONE);
                    vh.tvTuikuan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            HomeLoadUtil.startChat(context);
                            MainActivity.consultService(context);

                        }
                    });
                    vh.itemOrder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (orderItembean.getProductstate().equals("1")) {
                                Intent intent = new Intent(context, ShopDetailActivty.class);
                                intent.putExtra("id", orderItembean.getGoodsid());
                                context.startActivity(intent);
                                return;
                            }
                            StringUtil.showToast(context,"该商品已下架！");
                        }
                    });
                }
            }
            if (states != null) {
                vh.llTuikuan.setVisibility(View.VISIBLE);
                if(states.equals("-1")){
                    vh.tvTuikuan.setVisibility(View.GONE);
                    vh.tvTuikuanState.setVisibility(View.VISIBLE);
                    vh.tvTuikuanState.setText("退款中");
                    vh.itemOrder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, TuiKuanDeatailActivity.class);
                            intent.putExtra("dianpuid", dianpuid);
                            intent.putExtra("orderid", orderItembean.getId());
                            intent.putExtra("states", states);
                            context.startActivity(intent);
                        }
                    });
                }else if(states.equals("-3")){
                    vh.tvTuikuan.setVisibility(View.GONE);
                    vh.tvTuikuanState.setVisibility(View.VISIBLE);
                    vh.tvTuikuanState.setText("退款完成");
                    vh.itemOrder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, TuiKuanDeatailActivity.class);
                            intent.putExtra("dianpuid", dianpuid);
                            intent.putExtra("orderid", orderItembean.getId());
                            intent.putExtra("states", states);
                            context.startActivity(intent);
                        }
                    });
                }else{
//                    vh.llTuikuan.setVisibility(View.GONE);
                    vh.itemOrder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (orderItembean.getProductstate().equals("1")) {
                                Intent intent = new Intent(context, ShopDetailActivty.class);
                                intent.putExtra("id", orderItembean.getGoodsid());
                                context.startActivity(intent);
                                return;
                            }
                            StringUtil.showToast(context,"该商品已下架！");
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void refundGoodsl(String goodsid) {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        maps.put("goodsid", goodsid);
        RetrofitClient.getInstance(context).createBaseApi().refundGoods(
                maps, new BaseObserver<String>(context) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            if (jsonObject.optString("status").equals("1")) {
                                Logg.json(jsonObject);
                            }
                        } catch (JSONException e) {
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
