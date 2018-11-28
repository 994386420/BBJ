package com.bbk.activity;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.bbk.activity.UserFindPasswordActivity.TimeCount;
import com.bbk.client.BaseApiService;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.fragment.DataFragment;
import com.bbk.fragment.UserFragment;
import com.bbk.resource.Constants;
import com.bbk.resource.NewConstants;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.HttpUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.JiaMiUtil;
import com.bbk.util.MD5Util;
import com.bbk.util.RSAEncryptorAndroid;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.util.TencentLoginUtil;
import com.bbk.util.ValidatorUtil;
import com.logg.Logg;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import static com.bbk.util.MD5Util.Md5;

public class RegisterBangDingActivity extends BaseActivity implements OnClickListener{
	private EditText bangding_account,bangding_code,bangding_tjm;
	private ImageButton bangding_goback_btn;
	private TextView bangding_register,timeText;
	private TimeCount time;
	private Button get_code_btn;
	private DataFlow dataFlow;
	private String addr = "";
	private Context context = RegisterBangDingActivity.this;
	private String code = "";
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				try {
					String dataStr = (String) msg.obj;
					JSONObject data;
                        data = new JSONObject(dataStr);
					if ("1".equals(data.optString("status"))) {
						StringUtil.showToast(RegisterBangDingActivity.this,"发送成功");
					}else {
						StringUtil.showToast(RegisterBangDingActivity.this,data.optString("errmsg"));
					}
					DialogSingleUtil.dismiss(0);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				break;
			case 0:
				
				String dataStr = (String) msg.obj;
				JSONObject data;
				try {
					data = new JSONObject(dataStr);
				if ("1".equals(data.optString("status"))) {

					if (StringUtil.isMobilePhoneVerify(addr)) {
						get_code_btn.setEnabled(false);
						time.start();
						bangding_code.setSelection(bangding_code.getText().toString().length());
						String v = RSAEncryptorAndroid.getSendCode(addr).replace("\n","").replace("\r","");
						final Map<String, String> paramsMap = new HashMap<String, String>();
						final String url = BaseApiService.Base_URL + "apiService/sendMessage";
						paramsMap.put("phone", addr);
						paramsMap.put("type", "0");
						paramsMap.put("code", v);
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								Log.e("=====dataJo======","=========");
								String dataStr = HttpUtil.getHttp(paramsMap, url, context);
								try {
									Message mes = handler.obtainMessage();
									mes.obj = dataStr;
									mes.what =1;
									handler.sendMessage(mes);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}
						}).start();
					} else {
						StringUtil.showToast(RegisterBangDingActivity.this,"请输入11位正确手机号");
						DialogSingleUtil.dismiss(0);
					}
				}else{
					StringUtil.showToast(RegisterBangDingActivity.this,data.optString("errmsg"));
					DialogSingleUtil.dismiss(0);
				}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				break;
			
			case 2:
				Intent intent;
				String dataStr1 = (String) msg.obj;
				try {
					JSONObject data1 = new JSONObject(dataStr1);
//					if("1".equals(data1.optString("status"))) {
						if ("1".equals(data1.optString("status"))) {
							JSONObject content = data1.getJSONObject("content");
							StringUtil.showToast(RegisterBangDingActivity.this, content.optString("msg"));
							SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInfor", "thirdLogin", "yes");
							JSONObject inforJsonObj = content.optJSONObject("info");
							String usertjm = bangding_tjm.getText().toString();
//							String pass = MD5Util.Md5(userpassword);
//							SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInfor","password", pass);
							SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInfor", "userID", inforJsonObj.optString("u_id"));
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
							NewConstants.logFlag = "2";
							NewConstants.yingdaoFlag = "1";
							TencentLoginUtil.Login(RegisterBangDingActivity.this);
							String userId=SharedPreferencesUtil.getSharedData(RegisterBangDingActivity.this, "userInfor", "userID");
							Context context = getApplicationContext();
							XGPushConfig.setAccessId(context, 2100196420);
							XGPushConfig.setAccessKey(context, "AUTV25N58F3Z");
							XGPushManager.registerPush(context, userId, new XGIOperateCallback() {

								@Override
								public void onSuccess(Object data, int arg1) {
									Log.e("TPush====", "注册成功，设备token为：" + data);
								}

								@Override
								public void onFail(Object data, int errCode, String msg) {
									Log.e("TPush====", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
								}
							});
							XGPushManager.registerPush(context, new XGIOperateCallback() {
								@Override
								public void onSuccess(Object data, int flag) {
									Log.e("TPush", "注册成功，设备token为：" + data);
								}
								@Override
								public void onFail(Object data, int errCode, String msg) {
									Log.e("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
								}
							});
							    //友盟登录
							MobclickAgent.onProfileSignIn("Wx",bangding_account.getText().toString());
							final String userID = inforJsonObj.optString("u_id");
							PushAgent mPushAgent = PushAgent.getInstance(RegisterBangDingActivity.this);
							mPushAgent.addAlias(userId, "BBJ", new UTrack.ICallBack() {
								@Override
								public void onMessage(boolean isSuccess, String message) {
//									Logg.e("===>>>设置别名成功==="+userID);
								}
							});

//							intent = new Intent(RegisterBangDingActivity.this, TuiguangDialogActivity.class);
							intent = new Intent(RegisterBangDingActivity.this, HomeActivity.class);
//								setResult(3, intent);
							intent.putExtra("type", "4");
							if (DataFragmentActivity.login_remind != null) {
								DataFragmentActivity.login_remind.setVisibility(View.GONE);
							}
							startActivity(intent);
							}else {
								StringUtil.showToast(RegisterBangDingActivity.this, data1.optString("errmsg"));
							}
//					}else {
//						StringUtil.showToast(RegisterBangDingActivity.this, data1.optString("errmsg"));
					DialogSingleUtil.dismiss(0);
//					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					DialogSingleUtil.dismiss(0);
				}
				break;
			
			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_bang_ding);
		View topView = findViewById(R.id.login_main);
		// 实现沉浸式状态栏
		ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
		initView();
		initData();
	}

