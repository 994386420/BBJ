package com.bbk.shopcar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.CofirmOrderBean;
import com.bbk.Bean.GoodsBean;
import com.bbk.activity.AddressMangerActivity;
import com.bbk.activity.BaseActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.ZiYingZeroBuyDetailActivty;
import com.bbk.adapter.ConfirmOrderAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.model.BaseService;
import com.bbk.model.PayModel;
import com.bbk.resource.Constants;
import com.bbk.resource.NewConstants;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.MyScrollViewNew;
import com.logg.Logg;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 确认订单
 */
public class ConfirmOrderActivity extends BaseActivity {
    @BindView(R.id.title_back_btn)
    ImageButton titleBackBtn;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.ll_address)
    LinearLayout llAddress;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.scrollView)
    MyScrollViewNew scrollView;
    @BindView(R.id.total_price)
    TextView totalPrice;
    @BindView(R.id.go_pay)
    TextView goPay;
    @BindView(R.id.tag)
    TextView tag;
    @BindView(R.id.tv_dikou)
    TextView tvDikou;
    @BindView(R.id.ck_dikou)
    CheckBox ckDikou;
    @BindView(R.id.ll_check)
    LinearLayout llCheck;
    @BindView(R.id.ll_add_address)
    LinearLayout llAddAddress;
    @BindView(R.id.tv_yongjin)
    TextView tvYongjin;
    @BindView(R.id.ck_yongjin)
    CheckBox ckYongjin;
    @BindView(R.id.ll_yongjin)
    LinearLayout llYongjin;
    private String ids, nums, guiges, liuyans, type = "0";
    private PayReq mReq;
    private PayModel mPayModel;
    private IWXAPI msgApi = null;
    private String addrid = "";
    CofirmOrderBean cofirmOrderBean;
    private String usejinbi = "0",useyongjin = "0";
    List<GoodsBean> goodsBeans;
    ConfirmOrderAdapter confirmOrderAdapter;
    private double totalShopPrice = 0.00,yongjinDiscountPrice = 0.00;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_order_layout);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ButterKnife.bind(this);
        View topView = findViewById(R.id.topbar_layout);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        mPayModel = BaseService.getPayModel(this);
        initVeiw();
        ids = getIntent().getStringExtra("ids");
        nums = getIntent().getStringExtra("nums");
        guiges = getIntent().getStringExtra("guiges");
        registerBoradcastReceiver();
        if (ids != null && nums != null && guiges != null) {
            queryMyOrderToPay();
        }

        /**
         * 金币抵扣选择监听
         */
        ckDikou.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    usejinbi = "1";
                    if (cofirmOrderBean.getTotal() != null) {
                        if (ckYongjin.isChecked()) {
                            //此时可抵扣佣金数
                            yongjinDiscountPrice = Double.parseDouble(cofirmOrderBean.getTotal()) - Double.parseDouble(cofirmOrderBean.getJinbimoney());
                            if (Double.parseDouble(cofirmOrderBean.getYongjin()) > yongjinDiscountPrice) {
                                tvYongjin.setText("可用" + Double.parseDouble(cofirmOrderBean.getYongjin()) + "佣金抵" + yongjinDiscountPrice + "元");
                            }else {
                                tvYongjin.setText("可用" + Double.parseDouble(cofirmOrderBean.getYongjin()) + "佣金抵" + cofirmOrderBean.getYongjin() + "元");
                            }


                            totalShopPrice = Double.parseDouble(cofirmOrderBean.getTotal()) - Double.parseDouble(cofirmOrderBean.getJinbimoney())-Double.parseDouble(cofirmOrderBean.getYongjin());
                            if (totalShopPrice < 0) {
                                totalPrice.setText("0.00");
                                return;
                            }
                            totalPrice.setText(doubleToString(totalShopPrice));
                            return;
                        }
                        //此时可抵扣佣金数
                        yongjinDiscountPrice = Double.parseDouble(cofirmOrderBean.getTotal()) - Double.parseDouble(cofirmOrderBean.getJinbimoney());
                        if (Double.parseDouble(cofirmOrderBean.getYongjin()) > yongjinDiscountPrice) {
                            tvYongjin.setText("可用" + Double.parseDouble(cofirmOrderBean.getYongjin()) + "佣金抵" + yongjinDiscountPrice + "元");
                        }else {
                            tvYongjin.setText("可用" + Double.parseDouble(cofirmOrderBean.getYongjin()) + "佣金抵" + cofirmOrderBean.getYongjin() + "元");
                        }

                        totalShopPrice = Double.parseDouble(cofirmOrderBean.getTotal()) - Double.parseDouble(cofirmOrderBean.getJinbimoney());
                        if (totalShopPrice < 0) {
                            totalPrice.setText("0.00");
                            return;
                        }
                        totalPrice.setText(doubleToString(totalShopPrice));
                    }
                } else {
                    usejinbi = "0";
                    if (cofirmOrderBean.getTotal() != null) {
                        if (ckYongjin.isChecked()) {
                            //此时可抵扣佣金数
                            yongjinDiscountPrice = Double.parseDouble(cofirmOrderBean.getTotal());
                            if (Double.parseDouble(cofirmOrderBean.getYongjin()) > yongjinDiscountPrice) {
                                tvYongjin.setText("可用" + Double.parseDouble(cofirmOrderBean.getYongjin()) + "佣金抵" + yongjinDiscountPrice + "元");
                            }else {
                                tvYongjin.setText("可用" + Double.parseDouble(cofirmOrderBean.getYongjin()) + "佣金抵" + cofirmOrderBean.getYongjin() + "元");
                            }
                            totalShopPrice = Double.parseDouble(cofirmOrderBean.getTotal())-Double.parseDouble(cofirmOrderBean.getYongjin());
                            if (totalShopPrice < 0) {
                                totalPrice.setText("0.00");
                                return;
                            }
                            totalPrice.setText(doubleToString(totalShopPrice));
                            return;
                        }

                        //此时可抵扣佣金数
                        yongjinDiscountPrice = Double.parseDouble(cofirmOrderBean.getTotal());
                        if (Double.parseDouble(cofirmOrderBean.getYongjin()) > yongjinDiscountPrice) {
                            tvYongjin.setText("可用" + Double.parseDouble(cofirmOrderBean.getYongjin()) + "佣金抵" + yongjinDiscountPrice + "元");
                        }else {
                            tvYongjin.setText("可用" + Double.parseDouble(cofirmOrderBean.getYongjin()) + "佣金抵" + cofirmOrderBean.getYongjin() + "元");
                        }

                        totalShopPrice = Double.parseDouble(cofirmOrderBean.getTotal());
                        if (totalShopPrice < 0) {
                            totalPrice.setText("0.00");
                            return;
                        }
                        totalPrice.setText(doubleToString(totalShopPrice));
                    }
                }
            }
        });

        /**
         * 佣金抵扣选择监听
         */
        ckYongjin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    useyongjin = "1";
                    if (cofirmOrderBean.getTotal() != null) {
                        if (ckDikou.isChecked()) {
                            totalShopPrice = Double.parseDouble(cofirmOrderBean.getTotal()) - Double.parseDouble(cofirmOrderBean.getYongjin()) - Double.parseDouble(cofirmOrderBean.getJinbimoney());
                            if (totalShopPrice < 0) {
                                totalPrice.setText("0.00");
                                return;
                            }
                            totalPrice.setText(doubleToString(totalShopPrice));
                            return;
                        }

//                        if (Double.parseDouble(cofirmOrderBean.getYongjin()) > Double.parseDouble(cofirmOrderBean.getTotal())){
//                            ckDikou.setClickable(false);
//                        }else {
//                            ckDikou.setClickable(true);
//                        }

                        totalShopPrice = Double.parseDouble(cofirmOrderBean.getTotal()) - Double.parseDouble(cofirmOrderBean.getYongjin());
                        if (totalShopPrice < 0) {
                            totalPrice.setText("0.00");
                            return;
                        }
                        totalPrice.setText(doubleToString(totalShopPrice));
                    }
                } else {
                    useyongjin = "0";
//                    ckDikou.setClickable(true);
                    if (cofirmOrderBean.getTotal() != null) {
                        if (ckDikou.isChecked()) {
                            totalShopPrice = Double.parseDouble(cofirmOrderBean.getTotal()) - Double.parseDouble(cofirmOrderBean.getJinbimoney());
                            if (totalShopPrice < 0) {
                                totalPrice.setText("0.00");
                                return;
                            }
                            totalPrice.setText(doubleToString(totalShopPrice));
                            return;
                        }
                        totalShopPrice = Double.parseDouble(cofirmOrderBean.getTotal());
                        if (totalShopPrice < 0) {
                            totalPrice.setText("0.00");
                            return;
                        }
                        totalPrice.setText(doubleToString(totalShopPrice));
                    }
                }
            }
        });
    }

    private void initVeiw() {
        titleText.setText("确认订单");
    }

    /**
     * 查询支付的订单
     */
    private void queryMyOrderToPay() {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        maps.put("ids", ids);
        maps.put("nums", nums);
        maps.put("guiges", guiges);
        if (addrid != null) {
            maps.put("addrid", addrid);
        }
        RetrofitClient.getInstance(this).createBaseApi().queryMyOrderToPay(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            if (jsonObject.optString("status").equals("1")) {
                                Logg.json(jsonObject);
                                cofirmOrderBean = JSON.parseObject(content, CofirmOrderBean.class);
                                addrid = cofirmOrderBean.getAddrid();
                                if (addrid != null) {
                                    llAddress.setVisibility(View.VISIBLE);
                                    llAddAddress.setVisibility(View.GONE);
                                } else {
                                    llAddress.setVisibility(View.GONE);
                                    llAddAddress.setVisibility(View.VISIBLE);
                                }
                                tvAddress.setText(cofirmOrderBean.getAddr());
                                tvName.setText(cofirmOrderBean.getReceiver());
                                tvPhone.setText(cofirmOrderBean.getPhone());
                                double total = Double.parseDouble(cofirmOrderBean.getTotal());
                                totalPrice.setText("¥" + doubleToString(total));

                                if (cofirmOrderBean.getUsejinbi() != null) {
                                    if (cofirmOrderBean.getUsejinbi().equals("0")) {
                                        tvDikou.setVisibility(View.GONE);
                                        ckDikou.setVisibility(View.GONE);
                                        llCheck.setBackgroundColor(getResources().getColor(R.color.__picker_common_primary));
                                        llCheck.setVisibility(View.VISIBLE);
                                    } else {
                                        llCheck.setVisibility(View.VISIBLE);
                                        llCheck.setBackgroundColor(getResources().getColor(R.color.white));
                                        tvDikou.setText("可用" + Double.parseDouble(cofirmOrderBean.getJinbimoney()) * 1000 + "鲸币抵" + cofirmOrderBean.getJinbimoney() + "元");
                                    }
                                }
                                if (cofirmOrderBean.getUseyongjin() != null) {
                                    if (cofirmOrderBean.getUseyongjin().equals("0")) {
                                        tvYongjin.setVisibility(View.GONE);
                                        ckYongjin.setVisibility(View.GONE);
                                        llYongjin.setBackgroundColor(getResources().getColor(R.color.__picker_common_primary));
                                        llYongjin.setVisibility(View.VISIBLE);
                                    } else {
                                        llYongjin.setVisibility(View.VISIBLE);
                                        llYongjin.setBackgroundColor(getResources().getColor(R.color.white));
                                        if (Double.parseDouble(cofirmOrderBean.getYongjin()) > total){
                                            tvYongjin.setText("可用" + Double.parseDouble(cofirmOrderBean.getYongjin()) + "佣金抵" + total + "元");
                                        }else {
                                            tvYongjin.setText("可用" + Double.parseDouble(cofirmOrderBean.getYongjin()) + "佣金抵" + cofirmOrderBean.getYongjin() + "元");
                                        }
                                    }
                                }
                                if (cofirmOrderBean.getTag() != null && !cofirmOrderBean.getTag().equals("")) {
                                    tag.setText(cofirmOrderBean.getTag());
                                } else {
                                    tag.setVisibility(View.GONE);
                                }
                                recyclerview.setHasFixedSize(true);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ConfirmOrderActivity.this,
                                        LinearLayoutManager.VERTICAL, false) {
                                    @Override
                                    public boolean canScrollVertically() {
                                        return false;
                                    }

                                };
                                scrollView.scrollTo(0, 0);
                                recyclerview.setLayoutManager(linearLayoutManager);
                                if (cofirmOrderBean.getGoods() != null) {
                                    goodsBeans = JSON.parseArray(cofirmOrderBean.getGoods(), GoodsBean.class);
                                    confirmOrderAdapter = new ConfirmOrderAdapter(ConfirmOrderActivity.this, goodsBeans);
                                    recyclerview.setAdapter(confirmOrderAdapter);
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
                        DialogSingleUtil.show(ConfirmOrderActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(ConfirmOrderActivity.this, e.message);
                    }
                });
    }

    /**
     * double转String,保留小数点后两位
     *
     * @param num
     * @return
     */
    public static String doubleToString(double num) {
        //使用0.00不足位补0，#.##仅保留有效位
        return new DecimalFormat("0.00").format(num);
    }

    @OnClick({R.id.title_back_btn, R.id.go_pay, R.id.ll_add_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_btn:
                finish();
                break;
            case R.id.go_pay:
                final List<String> list = new ArrayList<>();
                if (addrid != null) {
                    for (int i = 0; i < goodsBeans.size(); i++) {
                        try {
                            JSONArray jsonArray = new JSONArray(goodsBeans.get(i).getList());
                            for (int j = 0; j < jsonArray.length(); j++) {
                                list.add(goodsBeans.get(i).getLiuyan());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    liuyans = list.toString().replace("[", "").replace("]", "").replace(",", "|").replace(" ", "").replace("0", " ");
                    getOrderInfo();

                } else {
                    StringUtil.showToast(this, "请选择收货地址");
                }
                break;
            case R.id.ll_add_address:
                NewConstants.address = "0";
                Intent intent = new Intent(this, AddressMangerActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 调起支付订单
     */
    private void getOrderInfo() {
        goPay.setClickable(false);
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        maps.put("addressid", addrid);
        maps.put("useJinbi", usejinbi);
        maps.put("ids", ids);
        maps.put("nums", nums);
        maps.put("guiges", guiges);
        maps.put("liuyans", liuyans);
        maps.put("type", type);
        maps.put("useYongjin", useyongjin);
        RetrofitClient.getInstance(this).createBaseApi().getOrderInfo(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            /**
                             * status = 1 拉起微信支付
                             *        = 2 跳到订单
                             *        = 0 错误提示
                             */
                            if (jsonObject.optString("status").equals("1")) {
                                Logg.json(jsonObject);
                                try {
                                    JSONObject object = new JSONObject(content);
                                    NewConstants.refeshFlag = "1";
                                    NewConstants.refeshOrderFlag = "1";
                                    mPayModel.wxPay(object, new PayModel.wxListener() {
                                        @Override
                                        public void onResult(PayReq req) {
                                            mReq = req;
                                            msgApi = WXAPIFactory.createWXAPI(ConfirmOrderActivity.this, Constants.APP_ID);
                                            msgApi.sendReq(mReq);
                                            DialogSingleUtil.dismiss(0);
                                            finish();
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else if (jsonObject.optString("status").equals("2")){
                                Intent intent = new Intent(ConfirmOrderActivity.this,ShopOrderActivity.class);
                                intent.putExtra("status", "2");
                                startActivity(intent);
                                StringUtil.showToast(ConfirmOrderActivity.this,"购买成功");
                            }else {
                                StringUtil.showToast(ConfirmOrderActivity.this, jsonObject.optString("errmsg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        DialogSingleUtil.dismiss(0);
                        goPay.setClickable(true);
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(ConfirmOrderActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        goPay.setClickable(true);
                        StringUtil.showToast(ConfirmOrderActivity.this, e.message);
                    }
                });
    }

    /**
     * totaljin   不为0的时候调用
     * 增加参数type（0不用金币（可选金币抵扣）	1要使用金币）
     * 调起支付订单
     */
    private void getOrderInfoByJinbi() {
        goPay.setClickable(false);
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        maps.put("addressid", addrid);
        maps.put("useJinbi", usejinbi);
        maps.put("ids", ids);
        maps.put("nums", nums);
        maps.put("guiges", guiges);
        maps.put("liuyans", liuyans);
        maps.put("type", type);
        RetrofitClient.getInstance(this).createBaseApi().getOrderInfoByJinbi(
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
                                    NewConstants.refeshFlag = "1";
                                    NewConstants.refeshOrderFlag = "1";
                                    mPayModel.wxPay(object, new PayModel.wxListener() {
                                        @Override
                                        public void onResult(PayReq req) {
                                            mReq = req;
                                            msgApi = WXAPIFactory.createWXAPI(ConfirmOrderActivity.this, Constants.APP_ID);
//                            msgApi.registerApp(Constants.APP_ID);
                                            msgApi.sendReq(mReq);
                                            DialogSingleUtil.dismiss(0);
                                            finish();
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                StringUtil.showToast(ConfirmOrderActivity.this, jsonObject.optString("errmsg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        DialogSingleUtil.dismiss(0);
                        goPay.setClickable(true);
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(ConfirmOrderActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        goPay.setClickable(true);
                        StringUtil.showToast(ConfirmOrderActivity.this, e.message);
                    }
                });
    }

    @OnClick(R.id.ll_address)
    public void onViewClicked() {
        NewConstants.address = "0";
        Intent intent = new Intent(ConfirmOrderActivity.this, AddressMangerActivity.class);
        startActivity(intent);
    }

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(AddressMangerActivity.ACTION);
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            tvName.setText(intent.getStringExtra("receiver"));
            tvPhone.setText(intent.getStringExtra("phone"));
            tvAddress.setText(intent.getStringExtra("area") + intent.getStringExtra("street"));
            tag.setText(intent.getStringExtra("tag"));
            tag.setVisibility(View.VISIBLE);
            addrid = intent.getStringExtra("addrid");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryMyOrderToPay();
    }

}
