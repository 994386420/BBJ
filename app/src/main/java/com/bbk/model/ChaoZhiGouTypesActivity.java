package com.bbk.model;

import android.animation.ObjectAnimator;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.ChaozhigouTypesBean;
import com.bbk.Bean.MiaoShaBean;
import com.bbk.Bean.NewHomeCzgBean;
import com.bbk.Bean.PinTuanBean;
import com.bbk.activity.BaseActivity;
import com.bbk.activity.IntentActivity;
import com.bbk.activity.MesageCenterActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.UserLoginNewActivity;
import com.bbk.activity.WebViewActivity;
import com.bbk.adapter.MiaoShaStatusAdapter;
import com.bbk.adapter.NewCzuanAdapter;
import com.bbk.adapter.NewMIaoShaAdapter;
import com.bbk.adapter.NewPinTuanAdapter;
import com.bbk.adapter.TypeGridAdapter;
import com.bbk.model.presenter.ChaoZhiPresenter;
import com.bbk.model.tablayout.XTabLayout;
import com.bbk.model.view.ChaoZhiTypesView;
import com.bbk.model.view.ChaoZhiView;
import com.bbk.resource.NewConstants;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.CommonLoadingView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 9.9，超级返
 */
public class ChaoZhiGouTypesActivity extends BaseActivity implements CommonLoadingView.LoadingHandler,NewMIaoShaAdapter.LogMiaoShaInterface,NewPinTuanAdapter.LogPinTuanInterface {
    @BindView(R.id.title_back_btn)
    ImageButton titleBackBtn;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tablayout)
    XTabLayout tablayout;
    @BindView(R.id.mrecycler)
    RecyclerView mrecycler;
    @BindView(R.id.progress)
    CommonLoadingView progress;
    @BindView(R.id.mPtrframe)
    SmartRefreshLayout mPtrframe;
    @BindView(R.id.type_image)
    LinearLayout typeImage;
    @BindView(R.id.lltype)
    LinearLayout lltype;
    @BindView(R.id.type_grid)
    GridView typeGrid;
    @BindView(R.id.ll_shouqi)
    LinearLayout llShouqi;
    @BindView(R.id.fl_type)
    FrameLayout flType;
    @BindView(R.id.miaosha_status)
    GridView miaoshaStatus;
    private int page = 1, x = 1;
    NewCzuanAdapter newCzuanAdapter;
    NewMIaoShaAdapter newMIaoShaAdapter;
    NewPinTuanAdapter newPinTuanAdapter;
    private String type = "2", materialId;
    private ChaoZhiPresenter chaoZhiPresenter = new ChaoZhiPresenter(this);
    TypeGridAdapter typeGridAdapter;
    MiaoShaStatusAdapter miaoShaStatusAdapter;
    List<ChaozhigouTypesBean> chaozhigouTypesBeans;
    private int showTime = 0, curposition = 0,statusPosition = 0;
    private int durationRotate = 700;// 旋转动画时间
    private int durationAlpha = 500;// 透明度动画时间
    private boolean isGlobalMenuShow = true;
    private String url1,title1,domain1,type1,isczg1,bprice1,quan1,zuan1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.home_types_layout);
        View topView = findViewById(R.id.lltype);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        ButterKnife.bind(this);
        refreshAndloda();
        getType();
        chaoZhiPresenter.attachView(chaoZhiView);
        chaoZhiPresenter.attachTypesView(chaoZhiTypesView);
        chaoZhiPresenter.getPageListChaozhigou99Types(type);
        mrecycler.setLayoutManager(new LinearLayoutManager(this));
        mrecycler.setHasFixedSize(true);
        tablayout.setxTabDisplayNum(5);
        progress.setLoadingHandler(this);
        tablayout.setOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                int j = tab.getPosition();
                curposition = j;
                x = 1;
                page = 1;
                materialId = chaozhigouTypesBeans.get(j).getKeyword();
                mPtrframe.setNoMoreData(false);
                chaoZhiPresenter.getPageListChaozhigou99(type, page, materialId, x);
            }

            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(XTabLayout.Tab tab) {

            }
        });
    }

    private void getType() {
        if (getIntent().getStringExtra("type") != null) {
            type = getIntent().getStringExtra("type");
            if (type.equals("2")) {
                titleText.setBackgroundResource(R.mipmap.title_06);
            }else if (type.equals("4")){
                titleText.setBackgroundResource(R.mipmap.title_05);
            }else if (type.equals("3")){
                titleText.setBackgroundResource(R.mipmap.title_07);
            }else {
                titleText.setBackgroundResource(R.mipmap.title_08);
            }
        }
    }

    private void refreshAndloda() {
        mPtrframe.setEnableLoadMore(false);
        mPtrframe.setNoMoreData(false);
        mPtrframe.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                x = 1;
                page = 1;
                chaoZhiPresenter.getPageListChaozhigou99(type, page, materialId, x);
            }
        });
        mPtrframe.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                x = 2;
                page++;
                chaoZhiPresenter.getPageListChaozhigou99(type, page, materialId, x);
            }
        });

    }

    /**
     * 9.9，超级返
     */
    private ChaoZhiView chaoZhiView = new ChaoZhiView() {
        @Override
        public void onSuccess(List<NewHomeCzgBean> czgBeans, List<MiaoShaBean> miaoShaBeans, List<PinTuanBean> pinTuanBeans,String state) {
            if (x == 1) {
                mPtrframe.setEnableLoadMore(true);
                if (typeGridAdapter != null) {
                    typeGridAdapter.setSeclection(curposition);
                    typeGridAdapter.notifyDataSetChanged();
                }
                mrecycler.setVisibility(View.VISIBLE);
                progress.loadSuccess();
                switch (type) {
                    case "1":
                        if (czgBeans != null && czgBeans.size() > 0) {
                            newCzuanAdapter = new NewCzuanAdapter(ChaoZhiGouTypesActivity.this, czgBeans);
                            mrecycler.setAdapter(newCzuanAdapter);
                        } else {
                            mrecycler.setVisibility(View.GONE);
                            progress.loadSuccess(true);
                            mPtrframe.setEnableLoadMore(false);
                        }
                        break;
                    case "2":
                        if (czgBeans != null && czgBeans.size() > 0) {
                            newCzuanAdapter = new NewCzuanAdapter(ChaoZhiGouTypesActivity.this, czgBeans);
                            mrecycler.setAdapter(newCzuanAdapter);
                        } else {
                            mrecycler.setVisibility(View.GONE);
                            progress.loadSuccess(true);
                            mPtrframe.setEnableLoadMore(false);
                        }
                        break;
                    case "3":
                        if (pinTuanBeans != null && pinTuanBeans.size() > 0) {
                            newPinTuanAdapter = new NewPinTuanAdapter(ChaoZhiGouTypesActivity.this, pinTuanBeans);
                            mrecycler.setAdapter(newPinTuanAdapter);
                            newPinTuanAdapter.setLogPinTuanInterface(ChaoZhiGouTypesActivity.this);
                        } else {
                            mrecycler.setVisibility(View.GONE);
                            progress.loadSuccess(true);
                            mPtrframe.setEnableLoadMore(false);
                        }
                        break;
                    case "4":
                        if (miaoShaBeans != null && miaoShaBeans.size() > 0) {
                            newMIaoShaAdapter = new NewMIaoShaAdapter(ChaoZhiGouTypesActivity.this, miaoShaBeans,state,materialId);
                            mrecycler.setAdapter(newMIaoShaAdapter);
                            newMIaoShaAdapter.setLogMiaoShaInterface(ChaoZhiGouTypesActivity.this);
                        } else {
                            mrecycler.setVisibility(View.GONE);
                            progress.loadSuccess(true);
                            mPtrframe.setEnableLoadMore(false);
                        }
                        break;
                }

            } else {

                switch (type) {
                    case "1":
                        if (czgBeans != null && czgBeans.size() > 0) {
                            newCzuanAdapter.notifyData(czgBeans);
                        } else {
                            mPtrframe.finishLoadMoreWithNoMoreData();
                        }
                        break;
                    case "2":
                        if (czgBeans != null && czgBeans.size() > 0) {
                            newCzuanAdapter.notifyData(czgBeans);
                        } else {
                            mPtrframe.finishLoadMoreWithNoMoreData();
                        }
                        break;
                    case "3":
                        if (pinTuanBeans != null && pinTuanBeans.size() > 0) {
                            newPinTuanAdapter.notifyData(pinTuanBeans);
                        } else {
                            mPtrframe.finishLoadMoreWithNoMoreData();
                        }
                        break;
                    case "4":
                        if (miaoShaBeans != null && miaoShaBeans.size() > 0) {
                            newMIaoShaAdapter.notifyData(miaoShaBeans);
                        } else {
                            mPtrframe.finishLoadMoreWithNoMoreData();
                        }
                        break;
                }

            }
        }

        @Override
        public void onError(String result) {
            finishLoad();
            StringUtil.showToast(ChaoZhiGouTypesActivity.this, result);
        }

        @Override
        public void onHide() {
            finishLoad();
        }

        @Override
        public void onFailed() {
            finishLoad();
            progress.loadError();
            progress.setVisibility(View.VISIBLE);
            mPtrframe.setEnableLoadMore(false);
            mrecycler.setVisibility(View.GONE);
        }

        @Override
        public void noData() {
            mPtrframe.finishLoadMoreWithNoMoreData();
        }

        @Override
        public void noDataFirst() {
            mrecycler.setVisibility(View.GONE);
            progress.setVisibility(View.VISIBLE);
            progress.loadSuccess(true);
            mPtrframe.setEnableLoadMore(false);
        }
    };

    /**
     * 9.9，超级返 分类
     */
    private ChaoZhiTypesView chaoZhiTypesView = new ChaoZhiTypesView() {
        @Override
        public void onSuccess(String content) {
            chaozhigouTypesBeans = JSON.parseArray(content, ChaozhigouTypesBean.class);
            if (showTime == 0) {
                showTime++;
                for (int i = 0; i < chaozhigouTypesBeans.size(); i++) {
                    String keyword = chaozhigouTypesBeans.get(i).getName();
                    tablayout.addTab(tablayout.newTab().setText(keyword));
                }
                switch (type) {
                    case "1":
                        if (chaozhigouTypesBeans.size() >= 5){
                            typeImage.setVisibility(View.VISIBLE);
                        }
                        miaoshaStatus.setVisibility(View.GONE);
                        tablayout.setVisibility(View.VISIBLE);
                        typeGridAdapter = new TypeGridAdapter(ChaoZhiGouTypesActivity.this, chaozhigouTypesBeans);
                        typeGrid.setAdapter(typeGridAdapter);
                        typeGrid.setOnItemClickListener(onItemClickListener);
                        break;
                    case "2":
                        if (chaozhigouTypesBeans.size() >= 5){
                            typeImage.setVisibility(View.VISIBLE);
                        }
                        miaoshaStatus.setVisibility(View.GONE);
                        tablayout.setVisibility(View.VISIBLE);
                        typeGridAdapter = new TypeGridAdapter(ChaoZhiGouTypesActivity.this, chaozhigouTypesBeans);
                        typeGrid.setAdapter(typeGridAdapter);
                        typeGrid.setOnItemClickListener(onItemClickListener);
                        break;
                    case "3":
                        if (chaozhigouTypesBeans.size() >= 5){
                            typeImage.setVisibility(View.VISIBLE);
                        }
                        miaoshaStatus.setVisibility(View.GONE);
                        tablayout.setVisibility(View.VISIBLE);
                        typeGridAdapter = new TypeGridAdapter(ChaoZhiGouTypesActivity.this, chaozhigouTypesBeans);
                        typeGrid.setAdapter(typeGridAdapter);
                        typeGrid.setOnItemClickListener(onItemClickListener);
                        break;
                    case "4":
                        miaoshaStatus.setVisibility(View.VISIBLE);
                        tablayout.setVisibility(View.GONE);
                        miaoShaStatusAdapter = new MiaoShaStatusAdapter(ChaoZhiGouTypesActivity.this,chaozhigouTypesBeans);
                        miaoshaStatus.setAdapter(miaoShaStatusAdapter);
                        miaoShaStatusAdapter.setSeclection(statusPosition);
                        miaoshaStatus.setOnItemClickListener(onItemStatusClickListener);
                        break;
                }
            }
        }

        @Override
        public void onError(String result) {
            finishLoad();
            StringUtil.showToast(ChaoZhiGouTypesActivity.this, result);
        }

        @Override
        public void onHide() {
            finishLoad();
        }

        @Override
        public void onFailed() {
            finishLoad();
            progress.loadError();
            progress.setVisibility(View.VISIBLE);
            mPtrframe.setEnableLoadMore(false);
            mrecycler.setVisibility(View.GONE);
        }
    };

    private void finishLoad() {
        mPtrframe.finishRefresh();
        mPtrframe.finishLoadMore();
    }

    /**
     * 下拉弹出窗点击事件
     */
    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            curposition = position;
            typeGridAdapter.setSeclection(position);
            typeGridAdapter.notifyDataSetChanged();
            XTabLayout.Tab tabAt = tablayout.getTabAt(position);
            tabAt.select();
            showGlobalMenu();
        }
    };
    AdapterView.OnItemClickListener onItemStatusClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            miaoShaStatusAdapter.setSeclection(position);
            miaoShaStatusAdapter.notifyDataSetChanged();
            XTabLayout.Tab tabAt = tablayout.getTabAt(position);
            tabAt.select();
        }
    };
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

    protected void onDestroy() {
        super.onDestroy();
        DialogSingleUtil.dismiss(0);
    }


    @OnClick({R.id.title_back_btn, R.id.ll_shouqi, R.id.type_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_btn:
                finish();
                break;
            case R.id.ll_shouqi:
                showGlobalMenu();
                break;
            case R.id.type_image:
                showGlobalMenu();
        }
    }

    @Override
    public void doRequestData() {
        progress.setVisibility(View.GONE);
        x = 1;
        page = 1;
        chaoZhiPresenter.getPageListChaozhigou99(type, page, materialId, x);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent;
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        if (userID != null && !userID.equals("")) {
            switch (requestCode) {
                case 1:
                    if ("beibei".equals(domain1)
                            || "jd".equals(domain1) || "taobao".equals(domain1)
                            || "tmall".equals(domain1) || "suning".equals(domain1)) {
                        intent = new Intent(ChaoZhiGouTypesActivity.this, IntentActivity.class);
                        if (url1 != null) {
                            intent.putExtra("url", url1);
                        }
                        if (title1 != null) {
                            intent.putExtra("title",title1);
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
                        if (zuan1 != null){
                            intent.putExtra("zuan", zuan1);
                        }
                        intent.putExtra("type", "miaosha");
                    } else {
                        intent = new Intent(ChaoZhiGouTypesActivity.this, WebViewActivity.class);
                        intent.putExtra("url", url1);
                        intent.putExtra("title", title1);
                    }
                    startActivity(intent);
                    break;
            }
        }
    }

    @Override
    public void IntentMiaoShaLog(String url, String title, String domain, String isczg, String bprice, String quan,String zuan ,String type) {
        url1 = url;
        title1 = title;
        domain1 = domain;
        isczg1 = isczg;
        bprice1 = bprice;
        quan1 = quan;
        type1 = type;
        zuan1 = zuan;
        Intent intent = new Intent(ChaoZhiGouTypesActivity.this, UserLoginNewActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void IntentPinTuanLog(String url, String title, String domain, String isczg, String bprice, String quan, String zuan ,String type) {
        url1 = url;
        title1 = title;
        domain1 = domain;
        isczg1 = isczg;
        bprice1 = bprice;
        quan1 = quan;
        type1 = type;
        zuan1 = zuan;
        Intent intent = new Intent(ChaoZhiGouTypesActivity.this, UserLoginNewActivity.class);
        startActivityForResult(intent, 1);
    }
}
