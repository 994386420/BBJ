package com.bbk.fragment;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.ChaozhigouTypesBean;
import com.bbk.Bean.NewHomeCzgBean;
import com.bbk.activity.BidHomeActivity;
import com.bbk.activity.HomeActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.SearchMainActivity;
import com.bbk.activity.SortActivity;
import com.bbk.adapter.NewCzgAdapter;
import com.bbk.adapter.TypeGridAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.dialog.HomeAlertDialog;
import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.model.BaseService;
import com.bbk.model.PayModel;
import com.bbk.resource.Constants;
import com.bbk.resource.NewConstants;
import com.bbk.util.AnimationUtil;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.EventIdIntentUtil;
import com.bbk.util.HomeLoadUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.CommonLoadingView;
import com.bbk.view.MyScrollViewNew;
import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.header.BezierCircleHeader;
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
import com.tencent.mm.sdk.openapi.WXAPIFactory;
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
import butterknife.Unbinder;

/**
 * 首页
 */
public class NewHomeFragment extends BaseViewPagerFragment implements ResultEvent, CommonLoadingView.LoadingHandler, MyScrollViewNew.ScrollViewListener, OnClickHomeListioner, OnMultiPurposeListener {
    @BindView(R.id.lin)
    LinearLayout lin;
    @BindView(R.id.mbox)
    LinearLayout mbox2;
    @BindView(R.id.mhscrollview)
    HorizontalScrollView mhscrollview;
    @BindView(R.id.ll_type)
    LinearLayout llType;
    @BindView(R.id.type_image)
    LinearLayout typeImage;
    @BindView(R.id.type_grid)
    GridView typeGrid;
    @BindView(R.id.ll_shouqi)
    LinearLayout llShouqi;
    @BindView(R.id.fl_type)
    FrameLayout flType;
    @BindView(R.id.msearch)
    LinearLayout msearch;
    @BindView(R.id.msort)
    LinearLayout msort;
    @BindView(R.id.scrollview)
    MyScrollViewNew scrollview;
    @BindView(R.id.ll_top)
    LinearLayout llTop;
    @BindView(R.id.mbox1)
    LinearLayout mboxtop;
    @BindView(R.id.mhscrollview1)
    HorizontalScrollView mhscrollviewtop;
    @BindView(R.id.image_fenlei)
    ImageView imageFenlei;
    @BindView(R.id.tv_fenlei)
    TextView tvFenlei;
    @BindView(R.id.image_puba)
    ImageView imagePuba;
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
    @BindView(R.id.mviewflipper)
    ViewFlipper mviewflipper;
    @BindView(R.id.mrecycler)
    RecyclerView mrecyclerview;
    @BindView(R.id.refresh_root)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.huodongimg)
    ImageView huodongimg;
    @BindView(R.id.to_top_btn)
    ImageButton imageButton;
    @BindView(R.id.progress)
    CommonLoadingView zLoadingView;
    private DataFlow6 dataFlow;
    private View mView;
    private JSONArray banner = new JSONArray();
    private JSONArray tag = new JSONArray();
    private List<Map<String, String>> taglist = new ArrayList<>();
    private JSONArray gongneng = new JSONArray();
    private JSONArray fabiao = new JSONArray();
    private JSONArray chaozhigouTypes = new JSONArray();
    private int page = 1, x = 1;
    private String type = "1", flag = "";
    //第一次引导页是否显示隐藏
    private boolean isshowzhezhao = true;
    JSONObject jo, preguanggao;
    JSONObject object;
    private LinearLayout mSuspensionBar;
    private IWXAPI msgApi = null;
    private int data;// 支付结果标识
    private PayReq mReq;
    private PayModel mPayModel;
    List<NewHomeCzgBean> czgBeans;//超值购数据
    private String content;
    private int currentIndex = 0, currentIndexTop = 0;
    private List<Map<String, String>> titlelist;
    private int durationRotate = 700;// 旋转动画时间
    private int durationAlpha = 500;// 透明度动画时间
    private boolean isGlobalMenuShow = true;
    TypeGridAdapter typeGridAdapter;
    NewCzgAdapter newCzgAdapter;
    private int imageHeight, height;
    private int showTime = 0;
    private String keyword = "";
    List<ChaozhigouTypesBean> chaozhigouTypesBeans;
    private HomeLoadUtil homeLoadUtil;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (null != getActivity().getIntent().getStringExtra("content")) {
            String content = getActivity().getIntent().getStringExtra("content");
            try {
                isshowzhezhao = false;
                JSONObject jsonObject = new JSONObject(content);
                EventIdIntentUtil.EventIdIntent(getActivity(), jsonObject);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        if (null == mView) {
            getActivity().getWindow().setBackgroundDrawable(null);
            mView = inflater.inflate(R.layout.activity_new_home_layout, null);
            ButterKnife.bind(this, mView);
            homeLoadUtil = new HomeLoadUtil(getActivity(), NewHomeFragment.this);
            dataFlow = new DataFlow6(getContext());
            mPayModel = BaseService.getPayModel(getActivity());
            View topView = mView.findViewById(R.id.lin);
            imageFenlei.setBackgroundResource(R.mipmap.fenlei_white);
            // 实现沉浸式状态栏
            ImmersedStatusbarUtils.initAfterSetContentView(getActivity(), topView);
            initView(mView);
            initListeners();
        }
        return mView;

    }

    private void initView(View v) {
        zLoadingView.setLoadingHandler(this);
        refresh();
        //设置 Footer 为 球脉冲 样式
//        refreshLayout.setRefreshFooter(new BallPulseFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale).setNormalColor(getActivity().getResources().getColor(R.color.button_color)).setAnimatingColor(getActivity().getResources().getColor(R.color.button_color)));
        mrecyclerview.setHasFixedSize(true);
        mrecyclerview.setNestedScrollingEnabled(false);
        ((SimpleItemAnimator) mrecyclerview.getItemAnimator()).setSupportsChangeAnimations(false);
        mSuspensionBar = mView.findViewById(R.id.layout_click);
        final LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity());
        mrecyclerview.setLayoutManager(gridLayoutManager);
    }

    /**
     * 首页数据请求
     */
    private void initData() {
        Map<String, String> maps = new HashMap<String, String>();
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        maps.put("userid", userID);
        RetrofitClient.getInstance(getActivity()).createBaseApi().queryAppIndexInfo(
                maps, new BaseObserver<String>(getActivity()) {
                    @Override
                    public void onNext(String s) {
                        try {
                            mrecyclerview.setVisibility(View.VISIBLE);
                            scrollview.setVisibility(View.VISIBLE);
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                object = jsonObject.optJSONObject("content");
                                handler.sendEmptyMessageDelayed(3, 0);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        zLoadingView.loadSuccess();
                        DialogSingleUtil.dismiss(0);
                    }

                    @Override
                    protected void showDialog() {
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        zLoadingView.setVisibility(View.VISIBLE);
                        zLoadingView.loadError();
                        scrollview.setVisibility(View.GONE);
                        mrecyclerview.setVisibility(View.GONE);
                        mSuspensionBar.setVisibility(View.GONE);
                        StringUtil.showToast(getActivity(), e.message);
                    }
                });
    }

    private void initDataWx(boolean is) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("paytype", "wx");
        dataFlow.requestData(3, "appPayService/getOrderInfo", paramsMap, this, is);
    }

