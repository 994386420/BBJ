package com.bbk.model;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbk.Bean.ZeroBuyBean;
import com.bbk.activity.BaseActivity;
import com.bbk.activity.R;
import com.bbk.activity.WebViewActivity;
import com.bbk.adapter.ZeroBuyAdapter;
import com.bbk.adapter.ZeroBuyForOlderAdapter;
import com.bbk.model.presenter.ChaoZhiPresenter;
import com.bbk.model.tablayout.XTabLayout;
import com.bbk.model.view.ZeroBuyView;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.StringUtil;
import com.bbk.view.CommonLoadingView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 自营0元购
 */
public class ZiYingZeroBuyShopActivity extends BaseActivity implements CommonLoadingView.LoadingHandler {
    @BindView(R.id.title_back_btn)
    ImageButton titleBackBtn;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tablayout)
    XTabLayout tablayout;
    @BindView(R.id.mrecycler)
    RecyclerView mrecycler;
    @BindView(R.id.progress)
    CommonLoadingView progress;
    @BindView(R.id.mPtrframe)
    SmartRefreshLayout mPtrframe;
    @BindView(R.id.type_image)
    LinearLayout typeImage;
    @BindView(R.id.lltype)
    LinearLayout lltype;
    @BindView(R.id.type_grid)
    GridView typeGrid;
    @BindView(R.id.ll_shouqi)
    LinearLayout llShouqi;
    @BindView(R.id.fl_type)
    FrameLayout flType;
    @BindView(R.id.miaosha_status)
    GridView miaoshaStatus;
    @BindView(R.id.img_tishi)
    ImageView imgTishi;
    @BindView(R.id.fl_types)
    FrameLayout flTypes;
    private int page = 1, x = 1;
    ZeroBuyAdapter zeroBuyAdapter;
    ZeroBuyForOlderAdapter zeroBuyForOlderAdapter;
    private ChaoZhiPresenter chaoZhiPresenter = new ChaoZhiPresenter(this);
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.home_types_layout);
        View topView = findViewById(R.id.lltype);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        ButterKnife.bind(this);
        refreshAndloda();
        getType();
        chaoZhiPresenter.attachZeroBuyView(zeroBuyView);
        chaoZhiPresenter.queryCpsZeroBuyNew(page);
        tablayout.setVisibility(View.VISIBLE);
        flTypes.setBackgroundResource(R.color.color_line);
        tablayout.setxTabDisplayNum(2);
        tablayout.addTab(tablayout.newTab().setText("新用户专享"));
        tablayout.addTab(tablayout.newTab().setText("老用户专享"));
        tablayout.setOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                mPtrframe.setNoMoreData(false);
                page = 1;
                x = 1;
                position = tab.getPosition();
                if (tab.getPosition() == 0) {
                    chaoZhiPresenter.queryCpsZeroBuyNew(page);
                    return;
                }
                chaoZhiPresenter.queryZiyingZeroBuyForOld(page);
            }

            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(XTabLayout.Tab tab) {

            }
        });
        mrecycler.setLayoutManager(new LinearLayoutManager(this));
        mrecycler.setHasFixedSize(true);
        progress.setLoadingHandler(this);
        imgTishi.setVisibility(View.VISIBLE);
        if (getIntent().getStringExtra("isOlder") != null){
            XTabLayout.Tab tabAt;
            switch (getIntent().getStringExtra("isOlder")){
                case "yes":
                    tabAt = tablayout.getTabAt(1);
                    tabAt.select();
                    chaoZhiPresenter.queryZiyingZeroBuyForOld(page);
                    break;
                case "no":
                    tabAt = tablayout.getTabAt(0);
                    tabAt.select();
                    break;
            }
        }
    }

    private void getType() {
        titleText.setBackgroundResource(R.mipmap.title_04);
    }

    private void refreshAndloda() {
        mPtrframe.setEnableLoadMore(false);
        mPtrframe.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mPtrframe.setNoMoreData(false);
                x = 1;
                page = 1;
                if (position == 0) {
                    chaoZhiPresenter.queryCpsZeroBuyNew(page);
                    return;
                }
                chaoZhiPresenter.queryZiyingZeroBuyForOld(page);
            }
        });
        mPtrframe.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                x = 2;
                page++;
                if (position == 0) {
                    chaoZhiPresenter.queryCpsZeroBuyNew(page);
                    return;
                }
                chaoZhiPresenter.queryZiyingZeroBuyForOld(page);
            }
        });

    }

    /**
     * 自营0元购
     */
    private ZeroBuyView zeroBuyView = new ZeroBuyView() {
        @Override
        public void onSuccess(List<ZeroBuyBean> zeroBuyBeans, String bannerimg, final String bannerurl) {
            if (x == 1) {
                mPtrframe.setEnableLoadMore(true);
                mrecycler.setVisibility(View.VISIBLE);
                progress.loadSuccess();
                Glide.with(ZiYingZeroBuyShopActivity.this)
                        .load(bannerimg)
                        .priority(Priority.HIGH)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(imgTishi);
                imgTishi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ZiYingZeroBuyShopActivity.this, WebViewActivity.class);
                        intent.putExtra("url", bannerurl);
                        startActivity(intent);
                    }
                });
                if (zeroBuyBeans != null && zeroBuyBeans.size() > 0) {
                    if (position == 0) {
                        zeroBuyAdapter = new ZeroBuyAdapter(ZiYingZeroBuyShopActivity.this, zeroBuyBeans);
                        mrecycler.setAdapter(zeroBuyAdapter);
                        return;
                    }
                    zeroBuyForOlderAdapter = new ZeroBuyForOlderAdapter(ZiYingZeroBuyShopActivity.this, zeroBuyBeans);
                    mrecycler.setAdapter(zeroBuyForOlderAdapter);
                } else {
                    mPtrframe.setEnableLoadMore(false);
                    mrecycler.setVisibility(View.GONE);
                    progress.setVisibility(View.VISIBLE);
                    if (position == 0) {
                        progress.loadSuccess(true);
                        return;
                    }
                    progress.loadNoOlder();
                }
            } else {
                if (zeroBuyBeans != null && zeroBuyBeans.size() > 0) {
                    if (position == 0) {
                        zeroBuyAdapter.notifyData(zeroBuyBeans);
                        return;
                    }
                    zeroBuyForOlderAdapter.notifyData(zeroBuyBeans);
                } else {
                    mPtrframe.finishLoadMoreWithNoMoreData();
                }

            }
        }

        @Override
        public void onError(String result) {
            finishLoad();
            StringUtil.showToast(ZiYingZeroBuyShopActivity.this, result);
        }

        @Override
        public void onHide() {
            finishLoad();
        }

        @Override
        public void onFailed() {
            finishLoad();
            progress.loadError();
            progress.setVisibility(View.VISIBLE);
            mPtrframe.setEnableLoadMore(false);
            mrecycler.setVisibility(View.GONE);
        }

        @Override
        public void noData() {
            mPtrframe.finishLoadMoreWithNoMoreData();
        }

        @Override
        public void noDataFirst() {
            mrecycler.setVisibility(View.GONE);
            progress.setVisibility(View.VISIBLE);
            progress.loadSuccess(true);
            mPtrframe.setEnableLoadMore(false);
        }
    };

    private void finishLoad() {
        mPtrframe.finishRefresh();
        mPtrframe.finishLoadMore();
    }

    protected void onDestroy() {
        super.onDestroy();
        DialogSingleUtil.dismiss(0);
    }


    @OnClick({R.id.title_back_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_btn:
                finish();
                break;
        }
    }

    @Override
    public void doRequestData() {
        progress.setVisibility(View.GONE);
        x = 1;
        page = 1;
        if (position == 0) {
            chaoZhiPresenter.queryCpsZeroBuyNew(page);
            return;
        }
        chaoZhiPresenter.queryZiyingZeroBuyForOld(page);
    }

    @OnClick(R.id.img_tishi)
    public void onViewClicked() {
    }
}
