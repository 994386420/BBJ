package com.bbk.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbk.adapter.BidDetailListAdapter;
import com.bbk.adapter.BidDetailListPLAdapter;
import com.bbk.chat.ui.ChatActivity;
import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.util.EventIdIntentUtil;
import com.bbk.util.GlideImageLoader;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.MyListView;
import com.tencent.imsdk.TIMConversationType;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.model.Line;

/**
 * 接镖_02_详情
 */
public class BidDetailActivity extends BaseActivity implements ResultEvent {

    private DataFlow6 dataFlow;
    private String fbid,status;
    private Banner mbanner;
    private TextView mtitle,mendtimetop,mprice,mcount,mspectatornum,mbidnum
            ,mbidnum2,mstarttime,mendtime,mprice2,mextra,mplnum,mallpl;
    private MyListView mlistview,mlistviewpl;
    private List<Map<String,String>> list;
    private List<Map<String,String>> listpl;
    private BidDetailListAdapter adapter;
    private LinearLayout mgobid,malllist,mplbox,mchat;
    private BidDetailListPLAdapter pladapter;
    private ImageView topbar_goback_btn;
    private String userid = "";
    private LinearLayout isJiebiaoLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_detail);
        View topView = findViewById(R.id.topbar_layout);
        // 实现沉浸式状态栏
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        dataFlow = new DataFlow6(this);
        fbid = getIntent().getStringExtra("id");
        initView();
        initData();
        //从我的接镖列表获取返回值status，如果status = 1，可接镖；其他为不可接镖
        if (getIntent().getStringExtra("status") != null){
            status = getIntent().getStringExtra("status");
            if (status.equals("1")){
                isJiebiaoLayout.setVisibility(View.VISIBLE);
            }else {
                isJiebiaoLayout.setVisibility(View.GONE);
            }
        }
    }

    private void initView() {
        isJiebiaoLayout = findViewById(R.id.isJiebiao_layout);
        list = new ArrayList<>();
        listpl = new ArrayList<>();
        topbar_goback_btn= (ImageView) findViewById(R.id.topbar_goback_btn);
        topbar_goback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mtitle= (TextView) findViewById(R.id.mtitle);
        mendtimetop= (TextView) findViewById(R.id.mendtimetop);
        mprice= (TextView) findViewById(R.id.mprice);
        mcount= (TextView) findViewById(R.id.mcount);
        mspectatornum= (TextView) findViewById(R.id.mspectatornum);
        mbidnum= (TextView) findViewById(R.id.mbidnum);
        mbidnum2= (TextView) findViewById(R.id.mbidnum2);
        mstarttime= (TextView) findViewById(R.id.mstarttime);
        mendtime= (TextView) findViewById(R.id.mendtime);
        mprice2= (TextView) findViewById(R.id.mprice2);
        mextra= (TextView) findViewById(R.id.mextra);
        mplnum= (TextView) findViewById(R.id.mplnum);
        mallpl= (TextView) findViewById(R.id.mallpl);
        mbanner= (Banner) findViewById(R.id.mbanner);
        mgobid= (LinearLayout) findViewById(R.id.mgobid);
        malllist= (LinearLayout) findViewById(R.id.malllist);
        mchat= (LinearLayout) findViewById(R.id.mchat);
        mchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
                if (TextUtils.isEmpty(userID)){
                    Intent intent4= new Intent(getApplicationContext(), UserLoginNewActivity.class);
                    startActivity(intent4);
                }else {
                    Intent intent = new Intent(BidDetailActivity.this,ChatActivity.class);
                    intent.putExtra("identify","bbj"+userid);
                    intent.putExtra("type", TIMConversationType.C2C);
                    startActivity(intent);
                }
            }
        });
        mplbox= (LinearLayout) findViewById(R.id.mplbox);
        mlistview= (MyListView) findViewById(R.id.mlistview);
        mlistviewpl= (MyListView) findViewById(R.id.mlistviewpl);
        adapter = new BidDetailListAdapter(this,list);
        mlistview.setAdapter(adapter);
        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BidDetailActivity.this,BidFilterPriceActivity.class);
                intent.putExtra("bidid",list.get(position).get("bidid"));
                intent.putExtra("fbid",fbid);
                intent.putExtra("type","1");
                startActivity(intent);
            }
        });
        pladapter = new BidDetailListPLAdapter(this,listpl);
        mlistviewpl.setAdapter(pladapter);
        malllist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BidDetailActivity.this,BidHistoryActivity.class);
                intent.putExtra("fbid",fbid);
                intent.putExtra("type","2");
                startActivity(intent);
            }
        });
        mgobid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
                if (TextUtils.isEmpty(userID)){
                    Intent intent4= new Intent(getApplicationContext(), UserLoginNewActivity.class);
                    startActivity(intent4);
                }else {
                    Intent intent = new Intent(BidDetailActivity.this, BidingActivity.class);
                    intent.putExtra("fbid", fbid);
                    startActivity(intent);
                }
            }
        });
    }
    private void initData() {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("fbid",fbid);
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
        paramsMap.put("userid",userID);
        dataFlow.requestData(1, "bid/queryBidDetail", paramsMap, this,true);
    }
    public void addList(JSONArray bidarr) throws JSONException {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        for (int i = 0; i < bidarr.length(); i++) {
            JSONObject object = bidarr.getJSONObject(i);
            Map<String,String> map = new HashMap<>();
            map.put("bidid",object.optString("bidid"));
            map.put("biduser",object.optString("biduser"));
            map.put("biduserid",object.optString("biduserid"));
            map.put("biddesc",object.optString("biddesc"));
            map.put("bidprice",object.optString("bidprice"));
            map.put("bidstatus",object.optString("bidstatus"));
            map.put("bidtime",object.optString("bidtime"));
            map.put("bidurl",object.optString("bidurl"));
//            if (i<3){
                list.add(map);
//            }
//            Log.i("status是否可接镖",object.optString("bidstatus"));
            //判断如果用户ID跟biduserid有一样的，如果bidstatus状态为-1则可以接镖
            if (list.get(i).get("biduserid").toString().equals(userID)){
                if (object.optString("bidstatus").equals("-1")){
                    isJiebiaoLayout.setVisibility(View.VISIBLE);
                }else {
                    isJiebiaoLayout.setVisibility(View.GONE);
                }
            }
        }
        adapter.notifyDataSetChanged();

    }
    public void addPLList(JSONArray bidarr) throws JSONException {
        if (bidarr.length() == 0){
            mplbox.setVisibility(View.GONE);
        }else {
            mplnum.setText("评论("+bidarr.length()+")");
            mallpl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BidDetailActivity.this,BidMyPlActivity.class);
                    intent.putExtra("id",fbid);
                    startActivity(intent);
                }
            });
            for (int i = 0; i < bidarr.length(); i++) {
                JSONObject object = bidarr.getJSONObject(i);
                Map<String,String> map = new HashMap<>();
                map.put("plcontent",object.optString("plcontent"));
                map.put("plrole",object.optString("plrole"));
                map.put("pluserid",object.optString("pluserid"));
                map.put("plid",object.optString("plid"));
                map.put("plstar",object.optString("plstar"));
                map.put("fbid",object.optString("fbid"));
                map.put("pldtime",object.optString("pldtime"));
                map.put("plhead",object.optString("plhead"));
                map.put("plusername",object.optString("plusername"));
                map.put("plimgs","0");
                if (i<2){
                    listpl.add(map);
                }
            }
            pladapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
        try {
            String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
            JSONObject object  = new JSONObject(content);
            String  endtime =object.optString("endtime");
            String  beginlong =object.optString("beginlong");
            String  begintime =object.optString("begintime");
            String  extra =object.optString("extra");
            String  chixutime =object.optString("chixutime");
            String  number =object.optString("number");
            String  spectator =object.optString("spectator");
            String  url =object.optString("url");
            String  id =object.optString("id");
            String  endlong =object.optString("endlong");
            String  title =object.optString("title");
            String  price =object.optString("price");
            String  bidnum =object.optString("bidnum");
            userid =object.optString("userid");
            JSONArray imgs = object.getJSONArray("imgs");
            JSONArray bidarr = object.getJSONArray("bidarr");
            JSONArray plarr = object.getJSONArray("plarr");
            try {
                mtitle.setText(title);
                mendtimetop.setText("待接镖   "+endtime+" 结束");
                mprice.setText(price);
                mprice2.setText("￥"+price);
                mcount.setText("x"+number);
                mspectatornum.setText("围观 "+spectator+"  人");
                mbidnum.setText("接镖 "+bidnum+"  人");
                mbidnum2.setText(bidnum+" 条");
                mstarttime.setText(begintime);
                mendtime.setText(endtime);
                mextra.setText(extra);
                //userid与登陆userid一样时，隐藏去接镖
                if (userid.equals(userID )){
                    isJiebiaoLayout.setVisibility(View.GONE);
                }else {
                    isJiebiaoLayout.setVisibility(View.VISIBLE);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                addList(bidarr);
                addPLList(plarr);
            }catch (Exception e){
                e.printStackTrace();
            }
            List<Object> imgUrlList = new ArrayList<>();
             for (int i = 0; i < imgs.length(); i++) {
                    String imgUrl = imgs.getString(i);
                    imgUrlList.add(imgUrl);
             }
            mbanner.setImages(imgUrlList)
                    .setImageLoader(new GlideImageLoader())
                    .setOnBannerListener(new OnBannerListener() {
                        @Override
                        public void OnBannerClick(int position) {

                        }
                    })
                    .setDelayTime(3000)
                    .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                    .setIndicatorGravity(BannerConfig.CENTER)
                    .start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
