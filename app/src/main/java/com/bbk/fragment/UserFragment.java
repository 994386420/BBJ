package com.bbk.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


import com.ali.auth.third.login.callback.LogoutCallback;
import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.alibaba.baichuan.android.trade.callback.AlibcLoginCallback;
import com.andview.refreshview.XRefreshView;
import com.bbk.activity.AboutUsActivity;
import com.bbk.activity.BidListDetailActivity;
import com.bbk.activity.BidMyListDetailActivity;
import com.bbk.activity.BrowseActivity;
import com.bbk.activity.CollectionActivity;
import com.bbk.activity.ContactActivity;
import com.bbk.activity.LogisticsQueryActivity;
import com.bbk.activity.MesageCenterActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.MyCoinActivity;
import com.bbk.activity.MyGossipActivity;
import com.bbk.activity.OrderListActivity;
import com.bbk.activity.R;
import com.bbk.activity.ShopCartActivity;
import com.bbk.activity.UserAccountActivity;
import com.bbk.activity.UserLoginNewActivity;
import com.bbk.activity.UserSuggestionActivity;
import com.bbk.activity.WebViewActivity;
import com.bbk.chat.ui.ChatActivity;
import com.bbk.chat.ui.HomeActivity;
import com.bbk.chat.ui.SplashActivity;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.resource.Constants;
import com.bbk.util.BaseTools;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.DialogUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.CircleImageView1;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;

import okhttp3.internal.framed.Variant;

import static com.tencent.qalsdk.service.QalService.tag;

public class UserFragment extends BaseViewPagerFragment implements OnClickListener ,ResultEvent{

	private View mView;
	private Context context;
	private RelativeLayout helpLayout,newpinglun;

	public TextView isLoginTv;
	private TextView hasNewTv,sign,mjb,mcollectnum,mfootnum,mnewmsg,mJlzText;
	private View user_head;
	private ImageView mjbimg;
	private LinearLayout mbackground;
	private LinearLayout mjingbi, mcollection, mfoot, mphonechongzhi, mshopcart, morderlist, mlogisticsquery, mycomment,
			mygossip, mfeedback, mcallservices, mabout, msign,mtaobaologin,mFabiaoLayout,mJiebiaoLayout;
	private boolean issign = true;
	private DataFlow dataFlow;
	private String token;
	private String signnum = "";
	private ViewGroup anim_mask_layout;
	private ImageView user_img;
	private TextView user_name;
	private boolean isoncreat = false;
	private int num;
	private TextView mchongshi,mtaobaouser,mtaobaotext;
	private RelativeLayout mzhanwei;
	private XRefreshView xrefresh;
	private boolean isTaoBaoLogin = false;

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
			mView = inflater.inflate(R.layout.fragment_user, null);
			dataFlow = new DataFlow(getActivity());
			user_head = mView.findViewById(R.id.user_head);
			initstateView();
			initView();
			initData();

