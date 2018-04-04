package com.bbk.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bbk.adapter.BidDetailListPLAdapter;
import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评论_02_评论
 */
public class BidMyPlActivity extends BaseActivity implements ResultEvent {

    private DataFlow6 dataFlow;
    private String fbid;
    private int page = 1;
    private ListView mlistview;
    private BidDetailListPLAdapter adapter;
    private List<Map<String, String>> list;
    private TextView mpltext;
    private ImageView topbar_goback_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_my_pl);
        dataFlow = new DataFlow6(this);
        fbid = getIntent().getStringExtra("id");
//        fbid = "2";
        initView();
        initData();
    }

    public void initView() {
        list = new ArrayList<>();
        topbar_goback_btn= (ImageView) findViewById(R.id.topbar_goback_btn);
        topbar_goback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mlistview = (ListView) findViewById(R.id.mlistview);
        mpltext = (TextView) findViewById(R.id.mpltext);
        adapter = new BidDetailListPLAdapter(this, list);
        mlistview.setAdapter(adapter);
    }

    public void initData() {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("fbid", fbid);
        paramsMap.put("page", page + "");
        dataFlow.requestData(1, "bid/queryPinglun", paramsMap, this, true);
    }

    public void addPLList(JSONArray bidarr) throws JSONException {
        for (int i = 0; i < bidarr.length(); i++) {
            JSONObject object = bidarr.getJSONObject(i);
            Map<String, String> map = new HashMap<>();
            map.put("plcontent", object.optString("plcontent"));
            map.put("plrole", object.optString("plrole"));
            map.put("pluserid", object.optString("pluserid"));
            map.put("plid", object.optString("plid"));
            map.put("plstar", object.optString("plstar"));
            map.put("fbid", object.optString("fbid"));
            map.put("pldtime", object.optString("pldtime"));
            map.put("plhead", object.optString("plhead"));
            map.put("plusername", object.optString("plusername"));
            map.put("plimgs", object.optString("plimgs"));
            list.add(map);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
        try {
            JSONObject object = new JSONObject(content);
            mpltext.setText("评论("+object.optString("plcount")+")");
            JSONArray plarr = object.getJSONArray("plarr");
            addPLList(plarr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
