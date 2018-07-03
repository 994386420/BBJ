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
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.bbk.Bean.FenXiangListBean;
import com.bbk.Bean.NewHomeCzgBean;
import com.bbk.Bean.NewHomeFxBean;
import com.bbk.Bean.NewHomePubaBean;
import com.bbk.activity.BidBillDetailActivity;
import com.bbk.activity.BidDetailActivity;
import com.bbk.activity.BidHomeActivity;
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
import com.bbk.util.BaseTools;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.EventIdIntentUtil;
import com.bbk.util.GlideImageLoader;
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
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
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

//OnClickListioner

public class NewHomeFragment extends BaseViewPagerFragment implements OnClickListener, ResultEvent, CommonLoadingView.LoadingHandler, MyScrollViewNew.ScrollViewListener,OnMultiPurposeListener {
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
    LinearLayout mbox1;
    @BindView(R.id.mhscrollview1)
    HorizontalScrollView mhscrollview1;
    @BindView(R.id.image_fenlei)
    ImageView imageFenlei;
    @BindView(R.id.tv_fenlei)
    TextView tvFenlei;
    @BindView(R.id.image_puba)
    ImageView imagePuba;
    private DataFlow6 dataFlow;
    private View mView;
    private ViewFlipper mviewflipper;//发标动态轮播
    private ImageView mImageView1, mImageView2, mImageView3, mImageView4, mImageView5;
    private TextView mTextView1, mTextView2, mTextView3, mTextView4, mTextView5;
    private LinearLayout mLlLayout1, mLlLayout2, mLlLayout3, mLlLayout4, mLlLayout5;
    /**
     * Banner
     */
    private Banner mBanner;//首页banner
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
    private int page = 1, x = 1;
    private String type = "1", flag = "";
    private View view;
    private ImageView huodongimg;//活动按钮
    //第一次引导页是否显示隐藏
    private boolean isshowzhezhao = true;
    final String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
    private boolean isHomeGudie = false;
    JSONObject jo, preguanggao;
    private RecyclerView mrecyclerview;
    private SmartRefreshLayout refreshLayout;
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
    private int currentIndex = 0,currentIndexTop = 0;
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
            imageFenlei.setBackgroundResource(R.mipmap.fenlei_white);
            // 实现沉浸式状态栏
            ImmersedStatusbarUtils.initAfterSetContentView(getActivity(), topView);
            initView(mView);
            initListeners();
        }
        return mView;

    }

    //控件实例化
    private void initView(View v) {
        mLlLayout1 = mView.findViewById(R.id.box1);
        mLlLayout2 = mView.findViewById(R.id.box2);
        mLlLayout3 = mView.findViewById(R.id.box3);
        mLlLayout4 = mView.findViewById(R.id.box4);
        mLlLayout5 = mView.findViewById(R.id.box5);
        mviewflipper = mView.findViewById(R.id.mviewflipper);
        mBanner = v.findViewById(R.id.banner);
        mTextView1 = mView.findViewById(R.id.text1);
        mTextView2 = mView.findViewById(R.id.text2);
        mTextView3 = mView.findViewById(R.id.text3);
        mTextView4 = mView.findViewById(R.id.text4);
        mTextView5 = mView.findViewById(R.id.text5);
        mImageView1 = mView.findViewById(R.id.img1);
        mImageView2 = mView.findViewById(R.id.img2);
        mImageView3 = mView.findViewById(R.id.img3);
        mImageView4 = mView.findViewById(R.id.img4);
        mImageView5 = mView.findViewById(R.id.img5);
        imageButton = mView.findViewById(R.id.to_top_btn);
        zLoadingView = mView.findViewById(R.id.progress);
        zLoadingView.setLoadingHandler(this);
        huodongimg = mView.findViewById(R.id.huodongimg);
        view = v.findViewById(R.id.view);
        mTopView = mView.findViewById(R.id.tv_topView);
        refreshLayout = (SmartRefreshLayout) mView.findViewById(R.id.refresh_root);
        refreshLayout.setPrimaryColorsId(R.color.button_color, android.R.color.white);//全局设置主题颜色
        refreshLayout.setEnableFooterFollowWhenLoadFinished(true);
        refreshLayout.setEnableOverScrollDrag(true);
        refreshLayout.setEnableOverScrollBounce(true);
        refreshLayout.setEnableFooterTranslationContent(true);
        //设置 Header 为 贝塞尔雷达 样式
        refreshLayout.setRefreshHeader(new BezierCircleHeader(getActivity()));
//        refreshLayout.setEnableClipHeaderWhenFixedBehind(true);
        //设置 Footer 为 球脉冲 样式
//        refreshLayout.setRefreshFooter(new BallPulseFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale).setNormalColor(getActivity().getResources().getColor(R.color.button_color)).setAnimatingColor(getActivity().getResources().getColor(R.color.button_color)));
        refreshLayout.setOnMultiPurposeListener(this);//刷新监听
        mrecyclerview = (RecyclerView) mView.findViewById(R.id.mrecycler);
        imageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //滑动到顶部
                scrollview.scrollTo(0, 0);
            }
        });
        mrecyclerview.setHasFixedSize(true);
        mrecyclerview.setNestedScrollingEnabled(false);
        ((SimpleItemAnimator) mrecyclerview.getItemAnimator()).setSupportsChangeAnimations(false);
        mSuspensionBar = mView.findViewById(R.id.layout_click);
        final LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity());
        mrecyclerview.setLayoutManager(gridLayoutManager);
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
                            scrollview.setVisibility(View.VISIBLE);
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                content = jsonObject.optString("content");
//                                Log.i("=====", content);
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
                        scrollview.setVisibility(View.GONE);
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
                            newCzgAdapter = new NewCzgAdapter(getActivity(), czgBeans);
                            mrecyclerview.setAdapter(newCzgAdapter);
                        } else if (x == 2) {
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
                                loadtitlekeywordsTop(chaozhigouTypes, mbox1);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        //判断传过来数据是否为null
                        if (banner != null && banner.length() > 0) {
                            loadbanner(banner);
                        }
                        if (tag != null && tag.length() > 0) {
                            loadTag(tag);
                        }
                        if (fabiao != null && fabiao.length() > 0) {
                            loadViewflipper(fabiao);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    /**
                     * eventid 为108 表示点击之后跳到登录页面。如果已经登录，则不显示preguanggao，显示guanggao
                     未登录 显示preguanggao
                     */
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
                    getIndexByType();
                    break;
                case 5:
                    initData();
                    break;
            }
        }
    };

    //超值购
    private void mIdex(String str) {
        type = str;
        x = 1;
        page = 1;
        getIndexByType();
    }

    @Override
    protected void loadLazyData() {
        titlelist = new ArrayList<>();
        mIdex("1");
        refreshLayout.autoRefresh();
    }

    @Override
    public void doRequestData() {
        DialogSingleUtil.show(getActivity());
        zLoadingView.setVisibility(View.GONE);
        initData();
//        getIndexByType();
        mIdex("1");
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
    private void addtitle(final String text, final int i, final LinearLayout mbox) {
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
                    updateTitle(i, mbox2,text);
                    updateTitleTop(i, mbox1,text);
                }
            }

        });
        mbox.addView(view);
    }

    private void updateTitle(int position, LinearLayout mbox,String text) {
        View view = mbox.getChildAt(position);
        TextView title1 = (TextView) view.findViewById(R.id.item_title);
        View henggang1 = view.findViewById(R.id.bottom_view);
        if (position == currentIndex){
            title1.setTextColor(Color.parseColor("#FF7D41"));
            henggang1.setBackgroundColor(Color.parseColor("#FF7D41"));
        }else {
            title1.setTextColor(Color.parseColor("#FF7D41"));
            henggang1.setBackgroundColor(Color.parseColor("#FF7D41"));
        }
        View view4 = mbox.getChildAt(currentIndex);
        TextView title3 = (TextView) view4.findViewById(R.id.item_title);
        View henggang3 = view4.findViewById(R.id.bottom_view);
        if (position == currentIndex){
            title3.setTextColor(Color.parseColor("#FF7D41"));
            henggang3.setBackgroundColor(Color.parseColor("#FF7D41"));
        }else {
            title3.setTextColor(Color.parseColor("#666666"));
            henggang3.setBackgroundColor(Color.parseColor("#ffffff"));
        }
         mhscrollview.scrollTo(view.getLeft() - 200, 0);
        currentIndex = position;
        if (position == 0) {
            flag = "0";
            keyword = "";
            DialogSingleUtil.show(getActivity());
            mIdex("1");
        } else {
            x = 1;
            page = 1;
            flag = "1";
            keyword = text;
            DialogSingleUtil.show(getActivity());
            initDataCzg(keyword);
        }
    }
    // 一级菜单一
    private void addtitleTop(final String text, final int i, final LinearLayout mbox) {
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
                if (i != currentIndexTop) {
                    updateTitleTop(i, mbox1,text);
                    updateTitle(i, mbox2,text);
                }
            }

        });
        mbox.addView(view);
    }

    private void updateTitleTop(int position, LinearLayout mbox,String text) {
        titlelist.get(currentIndex).put("isselect", "0");
        titlelist.get(position).put("isselect", "1");
        View view = mbox.getChildAt(position);
        TextView title1 = (TextView) view.findViewById(R.id.item_title);
        View henggang1 = view.findViewById(R.id.bottom_view);
        if (position == currentIndexTop){
            title1.setTextColor(Color.parseColor("#FF7D41"));
            henggang1.setBackgroundColor(Color.parseColor("#FF7D41"));
        }else {
            title1.setTextColor(Color.parseColor("#FF7D41"));
            henggang1.setBackgroundColor(Color.parseColor("#FF7D41"));
        }
        View view4 = mbox.getChildAt(currentIndexTop);
        TextView title3 = (TextView) view4.findViewById(R.id.item_title);
        View henggang3 = view4.findViewById(R.id.bottom_view);
        if (position == currentIndexTop){
            title3.setTextColor(Color.parseColor("#FF7D41"));
            henggang3.setBackgroundColor(Color.parseColor("#FF7D41"));
        }else {
            title3.setTextColor(Color.parseColor("#666666"));
            henggang3.setBackgroundColor(Color.parseColor("#ffffff"));
        }
         mhscrollview1.scrollTo(view.getLeft() - 200, 0);
        currentIndexTop = position;
        if (position == 0) {
            flag = "0";
            keyword = "";
            DialogSingleUtil.show(getActivity());
            mIdex("1");
        } else {
            x = 1;
            page = 1;
            flag = "1";
            keyword = text;
            DialogSingleUtil.show(getActivity());
            initDataCzg(keyword);
        }
    }
    private void loadtitlekeywords(JSONArray searchwords, LinearLayout mbox) throws JSONException {
        Map<String, String> map = new HashMap<>();
        map.put("keyword", "超值购");
        map.put("isselect", "0");
        titlelist.add(map);
        addtitle("超值购", 0, mbox);
        for (int i = 0; i < searchwords.length(); i++) {
            Map<String, String> map1 = new HashMap<>();
            String keyword = searchwords.getJSONObject(i).optString("name");
            map1.put("keyword", keyword);
            map1.put("isselect", "0");
            titlelist.add(map1);
            addtitle(keyword, i + 1, mbox);
        }
    }
    private void loadtitlekeywordsTop(JSONArray searchwords, LinearLayout mbox) throws JSONException {
        addtitleTop("超值购", 0, mbox);
        for (int i = 0; i < searchwords.length(); i++) {
            Map<String, String> map1 = new HashMap<>();
            String keyword = searchwords.getJSONObject(i).optString("name");
            addtitleTop(keyword, i + 1, mbox);
        }
    }


    @OnClick({R.id.type_image, R.id.ll_shouqi, R.id.msearch, R.id.msort})
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
                keyword = chaozhigouTypesBeans.get(position).getKeyword();
                x = 1;
                page = 1;
                DialogSingleUtil.show(getActivity());
                initDataCzg(keyword);
                updateTitle(position+1, mbox2,keyword);
                updateTitleTop(position+1, mbox1,keyword);
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
                                        newCzgAdapter = new NewCzgAdapter(getActivity(), czgBeans);
                                        mrecyclerview.setAdapter(newCzgAdapter);
                                    } else if (x == 2) {
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
                        DialogSingleUtil.dismiss(0);
                        zLoadingView.loadSuccess();
                    }

                    @Override
                    protected void showDialog() {
//                        DialogSingleUtil.show(getActivity());
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


    /***
     * 加载中部图标
     * @param tag
     * @throws Exception
     */
    private void loadTag(final JSONArray tag) throws Exception {
        taglist.clear();
        for (int i = 0; i < tag.length(); i++) {
            JSONObject object = tag.getJSONObject(i);
            Map<String, String> map = new HashMap<>();
            String htmlUrl = object.optString("htmlUrl");
            String eventId = object.optString("eventId");
            String img = object.optString("img");
            String name = object.optString("name");
            map.put("htmlUrl", htmlUrl);
            map.put("eventId", eventId);
            map.put("text", name);
            map.put("imageUrl", img);
            taglist.add(map);
        }
        List<ImageView> imglist = new ArrayList<>();
        List<TextView> textlist = new ArrayList<>();
        List<LinearLayout> boxlist = new ArrayList<>();
        imglist.add(mImageView1);
        imglist.add(mImageView2);
        imglist.add(mImageView3);
        imglist.add(mImageView4);
        imglist.add(mImageView5);
        textlist.add(mTextView1);
        textlist.add(mTextView2);
        textlist.add(mTextView3);
        textlist.add(mTextView4);
        textlist.add(mTextView5);
        boxlist.add(mLlLayout1);
        boxlist.add(mLlLayout2);
        boxlist.add(mLlLayout3);
        boxlist.add(mLlLayout4);
        boxlist.add(mLlLayout5);
        for (int i = 0; i < boxlist.size(); i++) {
            final int position = i;
            TextView textView = textlist.get(position);
            ImageView imageView = imglist.get(position);
            Map<String, String> map = taglist.get(position);
            String text = map.get("text").toString();
            textlist.get(position).setText(text);
            TextPaint tp = textView.getPaint();
            tp.setFakeBoldText(true);
            String imageUrl = map.get("imageUrl").toString();
            Glide.with(getActivity())
                    .load(imageUrl)
                    .placeholder(R.mipmap.zw_img_160)
                    .thumbnail(0.5f)
                    .into(imageView);
            boxlist.get(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        EventIdIntentUtil.EventIdIntent(getActivity(), tag.getJSONObject(position));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /***
     * 加载轮播
     * @param fabiao
     * @throws JSONException
     */
    private void loadViewflipper(JSONArray fabiao) throws JSONException {
        final String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        for (int i = 0; i < fabiao.length(); i++) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.flipper_bidhome, null);
            TextView mtitle = view.findViewById(R.id.mtitle);
            TextView mbuyprice = view.findViewById(R.id.mbuyprice);
            TextView msellprice = view.findViewById(R.id.msellprice);
            TextView mcount = view.findViewById(R.id.mcount);
            ImageView mimg = view.findViewById(R.id.mimg);
            final JSONObject object = fabiao.getJSONObject(i);
            final String id = object.optString("id");
            String title = object.optString("title");
            final String count = object.optString("count");
            String buyprice = object.optString("buyprice");
            String img = object.optString("img");
            String sellprice = object.optString("sellprice");
            final String url = object.optString("url");
            mtitle.setText(title);
            mbuyprice.setText("我要价 " + buyprice);
            msellprice.setText("扑倒价 " + sellprice);
            mcount.setText("扑倒中" + count + "人");
            Glide.with(getActivity()).load(img).into(mimg);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (object.get("userid").equals(userID)) {
                            Intent intent = new Intent(getActivity(), BidBillDetailActivity.class);
                            intent.putExtra("fbid", id);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getActivity(), BidDetailActivity.class);
                            intent.putExtra("id", id);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            mviewflipper.addView(view);
        }
        Animation ru = AnimationUtils.loadAnimation(getActivity(), R.anim.lunbo_ru);
        Animation chu = AnimationUtils.loadAnimation(getActivity(), R.anim.lunbo_chu);
        mviewflipper.setInAnimation(ru);
        mviewflipper.setOutAnimation(chu);
        mviewflipper.startFlipping();
    }

    /**
     * 加载Banner
     *
     * @param banner
     */
    private void loadbanner(final JSONArray banner) {
        List<Object> imgUrlList = new ArrayList<>();
        try {
            for (int i = 0; i < banner.length(); i++) {
                JSONObject jo = banner.getJSONObject(i);
                String imgUrl = jo.getString("img");
                imgUrlList.add(imgUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mBanner.setImages(imgUrlList)
                .setImageLoader(new GlideImageLoader())
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        try {
                            EventIdIntentUtil.EventIdIntent(getActivity(), banner.getJSONObject(position));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setDelayTime(3000)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setIndicatorGravity(BannerConfig.CENTER)
                .start();
        mBanner.setOnTouchListener(new View.OnTouchListener() {
            public float startX;
            public float startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mBanner.requestDisallowInterceptTouchEvent(true);
                        // 记录手指按下的位置
                        startY = event.getY();
                        startX = event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 获取当前手指位置
                        float endY = event.getY();
                        float endX = event.getX();
                        float distanceX = Math.abs(endX - startX);
                        float distanceY = Math.abs(endY - startY);
                        mBanner.requestDisallowInterceptTouchEvent(true);
//                        refreshLayout.setEnabled(false);
                        // 如果X轴位移大于Y轴位移，那么将事件交给viewPager处理。
                        if (distanceX + 500 < distanceY) {
                            mBanner.requestDisallowInterceptTouchEvent(false);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
//                        refreshLayout.setEnabled(true);
                        break;
                }
                return false;
            }
        });
    }

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
        height = llTop.getHeight()-lin.getHeight();
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

    @OnClick(R.id.image_puba)
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(), BidHomeActivity.class);
        startActivity(intent);
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
        if (flag != null) {
            if (flag.equals("0") || flag.equals("")) {
                handler.sendEmptyMessageDelayed(4, 100);
            } else {
                page++;
                x = 2;
                initDataCzg(keyword);
            }
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        initData();
        if (titlelist.size() > 0 && titlelist != null) {
            updateTitle(0, mbox2,keyword);
            updateTitleTop(0, mbox1,keyword);
        }
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
//        Log.i("=========",refreshLayout.getState()+"=====");
        switch (refreshLayout.getState()){
            case None:
                AnimationUtil.with().topMoveToViewLocation(lin,500);
                lin.setVisibility(View.VISIBLE);
                break;
            case PullDownCanceled:
                AnimationUtil.with().topMoveToViewLocation(lin,500);
                lin.setVisibility(View.VISIBLE);
                break;
            case Refreshing:
            case RefreshFinish:
            case RefreshReleased:
            case PullDownToRefresh:
                AnimationUtil.with().moveToViewTop(lin,500);
                lin.setVisibility(View.GONE);
                break;
        }
    }
}
