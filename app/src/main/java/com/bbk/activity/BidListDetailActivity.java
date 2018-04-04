package com.bbk.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.andview.refreshview.XRefreshView;
import com.bbk.adapter.BidListDetailAdapter;
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
 * 发镖_02_列表
 */
public class BidListDetailActivity extends BaseActivity implements ResultEvent {

    private TabLayout tablayout;
    private ListView mlistview;
    private XRefreshView xrefresh;
    private String userID;
    private List<Map<String, String>> list;
    private int status = -1;
    private boolean isclear = true;
    private int page = 1;
    private BidListDetailAdapter adapter;
    private DataFlow6 dataFlow;
    private ImageView topbar_goback_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_list_detail);
        dataFlow = new DataFlow6(this);
        initVeiw();
        initData();
    }
    private void initVeiw() {
        list = new ArrayList<>();
        topbar_goback_btn= (ImageView) findViewById(R.id.topbar_goback_btn);
        topbar_goback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");

        mlistview = (ListView) findViewById(R.id.mlistview);
        tablayout = (TabLayout) findViewById(R.id.tablayout);
        xrefresh = (XRefreshView) findViewById(R.id.xrefresh);
        tablayout.addTab(tablayout.newTab().setText("全部"));
        tablayout.addTab(tablayout.newTab().setText("待审核"));
        tablayout.addTab(tablayout.newTab().setText("待接镖"));
        tablayout.addTab(tablayout.newTab().setText("待评论"));
        tablayout.addTab(tablayout.newTab().setText("完成"));
        tablayout.setTabMode(TabLayout.MODE_FIXED);
        refreshAndloda();
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int j = tab.getPosition();
                if (j==0){
                    status = -1;
                }else if(j==1){
                    status = 0;
                }else if(j==2){
                    status = 1;
                }else if(j==3){
                    status = 2;
                }else if(j==4){
                    status =3;
                }
                isclear = true;
               loadData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BidListDetailActivity.this,BidBillDetailActivity.class);
                intent.putExtra("fbid",list.get(position).get("id"));
                startActivity(intent);
            }
        });

    }

    private void initData() {
        adapter = new BidListDetailAdapter(this,list);
        mlistview.setAdapter(adapter);
        if (getIntent().getStringExtra("status")!=null) {
            String status1 = getIntent().getStringExtra("status");
            int i = Integer.valueOf(status1);
            TabLayout.Tab tabAt = tablayout.getTabAt(i);
            tabAt.select();
        }else {
            loadData();
        }
    }

    private void loadData() {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("status",status+"");
        paramsMap.put("userid", userID);
        paramsMap.put("page", page+"");
        dataFlow.requestData(1, "bid/queryBidByStatus", paramsMap, this,true);
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
                page = 1;
                initData();

            }

            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onLoadMore(boolean isSilence) {
                page++;
                initData();
            }

            @Override
            public void onHeaderMove(double headerMovePercent, int offsetY) {
                // TODO Auto-generated method stub

            }
        });
        MyFootView footView = new MyFootView(this);
        xrefresh.setCustomFooterView(footView);
    }

    public void addList(JSONArray array) throws JSONException {
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            Map<String,String> map = new HashMap<>();
            map.put("endtime",object.optString("endtime"));
            map.put("id",object.optString("id"));
            map.put("title",object.optString("title"));
            map.put("price",object.optString("price"));
            map.put("status",object.optString("status"));
            map.put("extra",object.optString("extra"));
            map.put("img",object.optString("img"));
            map.put("number",object.optString("number"));
            map.put("bidnum",object.optString("bidnum"));
            map.put("url",object.optString("url"));
            map.put("finalprice",object.optString("finalprice"));
            list.add(map);
        }

    }
    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
        xrefresh.stopRefresh();
        xrefresh.stopLoadMore();
        try {
            switch (requestCode){
                case 1:
                    if (isclear){
                        list.clear();
                    }
                    JSONArray array = new JSONArray(content);
                    addList(array);
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
