package com.bbk.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbk.adapter.CustomFragmentPagerAdapter;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.fragment.BaseViewPagerFragment;
import com.bbk.fragment.BidAcceptanceFragment;
import com.bbk.fragment.BidChatFragment;
import com.bbk.fragment.BidFragment;
import com.bbk.fragment.BidHomeFragment;
import com.bbk.fragment.BidMessageFragment;
import com.bbk.fragment.BidUserFragment;
import com.bbk.util.BaseTools;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.CustomViewPager;
import com.bbk.view.NumImageView;
import com.bumptech.glide.Glide;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rtj on 2018/3/5.
 */

public class BidHomeActivity extends BaseFragmentActivity implements IWeiboHandler.Response, ResultEvent {
    private static final int TAB_SIZE = 5;
    private static CustomViewPager mViewPager;
    private CustomFragmentPagerAdapter mPagerAdapter;
    private ArrayList<BaseViewPagerFragment> fragments = new ArrayList<BaseViewPagerFragment>();
    private LinearLayout tabParentLayout;
    private int[] tabImgBlue = { R.mipmap.bj_51_02, R.mipmap.bj_52_02,R.mipmap.bj_53_02,
            R.mipmap.bj_54_02, R.mipmap.bj_55_02 };
    private int[] tabImgGray = { R.mipmap.bj_51_01, R.mipmap.bj_52_01,R.mipmap.bj_53_01,
            R.mipmap.bj_54_01, R.mipmap.bj_55_01 };
    private List<String> tabImgBlue2 = new ArrayList<>();
    private List<String> tabImgGray2 = new ArrayList<>();
    private boolean isshow = false;
    private int currentIndex = 0;
    private IWeiboShareAPI mWeiboShareAPI = null;
    private TextView mtext;
    private ImageView mimg;
    private int k = 0;
    private boolean iscli = false;
    private DataFlow dataFlow;
    public static ImageView mzhezhao;
    private boolean isuserzhezhao = false;
    public static Activity instance = null;
    private String bcolor = "#ffffff";
    private String tcolor = "#444444";
    private String ctcolor = "#b40000";
    private BidFragment bidFragment;
    public static String Flag = "";
    public static NumImageView mNumImage;//带数字角标的自定义view
    private String currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidhome);
        instance = this;
        dataFlow = new DataFlow(this);
        initView();
        initData();
    }
    public void initView() {
        TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        String token = TelephonyMgr.getDeviceId();
        SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "userInfor", "token", token);
        mViewPager = $(R.id.main_layout);
        mViewPager.setScanScroll(false);
        tabParentLayout = $(R.id.tab_layout);
        mtext = $(R.id.mtext);
        mimg = $(R.id.home_img_btn);
        mzhezhao = $(R.id.mzhezhao);
        mNumImage = findViewById(R.id.data_img_btn);
