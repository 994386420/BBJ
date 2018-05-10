package com.bbk.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.ali.auth.third.login.callback.LogoutCallback;
import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.alibaba.baichuan.android.trade.callback.AlibcLoginCallback;
import com.andview.refreshview.XRefreshView;
import com.bbk.Decoration.TwoDecoration;
import com.bbk.activity.BidAcceptanceActivity;
import com.bbk.activity.BidBillDetailActivity;
import com.bbk.activity.BidDetailActivity;
import com.bbk.activity.BidHomeActivity;
import com.bbk.activity.BidMyBillDetailActivity;
import com.bbk.activity.DataFragmentActivity;
import com.bbk.activity.DomainMoreActivity;
import com.bbk.activity.GossipPiazzaDetailActivity;
import com.bbk.activity.HomeActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.QueryHistoryActivity;
import com.bbk.activity.R;
import com.bbk.activity.SearchMainActivity;
import com.bbk.activity.SortActivity;
import com.bbk.activity.UserLoginNewActivity;
import com.bbk.activity.WebViewActivity;
import com.bbk.activity.WebViewWZActivity;
import com.bbk.adapter.BidAcceptanceAdapter;
import com.bbk.adapter.BidMsgInformAdapter;
import com.bbk.adapter.HomeAdapter;
import com.bbk.adapter.NewBjAdapter;
import com.bbk.adapter.NewBlAdapter;
import com.bbk.adapter.NewCzgAdapter;
import com.bbk.adapter.NewFxAdapter;
import com.bbk.adapter.NewHomeAdapter;
import com.bbk.dialog.HomeAlertDialog;
import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.resource.Constants;
import com.bbk.util.DensityUtils;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.EventIdIntentUtil;
import com.bbk.util.GlideImageLoader;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.HeaderView;
import com.bbk.view.HeaderViewHome;
import com.bbk.view.MyFootView;
import com.bbk.view.MyListView;
import com.bbk.view.MyNewScrollView;
import com.bbk.view.MyScrollListView;
import com.bbk.view.RefreshableView;
import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NewHomeFragment extends BaseViewPagerFragment implements OnClickListener, ResultEvent,MyNewScrollView.OnScrollListener,RefreshableView.RefreshListener {
    private DataFlow6 dataFlow;
    private View mView;
    /**
     * Banner
     */
    private Banner mBanner;//首页banner
    private LinearLayout mSort,mSearch;//搜索，分类;
    private JSONArray banner = new JSONArray();
    /**
     * 中间布局
     */
    private JSONArray tag = new JSONArray();
    private List<Map<String, String>> taglist = new ArrayList<>();
    private JSONArray gongneng = new JSONArray();
    private JSONArray fabiao = new JSONArray();
    private MyNewScrollView myScrollView;
    /**
     * 顶部固定的TabViewLayout
     */
    private LinearLayout mTopTabViewLayout;
    /**
     * 跟随ScrollView的TabviewLayout
     */
    private LinearLayout mTabViewLayout;

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
    private NewBjAdapter adapter;
    private NewBlAdapter mBlAdapter;
    private NewCzgAdapter mCzgAdapter;
    private NewFxAdapter mFxAdapter;
    private RecyclerView mlistview;
    private int page = 1,x = 1;
    private String type = "1";
    private List<Map<String,String>> list,addlist,mList;
    private String wztitle = "";
    private ViewFlipper mviewflipper;//发标动态轮播
    public static XRefreshView mRefreshableView;
    private View view;
    private ImageView huodongimg;//活动按钮
    //第一次引导页是否显示隐藏
    private boolean isshowzhezhao = true;
    final String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
    private boolean isHomeGudie = false;
    JSONObject jo;
    private boolean isclear = false;
    private RecyclerView mrecyclerview;

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
            dataFlow = new DataFlow6(getContext());
            getActivity().getWindow().setBackgroundDrawable(null);
            mView = inflater.inflate(R.layout.activity_new_home_layout, null);
            View topView = mView.findViewById(R.id.lin);
            // 实现沉浸式状态栏
            ImmersedStatusbarUtils.initAfterSetContentView(getActivity(), topView);
            initView(mView);
            mViewLoad();
        }
        return mView;

    }
    //控件实例化
    private void initView(View v){
        mrecyclerview = (RecyclerView) mView.findViewById(R.id.mrecycler);
        huodongimg =mView.findViewById(R.id.huodongimg);
        view = v.findViewById(R.id.view);
        mRefreshableView =  v.findViewById(R.id.refresh_root);
        mRefreshableView.setCustomHeaderView(new HeaderViewHome(getActivity()));
        refreshAndloda();
        mviewflipper = mView.findViewById(R.id.mviewflipper);
        mBanner = v.findViewById(R.id.banner);
        mSearch = v.findViewById(R.id.msearch);
        mSort = v.findViewById(R.id.msort);
        myScrollView = mView.findViewById(R.id.myScrollView);
        mTabViewLayout = mView.findViewById(R.id.ll_tabView);
        mTopTabViewLayout = mView.findViewById(R.id.ll_tabTopView);
        mTopView = mView. findViewById(R.id.tv_topView);
        //滑动监听
        myScrollView.setOnScrollListener(this);
        layout = mView.findViewById(R.id.layout);
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
        mlistview = mView.findViewById(R.id.mlistview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mlistview.setLayoutManager(linearLayoutManager);
        mSearch.setOnClickListener(this);
        mSort.setOnClickListener(this);
    }
    private void refreshAndloda() {
        mRefreshableView.setXRefreshViewListener(new XRefreshView.XRefreshViewListener() {

            @Override
            public void onRelease(float direction) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onRefresh(boolean isPullDown) {
//                mRefreshableView.stopRefresh();
                initData(true);
                setView();
                mViewLoad();
                mIdex("1",2);
                mCzgText.setTextColor(getResources().getColor(R.color.color_line_top));
                mCzgView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onLoadMore(boolean isSilence) {
                page++;
//                loadData();
                getIndexByType(false,2);
            }

            @Override
            public void onHeaderMove(double headerMovePercent, int offsetY) {
                // TODO Auto-generated method stub

            }
        });
        MyFootView footView = new MyFootView(getActivity());
        mRefreshableView.setCustomFooterView(footView);
    }
    //首页数据请求
    private void initData(boolean is) {
        HashMap<String, String> paramsMap = new HashMap<>();
        dataFlow.requestData(1, "newService/queryAppIndexInfo", paramsMap, this, is);
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
                setView();
                mIdex("1",2);
                setText(mCzgText,mCzgView);
                break;
            case R.id.ll_bj_layout:
                setView();
                mIdex("2",2);
                setText(mBjText,mBjView);
                break;
            case R.id.ll_bl_layout:
                setView();
                mIdex("3",2);
                setText(mBlText,mBlView);
                break;
            case R.id.ll_fx_layout:
                setView();
                mIdex("4",2);
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
        mRefreshableView.stopLoadMore();
        mRefreshableView.stopRefresh();
        switch (requestCode){
            case 1:
                try {
                    JSONObject object = new JSONObject(content);
                    if (object.has("fubiao")){
                        huodongimg.setVisibility(View.VISIBLE);
                        final JSONObject jo = object.getJSONObject("fubiao");
                        Glide.with(getActivity()).load(jo.optString("img")).into(huodongimg);
                        huodongimg.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EventIdIntentUtil.EventIdIntent(getActivity(),jo);
                            }
                        });
                    }else {
                        huodongimg.setVisibility(View.GONE);
                    }
                    banner = object.optJSONArray("banner");
                    tag = object.optJSONArray("tag");
                    if (object.has("gongneng")){
                        gongneng = object.optJSONArray("gongneng");
                    }
                    fabiao = object.optJSONArray("fabiao");
                    loadbanner(banner);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
                    mrecyclerview.setLayoutManager(gridLayoutManager);
                    mrecyclerview.addItemDecoration(new TwoDecoration(0,"#f3f3f3",3+banner.length()+tag.length()));
                    mrecyclerview.setHasFixedSize(true);
                    homeadapter = new NewHomeAdapter(getActivity(),taglist, banner, tag, fabiao,gongneng);
                    mrecyclerview.setAdapter(homeadapter);
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
                    JSONArray array = new JSONArray(content);
                    if (type.equals("2")){
                        addList(array);
                    }else if (type.equals("3")){
                        addBList(array);
                    }else if (type.equals("4")){
                        addFxList(array);
                    }else if (type.equals("1")){
                        addCzgList(array);
                    }
//                    loadBjData();
//                    initListener();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case 3:
            break;
            case 4:
                Intent intent = new Intent(getActivity(), WebViewWZActivity.class);
                intent.putExtra("url", content);
                intent.putExtra("title", wztitle);
                startActivity(intent);
                break;
        }
    }

    //放数据
    private void loadBjData(){
        if (x==1){
            mList = list;
            if (mList != null && mList.size() > 0) {
                if (type.equals("2")){
                    mlistview.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter = new NewBjAdapter(getActivity(), mList);
                    mlistview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }else if(type.equals("3")){
//                    mBlAdapter = new NewBlAdapter(getActivity(), mList);
//                    mlistview.setAdapter(mBlAdapter);
//                    mBlAdapter.notifyDataSetChanged();
                }else if (type.equals("4")){
//                    mFxAdapter = new NewFxAdapter(getActivity(), mList);
//                    mlistview.setAdapter(mFxAdapter);
//                    mFxAdapter.notifyDataSetChanged();
                }else if (type.equals("1")){
//                    mCzgAdapter = new NewCzgAdapter(getActivity(), mList);
//                    mlistview.setAdapter(mCzgAdapter);
//                    mCzgAdapter.notifyDataSetChanged();
                }
                mlistview.setVisibility(View.VISIBLE);
//                mlistview.setOnItemClickListener(onItemClickListener);
//                mlistview.LoadingComplete();   //告诉listview已经加载完毕,重置提示文字
//                myScrollView.loadingComponent();//告示scrollview已经加载完毕，重置并发控制符的值
            }else {
                mlistview.setVisibility(View.GONE);
            }
        }else if (x==2){
            addlist = list;
            if (addlist != null && addlist.size() > 0){
                if (type.equals("2")){
                    adapter.notifyData(addlist);
                }else if(type.equals("3")){
                    mBlAdapter.notifyData(addlist);
                }else if (type.equals("4")){
                    mFxAdapter.notifyData(addlist);
                }else if(type.equals("1")){
                    mCzgAdapter.notifyData(addlist);
                }
//                mlistview.LoadingComplete();
                myScrollView.loadingComponent();//告示scrollview已经加载完毕，重置并发控制符的值
            }else {
//                mlistview.LoadingCompleted();
            }
        }
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
        if (mFxAdapter != null){
            if (list != null && list.size() > 0){
                if (page == 1){
                    mFxAdapter = new NewFxAdapter(getActivity(), list);
                    mlistview.setAdapter(mFxAdapter);
                }else {
                    mFxAdapter.notifyData(list);
                }
                mlistview.setVisibility(View.VISIBLE);
            }
        }else {
            if (list != null && list.size() > 0){
                mFxAdapter = new NewFxAdapter(getActivity(), list);
                mlistview.setAdapter(mFxAdapter);
                mFxAdapter.notifyDataSetChanged();
                mlistview.setVisibility(View.VISIBLE);
            }else {
                mlistview.setVisibility(View.GONE);
            }
        }
        isclear = false;
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
            list.add(map);
        }
        if (mBlAdapter != null){
            if (list != null && list.size() > 0){
                if (page == 1){
                    mBlAdapter = new NewBlAdapter(getActivity(), list);
                    mlistview.setAdapter(mBlAdapter);
                }else {
                    mBlAdapter.notifyData(list);
                }
                mlistview.setVisibility(View.VISIBLE);
            }
        }else {
            if (list != null && list.size() > 0){
                mBlAdapter = new NewBlAdapter(getActivity(), list);
                mlistview.setAdapter(mBlAdapter);
                mBlAdapter.notifyDataSetChanged();
                mlistview.setVisibility(View.VISIBLE);
            }else {
                mlistview.setVisibility(View.GONE);
            }
        }
        isclear = false;
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
        if (adapter != null){
            if (list != null && list.size() > 0){
                if (page == 1){
                    adapter = new NewBjAdapter(getActivity(), list);
                    mlistview.setAdapter(adapter);
                }else {
                    adapter.notifyData(list);
                }
                mlistview.setVisibility(View.VISIBLE);
            }
        }else {
            if (list != null && list.size() > 0){
                adapter = new NewBjAdapter(getActivity(), list);
                mlistview.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                mlistview.setVisibility(View.VISIBLE);
            }else {
                mlistview.setVisibility(View.GONE);
            }
        }
        isclear = false;
    }
    //超值购数据
    public void addCzgList(JSONArray array) throws JSONException {
        mRefreshableView.setPullLoadEnable(true);
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
            if (!object.optString("hislowprice").isEmpty()){
                map.put("hislowprice",object.optString("hislowprice"));
            }
            list.add(map);
        }
        if (mCzgAdapter != null){
            if (list != null && list.size() > 0){
                if (page == 1){
                    mCzgAdapter = new NewCzgAdapter(getActivity(), list);
                    mlistview.setAdapter(mCzgAdapter);
                }else {
                    mCzgAdapter.notifyData(list);
                }
                mlistview.setVisibility(View.VISIBLE);
            }
        }else {
            if (list != null && list.size() > 0){
                mCzgAdapter = new NewCzgAdapter(getActivity(), list);
                mlistview.setAdapter(mCzgAdapter);
                mCzgAdapter.notifyDataSetChanged();
//                mlistview.setOnItemClickListener(onItemClickListener);
                mlistview.setVisibility(View.VISIBLE);
            }else {
                mlistview.setVisibility(View.GONE);
            }
        }
        isclear = false;
    }
