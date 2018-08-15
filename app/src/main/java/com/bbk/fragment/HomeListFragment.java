package com.bbk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.NewHomeCzgBean;
import com.bbk.activity.R;
import com.bbk.adapter.NewCzgAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.resource.NewConstants;
import com.bbk.util.DialogHomeUtil;
import com.bbk.util.StringUtil;
import com.logg.Logg;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeListFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.mrecycler)
    RecyclerView mrecycler;
    @BindView(R.id.refresh_root)
    SmartRefreshLayout refreshRoot;
    private List<String> urlList = new ArrayList<>();
    //    private TextView title;
//    private List<ImageView> imageList = new ArrayList<>();
    private int page = 1,x = 1;
    NewCzgAdapter newCzgAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        view.setScaleY(0.7f);
//        view.setScaleX(0.7f);
        Bundle bundle = getArguments();
        String title = bundle.getString("title");
        for (int i = 0; i < 5; i++) {
            urlList.add(bundle.getString("pic" + i));
        }
//        imageList.add(((ImageView) view.findViewById(R.id.pic4)));
//        for (int i = 0; i < imageList.size(); i++) {
//            Glide.with(getActivity())
//                    .load(urlList.get(i))
//                    .priority(Priority.HIGH)
//                    .placeholder(R.mipmap.tuiguang34)
//                    .into(imageList.get(i));
//        }
        mrecycler.setHasFixedSize(true);
//        mrecyclerview.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };
        mrecycler.setLayoutManager(linearLayoutManager);
        initDataCzg("");
        refreshRoot.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                        page++;
                        x = 2;
                        initDataCzg("");

            }
        });
    }


    /**
     * 超值购数据请求
     */
    private void initDataCzg(String keyword) {
        Logg.e("----------------刷新了");
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("keyword", keyword);
        paramsMap.put("sortWay", "5");
        paramsMap.put("page", page + "");
        paramsMap.put("client", "android");
        paramsMap.put("domain", "");
        RetrofitClient.getInstance(getActivity()).createBaseApi().getPageListChaozhigou(
                paramsMap, new BaseObserver<String>(getActivity()) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            JSONObject jo = new JSONObject(content);
                            String isBlandCzg = jo.optString("isBland");
                            if (jsonObject.optString("status").equals("1")) {
                                if (isBlandCzg.equals("1")) {
                                    JSONObject info = jo.getJSONObject("info");
                                    String tmpCzg = info.optString("page");
                                    NewConstants.Flag = "3";
                                    List<NewHomeCzgBean> czgBeans = JSON.parseArray(tmpCzg, NewHomeCzgBean.class);
                                    if (x == 1) {
                                        if (czgBeans != null && czgBeans.size() > 0) {
                                            refreshRoot.setEnableLoadMore(true);
//                                            mrecyclerview.setVisibility(View.GONE);
                                    newCzgAdapter = new NewCzgAdapter(getActivity(), czgBeans);
                                    mrecycler.setAdapter(newCzgAdapter);
                                        } else {
                                            refreshRoot.setEnableLoadMore(false);
//                                            mrecyclerview.setVisibility(View.GONE);
                                        }
                                    } else if (x == 2) {
//                                        mrecyclerview.setVisibility(View.GONE);
//                                        czgBeans.clear();
                                        if (tmpCzg != null && !tmpCzg.toString().equals("[]")) {
                                            czgBeans = JSON.parseArray(tmpCzg, NewHomeCzgBean.class);
                                            newCzgAdapter.notifyData(czgBeans);
                                        } else {
                                            refreshRoot.finishLoadMoreWithNoMoreData();
                                        }
                                    }
                                } else if (isBlandCzg.equals("-1") && x == 2 && NewConstants.Flag.equals("3")) {
                                    refreshRoot.finishLoadMoreWithNoMoreData();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        DialogHomeUtil.dismiss(0);
                        refreshRoot.finishLoadMore();
                        refreshRoot.finishRefresh();
                    }

                    @Override
                    protected void showDialog() {
//                        DialogSingleUtil.show(getActivity());
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogHomeUtil.dismiss(0);
                        StringUtil.showToast(getActivity(), e.message);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
