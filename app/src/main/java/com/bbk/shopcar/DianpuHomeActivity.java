package com.bbk.shopcar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.DianPuHomeBean;
import com.bbk.Bean.PinpaiBean;
import com.bbk.Bean.ShopDianpuBean;
import com.bbk.activity.BaseActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.adapter.DianPuGridAdapter;
import com.bbk.adapter.DianPuHoHotGridAdapter;
import com.bbk.adapter.DianPuHomePinpaiGridAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.dialog.HomeAlertDialog;
import com.bbk.flow.DataFlow6;
import com.bbk.fragment.OnClickMallListioner;
import com.bbk.model.BaseService;
import com.bbk.model.PayModel;
import com.bbk.util.AnimationUtil;
import com.bbk.util.DialogHomeUtil;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.EventIdIntentUtil;
import com.bbk.util.HomeLoadUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.MallLoadUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.CommonLoadingView;
import com.bbk.view.MyScrollViewNew;
import com.logg.Logg;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
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
public class DianpuHomeActivity extends BaseActivity implements CommonLoadingView.LoadingHandler, MyScrollViewNew.ScrollViewListener, OnClickMallListioner, OnMultiPurposeListener {
    @BindView(R.id.lin)
    LinearLayout lin;
    @BindView(R.id.mbox)
    LinearLayout mbox2;
    @BindView(R.id.mhscrollview)
    HorizontalScrollView mhscrollview;
    @BindView(R.id.ll_type)
    LinearLayout llType;
    @BindView(R.id.scrollview)
    MyScrollViewNew scrollview;
    @BindView(R.id.ll_top)
    FrameLayout llTop;
    @BindView(R.id.mbox1)
    LinearLayout mboxtop;
    @BindView(R.id.mhscrollview1)
    HorizontalScrollView mhscrollviewtop;
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
    @BindView(R.id.mrecycler)
    RecyclerView mrecyclerview;
    public static SmartRefreshLayout refreshLayout;
    @BindView(R.id.to_top_btn)
    ImageButton imageButton;
    @BindView(R.id.progress)
    CommonLoadingView zLoadingView;
    @BindView(R.id.hot_recyclerview)
    RecyclerView hotRecyclerview;
    @BindView(R.id.pinpai_recyclerview)
    RecyclerView pinpaiRecyclerview;
    @BindView(R.id.ll_center)
    LinearLayout llCenter;
    @BindView(R.id.title_back_btn1)
    ImageButton titleBackBtn1;
    @BindView(R.id.tv_dianpu_top)
    TextView tvDianpuTop;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.img_car)
    ImageButton imgCar;
    private DataFlow6 dataFlow;
    private int page = 1, x = 1;
    private String type = "1", flag = "";
    private int imageHeight, height;
    private String keyword = "";
    private MallLoadUtil homeLoadUtil;
    private int showTime = 0;
    DianPuGridAdapter dianPuGridAdapter;
    private List<Map<String, String>> titlelist;
    private boolean isshowGuanggao = true;
    JSONObject preguanggao;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.dianpu_home_layout);
        ButterKnife.bind(this);
        homeLoadUtil = new MallLoadUtil(this, DianpuHomeActivity.this);
        View topView = findViewById(R.id.lin);
