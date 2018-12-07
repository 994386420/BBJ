package com.bbk.shopcar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.DianPuTypesBean;
import com.bbk.Bean.DianpuBean;
import com.bbk.Bean.ShopDianpuBean;
import com.bbk.activity.BaseActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.UserLoginNewActivity;
import com.bbk.adapter.DianPuGridAdapter;
import com.bbk.adapter.DianpuTypesAdapter;
import com.bbk.model.DianpuSearchActivity;
import com.bbk.model.MainActivity;
import com.bbk.model.tablayout.XTabLayout;
import com.bbk.resource.NewConstants;
import com.bbk.shopcar.Utils.ShopDialog;
import com.bbk.shopcar.presenter.DianpuPresenter;
import com.bbk.shopcar.view.DianpuListView;
import com.bbk.shopcar.view.DianpuView;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.HomeLoadUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.CommonLoadingView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 店铺
 */
public class NewDianpuActivity extends BaseActivity implements DianpuTypesAdapter.TypeInterface, CommonLoadingView.LoadingHandler {
    String dianpuid;
    @BindView(R.id.banner)
    ImageView banner;
    @BindView(R.id.img_dianpu_logo)
    ImageView imgDianpuLogo;
    @BindView(R.id.title_back_btn)
    ImageButton titleBackBtn;
    @BindView(R.id.tv_dianpu)
    TextView tvDianpu;
    @BindView(R.id.tv_sale)
    TextView tvSale;
    @BindView(R.id.tablayout)
    XTabLayout tablayout;
    @BindView(R.id.mrecycler)
    RecyclerView mrecycler;
    @BindView(R.id.to_top_btn)
    ImageButton toTopBtn;
    @BindView(R.id.lin)
    LinearLayout lin;
    @BindView(R.id.tv_dianpu_top)
    TextView tvDianpuTop;
    @BindView(R.id.ll_back1)
    LinearLayout llback1;
    @BindView(R.id.tv_choose)
    TextView tvChoose;
    @BindView(R.id.ll_mall_choose)
    LinearLayout llMallChoose;
    @BindView(R.id.progress)
    CommonLoadingView progress;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.ll_kefu)
    LinearLayout llKefu;
    @BindView(R.id.img_car)
    ImageButton imgCar;
    @BindView(R.id.toolbaretail)
    Toolbar toolbaretail;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
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
    @BindView(R.id.img_more_black)
    ImageView imgMoreBlack;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    private int page = 1, x = 1;
    DianPuGridAdapter dianPuGridAdapter;
    private String sortway = "1", keywordType = "";
    private ShopDialog shopDialog;
    private DianpuBean dianpuBean;
    private DianpuTypesAdapter dianpuTypesAdapter;
    private List<DianPuTypesBean> dianPuTypesBeans;
    private DianpuPresenter dianpuPresenter = new DianpuPresenter(this);
    private String LogFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.new_dianpu_layout);
        View topView = findViewById(R.id.lin);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        ButterKnife.bind(this);
        dianpuPresenter.attachView(dianpuView);
        dianpuPresenter.attachListView(dianpuListView);
        progress.setLoadingHandler(this);
        NewConstants.postion = 1314;
        tablayout.addTab(tablayout.newTab().setText("综合"));
        tablayout.addTab(tablayout.newTab().setText("销量"));
        tablayout.addTab(tablayout.newTab().setText("新品"));
        tablayout.addTab(tablayout.newTab().setText("价格"));
        tablayout.setTabMode(TabLayout.MODE_FIXED);
        refreshAndloda();
        setToolBar();
        mrecycler.setLayoutManager(new GridLayoutManager(NewDianpuActivity.this, 2));
        mrecycler.setHasFixedSize(true);
        tablayout.setxTabDisplayNum(4);
        tablayout.setOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                int j = tab.getPosition();
                if (j == 0) {
                    sortway = "1";
                } else if (j == 1) {
                    sortway = "2";
                } else if (j == 2) {
                    sortway = "3";
                } else if (j == 3) {
                    sortway = "4";
                }
                x = 1;
                page = 1;
                dianpuPresenter.queryProductListByKeyword(dianpuid, sortway, keywordType, refresh, refreshLayout, progress, mrecycler, page);
            }

            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(XTabLayout.Tab tab) {

            }
        });
        if (getIntent().getStringExtra("dianpuid") != null) {
            dianpuid = getIntent().getStringExtra("dianpuid");
            dianpuPresenter.queryDianpuMainInfo(dianpuid, refreshLayout);
            dianpuPresenter.queryProductListByKeyword(dianpuid, sortway, keywordType, refresh, refreshLayout, progress, mrecycler, page);
        }
    }

    /**
     * 店铺主页
     */
    private DianpuView dianpuView = new DianpuView() {

        @Override
        public void onSuccess(DianpuBean dianpubean) {
            dianpuBean = dianpubean;
            Glide.with(NewDianpuActivity.this)
                    .load(dianpuBean.getBannerimg())
                    .priority(Priority.HIGH)
                    .into(banner);
            Glide.with(NewDianpuActivity.this)
                    .load(dianpuBean.getLogoimg())
                    .priority(Priority.HIGH)
                    .placeholder(R.mipmap.zw_img_300)
                    .into(imgDianpuLogo);
            tvDianpu.setText(dianpuBean.getDianpu());
            tvDianpuTop.setText(dianpuBean.getDianpu());
            if (dianpuBean.getTotalSale() != null) {
                tvSale.setText("已销:" + dianpuBean.getTotalSale() + "件");
            } else {
                tvSale.setVisibility(View.GONE);
            }
        }

        @Override
        public void onError(String result) {
            StringUtil.showToast(NewDianpuActivity.this, result);
        }
    };

    /**
     * 搜索商品结果
     */
    private DianpuListView dianpuListView = new DianpuListView() {

        @Override
        public void onSuccess(List<ShopDianpuBean> shopDianpuBeans) {
            if (x == 1) {
                if (shopDianpuBeans != null && shopDianpuBeans.size() > 0) {
                    refresh.setEnableLoadMore(true);
                    mrecycler.setVisibility(View.VISIBLE);
                    dianPuGridAdapter = new DianPuGridAdapter(NewDianpuActivity.this, shopDianpuBeans);
                    mrecycler.setAdapter(dianPuGridAdapter);
                    progress.loadSuccess();
                } else {
                    mrecycler.setVisibility(View.GONE);
                    progress.setVisibility(View.VISIBLE);
                    progress.loadSuccess(true);
                    refresh.setEnableLoadMore(false);
                }
            } else {
                if (shopDianpuBeans != null && shopDianpuBeans.size() > 0) {
                    dianPuGridAdapter.notifyData(shopDianpuBeans);
                } else {
                    StringUtil.showToast(NewDianpuActivity.this, "没有更多了");
                    refresh.setEnableLoadMore(false);
                }
            }
        }

        @Override
        public void onError(String result) {
            StringUtil.showToast(NewDianpuActivity.this, result);
        }
    };

    /**
     * 初始化刷新
     */
    private void refreshAndloda() {
        refreshLayout.setEnableLoadMore(false);
        refresh.setEnableRefresh(false);
        refresh.setEnableAutoLoadMore(true);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayoutt) {
                x = 1;
                page = 1;
                dianpuPresenter.queryProductListByKeyword(dianpuid, sortway, keywordType, refresh, refreshLayout, progress, mrecycler, page);
                dianpuPresenter.queryDianpuMainInfo(dianpuid, refreshLayout);
            }
        });
        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayoutt) {
                x = 2;
                page++;
                dianpuPresenter.queryProductListByKeyword(dianpuid, sortway, keywordType, refresh, refreshLayout, progress, mrecycler, page);
            }
        });

    }

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
                    tvDianpuTop.setTextColor(Color.WHITE);
                    tvDianpuTop.setVisibility(View.INVISIBLE);
                    llback1.setVisibility(View.INVISIBLE);
                    lin.setVisibility(View.GONE);
                    toolbaretail.setVisibility(View.GONE);
                } else if (Offset > 0 && Offset < appBarLayout.getTotalScrollRange() / 2) {
                    lin.setVisibility(View.VISIBLE);
                    toolbaretail.setVisibility(View.VISIBLE);
                    toolbaretail.setTitle("");
                    float scale = (float) Offset / appBarLayout.getTotalScrollRange();
                    float alpha = (255 * scale);
                    toolbaretail.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                    tvDianpuTop.setTextColor(Color.argb((int) alpha, 0, 0, 0));
                    tvDianpuTop.setVisibility(View.VISIBLE);
                    llback1.setVisibility(View.VISIBLE);
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

    protected void onDestroy() {
        super.onDestroy();
        DialogSingleUtil.dismiss(0);
    }


    /**
     * 点击事件
     *
     * @param view
     */
    @OnClick({R.id.title_back_btn, R.id.ll_mall_choose, R.id.ll_back1, R.id.ll_back, R.id.ll_kefu, R.id.img_car, R.id.to_top_btn, R.id.img_more_black,R.id.ll_search})
    public void onViewClicked(View view) {
        Intent intent;
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        switch (view.getId()) {
            case R.id.title_back_btn:
                finish();
                break;
            case R.id.ll_mall_choose:
                showBaozhangeDialog(this);
                break;
            case R.id.ll_back1:
                finish();
                break;
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_kefu:
                if (TextUtils.isEmpty(userID)) {
                    intent = new Intent(this, UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
//                    HomeLoadUtil.startChat(NewDianpuActivity.this);
                    MainActivity.consultService(this);
                }
                break;
            case R.id.img_car:
                if (TextUtils.isEmpty(userID)) {
                    LogFlag = "2";
                    intent = new Intent(this, UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(this, CarActivity.class);
                    intent.putExtra("ziying","yes");
                    startActivity(intent);
                }
                break;
            case R.id.to_top_btn:
                //滑动到顶部
                mrecycler.scrollToPosition(0);
                CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) appBar.getLayoutParams()).getBehavior();
                if (behavior instanceof AppBarLayout.Behavior) {
                    AppBarLayout.Behavior appBarLayoutBehavior = (AppBarLayout.Behavior) behavior;
                    int topAndBottomOffset = appBarLayoutBehavior.getTopAndBottomOffset();
                    if (topAndBottomOffset != 0) {
                        appBarLayoutBehavior.setTopAndBottomOffset(0);
                        llBtnBottom.setVisibility(View.GONE);
                        lin.setVisibility(View.GONE);
                        toolbaretail.setVisibility(View.GONE);
                    }
                }
                break;
            case R.id.img_more_black:
                if (TextUtils.isEmpty(userID)) {
                    LogFlag = "1";
                    intent = new Intent(this, UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    HomeLoadUtil.showItemPop(this, imgMoreBlack);
                }
                break;
            case R.id.ll_search:
                intent = new Intent(this, DianpuSearchActivity.class);
                intent.putExtra("dianpuid", dianpuid);
                intent.putExtra("producttype", "");
                intent.putExtra("plevel", "3");
                intent.putExtra("keyword", "");
                startActivity(intent);
                break;
        }
    }

    /**
     * 登陆回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Intent intent;
        if (userID != null && !userID.equals("")) {
            switch (requestCode) {
                case 1:
                    switch (LogFlag) {
                        case "1":
                            HomeLoadUtil.showItemPop(this, imgMoreBlack);
                            break;
                        case "2":
                            intent = new Intent(this, CarActivity.class);
                            intent.putExtra("ziying","yes");
                            startActivity(intent);
                            break;
                    }
                    break;
            }
        }
    }

    /**
     * 商品分类
     *
     * @param context
     */
    public void showBaozhangeDialog(final Context context) {
        if (shopDialog == null || !shopDialog.isShowing()) {
            //初始化弹窗 布局 点击事件的id
            shopDialog = new ShopDialog(context, R.layout.shop_dialog_layout,
                    new int[]{R.id.tv_ok});
            shopDialog.show();
            shopDialog.setCanceledOnTouchOutside(true);
            TextView tv_update_gengxin = shopDialog.findViewById(R.id.tv_ok);
            TextView tv_title = shopDialog.findViewById(R.id.tv_title);
            tv_title.setText("选择分类");
            RecyclerView recyclerView = shopDialog.findViewById(R.id.recyclerview_shop_dialog);
            LinearLayout linearLayout = shopDialog.findViewById(R.id.ll_baozhang);
            linearLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            if (dianpuBean != null) {
                if (dianpuBean.getTypes() != null) {
                    dianPuTypesBeans = JSON.parseArray(dianpuBean.getTypes(), DianPuTypesBean.class);
                    dianpuTypesAdapter = new DianpuTypesAdapter(context, dianPuTypesBeans);
                    dianpuTypesAdapter.setTypeInterface(NewDianpuActivity.this);
                    recyclerView.setAdapter(dianpuTypesAdapter);
                }
            }
            ImageView img_close = shopDialog.findViewById(R.id.img_close);
            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shopDialog.dismiss();
                }
            });
            tv_update_gengxin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shopDialog.dismiss();
                }
            });
        }
    }

    @Override
    public void doChoose(String keyword, int postion) {
        NewConstants.postion = postion;
        dianpuTypesAdapter.notifyDataSetChanged();
        tvChoose.setText(keyword);
        shopDialog.dismiss();
        keywordType = keyword;
        x = 1;
        page = 1;
        dianpuPresenter.queryProductListByKeyword(dianpuid, sortway, keywordType, refresh, refreshLayout, progress, mrecycler, page);
    }

