package com.bbk.activity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.fragment.DataFragment;
import com.bbk.fragment.UserFragment;
import com.bbk.resource.Constants;
import com.bbk.util.DialogUtil;
import com.bbk.util.HttpUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.MD5Util;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.TencentLoginUtil;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.User;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class UserLoginNewActivity extends BaseActivity implements OnClickListener, TextWatcher,ResultEvent {
	private Typeface typeFace;
	private DataFlow dataFlow;
	private ImageButton goBackBtn;
	private SQLiteDatabase db;
	private Context context;
	private ViewGroup clearName;
	private ViewGroup clearPwd;
	private TextView findPswTv;
	private ImageView showSwitchIv;
	private  final static String DB_NAME = "user.db";
	private TextView registerBtn;
	private EditText userNameText, userPasswordText;
	private Button loginBtn;
	private RelativeLayout qqLoginLayout, weiboLoginLayout, weixinLoginLayout;
	private String userLogin, userPass, openID = "", nickName = "", imgUrl = "";
	/**
	 * 微博登录变量
	 */

	private boolean isWeboLogin = false;
	private AuthInfo mAuthInfo;
	private Oauth2AccessToken mAccessToken;
	private SsoHandler mSsoHandler;
	private UsersAPI mUsersAPI;
	/**
	 * QQ登录变量
	 */
	private boolean isQQLogin = false;
	private Tencent mTencent;

	/**微信登录变量*/
	private IWXAPI wxApi;
	private String type;
	
	private AlertDialog alertDialog;
	
	private boolean showPwd = true;
	private boolean iswebyanzheng =false;
	private String url = "";
	private String wzurl = "";
	private CheckBox mCheckXieyi;
	private LinearLayout mLlUserXiyi;//用户协议
	private RelativeLayout inflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_login_new);
