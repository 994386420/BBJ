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
import android.widget.TextView;

import com.bbk.Bean.ShopDianpuBean;
import com.bbk.Bean.ShopFenLeiBean;
import com.bbk.activity.BaseActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.UserLoginNewActivity;
import com.bbk.adapter.DianPuGridAdapter;
import com.bbk.adapter.ShopFenleiAdapter;
import com.bbk.model.presenter.ChaoZhiPresenter;
import com.bbk.model.tablayout.XTabLayout;
import com.bbk.model.view.DianpuSearchView;
import com.bbk.model.view.ZiyingFelileiView;
import com.bbk.shopcar.CarActivity;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.HomeLoadUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.ClearableEditText;
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
 * 全部分类
 */
public class ShopFenleiActivity extends BaseActivity implements CommonLoadingView.LoadingHandler{
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
    @BindView(R.id.img_tishi)
    ImageView imgTishi;
    @BindView(R.id.img_more_black)
    ImageView imgMoreBlack;
    @BindView(R.id.title_text)
    TextView titleText;
    ShopFenleiAdapter shopFenleiAdapter;
    private ChaoZhiPresenter chaoZhiPresenter = new ChaoZhiPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.home_types_layout);
        View topView = findViewById(R.id.lltype);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        ButterKnife.bind(this);
        refreshAndloda();
        chaoZhiPresenter.attachZiyingFelileiView(ziyingFelileiView);
        chaoZhiPresenter.queryZiyingProducttype();
        tablayout.setVisibility(View.GONE);
        mrecycler.setLayoutManager(new LinearLayoutManager(this));
        mrecycler.setHasFixedSize(true);
        progress.setLoadingHandler(this);
        imgTishi.setVisibility(View.GONE);
        imgMoreBlack.setVisibility(View.VISIBLE);
        titleText.setVisibility(View.VISIBLE);
        titleText.setText("全部分类");
    }

    /**
     * 刷新事件
     */
    private void refreshAndloda() {
        mPtrframe.setEnableLoadMore(false);
        mPtrframe.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                chaoZhiPresenter.queryZiyingProducttype();
            }
        });
    }

    /**
     * 商城搜索
     */
    private ZiyingFelileiView ziyingFelileiView = new ZiyingFelileiView() {
        @Override
        public void onSuccess(List<ShopFenLeiBean> shopFenLeiBeans) {
                mPtrframe.setEnableLoadMore(false);
                mrecycler.setVisibility(View.VISIBLE);
                progress.loadSuccess();
                if (shopFenLeiBeans != null && shopFenLeiBeans.size() > 0) {
                    shopFenleiAdapter = new ShopFenleiAdapter(ShopFenleiActivity.this, shopFenLeiBeans);
                    mrecycler.setAdapter(shopFenleiAdapter);
                } else {
                    mrecycler.setVisibility(View.GONE);
                    progress.setVisibility(View.VISIBLE);
                    progress.loadSuccess(true, "暂无该商品~");
                    mPtrframe.setEnableLoadMore(false);
                }
        }

        @Override
        public void onError(String result) {
            finishLoad();
            StringUtil.showToast(ShopFenleiActivity.this, result);
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
        chaoZhiPresenter.queryZiyingProducttype();
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
}
