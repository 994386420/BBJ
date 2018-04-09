package com.bbk.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
 * 发镖_05_交镖
 */
public class BidFilterPriceActivity extends BaseActivity implements ResultEvent {
    private String bidid;
    private DataFlow6 dataFlow;
    private TextView mensure,mendprice,murltext,mintentbuy,mbidesc;
    private String type;
    private String fbid;
    private LinearLayout mcontact;
    private ImageView topbar_goback_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_filter_price);
        View topView = findViewById(R.id.topbar_layout);
        // 实现沉浸式状态栏
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        dataFlow = new DataFlow6(this);
        bidid = getIntent().getStringExtra("bidid");
        fbid = getIntent().getStringExtra("fbid");
        type = getIntent().getStringExtra("type");
        initView();
        initData();
    }

    private void initView() {
        topbar_goback_btn= (ImageView) findViewById(R.id.topbar_goback_btn);
        topbar_goback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mendprice = (TextView) findViewById(R.id.mendprice);
        murltext = (TextView) findViewById(R.id.murltext);
        mintentbuy = (TextView) findViewById(R.id.mintentbuy);
        mbidesc = (TextView) findViewById(R.id.mbidesc);
        mcontact = (LinearLayout) findViewById(R.id.mcontact);
        mensure = (TextView) findViewById(R.id.mensure);
        mensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAssociateBid();
            }
        });
        if ("1".equals(type)){
            mensure.setVisibility(View.VISIBLE);
        }else {
            mensure.setVisibility(View.GONE);
        }
    }
    private void initData() {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("jbid",bidid);
        dataFlow.requestData(1, "bid/queryJiePerson", paramsMap, this,true);
    }
    private void updateAssociateBid() {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("jbid",bidid);
        paramsMap.put("fbid",fbid);
        dataFlow.requestData(1, "bid/updateAssociateBid", paramsMap, this,true);
    }

    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
        try {
            JSONArray array = new JSONArray(content);
            JSONObject object = array.getJSONObject(0);
            String blidid = object.optString("bidid");
            String biduser = object.optString("biduser");
            String biddesc = object.optString("biddesc");
            String bidprice = object.optString("bidprice");
            String bidtime = object.optString("bidtime");
            final String bidurl = object.optString("bidurl");
            mendprice.setText("￥"+bidprice);
            murltext.setText(bidurl);
            mintentbuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BidFilterPriceActivity.this,WebViewActivity.class);
                    intent.putExtra("url",bidurl);
                    startActivity(intent);
                }
            });
            mbidesc.setText("留言:"+biddesc);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
