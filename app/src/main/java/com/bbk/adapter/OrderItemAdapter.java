package com.bbk.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bbk.Bean.OrderItembean;
import com.bbk.activity.R;
import com.bbk.activity.ShopDetailActivty;
import com.bbk.model.MainActivity;
import com.bbk.resource.NewConstants;
import com.bbk.shopcar.MyWantPLActivity;
import com.bbk.shopcar.ShopOrderDetailActivity;
import com.bbk.shopcar.TuiKuanDeatailActivity;
import com.bbk.util.HomeLoadUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.logg.Logg;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rtj on 2018/3/7.
 */

public class OrderItemAdapter extends RecyclerView.Adapter {
    private Context context;
    List<OrderItembean> orderItembeans;
    private String ordernum, state, dianpuid;

    public OrderItemAdapter(Context context, List<OrderItembean> orderItembeans, String ordernum, String state, String dianpuid) {
//        this.list = list;
        this.context = context;
        this.orderItembeans = orderItembeans;
        this.ordernum = ordernum;
        this.state = state;
        this.dianpuid = dianpuid;
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
        return orderItembeans.size();
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
            vh.itemTitle.setText(orderItembean.getTitle());
            vh.tvNum.setText("x" + orderItembean.getNumber());
            vh.tvShopPrice.setText("￥" + orderItembean.getPrice());
            vh.tvShopGuige.setText(orderItembean.getParam());
            if (state.equals("-4")){
                vh.itemOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NewConstants.refeshOrderFlag = "0";
                        Intent intent = new Intent(context, TuiKuanDeatailActivity.class);
                        intent.putExtra("dianpuid", dianpuid);
                        intent.putExtra("orderid", orderItembean.getId());
                        intent.putExtra("states", "-3");
                        context.startActivity(intent);
                    }
                });
            }else {
                vh.itemOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NewConstants.refeshOrderFlag = "0";
                        Intent intent = new Intent(context, ShopOrderDetailActivity.class);
                        intent.putExtra("orderid", ordernum);
                        intent.putExtra("state", state);
                        intent.putExtra("dianpuid", dianpuid);
                        context.startActivity(intent);
                    }
                });
            }
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
                        NewConstants.refeshOrderFlag = "0";
                        Intent intent = new Intent(context, MyWantPLActivity.class);
                        intent.putExtra("id",orderItembean.getGoodsid());
                        intent.putExtra("img",orderItembean.getImgurl());
                        intent.putExtra("title",orderItembean.getTitle());
                        intent.putExtra("ordernum",ordernum);
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
                            NewConstants.refeshOrderFlag = "0";
//                            HomeLoadUtil.startChat(context);
                            MainActivity.consultService(context);

                        }
                    });
