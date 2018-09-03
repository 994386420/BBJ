package com.bbk.shopcar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.DianPuHomeBean;
import com.bbk.Bean.PinpaiBean;
import com.bbk.Bean.ShopDianpuBean;
import com.bbk.activity.BaseActivity;
import com.bbk.activity.JumpDetailActivty;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.UserLoginNewActivity;
import com.bbk.adapter.DianPuGridAdapter;
import com.bbk.adapter.DianPuHoHotGridAdapter;
import com.bbk.adapter.DianPuHomePinpaiGridAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.dialog.HomeAlertDialog;
import com.bbk.model.tablayout.XTabLayout;
import com.bbk.shopcar.presenter.DianpuHomePresenter;
import com.bbk.shopcar.view.DianpuHomeView;
import com.bbk.shopcar.view.DianpuListView;
import com.bbk.util.DialogHomeUtil;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.EventIdIntentUtil;
import com.bbk.util.HomeLoadUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.CommonLoadingView;
import com.logg.Logg;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商城首页
 */
public class NewDianpuHomeActivity extends BaseActivity implements CommonLoadingView.LoadingHandler {
    @BindView(R.id.lin)
    LinearLayout lin;
    @BindView(R.id.ll_top)
    FrameLayout llTop;
    @BindView(R.id.banner)
    Banner mBanner;
    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.text1)
    TextView text1;
    @BindView(R.id.box1)
    LinearLayout box1;
    @BindView(R.id.img2)
    ImageView img2;
    @BindView(R.id.text2)
    TextView text2;
    @BindView(R.id.box2)
    LinearLayout box2;
    @BindView(R.id.img3)
    ImageView img3;
    @BindView(R.id.text3)
    TextView text3;
    @BindView(R.id.box3)
    LinearLayout box3;
    @BindView(R.id.img4)
    ImageView img4;
    @BindView(R.id.text4)
    TextView text4;
    @BindView(R.id.box4)
    LinearLayout box4;
    @BindView(R.id.img5)
    ImageView img5;
    @BindView(R.id.text5)
    TextView text5;
    @BindView(R.id.box5)
    LinearLayout box5;
    @BindView(R.id.to_top_btn)
    ImageButton imageButton;
    @BindView(R.id.progress)
    CommonLoadingView zLoadingView;
    @BindView(R.id.hot_recyclerview)
    RecyclerView hotRecyclerview;
    @BindView(R.id.pinpai_recyclerview)
    RecyclerView pinpaiRecyclerview;
    @BindView(R.id.title_back_btn1)
    ImageButton titleBackBtn1;
    @BindView(R.id.tv_dianpu_top)
    TextView tvDianpuTop;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.img_car)
    ImageButton imgCar;
    @BindView(R.id.toolbaretail)
    Toolbar toolbaretail;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.tablayout)
    XTabLayout tablayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_btn_bottom)
    LinearLayout llBtnBottom;
    @BindView(R.id.rl_home)
    RelativeLayout rlHome;
    @BindView(R.id.recyclerView)
    RecyclerView mrecyclerview;
    @BindView(R.id.loading_progress)
    CommonLoadingView loadingProgress;
    @BindView(R.id.img_more_black)
    ImageView imgMoreBlack;
    private int page = 1, x = 1;
    private String keyword = "";
    private int showTime = 0;
    DianPuGridAdapter dianPuGridAdapter;
    private List<Map<String, String>> titlelist;
    private boolean isshowGuanggao = true;
    JSONObject preguanggao;
    private DianpuHomePresenter dianpuHomePresenter = new DianpuHomePresenter(this);


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.new_dianpu_home_layout);
        View topView = findViewById(R.id.toolbaretail);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        ButterKnife.bind(this);
        dianpuHomePresenter.attachView(dianpuHomeView);
        dianpuHomePresenter.attachListView(dianpuListView);
        initView();
        setToolBar();
        dianpuHomePresenter.queryIndexMain(refreshLayout,mrecyclerview,zLoadingView);
        tablayout.setOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                Logg.e(tab.getText());
                loadingProgress.setVisibility(View.VISIBLE);
                loadingProgress.load();
                int j = tab.getPosition();
                keyword = tab.getText().toString();
                page = 1;
                x = 1;
                dianpuHomePresenter.queryProductListByKeyword("","",keyword,refresh,refreshLayout,loadingProgress,mrecyclerview,page);
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
     * 店铺首页数据
     */
    private DianpuHomeView dianpuHomeView = new DianpuHomeView() {
        @Override
        public void onSuccess(DianPuHomeBean dianPuHomeBean) {
            try {
                //店铺banner
                if (dianPuHomeBean.getBanner() != null) {
                    JSONArray banner = new JSONArray(dianPuHomeBean.getBanner());
                    if (banner != null && banner.length() > 0) {
                        HomeLoadUtil.loadbanner(NewDianpuHomeActivity.this, mBanner, banner);
                    }
                }
                List<Map<String, String>> taglist = new ArrayList<>();
                //店铺tag
                if (dianPuHomeBean.getTag() != null) {
                    JSONArray tag = new JSONArray(dianPuHomeBean.getTag());
                    if (tag != null && tag.length() > 0) {
                        HomeLoadUtil.loaddianpuTag(NewDianpuHomeActivity.this, taglist, tag, img1, img2, img3, img4, img5, text1, text2, text3, text4, text5, box1, box2, box3, box4, box5);
                    }
                }
                //分类
                if (dianPuHomeBean.getTypes() != null) {
                    JSONArray Types = new JSONArray(dianPuHomeBean.getTypes());
                    keyword = Types.getJSONObject(0).optString("keyword");
//                                    queryProductListByKeyword(keyword);
                    try {
                        if (showTime == 0) {
                            showTime++;
                            for (int i = 0; i < Types.length(); i++) {
                                String keyword = Types.getJSONObject(i).optString("name");
                                tablayout.addTab(tablayout.newTab().setText(keyword));
                            }
                        }
                        XTabLayout.Tab tabAt = tablayout.getTabAt(0);
                        tabAt.select();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //超值好货
                if (dianPuHomeBean.getHotlist() != null) {
                    List<ShopDianpuBean> shopDianpuBeans = JSON.parseArray(dianPuHomeBean.getHotlist(), ShopDianpuBean.class);
                    hotRecyclerview.setHasFixedSize(true);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NewDianpuHomeActivity.this);
                    linearLayoutManager.setOrientation(linearLayoutManager.HORIZONTAL);
                    hotRecyclerview.setLayoutManager(linearLayoutManager);
                    hotRecyclerview.setAdapter(new DianPuHoHotGridAdapter(NewDianpuHomeActivity.this, shopDianpuBeans));
                }
                //品牌优选
                if (dianPuHomeBean.getBrand() != null) {
                    List<PinpaiBean> pinpaiBeans = JSON.parseArray(dianPuHomeBean.getBrand(), PinpaiBean.class);
                    pinpaiRecyclerview.setHasFixedSize(true);
                    LinearLayoutManager linearLayoutManagePp = new LinearLayoutManager(NewDianpuHomeActivity.this);
                    linearLayoutManagePp.setOrientation(linearLayoutManagePp.HORIZONTAL);
                    pinpaiRecyclerview.setLayoutManager(linearLayoutManagePp);
                    pinpaiRecyclerview.setAdapter(new DianPuHomePinpaiGridAdapter(NewDianpuHomeActivity.this, pinpaiBeans));
                }

                if (dianPuHomeBean.getGuanggao() != null) {
                    if (isshowGuanggao) {
                        preguanggao = new JSONObject(dianPuHomeBean.getGuanggao());
                        new HomeAlertDialog(NewDianpuHomeActivity.this).builder()
                                .setimag(preguanggao.optString("img"))
                                .setonclick(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {
                                        EventIdIntentUtil.EventIdIntent(NewDianpuHomeActivity.this, preguanggao);
                                    }
                                }).show();
                        isshowGuanggao = false;
                    }

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onError(String result) {
            StringUtil.showToast(NewDianpuHomeActivity.this,result);
        }
    };

    /**
     *商品列表
     */
    private DianpuListView dianpuListView = new DianpuListView() {
        @Override
        public void onSuccess(List<ShopDianpuBean> shopDianpuBeans) {
            if (x == 1) {
                if (shopDianpuBeans != null && shopDianpuBeans.size() > 0) {
                    refresh.setEnableLoadMore(true);
                    mrecyclerview.setVisibility(View.VISIBLE);
                    loadingProgress.loadSuccess();
                    dianPuGridAdapter = new DianPuGridAdapter(NewDianpuHomeActivity.this, shopDianpuBeans);
                    mrecyclerview.setAdapter(dianPuGridAdapter);
                } else {
                    mrecyclerview.setVisibility(View.GONE);
                    loadingProgress.loadSuccess(true);
                    refresh.setEnableLoadMore(false);
                }
            } else {
                if (shopDianpuBeans != null && shopDianpuBeans.size() > 0) {
                    dianPuGridAdapter.notifyData(shopDianpuBeans);
                } else {
                    StringUtil.showToast(NewDianpuHomeActivity.this, "没有更多了");
                    refresh.setEnableLoadMore(false);
                }
            }
        }

        @Override
        public void onError(String result) {
            StringUtil.showToast(NewDianpuHomeActivity.this,result);
        }
    };
    /**
     * 初始化setToolBar
     */
    private void setToolBar() {
        toolbaretail.setTitleTextColor(Color.WHITE);
        toolbarLayout.setTitleEnabled(false);
        toolbarLayout.setExpandedTitleGravity(Gravity.CENTER);//设置展开后标题的位置
        toolbarLayout.setCollapsedTitleGravity(Gravity.CENTER);//设置收缩后标题的位置
        toolbarLayout.setExpandedTitleColor(Color.WHITE);//设置展开后标题的颜色
        toolbarLayout.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后标题的颜色
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                //verticalOffset  当前偏移量 appBarLayout.getTotalScrollRange() 最大高度 便宜值
                int Offset = Math.abs(verticalOffset); //目的是将负数转换为绝对正数；
                /**
                 * 当前最大高度便宜值除以2 在减去已偏移值 获取浮动 先显示在隐藏
                 */
                if (Offset == 0) {
                    llBtnBottom.setVisibility(View.GONE);
                    toolbaretail.setBackgroundResource(R.drawable.bg_home_yinying);
                    titleBackBtn1.setBackgroundResource(R.mipmap.shop_back_img);
                    tvDianpuTop.setTextColor(Color.WHITE);
                    imgMoreBlack.setBackgroundResource(R.mipmap.store_06);
                } else if (Offset < appBarLayout.getTotalScrollRange() / 2) {
                    toolbaretail.setTitle("");
                    float scale = (float) Offset / appBarLayout.getTotalScrollRange();
                    float alpha = (255 * scale);
                    toolbaretail.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                    titleBackBtn1.setBackgroundResource(R.mipmap.goback_btn);
                    imgMoreBlack.setBackgroundResource(R.mipmap.store_05);
                    tvDianpuTop.setTextColor(Color.argb((int) alpha, 0, 0, 0));
                    /**
                     * 从最低浮动开始渐显 当前 Offset就是  appBarLayout.getTotalScrollRange() / 2
                     * 所以 Offset - appBarLayout.getTotalScrollRange() / 2
                     */
                } else if (Offset > appBarLayout.getTotalScrollRange() / 2) {
                    toolbaretail.setTitle("");
                    toolbaretail.setBackgroundResource(R.color.white);
                    if (Offset == appBarLayout.getTotalScrollRange()) {
                        llBtnBottom.setVisibility(View.VISIBLE);
                    } else {
                        llBtnBottom.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void initView() {
        titlelist = new ArrayList<>();
        loadingProgress.setLoadingHandler(this);
        zLoadingView.setLoadingHandler(this);
        refresh();
        tablayout.setxTabDisplayNum(5);
        mrecyclerview.setHasFixedSize(true);
        mrecyclerview.setLayoutManager(new GridLayoutManager(NewDianpuHomeActivity.this, 2));
    }

    /**
     * 刷新事件
     */
    private void refresh() {
        zLoadingView.setLoadingHandler(this);
        refreshLayout.setEnableLoadMore(false);
        refresh.setEnableRefresh(false);
        refresh.setEnableAutoLoadMore(true);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayoutt) {
                dianpuHomePresenter.queryIndexMain(refreshLayout,mrecyclerview,zLoadingView);
                x = 1;
                page = 1;
                dianpuHomePresenter.queryProductListByKeyword("","",keyword,refresh,refreshLayout,loadingProgress,mrecyclerview,page);
            }
        });
        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayoutt) {
                page++;
                x = 2;
                dianpuHomePresenter.queryProductListByKeyword("","",keyword,refresh,refreshLayout,loadingProgress,mrecyclerview,page);
            }
        });
    }

    /**
     * 异常重试
     */
    @Override
    public void doRequestData() {
        DialogSingleUtil.show(this);
        zLoadingView.setVisibility(View.GONE);
        loadingProgress.setVisibility(View.GONE);
        dianpuHomePresenter.queryIndexMain(refreshLayout,mrecyclerview,zLoadingView);
        x = 1;
        page = 1;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DialogSingleUtil.dismiss(0);
    }

    /**
     * 点击事件
     *
     * @param view
     */
    @OnClick({R.id.to_top_btn, R.id.title_back_btn1, R.id.ll_back,R.id.img_more_black,R.id.img_car})
    public void onViewClicked(View view) {
        Intent intent;
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        switch (view.getId()) {
            case R.id.title_back_btn1:
                finish();
                break;
            case R.id.to_top_btn:
                //滑动到顶部
                mrecyclerview.scrollToPosition(0);
                CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) appBar.getLayoutParams()).getBehavior();
                if (behavior instanceof AppBarLayout.Behavior) {
                    AppBarLayout.Behavior appBarLayoutBehavior = (AppBarLayout.Behavior) behavior;
                    int topAndBottomOffset = appBarLayoutBehavior.getTopAndBottomOffset();
                    if (topAndBottomOffset != 0) {
                        appBarLayoutBehavior.setTopAndBottomOffset(0);
                        llBtnBottom.setVisibility(View.GONE);
                    }
                }
                break;
            case R.id.ll_back:
                finish();
                break;
            case R.id.img_more_black:
                if (TextUtils.isEmpty(userID)) {
                    intent = new Intent(this, UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    HomeLoadUtil.showItemPop(this,imgMoreBlack);
                }
                break;
            case R.id.img_car:
                if (TextUtils.isEmpty(userID)) {
                    intent = new Intent(this, UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(this, CarActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }
}
