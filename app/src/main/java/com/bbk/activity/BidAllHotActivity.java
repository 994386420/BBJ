package com.bbk.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.bbk.adapter.BidAllHotAdapter;
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
 * 全部热镖
 */
public class BidAllHotActivity extends BaseActivity implements ResultEvent {

    private DataFlow6 dataFlow;
    private ImageView topbar_goback_btn;
    private List<Map<String,String>> list;
    private ListView mlistview;
    private BidAllHotAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_all_hot);
        dataFlow = new DataFlow6(this);
        initView();
        initData();
    }
    public void initView(){
        list = new ArrayList<>();
        topbar_goback_btn= (ImageView) findViewById(R.id.topbar_goback_btn);
        mlistview= (ListView) findViewById(R.id.mlistview);
        topbar_goback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter = new BidAllHotAdapter(this,list);
        mlistview.setAdapter(adapter);
        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BidAllHotActivity.this, BidDetailActivity.class);
                intent.putExtra("id",list.get(position).get("id"));
                startActivity(intent);
            }
        });



    }



    public void initData(){
        HashMap<String, String> paramsMap = new HashMap<>();
        dataFlow.requestData(1, "bid/queryPushTwo", paramsMap, this,true);
    }

    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
        try {
            JSONArray array = new JSONArray(content);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                Map<String,String> map = new HashMap<>();
                map.put("endtime",object.optString("endtime"));
                map.put("id",object.optString("id"));
                map.put("imgs",object.optString("imgs"));
                map.put("title",object.optString("title"));
                map.put("price",object.optString("price"));
                map.put("extra",object.optString("extra"));
                map.put("number",object.optString("number"));
                map.put("type",object.optString("type"));
                list.add(map);
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
