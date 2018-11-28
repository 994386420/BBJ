package com.bbk.shopcar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.PlBean;
import com.bbk.activity.BaseActivity;
import com.bbk.activity.DianpuActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.ShopDetailActivty;
import com.bbk.adapter.MyShopPLAdapter;
import com.bbk.adapter.ShopOrderWaiCengAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.CommonLoadingView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.logg.Logg;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 评论
 */
public class MyPlActivity extends BaseActivity implements CommonLoadingView.LoadingHandler{


    @BindView(R.id.title_back_btn)
    ImageButton titleBackBtn;
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.img_car_black)
    ImageView imgCarBlack;
    @BindView(R.id.header)
    LinearLayout header;
    @BindView(R.id.headerParent)
    LinearLayout headerParent;
    @BindView(R.id.pl_list)
    RecyclerView plList;
    @BindView(R.id.pl_progress)
    CommonLoadingView plProgress;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.ll_dianpu)
    LinearLayout llDianpu;
    @BindView(R.id.ll_add_car)
    LinearLayout llAddCar;
    @BindView(R.id.ll_buy_now)
    LinearLayout llBuyNow;
    @BindView(R.id.layout)
    RelativeLayout layout;
    private String productid,img;
    private int page = 1,x =1;
    private List<PlBean> plBeans;
    private MyShopPLAdapter myShopPLAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_pll_layout);
        ButterKnife.bind(this);
        ImmersedStatusbarUtils.initAfterSetContentView(this, layout);
        productid = getIntent().getStringExtra("id");
        img = getIntent().getStringExtra("img");
        Glide.with(this)
                .load(img)
                .priority(Priority.HIGH)
                .placeholder(R.mipmap.zw_img_300)
                .into(icon);
        initView();
        queryPLByProductid();
    }

    public void initView() {
        plProgress.setLoadingHandler(this);
        plList.setHasFixedSize(true);
        plList.setLayoutManager(new LinearLayoutManager(MyPlActivity.this));
        refreshAndloda();
    }


    private void queryPLByProductid() {
        refresh.setNoMoreData(false);
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        maps.put("productid", productid);
        maps.put("page", page + "");
        RetrofitClient.getInstance(this).createBaseApi().queryPLByProductid(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            if (jsonObject.optString("status").equals("1")) {
                                Logg.json(jsonObject);
                                plBeans = JSON.parseArray(content,PlBean.class);
                                if (x == 1) {
                                    if ( plBeans != null &&  plBeans.size() > 0) {
                                        refresh.setEnableLoadMore(true);
                                        myShopPLAdapter = new MyShopPLAdapter(MyPlActivity.this,plBeans);
                                        plList.setAdapter(myShopPLAdapter);
                                        plList.setVisibility(View.VISIBLE);
                                        plProgress.loadSuccess();
                                    } else {
                                        plProgress.setVisibility(View.VISIBLE);
                                        plList.setVisibility(View.GONE);
                                        plProgress.loadSuccess(true);
                                        refresh.setEnableLoadMore(false);
                                    }
                                } else {
                                    plList.setVisibility(View.VISIBLE);
                                    plProgress.loadSuccess();
                                    if ( plBeans != null &&  plBeans.size() > 0) {
                                        myShopPLAdapter.notifyData( plBeans);
                                    } else {
                                        refresh.finishLoadMoreWithNoMoreData();
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
                        refresh.finishLoadMore();
                        refresh.finishRefresh();
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(MyPlActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        plProgress.setVisibility(View.VISIBLE);
                        plProgress.loadError();
                        plList.setVisibility(View.GONE);
                        refresh.finishLoadMore();
                        refresh.finishRefresh();
                        StringUtil.showToast(MyPlActivity.this, e.message);
                    }
                });
    }

    private void refreshAndloda() {
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                page = 1;
                x = 1;
                queryPLByProductid();
            }
        });
        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                page++;
                x = 2;
                queryPLByProductid();
            }
        });
    }

    @OnClick({R.id.title_back_btn, R.id.img_car_black, R.id.ll_dianpu, R.id.ll_add_car, R.id.ll_buy_now})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.title_back_btn:
                finish();
                break;
            case R.id.img_car_black:
                intent = new Intent(this, CarActivity.class);
                intent.putExtra("ziying","yes");
                startActivity(intent);
                break;
            case R.id.ll_dianpu:
                break;
            case R.id.ll_add_car:
                break;
            case R.id.ll_buy_now:
                break;
        }
    }

    @Override
    public void doRequestData() {
        plProgress.setVisibility(View.GONE);
        page = 1;
        x = 1;
        queryPLByProductid();
    }
}