//		helper = new MyHelper(this);
//		db = helper.getWritableDatabase();
		View topView = findViewById(R.id.login_main);
		// 实现沉浸式状态栏
		ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
		if (null!= getIntent().getStringExtra("url")) {
			url = getIntent().getStringExtra("url");
		}
		if (null!= getIntent().getStringExtra("wzurl")) {
			wzurl = getIntent().getStringExtra("wzurl");
		}
		dataFlow = new DataFlow(this);
		typeFace = getApp().getFontFace();
		initView();
		if (null!= getIntent().getStringExtra("iswebyanzheng")) {
			iswebyanzheng = true;
		}
		String addr = getIntent().getStringExtra("addr");
		if (null!=addr) {
			userNameText.setText(addr);
		}
		initData();
	}

	private void initView() {
		mLlUserXiyi = findViewById(R.id.ll_bbj_user_xieyi);
		mCheckXieyi = findViewById(R.id.checkbox_xieyi);
		mLlUserXiyi.setOnClickListener(this);
		alertDialog = buildDialog(R.layout.dialog_loading_progress);
		
		goBackBtn = (ImageButton) findViewById(R.id.topbar_goback);
		
		clearName = $(R.id.clean_name);
		clearPwd = $(R.id.clean_pwd);
		
		findPswTv = (TextView) findViewById(R.id.found_psw_tv);

		userNameText = (EditText) findViewById(R.id.user_name);
		userPasswordText = (EditText) findViewById(R.id.user_password);

		loginBtn = (Button) findViewById(R.id.login_btn);

		registerBtn = (TextView) findViewById(R.id.topbar_text_right);

//		qqLoginLayout = (RelativeLayout) findViewById(R.id.qq_login_layout);
//		qqLoginLayout.setOnTouchListener(new OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//
//				ImageView img = (ImageView) qqLoginLayout.getChildAt(0);
//				int action = event.getAction();
//				switch (action) {
//				case MotionEvent.ACTION_DOWN:
////					img.setImageResource(R.drawable.icon_qq_login_pressed);
//					break;
//				case MotionEvent.ACTION_UP:
//				case MotionEvent.ACTION_CANCEL:
////					img.setImageResource(R.drawable.icon_qq_login_normal);
//					break;
//				default:
//					break;
//				}
//
//				return false;
//			}
//		});
//		weiboLoginLayout = (RelativeLayout) findViewById(R.id.weibo_login_layout);
//		weiboLoginLayout.setOnTouchListener(new OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				ImageView img = (ImageView) weiboLoginLayout.getChildAt(0);
//				int action = event.getAction();
//				switch (action) {
//				case MotionEvent.ACTION_DOWN:
////					img.setImageResource(R.drawable.icon_weibo_login_pressed);
//					break;
//				case MotionEvent.ACTION_UP:
//				case MotionEvent.ACTION_CANCEL:
////					img.setImageResource(R.drawable.icon_weibo_login_normal);
//					break;
//				default:
//					break;
//				}
//
//				return false;
//			}
//		});

		weixinLoginLayout = (RelativeLayout) findViewById(R.id.wx_login_layout);
		weixinLoginLayout.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				ImageView img = (ImageView) weixinLoginLayout.getChildAt(0);
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
//					img.setImageResource(R.drawable.icon_wx_login_pressed);
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
//					img.setImageResource(R.drawable.icon_wx_login_normal);
					break;
				default:
					break;
				}

				return false;
			}
		});
		View showPwdLayout = $(R.id.show_pwd);
		showSwitchIv = $(R.id.show_switch_iv);
		showSwitchIv.setImageResource(R.mipmap.denglu_xianshimima);
		switchShowPwd();
		showPwdLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				switchShowPwd();
			}
		});

	}

	private void switchShowPwd(){
		if(showPwd){
			showSwitchIv.setImageResource(R.mipmap.denglvye_yingzhangmima);
			//默认状态显示密码--设置文本 要一起写才能起作用 InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
			userPasswordText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			userPasswordText.setTypeface(typeFace);
			showPwd = false;
		}else {
			showSwitchIv.setImageResource(R.mipmap.denglu_xianshimima);
			//选择状态 显示明文--设置为可见的密码
			userPasswordText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			userPasswordText.setTypeface(typeFace);
			showPwd = true;
		}
	}

	private void initData() {
		registerBtn.setOnClickListener(this);

		loginBtn.setOnClickListener(this);

		goBackBtn.setOnClickListener(this);

		userNameText.addTextChangedListener(this);
		userPasswordText.addTextChangedListener(this);

		clearName.setOnClickListener(this);
		clearPwd.setOnClickListener(this);
		findPswTv.setOnClickListener(this);

//		weiboLoginLayout.setOnClickListener(this);
//		qqLoginLayout.setOnClickListener(this);
		weixinLoginLayout.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			if(resultCode == 1) {
				Intent intent = new Intent();
				//				intent.putExtra("msg", data.getStringExtra("msg"));
				setResult(3, intent);
				finish();
			}
			break;
		default:
			break;
		}

		if (isWeboLogin && mSsoHandler != null) {
			isWeboLogin = false;
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
		if(isQQLogin) {
			isQQLogin = false;
			Tencent.onActivityResultData(requestCode, resultCode, data, new BaseUiListener());
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private boolean getUserLoginInfor() {
		userLogin = userNameText.getText().toString();
		userPass = userPasswordText.getText().toString();
		Toast toast;
		if(TextUtils.isEmpty(userLogin)) {
			toast = Toast.makeText(this, "用户名不能为空！",Toast.LENGTH_LONG );
			toast.show();
			return false;
		}
		if(TextUtils.isEmpty(userPass)) {
			toast = Toast.makeText(this, "密码不能为空！",Toast.LENGTH_LONG );
			toast.show();
			return false;
		}

		return true;
	}


	private void userLoginHttp() {
		loginBtn.setText("登录中...");
		loginBtn.setEnabled(false);
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("phone", userLogin);
		paramsMap.put("password", userPass);
		dataFlow.requestData(1, "apiService/loginUser", paramsMap, this, false);
	}

	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo,
			String content) {
		String str = content;
		try {
			JSONObject jsonObj = new JSONObject(str);
			
			Intent intent;
			if ("1".equals(dataJo.optString("status"))){
				switch (requestCode) {

					case 1:
						loginBtn.setText("立即登录");
						loginBtn.setEnabled(true);

						if("1".equals(jsonObj.optString("status"))) {
							JSONObject inforJsonObj = jsonObj.optJSONObject("info");
							String userID = inforJsonObj.optString("u_id");
							SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInfor","password", inforJsonObj.optString("u_pass"));
							SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "userInfor", "userID", userID);
							SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInfor", "userLogin", inforJsonObj.optString("u_name"));
							SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInfor", "userEmail", inforJsonObj.optString("u_email"));
							SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInfor", "userPhone", inforJsonObj.optString("u_phone"));
							SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInfor", "nickname", inforJsonObj.optString("u_nickname"));
							SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInfor", "gender", inforJsonObj.optString("u_sex"));
							SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInfor", "brithday", inforJsonObj.optString("u_birthday"));
							SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInfor", "province", inforJsonObj.optString("u_province"));
							SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInfor", "city", inforJsonObj.optString("u_city"));
							SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInfor", "imgUrl", inforJsonObj.optString("u_imgurl"));
							SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInfor", "mid", inforJsonObj.optString("u_mdid"));
							SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInfor", "identifier", inforJsonObj.optString("u_iden"));
							SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInfor", "userSig", inforJsonObj.optString("u_sig"));
							SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "userInfor", "login_STATE","1");
							intent = new Intent();
							setResult(3, intent);
							TencentLoginUtil.Login(this);
							if (DataFragment.login_remind!= null) {
								DataFragment.login_remind.setVisibility(View.GONE);
							}
							if (!"".equals(url)){
								Intent Intent = new Intent(this,WebViewActivity111.class);
								Intent.putExtra("url",url+"&mid="+inforJsonObj.optString("u_mdid"));
								Intent.putExtra("mid",inforJsonObj.optString("u_mdid"));
								startActivity(Intent);
							}
							if (!"".equals(wzurl)){
								Intent Intent = new Intent(this,WebViewActivity.class);
								Intent.putExtra("url",wzurl+"&mid="+inforJsonObj.optString("u_mdid")
										+"&userid="+inforJsonObj.optString("u_id"));
								startActivity(Intent);
							}
							finish();

						}else {
							Toast.makeText(getApplicationContext(), jsonObj.optString("msg"),
									Toast.LENGTH_SHORT).show();
							loginBtn.setEnabled(true);
						}
						break;
					case 2:

						break;
					case 3:
						progressDialogText.setText("登录成功");
						DialogUtil.dismiss(alertDialog,0);
						try {
							if("0".equals(dataJo.optString("status"))) {
								Toast.makeText(getApplicationContext(), dataJo.optString("msg"),
										Toast.LENGTH_SHORT).show();
							}
							if("1".equals(dataJo.optString("status"))) {
								if ("1".equals(jsonObj.optString("status"))) {
									String openID = SharedPreferencesUtil.getSharedData(UserLoginNewActivity.this,"userInfor","openID");
									String username = SharedPreferencesUtil.getSharedData(UserLoginNewActivity.this,"userInfor","username");
									SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInfor", "thirdLogin", "yes");
									JSONObject inforJsonObj = jsonObj.optJSONObject("info");
									SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInfor", "userID", inforJsonObj.optString("u_id"));
									SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInfor", "password", inforJsonObj.optString("u_pass"));
									SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInfor", "userLogin", inforJsonObj.optString("u_name"));
									SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInfor", "userEmail", inforJsonObj.optString("u_email"));
									SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInfor", "userPhone", inforJsonObj.optString("u_phone"));
									SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInfor", "nickname", inforJsonObj.optString("u_nickname"));
									SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInfor", "gender", inforJsonObj.optString("u_sex"));
									SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInfor", "brithday", inforJsonObj.optString("u_birthday"));
									SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInfor", "province", inforJsonObj.optString("u_province"));
									SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInfor", "city", inforJsonObj.optString("u_city"));
									SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInfor", "imgUrl", inforJsonObj.optString("u_imgurl"));
									SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInfor", "mid", inforJsonObj.optString("u_mdid"));
									SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInfor", "identifier", inforJsonObj.optString("u_iden"));
									SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInfor", "userSig", inforJsonObj.optString("u_sig"));
									SharedPreferencesUtil.putSharedData(getApplicationContext(),
											"userInfor", "login_STATE","1");
									intent = new Intent();
									setResult(3, intent);
									TencentLoginUtil.Login(this);
									if (DataFragment.login_remind!= null) {
										DataFragment.login_remind.setVisibility(View.GONE);
									}
									if (!"".equals(url)){
										Intent Intent = new Intent(this,WebViewActivity111.class);
										Intent.putExtra("url",url+"&mid="+inforJsonObj.optString("u_mdid"));
										Intent.putExtra("mid",inforJsonObj.optString("u_mdid"));
										startActivity(Intent);
									}
									if (!"".equals(wzurl)){
										Intent Intent = new Intent(this,WebViewActivity.class);
										Intent.putExtra("url",wzurl+"&mid="+inforJsonObj.optString("u_mdid")+
												"&userid="+inforJsonObj.optString("u_id"));
										startActivity(Intent);
									}
									finish();
								}else if("0".equals(jsonObj.optString("status"))){
//									intent = new Intent(this, SelectActivity.class);
									intent = new Intent(this, RegisterBangDingActivity.class);
									startActivity(intent);
									finish();
								}

							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					default:
						break;
				}
			}else {
				Toast.makeText(getApplicationContext(), dataJo.optString("errmsg"),
						Toast.LENGTH_SHORT).show();
			}


		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(isFinishing()) {
				return;
			}
			String str = msg.obj.toString();
			try {
				JSONObject jsonObj = new JSONObject(str);

				Intent intent;

				switch (msg.what) {
				case 4:
					if(!TextUtils.isEmpty(str)) {
						userLoginThirdPartyHttp(openID, nickName, imgUrl);
					}
					break;
				default:
					break;
				}

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	};

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.login_btn:
			if(getUserLoginInfor()) {
				userLoginHttp();
			}
			break;
		case R.id.topbar_text_right:
			intent = new Intent(this, UserRegisterGetCodeActivity.class);
			startActivityForResult(intent, 1);
			break;
			//		case R.id.find_password_btn:
			//			intent = new Intent(this, UserFindPasswordActivity.class);
			//			startActivity(intent);
			//			break;
		case R.id.topbar_goback:
			finish();
			break;
		case R.id.clean_name:
			userNameText.setText("");
			loginBtn.setEnabled(false);
			break;
		case R.id.clean_pwd:
			userPasswordText.setText("");
			loginBtn.setEnabled(false);
			break;
		case R.id.found_psw_tv:
			intent = new Intent(this, UserFindPasswordActivity.class);
			startActivity(intent);
			finish();
			break;
//		case R.id.weibo_login_layout:
//			isWeboLogin = true;
//			weiboLogin();
//			break;
//		case R.id.qq_login_layout:
//			isQQLogin = true;
//			QQLogin();
//			break;
		case R.id.wx_login_layout:
			if (mCheckXieyi.isChecked()){
				sendAuth();
			}else {
				showToast(getResources().getString(R.string.app_wx_text));
			}
			break;
			case R.id.ll_bbj_user_xieyi:
				String url = Constants.MAIN_BASE_URL_MOBILE +"mobile/user/agreement";
				intent = new Intent(this, WebViewActivity.class);
				intent.putExtra("url", url);
				startActivity(intent);
				break;
		default:
			break;
		}
	}
	public void showToast(String tishi) {
// TODO Auto-generated method stub
		View layout = inflater.inflate(this, R.layout.toast_layout, null);
		TextView title = (TextView) layout.findViewById(R.id.toast_title);
		title.setText(tishi);
		Toast toast = new Toast(this);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setView(layout);
		toast.show();
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		String userName = userNameText.getText().toString();
		String userPassword = userPasswordText.getText().toString();

		if(TextUtils.isEmpty(userName)) {
			clearName.setVisibility(View.GONE);
		} else {
			clearName.setVisibility(View.VISIBLE);
		}
		
		if(TextUtils.isEmpty(userPassword)) {
			clearPwd.setVisibility(View.GONE);
		} else {
			clearPwd.setVisibility(View.VISIBLE);
		}

		if(!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(userPassword)) {
			loginBtn.setEnabled(true);
			loginBtn.setTextColor(Color.parseColor("#FFFFFF"));
			loginBtn.setBackgroundResource(R.drawable.bg_user_btn);
		} else {
			loginBtn.setEnabled(false);
			loginBtn.setTextColor(Color.parseColor("#666666"));
			loginBtn.setBackgroundResource(R.drawable.bg_user_btn_unable);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		
	}

	public void weiboLogin() {
		mAuthInfo = new AuthInfo(UserLoginNewActivity.this, Constants.WEIBO_APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
		mSsoHandler = new SsoHandler(UserLoginNewActivity.this, mAuthInfo);
		mSsoHandler.authorize(new AuthListener());
	}

	class AuthListener implements WeiboAuthListener {
		@Override
		public void onComplete(Bundle values) {
			mAccessToken = Oauth2AccessToken.parseAccessToken(values); // 从Bundle中解析Token
			mUsersAPI = new UsersAPI(getApplicationContext(), Constants.WEIBO_APP_KEY, mAccessToken);
			if (mAccessToken.isSessionValid()) {
				
				progressDialogText.setText("正在使用微博登录");
				alertDialog.show();
				
				long uid = Long.parseLong(mAccessToken.getUid());
				mUsersAPI.show(uid, mListener);
			} else {
				System.out.println("++++++++++++++" + Bundle.class);
				// 当您注册的应用程序签名不正确时，就会收到错误Code，请确保签名正确
				String code = values.getString("code", "");
			}
		}
		@Override
		public void onCancel() {
		}
		@Override
		public void onWeiboException(WeiboException arg0) {
		}
	}

	/**
	 * 微博 OpenAPI 回调接口。
	 */
	private RequestListener mListener = new RequestListener() {
		@Override
		public void onComplete(String response) {
			if (!TextUtils.isEmpty(response)) {
				// 调用 User.parse 将JSON串解析成User对象
				User user = User.parse(response);
				if (user != null) {

					openID = user.id;
					nickName = user.screen_name;
					imgUrl = user.avatar_large;

//					Intent intent = new Intent();
//					intent.putExtra("openID", openID);
//					intent.putExtra("nickName", nickName);
//					intent.putExtra("imgUrl", imgUrl);
//					setResult(5, intent);
//					finish();

					SharedPreferencesUtil.putSharedData(getApplicationContext(), "thirdlogin", "imgUrl", imgUrl);
					userLoginThirdPartyHttp(openID, nickName, imgUrl);

				} else {
					Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
				}
			}
		}
		@Override
		public void onWeiboException(WeiboException e) {
			ErrorInfo info = ErrorInfo.parse(e.getMessage());
			Toast.makeText(getApplicationContext(), info.toString(), Toast.LENGTH_LONG).show();
		}
	};


	public void QQLogin() {
		mTencent = Tencent.createInstance(Constants.QQ_APP_ID, getApplicationContext());
		mTencent.login(this, "all", new BaseUiListener());
	}

	class BaseUiListener implements IUiListener {
		@Override
		public void onCancel() {
			Toast.makeText(getApplicationContext(), "登录取消", Toast.LENGTH_LONG).show();
		}
		@Override
		public void onComplete(Object response) {
			Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_LONG).show();
			try {
				JSONObject jsonObj = new JSONObject(response.toString());
				openID = jsonObj.optString("openid");
				mTencent.setOpenId(openID);
				mTencent.setAccessToken(jsonObj.optString("access_token"), jsonObj.optString("expires_in"));

				QQToken qqToken = mTencent.getQQToken();
				if(qqToken.isSessionValid()) {

					progressDialogText.setText("正在使用QQ登录");
					alertDialog.show();
					
					UserInfo userInfo = new UserInfo(getApplicationContext(), qqToken);
					userInfo.getUserInfo(new IUiListener() {
						@Override
						public void onComplete(final Object response) {
							try {
								JSONObject userInforJson = new JSONObject(response.toString());
								nickName = userInforJson.optString("nickname");
								imgUrl = userInforJson.optString("figureurl_qq_2");
								SharedPreferencesUtil.putSharedData(getApplicationContext(), "thirdlogin", "imgUrl", imgUrl);

//								Intent intent = new Intent();
//								intent.putExtra("openID", openID);
//								intent.putExtra("nickName", nickName);
//								intent.putExtra("imgUrl", imgUrl);
//								setResult(5, intent);
//								finish();
								
								userLoginThirdPartyHttp(openID, nickName, imgUrl);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						@Override
						public void onCancel() {
						}
						@Override
						public void onError(UiError arg0) {
						}
					});
				} else {

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		public void onError(UiError arg0) {
			Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_LONG).show();
		}

	}

	/**
	 * 申请微信授权
	 */
	private void sendAuth() {
		
		if (wxApi == null) {
			wxApi = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, false);
		}

		if (!wxApi.isWXAppInstalled()) {
			Toast.makeText(this, "您还未安装微信客户端",  
					Toast.LENGTH_SHORT).show();
			return;
		}

		wxApi.registerApp(Constants.WX_APP_ID);
		type = "wx_login";

		SendAuth.Req req = new SendAuth.Req();
		req.scope = "snsapi_userinfo";
		req.state = "bbj_login_state";
		wxApi.sendReq(req);
		
	}

	@Override
	protected void onResume() {
		if (type != null && type.equals("wx_login")) {
			SharedPreferences settings = getSharedPreferences("setting", 0);
			String code = settings.getString("code", null);
			if (code != null && !code.equals("")) {
				
				progressDialogText.setText("正在使用微信登录");
				alertDialog.show();
				
				getOpenid(code);
			}
			type = "";
			settings.edit().clear();
			settings.edit().commit();
		}
		super.onResume();
	}
	
	/**
	 * 获取openid accessToken值用于后期操作
	 * @param code 请求码
	 */
	private void getOpenid(final String code) {
		new Thread() {// 开启工作线程进行网络请求
			@Override
			public void run() {
				String path = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
						+ Constants.WX_APP_ID
						+ "&secret="
						+ Constants.WX_APP_SECRET
						+ "&code="
						+ code
						+ "&grant_type=authorization_code";
				try {
					String result = HttpUtil.requestByHttpGet(path, null);// 请求https连接并得到json结果
					
					JSONObject jsonObject = new JSONObject(result);

					if (null != jsonObject) {
						String openid = jsonObject.getString("openid")
								.toString().trim();
						String access_token = jsonObject
								.getString("access_token").toString().trim();

						getUID(openid,access_token);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			};
		}.start();
	}
	
	/**
	 * 获取用户唯一标识
	 * @param openId
	 * @param accessToken
	 */
	private void getUID(final String openId, final String accessToken) {
		new Thread() {
			@Override
			public void run() {
				String path = "https://api.weixin.qq.com/sns/userinfo?access_token="
						+ accessToken + "&openid=" + openId;
				try {
					String result = HttpUtil.requestByHttpGet(path,null);
					JSONObject jsonObject = new JSONObject(result);
					
					nickName = jsonObject.getString("nickname");
					openID = jsonObject.getString("unionid");
					imgUrl = jsonObject.getString("headimgurl");
					SharedPreferencesUtil.putSharedData(getApplicationContext(), "thirdlogin", "imgUrl", imgUrl);
					
					
//					Intent intent = new Intent();
//					intent.putExtra("openID", openID);
//					intent.putExtra("nickName", nickName);
//					intent.putExtra("imgUrl", imgUrl);
//					setResult(5, intent);
//					finish();
					
					Message msg = Message.obtain();
					msg.what = 4;
					
					msg.obj = result;
					handler.sendMessageDelayed(msg, 500);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	@Override
	protected void onDestroy() {
		setContentView(R.layout.view_null);
		System.gc();
		super.onDestroy();
	}
	
	
	private void userLoginThirdPartyHttp(final String openID, final String nickName, final String imgUrl) {
		progressDialogText.setText("正在获取登录信息");
		Map<String, String> paramsMap = new HashMap<String, String>();
		SharedPreferencesUtil.putSharedData(UserLoginNewActivity.this,"userInfor","openID", openID);
		SharedPreferencesUtil.putSharedData(UserLoginNewActivity.this,"userInfor","username", nickName);
		paramsMap.put("openid", openID);
		paramsMap.put("nickname", nickName);
		paramsMap.put("imgurl", imgUrl);
		dataFlow.requestData(3, "apiService/checkIsThirdBand" , paramsMap, this, false);
		
	}
	
	
	private TextView progressDialogText;
	
	public AlertDialog buildDialog(int id) {
		AlertDialog.Builder builder = new Builder(this, R.style.progress_dialog);
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(id, null);
		progressDialogText = (TextView) view.findViewById(R.id.loading_text);
		builder.setView(view);
		
		AlertDialog dialog = builder.create();
		
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.dialog_style);
		
		return dialog;
	}

}
