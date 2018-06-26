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
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.FenXiangListBean;
import com.bbk.Bean.NewHomeCzgBean;
import com.bbk.Bean.NewHomeFxBean;
import com.bbk.Bean.NewHomePubaBean;
import com.bbk.Decoration.TwoDecoration;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.SearchMainActivity;
import com.bbk.activity.SortActivity;
import com.bbk.adapter.NewHomeAdapter;
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
import com.bbk.util.BaseTools;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.EventIdIntentUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.CommonLoadingView;
import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

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


public class NewHomeFragment extends BaseViewPagerFragment implements OnClickListener, ResultEvent, OnClickListioner, CommonLoadingView.LoadingHandler {
    @BindView(R.id.lin)
    LinearLayout lin;
    @BindView(R.id.mbox)
    LinearLayout mbox;
    @BindView(R.id.mhscrollview)
    HorizontalScrollView mhscrollview;
    @BindView(R.id.ll_type)
    LinearLayout llType;
    @BindView(R.id.type_image)
    ImageView typeImage;
    @BindView(R.id.type_grid)
    GridView typeGrid;
    @BindView(R.id.ll_shouqi)
    LinearLayout llShouqi;
    @BindView(R.id.fl_type)
    FrameLayout flType;
    private DataFlow6 dataFlow;
    private View mView;
    /**
     * Banner
     */
    private JSONArray banner = new JSONArray();
    /**
     * 中间布局
     */
    private JSONArray tag = new JSONArray();
    private List<Map<String, String>> taglist = new ArrayList<>();
    private JSONArray gongneng = new JSONArray();
    private JSONArray fabiao = new JSONArray();
    private JSONArray chaozhigouTypes = new JSONArray();
//    private MyNewScrollView myScrollView;
    /**
     * 顶部固定的TabViewLayout
     */
//    private LinearLayout mTopTabViewLayout;
    /**
     * 跟随ScrollView的TabviewLayout
     */
//    private LinearLayout mTabViewLayout;

    /**
     * 要悬浮在顶部的View的子View
     */
    private LinearLayout mTopView;
    private LinearLayout layout;

