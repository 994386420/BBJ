package com.bbk.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbk.adapter.BidDetailListAdapter;
import com.bbk.adapter.BidListDetailAdapter;
import com.bbk.chat.ui.ChatActivity;
import com.bbk.dialog.AlertDialog;
import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.MyListView;
import com.bbk.view.RushBuyCountDownTimerView;
import com.bumptech.glide.Glide;
import com.tencent.imsdk.TIMConversationType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发镖_03_详情
 */
public class BidBillDetailActivity extends BaseActivity implements ResultEvent {

    private DataFlow6 dataFlow;
    private String fbid;
    private ImageView item_img;
    private TextView item_title,mprice,mcount,mspectatornum,mbidnum,mendprice,
            murltext,mintentbuy,mbidesc,mbidnum2,mordernum,mbegintime,mendtime
            ,mtext1,mtext2;
    private LinearLayout mbox,mcontact,malllist;
    private MyListView mlistview;
    private RushBuyCountDownTimerView mtime;
    private List<Map<String,String>> list;
    private BidDetailListAdapter adapter;
    private String status;
    private ImageView topbar_goback_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_bill_detail);
        View topView = findViewById(R.id.topbar_layout);
        // 实现沉浸式状态栏
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        dataFlow = new DataFlow6(this);
        fbid = getIntent().getStringExtra("fbid");
        initView();
        initData();
    }
    public void initView(){
        list = new ArrayList<>();
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
        mlistview = (MyListView) findViewById(R.id.mlistview);
        adapter = new BidDetailListAdapter(this,list);
        mlistview.setAdapter(adapter);
        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BidBillDetailActivity.this,BidFilterPriceActivity.class);
                intent.putExtra("bidid",list.get(position).get("bidid"));
                intent.putExtra("fbid",fbid);
                intent.putExtra("type","1");
                startActivity(intent);
            }
        });
        malllist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BidBillDetailActivity.this,BidHistoryActivity.class);
                intent.putExtra("fbid",fbid);
                intent.putExtra("type","1");
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
    private void initbutton() {
        switch (status){
            case "0":
                mtext2.setVisibility(View.GONE);
                mtext1.setText("取消发镖");
                mtext1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog(BidBillDetailActivity.this).builder().setTitle("提示").setMsg("是否取消发镖？")
                                .setPositiveButton("确定", new View.OnClickListener() {
                                    @SuppressLint("NewApi")
                                    @Override
                                    public void onClick(View v) {
                                        upData(fbid,"bid/cancelBid",1);
                                    }
                                }).setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();
                    }
                });
                break;
            case "1":
                mtext1.setText("取消发镖");
                mtext1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog(BidBillDetailActivity.this).builder().setTitle("提示").setMsg("是否取消发镖？")
                                .setPositiveButton("确定", new View.OnClickListener() {
                                    @SuppressLint("NewApi")
                                    @Override
                                    public void onClick(View v) {
                                        upData(fbid,"bid/cancelBid",1);
                                    }
                                }).setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();

                    }
                });
                mtext2.setText("延长时间");
                mtext2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog(BidBillDetailActivity.this).builder().setTitle("提示").setMsg("只能延长一次，且延长24小时")
                                .setPositiveButton("确定", new View.OnClickListener() {
                                    @SuppressLint("NewApi")
                                    @Override
                                    public void onClick(View v) {
                                        upData(fbid,"bid/extendTime",2);
                                    }
                                }).setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();

                    }
                });
                break;
            case "2":
                rebid(fbid);
                mtext2.setText("评论");
                mtext2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(BidBillDetailActivity.this,BidMyWantPLActivity.class);
                        intent.putExtra("id",fbid);
                        startActivity(intent);
                    }
                });
                break;
            case "3":
                mtext2.setVisibility(View.GONE);
                rebid(fbid);
                break;
            case "4":
                mtext1.setText("取消发镖");
                mtext1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog(BidBillDetailActivity.this).builder().setTitle("提示").setMsg("是否取消发镖？")
                                .setPositiveButton("确定", new View.OnClickListener() {
                                    @SuppressLint("NewApi")
                                    @Override
                                    public void onClick(View v) {
                                        upData(fbid,"bid/cancelBid",1);
                                    }
                                }).setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();
                    }
                });
                mtext2.setText("修改");
                mtext2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(BidBillDetailActivity.this, BidActivity.class);
                        intent.putExtra("type","2");
                        intent.putExtra("id",fbid);
                        startActivity(intent);
                    }
                });
                break;
            case "5":
                mtext2.setVisibility(View.GONE);
                rebid(fbid);
                break;
            case "6":
                mtext2.setText("查看评论");
                mtext2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(BidBillDetailActivity.this,BidMyPlActivity.class);
                        intent.putExtra("id",fbid);
                        startActivity(intent);
                    }
                });
                rebid(fbid);
                break;
        }
    }
    public void rebid(final String id){
        mtext1.setText("再次发镖");
        mtext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BidBillDetailActivity.this, BidActivity.class);
                intent.putExtra("type","1");
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
    }

    public void upData(String id, String api, int i){
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("fbid",id);
        dataFlow.requestData(i, api, paramsMap, this,true);
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
            if (i<3){
                list.add(map);
            }
        }
        adapter.notifyDataSetChanged();

    }
    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
        try {
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
            String  bidnum =object.optString("bidnum");
            final String  userid =object.optString("userid");
            JSONArray imgs = object.getJSONArray("imgs");
            status =object.optString("status");
            initbutton();
            JSONArray bidarr = object.getJSONArray("bidarr");
            if (!"".equals(object.optString("bidindex"))){
                int i = object.optInt("bidindex");
                String  bidprice =bidarr.getJSONObject(i).optString("bidprice");
                String  biddesc =bidarr.getJSONObject(i).optString("biddesc");
                final String  bidurl =bidarr.getJSONObject(i).optString("bidurl");
                mendprice.setText("￥"+bidprice);
                murltext.setText(bidurl);
                mintentbuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(BidBillDetailActivity.this,WebViewActivity.class);
                        intent.putExtra("url",bidurl);
                        startActivity(intent);
                    }
                });
                mbidesc.setText("留言:"+biddesc);
                mcontact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
                        if (TextUtils.isEmpty(userID)){
                            Intent intent4= new Intent(getApplicationContext(), UserLoginNewActivity.class);
                            startActivity(intent4);
                        }else {
                            Intent intent = new Intent(BidBillDetailActivity.this,ChatActivity.class);
                            intent.putExtra("identify","bbj"+userid);
                            intent.putExtra("type", TIMConversationType.C2C);
                            startActivity(intent);
                        }
                    }
                });
            }else {
                mbox.setVisibility(View.GONE);
            }
//            private TextView item_title,mprice,mcount,mtime,mspectatornum,mbidnum,mendprice,
//                    murltext,mintentbuy,mbidesc,mbidnum2,mordernum,mbegintime,mendtime;
//            private LinearLayout mpricebox,mcontact;
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
            addList(bidarr);
            Glide.with(this).load(img).placeholder(R.mipmap.zw_img_300).into(item_img);




        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
