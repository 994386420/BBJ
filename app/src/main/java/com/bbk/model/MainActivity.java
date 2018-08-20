package com.bbk.model;

import android.animation.ObjectAnimator;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import com.alibaba.fastjson.JSON;
import com.bbk.Bean.ChaozhigouTypesBean;
import com.bbk.Bean.CheckBean;
import com.bbk.Bean.NewHomeCzgBean;
import com.bbk.activity.BidHomeActivity;
import com.bbk.activity.BrowseActivity;
import com.bbk.activity.IntentActivity;
import com.bbk.activity.JumpDetailActivty;
import com.bbk.activity.MesageCenterActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.SearchMainActivity;
import com.bbk.activity.UserLoginNewActivity;
import com.bbk.adapter.NewCzgAdapter;
import com.bbk.adapter.TypeGridAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.dialog.HomeAlertDialog;
import com.bbk.fragment.BaseViewPagerFragment;
import com.bbk.model.tablayout.XTabLayout;
import com.bbk.resource.NewConstants;
import com.bbk.util.DialogCheckYouhuiUtil;
import com.bbk.util.DialogHomeUtil;
import com.bbk.util.EventIdIntentUtil;
import com.bbk.util.HomeLoadUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.util.UpdataDialog;
import com.bbk.view.CommonLoadingView;
import com.bumptech.glide.Glide;
import com.logg.Logg;
import com.scwang.smartrefresh.header.BezierCircleHeader;
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
import butterknife.Unbinder;


/**
 * 首页新版
 */
public class MainActivity extends BaseViewPagerFragment implements CommonLoadingView.LoadingHandler {
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.banner_layout)
    RelativeLayout bannerLayout;
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
    @BindView(R.id.image_puba)
    ImageView imagePuba;
    @BindView(R.id.mviewflipper)
    ViewFlipper mviewflipper;
    @BindView(R.id.jingtopic)
    LinearLayout jingtopic;
    @BindView(R.id.banner_guanggao)
    Banner bannerGuanggao;
    @BindView(R.id.guanggao_layout)
    RelativeLayout guanggaoLayout;
    @BindView(R.id.toolbaretail)
    Toolbar toolbaretail;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.tablayout)
    XTabLayout tablayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.msearch)
    LinearLayout msearch;
    @BindView(R.id.image_message)
    ImageView imageMessage;
    @BindView(R.id.newpinglun)
    RelativeLayout newpinglun;
    @BindView(R.id.tv_message)
    TextView tvMessage;
    @BindView(R.id.msort)
    LinearLayout msort;
    @BindView(R.id.lin)
    LinearLayout lin;
    @BindView(R.id.type_image)
    LinearLayout typeImage;
    @BindView(R.id.type_grid)
    GridView typeGrid;
    @BindView(R.id.ll_shouqi)
    LinearLayout llShouqi;
    @BindView(R.id.fl_type)
    FrameLayout flType;
    @BindView(R.id.to_top_btn)
    ImageButton toTopBtn;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.progress)
    CommonLoadingView zLoadingView;
    @BindView(R.id.rl_home)
    RelativeLayout rlHome;
    @BindView(R.id.huodongimg)
    ImageView huodongimg;
    private View mView;
    private boolean isshowzhezhao = true;
    private int page = 1, x = 1;
    List<NewHomeCzgBean> czgBeans;//超值购数据
    NewCzgAdapter newCzgAdapter;
    private String keyword = "";
    private int durationRotate = 700;// 旋转动画时间
    private int durationAlpha = 500;// 透明度动画时间
    private boolean isGlobalMenuShow = true;
    TypeGridAdapter typeGridAdapter;
    List<ChaozhigouTypesBean> chaozhigouTypesBeans;
    public static TextView mnewmsg;
    public static boolean isShowCheck = true;
    private ClipboardManager clipboardManager;
    public static boolean cancelCheck = true;// 是否取消查询
    private String copytext;
    private CheckBean checkBean;
    private static Handler mHandler = new Handler();
    private static UpdataDialog updataDialog;
    private HomeLoadUtil homeLoadUtil;
    private int showTime = 0,curposition = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

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
            mView = inflater.inflate(R.layout.new_home_layout, null);
            unbinder = ButterKnife.bind(this, mView);
            mnewmsg = mView.findViewById(R.id.mnewmsg);
            homeLoadUtil = new HomeLoadUtil(getActivity());
            imageMessage.setBackgroundResource(R.mipmap.praise);
            setToolBar();