    /**
     * 镖局,超值购...
     *
     * @param savedInstanceState
     */
    private LinearLayout mLlCzgLayout, mLlbjLayout, mLlblLayout, mLlfxLayout;
    private View mCzgView, mBjView, mBlView, mFxView;
    private TextView mCzgText, mBjText, mBlText, mFxText;
    private NewHomeAdapter homeadapter;
    private int page = 1, x = 1;
    private String type = "1", flag = "";
    private String wztitle = "";
    private View view;
    private ImageView huodongimg;//活动按钮
    //第一次引导页是否显示隐藏
    private boolean isshowzhezhao = true;
    final String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
    private boolean isHomeGudie = false;
    JSONObject jo, preguanggao;
    private RecyclerView mrecyclerview;
    private RefreshLayout refreshLayout;
    JSONObject object;
    private LinearLayout mSuspensionBar;
    private IWXAPI msgApi = null;
    private int data;// 支付结果标识
    private PayReq mReq;
    private PayModel mPayModel;
    private CommonLoadingView zLoadingView;//加载框
    List<NewHomeCzgBean> czgBeans;//超值购数据
    List<NewHomePubaBean> pubaBeans;//扑吧数据
    List<FenXiangListBean> blBeans;//爆料数据
    List<NewHomeFxBean> fxBeans;//发现数据
    private ImageButton imageButton;
    private String content;
    String isFirstResultUse;
    int mDistanceY;
    private int currentIndex = 0;
    private List<Map<String, String>> titlelist;
    private int durationRotate = 700;// 旋转动画时间
    private int durationAlpha = 500;// 透明度动画时间
    private boolean isGlobalMenuShow = true;
    TypeGridAdapter typeGridAdapter;

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
            dataFlow = new DataFlow6(getContext());
            mPayModel = BaseService.getPayModel(getActivity());
            View topView = mView.findViewById(R.id.lin);
            // 实现沉浸式状态栏
            ImmersedStatusbarUtils.initAfterSetContentView(getActivity(), topView);
            initView(mView);
        }
        return mView;

    }

    //控件实例化
    private void initView(View v) {
        imageButton = mView.findViewById(R.id.to_top_btn);
        zLoadingView = mView.findViewById(R.id.progress);
        zLoadingView.setLoadingHandler(this);
        huodongimg = mView.findViewById(R.id.huodongimg);
        view = v.findViewById(R.id.view);
        mTopView = mView.findViewById(R.id.tv_topView);
        mLlCzgLayout = mView.findViewById(R.id.ll_czg_layout);
        mLlbjLayout = mView.findViewById(R.id.ll_bj_layout);
        mLlblLayout = mView.findViewById(R.id.ll_bl_layout);
        mLlfxLayout = mView.findViewById(R.id.ll_fx_layout);
        mCzgText = mView.findViewById(R.id.czg_text);
        mBjText = mView.findViewById(R.id.bj_text);
        mBlText = mView.findViewById(R.id.bl_text);
        mFxText = mView.findViewById(R.id.fx_text);
        mCzgView = mView.findViewById(R.id.czg_view);
        mBjView = mView.findViewById(R.id.bj_view);
        mBlView = mView.findViewById(R.id.bl_view);
        mFxView = mView.findViewById(R.id.fx_view);
        mLlCzgLayout.setOnClickListener(this);
        mLlbjLayout.setOnClickListener(this);
        mLlblLayout.setOnClickListener(this);
        mLlfxLayout.setOnClickListener(this);
        refreshLayout = (RefreshLayout) mView.findViewById(R.id.refresh_root);
        refreshLayout.setPrimaryColorsId(R.color.button_color, android.R.color.white);//全局设置主题颜色
        refreshLayout.setEnableFooterFollowWhenLoadFinished(true);
        refreshLayout.setEnableOverScrollDrag(true);
        refreshLayout.setEnableOverScrollBounce(true);
        refreshLayout.setEnableFooterTranslationContent(true);
        //设置 Header 为 贝塞尔雷达 样式
        refreshLayout.setRefreshHeader(new BezierCircleHeader(getActivity()));
        //设置 Footer 为 球脉冲 样式
//        refreshLayout.setRefreshFooter(new BallPulseFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale).setNormalColor(getActivity().getResources().getColor(R.color.button_color)).setAnimatingColor(getActivity().getResources().getColor(R.color.button_color)));
//        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(final RefreshLayout refreshlayout) {
//                x = 1;
//                initData();
//                mCzgText.setTextColor(getResources().getColor(R.color.color_line_top));
//                mCzgView.setVisibility(View.VISIBLE);
//                mBjText.setTextColor(getResources().getColor(R.color.color_line_text));
//                mBjView.setVisibility(View.GONE);
//                mBlText.setTextColor(getResources().getColor(R.color.color_line_text));
//                mBlView.setVisibility(View.GONE);
//                mFxText.setTextColor(getResources().getColor(R.color.color_line_text));
//                mFxView.setVisibility(View.GONE);
////                        mIdex("1");
//                refreshlayout.finishRefresh(2000);
//            }
//        });
//        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
//            @Override
//            public void onLoadmore(RefreshLayout refreshlayout) {
//
////                refreshlayout.finishLoadmore(2000/*,false*/);//传入false表示加载失败
//            }
//        });
        refreshLayout.setOnMultiPurposeListener(new OnMultiPurposeListener() {
            @Override
            public void onHeaderPulling(RefreshHeader header, float percent, int offset, int headerHeight, int extendHeight) {
                lin.setVisibility(View.GONE);
            }

            @Override
            public void onHeaderReleased(RefreshHeader header, int headerHeight, int extendHeight) {
                lin.setVisibility(View.VISIBLE);
            }

            @Override
            public void onHeaderReleasing(RefreshHeader header, float percent, int offset, int headerHeight, int extendHeight) {
                lin.setVisibility(View.VISIBLE);
            }

            @Override
            public void onHeaderStartAnimator(RefreshHeader header, int headerHeight, int extendHeight) {

            }

            @Override
            public void onHeaderFinish(RefreshHeader header, boolean success) {
                lin.setVisibility(View.VISIBLE);
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
                handler.sendEmptyMessageDelayed(4, 100);
                refreshLayout.finishLoadMore(1500);
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                x = 1;
                initData();
                mCzgText.setTextColor(getResources().getColor(R.color.color_line_top));
                mCzgView.setVisibility(View.VISIBLE);
                mBjText.setTextColor(getResources().getColor(R.color.color_line_text));
                mBjView.setVisibility(View.GONE);
                mBlText.setTextColor(getResources().getColor(R.color.color_line_text));
                mBlView.setVisibility(View.GONE);
                mFxText.setTextColor(getResources().getColor(R.color.color_line_text));
                mFxView.setVisibility(View.GONE);
//                        mIdex("1");
                refreshLayout.finishRefresh(2000);
            }

            @Override
            public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {

            }
        });
        mrecyclerview = (RecyclerView) mView.findViewById(R.id.mrecycler);
        imageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //滑动到顶部
                mrecyclerview.scrollToPosition(0);
            }
        });
        mrecyclerview.setHasFixedSize(true);
        mrecyclerview.setNestedScrollingEnabled(false);
        ((SimpleItemAnimator) mrecyclerview.getItemAnimator()).setSupportsChangeAnimations(false);
        mSuspensionBar = mView.findViewById(R.id.layout_click);
        final LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity());
        mrecyclerview.setLayoutManager(gridLayoutManager);
        mrecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //滑动的距离
                mDistanceY += dy;
                //toolbar的高度
                int toolbarHeight = lin.getBottom();
                //当滑动的距离 <= toolbar高度的时候，改变Toolbar背景色的透明度，达到渐变的效果
                if (mDistanceY <= toolbarHeight) {
                    float scale = (float) mDistanceY / toolbarHeight;
                    float alpha = scale * 255;
                    lin.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                } else {
                    //将标题栏的颜色设置为完全不透明状态
                    lin.setBackgroundResource(R.color.white);
                }

                int firstPosition = gridLayoutManager.findFirstVisibleItemPosition();
                if (firstPosition > 0) {
                    llType.setVisibility(View.VISIBLE);
                    imageButton.setVisibility(View.VISIBLE);
                } else {
                    llType.setVisibility(View.GONE);
                    imageButton.setVisibility(View.GONE);
                }
            }
        });
    }

    //首页数据请求
    private void initData() {
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        RetrofitClient.getInstance(getActivity()).createBaseApi().queryAppIndexInfo(
                maps, new BaseObserver<String>(getActivity()) {
                    @Override
                    public void onNext(String s) {
                        try {
                            mrecyclerview.setVisibility(View.VISIBLE);
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

    //首页分类数据
    private void getIndexByType() {
        refreshLayout.setNoMoreData(false);
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("type", type);
        maps.put("page", page + "");
        maps.put("userid", userID);
        RetrofitClient.getInstance(getActivity()).createBaseApi().queryAppIndexByType(
                maps, new BaseObserver<String>(getActivity()) {
                    @Override
                    public void onNext(String s) {
                        try {
                            mrecyclerview.setVisibility(View.VISIBLE);
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                content = jsonObject.optString("content");
                                handler.sendEmptyMessageDelayed(2, 0);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        refreshLayout.finishLoadMore();
                        refreshLayout.finishRefresh();
                        zLoadingView.loadSuccess();
                        DialogSingleUtil.dismiss(0);
                    }

                    @Override
                    protected void showDialog() {
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        refreshLayout.finishLoadMore();
                        refreshLayout.finishRefresh();
                        DialogSingleUtil.dismiss(0);
                        zLoadingView.setVisibility(View.VISIBLE);
                        zLoadingView.loadError();
                        mrecyclerview.setVisibility(View.GONE);
                        mSuspensionBar.setVisibility(View.GONE);
                        StringUtil.showToast(getActivity(), e.message);
                    }
                });
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_czg_layout:
                flag = "1";
                setView();
                mIdex("1");
                setText(mCzgText, mCzgView);
//                initDataWx(true);
                break;
            case R.id.ll_bj_layout:
                flag = "2";
                setView();
                mIdex("2");
                setText(mBjText, mBjView);
                break;
            case R.id.ll_bl_layout:
                flag = "3";
                setView();
                mIdex("5");
                setText(mBlText, mBlView);
                break;
            case R.id.ll_fx_layout:
                flag = "4";
                setView();
                mIdex("4");
                setText(mFxText, mFxView);
                break;
            case R.id.msort:
                intent = new Intent(getActivity(), SortActivity.class);
                startActivity(intent);
                break;
            case R.id.msearch:
                intent = new Intent(getActivity(), SearchMainActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void setText(TextView text, View view) {
        text.setTextColor(getResources().getColor(R.color.color_line_top));
        view.setVisibility(View.VISIBLE);
    }

    private void setView() {
        DialogSingleUtil.show(getActivity());
        mCzgText.setTextColor(getResources().getColor(R.color.color_line_text));
        mCzgView.setVisibility(View.GONE);
        mBjText.setTextColor(getResources().getColor(R.color.color_line_text));
//            mQbText.setTextSize(TypedValue.COMPLEX_UNIT_PX,30);
        mBjView.setVisibility(View.GONE);
        mBlText.setTextColor(getResources().getColor(R.color.color_line_text));
//            mDfkText.setTextSize(TypedValue.COMPLEX_UNIT_PX,30);
        mBlView.setVisibility(View.GONE);
        mFxText.setTextColor(getResources().getColor(R.color.color_line_text));
//            mDfhText.setTextSize(TypedValue.COMPLEX_UNIT_PX,30);
        mFxView.setVisibility(View.GONE);
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
                        if (type.equals("2")) {
                            pubaBeans = JSON.parseArray(content, NewHomePubaBean.class);
                            if (x == 1) {
                                Refresh("2");
                            } else if (x == 2) {
                                if (pubaBeans != null && pubaBeans.size() > 0) {
                                    homeadapter.notifyBjData(pubaBeans);
                                } else {
                                    refreshLayout.finishLoadMoreWithNoMoreData();
                                }
                            }
                        } else if (type.equals("5")) {
                            blBeans = JSON.parseArray(content, FenXiangListBean.class);
                            if (x == 1) {
                                Refresh("5");
                            } else if (x == 2) {
                                if (blBeans != null && blBeans.size() > 0) {
                                    homeadapter.notifyBlData(blBeans);
                                } else {
                                    refreshLayout.finishLoadMoreWithNoMoreData();
                                }
                            }
                        } else if (type.equals("4")) {
                            fxBeans = JSON.parseArray(content, NewHomeFxBean.class);
                            if (x == 1) {
                                Refresh("4");
                            } else if (x == 2) {
                                if (fxBeans != null && fxBeans.size() > 0) {
                                    homeadapter.notifyFxData(fxBeans);
                                } else {
                                    refreshLayout.finishLoadMoreWithNoMoreData();
                                }
                            }
                        } else if (type.equals("1")) {
                            czgBeans = JSON.parseArray(content, NewHomeCzgBean.class);
                            if (x == 1) {
                                Refresh("1");
                            } else if (x == 2) {
                                if (czgBeans != null && czgBeans.size() > 0) {
                                    homeadapter.notifyData(czgBeans);
                                } else {
                                    refreshLayout.finishLoadMoreWithNoMoreData();
                                }
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
                        try {
                            loadtitlekeywords(chaozhigouTypes);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mrecyclerview.addItemDecoration(new TwoDecoration(0, "#f3f3f3", 3 + banner.length() + tag.length()));
                    mrecyclerview.setHasFixedSize(true);
                    mIdex("1");
//                    homeadapter = new NewHomeAdapter(getActivity(),taglist,banner, tag, fabiao,gongneng,type,czgBeans,pubaBeans,blBeans,fxBeans);
//                    mrecyclerview.setAdapter(homeadapter);
//                    homeadapter.setOnClickListioner(NewHomeFragment.this);
                    /**
                     * eventid 为108 表示点击之后跳到登录页面。如果已经登录，则不显示preguanggao，显示guanggao
                     未登录 显示preguanggao
                     */
                    if (TextUtils.isEmpty(userID)) {
                        if (object.has("preguanggao")) {
                            if (isshowzhezhao) {
                                preguanggao = object.optJSONObject("preguanggao");
                                String isFirstResultUse = SharedPreferencesUtil.getSharedData(getActivity(), "isFirstHomeUse", "isFirstHomeUserUse");
                                if (isFirstResultUse.equals("no")) {
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
                        }
                    } else {
                        if (object.has("guanggao")) {
                            if (isshowzhezhao) {
                                jo = object.optJSONObject("guanggao");
                                String isFirstResultUse = SharedPreferencesUtil.getSharedData(getActivity(), "isFirstHomeUse", "isFirstHomeUserUse");
                                if (isFirstResultUse.equals("no")) {
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
                    }
                    break;
                case 4:
                    x = 2;
                    page++;
                    getIndexByType();
                    break;
                case 5:
                    initData();
                    break;
            }
        }
    };

    public void Refresh(String type) {
        mSuspensionBar.setVisibility(View.GONE);
        homeadapter = new NewHomeAdapter(getActivity(), taglist, banner, tag, fabiao, gongneng, titlelist, type, czgBeans, pubaBeans, blBeans, fxBeans, blBeans, jo);
        mrecyclerview.setAdapter(homeadapter);
//        homeadapter.notifyDataSetChanged();
        if (homeadapter != null) {
            homeadapter.setOnClickListioner(NewHomeFragment.this);
        }
    }

    //首页视图数据加载
    private void mViewLoad() {
        handler.sendEmptyMessageDelayed(5, 0);
    }

    //超值购等数据
    private void mIdex(String str) {
        type = str;
        x = 1;
        page = 1;
//        zLoadingView.load();
        getIndexByType();
    }

    @Override
    protected void loadLazyData() {
//        refreshLayout.autoRefresh();
        isFirstResultUse = SharedPreferencesUtil.getSharedData(getActivity(), "isFirstHomeUse", "isFirstHomeUserUse");
        if (isFirstResultUse.equals("no")) {
            refreshLayout.autoRefresh();
        } else {
            DialogSingleUtil.show(getActivity());
            mViewLoad();
        }
    }

    @Override
    public void onCzgClick() {
        setView();
        mIdex("1");
        setText(mCzgText, mCzgView);
    }

    @Override
    public void onBjClick() {
        setView();
        mIdex("2");
        setText(mBjText, mBjView);
    }

    @Override
    public void onBlClick() {
        setView();
        mIdex("5");
        setText(mBlText, mBlView);
    }

    @Override
    public void onFxClick() {
        setView();
        mIdex("4");
        setText(mFxText, mFxView);
    }

    @Override
    public void onDissmissClick() {
//        if (isHomeGudie) {
//            SharedPreferencesUtil.putSharedData(getActivity(), "isFirstHomeUse","isFirstHomeUserUse", "no");
//        }else{
//            isHomeGudie = true;
//        }
        SharedPreferencesUtil.putSharedData(getActivity(), "isFirstHomeUse", "isFirstHomeUserUse", "no");
        isFirstResultUse = SharedPreferencesUtil.getSharedData(getActivity(), "isFirstHomeUse", "isFirstHomeUserUse");
        if (isFirstResultUse.equals("no")) {
            if (isshowzhezhao) {
                /**
                 * eventid 为108 表示点击之后跳到登录页面。如果已经登录，则不显示preguanggao，显示guanggao
                 未登录 显示preguanggao
                 */
                if (TextUtils.isEmpty(userID)) {
                    if (object.has("preguanggao")) {
                        if (isshowzhezhao) {
                            preguanggao = object.optJSONObject("preguanggao");
                            String isFirstResultUse = SharedPreferencesUtil.getSharedData(getActivity(), "isFirstHomeUse", "isFirstHomeUserUse");
                            if (isFirstResultUse.equals("no")) {
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
                    }
                } else {
                    if (object.has("guanggao")) {
                        if (isshowzhezhao) {
                            jo = object.optJSONObject("guanggao");
                            String isFirstResultUse = SharedPreferencesUtil.getSharedData(getActivity(), "isFirstHomeUse", "isFirstHomeUserUse");
                            if (isFirstResultUse.equals("no")) {
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
                }
            }
        }
    }

    @Override
    public void onClick(String keyword) {
        x = 1;
        page = 1;
        Log.i("----",keyword);
        initDataCzg(keyword);
    }

    @Override
    public void doRequestData() {
        DialogSingleUtil.show(getActivity());
        zLoadingView.setVisibility(View.GONE);
        initData();
        getIndexByType();
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


    // 一级菜单一
    private void addtitle(final String text, final int i) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.super_item_title, null);
        //设置view的weight为1，保证导航铺满当前页面
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
        final TextView title = (TextView) view.findViewById(R.id.item_title);
        final View henggang = view.findViewById(R.id.bottom_view);
        title.setText(text);
        title.setTextColor(Color.parseColor("#666666"));
        henggang.setBackgroundColor(Color.parseColor("#ffffff"));
        view.setPadding(BaseTools.getPixelsFromDp(getActivity(), 0), 0, BaseTools.getPixelsFromDp(getActivity(), 0), 0);
        if (i == 0) {
            view.setVisibility(View.VISIBLE);
            title.setTextColor(Color.parseColor("#FF7D41"));
            henggang.setBackgroundColor(Color.parseColor("#FF7D41"));
            title.setText("超值购");
        }
        if (i == 1) {

        }
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                if (i != currentIndex) {
//                    topViewHolder.mbox.getChildAt(0).setVisibility(View.GONE);
                    updateTitle(i);
                }
            }

        });
        mbox.addView(view);
    }

    private void updateTitle(int position) {
        titlelist.get(currentIndex).put("isselect", "0");
        titlelist.get(position).put("isselect", "1");
        View view = mbox.getChildAt(position);
        TextView title1 = (TextView) view.findViewById(R.id.item_title);
        View henggang1 = view.findViewById(R.id.bottom_view);
        title1.setTextColor(Color.parseColor("#FF7D41"));
        henggang1.setBackgroundColor(Color.parseColor("#FF7D41"));

        View view4 = mbox.getChildAt(currentIndex);
        TextView title3 = (TextView) view4.findViewById(R.id.item_title);
        View henggang3 = view4.findViewById(R.id.bottom_view);
        title3.setTextColor(Color.parseColor("#666666"));
        henggang3.setBackgroundColor(Color.parseColor("#ffffff"));
        // mhscrollview.scrollTo(view.getLeft() - 200, 0);
        currentIndex = position;
        if (position == 0) {
            page = 1;
            x = 1;
            type = "";
            initDataCzg(type);
        } else {
            x = 1;
            page = 1;
            type = titlelist.get(position).get("keyword");
            initDataCzg(type);
        }
    }

    private void loadtitlekeywords(JSONArray searchwords) throws JSONException {
        titlelist = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("keyword", "超值购");
        map.put("isselect", "0");
        titlelist.add(map);
        addtitle("超值购", 0);
        for (int i = 0; i < searchwords.length(); i++) {
            Map<String, String> map1 = new HashMap<>();
            String keyword = searchwords.getJSONObject(i).optString("name");
            map1.put("keyword", keyword);
            map1.put("isselect", "0");
            titlelist.add(map1);
            addtitle(keyword, i + 1);
        }
        typeGridAdapter = new TypeGridAdapter(getActivity(), titlelist);
        typeGrid.setAdapter(typeGridAdapter);
        typeGrid.setOnItemClickListener(onItemClickListener);
    }

    @OnClick({R.id.type_image,R.id.ll_shouqi})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.type_image:
                showGlobalMenu();
            break;
            case R.id.ll_shouqi:
                showGlobalMenu();
                break;
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

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            typeGridAdapter.setSeclection(position);
            typeGridAdapter.notifyDataSetChanged();
            Log.i("点击了","++++++");
        }
    };

    /**
     * 超值购数据请求
     */
    private void initDataCzg(String keyword) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("keyword", keyword);
        paramsMap.put("sortWay", "");
        paramsMap.put("page", page + "");
        paramsMap.put("client", "android");
        paramsMap.put("domain", "");
        RetrofitClient.getInstance(getActivity()).createBaseApi().getPageListChaozhigou(
                paramsMap, new BaseObserver<String>(getActivity()) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Log.i("超值购数据",s+"=====");
                            if (jsonObject.optString("status").equals("1")) {
                                content = jsonObject.optString("content");
                                JSONObject jo = new JSONObject(content);
                                JSONObject info = jo.getJSONObject("info");
                                String tmpCzg = info.optString("page");
                                czgBeans = JSON.parseArray(tmpCzg, NewHomeCzgBean.class);
                                if (x == 1) {
                                    Refresh("1");
                                } else if (x == 2) {
                                    if (czgBeans != null && czgBeans.size() > 0) {
                                        homeadapter.notifyData(czgBeans);
                                    } else {
                                        refreshLayout.finishLoadMoreWithNoMoreData();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        DialogSingleUtil.dismiss(0);
                        zLoadingView.loadSuccess();
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(getActivity());
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        zLoadingView.setVisibility(View.VISIBLE);
                        zLoadingView.loadError();
                        StringUtil.showToast(getActivity(), e.message);
                    }
                });
    }
}
