package com.bbk.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.ShensuBean;
import com.bbk.adapter.ShensuRecordAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.util.SystemUtil;
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
 * 申诉
 */
public class UserShenSuActivity extends BaseActivity implements CommonLoadingView.LoadingHandler {

    String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
    @BindView(R.id.title_back_btn)
    ImageButton titleBackBtn;
    @BindView(R.id.brokerage_detail_list)
    RecyclerView brokerageDetailList;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    int page = 1, x = 1;
    @BindView(R.id.progress)
    CommonLoadingView progress;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.medit)
    EditText medit;
    @BindView(R.id.msend)
    TextView msend;
    @BindView(R.id.ll_shensu)
    LinearLayout llShensu;
    private List<ShensuBean> shensuBeans;
    ShensuRecordAdapter shensuRecordAdapter;
    private int curclick = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shensu_record_layout);
        ButterKnife.bind(this);
        View topView = findViewById(R.id.topbar_layout);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        refreshLayout.setEnableLoadMore(false);
        brokerageDetailList.setLayoutManager(new LinearLayoutManager(UserShenSuActivity.this));
        brokerageDetailList.setHasFixedSize(true);
        progress.setLoadingHandler(this);
        tablayout.addTab(tablayout.newTab().setText("我要申诉"));
        tablayout.addTab(tablayout.newTab().setText("申诉记录"));
        tablayout.setTabMode(TabLayout.MODE_FIXED);
        tablayout.addOnTabSelectedListener(tabSelectedListener);
        llShensu.setVisibility(View.VISIBLE);
        refreshLayout.setVisibility(View.GONE);
        StringUtil.setIndicator(tablayout, 50, 50);
        refreshAndloda();
//        queryCpsOrderCheck();
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
     * table点击事件
     */
    TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            int j = tab.getPosition();
            if (j == 0) {
                curclick = 0;
                llShensu.setVisibility(View.VISIBLE);
                refreshLayout.setVisibility(View.GONE);
            } else if (j == 1) {
                llShensu.setVisibility(View.GONE);
                refreshLayout.setVisibility(View.VISIBLE);
                curclick = 1;
                page = 1;
                x = 1;
                queryCpsOrderCheck();
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    /**
     * 查询申诉列表
     */
    private void queryCpsOrderCheck() {
        refreshLayout.setNoMoreData(false);
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        maps.put("page", page + "");
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
                                        shensuRecordAdapter = new ShensuRecordAdapter(UserShenSuActivity.this, shensuBeans);
                                        brokerageDetailList.setAdapter(shensuRecordAdapter);
                                        brokerageDetailList.setVisibility(View.VISIBLE);
                                        progress.loadSuccess();
                                    } else {
                                        progress.setVisibility(View.VISIBLE);
                                        progress.loadSuccess(true);
                                        brokerageDetailList.setVisibility(View.GONE);
                                        refreshLayout.setEnableLoadMore(false);
                                    }
                                } else {
                                    if (shensuBeans != null && shensuBeans.size() > 0) {
                                        shensuRecordAdapter.notifyData(shensuBeans);
                                    } else {
                                        refreshLayout.finishLoadMoreWithNoMoreData();
                                    }
                                }
                            } else {
                                StringUtil.showToast(UserShenSuActivity.this, jsonObject.optString("errmsg"));
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
                        DialogSingleUtil.show(UserShenSuActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        progress.setVisibility(View.VISIBLE);
                        progress.loadError();
                        brokerageDetailList.setVisibility(View.GONE);
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        StringUtil.showToast(UserShenSuActivity.this, e.message);
                    }
                });
    }

    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void doRequestData() {
        progress.setVisibility(View.GONE);
        queryCpsOrderCheck();
    }

    @OnClick({R.id.title_back_btn, R.id.msend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_btn:
                finish();
                break;
            case R.id.msend:
                if (medit.getText().toString() != null && !medit.getText().toString().equals("")) {
                    /**
                     * 手机系统版本号，手机型号， 手机厂商
                     */
                    String string = SystemUtil.getDeviceBrand() + " " + SystemUtil.getSystemModel() + " android:" + SystemUtil.getSystemVersion();
                    final InputMethodManager inputManager =
                            (InputMethodManager) medit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(medit.getWindowToken(), 0);
                    insertCpsOrderCheck(medit.getText().toString());
                } else {
                    StringUtil.showToast(UserShenSuActivity.this, "请输入你要申诉的订单号");
                }
                break;
        }
    }


    private void insertCpsOrderCheck(String ddh) {
        Map<String, String> maps = new HashMap<String, String>();
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        maps.put("userid", userID);
        maps.put("ddh", ddh);
        RetrofitClient.getInstance(this).createBaseApi().insertCpsOrderCheck(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                StringUtil.showToast(UserShenSuActivity.this, "申诉成功");
                                medit.setText("");
                                DialogSingleUtil.dismiss(0);
//                                finish();
                                TabLayout.Tab tabAt = tablayout.getTabAt(1);
                                tabAt.select();
                            } else {
                                StringUtil.showToast(UserShenSuActivity.this, jsonObject.optString("errmsg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        DialogSingleUtil.dismiss(0);
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(UserShenSuActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(UserShenSuActivity.this, e.message);
                    }
                });
    }

}
