package com.bbk.shopcar;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.ShopOrderBean;
import com.bbk.activity.BaseActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.adapter.ShopOrderWaiCengAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.resource.NewConstants;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.HomeLoadUtil;
import com.bbk.util.HongbaoDialog;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.AdaptionSizeTextView;
import com.bbk.view.CommonLoadingView;
import com.logg.Logg;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商城订单
 */
public class ShopOrderActivity extends BaseActivity implements CommonLoadingView.LoadingHandler {


    @BindView(R.id.title_back_btn)
    ImageButton titleBackBtn;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_text2)
    TextView titleText2;
    @BindView(R.id.title_text1)
    TextView titleText1;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.henggang213)
    View henggang213;
    @BindView(R.id.mlistview)
    ListView mlistview;
    @BindView(R.id.progress)
    CommonLoadingView progress;
    @BindView(R.id.xrefresh)
    SmartRefreshLayout xrefresh;
    @BindView(R.id.img_more_black)
    ImageView imgMoreBlack;
    private int page = 1, x = 1, option;
    private List<ShopOrderBean> shopOrderBeans;
    private ShopOrderWaiCengAdapter shopOrderAdapter;
    private HongbaoDialog hongbaoDialog;
    private String hongbaoMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_order_layout);
        ButterKnife.bind(this);
        View topView = findViewById(R.id.topbar_layout);
        // 实现沉浸式状态栏
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        initVeiw();
        initData();
    }

    private void initVeiw() {
        imgMoreBlack.setVisibility(View.VISIBLE);
        progress.setLoadingHandler(this);
        titleText.setText("我的订单");
        tablayout.addTab(tablayout.newTab().setText("全部"));
        tablayout.addTab(tablayout.newTab().setText("待付款"));
        tablayout.addTab(tablayout.newTab().setText("待发货"));
        tablayout.addTab(tablayout.newTab().setText("待收货"));
        tablayout.addTab(tablayout.newTab().setText("待评论"));
        tablayout.setTabMode(TabLayout.MODE_FIXED);
        refreshAndloda();
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int j = tab.getPosition();
                if (j == 0) {
                    option = 0;
                    NewConstants.option = 0;
                } else if (j == 1) {
                    option = 1;
                    NewConstants.option = 1;
                } else if (j == 2) {
                    option = 2;
                    NewConstants.option = 1;
                } else if (j == 3) {
                    option = 3;
                    NewConstants.option = 1;
                } else if (j == 4) {
                    option = 4;
                    NewConstants.option = 1;
                }
                page = 1;
                x = 1;
                queryMyOrder();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void initData() {
        if (getIntent().getStringExtra("status") != null) {
            String status1 = getIntent().getStringExtra("status");
            int i = Integer.valueOf(status1);
            TabLayout.Tab tabAt = tablayout.getTabAt(i);
            tabAt.select();
        } else {
            page = 1;
            x = 1;
            queryMyOrder();
        }
    }

    /**
     * 查询订单列表
     */
    private void queryMyOrder() {
        xrefresh.setNoMoreData(false);
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("option", option + "");
        maps.put("userid", userID);
        maps.put("page", page + "");
        RetrofitClient.getInstance(this).createBaseApi().queryMyOrder(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            if (jsonObject.optString("status").equals("1")) {
                                shopOrderBeans = JSON.parseArray(content, ShopOrderBean.class);
                                Logg.json(jsonObject);
                                DialogSingleUtil.dismiss(0);
                                if (x == 1) {
                                    if (shopOrderBeans != null && shopOrderBeans.size() > 0) {
                                        xrefresh.setEnableLoadMore(true);
                                        shopOrderAdapter = new ShopOrderWaiCengAdapter(ShopOrderActivity.this, shopOrderBeans);
//                                        shopOrderAdapter.setOrderInterface(ShopOrderActivity.this);
                                        hongbaoMoney = shopOrderBeans.get(0).getAward();

                                        String isFirstResultUse = SharedPreferencesUtil.getSharedData(ShopOrderActivity.this, "isFirstHongbao", "isFirstHongbao");
                                        if (TextUtils.isEmpty(isFirstResultUse)) {
                                            isFirstResultUse = "yes";
                                        }
                                        if (isFirstResultUse.equals("yes")) {
                                            if (hongbaoMoney != null && !hongbaoMoney.equals("") && option == 2) {
                                                showHongbaoDialog(ShopOrderActivity.this);
                                                SharedPreferencesUtil.putSharedData(ShopOrderActivity.this, "isFirstHongbao", "isFirstHongbao", "no");
                                            }
                                        }
                                        mlistview.setAdapter(shopOrderAdapter);
                                        mlistview.setVisibility(View.VISIBLE);
                                        progress.loadSuccess();
                                    } else {
                                        progress.setVisibility(View.VISIBLE);
                                        mlistview.setVisibility(View.GONE);
                                        progress.loadSuccess(true);
                                        xrefresh.setEnableLoadMore(false);
                                    }
                                } else {
                                    mlistview.setVisibility(View.VISIBLE);
                                    progress.loadSuccess();
                                    if (shopOrderBeans != null && shopOrderBeans.size() > 0) {
                                        shopOrderAdapter.notifyData(shopOrderBeans);
                                    } else {
                                        xrefresh.finishLoadMoreWithNoMoreData();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        xrefresh.finishLoadMore();
                        xrefresh.finishRefresh();
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(ShopOrderActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        progress.setVisibility(View.VISIBLE);
                        progress.loadError();
                        mlistview.setVisibility(View.GONE);
                        xrefresh.finishLoadMore();
                        xrefresh.finishRefresh();
                        StringUtil.showToast(ShopOrderActivity.this, e.message);
                    }
                });
    }

    private void refreshAndloda() {
        xrefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                page = 1;
                x = 1;
                queryMyOrder();
            }
        });
        xrefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                page++;
                x = 2;
                queryMyOrder();
            }
        });
    }

    @Override
    public void doRequestData() {
        progress.setVisibility(View.GONE);
        page = 1;
        x = 1;
        queryMyOrder();
    }


    //    @Override
//    public void doDeleteOrder(String orderid) {
//        deleteMyOrder("1",orderid);
//    }
//
//    @Override
//    public void doQuXiaoOrder(String orderid) {
//        deleteMyOrder("0",orderid);
//    }
    public void showHongbaoDialog(final Context context) {
        if (hongbaoDialog == null || !hongbaoDialog.isShowing()) {
            hongbaoDialog = new HongbaoDialog(context, R.layout.hongbao_dialog_layout,
                    new int[]{R.id.mclose});
            hongbaoDialog.show();
            hongbaoDialog.setCanceledOnTouchOutside(true);
            AdaptionSizeTextView textView = hongbaoDialog.findViewById(R.id.tv_hongbao_money);
            if (hongbaoMoney != null) {
                textView.setText(hongbaoMoney);
            }
            LinearLayout llclose = hongbaoDialog.findViewById(R.id.mclose);
            llclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hongbaoDialog.dismiss();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NewConstants.refeshOrderFlag.equals("1")) {
            page = 1;
            x = 1;
            queryMyOrder();
        }
    }

    @OnClick({R.id.title_back_btn, R.id.img_more_black})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_btn:
                finish();
                break;
            case R.id.img_more_black:
                HomeLoadUtil.showItemPop(this,imgMoreBlack);
                break;
        }
    }
}
