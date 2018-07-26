package com.bbk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.OrderListBean;
import com.bbk.adapter.FanLiOrderAdapter;
import com.bbk.client.BaseApiService;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.CommonLoadingView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 淘宝京东返利订单
 */
public class FanLiOrderActivity extends BaseActivity implements CommonLoadingView.LoadingHandler {


    @BindView(R.id.title_back_btn)
    ImageButton titleBackBtn;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.tablayout_status)
    TabLayout tablayoutStatus;
    @BindView(R.id.fanli_order_list)
    RecyclerView fanliOrderList;
    @BindView(R.id.progress)
    CommonLoadingView progress;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_shensu)
    FrameLayout llShensu;
    @BindView(R.id.title_back_btn_right)
    ImageButton titleBackBtnRight;
    @BindView(R.id.topbar_layout)
    LinearLayout topbarLayout;
    private String domain = "taobao", type = "";
    private int page = 1, x = 1;
    private List<OrderListBean> orderListBeans;
    private FanLiOrderAdapter fanLiOrderAdapter;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fanli_order_layout);
        ButterKnife.bind(this);
        View topView = findViewById(R.id.topbar_layout);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        initView();
        queryCpsOrderList();
        refreshAndloda();
    }

    private void initView() {
        progress.setLoadingHandler(this);
        tablayout.addTab(tablayout.newTab().setText("淘宝订单"));
        tablayout.addTab(tablayout.newTab().setText("京东订单"));
        tablayout.setTabMode(TabLayout.MODE_FIXED);
        tablayout.addOnTabSelectedListener(tabSelectedListener);
        StringUtil.setIndicator(tablayout, 30, 30);
        tablayoutStatus.addTab(tablayoutStatus.newTab().setText("全部"));
        tablayoutStatus.addTab(tablayoutStatus.newTab().setText("跟踪中"));
        tablayoutStatus.addTab(tablayoutStatus.newTab().setText("待发放"));
        tablayoutStatus.addTab(tablayoutStatus.newTab().setText("已发放"));
        tablayoutStatus.addTab(tablayoutStatus.newTab().setText("失效"));
        tablayoutStatus.setTabMode(TabLayout.MODE_FIXED);
        tablayoutStatus.addOnTabSelectedListener(tabSelectedListenerStatus);
        StringUtil.setIndicator(tablayoutStatus, 10, 10);
        fanliOrderList.setLayoutManager(new LinearLayoutManager(this));
        fanliOrderList.setHasFixedSize(true);
    }

    private void refreshAndloda() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                x = 1;
                page = 1;
                queryCpsOrderList();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                x = 2;
                page++;
                queryCpsOrderList();
            }
        });
    }

    /**
     * table点击事件
     */
    TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            int j = tab.getPosition();
            if (j == 0) {
                domain = "taobao";
            } else if (j == 1) {
                domain = "jd";
            }
            x = 1;
            page = 1;
            queryCpsOrderList();
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };
    TabLayout.OnTabSelectedListener tabSelectedListenerStatus = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            int j = tab.getPosition();
            if (j == 0) {
                type = "";
            } else if (j == 1) {
                type = "1";
            } else if (j == 2) {
                type = "2";
            } else if (j == 3) {
                type = "3";
            } else if (j == 4) {
                type = "4";
            }
            x = 1;
            page = 1;
            queryCpsOrderList();
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    /**
     * 查询返利订单列表
     */
    private void queryCpsOrderList() {
        refreshLayout.setNoMoreData(false);
        userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        maps.put("type", type);//1跟踪中 2待发放 3已发放 4取消    空字符串为全部
        maps.put("page", page + "");
        maps.put("domain", domain);
        RetrofitClient.getInstance(this).createBaseApi().queryCpsOrderList(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                String content = jsonObject.optString("content");
                                orderListBeans = JSON.parseArray(content, OrderListBean.class);
                                if (x == 1) {
                                    if (orderListBeans != null && orderListBeans.size() > 0) {
                                        refreshLayout.setEnableLoadMore(true);
                                        fanLiOrderAdapter = new FanLiOrderAdapter(FanLiOrderActivity.this, orderListBeans);
                                        fanliOrderList.setAdapter(fanLiOrderAdapter);
                                        fanliOrderList.setVisibility(View.VISIBLE);
                                        progress.loadSuccess();
                                    } else {
                                        progress.setVisibility(View.VISIBLE);
                                        fanliOrderList.setVisibility(View.GONE);
                                        progress.loadSuccess(true);
                                        progress.loadEmpty("完成交易1~10分钟查看订单");
                                        refreshLayout.setEnableLoadMore(false);
                                    }
                                } else {
                                    fanliOrderList.setVisibility(View.VISIBLE);
                                    progress.loadSuccess();
                                    if (orderListBeans != null && orderListBeans.size() > 0) {
                                        fanLiOrderAdapter.notifyData(orderListBeans);
                                    } else {
                                        refreshLayout.finishLoadMoreWithNoMoreData();
                                    }
                                }
                            } else {
                                StringUtil.showToast(FanLiOrderActivity.this, jsonObject.optString("errmsg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        DialogSingleUtil.dismiss(0);
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(FanLiOrderActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        progress.setVisibility(View.VISIBLE);
                        progress.loadError();
                        fanliOrderList.setVisibility(View.GONE);
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        StringUtil.showToast(FanLiOrderActivity.this, e.message);
                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void doRequestData() {
        progress.setVisibility(View.GONE);
        queryCpsOrderList();
    }

    @OnClick({R.id.title_back_btn, R.id.title_back_btn_right, R.id.ll_shensu})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.title_back_btn:
                finish();
                break;
            case R.id.title_back_btn_right:
                //常见问题跳转链接
                String url = BaseApiService.Base_URL + "mobile/user/question";
                intent = new Intent(FanLiOrderActivity.this, WebViewActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
                break;
            case R.id.ll_shensu:
                intent = new Intent(FanLiOrderActivity.this,UserShenSuActivity.class);
                startActivity(intent);
                break;
        }
    }
}
