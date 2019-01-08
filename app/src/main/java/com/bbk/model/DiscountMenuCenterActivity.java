package com.bbk.model;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbk.Bean.DiscountMenuCenterBean;
import com.bbk.Bean.DiscountPersonBean;
import com.bbk.activity.BaseActivity;
import com.bbk.activity.R;
import com.bbk.adapter.DiscountMenuCenterAdapter;
import com.bbk.adapter.DiscountPersonAdapter;
import com.bbk.model.presenter.DiscountPresenter;
import com.bbk.model.tablayout.XTabLayout;
import com.bbk.model.view.DiscountCentenListView;
import com.bbk.model.view.DiscountCentenMenuView;
import com.bbk.model.view.DiscountView;
import com.bbk.util.HomeLoadUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.StringUtil;
import com.bbk.view.CommonLoadingView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 领券中心
 */
public class DiscountMenuCenterActivity extends BaseActivity implements CommonLoadingView.LoadingHandler {
    @BindView(R.id.title_back_btn)
    ImageButton titleBackBtn;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tablayout)
    XTabLayout tablayout;
    @BindView(R.id.mlistview)
    RecyclerView mrecycler;
    @BindView(R.id.progress)
    CommonLoadingView progress;
    @BindView(R.id.xrefresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.img_more_black)
    ImageView imgMoreBlack;
    private DiscountPresenter discountPresenter = new DiscountPresenter(this);
    private int page = 1,x = 1,showTime = 0;
    private String keyword = "";
    private DiscountMenuCenterAdapter discountMenuCenterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discount_menu_center_layout);
        ButterKnife.bind(this);
        //添加监听
        discountPresenter.attachCenterMenu(discountCentenMenuView);
        discountPresenter.attachCenterList(discountCentenListView);
        discountPresenter.queryCouponsCenterMenu();
        View topView = findViewById(R.id.topbar_layout);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        initVeiw();
    }


    private void initVeiw() {
        imgMoreBlack.setVisibility(View.VISIBLE);
        progress.setLoadingHandler(this);
        titleText.setText("领券中心");
        refreshAndloda();
        tablayout.setxTabDisplayNum(5);
        mrecycler.setLayoutManager(new LinearLayoutManager(this));
        mrecycler.setHasFixedSize(true);
        tablayout.setOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                refresh.setNoMoreData(false);
                page = 1;
                x = 1;
                keyword = tab.getText().toString();
                discountPresenter.queryCouponsCenterList(keyword,page);
            }

            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(XTabLayout.Tab tab) {

            }
        });
    }

    /**
     * 领劵中心列表
     */
    DiscountCentenListView discountCentenListView = new DiscountCentenListView() {
        @Override
        public void onSuccess(List<DiscountMenuCenterBean> discountPersonBeans) {

            if (x == 1) {
                if (discountPersonBeans != null && discountPersonBeans.size() > 0) {
                    refresh.setEnableLoadMore(true);
                    mrecycler.setVisibility(View.VISIBLE);
                    discountMenuCenterAdapter = new DiscountMenuCenterAdapter(DiscountMenuCenterActivity.this, discountPersonBeans);
                    mrecycler.setAdapter(discountMenuCenterAdapter);
                    progress.loadSuccess();
                } else {
                    mrecycler.setVisibility(View.GONE);
                    progress.setVisibility(View.VISIBLE);
                    progress.loadSuccess(true);
                    refresh.setEnableLoadMore(false);
                }
            } else {
                if (discountPersonBeans != null && discountPersonBeans.size() > 0) {
                    discountMenuCenterAdapter.notifyData(discountPersonBeans);
                } else {
                    refresh.finishLoadMoreWithNoMoreData();
                }
            }

        }

        @Override
        public void onError(String result) {
            StringUtil.showToast(DiscountMenuCenterActivity.this, result);
        }

        @Override
        public void onHide() {
            refresh.finishLoadMore();
            refresh.finishRefresh();
        }

        @Override
        public void onFailed() {
            progress.setVisibility(View.VISIBLE);
            progress.loadError();
            mrecycler.setVisibility(View.GONE);
            refresh.finishLoadMore();
            refresh.finishRefresh();
        }

    };

    /**
     * 领劵中心导航
     */
    DiscountCentenMenuView discountCentenMenuView = new DiscountCentenMenuView() {
        @Override
        public void onSuccess(JSONArray jsonArray) {
            if (showTime == 0) {
                showTime++;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        String keyword = jsonArray.getJSONObject(i).optString("keyword");
                        tablayout.addTab(tablayout.newTab().setText(keyword));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        public void onError(String result) {
            StringUtil.showToast(DiscountMenuCenterActivity.this, result);
        }

        @Override
        public void onHide() {
            refresh.finishLoadMore();
            refresh.finishRefresh();
        }

        @Override
        public void onFailed() {
            progress.setVisibility(View.VISIBLE);
            progress.loadError();
            mrecycler.setVisibility(View.GONE);
            refresh.finishLoadMore();
            refresh.finishRefresh();
        }
    };


    private void refreshAndloda() {
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                page = 1;
                x = 1;
                refresh.setNoMoreData(false);
                discountPresenter.queryCouponsCenterList(keyword,page);
            }
        });
        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                page++;
                x = 2;
                discountPresenter.queryCouponsCenterList(keyword,page);
            }
        });
    }

    @Override
    public void doRequestData() {
        progress.setVisibility(View.GONE);
        page = 1;
        x = 1;
        discountPresenter.queryCouponsCenterList(keyword,page);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick({R.id.title_back_btn, R.id.img_more_black})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_btn:
                finish();
                break;
            case R.id.img_more_black:
                HomeLoadUtil.showItemPop(this, imgMoreBlack);
                break;
        }
    }
}
