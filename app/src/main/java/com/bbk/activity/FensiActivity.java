package com.bbk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.FensiBean;
import com.bbk.Bean.InvitedBean;
import com.bbk.adapter.FenSiAdapter;
import com.bbk.adapter.FenSiInvitedAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.CommonLoadingView;
import com.logg.Logg;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 收益报表
 */
public class FensiActivity extends BaseActivity implements CommonLoadingView.LoadingHandler {

    String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
    @BindView(R.id.title_back_btn)
    ImageButton titleBackBtn;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.fensi_recyclerview)
    RecyclerView fensiRecyclerview;
    @BindView(R.id.fensi_layout)
    LinearLayout fensiLayout;
    @BindView(R.id.progress)
    CommonLoadingView progress;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    int page = 1, x = 1;
    List<FensiBean> fensiBeans;
    List<InvitedBean> invitedBeans;
    FenSiAdapter fenSiAdapter;
    FenSiInvitedAdapter fenSiInvitedAdapter;
    @BindView(R.id.friends_num)
    TextView friendsNum;
    @BindView(R.id.ll_yaoqing)
    LinearLayout llYaoqing;
    @BindView(R.id.fensi_recyclerview1)
    RecyclerView fensiRecyclerview1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fensi_layout);
        View topView = findViewById(R.id.topbar_layout);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        ButterKnife.bind(this);
        titleText.setText("好友红包");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }

        };
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }

        };
        fensiRecyclerview.setLayoutManager(linearLayoutManager);
        fensiRecyclerview.setHasFixedSize(true);
        fensiRecyclerview1.setLayoutManager(linearLayoutManager1);
        fensiRecyclerview1.setHasFixedSize(true);
        progress.setLoadingHandler(this);
        refreshAndloda();
        queryUserBrokerage();
    }

    private void refreshAndloda() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                page = 1;
                x = 1;
                queryUserBrokerage();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                page++;
                x = 2;
                queryUserBrokerage();
            }
        });
    }

    /**
     * 查询返利金币列表 （页面显示状态：0为未领1为已领）
     */
    private void queryUserBrokerage() {
        refreshLayout.setNoMoreData(false);
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        maps.put("page", page + "");
        RetrofitClient.getInstance(this).createBaseApi().querySignFanLi(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            JSONObject object = new JSONObject(content);
                            Logg.json(object.optString("arr2"));
                            if (jsonObject.optString("status").equals("1")) {
                                refreshLayout.setEnableLoadMore(true);
                                friendsNum.setText("共" + object.optString("count") + "位好友");
                                invitedBeans = JSON.parseArray(object.optString("arr2"), InvitedBean.class);
                                if (x == 1) {
                                    fensiBeans = JSON.parseArray(object.optString("arr"), FensiBean.class);
                                    if (fensiBeans.size() > 0 && fensiBeans != null) {
                                        fenSiAdapter = new FenSiAdapter(FensiActivity.this, fensiBeans);
                                        fensiRecyclerview.setAdapter(fenSiAdapter);
                                    } else {
                                        progress.setVisibility(View.VISIBLE);
                                        progress.loadSuccess(true);
                                        refreshLayout.setEnableLoadMore(false);
                                    }
                                    if (invitedBeans.size() > 0 && invitedBeans != null) {
                                        fenSiInvitedAdapter = new FenSiInvitedAdapter(FensiActivity.this, invitedBeans);
                                        fensiRecyclerview1.setAdapter(fenSiInvitedAdapter);
                                    } else {
                                        progress.setVisibility(View.VISIBLE);
                                        progress.loadSuccess(true);
                                        refreshLayout.setEnableLoadMore(false);
                                    }
                                } else {
                                    if (invitedBeans.size() > 0 && invitedBeans!= null) {
                                        fenSiInvitedAdapter.notifyData(invitedBeans);
                                    } else {
                                        refreshLayout.finishLoadMoreWithNoMoreData();
                                    }
                                }

                            } else {
                                StringUtil.showToast(FensiActivity.this, jsonObject.optString("errmsg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        DialogSingleUtil.dismiss(0);
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(FensiActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        progress.setVisibility(View.VISIBLE);
                        progress.loadError();
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        StringUtil.showToast(FensiActivity.this, e.message);
                    }
                });
    }

    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void doRequestData() {
        progress.setVisibility(View.GONE);
        queryUserBrokerage();
    }

    @OnClick({R.id.title_back_btn, R.id.ll_yaoqing})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_btn:
                finish();
                break;
            case R.id.ll_yaoqing:
//                Intent intent = new Intent(FensiActivity.this, CoinGoGoGoActivity.class);
//                intent.putExtra("type", "1");
                Intent intent = new Intent(FensiActivity.this, YaoqingFriendsActivity.class);
                startActivity(intent);
                break;
        }
    }
}