//        imageFenlei.setBackgroundResource(R.mipmap.fenlei_white);
        // 实现沉浸式状态栏
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        initView();
        initListeners();
        queryIndexMain();
    }


    private void initView() {
        titlelist = new ArrayList<>();
        refreshLayout = findViewById(R.id.refresh_root);
        zLoadingView.setLoadingHandler(this);
        refresh();
        mrecyclerview.setHasFixedSize(true);
        mrecyclerview.setNestedScrollingEnabled(false);
        ((SimpleItemAnimator) mrecyclerview.getItemAnimator()).setSupportsChangeAnimations(false);
        final LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);
        mrecyclerview.setLayoutManager(gridLayoutManager);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setEnableRefresh(false);
    }

    /**
     * 首页数据请求
     */
    private void queryIndexMain() {
        Map<String, String> maps = new HashMap<String, String>();
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        maps.put("userid", userID);
        RetrofitClient.getInstance(DianpuHomeActivity.this).createBaseApi().queryIndexMain(
                maps, new BaseObserver<String>(DianpuHomeActivity.this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            mrecyclerview.setVisibility(View.VISIBLE);
                            scrollview.setVisibility(View.VISIBLE);
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
//                                handler.sendEmptyMessageDelayed(3, 0);
//                                Logg.json(jsonObject);
                                DianPuHomeBean dianPuHomeBean = JSON.parseObject(jsonObject.optString("content"), DianPuHomeBean.class);
                                JSONArray banner = new JSONArray(dianPuHomeBean.getBanner());
                                if (banner != null && banner.length() > 0) {
                                    HomeLoadUtil.loadbanner(DianpuHomeActivity.this, mBanner, banner);
                                }
                                List<Map<String, String>> taglist = new ArrayList<>();
                                JSONArray tag = new JSONArray(dianPuHomeBean.getTag());
                                if (tag != null && tag.length() > 0) {
                                    HomeLoadUtil.loadTag(DianpuHomeActivity.this, taglist, tag, img1, img2, img3, img4, img5, text1, text2, text3, text4, text5, box1, box2, box3, box4, box5);
                                }
                                JSONArray Types = new JSONArray(dianPuHomeBean.getTypes());
                                keyword = Types.getJSONObject(0).optString("keyword");
                                flag = "1";
                                queryProductListByKeyword(keyword);
                                try {
                                    if (showTime == 0) {
                                        showTime++;
                                        loadtitlekeywords(Types, mbox2);
                                        loadtitlekeywordsTop(Types, mboxtop);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (dianPuHomeBean.getHotlist() != null) {
                                    List<ShopDianpuBean> shopDianpuBeans = JSON.parseArray(dianPuHomeBean.getHotlist(), ShopDianpuBean.class);
                                    hotRecyclerview.setHasFixedSize(true);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DianpuHomeActivity.this);
                                    linearLayoutManager.setOrientation(linearLayoutManager.HORIZONTAL);
                                    hotRecyclerview.setLayoutManager(linearLayoutManager);
                                    hotRecyclerview.setAdapter(new DianPuHoHotGridAdapter(DianpuHomeActivity.this, shopDianpuBeans));
                                }

                                if (dianPuHomeBean.getBrand() != null) {
                                    List<PinpaiBean> pinpaiBeans = JSON.parseArray(dianPuHomeBean.getBrand(), PinpaiBean.class);
                                    pinpaiRecyclerview.setHasFixedSize(true);
                                    LinearLayoutManager linearLayoutManagePp = new LinearLayoutManager(DianpuHomeActivity.this);
                                    linearLayoutManagePp.setOrientation(linearLayoutManagePp.HORIZONTAL);
                                    pinpaiRecyclerview.setLayoutManager(linearLayoutManagePp);
                                    pinpaiRecyclerview.setAdapter(new DianPuHomePinpaiGridAdapter(DianpuHomeActivity.this, pinpaiBeans));
                                }
                                JSONArray jsonArray = new JSONArray(dianPuHomeBean.getBrand());
//                                Logg.json(tag);


                                if (dianPuHomeBean.getGuanggao() != null) {
                                    if (isshowGuanggao) {
                                        preguanggao = new JSONObject(dianPuHomeBean.getGuanggao());
                                        new HomeAlertDialog(DianpuHomeActivity.this).builder()
                                                .setimag(preguanggao.optString("img"))
                                                .setonclick(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View arg0) {
                                                        EventIdIntentUtil.EventIdIntent(DianpuHomeActivity.this, preguanggao);
                                                    }
                                                }).show();
                                        isshowGuanggao = false;
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        refreshLayout.setEnableRefresh(true);
                        zLoadingView.loadSuccess();
                        DialogHomeUtil.dismiss(0);
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(DianpuHomeActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogHomeUtil.dismiss(0);
                        zLoadingView.setVisibility(View.VISIBLE);
                        zLoadingView.loadError();
                        scrollview.setVisibility(View.GONE);
                        mrecyclerview.setVisibility(View.GONE);
                        StringUtil.showToast(DianpuHomeActivity.this, e.message);
                    }
                });
    }


    private void queryProductListByKeyword(String keyword) {
        Logg.e(keyword+"===========>>>>>>>");
        refreshLayout.setNoMoreData(false);
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("dianpu", "");
        maps.put("keyword", keyword);
        maps.put("sortWay", "");
        maps.put("page", page + "");
        RetrofitClient.getInstance(DianpuHomeActivity.this).createBaseApi().queryProductListByKeyword(
                maps, new BaseObserver<String>(DianpuHomeActivity.this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            JSONObject jsonObject1 = new JSONObject(content);
                            if (jsonObject.optString("status").equals("1")) {
//                                Logg.json(content);
                                List<ShopDianpuBean> shopDianpuBeans = JSON.parseArray(jsonObject1.optString("list"), ShopDianpuBean.class);
                                //禁用滑动事件
                                mrecyclerview.setNestedScrollingEnabled(false);
                                mrecyclerview.setLayoutManager(new GridLayoutManager(DianpuHomeActivity.this, 2));
                                if (x == 1) {
                                    if (shopDianpuBeans != null && shopDianpuBeans.size() > 0) {
                                        refreshLayout.setEnableLoadMore(true);
                                        mrecyclerview.setVisibility(View.VISIBLE);
                                        dianPuGridAdapter = new DianPuGridAdapter(DianpuHomeActivity.this, shopDianpuBeans);
                                        mrecyclerview.setAdapter(dianPuGridAdapter);
                                    } else {
                                        mrecyclerview.setVisibility(View.GONE);
//                                        progress.setVisibility(View.VISIBLE);
//                                        progress.loadSuccess(true);
                                        refreshLayout.setEnableLoadMore(false);
                                    }
                                } else {
                                    if (shopDianpuBeans != null && shopDianpuBeans.size() > 0) {
                                        dianPuGridAdapter.notifyData(shopDianpuBeans);
                                    } else {
                                        refreshLayout.finishLoadMoreWithNoMoreData();
                                    }
                                }
                            } else {
                                StringUtil.showToast(DianpuHomeActivity.this, jsonObject.optString("errmsg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        refreshLayout.setEnableRefresh(true);
                        DialogSingleUtil.dismiss(0);
                    }

                    @Override
                    protected void showDialog() {
//                        DialogSingleUtil.show(DianpuHomeActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
//                        progress.loadError();
//                        progress.setVisibility(View.VISIBLE);
                        StringUtil.showToast(DianpuHomeActivity.this, e.message);
                    }
                });
    }

    /**
     * 刷新事件
     */
    private void refresh() {
        refreshLayout.setEnableFooterFollowWhenLoadFinished(true);
        refreshLayout.setEnableOverScrollDrag(true);
        refreshLayout.setEnableOverScrollBounce(true);
        refreshLayout.setEnableFooterTranslationContent(true);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                queryIndexMain();
                if (titlelist.size() > 0 && titlelist != null) {
                    homeLoadUtil.updateTitleMall(0, mbox2, keyword, mhscrollview);
                    homeLoadUtil.updateTitleTopMall(0, mboxtop, keyword, mhscrollviewtop);
                }
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                if (flag != null) {
                    if (flag.equals("0") || flag.equals("")) {
                        page++;
                        x = 2;
                        queryProductListByKeyword("");
                    } else {
                        page++;
                        x = 2;
                        queryProductListByKeyword(keyword);
                    }
                }
            }
        });
        refreshLayout.setOnMultiPurposeListener(this);
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 3:
                    break;
            }
        }
    };

    /**
     * 异常重试
     */
    @Override
    public void doRequestData() {
        DialogHomeUtil.show(this);
        zLoadingView.setVisibility(View.GONE);
        queryIndexMain();
        x = 1;
        page = 1;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DialogHomeUtil.dismiss(0);
    }


    /**
     * 加载分类菜单
     *
     * @param searchwords
     * @param mbox
     * @throws JSONException
     */
    private void loadtitlekeywords(JSONArray searchwords, LinearLayout mbox) throws JSONException {
        for (int i = 0; i < searchwords.length(); i++) {
            Map<String, String> map1 = new HashMap<>();
            String keyword = searchwords.getJSONObject(i).optString("name");
            map1.put("keyword", keyword);
            map1.put("isselect", "0");
            titlelist.add(map1);
            homeLoadUtil.addtitleMall(DianpuHomeActivity.this, keyword, i, mboxtop, mbox2, mhscrollviewtop, mhscrollview);
        }
    }

    private void loadtitlekeywordsTop(JSONArray searchwords, LinearLayout mbox) throws JSONException {
        for (int i = 0; i < searchwords.length(); i++) {
            String keyword = searchwords.getJSONObject(i).optString("name");
            homeLoadUtil.addtitleMallTop(DianpuHomeActivity.this, keyword, i, mboxtop, mbox2, mhscrollviewtop, mhscrollview);
        }
    }

    /**
     * 首页滑动监听
     *
     * @param scrollView
     * @param x
     * @param y
     * @param oldx
     * @param oldy
     */
    @Override
    public void onScrollChanged(MyScrollViewNew scrollView, int x, int y, int oldx, int oldy) {
        if (y <= 0) {
            lin.setBackgroundResource(R.drawable.bg_home_yinying);
            llType.setVisibility(View.GONE);
            imageButton.setVisibility(View.GONE);
            imgCar.setVisibility(View.GONE);
            lin.setVisibility(View.VISIBLE);
            tvDianpuTop.setTextColor(Color.WHITE);
            titleBackBtn1.setBackgroundResource(R.mipmap.shop_back_img);
        } else if (y > 0 && y <= imageHeight) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            float scale = (float) y / imageHeight;
            float alpha = (255 * scale);
            lin.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
            tvDianpuTop.setTextColor(Color.argb((int) alpha, 0, 0, 0));
            llType.setVisibility(View.GONE);
            imageButton.setVisibility(View.GONE);
            imgCar.setVisibility(View.GONE);
            lin.setVisibility(View.VISIBLE);
            if (y > 200) {
                titleBackBtn1.setBackgroundResource(R.mipmap.goback_btn);
            }
        } else {
            lin.setBackgroundResource(R.color.white);
            lin.setVisibility(View.VISIBLE);
            imgCar.setVisibility(View.VISIBLE);
            titleBackBtn1.setBackgroundResource(R.mipmap.goback_btn);
        }
        height = llCenter.getHeight();
        if (y > 0 && y <= height) {
            llType.setVisibility(View.GONE);
            imageButton.setVisibility(View.GONE);
            imgCar.setVisibility(View.GONE);
        } else if (y <= 0) {
            llType.setVisibility(View.GONE);
            imageButton.setVisibility(View.GONE);
            imgCar.setVisibility(View.GONE);
        } else {
            llType.setVisibility(View.VISIBLE);
            imageButton.setVisibility(View.VISIBLE);
            imgCar.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 获取顶部图片高度后，设置滚动监听
     */
    private void initListeners() {

        ViewTreeObserver vto = llCenter.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                llCenter.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                imageHeight = llCenter.getHeight();
                scrollview.setScrollViewListener(DianpuHomeActivity.this);
            }
        });
    }


    /**
     * 首页分类点击事件
     */
    @Override
    public void onMallClick(String text) {
        x = 1;
        page = 1;
        flag = "1";
        keyword = text;
        queryProductListByKeyword(keyword);
    }


    @Override
    public void onHeaderPulling(RefreshHeader header, float percent, int offset, int headerHeight, int extendHeight) {

    }

    @Override
    public void onHeaderReleased(RefreshHeader header, int headerHeight, int extendHeight) {

    }

    @Override
    public void onHeaderReleasing(RefreshHeader header, float percent, int offset, int headerHeight, int extendHeight) {

    }

    @Override
    public void onHeaderStartAnimator(RefreshHeader header, int headerHeight, int extendHeight) {

    }

    @Override
    public void onHeaderFinish(RefreshHeader header, boolean success) {

    }

    @Override
    public void onFooterPulling(RefreshFooter footer, float percent, int offset, int footerHeight, int extendHeight) {

    }

    @Override
    public void onFooterReleased(RefreshFooter footer, int footerHeight, int extendHeight) {

    }

    @Override
    public void onFooterReleasing(RefreshFooter footer, float percent, int offset, int footerHeight, int extendHeight) {

    }

    @Override
    public void onFooterStartAnimator(RefreshFooter footer, int footerHeight, int extendHeight) {

    }

    @Override
    public void onFooterFinish(RefreshFooter footer, boolean success) {

    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {

    }

    /**
     * 搜索框状态
     *
     * @param refreshLayout
     * @param oldState
     * @param newState
     */
    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        switch (refreshLayout.getState()) {
            case None:
                AnimationUtil.with().topMoveToViewLocation(lin, 500);
                lin.setVisibility(View.VISIBLE);
                break;
            case PullDownCanceled:
                AnimationUtil.with().topMoveToViewLocation(lin, 500);
                lin.setVisibility(View.VISIBLE);
                break;
            case Refreshing:
            case RefreshFinish:
            case RefreshReleased:
            case PullDownToRefresh:
                AnimationUtil.with().moveToViewTop(lin, 500);
                lin.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 点击事件
     *
     * @param view
     */
    @OnClick({R.id.to_top_btn, R.id.title_back_btn1, R.id.ll_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_btn1:
                finish();
                break;
            case R.id.to_top_btn:
                //滑动到顶部
                scrollview.scrollTo(0, 0);
                break;
            case R.id.ll_back:
                finish();
                break;
        }
    }

    @OnClick(R.id.img_car)
    public void onViewClicked() {
        Intent intent = new Intent(this,CarActivity.class);
        startActivity(intent);
    }
}
