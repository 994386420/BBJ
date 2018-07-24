package com.bbk.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.BrokerageDetailBean;
import com.bbk.Bean.ShensuBean;
import com.bbk.adapter.BrokerageDetailAdapter;
import com.bbk.adapter.ShensuRecordAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.CommonLoadingView;
import com.logg.Logg;
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
 * 申诉记录
 */
public class ShesuRecordActivity extends BaseActivity implements CommonLoadingView.LoadingHandler {

    String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
    @BindView(R.id.title_back_btn)
    ImageButton titleBackBtn;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.brokerage_detail_list)
    RecyclerView brokerageDetailList;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    int page = 1,x = 1;
    @BindView(R.id.progress)
    CommonLoadingView progress;
    private List<ShensuBean> shensuBeans;
    ShensuRecordAdapter shensuRecordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brokerage_detail_layout);
        ButterKnife.bind(this);
        View topView = findViewById(R.id.topbar_layout);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        refreshLayout.setEnableLoadMore(false);
        brokerageDetailList.setLayoutManager(new LinearLayoutManager(ShesuRecordActivity.this));
        brokerageDetailList.setHasFixedSize(true);
        progress.setLoadingHandler(this);
        refreshAndloda();
        titleText.setText("申诉记录");
        queryCpsOrderCheck();
    }

    private void refreshAndloda() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                page = 1;
                x = 1;
                queryCpsOrderCheck();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                page++;
                x = 2;
                queryCpsOrderCheck();
            }
        });
    }


    /**
     * 查询申诉列表
     */
    private void queryCpsOrderCheck() {
        refreshLayout.setNoMoreData(false);
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        maps.put("page", page+"");
        RetrofitClient.getInstance(this).createBaseApi().queryCpsOrderCheck(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            if (jsonObject.optString("status").equals("1")) {
                                Logg.json(content);
                                JSONObject jsonObject1 = new JSONObject(content);
                                shensuBeans = JSON.parseArray(jsonObject1.optString("list"), ShensuBean.class);
                                if (x == 1) {
                                    if (shensuBeans != null && shensuBeans.size() > 0) {
                                        refreshLayout.setEnableLoadMore(true);
                                        shensuRecordAdapter = new ShensuRecordAdapter(ShesuRecordActivity.this, shensuBeans);
                                        brokerageDetailList.setAdapter(shensuRecordAdapter);
                                        brokerageDetailList.setVisibility(View.VISIBLE);
                                        progress.loadSuccess();
                                    } else {
                                        progress.setVisibility(View.VISIBLE);
                                        progress.loadSuccess(true);
                                        brokerageDetailList.setVisibility(View.GONE);
                                        refreshLayout.setEnableLoadMore(false);
                                    }
                                }else {
                                    if (shensuBeans != null && shensuBeans.size() > 0) {
                                        shensuRecordAdapter.notifyData(shensuBeans);
                                    } else {
                                       refreshLayout.finishLoadMoreWithNoMoreData();
                                    }
                                }
                            } else {
                                StringUtil.showToast(ShesuRecordActivity.this, jsonObject.optString("errmsg"));
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
                        DialogSingleUtil.show(ShesuRecordActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        progress.setVisibility(View.VISIBLE);
                        progress.loadError();
                        brokerageDetailList.setVisibility(View.GONE);
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        StringUtil.showToast(ShesuRecordActivity.this, e.message);
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
        queryCpsOrderCheck();
    }
}
