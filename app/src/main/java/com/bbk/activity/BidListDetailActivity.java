package com.bbk.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.bbk.adapter.BidAcceptanceAdapter;
import com.bbk.adapter.BidListDetailAdapter;
import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.HeaderView;
import com.bbk.view.MyFootView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的发镖
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
    private LinearLayout mNoMessageLayout;//无数据显示页面
    private RelativeLayout mNoNetWorkLayout;//链接异常页面
    private TextView mchongshi,mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_list_detail);
        View topView = findViewById(R.id.topbar_layout);
        // 实现沉浸式状态栏
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        dataFlow = new DataFlow6(this);
        initVeiw();
        initData();
    }
    private void initVeiw() {
        mNoMessageLayout = findViewById(R.id.no_message_layout);
        mNoNetWorkLayout = findViewById(R.id.mzhanwei_layout);
        mchongshi = findViewById(R.id.mchongshi);
        mchongshi.setOnClickListener(onClickListener);
        mTitle = findViewById(R.id.title);
        mTitle.setText("我的发镖");
        list = new ArrayList<>();
        topbar_goback_btn= findViewById(R.id.topbar_goback_btn);
        topbar_goback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
        mlistview = findViewById(R.id.mlistview);
        tablayout =  findViewById(R.id.tablayout);
        xrefresh = findViewById(R.id.xrefresh);
        tablayout.addTab(tablayout.newTab().setText("全部"));
        tablayout.addTab(tablayout.newTab().setText("待审核"));
        tablayout.addTab(tablayout.newTab().setText("待接镖"));
        tablayout.addTab(tablayout.newTab().setText("待评论"));
        tablayout.addTab(tablayout.newTab().setText("完成"));
        tablayout.setTabMode(TabLayout.MODE_FIXED);
        xrefresh.setCustomHeaderView(new HeaderView(this));
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
                page = 1;
                loadData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            isclear = true;
            page = 1;
            loadData();
        }
    };

    private void initData() {
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
                loadData();

            }

            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onLoadMore(boolean isSilence) {
                page++;
                loadData();
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
        if (array.length()<10) {
            xrefresh.setPullLoadEnable(false);
        }else{
            xrefresh.setPullLoadEnable(true);
        }
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
            if (adapter != null){
                if (list != null && list.size() > 0){
                    adapter.notifyDataSetChanged();
                    mlistview.setVisibility(View.VISIBLE);
                    mNoMessageLayout.setVisibility(View.GONE);
                }else {
                    mlistview.setVisibility(View.GONE);
                    mNoMessageLayout.setVisibility(View.VISIBLE);
                }
            }else {
                if (list != null && list.size() > 0){
                    adapter = new BidListDetailAdapter(this,list);
                    mlistview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    mlistview.setOnItemClickListener(onItemClickListener);
                    mlistview.setVisibility(View.VISIBLE);
                    mNoMessageLayout.setVisibility(View.GONE);
                }else {
                    mlistview.setVisibility(View.GONE);
                    mNoMessageLayout.setVisibility(View.VISIBLE);
                }
            }
        isclear = false;
    }
    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
        xrefresh.stopRefresh();
        xrefresh.stopLoadMore();
            switch (requestCode){
                case 1:
                    if (isclear){
                        list.clear();
                    }
                    try {
                        JSONArray array = new JSONArray(content);
                        addList(array);
                    }catch (Exception e){
                        mNoNetWorkLayout.setVisibility(View.VISIBLE);
                        mlistview.setVisibility(View.GONE);
                        mNoMessageLayout.setVisibility(View.GONE);
                    }
                    break;
                default:
                    break;
            }
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(BidListDetailActivity.this,BidBillDetailActivity.class);
            intent.putExtra("fbid",list.get(i).get("id"));
            startActivity(intent);
        }
    };
}