//    首页Banner
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
       mBanner.setOnTouchListener(onTouchListener);
    }


//    private void initListener() {
//        myScrollView.setOnZdyScrollViewListener(new MyNewScrollView.OnZdyScrollViewListener() {
//            @Override
//            public void ZdyScrollViewListener() {
//                mlistview.onLoading();
//                setListView();
//            }
//        });
//    }
//    private void setListView() {
//            page++;
//            x=2;
//            getIndexByType(false,2);
//    };


    //首页数据下拉刷新
    @Override
    public void onRefresh(RefreshableView view) {
        initData(true);
        setView();
        mViewLoad();
        mIdex("1",2);
        mCzgText.setTextColor(getResources().getColor(R.color.color_line_top));
        mCzgView.setVisibility(View.VISIBLE);
//        initListenerczg();
//        initListener();
    }
    //首页视图数据加载
    private void mViewLoad(){
        isclear = true;
        initData(true);
        getIndexByType(true,2);
//        initListenerczg();
//        initListener();
    }
    //超值购等数据
    private void mIdex(String str,int code){
        type = str;
//        x=1;
        page=1;
        isclear = true;
        getIndexByType(true,code);
    }

    @Override
    public void onScroll(int scrollY) {
        int mHeight = layout.getBottom();
        //判断滑动距离scrollY是否大于0，因为大于0的时候就是可以滑动了，此时mTabViewLayout.getTop()才能取到值。
        if (scrollY > 0 && scrollY >= mHeight) {
            if (mTopView.getParent() != mTopTabViewLayout) {
                mTabViewLayout.removeView(mTopView);
                mTopTabViewLayout.addView(mTopView);
                view.setVisibility(View.VISIBLE);
            }
        } else {
            if (mTopView.getParent() != mTabViewLayout) {
                mTopTabViewLayout.removeView(mTopView);
                mTabViewLayout.addView(mTopView);
                view.setVisibility(View.GONE);
            }

        }
    }
    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
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
                    // 如果X轴位移大于Y轴位移，那么将事件交给viewPager处理。
                    if (distanceX+500 < distanceY) {
                        mBanner.requestDisallowInterceptTouchEvent(false);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    break;
            }
            return false;
        }
    };

    //1.在别的Fragment的时候mIsVisibleToUser肯定是false,不会调用开始轮播
