package com.bbk.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.andview.refreshview.XRefreshView;
import com.bbk.Decoration.TwoDecoration;
import com.bbk.Decoration.TwoDecoration2;
import com.bbk.activity.BidAcceptanceActivity;
import com.bbk.activity.BidDetailActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.adapter.BidHomeAdapter;
import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.HeaderView;
import com.bbk.view.MyFootView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发镖首页
 */

public class BidHomeFragment extends BaseViewPagerFragment implements View.OnClickListener, ResultEvent {
    private DataFlow6 dataFlow;
    private View mView;
    private RecyclerView mrecyclerview;
    private List<Map<String,String>> list;
    private BidHomeAdapter adapter;
    private JSONArray list3array = new JSONArray();
    private JSONArray list4array = new JSONArray();
    private View data_head;
    private View mzhuangtai;
    private XRefreshView mrefresh;
    private boolean isclear = true;
    private ImageView topbar_goback_btn;
    private LinearLayout ll_search_layout;


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
            View topView = mView.findViewById(R.id.lin1);
            // 实现沉浸式状态栏
            ImmersedStatusbarUtils.initAfterSetContentView(getActivity(), topView);
            initView();
            mzhuangtai = mView.findViewById(R.id.mzhuangtai);
            initstateView();
            initData();
        }
        return mView;
    }

    public void initView(){
        ll_search_layout = mView.findViewById(R.id.search_layout);
        ll_search_layout.setOnClickListener(this);
       list = new ArrayList<>();
        topbar_goback_btn = (ImageView)mView.findViewById(R.id.topbar_goback_btn);
        topbar_goback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        mrecyclerview = (RecyclerView)mView.findViewById(R.id.mrecyclerview);
        mrefresh = (XRefreshView) mView.findViewById(R.id.mrefresh);
        mrefresh.setCustomHeaderView(new HeaderView(getActivity()));
        refreshAndloda();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position < 2+list3array.length() ? 2 : 1;
            }
        });
        mrecyclerview.setLayoutManager(gridLayoutManager);
        mrecyclerview.addItemDecoration(new TwoDecoration2(10,"#f3f3f3",3));
        mrecyclerview.setHasFixedSize(true);
        adapter = new BidHomeAdapter(getActivity(),list);
        mrecyclerview.setAdapter(adapter);
    }
    public void initData(){
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userid",userID);
        dataFlow.requestData(1, "bid/queryIndex", paramsMap, this,true);
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
                initData();

            }

            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onLoadMore(boolean isSilence) {
                initData();
            }

            @Override
            public void onHeaderMove(double headerMovePercent, int offsetY) {
                // TODO Auto-generated method stub

            }
        });
        MyFootView footView = new MyFootView(getActivity());
        mrefresh.setCustomFooterView(footView);
    }
    // 状态栏高度
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

    // 沉浸式状态栏
    private void initstateView() {
        if (Build.VERSION.SDK_INT >= 19) {
            mzhuangtai.setVisibility(View.VISIBLE);
        }
        int result = getStatusBarHeight();
        ViewGroup.LayoutParams layoutParams = mzhuangtai.getLayoutParams();
        layoutParams.height = result;
        mzhuangtai.setLayoutParams(layoutParams);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_layout:
                Intent intent = new Intent(getActivity(), BidAcceptanceActivity.class);
                intent.putExtra("type","服饰");
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void loadLazyData() {

    }

    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
        try {
            mrefresh.stopLoadMore();
            mrefresh.stopRefresh();
            if (isclear){
                list.clear();
            }
            Map<String,String> map = new HashMap<>();
            JSONObject json = new JSONObject(content);
            String list1 = json.optString("list1");
            String list2 = json.optString("list2");
            String list3 = json.optString("list3");
            String list4 = json.optString("list4");
            list3array = new JSONArray(list3);
            list4array = new JSONArray(list4);
            map.put("list1",list1);
            map.put("list2",list2);
            map.put("list3",list3);
            map.put("list4",list4);
            list.add(map);
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
