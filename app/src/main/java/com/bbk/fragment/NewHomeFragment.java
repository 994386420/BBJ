package com.bbk.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.alibaba.fastjson.JSON;
import com.andview.refreshview.XScrollView;
import com.bbk.Bean.NewHomeCzgBean;
import com.bbk.Bean.NewHomePubaBean;
import com.bbk.Decoration.TwoDecoration;
import com.bbk.activity.HomeActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.ResultMainActivity;
import com.bbk.activity.SearchMainActivity;
import com.bbk.activity.SortActivity;
import com.bbk.activity.WebViewWZActivity;
import com.bbk.adapter.NewBjAdapter;
import com.bbk.adapter.NewBlAdapter;
import com.bbk.adapter.NewCzgAdapter;
import com.bbk.adapter.NewFxAdapter;
import com.bbk.adapter.NewHomeAdapter;
import com.bbk.dialog.HomeAlertDialog;
import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.model.BaseService;
import com.bbk.model.PayModel;
import com.bbk.resource.Constants;
import com.bbk.util.EventIdIntentUtil;
import com.bbk.util.GlideImageLoader;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.MyNewScrollView;
import com.bbk.view.RefreshableView;
import com.bumptech.glide.Glide;
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


public class NewHomeFragment extends BaseViewPagerFragment implements OnClickListener, ResultEvent,OnClickListioner {
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
    private int maxNum = 0;
    private String type = "1",flag = "";
    private List<Map<String,String>> list,addlist,mList,mAddList;
    private String wztitle = "";
    private ViewFlipper mviewflipper;//发标动态轮播
//    public static XRefreshView mRefreshableView;
    private SmartRefreshLayout smartRefreshLayout;
    private View view;
    private ImageView huodongimg;//活动按钮
    //第一次引导页是否显示隐藏
    private boolean isshowzhezhao = true;
    final String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
    private boolean isHomeGudie = false;
    JSONObject jo;
    private boolean isclear = false;
    private RecyclerView mrecyclerview;
    private  RefreshLayout refreshLayout;
    private boolean isrequest = true;
    JSONObject object;
    JSONArray array;
    private int mCurrentPosition = 0;

    private int mSuspensionHeight;
    private LinearLayout mSuspensionBar;
    private HashMap<String, Object> mPayMap, mPayDataMap;
    private IWXAPI msgApi = null;
    private int data;// 支付结果标识
    private PayReq mReq;
    private PayModel mPayModel;
    private ZLoadingView zLoadingView;//加载框
    List<NewHomeCzgBean> czgBeans;
    List<NewHomePubaBean> pubaBeans;
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
        zLoadingView = mView.findViewById(R.id.progress);
        zLoadingView.setVisibility(View.VISIBLE);
        zLoadingView.setLoadingBuilder(Z_TYPE.DOUBLE_CIRCLE,0.5);
        huodongimg =mView.findViewById(R.id.huodongimg);
        view = v.findViewById(R.id.view);
        refreshAndloda();
        mviewflipper = mView.findViewById(R.id.mviewflipper);
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
                        initData(false);
                        mCzgText.setTextColor(getResources().getColor(R.color.color_line_top));
                        mCzgView.setVisibility(View.VISIBLE);
                mBjText.setTextColor(getResources().getColor(R.color.color_line_text));
                mBjView.setVisibility(View.GONE);
                mBlText.setTextColor(getResources().getColor(R.color.color_line_text));
                mBlView.setVisibility(View.GONE);
                mFxText.setTextColor(getResources().getColor(R.color.color_line_text));
                mFxView.setVisibility(View.GONE);
                        mIdex("1",2,false);
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
        mrecyclerview.setHasFixedSize(true);
        mrecyclerview.setNestedScrollingEnabled(false);
        mSuspensionBar = mView.findViewById(R.id.layout_click);
        final LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity());
        mrecyclerview.setLayoutManager(gridLayoutManager);
        mrecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mSuspensionHeight = mSuspensionBar.getHeight();
            }

            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                View view = gridLayoutManager.findViewByPosition(mCurrentPosition + 1);
