package com.bbk.activity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.bbk.adapter.CustomFragmentPagerAdapter;
import com.bbk.entity.XGMessageEntity;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.fragment.BaseViewPagerFragment;
import com.bbk.fragment.BidMessageFragment;
import com.bbk.fragment.DataFragment;
import com.bbk.fragment.GossipPiazzaFragment;
import com.bbk.fragment.HomeFragment2;
import com.bbk.fragment.NewHomeFragment;
import com.bbk.fragment.NewRankFragment;
import com.bbk.fragment.RankFragment;
import com.bbk.fragment.UserFragment;
import com.bbk.resource.Constants;
import com.bbk.update.UpdateChecker;
import com.bbk.update.UpdateVersionService;
import com.bbk.util.BaseTools;
import com.bbk.util.DensityUtil;
import com.bbk.util.EventIdIntentUtil;
import com.bbk.util.HttpUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.LoadImgUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler.Response;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;
import com.umeng.analytics.MobclickAgent;

import okhttp3.internal.framed.Variant;

public class HomeActivity extends BaseFragmentActivity implements Response, ResultEvent {

	private static final int TAB_SIZE = 5;
	private int screenWidth = 0;
	private static ViewPager mViewPager;
	private CustomFragmentPagerAdapter mPagerAdapter;
	private ArrayList<BaseViewPagerFragment> fragments = new ArrayList<BaseViewPagerFragment>();
	private LinearLayout tabParentLayout;
	private int[] tabImgBlue = { R.mipmap.bottom_home02,R.mipmap.bottom_baoliao02,
			R.mipmap.new_bottom_news_btn, R.mipmap.bottom_find02, R.mipmap.bottom_my02 };
	private int[] tabImgGray = { R.mipmap.bottom_home01,R.mipmap.bottom_baoliao01,
			R.mipmap.new_bottom_news_normal, R.mipmap.bottom_find01, R.mipmap.bottom_my01 };
//	private int[] tabImgBlue = { R.mipmap.bottom_home02,R.mipmap.bottom_find02,R.mipmap.bottom_baoliao02
//			,R.mipmap.bottom_data02 , R.mipmap.bottom_my02 };
//	private int[] tabImgGray = { R.mipmap.bottom_home01,R.mipmap.bottom_find01,R.mipmap.bottom_baoliao01 ,
//			R.mipmap.bottom_data01, R.mipmap.bottom_my01 };
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
	public static boolean firstFlag = true,isAlertUpdate = false;
	private String ctcolor = "#ff7d41";
	private UpdateVersionService updateVersionService;
	private final String mPageName = "HomeActivity";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		instance = this;
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Constants.WEIBO_APP_KEY);
		mWeiboShareAPI.registerApp();
		dataFlow = new DataFlow(this);
		initView();
		initData();


	}

	@Override
	protected void onInit() {
		// 来自信鸽消息判断
		XGMessageEntity xgMessage = null;
		String xgString = getIntent().getStringExtra("xgMessage");
		if (xgString != null) {
			xgMessage = new Gson().fromJson(xgString, XGMessageEntity.class);
			Intent intent = null;
			switch (xgMessage.getStartType()) {
			case "update":
//				UpdateChecker updateChecker = new UpdateChecker(this);
//				updateChecker.checkForUpdates();
				isAlertUpdate = false;
				new Thread(updateRun).start();
				break;
			case "openWeb":
				intent = new Intent(this, PushWebViewActivity.class);
				intent.putExtra("url", xgMessage.getUrl());
				startActivity(intent);
				break;
			default:
				Class<?> cls = xgMessage.getActivityClass();
				if (cls != null) {
					intent = new Intent(this, cls);
					startActivity(intent);
				}
				break;
			}
		}
		if (xgMessage == null || !"update".equals(xgMessage.getStartType())) {
//			UpdateChecker updateChecker = new UpdateChecker(this);
//			updateChecker.checkBGForUpdates();
			isAlertUpdate = false;
			new Thread(updateRun).start();
		}
	}


	// 自动更新
	Runnable updateRun = new Runnable() {
		@Override
		public void run() {
			Looper.prepare();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			updateVersionService = new UpdateVersionService(HomeActivity.this);// 创建更新业务对象
			updateVersionService.checkForUpdates();// 调用检查更新的方法,如果可以更新.就更新.不能更新就提示已经是最新的版本了
			Looper.loop();
		}
	};

	public void initView() {
		TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		String token = TelephonyMgr.getDeviceId();
		SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "userInfor", "token", token);
		mViewPager = $(R.id.main_layout);
		tabParentLayout = $(R.id.tab_layout);
		mtext = $(R.id.mtext);
		mimg = $(R.id.home_img_btn);
		mzhezhao = $(R.id.mzhezhao);
		mzhezhao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
				
						if (isuserzhezhao) {
							mzhezhao.setVisibility(View.GONE);
						}else{
							mzhezhao.setImageResource(R.mipmap.app_qiandao);
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
		mViewPager.setOffscreenPageLimit(4);
		mViewPager.setCurrentItem(0);
		HashMap<String, String> paramsMap = new HashMap<>();
		dataFlow.requestData(1, "newService/queryIndexMenu", paramsMap, this, false);

	}

	public void initViewPager() {
		fragments.clear();
		mPagerAdapter = new CustomFragmentPagerAdapter(getSupportFragmentManager(), fragments);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
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
		NewHomeFragment homeFragment = new NewHomeFragment();
//		HomeFragment2 homeFragment = new HomeFragment2();
		NewRankFragment rankFragment = new NewRankFragment();
//		DataFragment dataFragment = new DataFragment();
//		RankFragment rankFragment = new RankFragment();
		BidMessageFragment bidMessageFragment = new BidMessageFragment();
		GossipPiazzaFragment gossipPiazzaFragment = new GossipPiazzaFragment();
		UserFragment userFragment = new UserFragment();
		fragments.add(homeFragment);//首页
		fragments.add(gossipPiazzaFragment);//爆料
		fragments.add(bidMessageFragment);//消息
		fragments.add(rankFragment);//发现
		fragments.add(userFragment);//我的
//		fragments.add(homeFragment);//首页
//		fragments.add(rankFragment);//发现
//		fragments.add(gossipPiazzaFragment);//爆料
//		fragments.add(dataFragment);//数据
//		fragments.add(userFragment);//我的
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
			// 如果不是在首页则显示图片加文字，在首页就只显示图片
			// if (index!=0) {
			// mtext.setVisibility(View.VISIBLE);
			// android.widget.LinearLayout.LayoutParams params =
			// (android.widget.LinearLayout.LayoutParams)
			// mimg.getLayoutParams();
			// params.height = not;
			// params.topMargin = BaseTools.getPixelsFromDp(this, 5);
			// mimg.setLayoutParams(params);

			// }else{
			//// mtext.setVisibility(View.GONE);
			// android.widget.LinearLayout.LayoutParams params =
			// (android.widget.LinearLayout.LayoutParams)
			// mimg.getLayoutParams();
			// params.height = select;
			// params.topMargin = BaseTools.getPixelsFromDp(this, 0);
			// mimg.setLayoutParams(params);
			// }
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

	public void clickTab() {
		for (int i = 0; i < TAB_SIZE; i++) {
			final int index = i;
			LinearLayout tabLayout = (LinearLayout) tabParentLayout.getChildAt(i);
			tabLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					 if (index == 4) {
						 try {
							 String isFirstResultUse = SharedPreferencesUtil.getSharedData(HomeActivity.this,"isFirstUse", "isFirstUserUse");
								if (TextUtils.isEmpty(isFirstResultUse)) {
									isFirstResultUse = "yes";
								}
								if (isFirstResultUse.equals("yes")) {
									SharedPreferencesUtil.putSharedData(HomeActivity.this, "isFirstUse","isFirstUserUse", "no");
									HomeActivity.mzhezhao.setVisibility(View.VISIBLE);
									HomeActivity.mzhezhao.setImageResource(R.mipmap.app_jingbi);
								} 
						} catch (Exception e) {
							// TODO: handle exception
						}
							
					 }
					 if (index == 2){
						 String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
						 if (TextUtils.isEmpty(userID)){
							 Intent intent4= new Intent(getApplicationContext(), UserLoginNewActivity.class);
							 startActivity(intent4);
						 }
					 }
					mViewPager.setCurrentItem(index);
				}
			});

		}
	}


	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {

		super.onActivityResult(arg0, arg1, arg2);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		mWeiboShareAPI.handleWeiboResponse(intent, this);

	}

	@Override
	public void onResponse(BaseResponse arg0) {
		switch (arg0.errCode) {
		case WBConstants.ErrorCode.ERR_OK:
			Toast.makeText(this, "分享成功", Toast.LENGTH_LONG).show();
			loadData();
			break;
		case WBConstants.ErrorCode.ERR_CANCEL:
			Toast.makeText(this, "分享取消", Toast.LENGTH_LONG).show();
			break;
		case WBConstants.ErrorCode.ERR_FAIL:
			Toast.makeText(this, "分享失败", Toast.LENGTH_LONG).show();
			break;
		}

	}

	private void loadData() {
//		Map<String, String> paramsMap = new HashMap<String, String>();
//		paramsMap.put("userid", SharedPreferencesUtil.getSharedData(getApplicationContext(), "userInfor", "userID"));
//		dataFlow.requestData(1, "newService/checkIsShare", paramsMap, this, false);
//		 HttpUtil.getHttp(paramsMap, Constants.MAIN_BASE_URL_MOBILE +
//		 "newService/checkIsShare",
//		 HomeActivity.this);
	}

	/**
	 * 菜单、返回键响应
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitBy2Click(); // 调用双击退出函数
		}
		return false;
	}

	/**
	 * 双击退出函数
	 */
	private static Boolean isExit = false;

	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else {
			finish();
			System.exit(0);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(mPageName);
		String type = SharedPreferencesUtil.getSharedData(getApplicationContext(), "homeactivty", "type");
		if (!TextUtils.isEmpty(type)) {
			SharedPreferencesUtil.cleanShareData(getApplicationContext(), "homeactivty");
			mViewPager.setCurrentItem(Integer.valueOf(type));
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
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

	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
		try {
			switch (requestCode){
                case 1:
					JSONObject object = new JSONObject(content);
					if ("1".equals(object.optString("isshow"))){
						isshow = true;
						JSONArray img = object.optJSONArray("img");
						JSONArray imgs = object.optJSONArray("imgs");
						for (int i = 0; i < 5; i++) {
							tabImgBlue2.add(imgs.optString(i));
							tabImgGray2.add(img.optString(i));
						}
						ctcolor = object.optString("ccolors");
						tcolor = object.optString("ccolor");
						bcolor = object.optString("bcolor");
						tabParentLayout.setBackgroundColor(Color.parseColor(bcolor));
						for (int i = 0; i < 5; i++) {
							LinearLayout nextLayout = ((LinearLayout) tabParentLayout.getChildAt(i));
							ImageView nextIV = (ImageView) nextLayout.getChildAt(0);
							if (isshow){
								Glide.with(this).load(tabImgGray2.get(i)).placeholder(tabImgGray[i]).into(nextIV);
							}else {
								nextIV.setImageResource(tabImgGray[i]);
							}
							TextView nextTV = (TextView) nextLayout.getChildAt(1);
							nextTV.setTextColor(Color.parseColor(tcolor));
						}
						LinearLayout currentLayout = ((LinearLayout) tabParentLayout.getChildAt(currentIndex));
						ImageView currentIV = (ImageView) currentLayout.getChildAt(0);
						if (isshow){
							Log.e("==================",""+tabImgGray2.get(currentIndex));
							Glide.with(this).
									load(tabImgBlue2.get(currentIndex))
									.placeholder(tabImgBlue[0]).
									into(currentIV);
						}else {
							currentIV.setImageResource(tabImgBlue[currentIndex]);
						}
						TextView currentTV = (TextView) currentLayout.getChildAt(1);
						currentTV.setTextColor(Color.parseColor(ctcolor));
					}else {
                		isshow = false;
                		switchTab(currentIndex);
					}
                    break;
            }
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	 public interface MyTouchListener
	 {
	         public void onTouchEvent(MotionEvent event);
	 }
	 /*
	 * 保存MyTouchListener接口的列表
	 */
	 private ArrayList<MyTouchListener> myTouchListeners = new ArrayList<HomeActivity.MyTouchListener>();
	 /**
	 * 提供给Fragment通过getActivity()方法来注册自己的触摸事件的方法
	 * @param listener
	 */
	 public void registerMyTouchListener(MyTouchListener listener)
	 {
	         myTouchListeners.add( listener );
	 }
	 /**
	 * 提供给Fragment通过getActivity()方法来取消注册自己的触摸事件的方法
	 * @param listener
	 */
	 public void unRegisterMyTouchListener(MyTouchListener listener)
	 {
	         myTouchListeners.remove( listener );
	 }
	 /**
	 * 分发触摸事件给所有注册了MyTouchListener的接口
	 */
	 @Override
	 public boolean dispatchTouchEvent(MotionEvent ev) {
	         // TODO Auto-generated method stub 
	         for (MyTouchListener listener : myTouchListeners) {
	                        listener.onTouchEvent(ev);
	         }
	         return super.dispatchTouchEvent(ev);
	 }
}

