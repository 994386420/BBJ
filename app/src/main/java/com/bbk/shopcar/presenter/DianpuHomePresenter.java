package com.bbk.shopcar.presenter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.DianPuHomeBean;
import com.bbk.Bean.DianpuBean;
import com.bbk.Bean.PinpaiBean;
import com.bbk.Bean.ShopDianpuBean;
import com.bbk.activity.MyApplication;
import com.bbk.adapter.DianPuHoHotGridAdapter;
import com.bbk.adapter.DianPuHomePinpaiGridAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.dialog.HomeAlertDialog;
import com.bbk.model.tablayout.XTabLayout;
import com.bbk.shopcar.NewDianpuHomeActivity;
import com.bbk.shopcar.view.DianpuHomeView;
import com.bbk.shopcar.view.DianpuListView;
import com.bbk.shopcar.view.DianpuView;
import com.bbk.shopcar.view.View;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.EventIdIntentUtil;
import com.bbk.util.HomeLoadUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.CommonLoadingView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2018/8/30/030.
 */

public class DianpuHomePresenter implements Presenter {
    private DianpuHomeView dianpuHomeView;
    private DianpuListView dianpuListView;
    private Context mContext;
    private Handler mHandler = new Handler();

    public DianpuHomePresenter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public void attachView(View view) {
        dianpuHomeView = (DianpuHomeView) view;
    }

    public void attachListView(View view) {
        dianpuListView = (DianpuListView) view;
    }

    /**
     * 首页数据请求
     */
    public void queryIndexMain(final SmartRefreshLayout refreshLayout, final RecyclerView mrecyclerview, final CommonLoadingView zLoadingView) {
        Map<String, String> maps = new HashMap<String, String>();
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        maps.put("userid", userID);
        RetrofitClient.getInstance(mContext).createBaseApi().queryIndexMain(
                maps, new BaseObserver<String>(mContext) {
                    @Override
                    public void onNext(String s) {
                        try {
                            mrecyclerview.setVisibility(android.view.View.VISIBLE);
                            refreshLayout.setVisibility(android.view.View.VISIBLE);
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                DianPuHomeBean dianPuHomeBean = JSON.parseObject(jsonObject.optString("content"), DianPuHomeBean.class);
                                dianpuHomeView.onSuccess(dianPuHomeBean);
                            }else {
                                dianpuHomeView.onError(jsonObject.optString("errmsg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        refreshLayout.setEnableRefresh(true);
                        refreshLayout.finishRefresh();
                        zLoadingView.loadSuccess();
                        DialogSingleUtil.dismiss(0);
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(mContext);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        refreshLayout.finishRefresh();
                        DialogSingleUtil.dismiss(0);
                        zLoadingView.setVisibility(android.view.View.VISIBLE);
                        zLoadingView.loadError();
                        refreshLayout.setVisibility(android.view.View.GONE);
                        mrecyclerview.setVisibility(android.view.View.GONE);
                        StringUtil.showToast(mContext, e.message);
                    }
                });
    }


    public void queryProductListByKeyword(String dianpuid, String sortWay, String keyword, final SmartRefreshLayout refresh, final SmartRefreshLayout refreshLayout, final CommonLoadingView loadingProgress, final RecyclerView mrecycler, int page) {
        refresh.setNoMoreData(false);
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("dianpu", dianpuid);
        maps.put("keyword", keyword);
        maps.put("sortWay", sortWay);
        maps.put("page", page + "");
        RetrofitClient.getInstance(mContext).createBaseApi().queryProductListByKeyword(
                maps, new BaseObserver<String>(mContext) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            JSONObject jsonObject1 = new JSONObject(content);
//                            Logg.json(jsonObject1);
                            if (jsonObject.optString("status").equals("1")) {
//                                Logg.json(content);
                                List<ShopDianpuBean> shopDianpuBeans = JSON.parseArray(jsonObject1.optString("list"), ShopDianpuBean.class);
                                dianpuListView.onSuccess(shopDianpuBeans);
                            } else {
                                dianpuListView.onError(jsonObject.optString("errmsg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        refresh.finishLoadMore();
                        refreshLayout.finishRefresh();
                        DialogSingleUtil.dismiss(0);
                    }

                    @Override
                    protected void showDialog() {
//                        DialogSingleUtil.show(mContext);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        refreshLayout.finishRefresh();
                        refresh.finishLoadMore();
                        mrecycler.setVisibility(android.view.View.GONE);
                        loadingProgress.setVisibility(android.view.View.VISIBLE);
                        loadingProgress.loadError();
                        StringUtil.showToast(mContext, e.message);
                    }
                });
    }
}
