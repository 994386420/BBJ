package com.bbk.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.OrderItemBeanList;
import com.bbk.Bean.OrderItembean;
import com.bbk.activity.DianpuActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.dialog.AlertDialog;
import com.bbk.model.BaseService;
import com.bbk.model.PayModel;
import com.bbk.resource.Constants;
import com.bbk.resource.NewConstants;
import com.bbk.shopcar.MyWantPLActivity;
import com.bbk.shopcar.NewDianpuActivity;
import com.bbk.shopcar.ShopOrderDetailActivity;
import com.bbk.shopcar.TuiKuanDeatailActivity;
import com.bbk.shopcar.WuLiuActivity;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.logg.Logg;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rtj on 2018/2/27.
 */

public class ShopOrderAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<OrderItemBeanList> orderItemBeanLists;
    private OrderInterface orderInterface;
    private String ordernum;
    private String ids, nums, guiges;
    private PayReq mReq;
    private PayModel mPayModel;
    private IWXAPI msgApi = null;

    public ShopOrderAdapter(Context context, List<OrderItemBeanList> list, String ordernum) {
        this.context = context;
        this.orderItemBeanLists = list;
        this.ordernum = ordernum;
        mPayModel = BaseService.getPayModel((Activity) context);

    }

//    public void notifyData(List<ShopOrderBean> beans) {
//        this.shopOrderBeans.addAll(beans);
//        notifyDataSetChanged();
//    }

//    @Override
//    public int getCount() {
//        return orderItemBeanLists.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return orderItemBeanLists.get(position);
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder ViewHolder = new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.shop_order_item, parent, false));
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
        if (orderItemBeanLists != null && orderItemBeanLists.size() > 0) {
            return orderItemBeanLists.size();
        }else {
            return 0;
        }
    }

//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//        ViewHolder vh;
//        if (convertView == null) {
//            convertView = View.inflate(context, R.layout.shop_order_item, null);
//            vh = new ViewHolder(convertView);
//            convertView.setTag(vh);
//        } else {
//            vh = (ViewHolder) convertView.getTag();
//        }
//        try {
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return convertView;
//    }

//    static class ViewHolder {
//
//
//        ViewHolder(View view) {
//            ButterKnife.bind(this, view);
//        }
//    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.mdianpu)
        TextView mdianpu;
        @BindView(R.id.mtypetext)
        TextView mtypetext;
        //        @BindView(R.id.item_img)
//        ImageView itemImg;
//        @BindView(R.id.item_title)
//        TextView itemTitle;
//        @BindView(R.id.tv_shop_price)
//        TextView tvShopPrice;
//        @BindView(R.id.tv_num)
//        TextView tvNum;
//        @BindView(R.id.tv_shop_guige)
//        TextView tvShopGuige;
        @BindView(R.id.tv_shop_num)
        TextView tvShopNum;
        @BindView(R.id.tv_shop_price_heji)
        TextView tvShopPriceHeji;
        @BindView(R.id.tv_shop_kuaidi)
        TextView tvShopKuaidi;
        @BindView(R.id.mpricebox)
        LinearLayout mpricebox;
        @BindView(R.id.mhenggang)
        View mhenggang;
        @BindView(R.id.mtextbox)
        LinearLayout mtextbox;
        @BindView(R.id.result_item)
        LinearLayout resultItem;
        @BindView(R.id.tv_quxiao)
        TextView tvQuxiao;
        @BindView(R.id.tv_wuliu)
        TextView tvWuliu;
        @BindView(R.id.tv_yanchang)
        TextView tvYanchang;
        @BindView(R.id.tv_delete)
        TextView tvDelete;
        @BindView(R.id.tv_tixing)
        TextView tvTixing;
        @BindView(R.id.tv_pay)
        TextView tvPay;
        @BindView(R.id.tv_pl)
        TextView tvPl;
        @BindView(R.id.tv_qr)
        TextView tvQr;
        @BindView(R.id.recyclerview_order_item)
        RecyclerView recyclerviewOrderItem;
        @BindView(R.id.ll_dianpu)
        LinearLayout llDianpu;

        public ViewHolder(View mView) {
            super(mView);
            ButterKnife.bind(this, mView);
        }
    }

    private void initTop(final ViewHolder vh, final int position) {
        try {
            final OrderItemBeanList shopOrderBean = orderItemBeanLists.get(position);
            vh.llDianpu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewConstants.refeshOrderFlag = "0";
                    Intent intent  = new Intent(context, NewDianpuActivity.class);
                    intent.putExtra("dianpuid",shopOrderBean.getDianpuid());
                    context.startActivity(intent);
                }
            });
            vh.mdianpu.setText(shopOrderBean.getDianpu());
            vh.tvShopPriceHeji.setText(shopOrderBean.getSubprice());
