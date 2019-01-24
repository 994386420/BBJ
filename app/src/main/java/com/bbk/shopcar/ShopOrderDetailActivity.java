package com.bbk.shopcar;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.OrderItembean;
import com.bbk.Bean.ShopOrderDetailBean;
import com.bbk.activity.BaseActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.adapter.OrderDetailItemAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.dialog.AlertDialog;
import com.bbk.model.BaseService;
import com.bbk.model.MainActivity;
import com.bbk.model.PayModel;
import com.bbk.resource.Constants;
import com.bbk.resource.NewConstants;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.HomeLoadUtil;
import com.bbk.util.ImmersedStatusbarUtils;
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
import butterknife.OnClick;

/**
 * 商城订单详情
 */
public class ShopOrderDetailActivity extends BaseActivity {


    @BindView(R.id.title_back_btn)
    ImageButton titleBackBtn;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.ll_wuliu)
    LinearLayout llWuliu;
    @BindView(R.id.ll_address)
    LinearLayout llAddress;
    @BindView(R.id.recyclerview_order_detail)
    RecyclerView recyclerviewOrderDetail;
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
    @BindView(R.id.tv_qr)
    TextView tvQr;
    @BindView(R.id.tv_pay)
    TextView tvPay;
    @BindView(R.id.tv_pl)
    TextView tvPl;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_wuliu_message)
    TextView tvWuliuMessage;
    @BindView(R.id.tv_wulitime)
    TextView tvWulitime;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_dianpu)
    TextView tvDianpu;
    @BindView(R.id.tv_yunfei)
    TextView tvYunfei;
    @BindView(R.id.tv_usejinbi)
    TextView tvUsejinbi;
    @BindView(R.id.tv_shifukuan)
    TextView tvShifukuan;
    @BindView(R.id.tv_ordernum)
    TextView tvOrdernum;
    @BindView(R.id.tv_xdtime)
    TextView tvXdtime;
    @BindView(R.id.tv_payway)
    TextView tvPayway;
    @BindView(R.id.tv_paytime)
    TextView tvPaytime;
    String orderid, state, dianpuid;
    @BindView(R.id.ll_order)
    LinearLayout llOrder;
    ShopOrderDetailBean shopOrderDetailBean;
    @BindView(R.id.tv_dikou)
    TextView tvDikou;
    @BindView(R.id.ck_dikou)
    CheckBox ckDikou;
    @BindView(R.id.ll_check)
    LinearLayout llCheck;
    @BindView(R.id.rl_dianpu)
    RelativeLayout rlDianpu;
    @BindView(R.id.ll_shifukuan)
    LinearLayout llShifukuan;
    @BindView(R.id.ll_lx_maijia)
    LinearLayout llLxMaijia;
    @BindView(R.id.ll_bohao)
    LinearLayout llBohao;
    @BindView(R.id.ll_copy)
    LinearLayout llCopy;
    @BindView(R.id.ll_userjinbi)
    LinearLayout llUserjinbi;
    @BindView(R.id.tv_youhui)
    TextView tvYouhui;
    @BindView(R.id.ll_youhui)
    LinearLayout llYouhui;
    @BindView(R.id.img_more_black)
    ImageView imgMoreBlack;
    @BindView(R.id.tv_useyongjin)
    TextView tvUseyongjin;
    @BindView(R.id.ll_useryongjin)
    LinearLayout llUseryongjin;
    @BindView(R.id.tv_yingfukuan)
    TextView tvYingfukuan;
    @BindView(R.id.ll_yingfukuan)
    LinearLayout llYingfukuan;
    private PayReq mReq;
    private PayModel mPayModel;
    private IWXAPI msgApi = null;
    private String addrid = "";
    private String usejinbi = "0";
    private String ids, nums, guiges;
    private String mKefuDescription;
    private String ordernum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_oreder_detail_layout);
        ButterKnife.bind(this);
        View topView = findViewById(R.id.topbar_layout);
        // 实现沉浸式状态栏
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        mPayModel = BaseService.getPayModel(this);
        initVeiw();
        if (getIntent().getStringExtra("orderid") != null && getIntent().getStringExtra("dianpuid") != null) {
            orderid = getIntent().getStringExtra("orderid");
            dianpuid = getIntent().getStringExtra("dianpuid");
            queryMyOrderDetail(orderid);
        }
        if (getIntent().getStringExtra("state") != null) {
            state = getIntent().getStringExtra("state");
            setVisible(state);
        }
        ckDikou.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    usejinbi = "1";
                } else {
                    usejinbi = "0";
                }
            }
        });
    }

    private void initVeiw() {
        imgMoreBlack.setVisibility(View.VISIBLE);
        titleText.setText("订单详情");
    }

    private void setVisible(String state) {
        switch (state) {
            case "-2":
                tvStatus.setText("交易关闭");
                llWuliu.setVisibility(View.GONE);
                tvDelete.setVisibility(View.VISIBLE);
                tvPay.setVisibility(View.GONE);
                tvQuxiao.setVisibility(View.GONE);
                tvTixing.setVisibility(View.GONE);
                tvWuliu.setVisibility(View.GONE);
                tvYanchang.setVisibility(View.GONE);
                tvQr.setVisibility(View.GONE);
                tvPl.setVisibility(View.GONE);
                llOrder.setVisibility(View.VISIBLE);
                llCheck.setVisibility(View.GONE);
                llUserjinbi.setVisibility(View.GONE);
                tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog(ShopOrderDetailActivity.this).builder().setTitle("确定删除订单？")
                                .setMsg("删除之后订单无法恢复，请慎重考虑")
                                .setPositiveButton("确认", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        deleteMyOrder("1", orderid, dianpuid);
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
                tvStatus.setText("退款中");
                llWuliu.setVisibility(View.GONE);
                tvDelete.setVisibility(View.GONE);
                tvPay.setVisibility(View.GONE);
                tvQuxiao.setVisibility(View.GONE);
                tvTixing.setVisibility(View.GONE);
                tvWuliu.setVisibility(View.GONE);
                tvYanchang.setVisibility(View.GONE);
                tvQr.setVisibility(View.GONE);
                tvPl.setVisibility(View.GONE);
                llOrder.setVisibility(View.GONE);
                llCheck.setVisibility(View.GONE);
                llUserjinbi.setVisibility(View.VISIBLE);
                break;
            case "0":
                tvStatus.setText("待付款");
                llWuliu.setVisibility(View.GONE);
                tvQuxiao.setVisibility(View.VISIBLE);
                tvPay.setVisibility(View.VISIBLE);
                tvDelete.setVisibility(View.GONE);
                tvTixing.setVisibility(View.GONE);
                tvWuliu.setVisibility(View.GONE);
                tvYanchang.setVisibility(View.GONE);
                tvQr.setVisibility(View.GONE);
                tvPl.setVisibility(View.GONE);
                llOrder.setVisibility(View.VISIBLE);
                llUserjinbi.setVisibility(View.GONE);
                tvQuxiao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog(ShopOrderDetailActivity.this).builder().setTitle("确定取消订单？")
                                .setMsg("是否确认取消订单")
                                .setPositiveButton("确认", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        deleteMyOrder("0", orderid, dianpuid);
                                    }
                                }).setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        }).show();
                    }
                });
                tvPay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NewConstants.refeshOrderFlag = "1";
                        getOrderInfo();
                    }
                });
                break;
            case "1":
                tvStatus.setText("待发货");
                llWuliu.setVisibility(View.GONE);
                tvTixing.setVisibility(View.GONE);
                tvDelete.setVisibility(View.GONE);
                tvPay.setVisibility(View.GONE);
                tvQuxiao.setVisibility(View.GONE);
                tvWuliu.setVisibility(View.GONE);
                tvYanchang.setVisibility(View.GONE);
                tvQr.setVisibility(View.GONE);
                tvPl.setVisibility(View.GONE);
                llOrder.setVisibility(View.GONE);
                llCheck.setVisibility(View.GONE);
                llUserjinbi.setVisibility(View.VISIBLE);
                break;
            case "2":
                tvStatus.setText("已发货");
                llWuliu.setVisibility(View.VISIBLE);
                tvWuliu.setVisibility(View.VISIBLE);
                tvYanchang.setVisibility(View.GONE);
                tvQr.setVisibility(View.VISIBLE);
                tvDelete.setVisibility(View.GONE);
                tvPay.setVisibility(View.GONE);
                tvQuxiao.setVisibility(View.GONE);
                tvTixing.setVisibility(View.GONE);
                tvPl.setVisibility(View.GONE);
                llOrder.setVisibility(View.VISIBLE);
                llCheck.setVisibility(View.GONE);
                llUserjinbi.setVisibility(View.VISIBLE);
                tvQr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog(ShopOrderDetailActivity.this).builder().setTitle("确定收货？")
                                .setMsg("是否确认收货")
                                .setPositiveButton("确认", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        receiptGoods(ordernum, dianpuid);
                                    }
                                }).setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        }).show();
                    }
                });
                tvWuliu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShopOrderDetailActivity.this, WuLiuActivity.class);
                        if (shopOrderDetailBean != null && shopOrderDetailBean.getExpressage() != null) {
                            intent.putExtra("expressnum", shopOrderDetailBean.getExpressage());
                        } else {
                            StringUtil.showToast(ShopOrderDetailActivity.this, "订单号不存在");
                        }
                        startActivity(intent);
                    }
                });
                break;
            case "3":
                tvStatus.setText("待评论");
                llWuliu.setVisibility(View.VISIBLE);
                tvWuliu.setVisibility(View.GONE);
                tvPl.setVisibility(View.GONE);
                tvDelete.setVisibility(View.GONE);
                tvPay.setVisibility(View.GONE);
                tvQuxiao.setVisibility(View.GONE);
                tvTixing.setVisibility(View.GONE);
                tvYanchang.setVisibility(View.GONE);
                tvQr.setVisibility(View.GONE);
                llOrder.setVisibility(View.VISIBLE);
                llCheck.setVisibility(View.GONE);
                llUserjinbi.setVisibility(View.VISIBLE);
                break;
            case "4":
                tvStatus.setText("交易成功");
                llWuliu.setVisibility(View.VISIBLE);
                tvDelete.setVisibility(View.GONE);
                tvPay.setVisibility(View.GONE);
                tvQuxiao.setVisibility(View.GONE);
                tvTixing.setVisibility(View.GONE);
                tvWuliu.setVisibility(View.GONE);
                tvYanchang.setVisibility(View.GONE);
                tvQr.setVisibility(View.GONE);
                tvPl.setVisibility(View.GONE);
                llOrder.setVisibility(View.GONE);
                llCheck.setVisibility(View.GONE);
                llUserjinbi.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 查询我的订单详情
     */
    private void queryMyOrderDetail(final String orderid) {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        maps.put("dianpuid", dianpuid);
        maps.put("ordernum", orderid);
        RetrofitClient.getInstance(this).createBaseApi().queryMyOrderDetail(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            if (jsonObject.optString("status").equals("1")) {
                                Logg.json(jsonObject);
                                llAddress.setVisibility(View.VISIBLE);
                                shopOrderDetailBean = JSON.parseObject(content, ShopOrderDetailBean.class);
                                addrid = shopOrderDetailBean.getAddrid();
                                tvAddress.setText(shopOrderDetailBean.getAddress());
                                tvDianpu.setText(shopOrderDetailBean.getDianpu());
                                tvName.setText(shopOrderDetailBean.getReceiver());
                                tvPhone.setText(shopOrderDetailBean.getPhone());
                                tvYunfei.setText("¥" + shopOrderDetailBean.getKuaidiM());
                                if (shopOrderDetailBean.getYouhui() != null) {
                                    llYouhui.setVisibility(View.VISIBLE);
                                    tvYouhui.setText(shopOrderDetailBean.getYouhui());
                                } else {
                                    llYouhui.setVisibility(View.GONE);
                                }
                                if (shopOrderDetailBean.getUsejinbi() != null && !shopOrderDetailBean.getUsejinbi().equals("")) {
                                    tvUsejinbi.setText("¥" +shopOrderDetailBean.getUsejinbi());
                                } else {
                                    tvUsejinbi.setText("0.00");
                                }

                                if (shopOrderDetailBean.getUseyongjin() != null && !shopOrderDetailBean.getUseyongjin().equals("")) {
                                    llUseryongjin.setVisibility(View.VISIBLE);
                                    tvUseyongjin.setText("¥" +shopOrderDetailBean.getUseyongjin());
                                } else {
                                    llUseryongjin.setVisibility(View.GONE);
                                }

                                if (shopOrderDetailBean.getTotalprice() != null && !shopOrderDetailBean.getTotalprice().equals("")) {
                                    llYingfukuan.setVisibility(View.VISIBLE);
                                    tvYingfukuan.setText("¥" +shopOrderDetailBean.getTotalprice());
                                } else {
                                    llYingfukuan.setVisibility(View.GONE);
                                }

                                tvWulitime.setText(shopOrderDetailBean.getWuliutime());
                                tvWuliuMessage.setText(shopOrderDetailBean.getWuliu());
                                ordernum = shopOrderDetailBean.getOrdernum();
                                tvOrdernum.setText("订单编号： " + ordernum);
                                tvXdtime.setText("下单时间： " + shopOrderDetailBean.getXdtime());
                                tvPayway.setText("支付方式： " + "微信");
                                if (shopOrderDetailBean.getZftime() != null && !shopOrderDetailBean.getZftime().equals("")) {
                                    tvPaytime.setVisibility(View.VISIBLE);
                                    tvPaytime.setText("支付时间： " + shopOrderDetailBean.getZftime());
                                    llShifukuan.setVisibility(View.VISIBLE);
                                    tvShifukuan.setText("¥" + shopOrderDetailBean.getSjprice());
                                } else {
                                    tvPaytime.setVisibility(View.GONE);
                                    llShifukuan.setVisibility(View.VISIBLE);
                                    tvShifukuan.setText("¥" + shopOrderDetailBean.getSjprice());
                                }
                                if (shopOrderDetailBean.getUsejinbi2() != null) {
                                    if (shopOrderDetailBean.getUsejinbi2().equals("0")) {
                                        tvDikou.setVisibility(View.GONE);
                                        ckDikou.setVisibility(View.GONE);
                                        llCheck.setBackgroundColor(getResources().getColor(R.color.__picker_common_primary));
                                    } else {
                                        llCheck.setVisibility(View.VISIBLE);
                                        llCheck.setBackgroundColor(getResources().getColor(R.color.white));
                                        tvDikou.setText("可用" + shopOrderDetailBean.getJinbi() + "鲸币抵" + shopOrderDetailBean.getJinbimoney() + "元");
                                    }
                                }
                                recyclerviewOrderDetail.setHasFixedSize(true);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShopOrderDetailActivity.this,
                                        LinearLayoutManager.VERTICAL, false) {
                                    @Override
                                    public boolean canScrollVertically() {
                                        return false;
                                    }

                                };
                                recyclerviewOrderDetail.setLayoutManager(linearLayoutManager);
                                List<OrderItembean> orderItembeans = JSON.parseArray(shopOrderDetailBean.getArr2(), OrderItembean.class);
                                List<String> list = new ArrayList<>();
                                List<String> listNum = new ArrayList<>();
                                List<String> listguiges = new ArrayList<>();
                                for (int i = 0; i < orderItembeans.size(); i++) {
                                    list.add(orderItembeans.get(i).getGoodsid());
                                    listNum.add(orderItembeans.get(i).getNumber());
                                    listguiges.add(orderItembeans.get(i).getParam());
                                }
                                ids = list.toString().replace("[", "").replace("]", "").replace(",", "|").replace(" ", "");
                                nums = listNum.toString().replace("[", "").replace("]", "").replace(",", "|").replace(" ", "");
                                guiges = listguiges.toString().replace("[", "").replace("]", "").replace(",", "|");
                                recyclerviewOrderDetail.setAdapter(new OrderDetailItemAdapter(ShopOrderDetailActivity.this, orderItembeans, state, orderid, dianpuid,ordernum));
                                if (state != null) {
                                    setVisible(state);
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
                        DialogSingleUtil.show(ShopOrderDetailActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(ShopOrderDetailActivity.this, e.message);
                    }
                });
    }

    @OnClick({R.id.title_back_btn, R.id.ll_wuliu, R.id.rl_dianpu, R.id.ll_lx_maijia, R.id.ll_bohao, R.id.ll_copy, R.id.img_more_black})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.title_back_btn:
                finish();
                break;
            case R.id.ll_wuliu:
                intent = new Intent(this, WuLiuActivity.class);
                if (shopOrderDetailBean != null) {
                    if (shopOrderDetailBean.getExpressage() != null) {
                        intent.putExtra("expressnum", shopOrderDetailBean.getExpressage());
                    }
                }
                startActivity(intent);
                break;
            case R.id.rl_dianpu:
                intent = new Intent(this, NewDianpuActivity.class);
                intent.putExtra("dianpuid", dianpuid);
                startActivity(intent);
                break;
            case R.id.ll_lx_maijia:
