package com.bbk.model.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.DiscountBean;
import com.bbk.Bean.DiscountMenuCenterBean;
import com.bbk.Bean.DiscountPersonBean;
import com.bbk.Bean.MiaoShaBean;
import com.bbk.Bean.NewHomeCzgBean;
import com.bbk.Bean.PinTuanBean;
import com.bbk.Bean.ShopDianpuBean;
import com.bbk.Bean.ShopFenLeiBean;
import com.bbk.Bean.ZeroBuyBean;
import com.bbk.activity.MyApplication;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.model.view.ChaoZhiTypesView;
import com.bbk.model.view.ChaoZhiView;
import com.bbk.model.view.DianpuSearchView;
import com.bbk.model.view.DiscountCentenListView;
import com.bbk.model.view.DiscountCentenMenuView;
import com.bbk.model.view.DiscountView;
import com.bbk.model.view.ZeroBuyView;
import com.bbk.model.view.ZiyingFelileiView;
import com.bbk.shopcar.view.View;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.logg.Logg;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 优惠券presenter.
 */

public class DiscountPresenter implements DisPresenter{
    private DiscountView discountView;
    private DiscountCentenMenuView discountCentenMenuView;
    private DiscountCentenListView discountCentenListView;
    private Context mContext;
    List<DiscountPersonBean> discountPersonBeans;
    List<DiscountMenuCenterBean> discountMenuCenterBeans;

    public DiscountPresenter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public void attachView(View view) {
        discountView = (DiscountView) view;
    }

    @Override
    public void attachCenterMenu(View view) {
        discountCentenMenuView = (DiscountCentenMenuView) view;
    }

    @Override
    public void attachCenterList(View view) {
        discountCentenListView = (DiscountCentenListView) view;
    }


    /**
     * 查询个人的优惠券情况
     * @param state 1正常 -1已使用 0过期
     * @param page
     */
    public void queryCouponsListByUserid(String state, int page) {
        Map<String, String> maps = new HashMap<String, String>();
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        maps.put("userid", userID);
        maps.put("page", page+"");
        maps.put("state", state);
        RetrofitClient.getInstance(mContext).createBaseApi().queryCouponsListByUserid(
                maps, new BaseObserver<String>(mContext) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jsonObject1 = new JSONObject(jsonObject.optString("content"));
                            if (jsonObject.optString("status").equals("1")) {
                                Logg.json(jsonObject);
                                discountPersonBeans = JSON.parseArray(jsonObject1.optString("list"),DiscountPersonBean.class);
                                discountView.onSuccess(discountPersonBeans,jsonObject1.optString("已使用"),jsonObject1.optString("未使用"),jsonObject1.optString("已过期"));
                            }else {
                                discountView.onError(jsonObject.optString("errmsg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        DialogSingleUtil.dismiss(0);
                        discountView.onHide();
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(mContext);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        discountView.onFailed();
                        StringUtil.showToast(mContext, e.message);
                    }
                });
    }


    /**
     * 查询领劵中心列表
     * @param keyword
     * @param page
     */
    public void queryCouponsCenterList(String keyword, int page) {
        Map<String, String> maps = new HashMap<String, String>();
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        maps.put("userid", userID);
        maps.put("page", page+"");
        maps.put("keyword", keyword);
        RetrofitClient.getInstance(mContext).createBaseApi().queryCouponsCenterList(
                maps, new BaseObserver<String>(mContext) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                Logg.json(jsonObject);
                                discountMenuCenterBeans = JSON.parseArray(jsonObject.optString("content"),DiscountMenuCenterBean.class);
                                discountCentenListView.onSuccess(discountMenuCenterBeans);
                            }else {
                                discountCentenListView.onError(jsonObject.optString("errmsg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        DialogSingleUtil.dismiss(0);
                        discountCentenListView.onHide();
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(mContext);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        discountCentenListView.onFailed();
                        StringUtil.showToast(mContext, e.message);
                    }
                });
    }

    /**
     *  查询领劵中心导航
     */
    public void queryCouponsCenterMenu() {
        Map<String, String> maps = new HashMap<String, String>();
        RetrofitClient.getInstance(mContext).createBaseApi().queryCouponsCenterMenu(
                maps, new BaseObserver<String>(mContext) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray jsonArray = new JSONArray(jsonObject.optString("content"));
                            if (jsonObject.optString("status").equals("1")) {
                                discountCentenMenuView.onSuccess(jsonArray);
                            }else {
                                discountCentenMenuView.onError(jsonObject.optString("errmsg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        discountCentenMenuView.onHide();
                    }

                    @Override
                    protected void showDialog() {
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        discountCentenMenuView.onFailed();
                        StringUtil.showToast(mContext, e.message);
                    }
                });
    }
}