//            vh.itemTitle.setText(shopOrderBean.getTitle());
//            vh.tvNum.setText("x" + shopOrderBean.getNumber());
//            vh.tvShopPrice.setText("￥" + shopOrderBean.getPrice());
//            vh.tvShopGuige.setText(shopOrderBean.getParam());
            vh.tvShopKuaidi.setText("（含运费￥"+shopOrderBean.getKuaidiM()+"）");
            vh.tvShopNum.setText("共" + shopOrderBean.getSubnumber() + "件商品 合计￥");
            vh.recyclerviewOrderItem.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,
                    LinearLayoutManager.VERTICAL, false) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }

            };
            vh.recyclerviewOrderItem.setLayoutManager(linearLayoutManager);
            final List<OrderItembean> orderItembeans = JSON.parseArray(shopOrderBean.getList(), OrderItembean.class);
//            List<String> list = new ArrayList<>();
//            List<String> listNum = new ArrayList<>();
//            List<String> listguiges = new ArrayList<>();
//            Logg.e(orderItembeans.size()+"==="+orderItemBeanLists.size());
//            for (int i = 0; i < orderItembeans.size(); i++) {
//                list.add(orderItembeans.get(position).getGoodsid());
//                listNum.add(orderItembeans.get(position).getNumber());
//                listguiges.add(orderItembeans.get(position).getParam());
//            }
//            ids = list.toString().replace("[", "").replace("]", "").replace(",", "|").replace(" ", "");
//            Logg.e(position+"===="+ids);
//            nums = listNum.toString().replace("[", "").replace("]", "").replace(",", "|").replace(" ", "");
//            guiges = listguiges.toString().replace("[", "").replace("]", "").replace(",", "|").replace(" ", "");
            vh.recyclerviewOrderItem.setAdapter(new OrderItemAdapter(context, orderItembeans, ordernum, shopOrderBean.getState(),shopOrderBean.getDianpuid()));
