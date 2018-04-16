package com.bbk.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.bbk.adapter.MyCoinAdapter;
import com.bbk.flow.DataFlow;
import com.bbk.flow.DataFlow2;
import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.resource.Constants;
import com.bbk.util.BaseTools;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.CircleImageView1;
import com.bbk.view.MyListView;
import com.bumptech.glide.Glide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MyCoinActivity extends BaseActivity implements ResultEvent ,OnClickListener,OnItemClickListener{
	private View data_head;
	private RelativeLayout mbackground, msign;
	private MyListView mlistview;
	private String[] str1 = { "邀请好友下载APP", "签到打卡", "新注册用户", "完善资料", "查历史价", "收藏奖励", "爆料奖励", "评论奖励", "意见反馈" };
	private String[] str2 = { "+500鲸币", "+5至50鲸币", "+800鲸币", "+5鲸币", "+15鲸币", "+5鲸币", "+50鲸币", "+10鲸币", "+50鲸币" };
	private String[] str3 = { "每邀请一个好友注册成功", "连续签到7天+50鲸币，每轮7天", "新用户注册成功", "完善个人资料", "查询商品历史价格一次(限10次/日)", "每日收藏商品一件",
			"发表一条图文信息并审核通过", "文章评论内容大于10个字", "反馈有效意见或建议" };
	private int[] drawable = { R.mipmap.rw_1, R.mipmap.rw_2, R.mipmap.rw_3, R.mipmap.rw_4, R.mipmap.jinbi_clsj,
			R.mipmap.rw_6, R.mipmap.rw_7, R.mipmap.rw_8, R.mipmap.rw_10 };
	private List<Map<String, Object>> list;
	private MyCoinAdapter adapter;
	private DataFlow6 dataFlow;
	private String userID;
	private TextView mjbcoin, mjbdetail, msigntext;
	private View henggang1, henggang2, henggang3, henggang4, henggang5, henggang6;
	private List<View> hlist = new ArrayList<>();
	private List<ImageView> ilist = new ArrayList<>();
	private ImageView userimg, mimg1, mimg2, mimg3, mimg4, mimg5, mimg6, mimg7;
	private ScrollView mscroll;
	private boolean issign = true;
	private String signnum = "";
	private ViewGroup anim_mask_layout;
	private LinearLayout morezhuanpan; 
	private int thisday = 0;
	private ImageButton topbar_goback_btn;
	private TextView textjingbi;
	private LinearLayout jinbinow;
	private boolean isoncreat = false;
	private int num;
	private TextView mrule;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_coin);
		dataFlow = new DataFlow6(this);
		data_head = findViewById(R.id.data_head);
		ImmersedStatusbarUtils.initAfterSetContentView(this, data_head);
		ImmersedStatusbarUtils.FlymeSetStatusBarLightMode(getWindow(),true);
		ImmersedStatusbarUtils.MIUISetStatusBarLightMode(this,true);
		initstateView();
		initView();
		initData();
	}

	private void initView() {
		list = new ArrayList<>();
		anim_mask_layout = createAnimLayout(); 
		topbar_goback_btn = (ImageButton) findViewById(R.id.topbar_goback_btn);
		morezhuanpan = (LinearLayout) findViewById(R.id.morezhuanpan);
		jinbinow = (LinearLayout) findViewById(R.id.jinbinow);
		mbackground = (RelativeLayout) findViewById(R.id.mbackground);
		msign = (RelativeLayout) findViewById(R.id.msign);
		mscroll = (ScrollView) findViewById(R.id.mscroll);
		mlistview = (MyListView) findViewById(R.id.mlistview);
		mjbcoin = (TextView) findViewById(R.id.mjbcoin);
		mjbdetail = (TextView) findViewById(R.id.mjbdetail);
		textjingbi = (TextView) findViewById(R.id.textjingbi);
		mrule = (TextView) findViewById(R.id.mrule);
		msigntext = (TextView) findViewById(R.id.msigntext);
		mjbdetail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MyCoinActivity.this, CoinGoGoGoActivity.class);
				intent.putExtra("type", "0");
				startActivity(intent);
			}
		});
		userimg = (ImageView) findViewById(R.id.user_img);
		mimg1 = (ImageView) findViewById(R.id.mimg1);
		mimg2 = (ImageView) findViewById(R.id.mimg2);
		mimg3 = (ImageView) findViewById(R.id.mimg3);
		mimg4 = (ImageView) findViewById(R.id.mimg4);
		mimg5 = (ImageView) findViewById(R.id.mimg5);
		mimg6 = (ImageView) findViewById(R.id.mimg6);
		mimg7 = (ImageView) findViewById(R.id.mimg7);

		henggang1 = findViewById(R.id.henggang1);
		henggang2 = findViewById(R.id.henggang2);
		henggang3 = findViewById(R.id.henggang3);
		henggang4 = findViewById(R.id.henggang4);
		henggang5 = findViewById(R.id.henggang5);
		henggang6 = findViewById(R.id.henggang6);
		hlist.add(henggang1);
		hlist.add(henggang2);
		hlist.add(henggang3);
		hlist.add(henggang4);
		hlist.add(henggang5);
		hlist.add(henggang6);
		ilist.add(mimg1);
		ilist.add(mimg2);
		ilist.add(mimg3);
		ilist.add(mimg4);
		ilist.add(mimg5);
		ilist.add(mimg6);
		ilist.add(mimg7);
		msign.setOnClickListener(this);
		morezhuanpan.setOnClickListener(this);
		topbar_goback_btn.setOnClickListener(this);
		jinbinow.setOnClickListener(this);
		mrule.setOnClickListener(this);

	}

	private void initData() {
		for (int i = 0; i < str1.length; i++) {
			Map<String, Object> map = new HashMap<>();
			map.put("str1", str1[i]);
			map.put("str2", str2[i]);
			map.put("str3", str3[i]);
			map.put("drawable", drawable[i]);
			map.put("isgo", "0");
			list.add(map);
		}
		adapter = new MyCoinAdapter(list, this);
		mlistview.setAdapter(adapter);
		mlistview.setOnItemClickListener(this);
		mscroll.fullScroll(View.FOCUS_UP);
		userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
		if (!TextUtils.isEmpty(userID)) {
			HashMap<String, String> paramsMap = new HashMap<>();
			paramsMap.put("userid", userID);
			dataFlow.requestData(1, "newService/queryUserJngbiMainByUserid", paramsMap, this);
		}
	}
	private void initdata(){
		if (!TextUtils.isEmpty(userID)) {
			HashMap<String, String> paramsMap = new HashMap<>();
			paramsMap.put("userid", userID);
			dataFlow.requestData(1, "newService/queryUserJngbiMainByUserid", paramsMap, this,false);
		}
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
		if (Build.VERSION.SDK_INT >= 19) {
			data_head.setVisibility(View.VISIBLE);
		}
		int result = getStatusBarHeight();
		LayoutParams layoutParams = data_head.getLayoutParams();
		layoutParams.height = result;
		data_head.setLayoutParams(layoutParams);
	}

	/**
	 * @Description: 创建动画层 @param @return void @throws
	 * 
	 */
	private ViewGroup createAnimLayout() {
		ViewGroup rootView = (ViewGroup) getWindow().getDecorView();
		LinearLayout animLayout = new LinearLayout(this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		animLayout.setLayoutParams(lp);
		animLayout.setId(R.id.anim_mask_layout);
		animLayout.setBackgroundResource(android.R.color.transparent);
		rootView.addView(animLayout);
		return animLayout;
	}

	/**
	 * @Description: 添加视图到动画层 @param @param vg @param @param view @param @param
	 * location @param @return @return View @throws
	 */
	private View addViewToAnimLayout(final ViewGroup vg) {
		// int x = location[0];
		// int y = location[1];
		ImageView view = new ImageView(this);
		view.setImageResource(R.mipmap.u_jb);
		vg.addView(view);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.height = BaseTools.getPixelsFromDp(this, 20);
		lp.width = BaseTools.getPixelsFromDp(this, 20);
		// lp.leftMargin = x;
		// lp.topMargin = y;
		WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
		 int width = wm.getDefaultDisplay().getWidth();
		 float x1 = width/2;
		float y1 = msign.getY();
		view.setX(x1);
		view.setY(y1);
		view.setLayoutParams(lp);
		return view;
	}

	private View addTViewToAnimLayout(final ViewGroup vg) {
		// int x = location[0];
		// int y = location[1];
		TextView view = new TextView(this);
		view.setText(signnum);
		view.setTextColor(Color.parseColor("#f23030"));
		view.setTextSize(BaseTools.getPixelsFromDp(this, 16));
		vg.addView(view);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		// lp.leftMargin = x;
		// lp.topMargin = y;
		WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
		 int width = wm.getDefaultDisplay().getWidth();
		 float x1 = width/2;
		float y1 = msign.getY();
		view.setX(x1);
		view.setY(y1);
		view.setLayoutParams(lp);
		return view;
	}

	/**
	 * 平移动画
	 * 
	 * @return
	 */
	private TranslateAnimation initAnimations_One() {
		/** 设置位移动画 向右位移150 */
		final TranslateAnimation animation = new TranslateAnimation(0, 0, 0, 500);
		animation.setDuration(2000);// 设置动画持续时间
		animation.setRepeatCount(2);// 设置重复次数
		animation.setRepeatMode(Animation.REVERSE);// 设置反方向执行
		return animation;
	}

	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
		mscroll.setVisibility(View.VISIBLE);
		switch (requestCode) {
		case 1:
			try {
				JSONObject object = new JSONObject(content);
				String day = object.optString("continuous_day");
				if (!object.optString("u_imgurl").equals("")) {
					CircleImageView1.getImg(this, object.optString("u_imgurl"), userimg);
				}
				String jinbi = object.optString("jinbi");
				String str = object.optString("addjinbi");
				textjingbi.setText("今天获得"+str+"鲸币");
				msigntext.setText("签到+"+str+"鲸币");
				signnum = "+"+str;
				num = Integer.valueOf(jinbi) + Integer.valueOf(str);
				mjbcoin.setText(object.optString("jinbi"));
				initday(day,jinbi);
				for (int i = 0; i < list.size(); i++) {
					initisgo(i, object);
				}
				adapter.notifyDataSetChanged();
				mscroll.smoothScrollTo(0, 0);
				mbackground.setFocusable(true);  
				mbackground.setFocusableInTouchMode(true);  
				mbackground.requestFocus();  
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 3:
			textjingbi.setText("今天获得"+content+"鲸币");
			sign();
			
			break;
		default:
			break;
		}
	}

	private void initday(String day, String jinbi) {
		inithenggang(Integer.valueOf(day));
		thisday = Integer.valueOf(day)+1;
	}

//	private void initsigntext(String day, Integer jinbi) {
//		switch (day) {
//		case "0":
//			
//			signnum = "+5";
//			
//			num = jinbi+5;
//			break;
//		case "1":
//			signnum = "+10";
//			num = jinbi+10;
//			break;
//		case "2":
//			msigntext.setText("签到+15鲸币");
//			signnum = "+15";
//			num = jinbi+15;
//			break;
//		case "3":
//			msigntext.setText("签到+20鲸币");
//			signnum = "+20";
//			num = jinbi+20;
//			break;
//		case "4":
//			msigntext.setText("签到+25鲸币");
//			signnum = "+25";
//			num = jinbi+25;
//			break;
//		case "5":
//			msigntext.setText("签到+30鲸币");
//			signnum = "+30";
//			num = jinbi+30;
//			break;
//		case "6":
//			msigntext.setText("签到+50鲸币");
//			signnum = "+50";
//			num = jinbi+50;
//			break;
//
//		default:
//			break;
//		}
//	}

	private void inithenggang(int i) {
		for (int k = 0; k < i; k++) {
			ilist.get(k).setImageResource(R.mipmap.day_00);
		}
		if (i >= 2) {
			for (int j = 2; j < i + 1; j++) {
				hlist.get(j - 2).setBackgroundColor(Color.parseColor("#ff7d41"));
			}
		}

	}

	private void initisgo(int i, JSONObject object) {
		Map<String, Object> map = list.get(i);
		if (i == 0) {
			map.put("isgo", object.optString("1"));
		} else if (i == 1) {
			map.put("isgo", object.optString("9"));
			if (object.optString("9").equals("1")) {
				 msigntext.setText("已签到"); 
				 textjingbi.setVisibility(View.VISIBLE);
				 issign = false;
			}
		} else {
			map.put("isgo", object.optString(String.valueOf(i)));
		}

	}
	private void sign() {
		 View view = addViewToAnimLayout(anim_mask_layout); 
		 int[] end_location = new int[2];   
		 Animation mTranslateAnimation = new TranslateAnimation 
				 (TranslateAnimation.RELATIVE_TO_SELF, 0.0f, TranslateAnimation.ABSOLUTE,
						 800, TranslateAnimation.RELATIVE_TO_SELF, 0.0f, TranslateAnimation.ABSOLUTE, -1000);// 移动  
		 mTranslateAnimation.setDuration(500); 
		 final View view2 = addTViewToAnimLayout(anim_mask_layout);
		 Animation mHiddenAction = AnimationUtils.loadAnimation(this, R.anim.pingyi_shang);  
		 view2.setAnimation(mHiddenAction);
		 mHiddenAction.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				view2.setVisibility(View.GONE);
			}
		});
		 mHiddenAction.start();
		 AnimationSet mAnimationSet=new AnimationSet(false); //这块要注意，必须设为false,不然组件动画结束后，不会归位。 
		 mAnimationSet.setFillAfter(false);   
		 mAnimationSet.addAnimation(mTranslateAnimation); 
		 view.startAnimation(mAnimationSet);
		 mAnimationSet.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				anim_mask_layout.getChildAt(0).setVisibility(View.GONE);
