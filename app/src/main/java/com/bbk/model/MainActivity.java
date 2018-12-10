package com.bbk.model;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.ClipData;
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
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.ChaoJiFanBean;
import com.bbk.Bean.ChaozhigouTypesBean;
import com.bbk.Bean.CheckBean;
import com.bbk.Bean.MiaoShaBean;
import com.bbk.Bean.NewHomeCzgBean;
import com.bbk.Bean.PinTuanBean;
import com.bbk.Bean.PinpaiBean;
import com.bbk.Bean.ShopDianpuBean;
import com.bbk.Bean.TagBean;
import com.bbk.Bean.ZeroBuyBean;
import com.bbk.activity.BidHomeActivity;
import com.bbk.activity.BrowseActivity;
import com.bbk.activity.IntentActivity;
import com.bbk.activity.MesageCenterActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.SearchMainActivity;
import com.bbk.activity.UserLoginNewActivity;
import com.bbk.activity.WebViewActivity;
import com.bbk.adapter.DianPuHoHotGridAdapter;
import com.bbk.adapter.DianPuHomePinpaiGridAdapter;
import com.bbk.adapter.MyHomeTagAdapter;
import com.bbk.adapter.NewCzgAdapter;
import com.bbk.adapter.TypeGridAdapter;
import com.bbk.adapter.ZeroBuyHomeAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.dialog.HomeAlertDialog;
import com.bbk.fragment.BaseViewPagerFragment;
import com.bbk.model.tablayout.XTabLayout;
import com.bbk.resource.NewConstants;
import com.bbk.shopcar.NewDianpuHomeActivity;
import com.bbk.util.DialogCheckYouhuiUtil;
import com.bbk.util.DialogHomeUtil;
import com.bbk.util.EventIdIntentUtil;
import com.bbk.util.HomeLoadUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.util.UpdataDialog;
import com.bbk.view.CommonLoadingView;
import com.bbk.view.RushBuyCountDownTimerHomeView;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.kuaishang.kssdk.KSConfig;
import cn.kuaishang.kssdk.util.KSIntentBuilder;
import cn.kuaishang.listener.KsInitListener;

import static com.ali.auth.third.core.context.KernelContext.context;


/**
 * 首页新版
 */