	private void initView() {
		bangding_account = (EditText) findViewById(R.id.bangding_account);
		bangding_code = (EditText) findViewById(R.id.bangding_code);
		bangding_tjm = (EditText) findViewById(R.id.bangding_password);
		bangding_goback_btn = (ImageButton) findViewById(R.id.bangding_goback_btn);
		bangding_register = (TextView) findViewById(R.id.bangding_register);
		get_code_btn = (Button) findViewById(R.id.get_code_btn);
		timeText = (TextView) findViewById(R.id.time_text);
		
		bangding_code.setOnClickListener(this);
		bangding_goback_btn.setOnClickListener(this);
		bangding_register.setOnClickListener(this);
	}
	private void initData() {
		time = new TimeCount(120000, 1000);
		get_code_btn.setOnClickListener(this);

		bangding_account.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String userPhone = bangding_account.getText().toString();
				if (!TextUtils.isEmpty(userPhone)) {
					get_code_btn.setEnabled(true);
					get_code_btn.setTextColor(Color.parseColor("#FFFFFF"));
					get_code_btn.setBackgroundResource(R.drawable.bg_user_btn);
				} else {
					get_code_btn.setEnabled(false);
					get_code_btn.setTextColor(Color.parseColor("#FFFFFF"));
					get_code_btn
							.setBackgroundResource(R.drawable.bg_user_btn_unable);

				}
				String userEmail = bangding_account.getText().toString();
				String userCode = bangding_code.getText().toString();
				String userpassword = bangding_tjm.getText().toString();
				if (!TextUtils.isEmpty(userEmail)// && !TextUtils.isEmpty(userpassword)
						&& !TextUtils.isEmpty(userCode)) {
					bangding_register.setEnabled(true);
					bangding_register.setTextColor(Color.parseColor("#FFFFFF"));
					bangding_register.setBackgroundResource(R.drawable.bg_user_btn);
				} else {
					bangding_register.setEnabled(false);
					bangding_register.setTextColor(Color.parseColor("#FFFFFF"));
					bangding_register.setBackgroundResource(R.drawable.bg_user_btn_unable);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		bangding_code.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String userEmail = bangding_account.getText().toString();
				String userCode = bangding_code.getText().toString();
				String userpassword = bangding_tjm.getText().toString();
				if (!TextUtils.isEmpty(userEmail)//&& !TextUtils.isEmpty(userpassword)
						&& !TextUtils.isEmpty(userCode) ) {
					bangding_register.setEnabled(true);
					bangding_register.setTextColor(Color.parseColor("#FFFFFF"));
					bangding_register.setBackgroundResource(R.drawable.bg_user_btn);
				} else {
					bangding_register.setEnabled(false);
					bangding_register.setTextColor(Color.parseColor("#FFFFFF"));
					bangding_register.setBackgroundResource(R.drawable.bg_user_btn_unable);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
//		bangding_tjm.addTextChangedListener(new TextWatcher() {
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before,
//					int count) {
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//				String userEmail = bangding_account.getText().toString();
//				String userCode = bangding_code.getText().toString();
//				String userpassword = bangding_tjm.getText().toString();
//				if (!TextUtils.isEmpty(userEmail)//&& !TextUtils.isEmpty(userpassword)
//						&& !TextUtils.isEmpty(userCode)) {
//					bangding_register.setEnabled(true);
//					bangding_register.setTextColor(Color.parseColor("#FFFFFF"));
//					bangding_register.setBackgroundResource(R.drawable.bg_user_btn);
//				} else {
//					bangding_register.setEnabled(false);
//					bangding_register.setTextColor(Color.parseColor("#666666"));
//					bangding_register.setBackgroundResource(R.drawable.bg_user_btn_unable);
//				}
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//			}
//		});
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.get_code_btn:
			DialogSingleUtil.show(this, "短信发送中...");
			bangding_code.setText("");
			addr = bangding_account.getText().toString();
			final Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("phone", addr);
			final String url = BaseApiService.Base_URL + "apiService/exsistPhone";
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					Log.e("=====dataJo======","=========");
					String dataStr = HttpUtil.getHttp(paramsMap, url, context);
					Message mes = handler.obtainMessage();
					mes.obj = dataStr;
					mes.what =0;
					handler.sendMessage(mes);
				}
			}).start();
			break;
		case R.id.bangding_goback_btn:
			finish();
			break;
		case R.id.bangding_register:
			    DialogSingleUtil.show(this,"绑定中...");
			    String invitcode;
			    if(bangding_tjm.getText().toString().equals("")){
			    	invitcode = "";
				}else {
					invitcode = bangding_tjm.getText().toString();//推荐码
				}
			    String mesgCode = bangding_code.getText().toString();//验证码
				String username = SharedPreferencesUtil.getSharedData(RegisterBangDingActivity.this, "userInfor", "username");
//				String openID = SharedPreferencesUtil.getSharedData(RegisterBangDingActivity.this, "userInfor", "openID");
				String imgUrl = SharedPreferencesUtil.getSharedData(RegisterBangDingActivity.this, "thirdlogin", "imgUrl");
//			    String unionid = SharedPreferencesUtil.getSharedData(RegisterBangDingActivity.this, "userInfor", "unionid");
			    String openid = SharedPreferencesUtil.getSharedData(RegisterBangDingActivity.this,  "thirdlogin", "openId");
			    String unionid = SharedPreferencesUtil.getSharedData(RegisterBangDingActivity.this,  "thirdlogin", "unionid");
				addr = bangding_account.getText().toString();//手机号
			   Logg.e(openid+"=========>>"+imgUrl+"=========>>>"+unionid);
				final Map<String, String> params = new HashMap<String, String>();
				params.put("phone", addr);
				params.put("invitcode", invitcode);
				params.put("username", username);
				params.put("openid", unionid);
				params.put("imgUrl", imgUrl);
			    params.put("mesgCode", mesgCode);
			    params.put("client", "android");
			    params.put("unionid",openid);
			    params.put("code",RSAEncryptorAndroid.getSendCode(addr+mesgCode));
//			try {
//				JSONObject jsonObject = new JSONObject();
//				jsonObject.put("phone", addr);
//				jsonObject.put("invitcode", invitcode);
//				jsonObject.put("username", username);
//				jsonObject.put("openid", unionid);
//				jsonObject.put("imgUrl", imgUrl);
//				jsonObject.put("mesgCode", mesgCode);
//				jsonObject.put("client", "android");
//				jsonObject.put("unionid",openid);
//				jsonObject.put("code",RSAEncryptorAndroid.getSendCode(addr+mesgCode));
//				params.put("param",RSAEncryptorAndroid.getSendCode(jsonObject.toString()));
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
			final String url1 = BaseApiService.Base_URL + "apiService/registBandOpenid";
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						String dataStr = HttpUtil.getHttp(params, url1, context);
						Message mes = handler.obtainMessage();
						mes.obj = dataStr;
						mes.what =2;
						handler.sendMessage(mes);
					}
				}).start();
			
			break;

		default:
			break;
		}
	}
	class TimeCount extends CountDownTimer {

		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			get_code_btn.setTextColor(Color.parseColor("#FFFFFF"));
			get_code_btn.setBackgroundResource(R.drawable.bg_user_btn_unable);
			get_code_btn.setEnabled(false);
			timeText.setVisibility(View.VISIBLE);
			timeText.setText(millisUntilFinished / 1000 + "秒后可重新发送");
		}

		@Override
		public void onFinish() {
			get_code_btn.setTextColor(Color.parseColor("#FFFFFF"));
			get_code_btn.setBackgroundResource(R.drawable.bg_user_btn);
			get_code_btn.setEnabled(true);
			timeText.setVisibility(View.GONE);
		}
	}
}
