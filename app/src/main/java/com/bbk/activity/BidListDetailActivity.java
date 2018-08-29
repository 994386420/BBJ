package com.bbk.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.andview.refreshview.XRefreshView;
import com.bbk.Bean.BiaoLiaoBean;
import com.bbk.Bean.PubaMessageBean;
import com.bbk.Bean.WoYaoBean;
import com.bbk.adapter.BidAcceptanceAdapter;
import com.bbk.adapter.BidListDetailAdapter;
import com.bbk.adapter.BidMsgInformAdapter;
import com.bbk.adapter.GossipPiazzaAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.ImmersedStatusbarUtils;
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

import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的发镖
 */
public class BidListDetailActivity extends BaseActivity implements CommonLoadingView.LoadingHandler {

    private TabLayout tablayout;
    private ListView mlistview;
    private SmartRefreshLayout xrefresh;
    private String userID;
    private int status = -1;
    private boolean isclear = true;
    private int page = 1,  x = 1;
    private BidListDetailAdapter adapter;
    private ImageView topbar_goback_btn;
    private TextView mTitle;
    private List<WoYaoBean> woYaoBeans;
    private CommonLoadingView zLoadingView;//加载框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_list_detail);
        View topView = findViewById(R.id.topbar_layout);
        // 实现沉浸式状态栏
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        initVeiw();
        initData();
    }
    private void initVeiw() {
        zLoadingView = findViewById(R.id.progress);
        zLoadingView.setLoadingHandler(this);
        mTitle = findViewById(R.id.title);
        mTitle.setText("我发的飙");
        topbar_goback_btn= findViewById(R.id.topbar_goback_btn);
        topbar_goback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
        mlistview = findViewById(R.id.mlistview);
        tablayout =  findViewById(R.id.tablayout);
        xrefresh = findViewById(R.id.xrefresh);
        tablayout.addTab(tablayout.newTab().setText("全部"));
        tablayout.addTab(tablayout.newTab().setText("待审核"));
        tablayout.addTab(tablayout.newTab().setText("待接单"));
        tablayout.addTab(tablayout.newTab().setText("待评论"));
        tablayout.addTab(tablayout.newTab().setText("完成"));
        tablayout.setTabMode(TabLayout.MODE_FIXED);
        refreshAndloda();
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int j = tab.getPosition();
                if (j==0){
                    status = -1;
                }else if(j==1){
                    status = 0;
                }else if(j==2){
                    status = 1;
                }else if(j==3){
                    status = 2;
                }else if(j==4){
                    status =3;
                }
                page = 1;
                x= 1;
                loadData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void initData() {
        if (getIntent().getStringExtra("status")!=null) {
            String status1 = getIntent().getStringExtra("status");
            int i = Integer.valueOf(status1);
            TabLayout.Tab tabAt = tablayout.getTabAt(i);
            tabAt.select();
        }else {
            loadData();
        }
    }

    private void loadData() {
        xrefresh.setNoMoreData(false);
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("status",status+"");
        maps.put("userid", userID);
        maps.put("page", page+"");
        RetrofitClient.getInstance(this).createBaseApi().queryBidByStatus(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            if (jsonObject.optString("status").equals("1")) {
                                woYaoBeans = JSON.parseArray(content, WoYaoBean.class);
                                DialogSingleUtil.dismiss(0);
                                if (x == 1){
                                    if (woYaoBeans != null && woYaoBeans.size() > 0) {
                                        xrefresh.setEnableLoadMore(true);
                                        adapter = new BidListDetailAdapter(BidListDetailActivity.this, woYaoBeans);
                                        mlistview.setAdapter(adapter);
                                        mlistview.setVisibility(View.VISIBLE);
                                        zLoadingView.loadSuccess();
                                    }else {
                                        zLoadingView.setVisibility(View.VISIBLE);
                                        mlistview.setVisibility(View.GONE);
                                        zLoadingView.loadSuccess(true);
                                        xrefresh.setEnableLoadMore(false);
                                    }
                                }else {
                                    mlistview.setVisibility(View.VISIBLE);
                                    zLoadingView.loadSuccess();
                                    if (woYaoBeans != null && woYaoBeans.size() > 0) {
                                        adapter.notifyData(woYaoBeans);
                                    } else {
//                                        StringUtil.showToast(BidListDetailActivity.this, "没有更多了");
                                        xrefresh.finishLoadMoreWithNoMoreData();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    protected void hideDialog() {
                        xrefresh.finishLoadMore();
                        xrefresh.finishRefresh();
                    }

                    @Override
                    protected void showDialog() {
//                        zLoadingView.load();
                        DialogSingleUtil.show(BidListDetailActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        zLoadingView.setVisibility(View.VISIBLE);
                        zLoadingView.loadError();
                        mlistview.setVisibility(View.GONE);
                        xrefresh.finishLoadMore();
                        xrefresh.finishRefresh();
                        StringUtil.showToast(BidListDetailActivity.this, e.message);
                    }
                });
    }

    private void refreshAndloda() {
        xrefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                page = 1;
                x = 1;
                loadData();
            }
        });
        xrefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                page++;
                x = 2;
                loadData();
            }
        });
    }

    @Override
    public void doRequestData() {
        zLoadingView.setVisibility(View.GONE);
        page = 1;
        x = 1;
        loadData();
    }
}
