package com.bbk.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.bbk.adapter.MyFragmentPagerAdapter;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.fragment.BidChatFragment;
import com.bbk.fragment.BidInformFragment;
import com.bbk.fragment.EverydayJbFragment;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.fragment.MoreJbFragment;
import com.bbk.view.CustomViewPager;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CoinGoGoGoActivity extends BaseFragmentActivity implements OnClickListener,ResultEvent{
	private List<TextView> tlist = new ArrayList<>();
	private List<View> vlist = new ArrayList<>();
	private ImageButton topbar_goback_btn;
	private TextView mtext2,mtext1;
	private RelativeLayout meverydayjb,mmorejb;
	private View henggang1,henggang2;
	private DataFlow dataFlow;
	private int curclick = 0;
	private CustomViewPager mViewPager;
	private MyFragmentPagerAdapter mPagerAdapter;
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	private View data_head;
	private String type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coin_go_go_go);
		type = getIntent().getStringExtra("type");
		dataFlow = new DataFlow(this);
		data_head = findViewById(R.id.data_head);
		ImmersedStatusbarUtils.initAfterSetContentView(this, data_head);
		ImmersedStatusbarUtils.FlymeSetStatusBarLightMode(getWindow(),true);
		ImmersedStatusbarUtils.MIUISetStatusBarLightMode(this,true);
		initstateView();
		initView();
		initData();
	}

	private void initView() {
		topbar_goback_btn = (ImageButton) findViewById(R.id.topbar_goback_btn);
		meverydayjb = (RelativeLayout) findViewById(R.id.meverydayjb);
		mmorejb = (RelativeLayout) findViewById(R.id.mmorejb);
		mViewPager = (CustomViewPager) findViewById(R.id.mviewpager);
		mViewPager.setScanScroll(false);
		mtext1 = (TextView) findViewById(R.id.mtext1);
		mtext2 = (TextView) findViewById(R.id.mtext2);
		henggang1 = findViewById(R.id.henggang1);
		henggang2 = findViewById(R.id.henggang2);
		tlist.add(mtext1);
		tlist.add(mtext2);
		vlist.add(henggang1);
		vlist.add(henggang2);
		
		meverydayjb.setOnClickListener(this);
		mmorejb.setOnClickListener(this);
		topbar_goback_btn.setOnClickListener(this);
	}

	private void initData() {
		initViewPager();
		initViewPagerData();
		mViewPager.setCurrentItem(Integer.valueOf(type));
	}
	public void initViewPager() {
		fragments.clear();
		mPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int i) {
				clickTabtitle(i);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}
	private int getStatusBarHeight() {  
        Class<?> c = null;  
  
        Object obj = null;  
  
        Field field = null;  
  
        int x = 0, sbar = 0;  
  
        try {  
  
            c = Class.forName("com.android.internal.R$dimen");  
  
            obj = c.newInstance();  
  
            field = c.getField("status_bar_height");  
  
            x = Integer.parseInt(field.get(obj).toString());  
  
            sbar = getResources().getDimensionPixelSize(x);  
  
        } catch (Exception e1) {  
  
            e1.printStackTrace();  
  
        }  
  
        return sbar;  
    } 
	private void initstateView() {
		if (Build.VERSION.SDK_INT >=19) {
			data_head.setVisibility(View.VISIBLE);
		}
		int result = getStatusBarHeight();
		LayoutParams layoutParams = data_head.getLayoutParams();
		layoutParams.height = result;
		data_head.setLayoutParams(layoutParams);
	}
	public void initViewPagerData() {
		MoreJbFragment moreJbFragment = new MoreJbFragment();
		EverydayJbFragment everydayJbFragment = new EverydayJbFragment();
		fragments.add(everydayJbFragment);
		fragments.add(moreJbFragment);
		mPagerAdapter.notifyDataSetChanged();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topbar_goback_btn:
			finish();
			break;
		case R.id.meverydayjb:
			clickTabtitle(0);
			mViewPager.setCurrentItem(0);
			break;
		case R.id.mmorejb:
			clickTabtitle(1);
			mViewPager.setCurrentItem(1);
			break;

		default:
			break;
		}
	}
	private void clickTabtitle(int i) {
		if (curclick != i) {
			tlist.get(0).setTextColor(Color.parseColor("#333333"));
			tlist.get(1).setTextColor(Color.parseColor("#333333"));
			vlist.get(0).setBackgroundColor(Color.parseColor("#ffffff"));
			vlist.get(1).setBackgroundColor(Color.parseColor("#ffffff"));
			tlist.get(i).setTextColor(Color.parseColor("#ff7d41"));
			vlist.get(i).setBackgroundColor(Color.parseColor("#ff7d41"));
			curclick = i;
		}

	}

	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
		// TODO Auto-generated method stub
		
	}
}