//    //首页分类数据
//    private void getIndexByType() {
//        refreshLayout.setNoMoreData(false);
//        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
//        Map<String, String> maps = new HashMap<String, String>();
//        maps.put("type", type);
//        maps.put("page", page + "");
//        maps.put("userid", userID);
//        RetrofitClient.getInstance(getActivity()).createBaseApi().queryAppIndexByType(
//                maps, new BaseObserver<String>(getActivity()) {
//                    @Override
//                    public void onNext(String s) {
//                        try {
//                            mrecyclerview.setVisibility(View.VISIBLE);
//                            scrollview.setVisibility(View.VISIBLE);
//                            JSONObject jsonObject = new JSONObject(s);
//                            if (jsonObject.optString("status").equals("1")) {
//                                content = jsonObject.optString("content");
////                                Log.i("=====", content);
//                                handler.sendEmptyMessageDelayed(2, 0);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    protected void hideDialog() {
//                        refreshLayout.finishLoadMore();
//                        refreshLayout.finishRefresh();
//                        zLoadingView.loadSuccess();
//                        DialogSingleUtil.dismiss(0);
//                    }
//
//                    @Override
//                    protected void showDialog() {
//                    }
//
//                    @Override
//                    public void onError(ExceptionHandle.ResponeThrowable e) {
//                        refreshLayout.finishLoadMore();
//                        refreshLayout.finishRefresh();
//                        DialogSingleUtil.dismiss(0);
//                        scrollview.setVisibility(View.GONE);
//                        zLoadingView.setVisibility(View.VISIBLE);
//                        zLoadingView.loadError();
//                        mrecyclerview.setVisibility(View.GONE);
//                        mSuspensionBar.setVisibility(View.GONE);
//                        StringUtil.showToast(getActivity(), e.message);
//                    }
//                });
//    }

    /**
     * 刷新事件
     */
    private void refresh() {
        refreshLayout.setPrimaryColorsId(R.color.button_color, android.R.color.white);//全局设置主题颜色
        refreshLayout.setEnableFooterFollowWhenLoadFinished(true);
        refreshLayout.setEnableOverScrollDrag(true);
        refreshLayout.setEnableOverScrollBounce(true);
        refreshLayout.setEnableFooterTranslationContent(true);
        //设置 Header 为 贝塞尔雷达 样式
        refreshLayout.setRefreshHeader(new BezierCircleHeader(getActivity()));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                initData();
                initDataCzg("");
                if (titlelist.size() > 0 && titlelist != null) {
                    homeLoadUtil.updateTitle(0, mbox2, keyword, mhscrollview);
                    homeLoadUtil.updateTitleTop(0, mboxtop, keyword, mhscrollviewtop);
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
                        initDataCzg("");
                    } else {
                        page++;
                        x = 2;
                        initDataCzg(keyword);
                    }
                }
            }
        });
        refreshLayout.setOnMultiPurposeListener(this);
    }

    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
        switch (requestCode) {
            case 3:
                Log.i("支付数据", content + "============");
                try {
                    JSONObject object = new JSONObject(content);
                    mPayModel.wxPay(object, new PayModel.wxListener() {
                        @Override
                        public void onResult(PayReq req) {
                            mReq = req;
                            msgApi = WXAPIFactory.createWXAPI(getActivity(), Constants.APP_ID);
//                            msgApi.registerApp(Constants.APP_ID);
                            msgApi.sendReq(mReq);
                            Log.i("调起微信支付", "============");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    break;
                case 2:
                    try {
                        czgBeans = JSON.parseArray(content, NewHomeCzgBean.class);
                        if (x == 1) {
                            if (czgBeans != null && czgBeans.size() > 0) {
                                refreshLayout.setEnableLoadMore(true);
                                mrecyclerview.setVisibility(View.VISIBLE);
                                newCzgAdapter = new NewCzgAdapter(getActivity(), czgBeans);
                                mrecyclerview.setAdapter(newCzgAdapter);
                            } else {
                                refreshLayout.setEnableLoadMore(false);
                                mrecyclerview.setVisibility(View.GONE);
                            }
                        } else if (x == 2) {
                            mrecyclerview.setVisibility(View.VISIBLE);
                            if (czgBeans != null && czgBeans.size() > 0) {
                                newCzgAdapter.notifyData(czgBeans);
                            } else {
                                refreshLayout.finishLoadMoreWithNoMoreData();
                            }
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    if (object.has("fubiao")) {
                        huodongimg.setVisibility(View.VISIBLE);
                        final JSONObject jo;
                        try {
                            jo = object.getJSONObject("fubiao");
                            Glide.with(getActivity()).load(jo.optString("img")).into(huodongimg);
                            huodongimg.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    EventIdIntentUtil.EventIdIntent(getActivity(), jo);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        huodongimg.setVisibility(View.GONE);
                    }
                    banner = object.optJSONArray("banner");
                    tag = object.optJSONArray("tag");
                    if (object.has("gongneng")) {
                        gongneng = object.optJSONArray("gongneng");
                    }
                    fabiao = object.optJSONArray("fabiao");
                    //chaozhigouTypes
                    if (object.has("chaozhigouTypes")) {
                        chaozhigouTypes = object.optJSONArray("chaozhigouTypes");
                        chaozhigouTypesBeans = JSON.parseArray(object.optString("chaozhigouTypes"), ChaozhigouTypesBean.class);
                        typeGridAdapter = new TypeGridAdapter(getActivity(), chaozhigouTypesBeans);
                        typeGrid.setAdapter(typeGridAdapter);
                        typeGrid.setOnItemClickListener(onItemClickListener);
                        try {
                            if (showTime == 0) {
                                showTime++;
                                loadtitlekeywords(chaozhigouTypes, mbox2);
                                loadtitlekeywordsTop(chaozhigouTypes, mboxtop);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        //判断传过来数据是否为null
                        if (banner != null && banner.length() > 0) {
                            HomeLoadUtil.loadbanner(getActivity(), mBanner, banner);
                        }
                        if (tag != null && tag.length() > 0) {
                            HomeLoadUtil.loadTag(getActivity(), taglist, tag, img1, img2, img3, img4, img5, text1, text2, text3, text4, text5, box1, box2, box3, box4, box5);
                        }
                        if (fabiao != null && fabiao.length() > 0) {
                            HomeLoadUtil.loadViewflipper(getActivity(), mviewflipper, fabiao);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (object.has("messages")) {
                        NewConstants.messages = object.optInt("messages");
                        if (HomeActivity.mNumImageView != null) {
                            HomeActivity.mNumImageView.setNum(NewConstants.messages);
                        }
                    }
                    /**
                     * eventid 为108 表示点击之后跳到登录页面。如果已经登录，则不显示preguanggao，显示guanggao
                     未登录 显示preguanggao
                     */
                    String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
                    if (TextUtils.isEmpty(userID)) {
                        if (object.has("preguanggao")) {
                            if (isshowzhezhao) {
                                preguanggao = object.optJSONObject("preguanggao");
                                new HomeAlertDialog(getActivity()).builder()
                                        .setimag(preguanggao.optString("img"))
                                        .setonclick(new OnClickListener() {
                                            @Override
                                            public void onClick(View arg0) {
                                                EventIdIntentUtil.EventIdIntent(getActivity(), preguanggao);
                                            }
                                        }).show();
                                isshowzhezhao = false;
                            }
                        }
                    } else {
                        if (object.has("guanggao")) {
                            if (isshowzhezhao) {
                                jo = object.optJSONObject("guanggao");
                                new HomeAlertDialog(getActivity()).builder()
                                        .setimag(jo.optString("img"))
                                        .setonclick(new OnClickListener() {
                                            @Override
                                            public void onClick(View arg0) {
                                                EventIdIntentUtil.EventIdIntent(getActivity(), jo);
                                            }
                                        }).show();
                                isshowzhezhao = false;
                            }
                        }
                    }
                    break;
                case 4:
                    x = 2;
                    page++;
//                    getIndexByType();
                    break;
                case 5:
                    initData();
                    break;
            }
        }
    };

    //超值购
//    private void mIdex(String str) {
//        type = str;
//        x = 1;
//        page = 1;
//        getIndexByType();
//    }

    /**
     * 懒加载
     */
    @Override
    protected void loadLazyData() {
        titlelist = new ArrayList<>();
        DialogSingleUtil.show(getActivity());
//        mIdex("1");
        x = 1;
        page = 1;
        initDataCzg("");
        initData();
    }

    /**
     * 异常重试
     */
    @Override
    public void doRequestData() {
        DialogSingleUtil.show(getActivity());
        zLoadingView.setVisibility(View.GONE);
        initData();
        x = 1;
        page = 1;
        initDataCzg("");
//        getIndexByType();
//        mIdex("1");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DialogSingleUtil.dismiss(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    /**
     * 加载分类菜单
     * @param searchwords
     * @param mbox
     * @throws JSONException
     */
    private void loadtitlekeywords(JSONArray searchwords, LinearLayout mbox) throws JSONException {
        Map<String, String> map = new HashMap<>();
        map.put("keyword", "超值购");
        map.put("isselect", "0");
        titlelist.add(map);
        homeLoadUtil.addtitle(getActivity(), "超值购", 0, mboxtop, mbox2, mhscrollviewtop, mhscrollview);
        for (int i = 0; i < searchwords.length(); i++) {
            Map<String, String> map1 = new HashMap<>();
            String keyword = searchwords.getJSONObject(i).optString("name");
            map1.put("keyword", keyword);
            map1.put("isselect", "0");
            homeLoadUtil.addtitle(getActivity(), keyword, i + 1, mboxtop, mbox2, mhscrollviewtop, mhscrollview);
        }
    }

    private void loadtitlekeywordsTop(JSONArray searchwords, LinearLayout mbox) throws JSONException {
        homeLoadUtil.addtitleTop(getActivity(), "超值购", 0, mboxtop, mbox2, mhscrollviewtop, mhscrollview);
        for (int i = 0; i < searchwords.length(); i++) {
            String keyword = searchwords.getJSONObject(i).optString("name");
            homeLoadUtil.addtitleTop(getActivity(), keyword, i + 1, mboxtop, mbox2, mhscrollviewtop, mhscrollview);
        }
    }


    /**
     * 显示菜单；图标动画
     */
    private void showGlobalMenu() {
        isGlobalMenuShow = !isGlobalMenuShow;
        if (isGlobalMenuShow) {
            ObjectAnimator.ofFloat(typeImage, "rotation", 180, 360)
                    .setDuration(durationRotate).start();
            ObjectAnimator.ofFloat(typeImage, "rotation", 180, 360)
                    .setDuration(durationRotate).start();
            ObjectAnimator.ofFloat(typeImage, "rotation", 180, 360)
                    .setDuration(durationRotate).start();
            ObjectAnimator.ofFloat(flType, "alpha", 1, 0)
                    .setDuration(durationAlpha).start();
            flType.postDelayed(new Runnable() {
                @Override
                public void run() {
                    flType.setVisibility(View.GONE);
                }
            }, durationAlpha);
        } else {
            ObjectAnimator.ofFloat(typeImage, "rotation", 0, 180)
                    .setDuration(durationRotate).start();
            ObjectAnimator.ofFloat(typeImage, "rotation", 0, 180)
                    .setDuration(durationRotate).start();
            ObjectAnimator.ofFloat(typeImage, "rotation", 0, 180)
                    .setDuration(durationRotate).start();
            flType.setVisibility(View.VISIBLE);
            ObjectAnimator.ofFloat(flType, "alpha", 0, 1)
                    .setDuration(durationAlpha).start();
        }

    }

    /**
     * 更多点击事件
     */
    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            typeGridAdapter.setSeclection(position);
            typeGridAdapter.notifyDataSetChanged();
            keyword = chaozhigouTypesBeans.get(position).getKeyword();
            x = 1;
            page = 1;
            DialogSingleUtil.show(getActivity());
            initDataCzg(keyword);
            homeLoadUtil.updateTitle(position + 1, mbox2, keyword, mhscrollview);
            homeLoadUtil.updateTitleTop(position + 1, mboxtop, keyword, mhscrollviewtop);
            showGlobalMenu();
        }
    };

    /**
     * 超值购数据请求
     */
    private void initDataCzg(String keyword) {
        refreshLayout.setNoMoreData(false);
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("keyword", keyword);
        paramsMap.put("sortWay", "5");
        paramsMap.put("page", page + "");
        paramsMap.put("client", "android");
        paramsMap.put("domain", "");
        RetrofitClient.getInstance(getActivity()).createBaseApi().getPageListChaozhigou(
                paramsMap, new BaseObserver<String>(getActivity()) {
                    @Override
                    public void onNext(String s) {
                        try {
                            mrecyclerview.setVisibility(View.VISIBLE);
                            scrollview.setVisibility(View.VISIBLE);
                            JSONObject jsonObject = new JSONObject(s);
                            content = jsonObject.optString("content");
                            JSONObject jo = new JSONObject(content);
                            String isBlandCzg = jo.optString("isBland");
                            if (jsonObject.optString("status").equals("1")) {
                                if (isBlandCzg.equals("1")) {
                                    JSONObject info = jo.getJSONObject("info");
                                    String tmpCzg = info.optString("page");
                                    NewConstants.Flag = "3";
                                    czgBeans = JSON.parseArray(tmpCzg, NewHomeCzgBean.class);
                                    if (x == 1) {
                                        if (czgBeans != null && czgBeans.size() > 0) {
                                            refreshLayout.setEnableLoadMore(true);
                                            mrecyclerview.setVisibility(View.VISIBLE);
                                            newCzgAdapter = new NewCzgAdapter(getActivity(), czgBeans);
                                            mrecyclerview.setAdapter(newCzgAdapter);
                                        } else {
                                            refreshLayout.setEnableLoadMore(false);
                                            mrecyclerview.setVisibility(View.GONE);
                                        }
                                    } else if (x == 2) {
                                        mrecyclerview.setVisibility(View.VISIBLE);
                                        if (tmpCzg != null && !tmpCzg.toString().equals("[]")) {
                                            czgBeans = JSON.parseArray(tmpCzg, NewHomeCzgBean.class);
                                            newCzgAdapter.notifyData(czgBeans);
                                        } else {
                                            refreshLayout.finishLoadMoreWithNoMoreData();
                                        }
                                    }
                                } else if (isBlandCzg.equals("-1") && x == 2 && NewConstants.Flag.equals("3")) {
                                    refreshLayout.finishLoadMoreWithNoMoreData();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        refreshLayout.finishLoadMore();
                        refreshLayout.finishRefresh();
                        DialogSingleUtil.dismiss(0);
                        zLoadingView.loadSuccess();
                    }

                    @Override
                    protected void showDialog() {
//                        DialogSingleUtil.show(getActivity());
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        refreshLayout.finishLoadMore();
                        refreshLayout.finishRefresh();
                        DialogSingleUtil.dismiss(0);
                        scrollview.setVisibility(View.GONE);
                        zLoadingView.setVisibility(View.VISIBLE);
                        zLoadingView.loadError();
                        mrecyclerview.setVisibility(View.GONE);
                        mSuspensionBar.setVisibility(View.GONE);
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(getActivity(), e.message);
                    }
                });
    }

    /**
     * 首页滑动监听
     * @param scrollView
     * @param x
     * @param y
     * @param oldx
     * @param oldy
     */
    @Override
    public void onScrollChanged(MyScrollViewNew scrollView, int x, int y, int oldx, int oldy) {
        if (y <= 0) {   //设置标题的背景颜色
//            lin.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
            lin.setBackgroundResource(R.drawable.bg_home_yinying);
            llType.setVisibility(View.GONE);
            imageButton.setVisibility(View.GONE);
            lin.setVisibility(View.VISIBLE);
            imageFenlei.setBackgroundResource(R.mipmap.fenlei_white);
            tvFenlei.setTextColor(getActivity().getResources().getColor(R.color.white));
        } else if (y > 0 && y <= imageHeight) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            float scale = (float) y / imageHeight;
            float alpha = (255 * scale);
            lin.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
            tvFenlei.setTextColor(getActivity().getResources().getColor(R.color.white));
            llType.setVisibility(View.GONE);
            imageButton.setVisibility(View.GONE);
            lin.setVisibility(View.VISIBLE);
            imageFenlei.setBackgroundResource(R.mipmap.fenlei_white);
        } else {
            //滑动到banner下面设置普通颜色
            //将标题栏的颜色设置为完全不透明状态
            lin.setBackgroundResource(R.color.white);
            lin.setVisibility(View.VISIBLE);
            imageFenlei.setBackgroundResource(R.mipmap.tuiguang_21);
            tvFenlei.setTextColor(getActivity().getResources().getColor(R.color.tuiguang_color3));
        }
        height = llTop.getHeight() - lin.getHeight();
        if (y > 0 && y <= height) {
            llType.setVisibility(View.GONE);
            imageButton.setVisibility(View.GONE);
        } else if (y <= 0) {
            llType.setVisibility(View.GONE);
            imageButton.setVisibility(View.GONE);
        } else {
            llType.setVisibility(View.VISIBLE);
            imageButton.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 获取顶部图片高度后，设置滚动监听
     */
    private void initListeners() {

        ViewTreeObserver vto = mBanner.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mBanner.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                imageHeight = mBanner.getHeight();
                scrollview.setScrollViewListener(NewHomeFragment.this);
            }
        });
    }


    /**
     * 首页分类点击事件
     * @param keywordd
     */
    @Override
    public void onClick(String keywordd) {
        x = 1;
        page = 1;
        flag = "1";
        keyword = keywordd;
        initDataCzg(keyword);
    }

    @Override
    public void onClickOnePosition() {
        x = 1;
        page = 1;
        flag = "0";
        keyword = "";
        initDataCzg(keyword);
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
     * @param view
     */
    @OnClick({R.id.type_image, R.id.ll_shouqi, R.id.msearch, R.id.msort,R.id.image_puba, R.id.to_top_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.type_image:
                showGlobalMenu();
                break;
            case R.id.ll_shouqi:
                showGlobalMenu();
                break;
            case R.id.msearch:
                Intent intent = new Intent(getActivity(), SearchMainActivity.class);
                startActivity(intent);
                break;
            case R.id.msort:
                Intent intent1 = new Intent(getActivity(), SortActivity.class);
                startActivity(intent1);
                break;
            case R.id.image_puba:
                Intent intent2 = new Intent(getActivity(), BidHomeActivity.class);
                startActivity(intent2);
                break;
            case R.id.to_top_btn:
                //滑动到顶部
                scrollview.scrollTo(0, 0);
                break;
        }
    }
}
