package com.bbk.shopcar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbk.activity.AddressMangerActivity;
import com.bbk.activity.BaseActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.resource.NewConstants;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.AdaptionSizeTextView;
import com.bbk.view.MyScrollViewNew;
import com.bumptech.glide.Glide;
import com.logg.Logg;

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
 * O元购确认订单
 */
public class ConfirmOrderZeroBuyActivity extends BaseActivity {
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
    @BindView(R.id.scrollView)
    MyScrollViewNew scrollView;
    @BindView(R.id.go_pay)
    TextView goPay;
    @BindView(R.id.tag)
    TextView tag;
    @BindView(R.id.ll_add_address)
    LinearLayout llAddAddress;
    @BindView(R.id.item_img)
    ImageView itemImg;
    @BindView(R.id.item_title)
    TextView itemTitle;
    @BindView(R.id.tv_shop_guige)
    TextView tvShopGuige;
    @BindView(R.id.mdianpu)
    TextView mdianpu;
    @BindView(R.id.tv_price)
    AdaptionSizeTextView tvPrice;
    @BindView(R.id.total_price)
    TextView totalPrice;
    @BindView(R.id.tv_yunfei)
    AdaptionSizeTextView tvYunfei;
    @BindView(R.id.tv_adanum)
    AdaptionSizeTextView tvNum;
    @BindView(R.id.ll_fuwu)
    LinearLayout llFuwu;
    private String id, guige, imgurl, prcie, title, dianpu, jifen;
    private String addrid = "";
    private String yunfei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_order_zerobuy_layout);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ButterKnife.bind(this);
        View topView = findViewById(R.id.topbar_layout);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        id = getIntent().getStringExtra("id");
        guige = getIntent().getStringExtra("guige");
        imgurl = getIntent().getStringExtra("imgurl");
        prcie = getIntent().getStringExtra("price");
        title = getIntent().getStringExtra("title");
        dianpu = getIntent().getStringExtra("dianpu");
        jifen = getIntent().getStringExtra("jifen");
        yunfei = getIntent().getStringExtra("yunfei");
        initVeiw();
        queryAddrSingle();
    }

    private void initVeiw() {
        titleText.setText("确认订单");
        Glide.with(this).load(imgurl).into(itemImg);
        itemTitle.setText(title);
        tvShopGuige.setText(guige);
        mdianpu.setText(dianpu);
        tvPrice.setText(jifen + "积分");
        totalPrice.setText(jifen + "积分");
        tvNum.setText("共1件商品，小计");
        tvYunfei.setText(" ( 含运费 ¥" + yunfei + " )");
        registerBoradcastReceiver();
    }


    @OnClick({R.id.title_back_btn, R.id.go_pay, R.id.ll_add_address,R.id.ll_fuwu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_btn:
                finish();
                break;
            case R.id.go_pay:
                final List<String> list = new ArrayList<>();
                if (addrid != null) {
                    if (id != null && guige != null) {
                        getZeroBuyOrderOld(id, guige);
                    }
                } else {
                    StringUtil.showToast(this, "请选择收货地址");
                }
                break;
            case R.id.ll_add_address:
                NewConstants.address = "0";
                Intent intent = new Intent(this, AddressMangerActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_fuwu:
                showBaozhangeDialog(this);
                break;
        }
    }


    /**
     * 老用户0元购支付
     */
    private void getZeroBuyOrderOld(String id, String guige) {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        maps.put("id", id);
        maps.put("guige", guige);
        RetrofitClient.getInstance(this).createBaseApi().getZeroBuyOrderOld(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("2")) {
                                Logg.json(jsonObject);
                                Intent intent = new Intent(ConfirmOrderZeroBuyActivity.this, ShopOrderActivity.class);
                                intent.putExtra("status", "2");
                                startActivity(intent);
                                finish();
                                StringUtil.showToast(ConfirmOrderZeroBuyActivity.this, "购买成功");
                            } else {
                                StringUtil.showToast(ConfirmOrderZeroBuyActivity.this, jsonObject.optString("errmsg"));
                                if (jsonObject.optString("errmsg").contains("收货地址")) {
                                    Intent intent = new Intent(ConfirmOrderZeroBuyActivity.this, AddressMangerActivity.class);
                                    startActivity(intent);
                                }
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
                        DialogSingleUtil.show(ConfirmOrderZeroBuyActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        goPay.setClickable(true);
                        StringUtil.showToast(ConfirmOrderZeroBuyActivity.this, e.message);
                    }
                });
    }


    /**
     * 查询个人的默认地址
     */
    private void queryAddrSingle() {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        RetrofitClient.getInstance(this).createBaseApi().queryAddrSingle(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                JSONObject jsonObject1 = new JSONObject(jsonObject.optString("content"));
                                if (jsonObject1.has("id")) {
                                    addrid = jsonObject1.optString("id");
                                    llAddress.setVisibility(View.VISIBLE);
                                    llAddAddress.setVisibility(View.GONE);
                                    tvName.setText(jsonObject1.optString("receiver"));
                                    tvPhone.setText(jsonObject1.optString("phone"));
                                    tvAddress.setText(jsonObject1.optString("area") + jsonObject1.optString("street"));
                                    tag.setText(jsonObject1.optString("tag"));
                                    tag.setVisibility(View.VISIBLE);
                                } else {
                                    llAddress.setVisibility(View.GONE);
                                    llAddAddress.setVisibility(View.VISIBLE);
                                }
                            } else {
                                StringUtil.showToast(ConfirmOrderZeroBuyActivity.this, jsonObject.optString("errmsg"));
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
                        DialogSingleUtil.show(ConfirmOrderZeroBuyActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        goPay.setClickable(true);
                        StringUtil.showToast(ConfirmOrderZeroBuyActivity.this, e.message);
                    }
                });
    }


    @OnClick(R.id.ll_address)
    public void onViewClicked() {
        NewConstants.address = "0";
        Intent intent = new Intent(ConfirmOrderZeroBuyActivity.this, AddressMangerActivity.class);
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
            if (intent.getStringExtra("addrid") != null) {
                llAddAddress.setVisibility(View.GONE);
                llAddress.setVisibility(View.VISIBLE);
                tvName.setText(intent.getStringExtra("receiver"));
                tvPhone.setText(intent.getStringExtra("phone"));
                tvAddress.setText(intent.getStringExtra("area") + intent.getStringExtra("street"));
                tag.setText(intent.getStringExtra("tag"));
                tag.setVisibility(View.VISIBLE);
                addrid = intent.getStringExtra("addrid");
                return;
            }
            llAddAddress.setVisibility(View.VISIBLE);
            llAddress.setVisibility(View.GONE);
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
    }

}
