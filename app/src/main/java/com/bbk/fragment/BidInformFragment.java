package com.bbk.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.andview.refreshview.XRefreshView;
import com.bbk.activity.BidBillDetailActivity;
import com.bbk.activity.BidMyBillDetailActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.adapter.BidMsgInformAdapter;
import com.bbk.flow.DataFlow;
import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.MyFootView;

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

public class BidInformFragment extends Fragment implements ResultEvent {

    private View mView;
    private DataFlow6 dataFlow;
    private ListView listView;
    private List<Map<String,String>> list;
    private BidMsgInformAdapter adapter;
    private XRefreshView xrefresh;
    private int topicpage = 1;
    private boolean isclear = false;
    private boolean isloadmore = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container
            , Bundle savedInstanceState) {

        mView = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_bid_chat, null);
        dataFlow = new DataFlow6(getActivity());
        initView();
        initData(true);
        return mView;
    }

    private void initView() {
        list = new ArrayList<>();
        listView =  mView.findViewById(R.id.list);
        xrefresh = mView.findViewById(R.id.xrefresh);
        refreshAndloda();
    }

    private void refreshAndloda() {
        xrefresh.setXRefreshViewListener(new XRefreshView.XRefreshViewListener() {

            @Override
            public void onRelease(float direction) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onRefresh(boolean isPullDown) {
                    isclear = true;
                    topicpage = 1;
                    initData(true);
            }

            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onLoadMore(boolean isSilence) {
                    topicpage++;
                    initData(true);
            }

            @Override
            public void onHeaderMove(double headerMovePercent, int offsetY) {
                // TODO Auto-generated method stub

            }
        });
        MyFootView footView = new MyFootView(getActivity());
        xrefresh.setCustomFooterView(footView);
    }
    private void readSysmsg(String id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("msgid",id);
        dataFlow.requestData(2, "bid/readSysmsg", paramsMap, this,true);
    }
    private void initData(boolean is) {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userid",userID);
        paramsMap.put("page", topicpage+"");
        dataFlow.requestData(1, "bid/querySysMessage", paramsMap, this,is);
    }
    public void addList(JSONArray array) throws JSONException {
        if (array.length()<10) {
            xrefresh.setPullLoadEnable(false);
        }else{
            xrefresh.setPullLoadEnable(true);
        }
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            Map<String,String> map = new HashMap<>();
            map.put("id",object.optString("id"));
            map.put("roledesc",object.optString("roledesc"));
            map.put("title",object.optString("title"));
            map.put("isread",object.optString("isread"));
            map.put("extra",object.optString("extra"));
            map.put("event",object.optString("event"));
            map.put("userid",object.optString("userid"));
            map.put("role",object.optString("role"));
            map.put("fbid",object.optString("fbid"));
            map.put("head",object.optString("head"));
            map.put("dtimes",object.optString("dtimes"));
            list.add(map);
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {
            adapter = new BidMsgInformAdapter(getActivity(), list);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Map<String, String> map = list.get(position);
                    String role = map.get("role");
                    Intent intent;
                    if ("1".equals(role)) {
                        intent = new Intent(getActivity(), BidBillDetailActivity.class);
                    } else {
                        intent = new Intent(getActivity(), BidMyBillDetailActivity.class);
                    }
                    intent.putExtra("fbid", map.get("fbid"));
                    startActivity(intent);
                    readSysmsg(map.get("id"));
                }
            });
        }
        isclear =false;
    }

    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
        xrefresh.stopLoadMore();
        xrefresh.stopRefresh();
            switch (requestCode){
                case 1:
                    try {
                    if (isclear) {
                        list.clear();
                    }
                    JSONArray array = new JSONArray(content);
                    addList(array);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        isclear = true;
        topicpage = 1;
        initData(false);
    }
}
