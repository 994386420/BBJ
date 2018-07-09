package com.bbk.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.TiXianDetailBean;
import com.bbk.adapter.TiXianDetailAdapter;
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
 * 提现明细
 */
public class TiXianDetailActivity extends BaseActivity implements CommonLoadingView.LoadingHandler {

    String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
    @BindView(R.id.title_back_btn)
    ImageButton titleBackBtn;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.brokerage_detail_list)
    RecyclerView brokerageDetailList;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.progress)
    CommonLoadingView progress;
    int page = 1, x = 1;
    List<TiXianDetailBean> tiXianDetailBeans;
    TiXianDetailAdapter tiXianDetailAdapter;
    @BindView(R.id.tv_jinbi)
    TextView tvJinbi;
    @BindView(R.id.ll_tixian)
    LinearLayout llTixian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brokerage_detail_layout);
        ButterKnife.bind(this);
        View topView = findViewById(R.id.topbar_layout);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        progress.setLoadingHandler(this);
        refreshAndloda();
        titleText.setText("提现明细");
        llTixian.setVisibility(View.VISIBLE);
        queryYongjinListByUserid();
    }

    private void refreshAndloda() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                x = 1;
                page = 1;
                queryYongjinListByUserid();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                x = 2;
                page++;
                queryYongjinListByUserid();
            }
        });
    }


    /**
     * 查询提现明细
     */
    private void queryYongjinListByUserid() {
        refreshLayout.setNoMoreData(false);
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", "662");
        maps.put("page", page + "");
        RetrofitClient.getInstance(this).createBaseApi().queryYongjinListByUserid(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            JSONObject jsonObject1 = new JSONObject(content);
                            if (jsonObject.optString("status").equals("1")) {
                                brokerageDetailList.setLayoutManager(new LinearLayoutManager(TiXianDetailActivity.this));
                                brokerageDetailList.setHasFixedSize(true);
                                tvJinbi.setText("¥"+jsonObject1.optString("total")+"元");
                                tiXianDetailBeans = JSON.parseArray(jsonObject1.optString("list"), TiXianDetailBean.class);
                                if (x == 1) {
                                    if (tiXianDetailBeans != null && tiXianDetailBeans.size() > 0) {
                                        tiXianDetailAdapter = new TiXianDetailAdapter(TiXianDetailActivity.this, tiXianDetailBeans);
                                        brokerageDetailList.setAdapter(tiXianDetailAdapter);
                                        brokerageDetailList.setVisibility(View.VISIBLE);
                                        progress.loadSuccess();
                                    } else {
                                        progress.setVisibility(View.VISIBLE);
                                        progress.loadSuccess(true);
                                        brokerageDetailList.setVisibility(View.GONE);
                                    }
                                }else {
                                    brokerageDetailList.setVisibility(View.VISIBLE);
                                    progress.loadSuccess();
                                    if (tiXianDetailBeans != null && tiXianDetailBeans.size() > 0) {
                                        tiXianDetailAdapter.notifyData(tiXianDetailBeans);
                                    } else {
                                        refreshLayout.finishLoadMoreWithNoMoreData();
                                    }
                                }
                            } else {
                                StringUtil.showToast(TiXianDetailActivity.this, jsonObject.optString("errmsg"));
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
                        DialogSingleUtil.show(TiXianDetailActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        progress.setVisibility(View.VISIBLE);
                        progress.loadError();
                        brokerageDetailList.setVisibility(View.GONE);
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        StringUtil.showToast(TiXianDetailActivity.this, e.message);
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
        queryYongjinListByUserid();
    }
}
