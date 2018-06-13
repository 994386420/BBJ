package com.bbk.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.BrokerageDetailBean;
import com.bbk.adapter.BrokerageDetailAdapter;
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
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 收益报表
 */
public class BrokerageDetailActivity extends BaseActivity implements CommonLoadingView.LoadingHandler {

    String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
    @BindView(R.id.title_back_btn)
    ImageButton titleBackBtn;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.brokerage_detail_list)
    RecyclerView brokerageDetailList;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    String type;
    @BindView(R.id.progress)
    CommonLoadingView progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brokerage_detail_layout);
        ButterKnife.bind(this);
        View topView = findViewById(R.id.topbar_layout);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        refreshLayout.setEnableLoadMore(false);
        progress.setLoadingHandler(this);
        refreshAndloda();
        type = getIntent().getStringExtra("type");
        if (type != null) {
            init(type);
            queryUserBrokerage();
        }
    }

    private void refreshAndloda() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                queryUserBrokerage();
            }
        });
    }

    private void init(String type) {
        switch (type) {
            case "t1":
                titleText.setText("淘宝本月结算预估");
                break;
            case "t2":
                titleText.setText("淘宝上月结算预估");
                break;
            case "t3":
                titleText.setText("淘宝本月付款预估");
                break;
            case "t4":
                titleText.setText("淘宝上月付款预估");
                break;
            case "j1":
                titleText.setText("京东本月结算预估");
                break;
            case "j2":
                titleText.setText("京东上月结算预估");
                break;
            case "j3":
                titleText.setText("京东本月付款预估");
                break;
            case "j4":
                titleText.setText("京东上月付款预估");
                break;

        }
    }

    /**
     * 查询报表明细
     */
    private void queryUserBrokerage() {
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        maps.put("type", type);
        RetrofitClient.getInstance(this).createBaseApi().queryBrokerageDetail(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            if (jsonObject.optString("status").equals("1")) {
                                brokerageDetailList.setLayoutManager(new LinearLayoutManager(BrokerageDetailActivity.this));
                                brokerageDetailList.setHasFixedSize(true);
                                List<BrokerageDetailBean> brokerageDetailBean = JSON.parseArray(content, BrokerageDetailBean.class);
                                if (brokerageDetailBean != null && brokerageDetailBean.size() >0) {
                                    brokerageDetailList.setAdapter(new BrokerageDetailAdapter(BrokerageDetailActivity.this, brokerageDetailBean));
                                    brokerageDetailList.setVisibility(View.VISIBLE);
                                    progress.loadSuccess();
                                }else {
                                    progress.setVisibility(View.VISIBLE);
                                    progress.loadSuccess(true);
                                    brokerageDetailList.setVisibility(View.GONE);
                                }
                            } else {
                                StringUtil.showToast(BrokerageDetailActivity.this, jsonObject.optString("errmsg"));
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
                        DialogSingleUtil.show(BrokerageDetailActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        progress.setVisibility(View.VISIBLE);
                        progress.loadError();
                        brokerageDetailList.setVisibility(View.GONE);
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        StringUtil.showToast(BrokerageDetailActivity.this, e.message);
                    }
                });
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.title_back_btn)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void doRequestData() {
        progress.setVisibility(View.GONE);
        queryUserBrokerage();
    }
}
