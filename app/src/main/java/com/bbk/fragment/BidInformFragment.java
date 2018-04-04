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
import com.bbk.activity.BidBillDetailActivity;
import com.bbk.activity.BidMyBillDetailActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.adapter.BidMsgInformAdapter;
import com.bbk.flow.DataFlow;
import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.util.SharedPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息_01_通知
 */

public class BidInformFragment extends Fragment implements ResultEvent {

    private View mView;
    private DataFlow6 dataFlow;
    private ListView listView;
    private List<Map<String,String>> list;
    private BidMsgInformAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container
            , Bundle savedInstanceState) {

        mView = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_bid_chat, null);
        dataFlow = new DataFlow6(getActivity());
        initView();
        initData();
        return mView;
    }

    private void initView() {
        list = new ArrayList<>();
        listView = (ListView) mView.findViewById(R.id.list);
        adapter = new BidMsgInformAdapter(getActivity(),list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> map = list.get(position);
                String role = map.get("role");
                Intent intent;
                if ("1".equals(role)){
                    intent = new Intent(getActivity(), BidBillDetailActivity.class);
                }else {
                    intent = new Intent(getActivity(), BidMyBillDetailActivity.class);
                }
                intent.putExtra("fbid",map.get("fbid"));
                startActivity(intent);
                readSysmsg(map.get("id"));
            }
        });
    }
    private void readSysmsg(String id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("msgid",id);
        dataFlow.requestData(2, "bid/readSysmsg", paramsMap, this,true);
    }
    private void initData() {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userid",userID);
        dataFlow.requestData(1, "bid/querySysMessage", paramsMap, this,false);
    }
    public void addList(JSONArray array) throws JSONException {
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
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
        try {
            switch (requestCode){
                case 1:
                    list.clear();
                    JSONArray array = new JSONArray(content);
                    addList(array);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }
}
