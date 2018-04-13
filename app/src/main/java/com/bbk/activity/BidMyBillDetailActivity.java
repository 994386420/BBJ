package com.bbk.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bbk.adapter.BidDetailListAdapter;
import com.bbk.chat.ui.ChatActivity;
import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.CircleImageView1;
import com.bbk.view.MyListView;
import com.bbk.view.RushBuyCountDownTimerView;
import com.bumptech.glide.Glide;
import com.tencent.imsdk.TIMConversationType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mtopsdk.common.util.StringUtils;

/**
 * 我的_03_详情
 */

public class BidMyBillDetailActivity extends BaseActivity implements ResultEvent {

    private DataFlow6 dataFlow;
    private ImageView item_img;
    private TextView item_title,mprice,mcount,mspectatornum,mbidnum,mendprice,
            murltext,mintentbuy,mbidesc,mbidnum2,mordernum,mbegintime,mendtime
            ,mtext1,mtext2;
    private LinearLayout mbox,mcontact,malllist,mtextbox;
    private MyListView mlistview;
    private RushBuyCountDownTimerView mtime;
    private List<Map<String,String>> list;
    private BidDetailListAdapter adapter;
    private String status;
    private ImageView topbar_goback_btn;
    String  bidurl,userid,fbid,bidid;
    TextView mbidprice,mbidtime,muser,mBid,mbidprice1,mbidtime1,muser1,mBid1,mbidprice2,mbidtime2,muser2,mBid2;
    ImageView mimg,mimg1,mimg2;
    private LinearLayout mLayout1,mLayout2,mLayout3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_bill_detail);
        View topView = findViewById(R.id.topbar_layout);
        // 实现沉浸式状态栏
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        dataFlow = new DataFlow6(this);
        fbid = getIntent().getStringExtra("fbid");
        bidid = getIntent().getStringExtra("bidid");
        initView();
        initData();
    }
    public void initView(){
        topbar_goback_btn= (ImageView) findViewById(R.id.topbar_goback_btn);
        topbar_goback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        item_img = (ImageView)findViewById(R.id.item_img);
        item_title = (TextView) findViewById(R.id.item_title);
        mprice = (TextView) findViewById(R.id.mprice);
        mcount = (TextView) findViewById(R.id.mcount);
        mtime = (RushBuyCountDownTimerView) findViewById(R.id.mtime);
        mspectatornum = (TextView) findViewById(R.id.mspectatornum);
        mbidnum = (TextView) findViewById(R.id.mbidnum);
        mendprice = (TextView) findViewById(R.id.mendprice);
        murltext = (TextView) findViewById(R.id.murltext);
        mintentbuy = (TextView) findViewById(R.id.mintentbuy);
        mbidesc = (TextView) findViewById(R.id.mbidesc);
        mbidnum2 = (TextView) findViewById(R.id.mbidnum2);
        mordernum = (TextView) findViewById(R.id.mordernum);
        mbegintime = (TextView) findViewById(R.id.mbegintime);
        mendtime = (TextView) findViewById(R.id.mendtime);
        mtext1 = (TextView) findViewById(R.id.mtext1);
        mtext2 = (TextView) findViewById(R.id.mtext2);
        mbox = (LinearLayout) findViewById(R.id.mbox);
        mcontact = (LinearLayout) findViewById(R.id.mcontact);
        malllist = (LinearLayout) findViewById(R.id.malllist);
        mtextbox = (LinearLayout) findViewById(R.id.mtextbox);
        mlistview = (MyListView) findViewById(R.id.mlistview);
        mbidtime = (TextView)findViewById(R.id.mbidtime);
        mbidprice = (TextView)findViewById(R.id.mbidprice);
        muser = (TextView)findViewById(R.id.muser);
        mimg = findViewById(R.id.mimg);
        mBid = findViewById(R.id.mbidtext);
        mbidtime1 = (TextView)findViewById(R.id.mbidtime1);
        mbidprice1 = (TextView)findViewById(R.id.mbidprice1);
        muser1 = (TextView)findViewById(R.id.muser1);
        mimg1 = findViewById(R.id.mimg1);
        mBid1= findViewById(R.id.mbidtext1);
        mbidtime2 = (TextView)findViewById(R.id.mbidtime2);
        mbidprice2 = (TextView)findViewById(R.id.mbidprice2);
        muser2 = (TextView)findViewById(R.id.muser2);
        mimg2 = findViewById(R.id.mimg2);
        mBid2 = findViewById(R.id.mbidtext2);
        mLayout1 = findViewById(R.id.ll_jbjl_layout1);
        mLayout2= findViewById(R.id.ll_jbjl_layout2);
        mLayout3 = findViewById(R.id.ll_jbjl_layout3);
    }



    public void initData(){
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("fbid",fbid);
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
        paramsMap.put("userid",userID);
        dataFlow.requestData(1, "bid/queryBidDetail", paramsMap, this,true);
    }
    private void initbutton() {
        mtext1.setVisibility(View.GONE);
        switch (status){
            case "-1":
                mtextbox.setVisibility(View.GONE);
                break;
            case "0":
                mtextbox.setVisibility(View.GONE);
                break;
            case "1":
                mtext2.setText("取消接镖");
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
            map.put("userid",object.optString("userid"));
            if (i<3){
                list.add(map);
            }
        }
    }
    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
        switch (requestCode){
            case 1:
                try {
                    list = new ArrayList<>();
                    JSONObject object = new JSONObject(content);
                    String  endtime =object.optString("endtime");
                    String  beginlong =object.optString("beginlong");
                    String  begintime =object.optString("begintime");
                    String  extra =object.optString("extra");
                    String  ordernum =object.optString("ordernum");
                    String  img =object.optString("img");
                    String  number =object.optString("number");
                    String  spectator =object.optString("spectator");
                    final String  id =object.optString("id");
                    String  endlong =object.optString("endlong");
                    String  title =object.optString("title");
                    String  price =object.optString("price");
                    userid =object.optString("userid");
                    String  bidnum =object.optString("bidnum");
                    status =object.optString("status");
                    initbutton();
                    JSONArray imgs = object.getJSONArray("imgs");
                    JSONArray bidarr = object.getJSONArray("bidarr");
                    addList(bidarr);
//                    Log.i("======",list+"");
                    if (list != null&& list.size() > 0){
                            String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
                            if (list.size() == 1){
                                mLayout1.setVisibility(View.VISIBLE);
                                mLayout2.setVisibility(View.GONE);
                                mLayout3.setVisibility(View.GONE);
                                mbidprice.setText("￥"+list.get(0).get("bidprice").toString());
                                mbidtime.setText(list.get(0).get("bidtime").toString());
                                muser.setText(list.get(0).get("biduser").toString());
                                if (list.get(0).get("biduserid").toString().equals(userID)){
                                    init(mbidprice,mbidtime,muser,mBid,mimg);
                                }else {
                                    initPostion1();
                                }
                            }else if (list.size() == 2){
                                mbidprice.setText("￥"+list.get(0).get("bidprice").toString());
                                mbidtime.setText(list.get(0).get("bidtime").toString());
                                muser.setText(list.get(0).get("biduser").toString());
                                mbidprice1.setText("￥" + list.get(1).get("bidprice").toString());
                                mbidtime1.setText(list.get(1).get("bidtime").toString());
                                muser1.setText(list.get(1).get("biduser").toString());
                                if (list.get(0).get("biduserid").toString().equals(userID)){
                                    init(mbidprice,mbidtime,muser,mBid,mimg);
                                }else {
                                    initPostion1();
                                }
                                if (list.get(1).get("biduserid").toString().equals(userID)){
                                    init(mbidprice1,mbidtime1,muser1,mBid1,mimg1);
                                }else {
                                    initPostion2();
                                }
                                mLayout1.setVisibility(View.VISIBLE);
                                mLayout2.setVisibility(View.VISIBLE);
                                mLayout3.setVisibility(View.GONE);
                            }else if(list.size() == 3){
                                mbidprice.setText("￥"+list.get(0).get("bidprice").toString());
                                mbidtime.setText(list.get(0).get("bidtime").toString());
                                muser.setText(list.get(0).get("biduser").toString());
                                mbidprice1.setText("￥" + list.get(1).get("bidprice").toString());
                                mbidtime1.setText(list.get(1).get("bidtime").toString());
                                muser1.setText(list.get(1).get("biduser").toString());
                                mbidprice2.setText("￥" + list.get(2).get("bidprice").toString());
                                mbidtime2.setText(list.get(2).get("bidtime").toString());
                                muser2.setText(list.get(2).get("biduser").toString());
                                if (list.get(0).get("biduserid").toString().equals(userID)){
                                    init(mbidprice,mbidtime,muser,mBid,mimg);
                                }else {
                                    initPostion1();
                                }
                                if (list.get(1).get("biduserid").toString().equals(userID)){
                                    init(mbidprice1,mbidtime1,muser1,mBid1,mimg1);
                                }else {
                                    initPostion2();
                                }
                                if (list.get(2).get("biduserid").toString().equals(userID)){
                                    init(mbidprice2,mbidtime2,muser2,mBid2,mimg2);
                                }else {
                                    initPostion3();
                                }
                                mLayout1.setVisibility(View.VISIBLE);
                                mLayout2.setVisibility(View.VISIBLE);
                                mLayout3.setVisibility(View.VISIBLE);
                            }
                    }
                    adapter = new BidDetailListAdapter(this,list);
                    mlistview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    mlistview.setOnItemClickListener(onItemClickListener);
                    if (object.has("bidindex")){
                        int i = object.optInt("bidindex");
                        try {
                            if (bidarr != null){
                                String  bidprice =bidarr.getJSONObject(i).optString("bidprice");
                                String  biddesc =bidarr.getJSONObject(i).optString("biddesc");
                                bidurl =bidarr.getJSONObject(i).optString("bidurl");
                                mendprice.setText("￥"+bidprice);
                                murltext.setText(bidurl);
                                mintentbuy.setOnClickListener(onClickListener);
                                mbidesc.setText("留言:"+biddesc);
                                mcontact.setOnClickListener(onClickListener);
                            }
                        }catch (Exception e){

                        }
                    }else {
                        mbox.setVisibility(View.GONE);
                    }
                    item_title.setText(title);
                    mprice.setText(price);
                    mcount.setText("x"+number);
                    mtime.addsum(endlong,"#999999");
                    mtime.start();
                    mspectatornum.setText("围观 "+spectator+"  人");
                    mbidnum.setText("接镖 "+bidnum+"  人");
                    mbidnum2.setText(bidnum+" 条");
                    mordernum.setText("镖单编号:"+ordernum);
                    mbegintime.setText("创建时间:"+beginlong);
                    mendtime.setText("完成时间:"+endlong);
                    malllist.setOnClickListener(onClickListener);
                    Glide.with(this).load(img).placeholder(R.mipmap.zw_img_300).into(item_img);
                } catch (Exception e) {
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
                    intent.putExtra("identify","bbj"+userid);
                    intent.putExtra("type", TIMConversationType.C2C);
                    startActivity(intent);
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

    private void init(TextView mbidprice,TextView mbidtime,TextView muser,TextView mBid,ImageView mimg){
        mbidprice.setTextColor(Color.parseColor("#1a9afc"));
        mbidtime.setTextColor(Color.parseColor("#1a9afc"));
        muser.setTextColor(Color.parseColor("#1a9afc"));
        mBid.setTextColor(Color.parseColor("#1a9afc"));
        String imgUrl = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "imgUrl");
        CircleImageView1.getImg(this,imgUrl,mimg);
    }
    private void initPostion1(){
        mbidprice.setTextColor(Color.parseColor("#b40000"));
        mbidtime.setTextColor(Color.parseColor("#b40000"));
        muser.setTextColor(Color.parseColor("#b40000"));
        mBid.setTextColor(Color.parseColor("#b40000"));
        mimg.setBackgroundResource(R.mipmap.bj_05);
    }
    private void initPostion2(){
        mbidprice1.setTextColor(Color.parseColor("#666666"));
        mbidtime1.setTextColor(Color.parseColor("#666666"));
        muser1.setTextColor(Color.parseColor("#666666"));
        mBid1.setTextColor(Color.parseColor("#666666"));
        mimg1.setBackgroundResource(R.mipmap.bj_06);
    }
    private void initPostion3(){
        mbidprice2.setTextColor(Color.parseColor("#999999"));
        mbidtime2.setTextColor(Color.parseColor("#999999"));
        muser2.setTextColor(Color.parseColor("#999999"));
        mBid2.setTextColor(Color.parseColor("#999999"));
        mimg2.setBackgroundResource(R.mipmap.bj_07);
    }
}