//            Glide.with(context)
//                    .load(shopOrderBean.getImgurl())
//                    .priority(Priority.HIGH)
//                    .placeholder(R.mipmap.zw_img_300)
//                    .into(vh.itemImg);

            switch (shopOrderBean.getState()) {
                case "-4":
                    vh.mtypetext.setText("退款成功");
                    vh.tvDelete.setVisibility(View.GONE);
                    vh.tvPay.setVisibility(View.GONE);
                    vh.tvQuxiao.setVisibility(View.GONE);
                    vh.tvTixing.setVisibility(View.GONE);
                    vh.tvWuliu.setVisibility(View.GONE);
                    vh.tvYanchang.setVisibility(View.GONE);
                    vh.tvQr.setVisibility(View.GONE);
                    vh.tvPl.setVisibility(View.GONE);
                    break;
                case "-2":
                    vh.mtypetext.setText("交易关闭");
                    vh.tvDelete.setVisibility(View.VISIBLE);
                    vh.tvPay.setVisibility(View.GONE);
                    vh.tvQuxiao.setVisibility(View.GONE);
                    vh.tvTixing.setVisibility(View.GONE);
                    vh.tvWuliu.setVisibility(View.GONE);
                    vh.tvYanchang.setVisibility(View.GONE);
                    vh.tvQr.setVisibility(View.GONE);
                    vh.tvPl.setVisibility(View.GONE);
                    vh.tvDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AlertDialog(context).builder().setTitle("确定删除订单？")
                                    .setMsg("删除之后订单无法恢复，请慎重考虑")
                                    .setPositiveButton("确认", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
//                                            orderInterface.doDeleteOrder(shopOrderBean.getSubnumber());
                                            deleteMyOrder("1", ordernum,shopOrderBean.getDianpuid(),position,orderItemBeanLists);
                                        }
                                    }).setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();
                        }
                    });
                    break;
                case "-1":
                    vh.mtypetext.setText("退款中");
                    vh.tvDelete.setVisibility(View.GONE);
                    vh.tvPay.setVisibility(View.GONE);
                    vh.tvQuxiao.setVisibility(View.GONE);
                    vh.tvTixing.setVisibility(View.GONE);
                    vh.tvWuliu.setVisibility(View.GONE);
                    vh.tvYanchang.setVisibility(View.GONE);
                    vh.tvQr.setVisibility(View.GONE);
                    vh.tvPl.setVisibility(View.GONE);
                    break;
                case "0":
                    vh.mtypetext.setText("待付款");
                    vh.tvQuxiao.setVisibility(View.VISIBLE);
                    vh.tvPay.setVisibility(View.VISIBLE);
                    vh.tvDelete.setVisibility(View.GONE);
                    vh.tvTixing.setVisibility(View.GONE);
                    vh.tvWuliu.setVisibility(View.GONE);
                    vh.tvYanchang.setVisibility(View.GONE);
                    vh.tvQr.setVisibility(View.GONE);
                    vh.tvPl.setVisibility(View.GONE);
                    vh.tvQuxiao.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AlertDialog(context).builder().setTitle("确定取消订单？")
                                    .setMsg("是否确认取消订单")
                                    .setPositiveButton("确认", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
//                                            orderInterface.doQuXiaoOrder(shopOrderBean.getSubnumber());
                                            deleteMyOrder("0", ordernum,shopOrderBean.getDianpuid(),position,orderItemBeanLists);
                                        }
                                    }).setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();
                        }
                    });
                    vh.tvPay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            NewConstants.refeshOrderFlag = "1";
                            getOrderInfo(shopOrderBean.getAddrid(),shopOrderBean.getDianpuid(),orderItembeans);
                        }
                    });
                    break;
                case "1":
                    vh.mtypetext.setText("待发货");
                    vh.tvTixing.setVisibility(View.GONE);
                    vh.tvDelete.setVisibility(View.GONE);
                    vh.tvPay.setVisibility(View.GONE);
                    vh.tvQuxiao.setVisibility(View.GONE);
                    vh.tvWuliu.setVisibility(View.GONE);
                    vh.tvYanchang.setVisibility(View.GONE);
                    vh.tvQr.setVisibility(View.GONE);
                    vh.tvPl.setVisibility(View.GONE);
                    break;
                case "2":
                    vh.mtypetext.setText("已发货");
                    vh.tvWuliu.setVisibility(View.VISIBLE);
                    vh.tvYanchang.setVisibility(View.GONE);
                    vh.tvQr.setVisibility(View.VISIBLE);
                    vh.tvQr.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AlertDialog(context).builder().setTitle("确定收货？")
                                    .setMsg("是否确认收货")
                                    .setPositiveButton("确认", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            receiptGoods(ordernum,shopOrderBean.getDianpuid(),position,orderItemBeanLists);
                                        }
                                    }).setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();
                        }
                    });
                    vh.tvDelete.setVisibility(View.GONE);
                    vh.tvPay.setVisibility(View.GONE);
                    vh.tvQuxiao.setVisibility(View.GONE);
                    vh.tvTixing.setVisibility(View.GONE);
                    vh.tvPl.setVisibility(View.GONE);
                    vh.tvWuliu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, WuLiuActivity.class);
                            intent.putExtra("expressnum",shopOrderBean.getExpressage());
                            context.startActivity(intent);
                        }
                    });
                    break;
                case "3":
                    vh.mtypetext.setText("待评论");
                    vh.tvWuliu.setVisibility(View.GONE);
                    vh.tvPl.setVisibility(View.GONE);
                    vh.tvDelete.setVisibility(View.GONE);
                    vh.tvPay.setVisibility(View.GONE);
                    vh.tvQuxiao.setVisibility(View.GONE);
                    vh.tvTixing.setVisibility(View.GONE);
                    vh.tvYanchang.setVisibility(View.GONE);
                    vh.tvQr.setVisibility(View.GONE);
                    break;
                case "4":
                    vh.mtypetext.setText("交易成功");
                    vh.tvDelete.setVisibility(View.GONE);
                    vh.tvPay.setVisibility(View.GONE);
                    vh.tvQuxiao.setVisibility(View.GONE);
                    vh.tvTixing.setVisibility(View.GONE);
                    vh.tvWuliu.setVisibility(View.GONE);
                    vh.tvYanchang.setVisibility(View.GONE);
                    vh.tvQr.setVisibility(View.GONE);
                    vh.tvPl.setVisibility(View.GONE);
                    break;
            }
