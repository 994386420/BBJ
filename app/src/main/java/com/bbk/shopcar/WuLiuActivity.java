package com.bbk.shopcar;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.NewHomeCzgBean;
import com.bbk.Bean.WuliuBean;
import com.bbk.Bean.WuliuItemBean;
import com.bbk.activity.BaseActivity;
import com.bbk.activity.JumpDetailActivty;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.adapter.NewCzgGridAdapter;
import com.bbk.adapter.WuliuAdapter;
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
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 物流
 */
public class WuLiuActivity extends BaseActivity {
    String expressage;
    @BindView(R.id.title_back_btn)
    ImageButton titleBackBtn;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tv_dh)
    TextView tvDh;
    @BindView(R.id.tv_gs)
    TextView tvGs;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.listview_wuliu)
    RecyclerView listviewWuliu;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.tv_kefu)
    TextView tvKefu;
    @BindView(R.id.progress)
    CommonLoadingView progress;
    @BindView(R.id.rl_guess_like)
    RelativeLayout rlGuessLike;
    @BindView(R.id.guess_like_list)
    RecyclerView guessLikeList;
    List<NewHomeCzgBean> czgBeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wuliu_layout);
        ButterKnife.bind(this);
        View topView = findViewById(R.id.topbar_layout);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        initVeiw();
        if (getIntent().getStringExtra("expressnum") != null) {
            expressage = getIntent().getStringExtra("expressnum");
            queryMyLogistics(expressage);
        }
    }

    private void initVeiw() {
        titleText.setText("订单追踪");
        refresh.setEnableLoadMore(false);
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                if (expressage != null) {
                    queryMyLogistics(expressage);
                }
            }
        });
    }

    /**
     * 查询物流
     * @param expressnum
     */
    private void queryMyLogistics(String expressnum) {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        maps.put("expressnum", expressnum);
        RetrofitClient.getInstance(this).createBaseApi().queryMyLogistics(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            if (jsonObject.optString("status").equals("1")) {
                                Logg.json(jsonObject);
                                WuliuBean wuliuBean = JSON.parseObject(content, WuliuBean.class);
                                tvDh.setText("运单号：" + wuliuBean.getExpressNum());
                                if ( wuliuBean.getCompany() != null && !wuliuBean.getCompany().equals("")) {
                                    tvGs.setText("承运公司：" + wuliuBean.getCompany());
                                    tvGs.setVisibility(View.VISIBLE);
                                }else {
                                    tvGs.setVisibility(View.GONE);
                                }
                                tvPhone.setText("承运电话：" + wuliuBean.getPhone());
                                if (wuliuBean.getList() != null) {
                                    List<WuliuItemBean> wuliuItemBeans = JSON.parseArray(wuliuBean.getList(), WuliuItemBean.class);
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(WuLiuActivity.this, OrientationHelper.VERTICAL, false);
                                    if (wuliuItemBeans != null && wuliuItemBeans.size() > 0) {
                                        WuliuAdapter mAdapter = new WuliuAdapter(WuLiuActivity.this, wuliuItemBeans);
                                        listviewWuliu.setLayoutManager(layoutManager);
                                        listviewWuliu.setNestedScrollingEnabled(false);
                                        listviewWuliu.setAdapter(mAdapter);
                                    } else {
                                        listviewWuliu.setVisibility(View.GONE);
                                        progress.setVisibility(View.VISIBLE);
                                        progress.loadSuccess(true, "暂无物流信息~");
                                        refresh.setEnableLoadMore(false);
                                    }
                                }else {
                                    listviewWuliu.setVisibility(View.GONE);
                                    progress.setVisibility(View.VISIBLE);
                                    progress.loadSuccess(true, "暂无物流信息~");
                                    refresh.setEnableLoadMore(false);
                                }
                                if (wuliuBean.getLikelist() != null && !wuliuBean.getLikelist().equals("")){
                                    czgBeans = JSON.parseArray(wuliuBean.getLikelist(), NewHomeCzgBean.class);
                                    //禁用滑动事件
                                    guessLikeList.setNestedScrollingEnabled(false);
                                    guessLikeList.setLayoutManager(new GridLayoutManager(WuLiuActivity.this, 2));
                                    guessLikeList.setAdapter(new NewCzgGridAdapter(WuLiuActivity.this, czgBeans));
                                }else {
                                    getIndexByType("","");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        DialogSingleUtil.dismiss(0);
                        refresh.finishRefresh();
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(WuLiuActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        refresh.finishRefresh();
                        StringUtil.showToast(WuLiuActivity.this, e.message);
                    }
                });
    }


    /**
     * 猜你喜欢
     * @param rowkey
     */
    private void getIndexByType(String rowkey, String title) {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("type", "1");
        maps.put("page", 1 + "");
        maps.put("rowkey", rowkey);
        maps.put("title", title);
        maps.put("userid", userID);
        RetrofitClient.getInstance(this).createBaseApi().queryAppIndexByType(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                String content = jsonObject.optString("content");
                                czgBeans = JSON.parseArray(content, NewHomeCzgBean.class);
                                //禁用滑动事件
                                guessLikeList.setNestedScrollingEnabled(false);
                                guessLikeList.setLayoutManager(new GridLayoutManager(WuLiuActivity.this, 2));
                                guessLikeList.setAdapter(new NewCzgGridAdapter(WuLiuActivity.this, czgBeans));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        DialogSingleUtil.dismiss(0);
                        refresh.finishRefresh();
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(WuLiuActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        refresh.finishRefresh();
                        StringUtil.showToast(WuLiuActivity.this, e.message);
                    }
                });
    }
    @OnClick({R.id.title_back_btn, R.id.tv_kefu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_btn:
                finish();
                break;
            case R.id.tv_kefu:
                break;
        }
    }
}
