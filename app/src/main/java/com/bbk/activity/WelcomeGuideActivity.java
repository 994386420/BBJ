package com.bbk.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.baidu.mobstat.StatService;
import com.bbk.resource.Constants;
import com.bbk.util.ImageUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bumptech.glide.Glide;
import com.tencent.tauth.Tencent;

public class WelcomeGuideActivity extends BaseActivity {

	private int[] pics = { 
			R.mipmap.splash1,
			R.mipmap.splash2,
			R.mipmap.splash3 };
	private ViewPager pager;
	private List<View> mListViews;
	private MyViewPagerAdapter mAdapter;
	private List<Bitmap> bitmaps = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome_guide);
//		clearMemory();
		StatService.onEvent(this, "loginout", "退出登录:个人设置页面");
		Tencent mTencent = Tencent.createInstance(Constants.QQ_APP_ID, getApplicationContext());
		mTencent.logout(this);
		SharedPreferencesUtil.cleanShareData(getApplicationContext(), "userInfor");
		TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		String token = TelephonyMgr.getDeviceId();
		SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "userInfor", "token", token);
		pager = (ViewPager) findViewById(R.id.viewPager);
		Bitmap bm = ImageUtil.readBitMap(this, R.mipmap.welcome_guide_btn);
		BitmapDrawable bd = new BitmapDrawable(this.getResources(), bm);
		mListViews = new ArrayList<View>();
		mAdapter = new MyViewPagerAdapter(mListViews);
		pager.setAdapter(mAdapter);

		for (int i = 0; i < pics.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			Bitmap bitmap = ImageUtil.readBitMap(this, pics[i]);
			BitmapDrawable bt = new BitmapDrawable(this.getResources(), bitmap);
			imageView.setBackgroundDrawable(bt);
			bitmaps.add(bitmap);
/*			Glide
			.with(this)
			.load(pics[i])
			.diskCacheStrategy(DiskCacheStrategy.NONE)
			.signature(new StringSignature(DateUtil.getDate()))
			.dontAnimate()
			.centerCrop()
			.into(imageView);*/
			mListViews.add(imageView);
			if (i == 2) {
				imageView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						startActivity(new Intent(WelcomeGuideActivity.this,HomeActivity.class));
						finish();
					}
				});
			}
		}

		mAdapter.notifyDataSetChanged();
		
	}

	public class MyViewPagerAdapter extends PagerAdapter {
		private List<View> mListViews;

		public MyViewPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;// 构造方法，参数是我们的页卡，这样比较方便。
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mListViews.get(position));// 删除页卡
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) { // 这个方法用来实例化页卡
			container.addView(mListViews.get(position), 0);// 添加页卡
			return mListViews.get(position);
		}

		@Override
		public int getCount() {
			return mListViews.size();// 返回页卡的数量
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;// 官方提示这样写
		}
	}

	public void clearMemory() {
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				Glide.get(WelcomeGuideActivity.this).clearDiskCache();
			}
		});
		Glide.get(this).clearMemory();
	}

	@Override
	protected void onDestroy() {
		for(Bitmap bitmap : bitmaps){
			bitmap.recycle();
		}
		bitmaps.clear();
//		clearMemory();
		setContentView(R.layout.view_null);
		System.gc();
		super.onDestroy();
	}

}
