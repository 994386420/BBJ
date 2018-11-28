package com.bbk.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.DianPuTypesBean;
import com.bbk.Bean.DianpuBean;
import com.bbk.Bean.ShopDianpuBean;
import com.bbk.adapter.DianPuGridAdapter;
import com.bbk.adapter.DianpuTypesAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.model.MainActivity;
import com.bbk.resource.NewConstants;
import com.bbk.shopcar.CarActivity;
import com.bbk.shopcar.Utils.ShopDialog;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.HomeLoadUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.StringUtil;
import com.bbk.view.CommonLoadingView;
import com.bbk.view.MyScrollViewNew;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.logg.Logg;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 店铺
 */
public class DianpuActivity extends BaseActivity implements MyScrollViewNew.ScrollViewListener, DianpuTypesAdapter.TypeInterface {
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
    TabLayout tablayout;
    @BindView(R.id.mrecycler)
    RecyclerView mrecycler;
    @BindView(R.id.refresh_root)
    SmartRefreshLayout refreshRoot;
    @BindView(R.id.to_top_btn)
    ImageButton toTopBtn;
    @BindView(R.id.lin)
    LinearLayout lin;
    @BindView(R.id.tablayout_top)
    TabLayout tablayoutTop;
    @BindView(R.id.scrollview)
    MyScrollViewNew scrollview;
    @BindView(R.id.ll_type)
    LinearLayout llType;
    @BindView(R.id.tv_dianpu_top)
    TextView tvDianpuTop;
    @BindView(R.id.title_back_btn1)
    ImageButton titleBackBtn1;
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
    private int page = 1, x = 1;
    DianPuGridAdapter dianPuGridAdapter;
    private int imageHeight, height;
    private String sortway = "1", keywordType = "";
    private ShopDialog shopDialog;
    DianpuBean dianpuBean;
    DianpuTypesAdapter dianpuTypesAdapter;
    List<DianPuTypesBean> dianPuTypesBeans;
    private String mKefuDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.dianpu_layout);
        View topView = findViewById(R.id.lin);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        ButterKnife.bind(this);
        initListeners();
        NewConstants.postion = 1314;
        tablayoutTop.addTab(tablayoutTop.newTab().setText("综合"));
        tablayoutTop.addTab(tablayoutTop.newTab().setText("销量"));
        tablayoutTop.addTab(tablayoutTop.newTab().setText("新品"));
        tablayoutTop.addTab(tablayoutTop.newTab().setText("价格"));
        tablayoutTop.setTabMode(TabLayout.MODE_FIXED);
        tablayout.addTab(tablayout.newTab().setText("综合"));
        tablayout.addTab(tablayout.newTab().setText("销量"));
        tablayout.addTab(tablayout.newTab().setText("新品"));
        tablayout.addTab(tablayout.newTab().setText("价格"));
        tablayout.setTabMode(TabLayout.MODE_FIXED);
        refreshAndloda();
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int j = tab.getPosition();
                if (j == 0) {
                    sortway = "1";
                    TabLayout.Tab tabAt = tablayoutTop.getTabAt(0);
                    tabAt.select();
                } else if (j == 1) {
                    sortway = "2";
                    TabLayout.Tab tabAt = tablayoutTop.getTabAt(1);
                    tabAt.select();
                } else if (j == 2) {
                    sortway = "3";
                    TabLayout.Tab tabAt = tablayoutTop.getTabAt(2);
                    tabAt.select();
                } else if (j == 3) {
                    sortway = "4";
                    TabLayout.Tab tabAt = tablayoutTop.getTabAt(3);
                    tabAt.select();
                }
                x = 1;
                page = 1;
                queryProductListByKeyword(dianpuid, sortway, keywordType);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tablayoutTop.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int j = tab.getPosition();
                if (j == 0) {
                    sortway = "1";
                    TabLayout.Tab tabAt = tablayout.getTabAt(0);
                    tabAt.select();
                } else if (j == 1) {
                    sortway = "2";
                    TabLayout.Tab tabAt = tablayout.getTabAt(1);
                    tabAt.select();
                } else if (j == 2) {
                    sortway = "3";
                    TabLayout.Tab tabAt = tablayout.getTabAt(2);
                    tabAt.select();
                } else if (j == 3) {
                    sortway = "4";
                    TabLayout.Tab tabAt = tablayout.getTabAt(3);
                    tabAt.select();
                }
                x = 1;
                page = 1;
                queryProductListByKeyword(dianpuid, sortway, keywordType);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        lin.setBackgroundResource(R.drawable.bg_home_yinying);
        refreshAndloda();
        if (getIntent().getStringExtra("dianpuid") != null) {
            dianpuid = getIntent().getStringExtra("dianpuid");
            queryDianpuMainInfo(dianpuid);
            queryProductListByKeyword(dianpuid, sortway, keywordType);
        }
    }

    private void refreshAndloda() {
        refreshRoot.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                x = 1;
                page = 1;
                queryProductListByKeyword(dianpuid, sortway, keywordType);
                queryDianpuMainInfo(dianpuid);
            }
        });
        refreshRoot.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                x = 2;
                page++;
                queryProductListByKeyword(dianpuid, sortway, keywordType);
            }
        });

    }

    /**
     * 查询
     */
    private void queryDianpuMainInfo(String dianpuid) {
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("dianpu", dianpuid);
        RetrofitClient.getInstance(this).createBaseApi().queryDianpuMainInfo(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            JSONObject object = new JSONObject(content);
                            if (jsonObject.optString("status").equals("1")) {
                                dianpuBean = JSON.parseObject(content, DianpuBean.class);
                                Glide.with(DianpuActivity.this)
                                        .load(dianpuBean.getBannerimg())
                                        .priority(Priority.HIGH)
                                        .into(banner);
                                Glide.with(DianpuActivity.this)
                                        .load(dianpuBean.getLogoimg())
                                        .priority(Priority.HIGH)
                                        .placeholder(R.mipmap.zw_img_300)
                                        .into(imgDianpuLogo);
                                tvDianpu.setText(dianpuBean.getDianpu());
                                tvDianpuTop.setText(dianpuBean.getDianpu());
                                tvSale.setText("已销:" + dianpuBean.getTotalSale() + "件");
                            } else {
                                StringUtil.showToast(DianpuActivity.this, jsonObject.optString("errmsg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        DialogSingleUtil.dismiss(0);
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(DianpuActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(DianpuActivity.this, e.message);
                    }
                });
    }

    private void queryProductListByKeyword(String dianpuid, String sortWay, String keyword) {
        refreshRoot.setNoMoreData(false);
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("dianpu", dianpuid);
        maps.put("keyword", keyword);
        maps.put("sortWay", sortWay);
        maps.put("page", page + "");
        RetrofitClient.getInstance(this).createBaseApi().queryProductListByKeyword(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            JSONObject jsonObject1 = new JSONObject(content);
                            if (jsonObject.optString("status").equals("1")) {
                                Logg.json(content);
                                List<ShopDianpuBean> shopDianpuBeans = JSON.parseArray(jsonObject1.optString("list"), ShopDianpuBean.class);
                                //禁用滑动事件
                                mrecycler.setNestedScrollingEnabled(false);
                                mrecycler.setLayoutManager(new GridLayoutManager(DianpuActivity.this, 2));
                                if (x == 1) {
                                    if (shopDianpuBeans != null && shopDianpuBeans.size() > 0) {
                                        refreshRoot.setEnableLoadMore(true);
                                        mrecycler.setVisibility(View.VISIBLE);
                                        dianPuGridAdapter = new DianPuGridAdapter(DianpuActivity.this, shopDianpuBeans);
                                        mrecycler.setAdapter(dianPuGridAdapter);
                                        progress.loadSuccess();
                                    } else {
                                        mrecycler.setVisibility(View.GONE);
                                        progress.setVisibility(View.VISIBLE);
                                        progress.loadSuccess(true);
                                        refreshRoot.setEnableLoadMore(false);
                                    }
                                } else {
                                    if (shopDianpuBeans != null && shopDianpuBeans.size() > 0) {
                                        dianPuGridAdapter.notifyData(shopDianpuBeans);
                                    } else {
                                        refreshRoot.finishLoadMoreWithNoMoreData();
                                    }
                                }
                            } else {
                                StringUtil.showToast(DianpuActivity.this, jsonObject.optString("errmsg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        refreshRoot.finishRefresh();
                        refreshRoot.finishLoadMore();
                        DialogSingleUtil.dismiss(0);
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(DianpuActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        refreshRoot.finishRefresh();
                        refreshRoot.finishLoadMore();
                        progress.loadError();
                        progress.setVisibility(View.VISIBLE);
                        StringUtil.showToast(DianpuActivity.this, e.message);
                    }
                });
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onScrollChanged(MyScrollViewNew scrollView, int x, int y, int oldx, int oldy) {
        if (y <= 0) {
            lin.setBackgroundResource(R.drawable.bg_home_yinying);
            lin.setVisibility(View.VISIBLE);
            llType.setVisibility(View.GONE);
            tvDianpuTop.setVisibility(View.VISIBLE);
            titleBackBtn1.setVisibility(View.INVISIBLE);
            toTopBtn.setVisibility(View.GONE);
            imgCar.setVisibility(View.GONE);
        } else if (y > 0 && y <= imageHeight) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            float scale = (float) y / imageHeight;
            float alpha = (255 * scale);
            lin.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
            tvDianpuTop.setTextColor(Color.argb((int) alpha, 0, 0, 0));
            lin.setVisibility(View.VISIBLE);
            llType.setVisibility(View.GONE);
            titleBackBtn1.setVisibility(View.INVISIBLE);
            toTopBtn.setVisibility(View.GONE);
            imgCar.setVisibility(View.GONE);
        } else {
            //滑动到banner下面设置普通颜色
            //将标题栏的颜色设置为完全不透明状态
            lin.setBackgroundResource(R.color.white);
            lin.setVisibility(View.VISIBLE);
            tvDianpuTop.setVisibility(View.VISIBLE);
            llType.setVisibility(View.VISIBLE);
            titleBackBtn1.setVisibility(View.VISIBLE);
            toTopBtn.setVisibility(View.VISIBLE);
            imgCar.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取顶部图片高度后，设置滚动监听
     */
    private void initListeners() {

        ViewTreeObserver vto = banner.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                banner.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                imageHeight = banner.getHeight();
                scrollview.setScrollViewListener(DianpuActivity.this);
            }
        });
    }


    @OnClick({R.id.title_back_btn, R.id.ll_mall_choose, R.id.title_back_btn1, R.id.ll_back, R.id.ll_kefu,R.id.img_car, R.id.to_top_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_btn:
                finish();
                break;
            case R.id.ll_mall_choose:
                showBaozhangeDialog(this);
                break;
            case R.id.title_back_btn1:
                finish();
                break;
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_kefu:
//                HomeLoadUtil.startChat(this);
                MainActivity.consultService(this, "", "店铺",null);
                break;
            case R.id.img_car:
                Intent intent = new Intent(this, CarActivity.class);
                startActivity(intent);
                break;
            case R.id.to_top_btn:
                scrollview.scrollTo(0, 0);
                break;
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
                    dianpuTypesAdapter.setTypeInterface(DianpuActivity.this);
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
        queryProductListByKeyword(dianpuid, sortway, keyword);
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

}
