package com.bbk.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.FenXiangListBean;
import com.bbk.adapter.FenXiangListAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.util.DialogCheckYouhuiUtil;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.ShareFenXiangUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.CommonLoadingView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by rtj on 2017/11/23.
 * 分享列表
 */
public class FenXiangActivty extends BaseActivity implements CommonLoadingView.LoadingHandler, FenXiangListAdapter.LogInterface {
    @BindView(R.id.topbar_title_iv)
    TextView topbarTitleIv;
    @BindView(R.id.title_back_btn)
    ImageButton titleBackBtn;
    @BindView(R.id.title_text)
    TextView titleText;
    private SmartRefreshLayout mrefresh;
    private RecyclerView mrecyclerview;
    private FenXiangListAdapter adapter;
    private int page = 1, x = 1;
    //    private View mView;
    private View data_head;
    private FloatingActionButton float_btn;
    private List<FenXiangListBean> fenXiangListBeans;
    private CommonLoadingView zLoadingView;//加载框
    private String rowkeysa, titlea;
    private View view;
    private ShareFenXiangUtil shareFenXiangUtil;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gossip_piazza);
        ButterKnife.bind(this);
        View topView = findViewById(R.id.topbar_layout);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
//        initstateView();
        initView();
        topbarTitleIv.setText("鲸港圈");
        titleText.setText("鲸港圈");
        queryCpsShareList();
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        if (null == mView) {
//            mView = inflater.inflate(R.layout.activity_gossip_piazza, null);
//            ButterKnife.bind(this, mView);
//            data_head = mView.findViewById(R.id.data_head);
//            initstateView();
//            initView();
//            topbarTitleIv.setText("鲸港圈");
//        }
//        return mView;
//    }

    private void queryCpsShareList() {
        mrefresh.setNoMoreData(false);
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        maps.put("page", page + "");
        RetrofitClient.getInstance(FenXiangActivty.this).createBaseApi().queryCpsShareList(
                maps, new BaseObserver<String>(FenXiangActivty.this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
//                            Log.i("分享",s);
                            if (jsonObject.optString("status").equals("1")) {
                                fenXiangListBeans = JSON.parseArray(jsonObject.optString("content"), FenXiangListBean.class);
                                if (x == 1) {
                                    adapter = new FenXiangListAdapter(FenXiangActivty.this, fenXiangListBeans);
                                    mrecyclerview.setAdapter(adapter);
                                    adapter.setLogInterface(FenXiangActivty.this);
                                } else if (x == 2) {
                                    if (fenXiangListBeans != null && fenXiangListBeans.size() > 0 && adapter != null) {
                                        adapter.notifyData(fenXiangListBeans);
                                    } else {
//                                        StringUtil.showToast(getActivity(),"没有更多了");
                                        mrefresh.finishLoadMoreWithNoMoreData();
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
                        zLoadingView.loadSuccess();
                        mrefresh.finishLoadMore();
                        mrefresh.finishRefresh();
                        mrecyclerview.setVisibility(View.VISIBLE);
                    }

                    @Override
                    protected void showDialog() {
//                        zLoadingView.load();
                        DialogSingleUtil.show(FenXiangActivty.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        zLoadingView.setVisibility(View.VISIBLE);
                        zLoadingView.loadError();
                        mrecyclerview.setVisibility(View.GONE);
                        mrefresh.finishLoadMore();
                        mrefresh.finishRefresh();
                        StringUtil.showToast(FenXiangActivty.this, e.message);
                    }
                });
    }

    private void initView() {
        zLoadingView = findViewById(R.id.progress);
        zLoadingView.setVisibility(View.GONE);
        zLoadingView.setLoadingHandler(this);
        mrefresh = findViewById(R.id.mrefresh);
        mrefresh.setBackgroundColor(getResources().getColor(R.color.__picker_common_primary));
        mrecyclerview = findViewById(R.id.mrecyclerview);
        float_btn = findViewById(R.id.float_btn);
        float_btn.setVisibility(View.GONE);
        mrecyclerview.setLayoutManager(new GridLayoutManager(FenXiangActivty.this, 1));
        mrecyclerview.setHasFixedSize(true);
        refreshAndloda();
        float_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
                if (!TextUtils.isEmpty(userID)) {
                    Intent Intent = new Intent(FenXiangActivty.this, MyGossipActivity.class);
                    startActivity(Intent);
                } else {
                    Intent Intent = new Intent(FenXiangActivty.this, UserLoginNewActivity.class);
                    startActivityForResult(Intent, 1);
                }

            }
        });
    }

    private void refreshAndloda() {
        mrefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                page = 1;
                x = 1;
                queryCpsShareList();
            }
        });
        mrefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                page++;
                x = 2;
                queryCpsShareList();
            }
        });
    }

    private int getStatusBarHeight() {
        Class<?> c = null;

        Object obj = null;

        Field field = null;

        int x = 0, sbar = 0;

        try {

            c = Class.forName("com.android.internal.R$dimen");

            obj = c.newInstance();

            field = c.getField("status_bar_height");

            x = Integer.parseInt(field.get(obj).toString());

            sbar = getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {

            e1.printStackTrace();

        }

        return sbar;
    }

    private void initstateView() {
        if (Build.VERSION.SDK_INT >= 19) {
            data_head.setVisibility(View.VISIBLE);
        }
        int result = getStatusBarHeight();
        ViewGroup.LayoutParams layoutParams = data_head.getLayoutParams();
        layoutParams.height = result;
        data_head.setLayoutParams(layoutParams);
        ImmersedStatusbarUtils.FlymeSetStatusBarLightMode(FenXiangActivty.this.getWindow(), true);
    }
//
//    @Override
//    protected void loadLazyData() {
////        mrefresh.autoRefresh();
//
//    }

    @Override
    public void doRequestData() {
        zLoadingView.setVisibility(View.GONE);
        queryCpsShareList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DialogSingleUtil.dismiss(0);
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//    }

    @Override
    public void IntentLog(View v, String rowkeys, String title) {
        rowkeysa = rowkeys;
        titlea = title;
        view = v;
        Intent intent = new Intent(FenXiangActivty.this, UserLoginNewActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        if (userID != null && !userID.equals("")) {
            switch (requestCode) {
                case 1:
                    shareCpsInfos(view, rowkeysa, titlea);
                    break;
            }
        }
    }


    /**
     * 分享多张图片到朋友圈
     *
     * @param v
     * @param rowkeys
     * @param title
     */
    private void shareCpsInfos(final View v, String rowkeys, final String title) {
        final String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        maps.put("rowkeys", rowkeys);
        RetrofitClient.getInstance(FenXiangActivty.this).createBaseApi().shareCpsInfos(
                maps, new BaseObserver<String>(FenXiangActivty.this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                List<String> DetailimgUrlList = new ArrayList<>();
                                JSONObject jsonObject1 = new JSONObject(jsonObject.optString("content"));
                                if (FenXiangListAdapter.cancelCheck) {
                                    Share(v, title, DetailimgUrlList, jsonObject1);
                                }
                            } else {
                                StringUtil.showToast(FenXiangActivty.this, jsonObject.optString("errmsg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        FenXiangListAdapter.cancelCheck = true;
                        DialogCheckYouhuiUtil.dismiss(0);
                    }

                    @Override
                    protected void showDialog() {
                        DialogCheckYouhuiUtil.show(FenXiangActivty.this, "正在生成您的专属分享图片...");
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogCheckYouhuiUtil.dismiss(0);
                        StringUtil.showToast(FenXiangActivty.this, e.message);
                    }
                });
    }

    /**
     * 微信分享
     *
     * @param v
     * @param title
     * @param DetailimgUrlList
     * @param jsonObject1
     */
    private void Share(View v, String title, List<String> DetailimgUrlList, JSONObject jsonObject1) {
        try {
            if (jsonObject1.has("wenan")) {
                String wenan = jsonObject1.optString("wenan");
                if (wenan != null && !wenan.equals("")) {
                    wenan = jsonObject1.optString("wenan").replace("|", "\n");
                }
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(title + "\n" + wenan);
            }
            if (jsonObject1.has("imgs")) {
                JSONArray detailImags = new JSONArray(jsonObject1.optString("imgs"));
                for (int i = 0; i < detailImags.length(); i++) {
                    String imgUrl = detailImags.getString(i);
                    DetailimgUrlList.add(imgUrl);
                }
                //调用转发微信功能类
                shareFenXiangUtil = new ShareFenXiangUtil(FenXiangActivty.this, v, title, DetailimgUrlList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.title_back_btn)
    public void onViewClicked() {
        finish();
    }
}
