package com.bbk.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.bbk.adapter.BidDetailListAdapter;
import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.util.ImmersedStatusbarUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发镖_04_记录
 */
public class BidHistoryActivity extends BaseActivity implements ResultEvent{
    private ListView mlistview;
    private BidDetailListAdapter adapter;
    private List<Map<String,String>> list;
    private String fbid;
    private DataFlow6 dataFlow;
    private String type;
    private ImageView topbar_goback_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_history);
        View topView = findViewById(R.id.topbar_layout);
        // 实现沉浸式状态栏
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        dataFlow = new DataFlow6(this);
        fbid = getIntent().getStringExtra("fbid");
//        type = getIntent().getStringExtra("type");
        initView();
        initData();
    }

    private void initView() {
        list = new ArrayList<>();
        topbar_goback_btn= (ImageView) findViewById(R.id.topbar_goback_btn);
        topbar_goback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mlistview = (ListView)findViewById(R.id.mlistview);
        adapter = new BidDetailListAdapter(this,list);
        mlistview.setAdapter(adapter);
        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BidHistoryActivity.this,BidFilterPriceActivity.class);
                intent.putExtra("bidid",list.get(position).get("bidid"));
                intent.putExtra("fbid",fbid);
                startActivity(intent);
            }
        });
    }
    private void initData() {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("fbid",fbid);
        dataFlow.requestData(1, "bid/queryJiePerson", paramsMap, this,true);
    }
    public void addList(JSONArray bidarr) throws JSONException {
        for (int i = 0; i < bidarr.length(); i++) {
            JSONObject object = bidarr.getJSONObject(i);
            Map<String,String> map = new HashMap<>();
            map.put("bidid",object.optString("bidid"));
            map.put("biduser",object.optString("biduser"));
            map.put("biddesc",object.optString("biddesc"));
            map.put("bidprice",object.optString("bidprice"));
            map.put("bidtime",object.optString("bidtime"));
            map.put("bidurl",object.optString("bidurl"));
            map.put("biduserid",object.optString("biduserid"));
            map.put("bidtime",object.optString("bidtime"));
            map.put("userid",object.optString("userid"));
            list.add(map);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
        try {
            JSONArray array = new JSONArray(content);
            addList(array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
