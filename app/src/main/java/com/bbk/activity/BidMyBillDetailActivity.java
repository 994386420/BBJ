package com.bbk.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bbk.adapter.BidDetailListAdapter;
import com.bbk.adapter.BidDetailListPLAdapter;
import com.bbk.chat.ui.ChatActivity;
import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.util.GlideImageLoader;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.CircleImageView1;
import com.bbk.view.MyListView;
import com.bbk.view.RushBuyCountDownTimerView;
import com.bumptech.glide.Glide;
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

import mtopsdk.common.util.StringUtils;

/**
 * 我的_接镖_详情
 */

public class BidMyBillDetailActivity extends BaseActivity implements ResultEvent {

    private DataFlow6 dataFlow;
    private TextView mtext1,mtext2,mTitle;
    private LinearLayout mtextbox;
    private ImageView topbar_goback_btn;
    private String  bidurl,userid,fbid,bidid,bidstatus;
    private String status;
    private Banner mbanner;
    private TextView mtitle,mendtimetop,mprice,mcount,mspectatornum,mbidnum
            ,mbidnum2,mstarttime,mendtime,mprice2,mextra,mplnum,mallpl;
    private MyListView mlistview,mlistviewpl;
    private List<Map<String,String>> list;
    private List<Map<String,String>> listpl;
    private BidDetailListAdapter adapter;
    private LinearLayout malllist,mplbox;
    private BidDetailListPLAdapter pladapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jiebiao_detail_layout);
        View topView = findViewById(R.id.topbar_layout);
        // 实现沉浸式状态栏
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        dataFlow = new DataFlow6(this);
        if (getIntent().getStringExtra("fbid") != null){
            fbid = getIntent().getStringExtra("fbid");
        }
        if (getIntent().getStringExtra("bidid") != null){
            bidid = getIntent().getStringExtra("bidid");
        }
        initView();
        initData();
    }
    public void initView(){
        mTitle = findViewById(R.id.title);
        mTitle.setText("扑倒详情");
        topbar_goback_btn= findViewById(R.id.topbar_goback_btn);
        topbar_goback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mtext1 = findViewById(R.id.mtext1);
        mtext2 =findViewById(R.id.mtext2);
        mtextbox = findViewById(R.id.mtextbox);
        list = new ArrayList<>();
        listpl = new ArrayList<>();
        topbar_goback_btn= findViewById(R.id.topbar_goback_btn);
        topbar_goback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mtitle= findViewById(R.id.mtitle);
        mendtimetop= findViewById(R.id.mendtimetop);
        mprice=  findViewById(R.id.mprice);
        mcount=  findViewById(R.id.mcount);
        mspectatornum= findViewById(R.id.mspectatornum);
        mbidnum=  findViewById(R.id.mbidnum);
        mbidnum2=  findViewById(R.id.mbidnum2);
        mstarttime= findViewById(R.id.mstarttime);
        mendtime=  findViewById(R.id.mendtime);
        mprice2= findViewById(R.id.mprice2);
        mextra=  findViewById(R.id.mextra);
        mplnum=  findViewById(R.id.mplnum);
        mallpl= findViewById(R.id.mallpl);
        mbanner=  findViewById(R.id.mbanner);
        malllist=  findViewById(R.id.malllist);
        mplbox=  findViewById(R.id.mplbox);
        mlistview= findViewById(R.id.mlistview);
        mlistviewpl=  findViewById(R.id.mlistviewpl);
        adapter = new BidDetailListAdapter(this,list);
        mlistview.setAdapter(adapter);
        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BidMyBillDetailActivity.this,BidFilterPriceActivity.class);
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
                Intent intent = new Intent(BidMyBillDetailActivity.this,BidHistoryActivity.class);
                intent.putExtra("fbid",fbid);
                intent.putExtra("type","2");
                startActivity(intent);
            }
        });
    }



    public void initData(){
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
            if (i<3){
            list.add(map);
            }
            //传进来的如果不为null
            if (getIntent().getStringExtra("bidstatus") != null){
                bidstatus = getIntent().getStringExtra("bidstatus");//功能键判断
                initbutton();
            }else {
                bidstatus = object.optString("bidstatus");//功能键判断
                initbutton();
            }
//            Log.i("status是否可接镖",object.optString("bidstatus"));
            //判断如果用户ID跟biduserid有一样的，如果bidstatus状态为-1则可以接镖
//            if (list.get(i).get("biduserid").toString().equals(userID)){
//                if (object.optString("bidstatus").equals("-1")){
//                    isJiebiaoLayout.setVisibility(View.VISIBLE);
//                }else {
//                    isJiebiaoLayout.setVisibility(View.GONE);
//                }
//            }
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
                    Intent intent = new Intent(BidMyBillDetailActivity.this,BidMyPlActivity.class);
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
    private void initbutton() {
        mtext1.setVisibility(View.GONE);
        switch (bidstatus){
            //审核不通过
            case "-3":
                mtextbox.setVisibility(View.GONE);
                break;
                //待审核
            case "-2":
                mtextbox.setVisibility(View.GONE);
                break;
            case "-1":
                mtextbox.setVisibility(View.GONE);
                mtext2.setVisibility(View.GONE);
                break;
            case "0":
                mtextbox.setVisibility(View.GONE);
                mtext2.setVisibility(View.GONE);
                break;
            case "1":
                mtext2.setText("取消扑倒");
                mtext2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        upData();
                    }
                });
                break;
            case "2":
                mtext2.setText("评论");
                mtext2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(BidMyBillDetailActivity.this,BidMyWantPLActivity.class);
                        intent.putExtra("id",fbid);
                        startActivity(intent);
                    }
                });
                break;
            case "3":
                mtext2.setText("查看评论");
                mtext2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(BidMyBillDetailActivity.this,BidMyPlActivity.class);
                        intent.putExtra("id",fbid);
                        startActivity(intent);
                    }
                });
                break;
        }
    }

    public void upData(){
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("jbid",bidid);
        dataFlow.requestData(3, "bid/cancelBid", paramsMap, this,true);
    }


    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
        switch (requestCode){
            case 1:
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
                    String mStatus = object.optString("status");//判断发镖详情页状态
                    userid =object.optString("userid");
                    JSONArray imgs = object.getJSONArray("imgs");
                    JSONArray bidarr = object.getJSONArray("bidarr");
                    JSONArray plarr = object.getJSONArray("plarr");
                    try {
                        mtitle.setText(title);
                        //根据status判断状态显示
                        switch (mStatus){
                            case "1":
                                mendtimetop.setText("待扑倒   "+endtime+" 结束");
                                mtextbox.setVisibility(View.GONE);
                                break;
                            case "2":
                                mendtimetop.setText("待评论 "+endtime);
                                break;
                            case "3":
                                mendtimetop.setText("已取消 "+endtime);
                                break;
                            case "4":
                                mendtimetop.setText("未审核通过 "+endtime);
                                break;
                            case "5":
                                mendtimetop.setText("已失效 "+endtime);
                                break;
                            case "6":
                                mendtimetop.setText("已完成 "+endtime);
                                break;
                        }
                        mprice.setText("￥"+price);
                        mprice2.setText("￥"+price);
                        mcount.setText("x"+number);
                        mspectatornum.setText("围观 "+spectator+"  人");
                        mbidnum.setText("扑倒 "+bidnum+"  人");
                        mbidnum2.setText(bidnum+" 条");
                        mstarttime.setText(begintime);
                        mendtime.setText(endtime);
                        mextra.setText(extra);
                        //userid与登陆userid一样时，隐藏去接镖
//                        if (userid.equals(userID )){
//                            isJiebiaoLayout.setVisibility(View.GONE);
//                        }else {
//                            isJiebiaoLayout.setVisibility(View.VISIBLE);
//                        }
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
                break;
            case 3:
                if(dataJo.optInt("status")<=0){
                    StringUtil.showToast(this,"取消失败");
                }else{
                    StringUtil.showToast(this,"取消成功");
                    Intent intent = new Intent(this,BidMyListDetailActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }
    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(BidMyBillDetailActivity.this,BidFilterPriceActivity.class);
            intent.putExtra("bidid",list.get(i).get("bidid"));
            intent.putExtra("fbid",fbid);
            intent.putExtra("type","1");
            startActivity(intent);
        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent;
            switch (view.getId()){
                case R.id.mintentbuy:
                    intent = new Intent(BidMyBillDetailActivity.this,WebViewActivity.class);
                    intent.putExtra("url",bidurl);
                    startActivity(intent);
                    break;
                case R.id.mcontact:
                    intent = new Intent(BidMyBillDetailActivity.this,ChatActivity.class);
                    if (userid != null){
                        intent.putExtra("identify","bbj"+userid);
                        intent.putExtra("type", TIMConversationType.C2C);
                        startActivity(intent);
                    }
                    break;
                case R.id.malllist:
                    intent = new Intent(BidMyBillDetailActivity.this,BidHistoryActivity.class);
                    intent.putExtra("fbid",fbid);
                    intent.putExtra("type","1");
                    startActivity(intent);
                    break;
            }
        }
    };

}