		return mView;
	}

	public void initView() {
		mFabiaoLayout = mView.findViewById(R.id.ll_my_fabiao);
		mJiebiaoLayout = mView.findViewById(R.id.ll_my_jiebiao);
		mJlzText = mView.findViewById(R.id.jlz_text);
		anim_mask_layout = createAnimLayout(); 
		newpinglun = (RelativeLayout) mView.findViewById(R.id.newpinglun);
		xrefresh = (XRefreshView) mView.findViewById(R.id.xrefresh);
		mzhanwei = (RelativeLayout) mView.findViewById(R.id.mzhanwei);
		user_img = (ImageView) mView.findViewById(R.id.user_img);
		CircleImageView1.getImg(getActivity(), R.mipmap.logo_01, user_img);
		mbackground = (LinearLayout) mView.findViewById(R.id.mbackground);
		sign = (TextView) mView.findViewById(R.id.sign);
		user_name = (TextView) mView.findViewById(R.id.user_name);
		mjb = (TextView) mView.findViewById(R.id.mjb);
		mcollectnum = (TextView) mView.findViewById(R.id.mcollectnum);
		mfootnum = (TextView) mView.findViewById(R.id.mfootnum);
		mnewmsg = (TextView) mView.findViewById(R.id.mnewmsg);
		mchongshi = (TextView) mView.findViewById(R.id.mchongshi);
		mtaobaotext = (TextView) mView.findViewById(R.id.mtaobaotext);
		mtaobaouser = (TextView) mView.findViewById(R.id.mtaobaouser);
		WindowManager wm = getActivity().getWindowManager();
		int width = wm.getDefaultDisplay().getWidth();
		LayoutParams params = (LayoutParams) mbackground.getLayoutParams();
		params.height = (width * 360) / 828;
		mbackground.setLayoutParams(params);
		mjingbi = (LinearLayout) mView.findViewById(R.id.mjingbi);
		mcollection = (LinearLayout) mView.findViewById(R.id.mcollection);
		mfoot = (LinearLayout) mView.findViewById(R.id.mfoot);
		mphonechongzhi = (LinearLayout) mView.findViewById(R.id.mphonechongzhi);
		mshopcart = (LinearLayout) mView.findViewById(R.id.mshopcart);
		morderlist = (LinearLayout) mView.findViewById(R.id.morderlist);
		mlogisticsquery = (LinearLayout) mView.findViewById(R.id.mlogisticsquery);
		mycomment = (LinearLayout) mView.findViewById(R.id.mycomment);
		mygossip = (LinearLayout) mView.findViewById(R.id.mygossip);
		mfeedback = (LinearLayout) mView.findViewById(R.id.mfeedback);
		mtaobaologin = (LinearLayout) mView.findViewById(R.id.mtaobaologin);
		mcallservices = (LinearLayout) mView.findViewById(R.id.mcallservices);
		mabout = (LinearLayout) mView.findViewById(R.id.mabout);
		msign = (LinearLayout) mView.findViewById(R.id.msign);
		mcallservices = (LinearLayout) mView.findViewById(R.id.mcallservices);
		mjbimg = (ImageView) mView.findViewById(R.id.mjbimg);
//		mjbimg1 = (ImageView) mView.findViewById(R.id.mjbimg1);
		mFabiaoLayout.setOnClickListener(this);
		mJiebiaoLayout.setOnClickListener(this);
		mjingbi.setOnClickListener(this);
		mfoot.setOnClickListener(this);
		mphonechongzhi.setOnClickListener(this);
		mshopcart.setOnClickListener(this);
		morderlist.setOnClickListener(this);
		mlogisticsquery.setOnClickListener(this);
		mycomment.setOnClickListener(this);
		mygossip.setOnClickListener(this);
		mfeedback.setOnClickListener(this);
		mcallservices.setOnClickListener(this);
		mabout.setOnClickListener(this);
		msign.setOnClickListener(this);
		user_img.setOnClickListener(this);
		newpinglun.setOnClickListener(this);
		mcollection.setOnClickListener(this);
		user_name.setOnClickListener(this);
		mtaobaologin.setOnClickListener(this);
		xrefresh.setXRefreshViewListener(new XRefreshView.XRefreshViewListener() {
			@Override
			public void onRefresh() {

			}

			@Override
			public void onRefresh(boolean isPullDown) {
				initData();
			}

			@Override
			public void onLoadMore(boolean isSilence) {

			}

			@Override
			public void onRelease(float direction) {

			}

			@Override
			public void onHeaderMove(double headerMovePercent, int offsetY) {

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

			sbar = getContext().getResources().getDimensionPixelSize(x);

		} catch (Exception e1) {

			e1.printStackTrace();

		}

		return sbar;
	}

	private void initstateView() {
		if (Build.VERSION.SDK_INT >= 19) {
			user_head.setVisibility(View.VISIBLE);
		}
		int result = getStatusBarHeight();
		LayoutParams layoutParams = user_head.getLayoutParams();
		layoutParams.height = result;
		user_head.setLayoutParams(layoutParams);
	}

	public void initData() {
		if (AlibcLogin.getInstance().getSession()!= null){
			String nick = AlibcLogin.getInstance().getSession().nick;
			if (nick!= null && !"".equals(nick)) {
				mtaobaotext.setText("取消淘宝授权");
				mtaobaouser.setText(nick);
				isTaoBaoLogin = true;
			} else {
				mtaobaotext.setText("淘宝授权登录");
				mtaobaouser.setText("");
				isTaoBaoLogin = false;
			}
		}else {
			mtaobaotext.setText("淘宝授权登录");
			mtaobaouser.setText("");
			isTaoBaoLogin = false;
		}

		String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
		TelephonyManager TelephonyMgr = (TelephonyManager)getActivity().getSystemService(getActivity().TELEPHONY_SERVICE);
		String token = TelephonyMgr.getDeviceId();
		SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "userInfor", "token", token);
		if (!TextUtils.isEmpty(userID)) {			
			Map<String, String> params = new HashMap<>();
			params.put("userid", userID);
			params.put("token", token);
			dataFlow.requestData(1, "newService/queryUserInfoMain", params, this,false);
		}else{
			Map<String, String> params = new HashMap<>();
			params.put("token", token);
			dataFlow.requestData(2, "newService/queryUserInfoMain", params, this,false);
			user_name.setText("请登录");
			user_img.setImageResource(R.mipmap.logo_01);
			mjb.setText("0");
		}
		
		
	}
	@Override
	public void lazyLoad() {
	}

	/**
	 * 淘宝授权登录
	 */
	private void TaoBaoLoginandLogout(){
		if (isTaoBaoLogin){
			DialogSingleUtil.show(getActivity(),"取消授权中...");
			AlibcLogin alibcLogin = AlibcLogin.getInstance();

			alibcLogin.logout(getActivity(), new LogoutCallback() {
				@Override
				public void onSuccess() {
					DialogSingleUtil.dismiss(0);
					Toast.makeText(getActivity(), "退出登录成功",
							Toast.LENGTH_SHORT).show();
					mtaobaotext.setText("淘宝授权登录");
					mtaobaouser.setText("");
					SharedPreferencesUtil.cleanShareData(MyApplication.getApplication(), "taobao");
					isTaoBaoLogin = false;
				}

				@Override
				public void onFailure(int code, String msg) {
					DialogSingleUtil.dismiss(0);
					Toast.makeText(getActivity(), "退出登录失败 " + code + msg,
							Toast.LENGTH_SHORT).show();
					isTaoBaoLogin = true;
				}
			});
		}else {
			DialogSingleUtil.show(getActivity(),"授权中...");
			final AlibcLogin alibcLogin = AlibcLogin.getInstance();

			alibcLogin.showLogin(getActivity(), new AlibcLoginCallback() {

				@Override
				public void onSuccess() {
					DialogSingleUtil.dismiss(0);
					Toast.makeText(getActivity(), "登录成功 ",
							Toast.LENGTH_LONG).show();
					mtaobaotext.setText("取消淘宝授权");
					mtaobaouser.setText(AlibcLogin.getInstance().getSession().nick.toString());
					isTaoBaoLogin = true;
					SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					String date = sDateFormat.format(new java.util.Date());
					SharedPreferencesUtil.putSharedData(MyApplication.getApplication(),"taobao","taobaodata",date);
					//获取淘宝用户信息
//					Log.e("=====淘宝授权=====", "获取淘宝用户信息: "+AlibcLogin.getInstance().getSession().nick);

				}
				@Override
				public void onFailure(int code, String msg) {
					DialogSingleUtil.dismiss(0);
					Toast.makeText(getActivity(), "登录失败 ",
							Toast.LENGTH_LONG).show();
				}
			});
		}
	}
	@Override
	public void onClick(View v) {
		String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
		Intent intent;
		switch (v.getId()) {
			case R.id.mtaobaologin:
				TaoBaoLoginandLogout();
				break;
		case R.id.mjingbi:
			if (TextUtils.isEmpty(userID)) {
				intent = new Intent(getActivity(), UserLoginNewActivity.class);
				startActivityForResult(intent, 1);
			} else {
				intent = new Intent(getActivity(), MyCoinActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.mcollection:
			if (TextUtils.isEmpty(userID)) {
				intent = new Intent(getActivity(), UserLoginNewActivity.class);
				startActivityForResult(intent, 1);
			} else {
				intent = new Intent(getActivity(), CollectionActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.mfoot:
			intent = new Intent(getActivity(), BrowseActivity.class);
			startActivity(intent);
			break;
		case R.id.mphonechongzhi:
			intent = new Intent(getActivity(), WebViewActivity.class);
			intent.putExtra("url", "http://www.bibijing.com/mobile/recharge");
			startActivity(intent);
			break;
		case R.id.mshopcart:
			if (TextUtils.isEmpty(userID)) {
				intent = new Intent(getActivity(), UserLoginNewActivity.class);
				startActivityForResult(intent, 1);
			} else {
				intent = new Intent(getActivity(), ShopCartActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.morderlist:
			if (TextUtils.isEmpty(userID)) {
				intent = new Intent(getActivity(), UserLoginNewActivity.class);
				startActivityForResult(intent, 1);
			} else {
				intent = new Intent(getActivity(), OrderListActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.mlogisticsquery:
			intent = new Intent(getActivity(), LogisticsQueryActivity.class);
			startActivity(intent);
			break;
		case R.id.mycomment:
			if (TextUtils.isEmpty(userID)) {
				intent = new Intent(getActivity(), UserLoginNewActivity.class);
				startActivityForResult(intent, 1);
			} else {
				intent = new Intent(getActivity(), MesageCenterActivity.class);
				intent.putExtra("type", "1");
				startActivity(intent);
			}
			break;
		case R.id.newpinglun:
			if (TextUtils.isEmpty(userID)) {
				intent = new Intent(getActivity(), UserLoginNewActivity.class);
				startActivityForResult(intent, 1);
			} else {
				intent = new Intent(getActivity(), MesageCenterActivity.class);
				intent.putExtra("type", "0");
				startActivity(intent);
			}
			break;
		case R.id.mygossip:
			if (TextUtils.isEmpty(userID)) {
				intent = new Intent(getActivity(), UserLoginNewActivity.class);
				startActivityForResult(intent, 1);
			} else {
				intent = new Intent(getActivity(), MyGossipActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.mfeedback:
			if (TextUtils.isEmpty(userID)) {
				intent = new Intent(getActivity(), UserLoginNewActivity.class);
				startActivityForResult(intent, 1);
			} else {
				intent = new Intent(getActivity(), UserSuggestionActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.mcallservices:
			intent = new Intent(getActivity(), ContactActivity.class);
			startActivity(intent);
			break;
		case R.id.user_img:
			if (TextUtils.isEmpty(userID)) {
				intent = new Intent(getActivity(), UserLoginNewActivity.class);
				startActivityForResult(intent, 1);
			} else {
			intent = new Intent(getActivity(), UserAccountActivity.class);
			startActivity(intent);
			}
			break;
		case R.id.user_name:
			if (TextUtils.isEmpty(userID)) {
				intent = new Intent(getActivity(), UserLoginNewActivity.class);
				startActivityForResult(intent, 1);
			} else {
				intent = new Intent(getActivity(), UserAccountActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.mabout:
			intent = new Intent(getActivity(), AboutUsActivity.class);
			startActivity(intent);
			break;
		case R.id.msign:
			if (!TextUtils.isEmpty(userID)) {
				if (issign) {
					 Map<String, String> params = new HashMap<>();
					 params.put("userid", userID);
					 dataFlow.requestData(3, "newService/userSign", params, this,false);
					issign = false;
				}else{

				}
			}else{
				Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_SHORT).show();
				intent = new Intent(getActivity(), UserLoginNewActivity.class);
				startActivity(intent);
			}
			
			break;
			case R.id.ll_my_fabiao:
				if (TextUtils.isEmpty(userID)) {
					intent = new Intent(getActivity(), UserLoginNewActivity.class);
					startActivityForResult(intent, 1);
				} else {
					intent = new Intent(getActivity(), BidListDetailActivity.class);
					startActivity(intent);
				}
				break;
			case R.id.ll_my_jiebiao:
				if (TextUtils.isEmpty(userID)) {
					intent = new Intent(getActivity(), UserLoginNewActivity.class);
					startActivityForResult(intent, 1);
				} else {
					intent = new Intent(getActivity(), BidMyListDetailActivity.class);
					startActivity(intent);
				}
				break;

		default:
			break;
		}
	}

	

	/**
	 * @Description: 创建动画层  
	 * @param   
	 * @return void 
	 * @throws  
	 * 
     */   
	private ViewGroup createAnimLayout(){   
		ViewGroup rootView = (ViewGroup)getActivity().getWindow().getDecorView();
		LinearLayout animLayout = new LinearLayout(getActivity());
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
		animLayout.setLayoutParams(lp);
		animLayout.setId(R.id.anim_mask_layout);
		animLayout.setBackgroundResource(android.R.color.transparent);
		rootView.addView(animLayout);
		return animLayout;
		}

	/** 
	* @Description: 添加视图到动画层
	* @param @param vg 
	* @param @param view
	* @param @param location
	* @param @return
	* @return View
	* @throws
	*/
	private View addViewToAnimLayout(final ViewGroup vg){
//		int x = location[0];
//		int y = location[1];
		ImageView view = new ImageView(getActivity());
		view.setImageResource(R.mipmap.u_jb);
		vg.addView(view);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.height = BaseTools.getPixelsFromDp(getActivity(), 20);
		lp.width = BaseTools.getPixelsFromDp(getActivity(), 20);
//		lp.leftMargin = x;
//		lp.topMargin = y;
		float y1 = msign.getY();
		float x1 = msign.getX();
		view.setX(x1+50);
		view.setY(y1);
		view.setLayoutParams(lp);
		return view; 
	 }
	private View addTViewToAnimLayout(final ViewGroup vg){
//		int x = location[0];
//		int y = location[1];
		TextView view = new TextView(getActivity());
		view.setText(signnum);
		view.setTextColor(Color.parseColor("#f23030"));
		view.setTextSize(BaseTools.getPixelsFromDp(getActivity(), 16));
		vg.addView(view);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//		lp.leftMargin = x;
//		lp.topMargin = y;
		float y1 = msign.getY();
		float x1 = msign.getX();
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
		animation.setDuration(3000);// 设置动画持续时间
		animation.setRepeatCount(2);// 设置重复次数
		animation.setRepeatMode(Animation.REVERSE);// 设置反方向执行
		return animation;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

	}
//	private void initsigntext(String day, String jinbi) {
//		switch (day) {
//		case "0":
//			signnum = "+5";
//			num = Integer.valueOf(jinbi)+5;
//			break;
//		case "1":
//			signnum = "+10";
//			num = Integer.valueOf(jinbi)+10;
//			break;
//		case "2":
//			signnum = "+15";
//			num = Integer.valueOf(jinbi)+15;
//			break;
//		case "3":
//			signnum = "+20";
//			num = Integer.valueOf(jinbi)+20;
//			break;
//		case "4":
//			signnum = "+25";
//			num = Integer.valueOf(jinbi)+25;
//			break;
//		case "5":
//			signnum = "+30";
//			num = Integer.valueOf(jinbi)+30;
//			break;
//		case "6":
//			signnum = "+50";
//			num = Integer.valueOf(jinbi)+50;
//			break;
//
//		default:
//			break;
//		}
//	}
	private void sign() {
		 View view = addViewToAnimLayout(anim_mask_layout); 
		 int[] end_location = new int[2];   
		 Animation mTranslateAnimation = new TranslateAnimation 
				 (TranslateAnimation.RELATIVE_TO_SELF, 0.0f, TranslateAnimation.ABSOLUTE,
						 800, TranslateAnimation.RELATIVE_TO_SELF, 0.0f, TranslateAnimation.ABSOLUTE, -1000);// 移动  
		 mTranslateAnimation.setDuration(3000); 
		 final View view2 = addTViewToAnimLayout(anim_mask_layout);
		 Animation mHiddenAction = AnimationUtils.loadAnimation(getActivity(), R.anim.pingyi_shang);  
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
		 sign.setText("已签到"); 
		 mjbimg.setVisibility(View.GONE);
		 mjb.setText(num+"");
		
		 
	}
	private void initsignnum(String sign2) {
		if (sign2.equals("1")) {
			sign.setText("已签到");
			mjbimg.setVisibility(View.GONE);
			issign = false;
		} else {
			sign.setText("签到+");
			mjbimg.setVisibility(View.VISIBLE);
			issign = true;
		}
		
	}
	@Override
	public void onResume() {
		super.onResume();
		if (isoncreat) {
			System.gc();
			initData();
		}else{
			isoncreat = true;
		}

	}
	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
		JSONObject object;
		xrefresh.stopRefresh();
		switch (requestCode) {
		case 1:
			try {
			object = new JSONObject(content);
			String footprint = String.valueOf(object.optInt("footprint"));
			String messages = String.valueOf(object.optInt("messages"));
			String collect = String.valueOf(object.optInt("collect"));
			String sign = object.optString("sign");
			String jinbi = String.valueOf(object.optInt("jinbi"));
			String continuous_day = object.optString("continuous_day");
			String username = object.optString("username");
			String imgurl = object.optString("imgurl");
			String str = object.optString("addjinbi");
			String exp = object.optString("exp");//鲸力值
			SharedPreferencesUtil.putSharedData(getActivity(), "userInfor", "footprint", object.optString("footprint"));
			SharedPreferencesUtil.putSharedData(getActivity(), "userInfor", "collect", object.optString("collect"));
			signnum = "+"+str;
			num = Integer.valueOf(jinbi) + Integer.valueOf(str);
			mjb.setText(jinbi);
			mcollectnum.setText(collect);
			mfootnum.setText(footprint);
			mnewmsg.setText(messages);
			user_name.setText(username);
			mJlzText.setText("鲸力值"+exp);
			CircleImageView1.getImg(getActivity(), imgurl, user_img);
//				Glide.with(getActivity())
//						.load(imgurl).asBitmap().centerCrop()
//						.priority(Priority.HIGH)
//						.placeholder(R.mipmap.logo_01)
//						.into(user_img);
//			Glide.with(getActivity()).load(imgurl).asBitmap().into(new SimpleTarget<Bitmap>() {  
//                @Override  
//                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {  
//                	user_img.setImageBitmap(resource);  
//                	resource.recycle();
//                }  
//            }); //方法中设置asBitmap可以设置回调类型  
			SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "userInfor", "imgUrl",
					imgurl);
			SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "userInfor", "nickname",
					username);
//			initsigntext(continuous_day,jinbi);
			if (!messages.equals("0")) {
				mnewmsg.setVisibility(View.VISIBLE);
			}else{
				mnewmsg.setVisibility(View.GONE);
			}
			initsignnum(sign);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		case 2:
			try {
				object = new JSONObject(content);
				String footprint = String.valueOf(object.optInt("footprint"));
				String messages = String.valueOf(object.optInt("messages"));
				String collect = String.valueOf(object.optInt("collect"));
				mcollectnum.setText(collect);
				mfootnum.setText(footprint);
				mnewmsg.setText(messages);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 3:
			if (content.equals("-1")) {
				sign.setText("已签到");
				mjbimg.setVisibility(View.GONE);
				issign = false;
			}else if(content.equals("-2")){
				Toast.makeText(getActivity(), "签到异常", Toast.LENGTH_SHORT).show();
			}else{
				mjb.setText(content);
				sign();
			}
			break;
		default:
			break;
		}
	}


}
