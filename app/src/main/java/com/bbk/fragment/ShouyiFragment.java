package com.bbk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.BrokerageDetailBean;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.adapter.BrokerageDetailAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.util.DialogSingleUtil;
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


/**
 * 收益fragment,展示数据
 */
public class ShouyiFragment extends BaseViewPagerFragment implements CommonLoadingView.LoadingHandler {
    @BindView(R.id.brokerage_detail_list)
    RecyclerView brokerageDetailList;
    @BindView(R.id.progress)
    CommonLoadingView progress;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.tv_tixian)
    TextView tvTixian;
    @BindView(R.id.tv_jinbi)
    TextView tvJinbi;
    @BindView(R.id.ll_tixian)
    LinearLayout llTixian;
    private String type = "", level = "",money = "";
    private int page = 1, x = 1;
    private BrokerageDetailAdapter brokerageDetailAdapter;
    private View mView;

    public static ShouyiFragment newInstance(String type, String level,String money) {
        Bundle bundle = new Bundle();
        ShouyiFragment fragment = new ShouyiFragment();
        bundle.putString("type", type);
        bundle.putString("level", level);
        bundle.putString("money", money);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString("type");
        level = getArguments().getString("level");
        money = getArguments().getString("money");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        mView = inflater.inflate(R.layout.brokerage_detail_layout, null);
        ButterKnife.bind(this, mView);
        brokerageDetailList.setLayoutManager(new LinearLayoutManager(getActivity()));
        brokerageDetailList.setHasFixedSize(true);
        progress.setLoadingHandler(this);
        refreshAndloda();
        return mView;
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
            public void onLoadMore(RefreshLayout refreshLayout) {
                x = 2;
                page++;
                queryUserBrokerage();
            }
        });
    }

    /**
     * 查询报表明细
     */
    private void queryUserBrokerage() {
        if (refreshLayout != null) {
            refreshLayout.setNoMoreData(false);
        }
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        maps.put("type", type);
        maps.put("page", page + "");
        maps.put("level", level);
        RetrofitClient.getInstance(getActivity()).createBaseApi().queryBrokerageDetail(
                maps, new BaseObserver<String>(getActivity()) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            if (jsonObject.optString("status").equals("1")) {
                                List<BrokerageDetailBean> brokerageDetailBean = JSON.parseArray(content, BrokerageDetailBean.class);
                                if (x == 1) {
                                    if (brokerageDetailBean != null && brokerageDetailBean.size() > 0) {
                                        refreshLayout.setEnableLoadMore(true);
                                        brokerageDetailAdapter = new BrokerageDetailAdapter(getActivity(), brokerageDetailBean);
                                        brokerageDetailList.setAdapter(brokerageDetailAdapter);
                                        brokerageDetailList.setVisibility(View.VISIBLE);
                                        progress.loadSuccess();
                                    } else {
                                        progress.setVisibility(View.VISIBLE);
                                        progress.loadSuccess(true);
                                        brokerageDetailList.setVisibility(View.GONE);
                                        refreshLayout.setEnableLoadMore(false);
                                    }
                                } else {
                                    if (brokerageDetailBean != null && brokerageDetailBean.size() > 0) {
                                        brokerageDetailAdapter.notifyData(brokerageDetailBean);
                                    } else {
                                        refreshLayout.finishLoadMoreWithNoMoreData();
                                    }
                                }
                            } else {
                                StringUtil.showToast(getActivity(), jsonObject.optString("errmsg"));
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
                        DialogSingleUtil.show(getActivity());
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        progress.setVisibility(View.VISIBLE);
                        progress.loadError();
                        brokerageDetailList.setVisibility(View.GONE);
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        StringUtil.showToast(getActivity(), e.message);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void doRequestData() {
        progress.setVisibility(View.GONE);
        queryUserBrokerage();
    }

    @Override
    protected void loadLazyData() {
        Logg.json(type + level);
        llTixian.setVisibility(View.VISIBLE);
        tvTixian.setText("全部合计 ");
        tvJinbi.setText(money+"元");
        queryUserBrokerage();
    }
}
