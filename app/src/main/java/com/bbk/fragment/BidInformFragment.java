package com.bbk.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.andview.refreshview.XRefreshView;
import com.bbk.Bean.PubaMessageBean;
import com.bbk.activity.BidBillDetailActivity;
import com.bbk.activity.BidMyBillDetailActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.adapter.BidListDetailAdapter;
import com.bbk.adapter.BidMsgInformAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.flow.DataFlow;
import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.CommonLoadingView;
import com.bbk.view.HeaderView;
import com.bbk.view.MyFootView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息_发镖_通知
 */

public class BidInformFragment extends Fragment implements CommonLoadingView.LoadingHandler {

    private View mView;
    private DataFlow6 dataFlow;
    private ListView listView;
    private BidMsgInformAdapter adapter;
    private SmartRefreshLayout xrefresh;
    private int topicpage = 1,x = 1;
    private LinearLayout mNoMessageLayout;//无数据显示页面
    private List<PubaMessageBean> pubaMessageBeans;
    private CommonLoadingView zLoadingView;//加载框

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container
            , Bundle savedInstanceState) {
        /***
         * 判断Fragment是否已经添加了contentView（第一次加载时，可以将view保存下 来，再  次加载时，判断保存下来的view是否为null），
         * 如果保存的view为null，返回新的view ，否则，先将 保存的view从父view中移除，然后将该view返回出去
         */
        if (mView != null) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
            return mView;
        }
        mView = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_bid_chat,container,false);
        dataFlow = new DataFlow6(getActivity());
        initView();
        return mView;
    }

    private void initView() {
        zLoadingView = mView.findViewById(R.id.progress);
        zLoadingView.setLoadingHandler(this);
        mNoMessageLayout = mView.findViewById(R.id.no_message_layout);
        listView =  mView.findViewById(R.id.list);
        xrefresh = mView.findViewById(R.id.xrefresh);
        refreshAndloda();
    }

    private void refreshAndloda() {
        xrefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                topicpage = 1;
                x = 1;
                initData();
            }
        });
        xrefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                topicpage++;
                x = 2;
                initData();
            }
        });
    }

    private void initData() {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid",userID);
        maps.put("page", topicpage+"");
        RetrofitClient.getInstance(getActivity()).createBaseApi().querySysMessage(
                maps, new BaseObserver<String>(getActivity()) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            if (jsonObject.optString("status").equals("1")) {
                                pubaMessageBeans = JSON.parseArray(content, PubaMessageBean.class);
                                 if (x == 1){
                                     adapter = new BidMsgInformAdapter(getActivity(), pubaMessageBeans);
                                     listView.setAdapter(adapter);
                                 }else {
                                    if (content != null && !content.equals("[]")) {
                                        adapter.notifyData(pubaMessageBeans);
                                    } else {
                                        StringUtil.showToast(getActivity(), "没有更多了");
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    protected void hideDialog() {
                        listView.setVisibility(View.VISIBLE);
                        zLoadingView.loadSuccess();
                        xrefresh.finishLoadMore();
                        xrefresh.finishRefresh();
                    }

                    @Override
                    protected void showDialog() {
                        zLoadingView.load();
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        zLoadingView.loadError();
                        listView.setVisibility(View.GONE);
                        xrefresh.finishLoadMore();
                        xrefresh.finishRefresh();
                        StringUtil.showToast(getActivity(), "网络异常");
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        topicpage = 1;
        x = 1;
        initData();
    }

    @Override
    public void doRequestData() {
        initData();
    }
}
