package com.bbk.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bbk.Decoration.TwoDecoration2;
import com.bbk.activity.BidAcceptanceActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.UserLoginNewActivity;
import com.bbk.adapter.BidHomeAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.flow.DataFlow6;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.HomeLoadUtil;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 发镖首页
 */

public class BidHomeFragment extends BaseViewPagerFragment implements View.OnClickListener, CommonLoadingView.LoadingHandler {
    @BindView(R.id.img_more_black)
    ImageView imgMoreBlack;
    Unbinder unbinder;
    private DataFlow6 dataFlow;
    private View mView;
    private RecyclerView mrecyclerview;
    private List<Map<String, String>> list;
    private BidHomeAdapter adapter;
    private JSONArray list3array = new JSONArray();
    private JSONArray list4array = new JSONArray();
    private View mzhuangtai;
    private SmartRefreshLayout mrefresh;
    private boolean isclear = true;
    private ImageView topbar_goback_btn;
    private LinearLayout ll_search_layout;
    private CommonLoadingView zLoadingView;//加载框


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (null == mView) {
            dataFlow = new DataFlow6(getActivity());
            getActivity().getWindow().setBackgroundDrawable(null);
            mView = inflater.inflate(R.layout.fragment_bid_home, null);
            initView();
            mzhuangtai = mView.findViewById(R.id.mzhuangtai);
            initData();
        }
        unbinder = ButterKnife.bind(this, mView);
        return mView;
    }

    public void initView() {
        zLoadingView = mView.findViewById(R.id.progress);
        zLoadingView.setLoadingHandler(this);
        ll_search_layout = mView.findViewById(R.id.search_layout);
        ll_search_layout.setOnClickListener(this);
        list = new ArrayList<>();
        topbar_goback_btn = (ImageView) mView.findViewById(R.id.topbar_goback_btn);
        topbar_goback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        mrecyclerview = (RecyclerView) mView.findViewById(R.id.mrecyclerview);
        mrefresh = (SmartRefreshLayout) mView.findViewById(R.id.mrefresh);
        refreshAndloda();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position < 2 + list3array.length() ? 2 : 1;
            }
        });
        mrecyclerview.setLayoutManager(gridLayoutManager);
        mrecyclerview.addItemDecoration(new TwoDecoration2(10, "#f3f3f3", 3));
        mrecyclerview.setHasFixedSize(true);
        adapter = new BidHomeAdapter(getActivity(), list);
        mrecyclerview.setAdapter(adapter);
    }

    public void initData() {
//        mrefresh.setNoMoreData(false);
        mrefresh.finishLoadMoreWithNoMoreData();
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        RetrofitClient.getInstance(getActivity()).createBaseApi().queryIndex(
                maps, new BaseObserver<String>(getActivity()) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (isclear) {
                                list.clear();
                            }
                            Map<String, String> map = new HashMap<>();
                            JSONObject json = new JSONObject(jsonObject.optString("content"));
                            String list1 = json.optString("list1");
                            String list2 = json.optString("list2");
                            String list3 = json.optString("list3");
                            String list4 = json.optString("list4");
                            list3array = new JSONArray(list3);
                            list4array = new JSONArray(list4);
                            map.put("list1", list1);
                            map.put("list2", list2);
                            map.put("list3", list3);
                            map.put("list4", list4);
                            list.add(map);
                            adapter.notifyDataSetChanged();
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
                        StringUtil.showToast(getActivity(), e.message);
                    }
                });
    }

    private void refreshAndloda() {
        mrefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                isclear = true;
                initData();
            }
        });
        mrefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                initData();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_layout:
                Intent intent = new Intent(getActivity(), BidAcceptanceActivity.class);
                intent.putExtra("type", "服饰");
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void loadLazyData() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        StringUtil.cancelToast();
    }

    @Override
    public void doRequestData() {
        zLoadingView.setVisibility(View.GONE);
        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.img_more_black)
    public void onViewClicked() {
        Intent intent;
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        if (TextUtils.isEmpty(userID)) {
            intent = new Intent(getActivity(), UserLoginNewActivity.class);
            startActivityForResult(intent, 1);
        } else {
            HomeLoadUtil.showItemPop(getActivity(), imgMoreBlack);
        }
    }
}
