package com.bbk.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.bbk.activity.BidMyListDetailActivity;
import com.bbk.activity.R;
import com.bbk.adapter.MyFragmentPagerAdapter;
import com.bbk.chat.ui.ContactFragment;
import com.bbk.chat.ui.ConversationFragment;
import com.bbk.chat.ui.SettingFragment;
import com.bbk.flow.DataFlow;
import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.util.ImmersionUtil;
import com.bbk.view.CustomViewPager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息
 */

public class BidMessageFragment extends BaseViewPagerFragment implements ResultEvent,View.OnClickListener{

    private View mView;
    private DataFlow6 dataFlow;
    private ImageView msgUnread;
//    private List<TextView> tlist = new ArrayList<>();
//    private List<View> vlist = new ArrayList<>();
    private TextView mtext2,mtext1;
    private RelativeLayout meverydayjb,mmorejb;
    private View henggang1,henggang2;
    private int curclick = 0;
    private CustomViewPager mViewPager;
    private MyFragmentPagerAdapter mPagerAdapter;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private ImageView topbar_goback_btn;
    private View data_head;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);
        // TODO 自动生成的方法存根
        /***
         * 判断Fragment是否已经添加了contentView（第一次加载时，可以将view保存下 来，再  次加载时，判断保存下来的view是否为null），
         * 如果保存的view为null，返回新的view ，否则，先将 保存的view从父view中移除，然后将该view返回出去
         */
        if (mView != null) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
            return mView;
        }
        mView = inflater.inflate(R.layout.fragment_bid_message, container,false);
        data_head = mView.findViewById(R.id.data_head);
        ImmersionUtil.initstateView(getActivity(),data_head);
        dataFlow = new DataFlow6(getActivity());
        initView();
        initData();
        return mView;
    }
    public void initView(){
        topbar_goback_btn = (ImageView)mView.findViewById(R.id.topbar_goback_btn);
        topbar_goback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        meverydayjb = (RelativeLayout) mView.findViewById(R.id.meverydayjb);
        mmorejb = (RelativeLayout) mView.findViewById(R.id.mmorejb);
        mViewPager = mView.findViewById(R.id.mviewpager);
        mViewPager.setScanScroll(false);//
        msgUnread = (ImageView)mView.findViewById(R.id.tabUnread) ;
        mtext1 = (TextView) mView.findViewById(R.id.mtext1);
        mtext2 = (TextView) mView.findViewById(R.id.mtext2);
        henggang1 =mView. findViewById(R.id.henggang1);
        henggang2 =mView.findViewById(R.id.henggang2);
        mtext1.setTextColor(getActivity().getResources().getColor(R.color.biao_color));
        henggang1.setVisibility(View.VISIBLE);
        mtext2.setTextColor(getActivity().getResources().getColor(R.color.black));
        henggang2.setVisibility(View.GONE);
        meverydayjb.setOnClickListener(this);
        mmorejb.setOnClickListener(this);
    }
    private void initData() {
        initViewPager();
        initViewPagerData();
        mViewPager.setCurrentItem(0);
    }
    public void initViewPager() {
        fragments.clear();
        mPagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), fragments);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int i) {
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
        BidInformFragment bidInformFragment = new BidInformFragment();
        BidChatFragment bidChatFragment = new BidChatFragment();
        fragments.add(bidInformFragment);
        fragments.add(bidChatFragment);
        mPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.meverydayjb:
                mtext1.setTextColor(getActivity().getResources().getColor(R.color.biao_color));
                henggang1.setVisibility(View.VISIBLE);
                mtext2.setTextColor(getActivity().getResources().getColor(R.color.black));
                henggang2.setVisibility(View.GONE);
                mViewPager.setCurrentItem(0);
                break;
            case R.id.mmorejb:
                mtext1.setTextColor(getActivity().getResources().getColor(R.color.black));
                henggang1.setVisibility(View.GONE);
                mtext2.setTextColor(getActivity().getResources().getColor(R.color.biao_color));
                henggang2.setVisibility(View.VISIBLE);
                mViewPager.setCurrentItem(1);
                break;
            default:
                break;
        }
    }
    /**
     * 设置未读tab显示
     */
    public void setMsgUnread(boolean noUnread){
        msgUnread.setVisibility(noUnread?View.GONE:View.VISIBLE);
    }

    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {

    }

    @Override
    public void lazyLoad() {

    }

}
