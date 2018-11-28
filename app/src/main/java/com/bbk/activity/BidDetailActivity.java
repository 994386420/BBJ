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

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.PubaDetailBean;
import com.bbk.adapter.BidDetailListAdapter;
import com.bbk.adapter.BidDetailListPLAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.flow.DataFlow6;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.GlideImageLoader;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.CommonLoadingView;
import com.bbk.view.MyListView;
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
public class BidDetailActivity extends BaseActivity implements CommonLoadingView.LoadingHandler {

    private DataFlow6 dataFlow;
    private String fbid,status;
    private Banner mbanner;
    private TextView mtitle,mendtimetop,mprice,mcount,mspectatornum,mbidnum
            ,mbidnum2,mstarttime,mendtime,mprice2,mextra,mplnum,mallpl,mGoJbText,mGoJbTextStaus,mJieBiaojiaText;
    private MyListView mlistview,mlistviewpl;
    private List<Map<String,String>> list;
    private List<Map<String,String>> listpl;
    private BidDetailListAdapter adapter;
    private LinearLayout mgobid,malllist,mplbox,mchat,mFabiaoLayout;
    private BidDetailListPLAdapter pladapter;
    private ImageView topbar_goback_btn;
    private String userid = "";
    private LinearLayout pubaDetailLayout;
    private PubaDetailBean pubaDetailBean;
    private CommonLoadingView zLoadingView;//加载框

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
    }

    private void initView() {
        zLoadingView = findViewById(R.id.progress);
        zLoadingView.setLoadingHandler(this);
        mJieBiaojiaText = findViewById(R.id.tv_jbj);
        mFabiaoLayout = findViewById(R.id.fabiao_layout);
        mFabiaoLayout.setVisibility(View.GONE);
        mGoJbText =findViewById(R.id.gojb_text);
        mGoJbTextStaus =findViewById(R.id.gojb_text_status);
        pubaDetailLayout = findViewById(R.id.puba_detail_layout);
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
//                    Intent intent = new Intent(BidDetailActivity.this,ChatActivity.class);
//                    intent.putExtra("identify","bbj"+userid);
//                    intent.putExtra("type", TIMConversationType.C2C);
//                    startActivity(intent);
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
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("fbid",fbid);
        final String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
        paramsMap.put("userid",userID);
        RetrofitClient.getInstance(this).createBaseApi().queryBidDetail(
                paramsMap, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                pubaDetailBean = JSON.parseObject(jsonObject.optString("content"), PubaDetailBean.class);
                                //接镖价有则显示，没有显示待定
                                if (!pubaDetailBean.getFinalprice().equals("") && pubaDetailBean.getFinalprice() != null){
                                    mJieBiaojiaText.setText("￥"+pubaDetailBean.getFinalprice());
                                }else {
                                    mJieBiaojiaText.setText("待定");
                                }
                                mtitle.setText(pubaDetailBean.getTitle());
                                String endtime = pubaDetailBean.getEndtime();
                                userid = pubaDetailBean.getUserid();
                                    //根据status判断状态显示
                                    switch (pubaDetailBean.getStatus()){
                                        case "1":
                                            mendtimetop.setText("待接单   "+endtime+" 结束");
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
                                    mprice.setText("￥"+pubaDetailBean.getPrice());
                                    mprice2.setText("￥"+pubaDetailBean.getPrice());
                                    mcount.setText("x"+pubaDetailBean.getNumber());
                                    mspectatornum.setText("围观 "+pubaDetailBean.getSpectator()+"  人");
                                    mbidnum.setText("接单 "+pubaDetailBean.getBidnum()+"  人");
                                    mbidnum2.setText(pubaDetailBean.getBidnum()+" 条");
                                    mstarttime.setText(pubaDetailBean.getBegintime());
                                    mendtime.setText(endtime);
                                    mextra.setText(pubaDetailBean.getExtra());
                                    //userid与登陆userid一样时，隐藏去接镖
                                    if (pubaDetailBean.getUserid() != null) {
                                        if (pubaDetailBean.getUserid().equals(userID)) {
                                            mgobid.setClickable(false);
                                            mGoJbText.setText("不能自己接单");
                                            mGoJbText.setTextColor(getResources().getColor(R.color.tuiguang_color5));
                                            mGoJbTextStaus.setVisibility(View.GONE);
                                            mgobid.setBackgroundColor(getResources().getColor(R.color.gray));
                                        } else {
                                            mgobid.setClickable(true);
                                            mgobid.setBackgroundColor(getResources().getColor(R.color.tuiguang_color5));
                                        }
                                    }
                                    //从我的接镖列表获取返回值status，如果status = 1，可接镖；其他为不可接镖
                                    if (getIntent().getStringExtra("status") != null) {
                                        status = getIntent().getStringExtra("status");
                                        if (status.equals("1")) {
                                            mgobid.setClickable(true);
                                            mgobid.setBackgroundColor(getResources().getColor(R.color.tuiguang_color5));
                                        } else {
                                            mgobid.setClickable(false);
                                            mGoJbText.setText("已结束");
                                            mGoJbText.setTextColor(getResources().getColor(R.color.color_line_text));
                                            mGoJbTextStaus.setVisibility(View.GONE);
                                            mgobid.setBackgroundColor(getResources().getColor(R.color.gray));
                                        }
                                    }
                            }
                            /**
                             * 加载banner
                             */
                            List<Object> imgUrlList = new ArrayList<>();
                            JSONArray imgs = new JSONArray(pubaDetailBean.getImgs());
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
                            JSONArray bidarr = new JSONArray(pubaDetailBean.getBidarr());
                            JSONArray plarr = new JSONArray(pubaDetailBean.getPlarr());
                            addList(bidarr);
                            addPLList(plarr);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    protected void hideDialog() {
                        zLoadingView.loadSuccess();
                        DialogSingleUtil.dismiss(0);
                        pubaDetailLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    protected void showDialog() {
//                        zLoadingView.load();
                        DialogSingleUtil.show(BidDetailActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        zLoadingView.setVisibility(View.VISIBLE);
                        zLoadingView.loadError();
                        pubaDetailLayout.setVisibility(View.GONE);
                        StringUtil.showToast(BidDetailActivity.this, e.message);
                    }
                });
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
                //判断如果用户ID跟biduserid有一样的，如果bidstatus状态为-1则可以接镖
                if (list.get(i).get("biduserid").toString().equals(userID)){
                    if (object.optString("bidstatus").equals("-1")){
                        mgobid.setClickable(true);
                        mgobid.setBackgroundColor(getResources().getColor(R.color.tuiguang_color5));
                    }else {
                        mgobid.setClickable(false);
                        mGoJbText.setText("已接单");
                        mGoJbText.setTextColor(getResources().getColor(R.color.color_line_text));
                        mGoJbTextStaus.setVisibility(View.GONE);
                        mgobid.setBackgroundColor(getResources().getColor(R.color.gray));
                    }
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
    public void doRequestData() {
        zLoadingView.setVisibility(View.GONE);
        initData();
    }
}
