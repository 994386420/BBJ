package com.bbk.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.alibaba.fastjson.JSON;
import com.andview.refreshview.XScrollView;
import com.bbk.Bean.NewHomeBlBean;
import com.bbk.Bean.NewHomeCzgBean;
import com.bbk.Bean.NewHomeFxBean;
import com.bbk.Bean.NewHomePubaBean;
import com.bbk.Decoration.TwoDecoration;
import com.bbk.activity.BidListDetailActivity;
import com.bbk.activity.HomeActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.ResultMainActivity;
import com.bbk.activity.SearchMainActivity;
import com.bbk.activity.SortActivity;
import com.bbk.activity.UserLoginNewActivity;
import com.bbk.activity.WebViewWZActivity;
import com.bbk.adapter.NewBjAdapter;
import com.bbk.adapter.NewBlAdapter;
import com.bbk.adapter.NewCzgAdapter;
import com.bbk.adapter.NewFxAdapter;
import com.bbk.adapter.NewHomeAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.dialog.HomeAlertDialog;
import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.model.BaseService;
import com.bbk.model.PayModel;
import com.bbk.resource.Constants;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.EventIdIntentUtil;
import com.bbk.util.GlideImageLoader;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.CommonLoadingView;
import com.bbk.view.MyNewScrollView;
import com.bbk.view.RefreshableView;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zyao89.view.zloading.ZLoadingView;
import com.zyao89.view.zloading.Z_TYPE;


public class NewHomeFragment extends BaseViewPagerFragment implements OnClickListener, ResultEvent,OnClickListioner,CommonLoadingView.LoadingHandler{
    private DataFlow6 dataFlow;
    private View mView;
    /**
     * Banner
     */
//    private Banner mBanner;//首页banner
//    private LinearLayout mSort,mSearch;//搜索，分类;
    private JSONArray banner = new JSONArray();
    /**
     * 中间布局
     */
    private JSONArray tag = new JSONArray();
    private List<Map<String, String>> taglist = new ArrayList<>();
    private JSONArray gongneng = new JSONArray();
    private JSONArray fabiao = new JSONArray();
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
     * @param savedInstanceState
     */
    private LinearLayout mLlCzgLayout,mLlbjLayout,mLlblLayout,mLlfxLayout;
    private View mCzgView,mBjView,mBlView,mFxView;
    private TextView mCzgText,mBjText,mBlText,mFxText;
    private NewHomeAdapter homeadapter;
    private int page = 1,x = 1;
    private String type = "1",flag = "";
    private String wztitle = "";
    private View view;
    private ImageView huodongimg;//活动按钮
    //第一次引导页是否显示隐藏
    private boolean isshowzhezhao = true;
    final String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
    private boolean isHomeGudie = false;
    JSONObject jo,preguanggao;
    private RecyclerView mrecyclerview;
    private  RefreshLayout refreshLayout;
    JSONObject object;
    private LinearLayout mSuspensionBar;
    private IWXAPI msgApi = null;
    private int data;// 支付结果标识
    private PayReq mReq;
    private PayModel mPayModel;
    private CommonLoadingView zLoadingView;//加载框
    List<NewHomeCzgBean> czgBeans;//超值购数据
    List<NewHomePubaBean> pubaBeans;//扑吧数据
    List<NewHomeBlBean> blBeans;//爆料数据
    List<NewHomeFxBean> fxBeans;//发现数据
    private ImageButton imageButton;
    private String content;
    String isFirstResultUse;
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
    private void initView(View v){
        imageButton = mView.findViewById(R.id.to_top_btn);
        zLoadingView = mView.findViewById(R.id.progress);
        zLoadingView.setLoadingHandler(this);
        huodongimg =mView.findViewById(R.id.huodongimg);
        view = v.findViewById(R.id.view);
        mTopView = mView. findViewById(R.id.tv_topView);
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
        refreshLayout = (RefreshLayout)mView.findViewById(R.id.refresh_root);
        refreshLayout.setPrimaryColorsId(R.color.button_color, android.R.color.white);//全局设置主题颜色
        refreshLayout.setEnableFooterFollowWhenLoadFinished(true);
        refreshLayout.setEnableOverScrollDrag(true);
        refreshLayout.setEnableOverScrollBounce(true);
        refreshLayout.setEnableFooterTranslationContent(true);
        //设置 Header 为 贝塞尔雷达 样式
        refreshLayout.setRefreshHeader(new BezierCircleHeader(getActivity()));
      //设置 Footer 为 球脉冲 样式
        refreshLayout.setRefreshFooter(new BallPulseFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale).setNormalColor(getActivity().getResources().getColor(R.color.button_color)).setAnimatingColor(getActivity().getResources().getColor(R.color.button_color)));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
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
                        refreshlayout.finishRefresh(2000);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                handler.sendEmptyMessageDelayed(4,100);
                refreshlayout.finishLoadMore(1500);
//                refreshlayout.finishLoadmore(2000/*,false*/);//传入false表示加载失败
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
        ((SimpleItemAnimator)mrecyclerview.getItemAnimator()).setSupportsChangeAnimations(false);
        mSuspensionBar = mView.findViewById(R.id.layout_click);
        final LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity());
        mrecyclerview.setLayoutManager(gridLayoutManager);
        mrecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstPosition = gridLayoutManager.findFirstVisibleItemPosition();
                if (firstPosition >0) {
                    mSuspensionBar.setVisibility(View.VISIBLE);
                    imageButton.setVisibility(View.VISIBLE);
                } else {
                    mSuspensionBar.setVisibility(View.GONE);
                    imageButton.setVisibility(View.GONE);
                }
            }
        });
    }

    //首页数据请求
    private void initData() {
        Map<String, String> maps = new HashMap<String, String>();
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
                        StringUtil.showToast(getActivity(), "网络异常");
                    }
                });
    }
    private void initDataWx(boolean is) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("paytype","wx");
        dataFlow.requestData(3, "appPayService/getOrderInfo", paramsMap, this, is);
    }
    //首页分类数据
    private void getIndexByType() {
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("type",type);
        maps.put("page",page+"");
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
                        StringUtil.showToast(getActivity(), "网络异常");
                    }
                });
    }
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.ll_czg_layout:
                flag ="1";
                setView();
                mIdex("1");
                setText(mCzgText,mCzgView);
