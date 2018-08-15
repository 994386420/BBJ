package com.bbk.activity;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.resource.Constants;
import com.bbk.resource.NewConstants;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.JiaMiUtil;
import com.bbk.util.RSAEncryptorAndroid;
import com.bbk.util.StringUtil;
import com.bbk.util.ValidatorUtil;

import static com.bbk.util.MD5Util.Md5;

public class UserFindPasswordActivity extends BaseActivity implements
		OnClickListener, ResultEvent {
	private DataFlow dataFlow;
	private ImageButton goBackButton;
	private Button nextBtn, getCodeBtn;
	private EditText userPhoneEditText, userCodeEditText;
	private String userCodeStr;
	private TimeCount time;
	private TextView timeText;
	private String addr = "";
	private EditText mNewPassword;//新密码
    public static Activity instance = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_find_password_main);
		View topView = findViewById(R.id.login_main);
		// 实现沉浸式状态栏
		ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
		dataFlow = new DataFlow(this);
        instance = this;
		initView();
		initData();
	}

	private void initView() {
		nextBtn = (Button) findViewById(R.id.next_btn);
		getCodeBtn = (Button) findViewById(R.id.get_code_btn);
		userPhoneEditText = (EditText) findViewById(R.id.user_email);
		userCodeEditText = (EditText) findViewById(R.id.user_code);
		timeText = (TextView) findViewById(R.id.time_text);
		goBackButton = (ImageButton) findViewById(R.id.topbar_goback);
		mNewPassword = findViewById(R.id.new_password);
		goBackButton.setOnClickListener(this);
	}

	private void initData() {
		time = new TimeCount(120000, 1000);
		nextBtn.setOnClickListener(this);
		getCodeBtn.setOnClickListener(this);

		userPhoneEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				String userPhone = userPhoneEditText.getText().toString();
				if (!TextUtils.isEmpty(userPhone)) {
					getCodeBtn.setEnabled(true);
					getCodeBtn.setTextColor(Color.parseColor("#FFFFFF"));
					getCodeBtn.setBackgroundResource(R.drawable.bg_user_btn);
				} else {
					getCodeBtn.setEnabled(false);
					getCodeBtn.setTextColor(Color.parseColor("#FFFFFF"));
					getCodeBtn
							.setBackgroundResource(R.drawable.bg_user_btn_unable);

					nextBtn.setEnabled(false);
					nextBtn.setTextColor(Color.parseColor("#FFFFFF"));
					nextBtn.setBackgroundResource(R.drawable.bg_user_btn_unable);
				}

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		userCodeEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				String userEmail = userPhoneEditText.getText().toString();
				String userCode = userCodeEditText.getText().toString();
				if (!TextUtils.isEmpty(userEmail)
						&& !TextUtils.isEmpty(userCode)) {
					nextBtn.setEnabled(true);
					nextBtn.setTextColor(Color.parseColor("#FFFFFF"));
					nextBtn.setBackgroundResource(R.drawable.bg_user_btn);
				} else {
					nextBtn.setEnabled(false);
					nextBtn.setTextColor(Color.parseColor("#FFFFFF"));
					nextBtn.setBackgroundResource(R.drawable.bg_user_btn_unable);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.next_btn:
			addr = userPhoneEditText.getText().toString();
			userCodeStr = userCodeEditText.getText().toString();
			String password = mNewPassword.getText().toString();
			if (password != null && !password.equals("")){
				Map<String, String> paramsMap = new HashMap<String, String>();
				paramsMap.put("phone", addr);
				paramsMap.put("password", password);
				paramsMap.put("mesgCode", userCodeStr);
				dataFlow.requestData(2, Constants.findPwdByPhone,paramsMap, this,true,"找回中...");
			}else {
				StringUtil.showToast(UserFindPasswordActivity.this,"请输入密码");
			}
			break;
		case R.id.get_code_btn:
			userCodeEditText.setText("");
			addr = userPhoneEditText.getText().toString();
			if (StringUtil.isNullOrEmpty(addr)) {
				getCodeBtn.setEnabled(false);
				Map<String, String> paramsMap1 = new HashMap<String, String>();
				String cc = RSAEncryptorAndroid.getSendCode(addr).replace("\n","").replace("\r","");
				paramsMap1.put("phone", addr);
				paramsMap1.put("code", cc);
				dataFlow.requestData(1, "apiService/sendMessage", paramsMap1, this,true,"短信发送中...");
			} else {
				StringUtil.showToast(UserFindPasswordActivity.this,"请输入11位正确手机号");
				DialogSingleUtil.dismiss(0);
			}
			break;
		case R.id.topbar_goback:
			finish();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo,
			String content) {
		getCodeBtn.setEnabled(true);
		switch (requestCode) {
		case 1:
            if ("1".equals(dataJo.optString("status"))){
				StringUtil.showToast(UserFindPasswordActivity.this,"发送成功");
                time.start();
            }else {
				StringUtil.showToast(getApplicationContext(), dataJo.optString("errmsg"));
            }
			break;
		case 2:
            if ("1".equals(dataJo.optString("status"))){
				StringUtil.showToast(getApplicationContext(), "修改成功");
				Intent intent = new Intent(UserFindPasswordActivity.this,UserLoginNewActivity.class);
				startActivity(intent);
				finish();
            }else {
				StringUtil.showToast(getApplicationContext(), dataJo.optString("errmsg"));
            }
		}
	}

	class TimeCount extends CountDownTimer {

		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			getCodeBtn.setTextColor(Color.parseColor("#FFFFFF"));
			getCodeBtn.setBackgroundResource(R.drawable.bg_user_btn_unable);
			getCodeBtn.setEnabled(false);
			timeText.setVisibility(View.VISIBLE);
			timeText.setText(millisUntilFinished / 1000 + "秒后可重新发送");
		}

		@Override
		public void onFinish() {
			getCodeBtn.setTextColor(Color.parseColor("#FFFFFF"));
			getCodeBtn.setBackgroundResource(R.drawable.bg_user_btn);
			getCodeBtn.setEnabled(true);
			timeText.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onDestroy() {
		setContentView(R.layout.view_null);
		System.gc();
		super.onDestroy();
	}

}
