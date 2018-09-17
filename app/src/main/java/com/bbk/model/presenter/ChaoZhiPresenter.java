package com.bbk.model.presenter;

import android.content.Context;
import com.alibaba.fastjson.JSON;
import com.bbk.Bean.MiaoShaBean;
import com.bbk.Bean.NewHomeCzgBean;
import com.bbk.Bean.PinTuanBean;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.model.view.ChaoZhiTypesView;
import com.bbk.model.view.ChaoZhiView;
import com.bbk.resource.NewConstants;
import com.bbk.shopcar.view.View;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.StringUtil;
import com.logg.Logg;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2018/8/30/030.
 */

public class ChaoZhiPresenter implements Presenter {
    private ChaoZhiView chaoZhiView;
    private ChaoZhiTypesView chaoZhiTypesView;
    private Context mContext;
    private String Flag;
    List<NewHomeCzgBean> czgBeans;
    List<MiaoShaBean> miaoShaBeans;
    List<PinTuanBean> pinTuanBeans;

    public ChaoZhiPresenter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public void attachView(View view) {
        chaoZhiView = (ChaoZhiView) view;
    }

    @Override
    public void attachTypesView(View view) {
        chaoZhiTypesView = (ChaoZhiTypesView) view;
    }


    /**
     * 9.9，超级返  数据接口
     * @param type 1表示9.9   2表示超级返
     * @param page 分页
     * @param materialId 分类参数
     */
    public void getPageListChaozhigou99(final String type, int page, String materialId, final int x) {
        Logg.e(type+"===="+page+"===="+materialId+"===="+x);
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("type", type);
        maps.put("page", page+"");
        maps.put("materialId", materialId);
        RetrofitClient.getInstance(mContext).createBaseApi().getPageListChaozhigou99(
                maps, new BaseObserver<String>(mContext) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            JSONObject jo = new JSONObject(content);
                            String isBlandCzg = jo.optString("isBland");
                            String state = jo.optString("state");
                            if (jsonObject.optString("status").equals("1")) {
                                if (isBlandCzg.equals("1")) {
                                    JSONObject info = jo.getJSONObject("info");
                                    String tmpCzg = info.optString("page");
                                    Logg.json(tmpCzg);
                                    Flag = "3";
                                    switch (type){
                                        case "1":
                                            czgBeans = JSON.parseArray(tmpCzg, NewHomeCzgBean.class);
                                            break;
                                        case "2":
                                            czgBeans = JSON.parseArray(tmpCzg, NewHomeCzgBean.class);
                                            break;
                                        case "3":
                                            pinTuanBeans =JSON.parseArray(tmpCzg,PinTuanBean.class);
                                            break;
                                        case "4":
                                            miaoShaBeans = JSON.parseArray(tmpCzg, MiaoShaBean.class);
                                            break;
                                    }
                                    chaoZhiView.onSuccess(czgBeans,miaoShaBeans,pinTuanBeans,state);
                                } else if (isBlandCzg.equals("-1") && x == 1){
                                    chaoZhiView.noDataFirst();
                                }else if (isBlandCzg.equals("-1") && x == 2 && Flag.equals("3")) {
                                    chaoZhiView.noData();
                                }
                            }else {
                                chaoZhiView.onError(jsonObject.optString("errmsg"));
                            }
//                            if (jsonObject.optString("status").equals("1")) {
//                                    List<NewHomeCzgBean> czgBeans = JSON.parseArray(content, NewHomeCzgBean.class);
//                                    chaoZhiView.onSuccess(czgBeans);
//                                } else {
//                                    chaoZhiView.onError(jsonObject.optString("errmsg"));
//                                }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        DialogSingleUtil.dismiss(0);
                        chaoZhiView.onHide();
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(mContext);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        chaoZhiView.onFailed();
                        StringUtil.showToast(mContext, e.message);
                    }
                });
    }


    /**
     * 9.9，超级返  筛选的分类
     * @param type
     */
    public void getPageListChaozhigou99Types(String type) {
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("type", type);
        RetrofitClient.getInstance(mContext).createBaseApi().getPageListChaozhigou99Types(
                maps, new BaseObserver<String>(mContext) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            if (jsonObject.optString("status").equals("1")) {
//                                Logg.json(content);
                                chaoZhiTypesView.onSuccess(content);
                            } else {
                                chaoZhiTypesView.onError(jsonObject.optString("errmsg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        chaoZhiTypesView.onHide();
//                        DialogSingleUtil.dismiss(0);
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(mContext);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        chaoZhiTypesView.onFailed();
                        StringUtil.showToast(mContext, e.message);
                    }
                });
    }
}