//                    vh.itemOrder.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(context, ShopDetailActivty.class);
//                            intent.putExtra("id", orderItembean.getGoodsid());
//                            context.startActivity(intent);
//                        }
//                    });
                }
            }
            String states = orderItembean.getSubstate();
            if (states != null) {
                vh.llTuikuan.setVisibility(View.VISIBLE);
                if (states.equals("-1")) {
                    vh.tvTuikuan.setVisibility(View.GONE);
                    vh.tvTuikuanState.setVisibility(View.VISIBLE);
                    vh.tvTuikuanState.setText("退款中");
                } else if (states.equals("-3")) {
                    vh.tvTuikuan.setVisibility(View.GONE);
                    vh.tvTuikuanState.setVisibility(View.VISIBLE);
                    vh.tvTuikuanState.setText("退款完成");
                } else {
//                    vh.llTuikuan.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    // 电商专用咨询页面
//    private void startECChat(OrderItembean orderItembean) {
//        String  mKefuDescription = "<img border='0' src='" + orderItembean.getImgurl() + "' /> <p>商品名称：" + orderItembean.getTitle() + " </p>   <p>订单号：" + ordernum + " </p>   <p>商品价格："+orderItembean.getPrice()+"</p> ";
//        KFAPIs.startECChat(context,
//                "bbjkfxz",//1. 客服工作组名称(请务必保证大小写一致)，请在管理后台分配
//                "比比鲸客服",//2. 会话界面标题，可自定义
//                mKefuDescription,//3. 附加信息，在成功对接客服之后，会自动将此信息发送给客服;
//                //   如果不想发送此信息，可以将此信息设置为""或者null
//                true,//4. 是否显示自定义菜单,如果设置为显示,请务必首先在管理后台设置自定义菜单,
//                //	请务必至少分配三个且只分配三个自定义菜单,多于三个的暂时将不予显示
//                //	显示:true, 不显示:false
//                5,//5. 默认显示消息数量
//                "http://www.bibijing.com/images/zhanwei/logo.png",//6. 修改默认客服头像，如果不想修改默认头像，设置此参数为null
//                NewConstants.imgurl, //7. 修改默认用户头像, 如果不想修改默认头像，设置此参数为null
//                false,                    //8. 默认机器人应答
//                true,                    //9. 是否显示商品详情，显示：true；不显示：false
//                orderItembean.getImgurl(),//10.商品详情图片
//                orderItembean.getTitle(),                    //11.商品详情简介
//                orderItembean.getPrice(),                                            //12.商品详情价格
//                "http://www.bibijing.com/images/zhanwei/logo.png",                            //13.商品网址链接
//                "goodsCallbackId",                                //14.点击商品详情布局回调参数
//                false,                                            //15.退出对话的时候是否强制评价，强制：true，不评价：false
//                new KFCallBack() {        //15. 会话页面右上角回调函数
//
//                    /**
//                     * 16.是否使用对话界面右上角默认动作. 使用默认动作返回：true, 否则返回false
//                     */
//                    @Override
//                    public Boolean useTopRightBtnDefaultAction() {
//
//                        return true;
//                    }
//
//                    /**
//                     * 17.点击对话界面右上角按钮动作，依赖于 上面一个函数的返回结果
//                     */
//                    @Override
//                    public void OnChatActivityTopRightButtonClicked() {
//                        // TODO Auto-generated method stub
//                        Log.d("KFMainActivity", "右上角回调接口调用");
//
//                    }
//
//                    /**
//                     * 18.点击商品详情图片回调函数
//                     */
//                    @Override
//                    public void OnECGoodsImageViewClicked(String imageViewURL) {
//                        // TODO Auto-generated method stub
//
//                        Log.d("KFMainActivity", "OnECGoodsImageViewClicked" + imageViewURL);
//
//                    }
//
//                    /**
//                     * 19.点击商品详情简介回调函数
//                     */
//                    @Override
//                    public void OnECGoodsTitleDetailClicked(String titleDetailString) {
//                        // TODO Auto-generated method stub
//                        Log.d("KFMainActivity", "OnECGoodsIntroductionClicked" + titleDetailString);
//
//                    }
//
//                    /**
//                     * 20.点击商品详情价格回调函数
//                     */
//                    @Override
//                    public void OnECGoodsPriceClicked(String priceString) {
//                        // TODO Auto-generated method stub
//                        Log.d("KFMainActivity", "OnECGoodsPriceClicked" + priceString);
//
//                    }
//
//                    /**
//                     * 21.点击商品详情布局回调函数
//                     */
//                    @Override
//                    public void OnEcGoodsInfoClicked(String callbackId) {
//                        // TODO Auto-generated method stub
//                        Log.d("KFMainActivity", "OnEcGoodsInfoClicked" + callbackId);
//
//                    }
//
//                    /**
//                     * 用户点击会话页面下方“常见问题”按钮时，是否使用自定义action，如果返回true,
//                     * 则默认action将不起作用，会调用下方OnFaqButtonClicked函数
//                     */
//                    public Boolean userSelfFaqAction() {
//                        return false;
//                    }
//
//                    /**
//                     * 用户点击“常见问题”按钮时，自定义action回调函数接口
//                     */
//                    @Override
//                    public void OnFaqButtonClicked() {
//                        Log.d("KFMainActivity", "OnFaqButtonClicked");
//                    }
//
//                });
//
//    }
}
