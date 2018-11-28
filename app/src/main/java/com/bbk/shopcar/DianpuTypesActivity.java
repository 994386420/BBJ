package com.bbk.shopcar;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.DianpuBean;
import com.bbk.Bean.ShopDianpuBean;
import com.bbk.activity.BaseActivity;
import com.bbk.activity.R;
import com.bbk.adapter.DianPuGridAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.model.tablayout.XTabLayout;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.StringUtil;
import com.bbk.view.CommonLoadingView;
import com.logg.Logg;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
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
public class DianpuTypesActivity extends BaseActivity implements CommonLoadingView.LoadingHandler{
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
    private int page = 1, x = 1;
    DianPuGridAdapter dianPuGridAdapter;
    private String sortway = "", keyword = "",position = "0";
    DianpuBean dianpuBean;
    JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.home_types_layout);
        View topView = findViewById(R.id.lltype);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        ButterKnife.bind(this);
        refreshAndloda();
        String tag = getIntent().getStringExtra("tag");
        keyword = getIntent().getStringExtra("keyword");
        position = getIntent().getStringExtra("position");
        mrecycler.setLayoutManager(new GridLayoutManager(DianpuTypesActivity.this, 2));
        mrecycler.setHasFixedSize(true);
        tablayout.setxTabDisplayNum(5);
        progress.setLoadingHandler(this);
        try {
            jsonArray = new JSONArray(tag);
            Logg.json(jsonArray);
            for (int i = 0;i < jsonArray.length();i++){
                titleText.setText(jsonArray.getJSONObject(Integer.parseInt(position)).optString("name"));
                if (!jsonArray.getJSONObject(i).optString("name").equals("全部分类")) {
                    tablayout.addTab(tablayout.newTab().setText(jsonArray.getJSONObject(i).optString("name")));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tablayout.setOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                int j = tab.getPosition();
                titleText.setText(tab.getText().toString());
                try {
                    switch (j){
                        case 0:
                            keyword =jsonArray.getJSONObject(0).optString("keyword");
                            break;
                        case 1:
                            keyword =jsonArray.getJSONObject(1).optString("keyword");
                            break;
                        case 2:
                            keyword =jsonArray.getJSONObject(2).optString("keyword");
                            break;
                        case 3:
                            keyword =jsonArray.getJSONObject(3).optString("keyword");
                            break;
                        case 4:
                            keyword =jsonArray.getJSONObject(4).optString("keyword");
                            break;
                    }
                    x = 1;
                    page = 1;
                    queryProductListByKeyword("", sortway, keyword);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(XTabLayout.Tab tab) {

            }
        });
        if (position  != null && position.equals("0")) {
            x = 1;
            page = 1;
            queryProductListByKeyword("", sortway, keyword);
        }else {
            XTabLayout.Tab tabAt = tablayout.getTabAt(Integer.parseInt(position));
            tabAt.select();
        }
    }

    private void refreshAndloda() {
        mPtrframe.setEnableLoadMore(false);
        mPtrframe.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                x = 1;
                page = 1;
                queryProductListByKeyword("", sortway, keyword);
            }
        });
        mPtrframe.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                x = 2;
                page++;
                queryProductListByKeyword("", sortway, keyword);
            }
        });

    }


    private void queryProductListByKeyword(String dianpuid, String sortWay, String keyword) {
        Logg.e(keyword);
        mPtrframe.setNoMoreData(false);
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
//                                Logg.json(content);
                                List<ShopDianpuBean> shopDianpuBeans = JSON.parseArray(jsonObject1.optString("list"), ShopDianpuBean.class);
                                if (x == 1) {
                                    if (shopDianpuBeans != null && shopDianpuBeans.size() > 0) {
                                        mPtrframe.setEnableLoadMore(true);
                                        mrecycler.setVisibility(View.VISIBLE);
                                        dianPuGridAdapter = new DianPuGridAdapter(DianpuTypesActivity.this, shopDianpuBeans);
                                        mrecycler.setAdapter(dianPuGridAdapter);
                                        progress.loadSuccess();
                                    } else {
                                        mrecycler.setVisibility(View.GONE);
                                        progress.setVisibility(View.VISIBLE);
                                        progress.loadSuccess(true);
                                        mPtrframe.setEnableLoadMore(false);
                                    }
                                } else {
                                    if (shopDianpuBeans != null && shopDianpuBeans.size() > 0) {
                                        dianPuGridAdapter.notifyData(shopDianpuBeans);
                                    } else {
                                        StringUtil.showToast(DianpuTypesActivity.this, "没有更多了");
                                        mPtrframe.setEnableLoadMore(false);
                                    }
                                }
                            } else {
                                StringUtil.showToast(DianpuTypesActivity.this, jsonObject.optString("errmsg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        mPtrframe.finishLoadMore();
                        mPtrframe.finishRefresh();
                        DialogSingleUtil.dismiss(0);
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(DianpuTypesActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        mPtrframe.finishRefresh();
                        mPtrframe.finishLoadMore();
                        progress.loadError();
                        progress.setVisibility(View.VISIBLE);
                        mrecycler.setVisibility(View.GONE);
                        StringUtil.showToast(DianpuTypesActivity.this, e.message);
                    }
                });
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.title_back_btn)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void doRequestData() {
        x = 1;
        page = 1;
        queryProductListByKeyword("", sortway, keyword);
    }
}
