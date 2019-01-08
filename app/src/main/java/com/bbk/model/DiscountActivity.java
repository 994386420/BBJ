package com.bbk.model;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbk.Bean.DiscountPersonBean;
import com.bbk.activity.BaseActivity;
import com.bbk.activity.R;
import com.bbk.adapter.DiscountPersonAdapter;
import com.bbk.model.presenter.DiscountPresenter;
import com.bbk.model.tablayout.XTabLayout;
import com.bbk.model.view.DiscountView;
import com.bbk.util.HomeLoadUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.StringUtil;
import com.bbk.view.CommonLoadingView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 优惠券
 */
public class DiscountActivity extends BaseActivity implements CommonLoadingView.LoadingHandler {
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
    @BindView(R.id.tv_more_quan)
    TextView tvMoreQuan;
    @BindView(R.id.ll_more_quan)
    LinearLayout llMoreQuan;
    private DiscountPresenter discountPresenter = new DiscountPresenter(this);
    private int page = 1, x = 1, showtime = 0;
    private String state = "1";
    private DiscountPersonAdapter discountPersonAdapter;
    private String yishiyong1, weishiyong1, yiguoqi1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discount_layout);
        ButterKnife.bind(this);
        discountPresenter.attachView(discountView);
        discountPresenter.queryCouponsListByUserid(state, page);
        View topView = findViewById(R.id.topbar_layout);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        initVeiw();
    }


    private void initVeiw() {
        llMoreQuan.setVisibility(View.VISIBLE);
        imgMoreBlack.setVisibility(View.VISIBLE);
        progress.setLoadingHandler(this);
        titleText.setText("优惠券");
        refreshAndloda();
        tablayout.setxTabDisplayNum(3);
        mrecycler.setLayoutManager(new LinearLayoutManager(this));
        mrecycler.setHasFixedSize(true);
        tablayout.setOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                int j = tab.getPosition();
                refresh.setNoMoreData(false);
                page = 1;
                x = 1;
                if (j == 0) {
                    state = "1";
                } else if (j == 1) {
                    state = "-1";
                } else if (j == 2) {
                    state = "0";
                }
                discountPresenter.queryCouponsListByUserid(state, page);
            }

            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(XTabLayout.Tab tab) {

            }
        });
//        tablayout.addTab(tablayout.newTab().setText("未使用"));
//        tablayout.addTab(tablayout.newTab().setText("使用记录"));
//        tablayout.addTab(tablayout.newTab().setText("已过期"));
    }


    DiscountView discountView = new DiscountView() {
        @Override
        public void onSuccess(List<DiscountPersonBean> discountBeans, String yishiyong, String weishiyong, String yiguoqi) {
            yiguoqi1 = yiguoqi;
            yishiyong1 = yishiyong;
            weishiyong1 = weishiyong;
            if (showtime == 0) {
                showtime++;
                tablayout.addTab(tablayout.newTab().setText("未使用" + "(" + weishiyong1 + ")"));
                tablayout.addTab(tablayout.newTab().setText("使用记录" + "(" + yishiyong1 + ")"));
                tablayout.addTab(tablayout.newTab().setText("已过期" + "(" + yiguoqi1 + ")"));
            }
            if (x == 1) {
                if (discountBeans != null && discountBeans.size() > 0) {
                    refresh.setEnableLoadMore(true);
                    mrecycler.setVisibility(View.VISIBLE);
                    discountPersonAdapter = new DiscountPersonAdapter(DiscountActivity.this, discountBeans, state);
                    mrecycler.setAdapter(discountPersonAdapter);
                    progress.loadSuccess();
                } else {
                    mrecycler.setVisibility(View.GONE);
                    progress.setVisibility(View.VISIBLE);
                    progress.loadSuccess(true);
                    refresh.setEnableLoadMore(false);
                }
            } else {
                if (discountBeans != null && discountBeans.size() > 0) {
                    discountPersonAdapter.notifyData(discountBeans);
                } else {
                    refresh.finishLoadMoreWithNoMoreData();
//                    refresh.setEnableLoadMore(false);
                }
            }

        }

        @Override
        public void onError(String result) {
            StringUtil.showToast(DiscountActivity.this, result);
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

        @Override
        public void noData() {

        }

        @Override
        public void noDataFirst() {

        }
    };


    private void refreshAndloda() {
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                page = 1;
                x = 1;
                refresh.setNoMoreData(false);
                showtime = 0;
                tablayout.removeAllTabs();
                discountPresenter.queryCouponsListByUserid(state, page);
            }
        });
        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                page++;
                x = 2;
                discountPresenter.queryCouponsListByUserid(state, page);
            }
        });
    }

    @Override
    public void doRequestData() {
        progress.setVisibility(View.GONE);
        page = 1;
        x = 1;
        discountPresenter.queryCouponsListByUserid(state, page);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick({R.id.title_back_btn, R.id.img_more_black, R.id.tv_more_quan})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.title_back_btn:
                finish();
                break;
            case R.id.img_more_black:
                HomeLoadUtil.showItemPop(this, imgMoreBlack);
                break;
            case R.id.tv_more_quan:
                intent = new Intent(this, DiscountMenuCenterActivity.class);
                startActivity(intent);
                break;
        }
    }

}