//				vg.addView(mjbimg);
			}
		});
		 String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
		 msigntext.setText("已签到"); 
		 textjingbi.setVisibility(View.VISIBLE);
		 mjbcoin.setText(num+"");
		 inithenggang(thisday);
		 list.get(1).put("isgo", "1");
		 adapter.notifyDataSetChanged();
		 
	}
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.msign:
			if (!TextUtils.isEmpty(userID)) {
				if (issign) {
					Map<String, String> params = new HashMap<>();
					params.put("userid", userID);
					dataFlow.requestData(3, "newService/userSign", params, this,false);
					issign = false;
				}
			}else{
				Toast.makeText(this, "请先登录！", Toast.LENGTH_SHORT).show();
				intent = new Intent(this, UserLoginNewActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.morezhuanpan:
			String mid = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "mid");
			if (!TextUtils.isEmpty(userID)) {
				intent = new Intent(this, WebViewActivity.class);
				String url = Constants.MAIN_BASE_URL_MOBILE +"mobile/user/lottery?mid=";
				intent.putExtra("url", url+mid);
				startActivity(intent);
			}
			break;
		case R.id.topbar_goback_btn:
			finish();
			break;
		case R.id.mrule:
			intent = new Intent(this, WebViewActivity.class);
			String url = Constants.MAIN_BASE_URL_MOBILE +"mobile/user/moneyRule";
			intent.putExtra("url", url);
			startActivity(intent);
			break;
		case R.id.jinbinow:
			intent = new Intent(this, CoinGoGoGoActivity.class);
			intent.putExtra("type", "0");
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Map<String, Object> map = list.get(position);
		String isgo = map.get("isgo").toString();
		if (isgo.equals("0")) {
			Intent intent;
			switch (position) {
			case 0:
				intent = new Intent(this, CoinGoGoGoActivity.class);
				intent.putExtra("type", "1");
				startActivity(intent);
				break;
			case 1:
				mscroll.fullScroll(View.FOCUS_UP);
				break;
			case 2:
				
				break;
			case 3:
				intent = new Intent(this, UserAccountActivity.class);
				startActivity(intent);
				break;
			case 4:
//				SharedPreferencesUtil.putSharedData(getApplicationContext(), "homeactivty", "type", "0");
				intent = new Intent(this, QueryHistoryActivity.class);
				startActivity(intent);
				break;
			case 5:
				intent = new Intent(this, HomeActivity.class);
				SharedPreferencesUtil.putSharedData(getApplicationContext(), "homeactivty", "type", "0");
				startActivity(intent);
				break;
			case 6:
				intent = new Intent(this, MyGossipActivity.class);
				startActivity(intent);
				break;
			case 7:
				SharedPreferencesUtil.putSharedData(getApplicationContext(), "homeactivty", "type", "3");
				intent = new Intent(this, HomeActivity.class);
				startActivity(intent);
				break;
			case 8:
				intent = new Intent(this, UserSuggestionActivity.class);
				startActivity(intent);
				break;
			default:
				break;
			}
		}
		
	}
	@Override
	protected void onResume() {
		super.onResume();
		if (isoncreat) {
			System.gc();
			initdata();
		}else{
			isoncreat = true;
		}
	}
}