//    private void startChat() {
//        //
//        KFAPIs.startChat(this,
//                "bbjkfxz", // 1. 客服工作组ID(请务必保证大小写一致)，请在管理后台分配
//                "比比鲸客服", // 2. 会话界面标题，可自定义
//                null, // 3. 附加信息，在成功对接客服之后，会自动将此信息发送给客服;
//                // 如果不想发送此信息，可以将此信息设置为""或者null
//                true, // 4. 是否显示自定义菜单,如果设置为显示,请务必首先在管理后台设置自定义菜单,
//                // 请务必至少分配三个且只分配三个自定义菜单,多于三个的暂时将不予显示
//                // 显示:true, 不显示:false
//                5, // 5. 默认显示消息数量
//                //修改SDK自带的头像有两种方式，1.直接替换appkefu_message_toitem和appkefu_message_fromitem.xml里面的头像，2.传递网络图片自定义
//                "http://www.bibijing.com/images/zhanwei/logo.png",//6. 修改默认客服头像，如果不想修改默认头像，设置此参数为null
//                NewConstants.imgurl, //7. 修改默认用户头像, 如果不想修改默认头像，设置此参数为null
//                false, // 8. 默认机器人应答
//                false,  //9. 是否强制用户在关闭会话的时候 进行“满意度”评价， true:是， false:否
//                null);
//
//    }

    @Override
    public void doRequestData() {
        progress.setVisibility(View.GONE);
        dianpuPresenter.queryProductListByKeyword(dianpuid, sortway, keywordType, refresh, refreshLayout, progress, mrecycler, page);
    }

}