//            if (shopOrderBean.getState().equals("-4")){
//                vh.resultItem.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        NewConstants.refeshOrderFlag = "0";
//                        Intent intent = new Intent(context, TuiKuanDeatailActivity.class);
//                        intent.putExtra("dianpuid", shopOrderBean.getDianpuid());
//                        intent.putExtra("orderid", ordernum);
//                        intent.putExtra("states", "-3");
//                        context.startActivity(intent);
//                    }
//                });
//            }else {
                vh.resultItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NewConstants.refeshOrderFlag = "0";
                        Intent intent = new Intent(context, ShopOrderDetailActivity.class);
                        intent.putExtra("orderid", ordernum);
                        intent.putExtra("dianpuid",shopOrderBean.getDianpuid());
                        intent.putExtra("state", shopOrderBean.getState());
                        context.startActivity(intent);
                    }
                });
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 参数state(1为删除	0为取消)
     *
     * @param status
     * @param orderid
     */
    private void deleteMyOrder(final String status, final String orderid, String dianpuid, final int postion, final List<OrderItemBeanList> orderItemBeanLists) {
        Map<String, String> maps = new HashMap<String, String>();
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        maps.put("userid", userID);
        maps.put("state", status);
        maps.put("ordernum", orderid);
        maps.put("dianpuid", dianpuid);
        RetrofitClient.getInstance(context).createBaseApi().deleteMyOrder(
                maps, new BaseObserver<String>(context) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                Logg.json(jsonObject);
                                if (status.equals("1")) {
                                    StringUtil.showToast(context, "删除订单成功");
                                    orderItemBeanLists.remove(postion);
                                    notifyDataSetChanged();
                                } else {
                                    if (NewConstants.option == 0) {
                                        StringUtil.showToast(context, "取消订单成功");
                                        orderItemBeanLists.get(postion).setState("-2");
                                        notifyDataSetChanged();
                                    }else {
                                        StringUtil.showToast(context, "取消订单成功");
                                        orderItemBeanLists.remove(postion);
                                        notifyDataSetChanged();
                                    }
                                }
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

    private void receiptGoods(final String orderid, String dianpuid, final int postion, final List<OrderItemBeanList> orderItemBeanLists) {
        Map<String, String> maps = new HashMap<String, String>();
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        maps.put("userid", userID);
        maps.put("ordernum", orderid);
        maps.put("dianpuid", dianpuid);
        RetrofitClient.getInstance(context).createBaseApi().receiptGoods(
                maps, new BaseObserver<String>(context) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                Logg.json(jsonObject);
                                StringUtil.showToast(context, "确认收货成功");
                                orderItemBeanLists.remove(postion);
                                notifyDataSetChanged();
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
    public void setOrderInterface(OrderInterface modifyCountInterface) {
        this.orderInterface = modifyCountInterface;
    }
    /**
     * 调起支付订单
     */
    private void getOrderInfo(String addrid,String dianpuid,List<OrderItembean> orderItembeans) {
        List<String> list = new ArrayList<>();
        List<String> listNum = new ArrayList<>();
        List<String> listguiges = new ArrayList<>();
        Logg.e(orderItembeans.size());
        for (int i = 0; i < orderItembeans.size(); i++) {
            list.add(orderItembeans.get(i).getGoodsid());
            listNum.add(orderItembeans.get(i).getNumber());
            listguiges.add(orderItembeans.get(i).getParam());
        }
        ids = list.toString().replace("[", "").replace("]", "").replace(",", "|").replace(" ", "");
        nums = listNum.toString().replace("[", "").replace("]", "").replace(",", "|").replace(" ", "");
        guiges = listguiges.toString().replace("[", "").replace("]", "").replace(",", "|");
        Logg.e("调起支付",ids+"===="+nums+"===="+guiges);
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        if (addrid != null && !addrid.equals("")) {
            maps.put("addressid", addrid);
        }
        maps.put("useJinbi","0");
        maps.put("ids", ids);
        maps.put("nums", nums);
        maps.put("guiges", guiges);
        maps.put("dianpuid", dianpuid);
        maps.put("ordernum",ordernum);
        maps.put("liuyans"," ");
        RetrofitClient.getInstance(context).createBaseApi().getOrderInfo(
                maps, new BaseObserver<String>(context) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            if (jsonObject.optString("status").equals("1")) {
                                Logg.json(jsonObject);
                                try {
                                    JSONObject object = new JSONObject(content);
                                    mPayModel.wxPay(object, new PayModel.wxListener() {
                                        @Override
                                        public void onResult(PayReq req) {
                                            mReq = req;
                                            msgApi = WXAPIFactory.createWXAPI(context, Constants.APP_ID);
                                            msgApi.registerApp(Constants.APP_ID);
                                            msgApi.sendReq(mReq);
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                StringUtil.showToast(context, jsonObject.optString("errmsg"));
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

    /**
     * 订单中心操作接口
     */
    public interface OrderInterface {
        //删除订单
        void doDeleteOrder(String orderid);

        //取消订单
        void doQuXiaoOrder(String orderid);
        //确认订单
        void confirmOrder(String dianpuid,String orderid);
    }
}