//                initDataWx(true);
                break;
            case R.id.ll_bj_layout:
                flag ="2";
                setView();
                mIdex("2");
                setText(mBjText,mBjView);
                break;
            case R.id.ll_bl_layout:
                flag ="3";
                setView();
                mIdex("3");
                setText(mBlText,mBlView);
                break;
            case R.id.ll_fx_layout:
                flag ="4";
                setView();
                mIdex("4");
                setText(mFxText,mFxView);
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

    private void setText(TextView text,View view){
        text.setTextColor(getResources().getColor(R.color.color_line_top));
        view.setVisibility(View.VISIBLE);
    }

    private void setView(){
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
        switch (requestCode){
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
                }catch (Exception e){
                    e.printStackTrace();
                }
            break;
        }
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    break;
                case 2:
                    try {
                        refreshLayout.finishLoadmore();
                        refreshLayout.finishRefresh();
                        if (type.equals("2")){
                            pubaBeans = JSON.parseArray(content, NewHomePubaBean.class);
                            if (x == 1) {
                                Refresh("2");
                            }else if (x == 2) {
                                if (pubaBeans != null && pubaBeans.size()>0){
                                    homeadapter.notifyBjData(pubaBeans);
                                }else {
                                    StringUtil.showToast(getActivity(),"没有更多了");
                                }
                            }
                        }else if (type.equals("3")){
                            blBeans = JSON.parseArray(content, NewHomeBlBean.class);
                            if (x == 1) {
                                Refresh("3");
                            }else if (x == 2) {
                                if (blBeans != null && blBeans.size() > 0){
                                    homeadapter.notifyBlData(blBeans);
                                }else {
                                    StringUtil.showToast(getActivity(),"没有更多了");
                                }
                            }
                        }else if (type.equals("4")){
                            fxBeans = JSON.parseArray(content, NewHomeFxBean.class);
                            if (x == 1) {
                                Refresh("4");
                            }else if (x == 2) {
                                if (fxBeans != null && fxBeans.size() > 0){
                                    homeadapter.notifyFxData(fxBeans);
                                }else {
                                    StringUtil.showToast(getActivity(),"没有更多了");
                                }
                            }
                        }else if (type.equals("1")){
                            czgBeans = JSON.parseArray(content, NewHomeCzgBean.class);
                            if (x == 1) {
                                Refresh("1");
                            }else if (x == 2) {
                                if (czgBeans != null && czgBeans.size() > 0){
                                    homeadapter.notifyData(czgBeans);
                                }else {
                                    StringUtil.showToast(getActivity(),"没有更多了");
                                }
                            }
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    if (object.has("fubiao")){
                        huodongimg.setVisibility(View.VISIBLE);
                        final JSONObject jo;
                        try {
                            jo = object.getJSONObject("fubiao");
                            Glide.with(getActivity()).load(jo.optString("img")).into(huodongimg);
                            huodongimg.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    EventIdIntentUtil.EventIdIntent(getActivity(),jo);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {
                        huodongimg.setVisibility(View.GONE);
                    }
                    banner = object.optJSONArray("banner");
                    tag = object.optJSONArray("tag");
                    if (object.has("gongneng")){
                        gongneng = object.optJSONArray("gongneng");
                    }
                    fabiao = object.optJSONArray("fabiao");
                    mrecyclerview.addItemDecoration(new TwoDecoration(0,"#f3f3f3",3+banner.length()+tag.length()));
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
                                String isFirstResultUse = SharedPreferencesUtil.getSharedData(getActivity(),"isFirstHomeUse", "isFirstHomeUserUse");
                                if (isFirstResultUse.equals("no")){
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

    public void Refresh(String type){
        mSuspensionBar.setVisibility(View.GONE);
        homeadapter = new NewHomeAdapter(getActivity(),taglist,banner, tag, fabiao,gongneng,type,czgBeans,pubaBeans,blBeans,fxBeans,jo);
        mrecyclerview.setAdapter(homeadapter);
//        homeadapter.notifyDataSetChanged();
        if (homeadapter != null) {
            homeadapter.setOnClickListioner(NewHomeFragment.this);
        }
    }
    //首页视图数据加载
    private void mViewLoad(){
        handler.sendEmptyMessageDelayed(5,0);
    }
    //超值购等数据
    private void mIdex(String str){
        type = str;
        x=1;
        page=1;
//        zLoadingView.load();
        getIndexByType();
    }
    @Override
    protected void loadLazyData() {
//        refreshLayout.autoRefresh();
        isFirstResultUse = SharedPreferencesUtil.getSharedData(getActivity(),"isFirstHomeUse", "isFirstHomeUserUse");
        if (isFirstResultUse.equals("no")) {
            refreshLayout.autoRefresh();
        }else {
            DialogSingleUtil.show(getActivity());
            mViewLoad();
        }
    }

    @Override
    public void onCzgClick() {
        setView();
        mIdex("1");
        setText(mCzgText,mCzgView);
    }

    @Override
    public void onBjClick() {
        setView();
        mIdex("2");
        setText(mBjText,mBjView);
    }

    @Override
    public void onBlClick() {
        setView();
        mIdex("3");
        setText(mBlText,mBlView);
    }

    @Override
    public void onFxClick() {
        setView();
        mIdex("4");
        setText(mFxText,mFxView);
    }

    @Override
    public void onDissmissClick() {
//        if (isHomeGudie) {
//            SharedPreferencesUtil.putSharedData(getActivity(), "isFirstHomeUse","isFirstHomeUserUse", "no");
//        }else{
//            isHomeGudie = true;
//        }
        SharedPreferencesUtil.putSharedData(getActivity(), "isFirstHomeUse","isFirstHomeUserUse", "no");
        isFirstResultUse = SharedPreferencesUtil.getSharedData(getActivity(),"isFirstHomeUse", "isFirstHomeUserUse");
        if (isFirstResultUse.equals("no")){
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
                            String isFirstResultUse = SharedPreferencesUtil.getSharedData(getActivity(),"isFirstHomeUse", "isFirstHomeUserUse");
                            if (isFirstResultUse.equals("no")){
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
}
