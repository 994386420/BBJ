package com.bbk.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bbk.adapter.BidDetailListAdapter;
import com.bbk.chat.ui.ChatActivity;
import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.tencent.imsdk.TIMConversationType;

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
public class BidFilterPriceActivity extends BaseActivity implements ResultEvent,View.OnClickListener {
    private String bidid;
    private DataFlow6 dataFlow;
    private TextView mensure,mendprice,murltext,mintentbuy,mbidesc;
    private String type;
    private String fbid,userid;
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
//        type = getIntent().getStringExtra("type");
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
        mcontact.setOnClickListener(this);
        mensure = (TextView) findViewById(R.id.mensure);
        mensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAssociateBid();
            }
        });
    }
    private void initData() {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("jbid",bidid);
        dataFlow.requestData(1, "bid/queryJiePerson", paramsMap, this,true);
    }
    private void updateAssociateBid() {
        HashMap<String, String> paramsMap = new HashMap<>();
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
        paramsMap.put("jbid",bidid);
        paramsMap.put("fbid",fbid);
        paramsMap.put("userid",userID);
        dataFlow.requestData(2, "bid/updateAssociateBid", paramsMap, this,true);
    }

    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
        switch (requestCode){
            case 1:
                try {
                    String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
                    JSONArray array = new JSONArray(content);
                    JSONObject object = array.getJSONObject(0);
                    String blidid = object.optString("bidid");
                    String biduser = object.optString("biduser");
                    String biddesc = object.optString("biddesc");
                    String bidprice = object.optString("bidprice");
                    String bidtime = object.optString("bidtime");
                    String biduserid = object.optString("biduserid");
                    final String bidurl = object.optString("bidurl");
                    userid = object.optString("userid");
                    mendprice.setText("￥"+bidprice);
                    if (biduserid.equals(userID)){
                        mensure.setVisibility(View.VISIBLE);
                    }else {
                        mensure.setVisibility(View.GONE);
                    }
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
                break;
            case 2:
                if (dataJo.optString("status").equals("1")){
                    StringUtil.showToast(this, "交镖成功");
                    Intent intent = new Intent(this, BidListDetailActivity.class);
                    intent.putExtra("status","3");
                    startActivity(intent);
                    finish();
                }else {
                    StringUtil.showToast(this, "交镖失败");
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mcontact:
                String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
                if (TextUtils.isEmpty(userID)){
                    Intent intent4= new Intent(getApplicationContext(), UserLoginNewActivity.class);
                    startActivity(intent4);
                }else {
                    Intent intent = new Intent(BidFilterPriceActivity.this, ChatActivity.class);
                    if (userid != null) {
                        intent.putExtra("identify","bbj"+userid);
                        intent.putExtra("type", TIMConversationType.C2C);
                        startActivity(intent);
                    }
                }
                break;
        }
    }
}