//        mNumImage.setNum((int) BidChatFragment.mChatMessage);
        try {
            String isFirstResultUse = SharedPreferencesUtil.getSharedData(BidHomeActivity.this,"isFirstBidUse", "isFirstBidUserUse");
            if (TextUtils.isEmpty(isFirstResultUse)) {
                isFirstResultUse = "yes";
            }
            if (isFirstResultUse.equals("yes")) {
                SharedPreferencesUtil.putSharedData(BidHomeActivity.this, "isFirstBidUse","isFirstBidUserUse", "bidno");
                BidHomeActivity.mzhezhao.setVisibility(View.VISIBLE);
                BidHomeActivity.mzhezhao.setImageResource(R.mipmap.new_bid_guide_fabiao);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        mzhezhao.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {

                    if (isuserzhezhao) {
                        mzhezhao.setVisibility(View.GONE);
                    }else{
                        mzhezhao.setImageResource(R.mipmap.new_bid_guide_jiebiao);
                        isuserzhezhao = true;

                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });
    }

    public void initData() {
        initViewPager();
        initViewPagerData();
        clickTab();
        mViewPager.setOffscreenPageLimit(0);
        if (getIntent().getStringExtra("currentitem") != null){
            mViewPager.setCurrentItem(3);
        }else {
            mViewPager.setCurrentItem(0);
        }
        HashMap<String, String> paramsMap = new HashMap<>();
        dataFlow.requestData(1, "newService/queryIndexMenu", paramsMap, this, false);

    }
//    //获取未读消息
//    public void initMsg(){
//        HashMap<String, String> paramsMap = new HashMap<>();
//        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
//        paramsMap.put("userid",userID);
//        dataFlow.requestData(2, "bid/queryMyBiaoMsg", paramsMap, this,false);
//    }
    public void initViewPager() {
        fragments.clear();
        mPagerAdapter = new CustomFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                switchTab(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    public void initViewPagerData() {
        BidHomeFragment bidHomeFragment = new BidHomeFragment();
//		SearchFragment searchFragment = new SearchFragment();
        bidFragment = new BidFragment();
        BidAcceptanceFragment bidAcceptanceFragment = new BidAcceptanceFragment();
        BidMessageFragment bidMessageFragment = new BidMessageFragment();
        BidUserFragment userFragment = new BidUserFragment();
        fragments.add(bidHomeFragment);
        fragments.add(bidFragment);
        fragments.add(bidAcceptanceFragment);
        fragments.add(bidMessageFragment);
        fragments.add(userFragment);
        mPagerAdapter.notifyDataSetChanged();

    }

    public void switchTab(int index) {
        if (index != currentIndex) {
            Resources resource = (Resources) getBaseContext().getResources();

            LinearLayout currentLayout = ((LinearLayout) tabParentLayout.getChildAt(currentIndex));
            ImageView currentIV = (ImageView) currentLayout.getChildAt(0);
            if (isshow){
                Log.e("==================",""+tabImgGray2.get(currentIndex));
                Glide.with(this).
                        load(tabImgGray2.get(currentIndex)).
                        into(currentIV);
            }else {
                currentIV.setImageResource(tabImgGray[currentIndex]);
            }
            TextView currentTV = (TextView) currentLayout.getChildAt(1);
            currentTV.setTextColor(Color.parseColor(tcolor));

            LinearLayout nextLayout = ((LinearLayout) tabParentLayout.getChildAt(index));
            ImageView nextIV = (ImageView) nextLayout.getChildAt(0);
            int select = BaseTools.getPixelsFromDp(this, 35);
            int not = BaseTools.getPixelsFromDp(this, 20);
            if (isshow){
                Glide.with(this).load(tabImgBlue2.get(index)).into(nextIV);
            }else {
                nextIV.setImageResource(tabImgBlue[index]);
            }
            TextView nextTV = (TextView) nextLayout.getChildAt(1);
            nextTV.setTextColor(Color.parseColor(ctcolor));

            currentIndex = index;
        }
    }
    public static void initone() {
        mViewPager.setCurrentItem(1);
    }
    public static void inittwo() {
        mViewPager.setCurrentItem(2);
    }
    public static void initThree() {
        mViewPager.setCurrentItem(3);
    }
    public static void initfour() {
        mViewPager.setCurrentItem(4);
    }
    public void clickTab() {
        for (int i = 0; i < TAB_SIZE; i++) {
            final int index = i;
            final LinearLayout tabLayout = (LinearLayout) tabParentLayout.getChildAt(i);
            tabLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (index == 3 ){
                        Flag = "bifhome3";
                        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
                        if (TextUtils.isEmpty(userID)){
                            Intent intent4= new Intent(BidHomeActivity.this, UserLoginNewActivity.class);
                            startActivityForResult(intent4,3);
                        }
                    }
                    if (index == 1){
                        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
                        if (TextUtils.isEmpty(userID)){
                            Flag = "bifhome1";
                            Intent intent4= new Intent(BidHomeActivity.this, UserLoginNewActivity.class);
                            startActivityForResult(intent4,2);
                        }else {
                            Intent intent= new Intent(BidHomeActivity.this, BidFbActivity.class);
                            startActivityForResult(intent,1);
                        }
                    }
                    mViewPager.setCurrentItem(index);
                }
            });

        }
    }

    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
        switch (requestCode){
            case 2:
//                try {
//                    JSONObject objectMeaage = new JSONObject(content);
//                    BidUserFragment.mMessage = objectMeaage.optInt("sysmsg");
////                    mNumImage.setNum(BidUserFragment.mMessage);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
                break;
        }

    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bidFragment.IntentResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                mViewPager.setCurrentItem(0);
                break;
            case 2:
                mViewPager.setCurrentItem(0);
                break;
            case 3:
                mViewPager.setCurrentItem(2);
                break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
//        String type = SharedPreferencesUtil.getSharedData(getApplicationContext(), "Bidhomeactivty", "type");
//        if (!TextUtils.isEmpty(type)) {
//            SharedPreferencesUtil.cleanShareData(getApplicationContext(), "Bidhomeactivty");
//            mViewPager.setCurrentItem(Integer.valueOf(type));
//        }
    }
    @Override
    protected void onStart() {
        super.onStart();
//        initMsg();
    }
}