//                HomeLoadUtil.startChat(this);
                MainActivity.consultService(this);
                break;
            case R.id.ll_bohao:
                break;
            case R.id.ll_copy:
                ClipboardManager cm = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(ordernum);
                StringUtil.showToast(this, "订单号复制成功");
                break;
            case R.id.img_more_black:
                HomeLoadUtil.showItemPop(this, imgMoreBlack);
                break;
        }
    }


    /**
     * 调起支付订单
     */
    private void getOrderInfo() {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        if (addrid != null && !addrid.equals("")) {
            maps.put("addressid", addrid);
        }
        maps.put("useJinbi", usejinbi);
        maps.put("ids", ids);
        maps.put("nums", nums);
        maps.put("guiges", guiges);
        maps.put("dianpuid", dianpuid);
        maps.put("ordernum", orderid);
        maps.put("liuyans", " ");
        RetrofitClient.getInstance(this).createBaseApi().getOrderInfo(
                maps, new BaseObserver<String>(this) {
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
                                            msgApi = WXAPIFactory.createWXAPI(ShopOrderDetailActivity.this, Constants.APP_ID);
                                            msgApi.registerApp(Constants.APP_ID);
                                            msgApi.sendReq(mReq);
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                StringUtil.showToast(ShopOrderDetailActivity.this, jsonObject.optString("errmsg"));
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
                        DialogSingleUtil.show(ShopOrderDetailActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(ShopOrderDetailActivity.this, e.message);
                    }
                });
    }


    /**
     * 参数state(1为删除	0为取消)
     *
     * @param status
     * @param orderid
     */
    private void deleteMyOrder(final String status, final String orderid, String dianpuid) {
        final Context context = ShopOrderDetailActivity.this;
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
                                    StringUtil.showToast(context, "订单已删除");
                                    NewConstants.refeshOrderFlag = "1";
                                    Intent intent = new Intent(ShopOrderDetailActivity.this, ShopOrderActivity.class);
                                    startActivity(intent);
                                } else {
                                    NewConstants.refeshOrderFlag = "1";
                                    StringUtil.showToast(context, "订单已取消");
                                    state = "-2";
                                    queryMyOrderDetail(orderid);
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


    //    private void startChat() {
//        //
//        KFAPIs.startChat(this,
//                "bbjkfxz", // 1. 客服工作组ID(请务必保证大小写一致)，请在管理后台分配
//                "比比鲸客服", // 2. 会话界面标题，可自定义
//                null, // 3. 附加信息，在成功对接客服之后，会自动将此信息发送给客服;
//                // 如果不想发送此信息，可以将此信息设置为""或者null
//                true, // 4. 是否显示自定义菜单,如果设置为显示,请务必首先在管理后台设置自定义菜单,
//                // 请务必至少分配三个且只分配三个自定义菜单,多于三个的暂时将不予显示
//                // 显示:true, 不显示:false
//                5, // 5. 默认显示消息数量
//                //修改SDK自带的头像有两种方式，1.直接替换appkefu_message_toitem和appkefu_message_fromitem.xml里面的头像，2.传递网络图片自定义
//                "http://www.bibijing.com/images/zhanwei/logo.png",//6. 修改默认客服头像，如果不想修改默认头像，设置此参数为null
//                NewConstants.imgurl, //7. 修改默认用户头像, 如果不想修改默认头像，设置此参数为null
//                false, // 8. 默认机器人应答
//                false,  //9. 是否强制用户在关闭会话的时候 进行“满意度”评价， true:是， false:否
//                null);
//
//    }
    private void receiptGoods(final String orderid, String dianpuid) {
        Map<String, String> maps = new HashMap<String, String>();
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        maps.put("userid", userID);
        maps.put("ordernum", orderid);
        maps.put("dianpuid", dianpuid);
        RetrofitClient.getInstance(ShopOrderDetailActivity.this).createBaseApi().receiptGoods(
                maps, new BaseObserver<String>(ShopOrderDetailActivity.this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                StringUtil.showToast(ShopOrderDetailActivity.this, "确认收货成功");
                                NewConstants.refeshOrderFlag = "1";
                                Intent intent = new Intent(ShopOrderDetailActivity.this, ShopOrderActivity.class);
                                startActivity(intent);
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
                        DialogSingleUtil.show(ShopOrderDetailActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(ShopOrderDetailActivity.this, e.message);
                    }
                });
    }
}