public class MainActivity extends BaseViewPagerFragment implements CommonLoadingView.LoadingHandler, HomeGridAdapter.LogInterface, HomeGridPinTuanAdapter.LogPinTuanInterface {
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
    @BindView(R.id.tag_list)
    RecyclerView tagList;
    @BindView(R.id.ll_miaosha)
    LinearLayout llMiaosha;
    @BindView(R.id.hot_recyclerview)
    RecyclerView hotRecyclerview;
    @BindView(R.id.ll_pingtuan)
    LinearLayout llPingtuan;
    @BindView(R.id.pingtuan_recyclerview)
    RecyclerView pingtuanRecyclerview;
    @BindView(R.id.ll_czuan)
    LinearLayout llCzuan;
    @BindView(R.id.czuan_recyclerview)
    RecyclerView czuanRecyclerview;
    @BindView(R.id.mtime)
    RushBuyCountDownTimerHomeView mtime;
    @BindView(R.id.ll_huodong)
    LinearLayout llHuodong;
    @BindView(R.id.view_miaosha)
    View viewMiaosha;
    @BindView(R.id.view_pingtuan)
    View viewPingtuan;
    @BindView(R.id.view_czuan)
    View viewCzuan;
    @BindView(R.id.view_zerobuy)
    View viewZerobuy;
    @BindView(R.id.ll_zerobuy)
    LinearLayout llZerobuy;
    @BindView(R.id.zerobuy_recyclerview)
    RecyclerView zerobuyRecyclerview;
    @BindView(R.id.ll_zerobuy_new)
    LinearLayout llZerobuyNew;
    @BindView(R.id.zerobuy_new_recyclerview)
    RecyclerView zerobuyNewRecyclerview;
    @BindView(R.id.view_zerobuy_new)
    View viewZerobuyNew;
    @BindView(R.id.ll_everyday_goodsshop)
    LinearLayout llEverydayGoodsshop;
    @BindView(R.id.everyday_goodsshop_recyclerview)
    RecyclerView everydayGoodsshopRecyclerview;
    @BindView(R.id.view_everyday_goodsshop)
    View viewEverydayGoodsshop;
    @BindView(R.id.ll_gooddianpu)
    LinearLayout llGooddianpu;
    @BindView(R.id.gooddianpu_recyclerview)
    RecyclerView gooddianpuRecyclerview;
    @BindView(R.id.img6)
    ImageView img6;
    @BindView(R.id.tv_sub1)
    TextView tvSub1;
    @BindView(R.id.tv_sub2)
    TextView tvSub2;
    @BindView(R.id.tv_sub3)
    TextView tvSub3;
    @BindView(R.id.tv_sub4)
    TextView tvSub4;
    @BindView(R.id.ll_sub)
    LinearLayout llSub;
    Unbinder unbinder1;
    private View mView;
    private boolean isshowzhezhao = true;
    private int page = 1, x = 1;
    List<NewHomeCzgBean> czgBeans;//超值购数据
    private String chaozhigou;
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
    private int showTime = 0, curposition = 0;
    private String url1, title1, domain1, type1, isczg1, bprice1, quan1, zuan1;
    JSONArray chaozhigouTypes;

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
            refresh();
            doInit();
        }

        unbinder1 = ButterKnife.bind(this, mView);
        return mView;
    }

    /**
     * 快商通客服
     */
    private void doInit() {
        KSConfig.init(getActivity(), "lRUfZ3l/ufCyYcN71F+kh1TMTJuOOgLW", new KsInitListener(){
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(int code, String message) {
            }
        });
    }
    /**
     * 刷新事件
     */
    private void refresh() {
        tagList.setNestedScrollingEnabled(false);
        tagList.setHasFixedSize(true);
        tagList.setFocusable(false);
        tagList.setLayoutManager(new GridLayoutManager(getActivity(), 5));

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
                page = 1;
                try {
                    XTabLayout.Tab tabAt = tablayout.getTabAt(0);
                    tabAt.select();
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
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
                    toolbaretail.setBackgroundColor(Color.argb((int) alpha, 255, 100, 60));
                    tvMessage.setTextColor(Color.argb((int) alpha, 255, 255, 255));
//                    imageMessage.setAlpha((appBarLayout.getTotalScrollRange() / 2 - Offset * 1.0f) / appBarLayout.getTotalScrollRange());
//                    imageMessage.setBackgroundResource(R.mipmap.order_09);
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
                    float scale = (float) Offset / appBarLayout.getTotalScrollRange();
                    float alpha = (255 * scale);
                    toolbaretail.setBackgroundResource(R.color.tuiguang_color5);
                    tvMessage.setTextColor(Color.argb((int) alpha, 255, 255, 255));
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
                    llSub.setVisibility(View.GONE);
                    keyword = "";
                    curposition = 0;
                    czgBeans = JSON.parseArray(chaozhigou, NewHomeCzgBean.class);
                    if (czgBeans != null && czgBeans.size() > 0) {
                        if (typeGridAdapter != null) {
                            typeGridAdapter.setSeclection(curposition);
                            typeGridAdapter.notifyDataSetChanged();
                        }
                        refresh.setEnableLoadMore(true);
                        recyclerView.setVisibility(View.VISIBLE);
                        newCzgAdapter = new NewCzgAdapter(getActivity(), czgBeans);
                        recyclerView.setAdapter(newCzgAdapter);
                    } else {
                        refresh.setEnableLoadMore(false);
                        recyclerView.setVisibility(View.GONE);
                    }
                } else {
                    curposition = j - 1;
                    DialogHomeUtil.show(getActivity());
                    keyword = tab.getText().toString();
                    page = 1;
                    x = 1;
                    try {
                        if (chaozhigouTypes.getJSONObject(curposition).has("sub")) {
                            String[] sub = chaozhigouTypes.getJSONObject(curposition).optString("sub").split("\\|");
                            if (sub.length > 0) {
                                llSub.setVisibility(View.VISIBLE);
                                switch (sub.length) {
                                    case 1:
                                        tvSub1.setText(sub[0]);
                                        tvSub2.setVisibility(View.GONE);
                                        tvSub3.setVisibility(View.GONE);
                                        tvSub4.setVisibility(View.GONE);
                                        break;
                                    case 2:
                                        tvSub1.setText(sub[0]);
                                        tvSub2.setText(sub[1]);
                                        tvSub3.setVisibility(View.GONE);
                                        tvSub4.setVisibility(View.GONE);
                                        break;
                                    case 3:
                                        tvSub1.setText(sub[0]);
                                        tvSub2.setText(sub[1]);
                                        tvSub3.setText(sub[2]);
                                        tvSub4.setVisibility(View.GONE);
                                        break;
                                    case 4:
                                        tvSub1.setText(sub[0]);
                                        tvSub2.setText(sub[1]);
                                        tvSub3.setText(sub[2]);
                                        tvSub4.setText(sub[3]);
                                        break;
                                }
                            }
                        }else {
                            llSub.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    tvSub1.setClickable(true);
                    tvSub2.setClickable(true);
                    tvSub3.setClickable(true);
                    tvSub4.setClickable(true);
                    tvSub1.setBackgroundResource(R.drawable.bg_sub);
                    tvSub1.setTextColor(getActivity().getResources().getColor(R.color.shop_color1));
                    tvSub2.setBackgroundResource(R.drawable.bg_sub);
                    tvSub2.setTextColor(getActivity().getResources().getColor(R.color.shop_color1));
                    tvSub3.setBackgroundResource(R.drawable.bg_sub);
                    tvSub3.setTextColor(getActivity().getResources().getColor(R.color.shop_color1));
                    tvSub4.setBackgroundResource(R.drawable.bg_sub);
                    tvSub4.setTextColor(getActivity().getResources().getColor(R.color.shop_color1));
                    initDataCzg(keyword);
                }
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
                                SharedPreferencesUtil.putSharedData(getActivity(), "homeContent", "homeContent", object.toString());
                                LoadHomeData(object);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        refreshLayout.setEnableRefresh(true);
                        zLoadingView.loadSuccess();
                        refreshLayout.finishRefresh();
                        DialogHomeUtil.dismiss(0);
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
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogHomeUtil.dismiss(0);
                        String homeContent = SharedPreferencesUtil.getSharedData(getActivity(), "homeContent", "homeContent");
                        try {
                            JSONObject object = new JSONObject(homeContent);
                            if (object.length() > 0) {
                                LoadHomeData(object);
                            } else {
                                if (zLoadingView != null) {
                                    zLoadingView.setVisibility(View.VISIBLE);
                                    zLoadingView.loadError();
                                }
                                refreshLayout.finishRefresh();
                                refreshLayout.setVisibility(View.GONE);
                                StringUtil.showToast(getActivity(), e.message);
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 加载首页数据
     *
     * @param object
     */
    private void LoadHomeData(JSONObject object) {
        try {
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

            Logg.json("分类", object.optString("chaozhigouTypes"));
            //分类
            if (object.has("chaozhigouTypes")) {
                if (showTime == 0) {
                    chaozhigouTypes = object.optJSONArray("chaozhigouTypes");
                    chaozhigouTypesBeans = JSON.parseArray(object.optString("chaozhigouTypes"), ChaozhigouTypesBean.class);
                    showTime++;
                    typeGridAdapter = new TypeGridAdapter(getActivity(), chaozhigouTypesBeans);
                    typeGrid.setAdapter(typeGridAdapter);
                    typeGrid.setOnItemClickListener(onItemClickListener);
                    for (int i = 0; i < chaozhigouTypes.length(); i++) {
                        Map<String, String> map1 = new HashMap<>();
                        String keyword = chaozhigouTypes.getJSONObject(i).optString("name");
                        if (i == 0) {
                            tablayout.addTab(tablayout.newTab().setText("超值购"));
                        }
                        tablayout.addTab(tablayout.newTab().setText(keyword));
                    }
                }
            }
            //banner
            if (object.has("banner")) {
                JSONArray bannerarray = object.optJSONArray("banner");
                if (bannerarray != null && bannerarray.length() > 0) {
                    HomeLoadUtil.loadbanner(getActivity(), banner, bannerarray);
                }
            }
            //tag
            if (object.has("tag")) {
                List<TagBean> tagBeans = JSON.parseArray(object.optString("tag"), TagBean.class);
                JSONArray tag = object.optJSONArray("tag");
                if (tagBeans != null && tagBeans.size() > 0) {
                    tagList.setVisibility(View.VISIBLE);
                    MyHomeTagAdapter myHomeTagAdapter = new MyHomeTagAdapter(getActivity(), tagBeans, tag);
                    tagList.setAdapter(myHomeTagAdapter);
                }
            }
            Logg.json("0元购" + object.optString("zerobuy"));
            //0元购
            if (object.has("zerobuy")) {
                List<ZeroBuyBean> zeroBuyBeans = JSON.parseArray(object.optString("zerobuy"), ZeroBuyBean.class);
                zerobuyRecyclerview.setVisibility(View.VISIBLE);
                zerobuyRecyclerview.setHasFixedSize(true);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(linearLayoutManager.HORIZONTAL);
                zerobuyRecyclerview.setLayoutManager(linearLayoutManager);
                if (zeroBuyBeans != null && zeroBuyBeans.size() > 0) {
                    llZerobuy.setVisibility(View.VISIBLE);
                    viewZerobuy.setVisibility(View.VISIBLE);
                    ZeroBuyHomeAdapter homeGridAdapter = new ZeroBuyHomeAdapter(getActivity(), zeroBuyBeans);
                    zerobuyRecyclerview.setAdapter(homeGridAdapter);
                } else {
                    llZerobuy.setVisibility(View.GONE);
                    viewZerobuy.setVisibility(View.GONE);
                    zerobuyRecyclerview.setVisibility(View.GONE);
                }
            } else {
                llZerobuy.setVisibility(View.GONE);
                viewZerobuy.setVisibility(View.GONE);
                zerobuyRecyclerview.setVisibility(View.GONE);
            }
            //今日秒杀
            if (object.has("miaoshatime")) {
                mtime.addsum(object.optString("miaoshatime"), "#FFFFFFFF");
                mtime.start();
            }
            if (object.has("miaosha")) {
                List<MiaoShaBean> miaoShaBeans = JSON.parseArray(object.optString("miaosha"), MiaoShaBean.class);
                hotRecyclerview.setVisibility(View.VISIBLE);
                hotRecyclerview.setHasFixedSize(true);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(linearLayoutManager.HORIZONTAL);
                hotRecyclerview.setLayoutManager(linearLayoutManager);
                if (miaoShaBeans != null && miaoShaBeans.size() > 0) {
                    llMiaosha.setVisibility(View.VISIBLE);
                    viewMiaosha.setVisibility(View.VISIBLE);
                    HomeGridAdapter homeGridAdapter = new HomeGridAdapter(getActivity(), miaoShaBeans);
                    hotRecyclerview.setAdapter(homeGridAdapter);
                    homeGridAdapter.setLogInterface(MainActivity.this);
                } else {
                    llMiaosha.setVisibility(View.GONE);
                    viewMiaosha.setVisibility(View.GONE);
                    hotRecyclerview.setVisibility(View.GONE);
                }
            } else {
                llMiaosha.setVisibility(View.GONE);
                viewMiaosha.setVisibility(View.GONE);
                hotRecyclerview.setVisibility(View.GONE);
            }
            //超值拼团
            if (object.has("pintuan")) {
                List<PinTuanBean> pinTuanBeans = JSON.parseArray(object.optString("pintuan"), PinTuanBean.class);
                pingtuanRecyclerview.setVisibility(View.VISIBLE);
                pingtuanRecyclerview.setHasFixedSize(true);
                LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity());
                linearLayoutManager1.setOrientation(linearLayoutManager1.HORIZONTAL);
                pingtuanRecyclerview.setLayoutManager(linearLayoutManager1);
                if (pinTuanBeans != null && pinTuanBeans.size() > 0) {
                    llPingtuan.setVisibility(View.VISIBLE);
                    viewPingtuan.setVisibility(View.VISIBLE);
                    HomeGridPinTuanAdapter homeGridPinTuanAdapter = new HomeGridPinTuanAdapter(getActivity(), pinTuanBeans);
                    pingtuanRecyclerview.setAdapter(homeGridPinTuanAdapter);
                    homeGridPinTuanAdapter.setLogPinTuanInterface(MainActivity.this);
                } else {
                    llPingtuan.setVisibility(View.GONE);
                    viewPingtuan.setVisibility(View.GONE);
                    pingtuanRecyclerview.setVisibility(View.GONE);
                }
            } else {
                llPingtuan.setVisibility(View.GONE);
                viewPingtuan.setVisibility(View.GONE);
                pingtuanRecyclerview.setVisibility(View.GONE);
            }
            //超高赚
            if (object.has("chaojifan")) {
                List<ChaoJiFanBean> chaoJiFanBeans = JSON.parseArray(object.optString("chaojifan"), ChaoJiFanBean.class);
                czuanRecyclerview.setVisibility(View.VISIBLE);
                czuanRecyclerview.setHasFixedSize(true);
                LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity());
                linearLayoutManager2.setOrientation(linearLayoutManager2.HORIZONTAL);
                czuanRecyclerview.setLayoutManager(linearLayoutManager2);
                if (chaoJiFanBeans != null && chaoJiFanBeans.size() > 0) {
                    llCzuan.setVisibility(View.VISIBLE);
                    viewCzuan.setVisibility(View.VISIBLE);
                    czuanRecyclerview.setAdapter(new HomeGridChaojiFanAdapter(getActivity(), chaoJiFanBeans));
                } else {
                    llCzuan.setVisibility(View.GONE);
                    viewCzuan.setVisibility(View.GONE);
                    czuanRecyclerview.setVisibility(View.GONE);
                }
            } else {
                llCzuan.setVisibility(View.GONE);
                viewCzuan.setVisibility(View.GONE);
                czuanRecyclerview.setVisibility(View.GONE);
            }
            Logg.json("=====>>>>>>>>>>", object.optString("hotlist"));
            //每日好货
            if (object.has("hotlist")) {
                List<ShopDianpuBean> shopDianpuBeans = JSON.parseArray(object.optString("hotlist"), ShopDianpuBean.class);
                everydayGoodsshopRecyclerview.setVisibility(View.VISIBLE);
                everydayGoodsshopRecyclerview.setHasFixedSize(true);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(linearLayoutManager.HORIZONTAL);
                if (shopDianpuBeans != null && shopDianpuBeans.size() > 0) {
                    llEverydayGoodsshop.setVisibility(View.VISIBLE);
                    viewEverydayGoodsshop.setVisibility(View.VISIBLE);
                    everydayGoodsshopRecyclerview.setLayoutManager(linearLayoutManager);
                    everydayGoodsshopRecyclerview.setAdapter(new DianPuHoHotGridAdapter(getActivity(), shopDianpuBeans));
                } else {
                    llGooddianpu.setVisibility(View.GONE);
                    viewEverydayGoodsshop.setVisibility(View.GONE);
                    everydayGoodsshopRecyclerview.setVisibility(View.GONE);
                }
            } else {
                llEverydayGoodsshop.setVisibility(View.GONE);
                viewEverydayGoodsshop.setVisibility(View.GONE);
                everydayGoodsshopRecyclerview.setVisibility(View.GONE);
            }
            //精选好店
            if (object.has("activity")) {
                List<PinpaiBean> pinpaiBeans = JSON.parseArray(object.optString("activity"), PinpaiBean.class);
                gooddianpuRecyclerview.setVisibility(View.VISIBLE);
                gooddianpuRecyclerview.setHasFixedSize(true);
                LinearLayoutManager linearLayoutManagePp = new LinearLayoutManager(getActivity());
                linearLayoutManagePp.setOrientation(linearLayoutManagePp.HORIZONTAL);
                if (pinpaiBeans != null && pinpaiBeans.size() > 0) {
                    llGooddianpu.setVisibility(View.VISIBLE);
                    viewZerobuy.setVisibility(View.VISIBLE);
                    gooddianpuRecyclerview.setLayoutManager(linearLayoutManagePp);
                    gooddianpuRecyclerview.setAdapter(new DianPuHomePinpaiGridAdapter(getActivity(), pinpaiBeans));
                } else {
                    llGooddianpu.setVisibility(View.GONE);
                    viewZerobuy.setVisibility(View.GONE);
                    gooddianpuRecyclerview.setVisibility(View.GONE);
                }
            } else {
                llGooddianpu.setVisibility(View.GONE);
                viewZerobuy.setVisibility(View.GONE);
                gooddianpuRecyclerview.setVisibility(View.GONE);
            }
            Logg.json(object.optString("zerobuynew"));
            //新0元购
            if (object.has("zerobuynew")) {
                List<ZeroBuyBean> zeroBuyBeans = JSON.parseArray(object.optString("zerobuynew"), ZeroBuyBean.class);
                zerobuyNewRecyclerview.setVisibility(View.VISIBLE);
                zerobuyNewRecyclerview.setHasFixedSize(true);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(linearLayoutManager.HORIZONTAL);
                zerobuyNewRecyclerview.setLayoutManager(linearLayoutManager);
                if (zeroBuyBeans != null && zeroBuyBeans.size() > 0) {
                    llZerobuyNew.setVisibility(View.VISIBLE);
                    viewZerobuyNew.setVisibility(View.VISIBLE);
                    ZeroBuyHomeAdapter homeGridAdapter = new ZeroBuyHomeAdapter(getActivity(), zeroBuyBeans);
                    zerobuyNewRecyclerview.setAdapter(homeGridAdapter);
                } else {
                    llZerobuyNew.setVisibility(View.GONE);
                    viewZerobuyNew.setVisibility(View.GONE);
                    zerobuyNewRecyclerview.setVisibility(View.GONE);
                }
            } else {
                llZerobuyNew.setVisibility(View.GONE);
                viewZerobuyNew.setVisibility(View.GONE);
                zerobuyNewRecyclerview.setVisibility(View.GONE);
            }
            llHuodong.setVisibility(View.VISIBLE);
            //发镖滚动信息
            if (object.has("fabiao")) {
                JSONArray fabiao = object.optJSONArray("fabiao");
                if (fabiao != null && fabiao.length() > 0) {
                    HomeLoadUtil.loadViewflipper(getActivity(), mviewflipper, fabiao);
                }
            }

            //通栏广告
            if (object.has("tonglanguanggaoandroid")) {
                guanggaoLayout.setVisibility(View.VISIBLE);
                JSONObject guanggaobanner = object.optJSONObject("tonglanguanggaoandroid");
                DisplayMetrics dm = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                int width = dm.widthPixels;
                int height = dm.heightPixels;
                ViewGroup.LayoutParams lp = bannerGuanggao.getLayoutParams();
                lp.width = width;
                lp.height = width * 1 / 4;
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
            img6.post(new Runnable() {
                //                    @Override
                public void run() {
                    //首页引导页只显示一次
                    String isFirstResultUse = SharedPreferencesUtil.getSharedData(getActivity(), "isFirstHomeUse", "isFirstHomeUserUse");
                    if (TextUtils.isEmpty(isFirstResultUse)) {
                        isFirstResultUse = "yes";
                    }
                    if (isFirstResultUse.equals("yes")) {
                        HomeLoadUtil.showGuideViewZiying(getActivity(), img3, img4, img6);
                    }
                }
            });

            chaozhigou = object.optString("chaozhigou");
            //第一页超值购数据
            czgBeans = JSON.parseArray(chaozhigou, NewHomeCzgBean.class);
            if (czgBeans != null && czgBeans.size() > 0) {
                if (typeGridAdapter != null) {
                    typeGridAdapter.setSeclection(curposition);
                    typeGridAdapter.notifyDataSetChanged();
                }
                refresh.setEnableLoadMore(true);
                recyclerView.setVisibility(View.VISIBLE);
                newCzgAdapter = new NewCzgAdapter(getActivity(), czgBeans);
                recyclerView.setAdapter(newCzgAdapter);
            } else {
                refresh.setEnableLoadMore(false);
                recyclerView.setVisibility(View.GONE);
            }

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 超值购数据请求
     *
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
                                            if (typeGridAdapter != null) {
                                                typeGridAdapter.setSeclection(curposition);
                                                typeGridAdapter.notifyDataSetChanged();
                                            }
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
                                            newCzgAdapter.notifyData(czgBeans);
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
                    }

                    @Override
                    protected void showDialog() {
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
//                        refreshLayout.setVisibility(View.GONE);
//                        zLoadingView.setVisibility(View.VISIBLE);
//                        zLoadingView.loadError();
                        refresh.finishLoadMore();
                        refresh.finishRefresh();
                        DialogHomeUtil.dismiss(0);
//                        recyclerView.setVisibility(View.GONE);
                        StringUtil.showToast(getActivity(), e.message);
                    }
                });
    }

    /**
     * 查询优惠券
     *
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
                        Intent intent = new Intent(context, UserLoginNewActivity.class);
                        startActivityForResult(intent, 3);
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
            LinearLayout llYouhui = updataDialog.findViewById(R.id.ll_youhui);
            llYouhui.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updataDialog.dismiss();
                    NewConstants.showdialogFlg = "1";
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
            tv_update_gengxin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updataDialog.dismiss();
                    NewConstants.showdialogFlg = "1";
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
            XTabLayout.Tab tabAt = tablayout.getTabAt(position + 1);
            tabAt.select();
            showGlobalMenu();
        }
    };


    /**
     * 点击事件
     *
     * @param view
     */
    @OnClick({R.id.msearch, R.id.msort, R.id.type_image, R.id.ll_shouqi, R.id.image_puba, R.id.to_top_btn, R.id.ll_miaosha, R.id.ll_pingtuan, R.id.ll_czuan, R.id.ll_zerobuy, R.id.ll_zerobuy_new,
            R.id.ll_everyday_goodsshop, R.id.ll_gooddianpu,R.id.tv_sub1, R.id.tv_sub2, R.id.tv_sub3, R.id.tv_sub4})
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
            case R.id.ll_miaosha:
                intent = new Intent(getActivity(), ChaoZhiGouTypesActivity.class);
                intent.putExtra("type", "4");
                startActivity(intent);
                break;
            case R.id.ll_pingtuan:
                intent = new Intent(getActivity(), ChaoZhiGouTypesActivity.class);
                intent.putExtra("type", "3");
                startActivity(intent);
                break;
            case R.id.ll_czuan:
                intent = new Intent(getActivity(), ChaoZhiGouTypesActivity.class);
                intent.putExtra("type", "2");
                startActivity(intent);
                break;
            case R.id.ll_zerobuy:
                intent = new Intent(getActivity(), ZeroBuyShopActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_zerobuy_new:
                intent = new Intent(getActivity(), ZiYingZeroBuyShopActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_everyday_goodsshop:
                intent = new Intent(getActivity(), NewDianpuHomeActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_gooddianpu:
                intent = new Intent(getActivity(), NewDianpuHomeActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_sub1:
                DialogHomeUtil.show(getActivity());
                tvSub1.setClickable(false);
                tvSub2.setClickable(true);
                tvSub3.setClickable(true);
                tvSub4.setClickable(true);
                tvSub1.setBackgroundResource(R.drawable.bg_sub2);
                tvSub1.setTextColor(getActivity().getResources().getColor(R.color.color_line_top));
                tvSub2.setBackgroundResource(R.drawable.bg_sub);
                tvSub2.setTextColor(getActivity().getResources().getColor(R.color.shop_color1));
                tvSub3.setBackgroundResource(R.drawable.bg_sub);
                tvSub3.setTextColor(getActivity().getResources().getColor(R.color.shop_color1));
                tvSub4.setBackgroundResource(R.drawable.bg_sub);
                tvSub4.setTextColor(getActivity().getResources().getColor(R.color.shop_color1));
                initDataCzg(keyword+""+tvSub1.getText().toString());
                break;
            case R.id.tv_sub2:
                DialogHomeUtil.show(getActivity());
                tvSub1.setClickable(true);
                tvSub2.setClickable(false);
                tvSub3.setClickable(true);
                tvSub4.setClickable(true);
                tvSub1.setBackgroundResource(R.drawable.bg_sub);
                tvSub1.setTextColor(getActivity().getResources().getColor(R.color.shop_color1));
                tvSub2.setBackgroundResource(R.drawable.bg_sub2);
                tvSub2.setTextColor(getActivity().getResources().getColor(R.color.color_line_top));
                tvSub3.setBackgroundResource(R.drawable.bg_sub);
                tvSub3.setTextColor(getActivity().getResources().getColor(R.color.shop_color1));
                tvSub4.setBackgroundResource(R.drawable.bg_sub);
                tvSub4.setTextColor(getActivity().getResources().getColor(R.color.shop_color1));
                initDataCzg(keyword+""+tvSub2.getText().toString());
                break;
            case R.id.tv_sub3:
                DialogHomeUtil.show(getActivity());
                tvSub1.setClickable(true);
                tvSub2.setClickable(true);
                tvSub3.setClickable(false);
                tvSub4.setClickable(true);
                tvSub1.setBackgroundResource(R.drawable.bg_sub);
                tvSub1.setTextColor(getActivity().getResources().getColor(R.color.shop_color1));
                tvSub2.setBackgroundResource(R.drawable.bg_sub);
                tvSub2.setTextColor(getActivity().getResources().getColor(R.color.shop_color1));
                tvSub3.setBackgroundResource(R.drawable.bg_sub2);
                tvSub3.setTextColor(getActivity().getResources().getColor(R.color.color_line_top));
                tvSub4.setBackgroundResource(R.drawable.bg_sub);
                tvSub4.setTextColor(getActivity().getResources().getColor(R.color.shop_color1));
                initDataCzg(keyword+""+tvSub3.getText().toString());
                break;
            case R.id.tv_sub4:
                DialogHomeUtil.show(getActivity());
                tvSub1.setClickable(true);
                tvSub2.setClickable(true);
                tvSub3.setClickable(true);
                tvSub4.setClickable(false);
                tvSub4.setBackgroundResource(R.drawable.bg_sub2);
                tvSub4.setTextColor(getActivity().getResources().getColor(R.color.color_line_top));
                tvSub2.setBackgroundResource(R.drawable.bg_sub);
                tvSub2.setTextColor(getActivity().getResources().getColor(R.color.shop_color1));
                tvSub3.setBackgroundResource(R.drawable.bg_sub);
                tvSub3.setTextColor(getActivity().getResources().getColor(R.color.shop_color1));
                tvSub1.setBackgroundResource(R.drawable.bg_sub);
                tvSub1.setTextColor(getActivity().getResources().getColor(R.color.shop_color1));
                initDataCzg(keyword+""+tvSub4.getText().toString());
                break;
        }
    }

    /**
     * 异常重试
     */
    @Override
    public void doRequestData() {
        DialogHomeUtil.show(getActivity());
        zLoadingView.setVisibility(View.GONE);
        queryAppIndexInfo();
        x = 1;
        page = 1;
        initDataCzg(keyword);
    }

    /**
     * 登录回调
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent;
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        if (userID != null && !userID.equals("")) {
            switch (requestCode) {
                case 1:
                    intent = new Intent(getActivity(), MesageCenterActivity.class);
                    intent.putExtra("type", "0");
                    startActivity(intent);
                    break;
                case 2:
                    if ("beibei".equals(domain1)
                            || "jd".equals(domain1) || "taobao".equals(domain1)
                            || "tmall".equals(domain1) || "suning".equals(domain1)) {
                        clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        clipboardManager.setPrimaryClip(ClipData.newPlainText(null, ""));
                        intent = new Intent(getActivity(), IntentActivity.class);
                        if (url1 != null) {
                            intent.putExtra("url", url1);
                        }
                        if (title1 != null) {
                            intent.putExtra("title", title1);
                        }
                        if (domain1 != null) {
                            intent.putExtra("domain", domain1);
                        }
                        intent.putExtra("isczg", "0");
                        if (bprice1 != null) {
                            intent.putExtra("bprice", bprice1);
                        }
                        if (quan1 != null && !quan1.equals("0")) {
                            intent.putExtra("quan", quan1);
                        }
                        if (zuan1 != null) {
                            intent.putExtra("zuan", zuan1);
                        }
                        intent.putExtra("type", "miaosha");
                    } else {
                        intent = new Intent(getActivity(), WebViewActivity.class);
                        intent.putExtra("url", url1);
                        intent.putExtra("title", title1);
                    }
                    startActivity(intent);
                    break;
                case 3:
                    intent = new Intent(getActivity(), BrowseActivity.class);
                    updataDialog.dismiss();
                    cancelCheck = false;
                    startActivity(intent);
                    break;
            }
        }
    }

    @Override
    public void IntentLog(String url, String title, String domain, String isczg, String bprice, String quan, String zuan, String type) {
        url1 = url;
        title1 = title;
        domain1 = domain;
        isczg1 = isczg;
        bprice1 = bprice;
        quan1 = quan;
        type1 = type;
        zuan1 = zuan;
        Intent intent = new Intent(getActivity(), UserLoginNewActivity.class);
        startActivityForResult(intent, 2);
    }

    @Override
    public void IntentPinTuanLog(String url, String title, String domain, String isczg, String bprice, String quan, String zuan, String type) {
        url1 = url;
        title1 = title;
        domain1 = domain;
        isczg1 = isczg;
        bprice1 = bprice;
        quan1 = quan;
        type1 = type;
        zuan1 = zuan;
        Intent intent = new Intent(getActivity(), UserLoginNewActivity.class);
        startActivityForResult(intent, 2);
    }

    /**
     * 客服
     * @param context
     */
    public static void consultService(final Context context) {
        Intent intent = new KSIntentBuilder(context).build();
        context.startActivity(intent);
    }
}