//                if (view != null) {
//                    if (view.getTop() <= mSuspensionHeight) {
//                        mSuspensionBar.setVisibility(View.VISIBLE);
//                        mSuspensionBar.setY(0);
//                    } else if (mSuspensionHeight <= view.getTop()){
//                        mSuspensionBar.setVisibility(View.GONE);
//                        mSuspensionBar.setY(0);
//                    }
//                }
//
//                if (mCurrentPosition != gridLayoutManager.findFirstVisibleItemPosition()) {
//                    mCurrentPosition = gridLayoutManager.findFirstVisibleItemPosition();
//                    mSuspensionBar.setY(0);
//                }
                int firstPosition = gridLayoutManager.findFirstVisibleItemPosition();
                if (firstPosition >0) {
                    mSuspensionBar.setVisibility(View.VISIBLE);
                } else {
                    mSuspensionBar.setVisibility(View.GONE);
                }
            }
        });
    }
    private void refreshAndloda() {
    }
    //首页数据请求
    private void initData(boolean is) {
        HashMap<String, String> paramsMap = new HashMap<>();
        dataFlow.requestData(1, "newService/queryAppIndexInfo", paramsMap, this, is);
    }
    private void initDataWx(boolean is) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("paytype","wx");
        dataFlow.requestData(3, "appPayService/getOrderInfo", paramsMap, this, is);
    }
    //首页分类数据
    private void getIndexByType(boolean is,int code) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("type",type);
        paramsMap.put("page",page+"");
        dataFlow.requestData(code, Constants.GetQueryAppIndexByType, paramsMap, this, is);
    }
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.ll_czg_layout:
                flag ="1";
                setView();
                mIdex("1",2,true);
                setText(mCzgText,mCzgView);
