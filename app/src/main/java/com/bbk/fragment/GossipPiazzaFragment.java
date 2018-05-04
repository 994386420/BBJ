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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.bbk.activity.GossipPiazzaDetailActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.MyGossipActivity;
import com.bbk.activity.R;
import com.bbk.activity.UserLoginNewActivity;
import com.bbk.activity.WebViewActivity;
import com.bbk.activity.WebViewWZActivity;
import com.bbk.adapter.GossipPiazzaAdapter;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.fragment.BaseViewPagerFragment;
import com.bbk.resource.Constants;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.HeaderView;
import com.bbk.view.MyFootView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
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
public class GossipPiazzaFragment extends BaseViewPagerFragment implements ResultEvent {
    private DataFlow dataFlow;
    private XRefreshView mrefresh;
    private RecyclerView mrecyclerview;
    private GossipPiazzaAdapter adapter;
    private List<Map<String, String>> list;
    private int page = 1;
    private boolean isclear = false;
    private View mView;
    private View data_head;
    private FloatingActionButton float_btn;
    private TextView mchongshi;

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
            dataFlow = new DataFlow(getActivity());
            data_head = mView.findViewById(R.id.data_head);
            initstateView();
            initView();
        }
        return mView;
    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_gossip_piazza);
//        dataFlow = new DataFlow(this);
//        initView();
//        initData();
//    }

    private void initData(boolean is) {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userid", userID);
        paramsMap.put("page", page + "");
        dataFlow.requestData(1, "newService/queryBaoliaoMessage", paramsMap, this, is);
    }

    private void initView() {
        list = new ArrayList<>();
        mrefresh =  mView.findViewById(R.id.mrefresh);
        mrecyclerview =  mView.findViewById(R.id.mrecyclerview);
        float_btn = mView.findViewById(R.id.float_btn);
        mrecyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        adapter = new GossipPiazzaAdapter(getActivity(), list);
        mrecyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(new GossipPiazzaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), GossipPiazzaDetailActivity.class);
                intent.putExtra("blid",list.get(position).get("blid"));
                startActivity(intent);
            }
        });
        mrefresh.setCustomHeaderView(new HeaderView(getActivity()));
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
        mrefresh.setXRefreshViewListener(new XRefreshView.XRefreshViewListener() {

            @Override
            public void onRelease(float direction) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onRefresh(boolean isPullDown) {
                isclear = true;
                page = 1;
                initData(true);

            }

            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onLoadMore(boolean isSilence) {
                page++;
                initData(true);
            }

            @Override
            public void onHeaderMove(double headerMovePercent, int offsetY) {
                // TODO Auto-generated method stub

            }
        });
        MyFootView footView = new MyFootView(getActivity());
        mrefresh.setCustomFooterView(footView);
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
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
        mrefresh.stopLoadMore();
        mrefresh.stopRefresh();
        try {
                if (isclear) {
                    list.clear();
                }
                JSONArray array = new JSONArray(content);
                if (array.length() < 10) {
                    mrefresh.setPullLoadEnable(false);
                } else {
                    mrefresh.setPullLoadEnable(true);
                }
                for (int i = 0; i < array.length(); i++) {
                    Map<String, String> map = new HashMap<>();
                    JSONObject Object = array.getJSONObject(i);
                    map.put("content", Object.optString("content"));
                    map.put("imgs", Object.optString("imgs"));
                    map.put("username", Object.optString("username"));
                    map.put("title", Object.optString("title"));
                    map.put("headimg", Object.optString("headimg"));
                    map.put("dtime", Object.optString("dtime"));
                    map.put("isZan", Object.optString("isZan"));
                    map.put("zannum", Object.optString("zannum"));
                    map.put("readnum", Object.optString("readnum"));
                    map.put("url", Object.optString("url"));
                    map.put("plnum", Object.optString("plnum"));
                    map.put("blid", Object.optString("blid"));
                    if (Object.has("video")){
                        map.put("video", Object.optString("video"));
                    }else {
                        map.put("video", Object.optString("0"));
                    }
                    list.add(map);
                }
                adapter.notifyDataSetChanged();
                isclear = false;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void loadLazyData() {
        initData(true);
    }
}