//            viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
//            viewpager.setAdapter(viewPagerAdapter);
//            viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
            refresh();

        }
        return mView;
    }

    /**
     * 刷新事件
     */
    private void refresh() {
        zLoadingView.setLoadingHandler(this);
        refreshLayout.setEnableLoadMore(false);
        refresh.setEnableRefresh(false);
        refreshLayout.setPrimaryColorsId(R.color.button_color, android.R.color.white);//全局设置主题颜色
        refresh.setEnableAutoLoadMore(true);
        //设置 Header 为 贝塞尔雷达 样式
        refreshLayout.setRefreshHeader(new BezierCircleHeader(getActivity()));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                queryAppIndexInfo();
                if (keyword.equals("")) {
                    page = 1;
                    x = 1;
                    initDataCzg(keyword);
                } else {
                    XTabLayout.Tab tabAt = tablayout.getTabAt(0);
                    tabAt.select();
                }
            }
        });
        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                page++;
                x = 2;
                initDataCzg(keyword);
            }
        });
    }

    /**
     * 初始化setToolBar
     */
    private void setToolBar() {
//        setSupportActionBar(toolbaretail);
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
                //标题栏的渐变
//                toolbaretail.setBackgroundColor(changeAlpha(getResources().getColor(R.color.white)
//                        , Math.abs(verticalOffset * 1.0f) / appBarLayout.getTotalScrollRange()));
                /**
                 * 当前最大高度便宜值除以2 在减去已偏移值 获取浮动 先显示在隐藏
                 */
//                Logg.e(Offset);
                if (Offset == 0) {
                    typeImage.setVisibility(View.GONE);
                    toTopBtn.setVisibility(View.GONE);
                    toolbaretail.setBackgroundResource(R.drawable.bg_home_yinying);
                    tvMessage.setTextColor(Color.WHITE);
                    imageMessage.setBackgroundResource(R.mipmap.bibijing_01);
                } else if (Offset < appBarLayout.getTotalScrollRange() / 2) {
                    toolbaretail.setTitle("");
                    float scale = (float) Offset / appBarLayout.getTotalScrollRange();
                    float alpha = (255 * scale);
//                    toolbaretail.setAlpha();
                    toolbaretail.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                    tvMessage.setTextColor(Color.argb((int) alpha, 0, 0, 0));
//                    imageMessage.setAlpha((appBarLayout.getTotalScrollRange() / 2 - Offset * 1.0f) / appBarLayout.getTotalScrollRange());
                    imageMessage.setBackgroundResource(R.mipmap.order_09);
//                    shareImg.setImageDrawable(getResources().getDrawable(R.mipmap.search));
//                    toolbaretail.setNavigationIcon(R.mipmap.search);
                    /**
                     * 从最低浮动开始渐显 当前 Offset就是  appBarLayout.getTotalScrollRange() / 2
                     * 所以 Offset - appBarLayout.getTotalScrollRange() / 2
                     */
                } else if (Offset > appBarLayout.getTotalScrollRange() / 2) {
//                    float floate = (Offset - appBarLayout.getTotalScrollRange() / 2) * 1.0f / (appBarLayout.getTotalScrollRange() / 2);
//                    toolbaretail.setAlpha(floate);
//                    shareImg.setAlpha(floate);
//                    toolbaretail.setNavigationIcon(R.mipmap.search);
//                    shareImg.setImageDrawable(getResources().getDrawable(R.mipmap.search));
                    toolbaretail.setTitle("");
//                    toolbaretail.setAlpha(floate);
                    toolbaretail.setBackgroundResource(R.color.white);
                    if (Offset == appBarLayout.getTotalScrollRange()) {
                        typeImage.setVisibility(View.VISIBLE);
                        toTopBtn.setVisibility(View.VISIBLE);
                    } else {
                        typeImage.setVisibility(View.GONE);
                        toTopBtn.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    /**
     * 根据百分比改变颜色透明度
     */
    public int changeAlpha(int color, float fraction) {
        int alpha = (int) (Color.alpha(color) * fraction);
        return Color.argb(alpha, 0, 128, 0);
    }

    /**
     * TabLayout
     * 初始化
     */
    private void TabLayout() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };
        recyclerView.setLayoutManager(linearLayoutManager);
        tablayout.setxTabDisplayNum(5);
        tablayout.setOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                Logg.e(tab.getText());
                int j = tab.getPosition();
                if (j == 0) {
                    keyword = "";
                    curposition = 0;
                } else {
                    curposition = j - 1;
                    DialogHomeUtil.show(getActivity());
                    keyword = tab.getText().toString();
                }
                page = 1;
                x = 1;
                initDataCzg(keyword);
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
     * 懒加载
     */
    @Override
    protected void loadLazyData() {
        DialogHomeUtil.show(getActivity());
        queryAppIndexInfo();
        TabLayout();
    }


    /**
     * 首页数据请求
     */
    private void queryAppIndexInfo() {
        Map<String, String> maps = new HashMap<String, String>();
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        maps.put("userid", userID);
        RetrofitClient.getInstance(getActivity()).createBaseApi().queryAppIndexInfo(
                maps, new BaseObserver<String>(getActivity()) {
                    @Override
                    public void onNext(String s) {
                        try {
                            recyclerView.setVisibility(View.VISIBLE);
                            refreshLayout.setVisibility(View.VISIBLE);
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                JSONObject object = jsonObject.optJSONObject("content");
                                //活动图标
                                if (object.has("fubiao")) {
                                    huodongimg.setVisibility(View.VISIBLE);
                                    final JSONObject jo;
                                    try {
                                        jo = object.getJSONObject("fubiao");
                                        Glide.with(getActivity()).load(jo.optString("img")).into(huodongimg);
                                        huodongimg.setOnClickListener(new View.OnClickListener() {
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

                                //分类
                                if (object.has("chaozhigouTypes")) {
                                    JSONArray chaozhigouTypes = object.optJSONArray("chaozhigouTypes");
                                    chaozhigouTypesBeans = JSON.parseArray(object.optString("chaozhigouTypes"), ChaozhigouTypesBean.class);
                                    if (showTime == 0) {
                                        showTime++;
                                        typeGridAdapter = new TypeGridAdapter(getActivity(), chaozhigouTypesBeans);
                                        typeGrid.setAdapter(typeGridAdapter);
                                        typeGrid.setOnItemClickListener(onItemClickListener);
                                        for (int i = 0; i < chaozhigouTypes.length(); i++) {
                                            Map<String, String> map1 = new HashMap<>();
                                            String keyword = chaozhigouTypes.getJSONObject(i).optString("name");
                                            if (i == 0) {
                                                tablayout.addTab(tablayout.newTab().setText("超值购"));
//                                            viewPagerAdapter.addItem(new DemoFragment(), "超值购");
                                            }
                                            tablayout.addTab(tablayout.newTab().setText(keyword));
//                                        viewPagerAdapter.addItem(new DemoFragment(), keyword);
//                                        viewpager.setAdapter(viewPagerAdapter);
//                                        tablayout.setupWithViewPager(viewpager);
                                        }
                                    }
                                }
                                //banner
                                if (object.has("banner")){
                                    JSONArray bannerarray = object.optJSONArray("banner");
                                    if (bannerarray != null && bannerarray.length() > 0) {
                                        HomeLoadUtil.loadbanner(getActivity(), banner, bannerarray);
                                    }
                                }
                                //tag
                                if (object.has("tag")) {
                                    JSONArray tag = object.optJSONArray("tag");
                                    List<Map<String, String>> taglist = new ArrayList<>();
                                    if (tag != null && tag.length() > 0) {
                                        HomeLoadUtil.loadTag(getActivity(), taglist, tag, img1, img2, img3, img4, img5, text1, text2, text3, text4, text5, box1, box2, box3, box4, box5);
                                    }
                                }
                                //发镖滚动信息
                                if (object.has("fabiao")){
                                    JSONArray fabiao = object.optJSONArray("fabiao");
                                    if (fabiao != null && fabiao.length() > 0) {
                                        HomeLoadUtil.loadViewflipper(getActivity(), mviewflipper, fabiao);
                                    }
                                }

                                //通栏广告
                                if (object.has("tonglanguanggao")) {
                                    guanggaoLayout.setVisibility(View.VISIBLE);
                                    JSONObject guanggaobanner = object.optJSONObject("tonglanguanggao");
                                    DisplayMetrics dm = new DisplayMetrics();
                                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                                    int width = dm.widthPixels;
                                    int height = dm.heightPixels;
                                    ViewGroup.LayoutParams lp = bannerGuanggao.getLayoutParams();
                                    lp.width = width;
                                    lp.height = height * 1 / 7;
                                    bannerGuanggao.setLayoutParams(lp);
                                    HomeLoadUtil.loadGuanggaoBanner(getActivity(), bannerGuanggao, guanggaobanner);
                                } else {
                                    guanggaoLayout.setVisibility(View.GONE);
                                }

                                //未读消息
                                if (object.has("messages")) {
                                    NewConstants.messages = object.optInt("messages");
                                }

                                //引导页
                                img3.post(new Runnable() {
                                    //                    @Override
                                    public void run() {
                                        //首页引导页只显示一次
                                        String isFirstResultUse = SharedPreferencesUtil.getSharedData(getActivity(), "isFirstHomeUse", "isFirstHomeUserUse");
                                        if (TextUtils.isEmpty(isFirstResultUse)) {
                                            isFirstResultUse = "yes";
                                        }
                                        if (isFirstResultUse.equals("yes")) {
                                            HomeLoadUtil.showGuideView(getActivity(), img3, img4);
                                        }
                                    }
                                });

                                //eventid 为108 表示点击之后跳到登录页面。如果已经登录，则不显示preguanggao，显示guanggao 未登录 显示preguanggao
                                String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
                                if (TextUtils.isEmpty(userID)) {
                                    if (object.has("preguanggao")) {
                                        if (isshowzhezhao) {
                                            final JSONObject preguanggao = object.optJSONObject("preguanggao");
                                            new HomeAlertDialog(getActivity()).builder()
                                                    .setimag(preguanggao.optString("img"))
                                                    .setonclick(new View.OnClickListener() {
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
                                            final JSONObject jo = object.optJSONObject("guanggao");
                                            new HomeAlertDialog(getActivity()).builder()
                                                    .setimag(jo.optString("img"))
                                                    .setonclick(new View.OnClickListener() {
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
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        refreshLayout.setEnableRefresh(true);
                        zLoadingView.loadSuccess();
                    }

                    @Override
                    protected void showDialog() {
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogHomeUtil.dismiss(0);
                        zLoadingView.setVisibility(View.VISIBLE);
                        zLoadingView.loadError();
                        refreshLayout.setVisibility(View.GONE);
                        StringUtil.showToast(getActivity(), e.message);
                    }
                });
    }

    /**
     * 超值购数据请求
     * @param keyword
     */
    private void initDataCzg(String keyword) {
        refresh.setNoMoreData(false);
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
                            refreshLayout.setVisibility(View.VISIBLE);
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
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
                                            typeGridAdapter.setSeclection(curposition);
                                            typeGridAdapter.notifyDataSetChanged();

                                            refresh.setEnableLoadMore(true);
                                            recyclerView.setVisibility(View.VISIBLE);
                                            newCzgAdapter = new NewCzgAdapter(getActivity(), czgBeans);
                                            recyclerView.setAdapter(newCzgAdapter);
                                        } else {
                                            refresh.setEnableLoadMore(false);
                                            recyclerView.setVisibility(View.GONE);
                                        }
                                    }
                                    if (x == 2) {
                                        recyclerView.setVisibility(View.VISIBLE);
                                        if (tmpCzg != null && !tmpCzg.toString().equals("[]")) {
                                            newCzgAdapter.notifyData(czgBeans);;
                                        } else {
                                            refresh.finishLoadMoreWithNoMoreData();
                                        }
                                    }
                                } else if (isBlandCzg.equals("-1") && x == 2 && NewConstants.Flag.equals("3")) {
                                    refresh.finishLoadMoreWithNoMoreData();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        refresh.finishLoadMore();
                        refresh.finishRefresh();
                        refreshLayout.finishRefresh();
                        refresh.setEnableRefresh(false);
                        DialogHomeUtil.dismiss(0);
                        newCzgAdapter.notifyData(czgBeans);
                        NewConstants.showdialogFlg = "0";
                        cancelCheck = true;
                        isShowCheck = false;
                        clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        if (clipboardManager.getText() != null) {
//                            if (isShowCheck) {
                            String text = clipboardManager.getText().toString();
                            copytext = text;
                            if (text != null && !text.equals("") && !text.equals("null")) {
                                if (text.contains("bbj")) {
                                    NewConstants.copyText = text;
                                }
                                // //获得当前activity的名字
                                if (!text.contains("标题:")) {
                                    SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "clipchange", "cm", text);
                                    if (text.contains("http") && text.contains("jd") || text.contains("https") && text.contains("jd") || text.contains("http") && text.contains("taobao") || text.contains("http") && text.contains("tmall") ||
                                            text.contains("http") && text.contains("zmnxbc") || text.contains("http") && text.contains("淘") || text.contains("http") && text.contains("喵口令") || text.contains("https") && text.contains("taobao")
                                            || text.contains("https") && text.contains("tmall") || text.contains("https") && text.contains("zmnxbc") || text.contains("https") && text.contains("淘") || text.contains("https") && text.contains("喵口令")) {
                                        String cliptext = SharedPreferencesUtil.getSharedData(getActivity(), "copyText", "copyText");
                                        if (!text.equals(cliptext)) {
                                            checkExsistProduct(text);
                                        }
                                    }
                                }
//                                }
                            }
                        }
                    }

                    @Override
                    protected void showDialog() {
//                        DialogSingleUtil.show(getActivity());
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        refreshLayout.setVisibility(View.GONE);
                        zLoadingView.setVisibility(View.VISIBLE);
                        zLoadingView.loadError();
                        refresh.finishLoadMore();
                        refresh.finishRefresh();
                        DialogHomeUtil.dismiss(0);
                        recyclerView.setVisibility(View.GONE);
                        StringUtil.showToast(getActivity(), e.message);
                    }
                });
    }

    /**
     * 查询优惠券
     * @param text
     */
    private void checkExsistProduct(String text) {
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("url", text);
        RetrofitClient.getInstance(getActivity()).createBaseApi().checkExsistProduct(
                paramsMap, new BaseObserver<String>(getActivity()) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                String content = jsonObject.optString("content");
                                checkBean = JSON.parseObject(content, CheckBean.class);
                                if (checkBean.getHasCps() != null) {
                                    if (checkBean.getHasCps().equals("1")) {
                                        DialogCheckYouhuiUtil.dismiss(2000);
                                        mHandler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (cancelCheck) {
                                                    showYouhuiDialog(getActivity());
                                                }
                                            }
                                        }, 2000);
                                    }
                                } else {
                                    DialogCheckYouhuiUtil.dismiss(2000);
                                    if (cancelCheck) {
                                        mHandler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                showMessageDialog(getActivity(), checkBean.getUrl());
                                                ;//耗时操作
                                            }
                                        }, 2000);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        SharedPreferencesUtil.putSharedData(getActivity(), "copyText", "copyText", copytext);
//                        clipboardManager.setPrimaryClip(ClipData.newPlainText(null, ""));
                    }

                    @Override
                    protected void showDialog() {
                        if (MainActivity.this != null) {
                            DialogCheckYouhuiUtil.show(getActivity());
                        }
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        StringUtil.showToast(getActivity(), e.message);
                        DialogCheckYouhuiUtil.dismiss(0);
                    }
                });

    }

    /**
     * 有优惠券弹窗
     *
     * @param context
     */
    public void showYouhuiDialog(final Context context) {
        if (updataDialog == null || !updataDialog.isShowing()) {
            //初始化弹窗 布局 点击事件的id
            updataDialog = new UpdataDialog(context, R.layout.check_nomessage_dialog_layout,
                    new int[]{R.id.tv_update_gengxin});
            updataDialog.show();
            updataDialog.setCanceledOnTouchOutside(true);
            LinearLayout llZuji = updataDialog.findViewById(R.id.ll_zuji);
            llZuji.setVisibility(View.VISIBLE);
            llZuji.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
                    if (TextUtils.isEmpty(userID)) {
                        JumpDetailActivty.Flag = "home";
                        Intent intent = new Intent(context, UserLoginNewActivity.class);
                        startActivityForResult(intent, 1);
                    } else {
                        Intent intent = new Intent(context, BrowseActivity.class);
                        updataDialog.dismiss();
                        cancelCheck = false;
                        startActivity(intent);
                    }
                }
            });
            TextView tvZuan = updataDialog.findViewById(R.id.tv_zuan);
            TextView tvQuan = updataDialog.findViewById(R.id.tv_update);
            tvQuan.setTextColor(context.getResources().getColor(R.color.tuiguang_color2));
            tvZuan.setText(checkBean.getMessage1());
            tvQuan.setText(checkBean.getMessage2());
            TextView tv_update_gengxin = updataDialog.findViewById(R.id.tv_update_gengxin);
            tv_update_gengxin.setText("查看优惠");
            tv_update_gengxin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updataDialog.dismiss();
                    Intent intent = new Intent(context, IntentActivity.class);
                    if (checkBean.getUrl() != null && !checkBean.getUrl().equals("")) {
                        intent.putExtra("url", checkBean.getUrl());
                    }
                    if (checkBean.getDomain() != null && !checkBean.getDomain().equals("")) {
                        intent.putExtra("domain", checkBean.getDomain());
                    }
                    if (checkBean.getRowkey() != null && !checkBean.getRowkey().equals("")) {
                        intent.putExtra("groupRowKey", checkBean.getRowkey());
                    }
                    if (checkBean.getPrice() != null && !checkBean.getPrice().equals("")) {
                        intent.putExtra("bprice", checkBean.getPrice());
                    }
                    startActivity(intent);
                }
            });
            LinearLayout ll_close = updataDialog.findViewById(R.id.ll_close);
            ll_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updataDialog.dismiss();
                    cancelCheck = false;
                }
            });
        }
    }

    /**
     * 无优惠券弹窗
     *
     * @param context
     */
    public void showMessageDialog(final Context context, final String url) {
        if (updataDialog == null || !updataDialog.isShowing()) {
            //初始化弹窗 布局 点击事件的id
            updataDialog = new UpdataDialog(context, R.layout.check_nomessage_dialog_layout,
                    new int[]{R.id.tv_update_gengxin});
            updataDialog.show();
            updataDialog.setCanceledOnTouchOutside(true);
            TextView tv_update_gengxin = updataDialog.findViewById(R.id.tv_update_gengxin);
            tv_update_gengxin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updataDialog.dismiss();
                    homeLoadUtil.jumpThirdApp(url);
                }
            });
            LinearLayout ll_close = updataDialog.findViewById(R.id.ll_close);
            ll_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updataDialog.dismiss();
                    cancelCheck = false;
                }
            });
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        DialogHomeUtil.dismiss(0);
        unbinder.unbind();
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
     * 下拉弹出窗点击事件
     */
    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            curposition = position;
            typeGridAdapter.setSeclection(curposition);
            typeGridAdapter.notifyDataSetChanged();
            keyword = chaozhigouTypesBeans.get(position).getKeyword();
