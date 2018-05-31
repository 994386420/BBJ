package com.bbk.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.andview.refreshview.XRefreshView;
import com.bbk.Bean.BiaoLiaoBean;
import com.bbk.Bean.NewHomeCzgBean;
import com.bbk.activity.GossipPiazzaDetailActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.MyGossipActivity;
import com.bbk.activity.R;
import com.bbk.activity.UserLoginNewActivity;
import com.bbk.activity.WebViewActivity;
import com.bbk.activity.WebViewWZActivity;
import com.bbk.adapter.GossipPiazzaAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.fragment.BaseViewPagerFragment;
import com.bbk.resource.Constants;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.CommonLoadingView;
import com.bbk.view.HeaderView;
import com.bbk.view.MyFootView;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.OnLoadmoreListener;
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

/**
 * Created by rtj on 2017/11/23.
 * 爆料
 */
public class GossipPiazzaFragment extends BaseViewPagerFragment implements CommonLoadingView.LoadingHandler {
    private SmartRefreshLayout mrefresh;
    private RecyclerView mrecyclerview;
    private GossipPiazzaAdapter adapter;
    private int page = 1,x = 1;
    private View mView;
    private View data_head;
    private FloatingActionButton float_btn;
    private List<BiaoLiaoBean> biaoLiaoBeans;
    private CommonLoadingView zLoadingView;//加载框

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == mView) {
            mView = inflater.inflate(R.layout.activity_gossip_piazza, null);
            data_head = mView.findViewById(R.id.data_head);
            initstateView();
            initView();
        }
        return mView;
    }

    private void initData() {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        maps.put("page", page + "");
        RetrofitClient.getInstance(getActivity()).createBaseApi().queryBaoliaoMessage(
                maps, new BaseObserver<String>(getActivity()) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                biaoLiaoBeans = JSON.parseArray(jsonObject.optString("content"),BiaoLiaoBean.class);
                                if (x == 1) {
                                    adapter = new GossipPiazzaAdapter(getActivity(), biaoLiaoBeans);
                                    mrecyclerview.setAdapter(adapter);
                                }else if (x == 2) {
                                    if (biaoLiaoBeans != null && biaoLiaoBeans.size() > 0 && adapter != null){
                                        adapter.notifyData(biaoLiaoBeans);
                                    }else {
                                        StringUtil.showToast(getActivity(),"没有更多了");
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
                        DialogSingleUtil.show(getActivity());
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        zLoadingView.setVisibility(View.VISIBLE);
                        zLoadingView.loadError();
                        mrecyclerview.setVisibility(View.GONE);
                        mrefresh.finishLoadMore();
                        mrefresh.finishRefresh();
                        StringUtil.showToast(getActivity(), "网络异常");
                    }
                });
    }

    private void initView() {
        zLoadingView = mView.findViewById(R.id.progress);
        zLoadingView.setVisibility(View.GONE);
        zLoadingView.setLoadingHandler(this);
        mrefresh =  mView.findViewById(R.id.mrefresh);
        mrecyclerview =  mView.findViewById(R.id.mrecyclerview);
        float_btn = mView.findViewById(R.id.float_btn);
        mrecyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        refreshAndloda();
        float_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
                if (!TextUtils.isEmpty(userID)){
                    Intent Intent = new Intent(getActivity(), MyGossipActivity.class);
                    startActivity(Intent);
                }else {
                    Intent Intent = new Intent(getActivity(), UserLoginNewActivity.class);
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
                initData();
            }
        });
        mrefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                page++;
                x = 2;
                initData();
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

            sbar = getContext().getResources().getDimensionPixelSize(x);

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
        ImmersedStatusbarUtils.FlymeSetStatusBarLightMode(getActivity().getWindow(),true);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void loadLazyData() {
        mrefresh.autoRefresh();
//        initData();
    }

    @Override
    public void doRequestData() {
        zLoadingView.setVisibility(View.GONE);
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DialogSingleUtil.dismiss(0);
    }
}