//2.在当然Fragment的时候mIsVisibleToUser肯定是true,所有我从这个Fragment
//  进入别的Activity又退来的时候,就会开始轮播
//3.从别的Fragment进入Activity再回来的时候触发onResume也会开始轮播,因为
//  mIsVisibleToUser在切换到别的Fragment的时候就已经被置为false了

    @Override
    public void onResume() {
        super.onResume();
//        if (mviewflipper !=null&&mIsVisibleToUser) {//在这里进行一下判断
//            mviewflipper.startFlipping();
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        if (mviewflipper !=null) {
//            mviewflipper.stopFlipping();
//        }
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        mIsVisibleToUser = isVisibleToUser;//被调用时记录下状态
//        if (mviewflipper == null) {
//            return;
//        }
//        if (isVisibleToUser) {
//            mviewflipper.startFlipping();
//        } else {
//            mviewflipper.stopFlipping();
//        }
    }

    @Override
    protected void loadLazyData() {
        //首页引导页只显示一次
        String isFirstResultUse = SharedPreferencesUtil.getSharedData(getActivity(),"isFirstHomeUse", "isFirstHomeUserUse");
        if (TextUtils.isEmpty(isFirstResultUse)) {
            isFirstResultUse = "yes";
        }
        if (isFirstResultUse.equals("yes")) {
            HomeActivity.mHomeGudieImage.setVisibility(View.VISIBLE);
            HomeActivity.mHomeGudieImage.setImageResource(R.mipmap.new_guide_biaoju);
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

}