//                initDataWx(true);
                break;
            case R.id.ll_bj_layout:
                flag ="2";
                setView();
                mIdex("2",2,true);
                setText(mBjText,mBjView);
                break;
            case R.id.ll_bl_layout:
                flag ="3";
                setView();
                mIdex("3",2,true);
                setText(mBlText,mBlView);
                break;
            case R.id.ll_fx_layout:
                flag ="4";
                setView();
                mIdex("4",2,true);
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
            case 1:
                try {
                    object = new JSONObject(content);
                   handler.sendEmptyMessageDelayed(3,0);
                } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                break;

            case 2:
                try {
                list = new ArrayList<>();
                if (isclear) {
                    list.clear();
                }
                array = new JSONArray(content);
                if (type.equals("2")){
                    if (x == 1) {
                        pubaBeans = JSON.parseArray(content, NewHomePubaBean.class);
                        homeadapter.notifyBjData1(pubaBeans);
                        Refresh(type);
                    }else if (x == 2) {
                        Log.i("支付数据====", content + "============");
                        if (content != null && !content.toString().equals("[]")){
                            pubaBeans = JSON.parseArray(content, NewHomePubaBean.class);
                            homeadapter.notifyBjData(pubaBeans);
                        }else {
                            StringUtil.showToast(getActivity(),"没有更多了");
                        }
                    }
                }else if (type.equals("3")){
                    addBList(array);
                }else if (type.equals("4")){
                    addFxList(array);
                }else if (type.equals("1")){
//                    addCzgList(array);
                    if (x == 1) {
                        czgBeans = JSON.parseArray(content, NewHomeCzgBean.class);
                        homeadapter.notifyData1(czgBeans);
                        Refresh(type);
                    }else if (x == 2) {
                        if (content != null && !content.toString().equals("[]")){
                            czgBeans = JSON.parseArray(content,NewHomeCzgBean.class);
                            homeadapter.notifyData(czgBeans);
                        }else {
                            StringUtil.showToast(getActivity(),"没有更多了");
                        }
                   }
                }
//                if (x == 1) {
//                    mList = list;
////                    Log.i("list=======1111",mList+"==");
//                    handler.sendEmptyMessageDelayed(1, 0);
//                } else if (x == 2) {
////                    Log.i("addlist=======222",mAddList+"====");
//                    mAddList = list;
//                    if (isrequest == true){
//                        handler.sendEmptyMessageDelayed(2, 0);
//                    }
//                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            break;
            case 3:
                Log.i("支付数据", content + "============");
//                String[] strs = content.toString().split("&");
//                mPayDataMap = new HashMap<String, Object>();
//                for (String s : strs) {
//                    String[] str = s.split("=");
//                    mPayDataMap.put(str[0], str[1]);
//                }
//                Log.i("获取json对象", mPayDataMap + "============");
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
            case 4:
                Intent intent = new Intent(getActivity(), WebViewWZActivity.class);
                intent.putExtra("url", content);
                intent.putExtra("title", wztitle);
                startActivity(intent);
                break;
        }
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    refreshLayout.finishLoadmore();
                    refreshLayout.finishRefresh();
                    if (mList.size() > 0 && mList.size()<5){
                    }
                    if (mList != null && mList.size() > 0) {
                        if (type.equals("2")){
//                            homeadapter.notifyBjData1(mList);
                            Refresh(type);
                        }else if (type.equals("3")){
                            homeadapter.notifyBlData1(mList);
                            Refresh(type);
                        }else if (type.equals("4")){
                            homeadapter.notifyFxData1(mList);
                            Refresh(type);
                        }else if (type.equals("1")){
                            homeadapter.notifyData1(czgBeans);
                            Refresh(type);
                        }
                    }
                    break;
                case 2:
                    refreshLayout.finishLoadmore();
                    refreshLayout.finishRefresh();
                    if(mAddList!=null && mAddList.size()>0){
                        if (type.equals("2")){
//                            homeadapter.notifyBjData(mAddList);
                        }else if (type.equals("3")){
                            homeadapter.notifyBlData(mAddList);
                        }else if (type.equals("4")){
                            homeadapter.notifyFxData(mAddList);
                        }else if (type.equals("1")){
//                            Log.i("支付数据=====", mAddList + "============");
//                            czgBeans = JSON.parseArray(mAddList.toString(),NewHomeCzgBean.class);
//                            homeadapter.notifyData(czgBeans);
                        }
                    }else {
                        Log.i("支付数据",  "============");
                        StringUtil.showToast(getActivity(),"没有更多了");
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
//                    loadbanner(banner);
                    mrecyclerview.addItemDecoration(new TwoDecoration(0,"#f3f3f3",3+banner.length()+tag.length()));
                    mrecyclerview.setHasFixedSize(true);
                    homeadapter = new NewHomeAdapter(getActivity(),taglist,list, banner, tag, fabiao,gongneng,type);
                    mrecyclerview.setAdapter(homeadapter);
                    homeadapter.setOnClickListioner(NewHomeFragment.this);
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
                    zLoadingView.setVisibility(View.GONE);
                    break;
                case 4:
                    x = 2;
                    page++;
                    getIndexByType(false,2);
                    break;
                case 5:
                    isclear = true;
                    initData(true);
//                    getIndexByType(true,2);
                    break;
            }
        }
    };

    public void Refresh(String type){
        mSuspensionBar.setVisibility(View.GONE);
        mrecyclerview.setHasFixedSize(true);
        homeadapter = new NewHomeAdapter(getActivity(),taglist,list, banner, tag, fabiao,gongneng,type);
        mrecyclerview.setAdapter(homeadapter);
        homeadapter.setOnClickListioner(NewHomeFragment.this);
        homeadapter.notifyDataSetChanged();
    }
    //发现数据
    public void addFxList(JSONArray array) throws JSONException {
        for (int i = 0; i < array.length() ; i++) {
            JSONObject object = array.getJSONObject(i);
            Map<String,String> map = new HashMap<>();
            map.put("content",object.optString("content"));
            map.put("img",object.optString("img"));
            map.put("atime",object.optString("atime"));
            map.put("count",object.optString("count"));
            map.put("zan",object.optString("zan"));
            map.put("title",object.optString("title"));
            map.put("id",object.optString("id"));
            list.add(map);
        }

    }
    //爆料数据
    public void addBList(JSONArray array) throws JSONException {
        for (int i = 0; i < array.length() ; i++) {
            JSONObject object = array.getJSONObject(i);
            Map<String,String> map = new HashMap<>();
            map.put("extra",object.optString("extra"));
            map.put("img",object.optString("img"));
            map.put("dtime",object.optString("dtime"));
            map.put("readnum",object.optString("readnum"));
            map.put("title",object.optString("title"));
            map.put("plnum",object.optString("plnum"));
            map.put("zannum",object.optString("zannum"));
            map.put("blid",object.optString("blid"));
            map.put("content",object.optString("content"));
            map.put("price",object.optString("price"));
            map.put("url",object.optString("url"));
            list.add(map);
        }
    }
    //镖局数据
    public void addList(JSONArray array) throws JSONException {
        for (int i = 0; i < array.length() ; i++) {
            JSONObject object = array.getJSONObject(i);
            Map<String,String> map = new HashMap<>();
            map.put("endtime",object.optString("endtime"));
            map.put("id",object.optString("id"));
            map.put("img",object.optString("img"));
            map.put("title",object.optString("title"));
            map.put("price",object.optString("price"));
            map.put("extra",object.optString("extra"));
            map.put("number",object.optString("number"));
            map.put("type",object.optString("type"));
            map.put("userid",object.optString("userid"));//新增字段，用于判断是否是自己的发标，是则跳转发标详情
            list.add(map);
        }

    }
    //超值购数据
    public void addCzgList(JSONArray array) throws JSONException {
        for (int i = 0; i < array.length() ; i++) {
            JSONObject object = array.getJSONObject(i);
            Map<String,String> map = new HashMap<>();
            map.put("id",object.optString("id"));
            map.put("imgurl",object.optString("imgurl"));
            map.put("title",object.optString("title"));
            map.put("price",object.optString("price"));
            map.put("dianpu",object.optString("dianpu"));
            map.put("youhui",object.optString("youhui"));
            map.put("url",object.optString("url"));
            if (object.optString("hislowprice") != null){
                map.put("hislowprice",object.optString("hislowprice"));
            }
            list.add(map);
        }
    }
    //首页视图数据加载
    private void mViewLoad(){
        handler.sendEmptyMessageDelayed(5,0);
    }
    //超值购等数据
    private void mIdex(String str,int code,boolean is){
        type = str;
        x=1;
        page=1;
        isclear = true;
        getIndexByType(is,code);
    }
    @Override
    protected void loadLazyData() {
        mViewLoad();
        //首页引导页只显示一次
        String isFirstResultUse = SharedPreferencesUtil.getSharedData(getActivity(),"isFirstHomeUse", "isFirstHomeUserUse");
        if (TextUtils.isEmpty(isFirstResultUse)) {
            isFirstResultUse = "yes";
        }
        if (isFirstResultUse.equals("yes")) {
            HomeActivity.mHomeGudieImage.setVisibility(View.VISIBLE);
            HomeActivity.mHomeGudieImage.setImageResource(R.mipmap.yaoma);
        }
        HomeActivity.mHomeGudieImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (isHomeGudie) {
                        HomeActivity.mHomeGudieImage.setVisibility(View.GONE);
                        SharedPreferencesUtil.putSharedData(getActivity(), "isFirstHomeUse","isFirstHomeUserUse", "no");
                    }else{
                        HomeActivity.mHomeGudieImage.setImageResource(R.mipmap.new_guide_bijia);
                        isHomeGudie = true;
                    }
                    String isFirstResultUse = SharedPreferencesUtil.getSharedData(getActivity(),"isFirstHomeUse", "isFirstHomeUserUse");
                    if (isFirstResultUse.equals("no")){
                        if (isshowzhezhao) {
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
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });
    }

    @Override
    public void onCzgClick() {
        setView();
        mIdex("1",2,true);
        setText(mCzgText,mCzgView);
    }

    @Override
    public void onBjClick() {
        setView();
        mIdex("2",2,true);
        setText(mBjText,mBjView);
    }

    @Override
    public void onBlClick() {
        setView();
        mIdex("3",2,true);
        setText(mBlText,mBlView);
    }

    @Override
    public void onFxClick() {
        setView();
        mIdex("4",2,true);
        setText(mFxText,mFxView);
    }
}