//            x = 1;
//            page = 1;
//            initDataCzg(keyword);
            XTabLayout.Tab tabAt = tablayout.getTabAt(position + 1);
            tabAt.select();
            showGlobalMenu();
        }
    };


    /**
     * 点击事件
     * @param view
     */
    @OnClick({R.id.msearch, R.id.msort, R.id.type_image, R.id.ll_shouqi, R.id.image_puba,R.id.to_top_btn})
    public void onViewClicked(View view) {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Intent intent;
        switch (view.getId()) {
            case R.id.msearch:
                intent = new Intent(getActivity(), SearchMainActivity.class);
                startActivity(intent);
                break;
            case R.id.msort:
                if (TextUtils.isEmpty(userID)) {
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), MesageCenterActivity.class);
                    intent.putExtra("type", "0");
                    startActivity(intent);
                }
                break;
            case R.id.type_image:
                showGlobalMenu();
                break;
            case R.id.ll_shouqi:
                showGlobalMenu();
                break;
            case R.id.image_puba:
                intent = new Intent(getActivity(), BidHomeActivity.class);
                startActivity(intent);
                break;
            case R.id.to_top_btn:
                //回到顶部
                recyclerView.scrollToPosition(0);
                CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) appBar.getLayoutParams()).getBehavior();
                if (behavior instanceof AppBarLayout.Behavior) {
                    AppBarLayout.Behavior appBarLayoutBehavior = (AppBarLayout.Behavior) behavior;
                    int topAndBottomOffset = appBarLayoutBehavior.getTopAndBottomOffset();
                    if (topAndBottomOffset != 0) {
                        appBarLayoutBehavior.setTopAndBottomOffset(0);
                        typeImage.setVisibility(View.GONE);
                        toTopBtn.setVisibility(View.GONE);
                    }
                }
                break;
        }
    }

    @Override
    public void doRequestData() {
        DialogHomeUtil.show(getActivity());
        zLoadingView.setVisibility(View.GONE);
        queryAppIndexInfo();
        x = 1;
        page = 1;
        initDataCzg(keyword);
    }
}
