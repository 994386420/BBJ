package com.bbk.model;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bbk.Bean.ShopDianpuBean;
import com.bbk.Bean.ZeroBuyBean;
import com.bbk.activity.BaseActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.UserLoginNewActivity;
import com.bbk.activity.WebViewActivity;
import com.bbk.adapter.DianPuGridAdapter;
import com.bbk.adapter.ZeroBuyAdapter;
import com.bbk.model.presenter.ChaoZhiPresenter;
import com.bbk.model.tablayout.XTabLayout;
import com.bbk.model.view.DianpuSearchView;
import com.bbk.model.view.ZeroBuyView;
import com.bbk.shopcar.DianpuTypesActivity;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.HomeLoadUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.ClearableEditText;
import com.bbk.view.CommonLoadingView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.logg.Logg;
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
 * 店铺搜索
 */
public class DianpuSearchActivity extends BaseActivity implements CommonLoadingView.LoadingHandler ,View.OnKeyListener{
    @BindView(R.id.title_back_btn)
    ImageButton titleBackBtn;
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
    @BindView(R.id.topbar_search_input)
    ClearableEditText topbarSearchInput;
    @BindView(R.id.img_more_black)
    ImageView imgMoreBlack;
    private int page = 1, x = 1,showTime = 0;
    private String type = "0";
    DianPuGridAdapter dianPuGridAdapter;
    private ChaoZhiPresenter chaoZhiPresenter = new ChaoZhiPresenter(this);
    private String dianpuid,producttype,plevel,keyword,search;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.home_types_layout);
        View topView = findViewById(R.id.lltype);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        ButterKnife.bind(this);
        refreshAndloda();
        dianpuid = getIntent().getStringExtra("dianpuid");
        producttype = getIntent().getStringExtra("producttype");
        plevel = getIntent().getStringExtra("plevel");
        keyword = getIntent().getStringExtra("keyword");
        search = getIntent().getStringExtra("search");
        chaoZhiPresenter.attachDianpuSearchView(dianpuSearchView);
        chaoZhiPresenter.queryZiyingListByKeyword(dianpuid,producttype,keyword,plevel,page);
        tablayout.setVisibility(View.VISIBLE);
        tablayout.setxTabDisplayNum(5);
        mrecycler.setLayoutManager(new GridLayoutManager(this, 2));
        mrecycler.setHasFixedSize(true);
        progress.setLoadingHandler(this);
        imgTishi.setVisibility(View.GONE);
        topbarSearchInput.setVisibility(View.VISIBLE);
        topbarSearchInput.setOnKeyListener(this);
        if (search != null && !search.equals("")) {
            topbarSearchInput.setText(keyword);
        }
        imgMoreBlack.setVisibility(View.VISIBLE);
        tablayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        /**
         * tab选择
         */
        tablayout.setOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                mPtrframe.setNoMoreData(false);
                x = 1;
                page = 1;
                plevel = "3";
                producttype = tab.getText().toString();
                chaoZhiPresenter.queryZiyingListByKeyword(dianpuid, producttype, keyword, plevel, page);
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
     * 刷新事件
     */
    private void refreshAndloda() {
        mPtrframe.setEnableLoadMore(false);
        mPtrframe.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mPtrframe.setNoMoreData(false);
                x = 1;
                page = 1;
                plevel = "3";
                chaoZhiPresenter.queryZiyingListByKeyword(dianpuid,producttype,keyword,plevel,page);
            }
        });
        mPtrframe.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                x = 2;
                page++;
                plevel = "3";
                chaoZhiPresenter.queryZiyingListByKeyword(dianpuid,producttype,keyword,plevel,page);
            }
        });

    }

    /**
     * 商城搜索
     */
    private DianpuSearchView dianpuSearchView = new DianpuSearchView() {
        @Override
        public void onSuccess(List<ShopDianpuBean> shopDianpuBeans,String thirdLevels) {
            if (x == 1) {
                mPtrframe.setEnableLoadMore(true);
                mrecycler.setVisibility(View.VISIBLE);
                progress.loadSuccess();
                //显示tab选项
                try {
                    if (showTime == 0) {
                        showTime++;
                        JSONArray jsonArray = new JSONArray(thirdLevels);
                        if (jsonArray.length() > 0) {
                            position = jsonArray.length();
                            tablayout.setVisibility(View.VISIBLE);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                tablayout.addTab(tablayout.newTab().setText(jsonArray.optString(i)));
                            }
                        }else {
                            tablayout.setVisibility(View.GONE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //搜索结果展示
                if (shopDianpuBeans != null && shopDianpuBeans.size() > 0) {
                    dianPuGridAdapter = new DianPuGridAdapter(DianpuSearchActivity.this, shopDianpuBeans);
                    mrecycler.setAdapter(dianPuGridAdapter);
                } else {
                    mrecycler.setVisibility(View.GONE);
                    progress.setVisibility(View.VISIBLE);
                    progress.loadSuccess(true,"暂无该商品~");
                    mPtrframe.setEnableLoadMore(false);
                }
            } else {
                //翻页
                if (shopDianpuBeans != null && shopDianpuBeans.size() > 0) {
                    dianPuGridAdapter.notifyData(shopDianpuBeans);
                } else {
                    mPtrframe.finishLoadMoreWithNoMoreData();
                }

            }
        }

        @Override
        public void onError(String result) {
            finishLoad();
            StringUtil.showToast(DianpuSearchActivity.this, result);
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

    /**
     * 超时重试
     */
    @Override
    public void doRequestData() {
        progress.setVisibility(View.GONE);
        x = 1;
        page = 1;
        chaoZhiPresenter.queryZiyingListByKeyword(dianpuid,producttype,keyword,plevel,page);
    }
    @OnClick(R.id.img_more_black)
    public void onViewClicked() {
        Intent intent;
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        if (TextUtils.isEmpty(userID)) {
            intent = new Intent(this, UserLoginNewActivity.class);
            startActivityForResult(intent, 1);
        } else {
            HomeLoadUtil.showItemPop(this, imgMoreBlack);
        }
    }
    /**
     * 登陆回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        if (userID != null && !userID.equals("")) {
            switch (requestCode) {
                case 1:
                    HomeLoadUtil.showItemPop(this, imgMoreBlack);
                    break;
            }
        }
    }

    /**
     * 搜索监听
     * @param v
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            InputMethodManager imm = (InputMethodManager) v.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
            }
            doSearch();
            return true;
        }
        return false;
    }

    /**
     * 搜索事件
     */
    public void doSearch() {
        showTime = 0;//重置showtime
        tablayout.removeAllTabs();//移除所有tab
        producttype = "";
        keyword = topbarSearchInput.getText().toString();
        if (keyword == null || keyword.equals("")) {
            StringUtil.showToast(this, "搜索内容为空");
            return;
        }
        page = 1;
        page = 1;
        x = 1;
        plevel = "2";
        chaoZhiPresenter.queryZiyingListByKeyword(dianpuid,producttype,keyword,plevel,page);
    }
}
