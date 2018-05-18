package com.bbk.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
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
import com.bbk.adapter.BidListDetailAdapter;
import com.bbk.adapter.XjppAdapter;
import com.bbk.chat.ui.ChatActivity;
import com.bbk.dialog.AlertDialog;
import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.HorizontalListView;
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
            ,mtext1,mtext2,mTitle;
    private LinearLayout mbox,mcontact,malllist;
    private MyListView mlistview;
//    private RushBuyCountDownTimerView mtime;
    private List<Map<String,String>> list,mXjppList;
    private BidDetailListAdapter adapter;
    private String status;
    private ImageView topbar_goback_btn;
    private TextView tv_status;//发镖详情状态
    private HorizontalListView horizontalListView;
    private int durationRotate = 700;// 旋转动画时间
    private int durationAlpha = 500;// 透明度动画时间
    private boolean isGlobalMenuShow;
    private LinearLayout xjpp_layout;//小鲸扑扑
    private LinearLayout xjpp_view_layout;//小鲸扑扑
    private ImageView xjpp_iamge;//小鲸扑扑

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
        xjpp_layout = findViewById(R.id.xjpp_layout);
        xjpp_view_layout = findViewById(R.id.xjpp_view_layout);
        xjpp_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isGlobalMenuShow = !isGlobalMenuShow;
                showGlobalMenu();
            }
        });
        xjpp_iamge = findViewById(R.id.xjpp_image);
        horizontalListView = findViewById(R.id.horizontal_listview);
        list = new ArrayList<>();
        mXjppList = new ArrayList<>();
        mTitle = findViewById(R.id.title);
        mTitle.setText("扑倒详情");
        tv_status = findViewById(R.id.tv_statuus);
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
//        mtime = (RushBuyCountDownTimerView) findViewById(R.id.mtime);
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
        Log.i("===",fbid+userID);
        dataFlow.requestData(1, "bid/queryBidDetail", paramsMap, this,true);
    }
    private void initbutton(String time) {
        switch (status){
            case "0":
                tv_status.setText("待审核 "+time);
                mtext2.setVisibility(View.GONE);
                mtext1.setText("取消我要");
                mtext1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog(BidBillDetailActivity.this).builder().setTitle("提示").setMsg("是否取消我要？")
                                .setPositiveButton("确定", new View.OnClickListener() {
                                    @SuppressLint("NewApi")
                                    @Override
                                    public void onClick(View v) {
                                        upData(fbid,"bid/cancelBid",3);
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
                tv_status.setText("待扑倒 "+ time);
                mtext1.setText("取消我要");
                mtext1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog(BidBillDetailActivity.this).builder().setTitle("提示").setMsg("是否取消我要？")
                                .setPositiveButton("确定", new View.OnClickListener() {
                                    @SuppressLint("NewApi")
                                    @Override
                                    public void onClick(View v) {
                                        upData(fbid,"bid/cancelBid",3);
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
                tv_status.setText("待评论 "+ time);
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
                tv_status.setText("已取消 "+ time);
                mtext2.setVisibility(View.GONE);
                rebid(fbid);
                break;
            case "4":
                tv_status.setText("未审核通过 "+ time);
                mtext1.setText("取消我要");
                mtext1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog(BidBillDetailActivity.this).builder().setTitle("提示").setMsg("是否取消我要？")
                                .setPositiveButton("确定", new View.OnClickListener() {
                                    @SuppressLint("NewApi")
                                    @Override
                                    public void onClick(View v) {
                                        upData(fbid,"bid/cancelBid",3);
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
                tv_status.setText("已失效 "+ time);
                mtext2.setVisibility(View.GONE);
                rebid(fbid);
                break;
            case "6":
                tv_status.setText("已完成 "+ time);
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
        mtext1.setText("再次我要");
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
            map.put("biduserid",object.optString("biduserid"));
            map.put("biddesc",object.optString("biddesc"));
            map.put("bidprice",object.optString("bidprice"));
            map.put("bidstatus",object.optString("bidstatus"));
            map.put("bidtime",object.optString("bidtime"));
            map.put("bidurl",object.optString("bidurl"));
            if (i<3){
                list.add(map);
                //判断发镖用户id是否一致，一直则隐藏聊天
                mcontact.setVisibility(View.VISIBLE);
                final int finalI = i;
                mcontact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
                        if (TextUtils.isEmpty(userID)){
                            Intent intent4= new Intent(getApplicationContext(), UserLoginNewActivity.class);
                            startActivity(intent4);
                        }else {
                            Intent intent = new Intent(BidBillDetailActivity.this,ChatActivity.class);
                            intent.putExtra("identify","bbj"+list.get(finalI).get("biduserid"));
                            intent.putExtra("type", TIMConversationType.C2C);
                            startActivity(intent);
                        }
                    }
                });
            }
        }
    }

    public void addListXjpp(JSONArray array) throws JSONException {
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            Map<String,String> map = new HashMap<>();
            map.put("imgUrl",object.optString("imgUrl"));
            map.put("title",object.optString("title"));
            map.put("price",object.optString("price"));
            map.put("rowkey",object.optString("rowkey"));
            map.put("url",object.optString("url"));
                mXjppList.add(map);
            }
            horizontalListView.setAdapter(new XjppAdapter(this,mXjppList));
            horizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(BidBillDetailActivity.this, WebViewActivity.class);
                        intent.putExtra("url", mXjppList.get(i).get("url"));
                        intent.putExtra("groupRowKey", mXjppList.get(i).get("rowkey"));
                         startActivity(intent);
                }
            });
    }
    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
        switch (requestCode){
            case 1:
                try {
                    Log.i("====",content);
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
                    if (object.has("imgs")){
                        JSONArray imgs = object.getJSONArray("imgs");
                    }
                    if (object.has("tjList")) {
                        JSONArray tjlistArray = object.getJSONArray("tjList");
                        addListXjpp(tjlistArray);
                    }
                    status =object.optString("status");
                    initbutton(endtime);
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
                    }else {
                        mbox.setVisibility(View.GONE);
                    }
                    item_title.setText(title);
                    mprice.setText("￥"+price);
                    mcount.setText("x"+number);
//                    mtime.addsum(endlong,"#999999");
//                    mtime.start();
                    mspectatornum.setText("围观 "+spectator+"  人");
                    mbidnum.setText("扑倒 "+bidnum+"  人");
                    mbidnum2.setText(bidnum+" 条");

                    mordernum.setText("订单编号:"+ordernum);
                    mbegintime.setText("创建时间:"+beginlong);
                    mendtime.setText("结束时间:"+endlong);
//                    Log.i("--------",bidarr.length()+"====");
                    if (bidarr.length() > 0){
                        addList(bidarr);
                    }else {
                        isGlobalMenuShow = true;
                        xjpp_iamge.setImageResource(R.mipmap.xjpp_top);
                        xjpp_view_layout.setVisibility(View.VISIBLE);
                    }
                    adapter = new BidDetailListAdapter(this,list);
                    mlistview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
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
                    Glide.with(this).load(img).placeholder(R.mipmap.zw_img_300).into(item_img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                switch (content){
                    case "-1":
                        StringUtil.showToast(this,"已经延长过了");
                        break;
                    case "0":
                        StringUtil.showToast(this,"延长失败");
                        break;
                    case "1":
                        StringUtil.showToast(this,"延长成功");
                        break;
                }
                break;
            case 3:
                if (dataJo.optString("status").equals("1")){
                    StringUtil.showToast(this,"取消成功");
                    Intent intent = new Intent(this, BidListDetailActivity.class);
                    intent.putExtra("status","4");
                    startActivity(intent);
                    finish();
                }else {
                    StringUtil.showToast(this,"取消失败");
                }
                break;
        }
    }

    /**
     * 显示菜单；图标动画
     */
    private void showGlobalMenu() {
        if (isGlobalMenuShow) {
            ObjectAnimator.ofFloat(xjpp_iamge, "rotation", 0, 180)
                    .setDuration(durationRotate).start();
            ObjectAnimator.ofFloat(xjpp_iamge, "rotation", 0, 180)
                    .setDuration(durationRotate).start();
            ObjectAnimator.ofFloat(xjpp_iamge, "rotation", 0, 180)
                    .setDuration(durationRotate).start();
            xjpp_view_layout.setVisibility(View.VISIBLE);
//            xjpp_view_layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            ObjectAnimator.ofFloat(xjpp_view_layout, "alpha", 0, 1)
                    .setDuration(durationAlpha).start();
        } else {
            ObjectAnimator.ofFloat(xjpp_iamge, "rotation", 180, 360)
                    .setDuration(durationRotate).start();
            ObjectAnimator.ofFloat(xjpp_iamge, "rotation", 180, 360)
                    .setDuration(durationRotate).start();
            ObjectAnimator.ofFloat(xjpp_iamge, "rotation", 180, 360)
                    .setDuration(durationRotate).start();
            ObjectAnimator.ofFloat(xjpp_view_layout, "alpha", 1, 0)
                    .setDuration(durationAlpha).start();
            xjpp_view_layout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    xjpp_view_layout.setVisibility(View.GONE);
//                    xjpp_view_layout.setLayoutParams(new LinearLayout.LayoutParams(0 ,0));
                }
            }, durationAlpha);
        }

    }
}
