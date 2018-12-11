package com.bbk.shopcar.presenter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import com.alibaba.fastjson.JSON;
import com.bbk.Bean.DianpuBean;
import com.bbk.Bean.ShopDianpuBean;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.shopcar.view.DianpuListView;
import com.bbk.shopcar.view.DianpuView;
import com.bbk.shopcar.view.View;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.CommonLoadingView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2018/8/30/030.
 */

public class DianpuPresenter implements Presenter {
    private DianpuView dianpuView;
    private DianpuListView dianpuListView;
    private Context mContext;
    private Handler mHandler = new Handler();

    public DianpuPresenter (Context mContext){
        this.mContext = mContext;
    }

    @Override
    public void attachView(View view) {
        dianpuView = (DianpuView) view;
    }

    public void attachListView(View view) {
        dianpuListView = (DianpuListView) view;
    }

    public void queryDianpuMainInfo(String dianpuid, final SmartRefreshLayout refreshLayout)
    {
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("dianpu", dianpuid);
        RetrofitClient.getInstance(mContext).createBaseApi().queryDianpuMainInfo(
                maps, new BaseObserver<String>(mContext) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            JSONObject object = new JSONObject(content);
                            if (jsonObject.optString("status").equals("1")) {
                                DianpuBean dianpuBean = JSON.parseObject(content, DianpuBean.class);
                                dianpuView.onSuccess(dianpuBean);
                            } else {
                                dianpuView.onError(jsonObject.optString("errmsg"));
//                                StringUtil.showToast(NewDianpuActivity.this, jsonObject.optString("errmsg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        DialogSingleUtil.dismiss(0);
                        refreshLayout.finishRefresh();
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(mContext);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        refreshLayout.finishRefresh();
                        StringUtil.showToast(mContext, e.message);
                    }
                });
    }


    public void queryProductListByKeyword(String producttype,String plevel, String dianpuid, String sortWay, String keyword, final SmartRefreshLayout refresh, final SmartRefreshLayout refreshLayout, final CommonLoadingView progress, final RecyclerView mrecycler, int page) {
        refresh.setNoMoreData(false);
        Map<String, String> maps = new HashMap<String, String>();
//        maps.put("dianpu", dianpuid);
//        maps.put("keyword", keyword);
        maps.put("sortWay", sortWay);
//        maps.put("page", page + "");
        maps.put("dianpu", dianpuid);
        maps.put("keyword", keyword);
        maps.put("producttype",producttype);
        maps.put("plevel",plevel);
        maps.put("page", page + "");
        RetrofitClient.getInstance(mContext).createBaseApi().queryZiyingListByKeyword(
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
                        DialogSingleUtil.show(mContext);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        refreshLayout.finishRefresh();
                        refresh.finishLoadMore();
                        mrecycler.setVisibility(android.view.View.GONE);
                        progress.loadError();
                        progress.setVisibility(android.view.View.VISIBLE);
                        StringUtil.showToast(mContext, e.message);
                    }
                });
    }
}
