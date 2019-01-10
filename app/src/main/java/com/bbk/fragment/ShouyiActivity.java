package com.bbk.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.BrokerageDetailBean;
import com.bbk.Bean.BrokerageDetailInfoBean;
import com.bbk.activity.BaseFragmentActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.adapter.BrokerageDetailAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.model.tablayout.XTabLayout;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.HomeLoadUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.logg.Logg;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 预估收益
 */
public class ShouyiActivity extends BaseFragmentActivity {

    @BindView(R.id.title_back_btn)
    ImageButton titleBackBtn;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.img_more_black)
    ImageView imgMoreBlack;
    @BindView(R.id.tablayout)
    XTabLayout mTablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shouyi_fragment);
        ButterKnife.bind(this);
        View topView = findViewById(R.id.topbar_layout);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        type = getIntent().getStringExtra("type");
        if (type != null) {
            init(type);
            initView();
            queryBrokerageDetailInfo();
        }
    }
    private void init(String type) {
        switch (type) {
            case "t1":
                titleText.setText("淘宝本月结算预估");
                break;
            case "t2":
                titleText.setText("淘宝上月结算预估");
                break;
            case "t3":
                titleText.setText("淘宝本月付款预估");
                break;
            case "t4":
                titleText.setText("淘宝上月付款预估");
                break;
            case "j1":
                titleText.setText("京东本月结算预估");
                break;
            case "j2":
                titleText.setText("京东上月结算预估");
                break;
            case "j3":
                titleText.setText("京东本月付款预估");
                break;
            case "j4":
                titleText.setText("京东上月付款预估");
                break;

        }

    }

    private void initView() {
        imgMoreBlack.setVisibility(View.VISIBLE);
        mTablayout.setxTabDisplayNum(4);
        setupViewPager(viewpager);
    }

    private void setupViewPager(ViewPager viewpager) {

    }


    /**
     * 查询报表明细
     */
    private void queryBrokerageDetailInfo() {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        maps.put("type", type);
        RetrofitClient.getInstance(ShouyiActivity.this).createBaseApi().queryBrokerageDetailInfo(
                maps, new BaseObserver<String>(ShouyiActivity.this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            if (jsonObject.optString("status").equals("1")) {
                                BrokerageDetailInfoBean brokerageDetailInfoBean = JSON.parseObject(content,BrokerageDetailInfoBean.class);
                                mTablayout.addTab(mTablayout.newTab().setText("全部"+"("+brokerageDetailInfoBean.getAll()+")"));
                                mTablayout.addTab(mTablayout.newTab().setText("自己"+"("+brokerageDetailInfoBean.getLevel1()+")"));
                                mTablayout.addTab(mTablayout.newTab().setText("一级粉丝"+"("+brokerageDetailInfoBean.getLevel2()+")"));
                                mTablayout.addTab(mTablayout.newTab().setText("N级粉丝"+"("+brokerageDetailInfoBean.getLevel3()+")"));
                                MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
                                adapter.addFragment(ShouyiFragment.newInstance(type,"0",brokerageDetailInfoBean.getAllmoney()), "全部"+"("+brokerageDetailInfoBean.getAll()+")");
                                adapter.addFragment(ShouyiFragment.newInstance(type,"1",brokerageDetailInfoBean.getLevel1money()), "自己"+"("+brokerageDetailInfoBean.getLevel1()+")");
                                adapter.addFragment(ShouyiFragment.newInstance(type,"2",brokerageDetailInfoBean.getLevel2money()), "一级粉丝"+"("+brokerageDetailInfoBean.getLevel2()+")");
                                adapter.addFragment(ShouyiFragment.newInstance(type,"3",brokerageDetailInfoBean.getLevel3money()), "N级粉丝"+"("+brokerageDetailInfoBean.getLevel3()+")");
                                viewpager.setAdapter(adapter);
                                //把tablayout与viewpager关联起来
                                mTablayout.setupWithViewPager(viewpager);
                            } else {
                                StringUtil.showToast(ShouyiActivity.this, jsonObject.optString("errmsg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                    }
                    @Override
                    protected void showDialog() {
                    }
                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                    }
                });
    }

    @OnClick({R.id.title_back_btn, R.id.img_more_black})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_btn:
                finish();
                break;
            case R.id.img_more_black:
                HomeLoadUtil.showItemPop(this,view);
                break;
        }
    }


    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragment = new ArrayList<Fragment>();
        private final List<String> mFragmentTitle = new ArrayList<String>();

        public void addFragment(Fragment fragment, String title) {
            mFragment.add(fragment);
            mFragmentTitle.add(title);
        }

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragment.get(position);
        }

        @Override
        public int getCount() {
            return mFragment.size();

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitle.get(position);
        }
    }
}
