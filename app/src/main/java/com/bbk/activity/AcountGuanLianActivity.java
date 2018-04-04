package com.bbk.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.fragment.DataFragment;
import com.bbk.fragment.UserFragment;
import com.bbk.resource.Constants;
import com.bbk.util.HttpUtil;
import com.bbk.util.MD5Util;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.TencentLoginUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class AcountGuanLianActivity extends BaseActivity implements OnClickListener{
	private EditText select_account,select_password;
	private TextView guanlian_login,guanlian_getpassword;
	private ImageButton guanlian_goback;
	private DataFlow dataFlow;
	private Context context;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Intent intent;
				String dataStr = (String) msg.obj;
				try {
					JSONObject data = new JSONObject(dataStr);
					JSONObject content = data.getJSONObject("content");
					if("0".equals(data.optString("status"))) {
						Toast.makeText(AcountGuanLianActivity.this, data.optString("errmsg"),Toast.LENGTH_LONG ).show();
					}
					if("1".equals(data.optString("status"))) {
						if ("1".equals(content.optString("status"))) {
							Toast.makeText(AcountGuanLianActivity.this, content.optString("msg"),Toast.LENGTH_LONG ).show();
							SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInfor", "thirdLogin", "yes");
							JSONObject inforJsonObj = content.optJSONObject("info");
							String password = select_password.getText().toString();
							String pass = MD5Util.Md5(password);
							SharedPreferencesUtil.putSharedData(getApplicationContext(), "userInfor","password", pass);
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
								intent = new Intent();
								setResult(3, intent);
							TencentLoginUtil.Login(AcountGuanLianActivity.this);
								if (DataFragment.login_remind!= null) {
									DataFragment.login_remind.setVisibility(View.GONE);
								}

								finish();
							}else if("0".equals(content.optString("status"))){
								Toast.makeText(AcountGuanLianActivity.this, content.optString("msg"),Toast.LENGTH_LONG ).show();
							}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
		setContentView(R.layout.activity_acount_guan_lian);
		initView();
	}

	private void initView() {
		select_account = (EditText) findViewById(R.id.select_account);
		select_password = (EditText) findViewById(R.id.select_password);
		guanlian_login = (TextView) findViewById(R.id.guanlian_login);
		guanlian_getpassword = (TextView) findViewById(R.id.guanlian_getpassword);
		guanlian_goback = (ImageButton) findViewById(R.id.guanlian_goback);
		
		guanlian_goback.setOnClickListener(this);
		select_account.setOnClickListener(this);
		select_password.setOnClickListener(this);
		guanlian_login.setOnClickListener(this);
		guanlian_getpassword.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.guanlian_login:
			String phone = select_account.getText().toString();
			String password = select_password.getText().toString();
			if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password)) {
				String username = SharedPreferencesUtil.getSharedData(this, "userInfor", "username");
				String openID = SharedPreferencesUtil.getSharedData(this, "userInfor", "openID");
				String imgUrl = SharedPreferencesUtil.getSharedData(this, "thirdlogin", "imgUrl");
				final Map<String, String> paramsMap = new HashMap<String, String>();
				paramsMap.put("phone", phone);
				paramsMap.put("pass", password);
				paramsMap.put("username", username);
				paramsMap.put("openid", openID);
				paramsMap.put("imgUrl", imgUrl);
				final String url = Constants.MAIN_BASE_URL_MOBILE + "apiService/relationBandOpenid";
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
				
			}else{
				Toast.makeText(this, "账号密码不能为空！", Toast.LENGTH_LONG).show();
			}
			
			
			break;
		case R.id.guanlian_goback:
			finish();
			break;
		case R.id.guanlian_getpassword:
			intent = new Intent(this, UserFindPasswordActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

}
