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
import com.bbk.util.JiaMiUtil;
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
    public static Activity instance = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_find_password_main);
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
					getCodeBtn.setTextColor(Color.parseColor("#666666"));
					getCodeBtn
							.setBackgroundResource(R.drawable.bg_user_btn_unable);

					nextBtn.setEnabled(false);
					nextBtn.setTextColor(Color.parseColor("#666666"));
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
					nextBtn.setTextColor(Color.parseColor("#666666"));
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
			userCodeStr = userCodeEditText.getText().toString();
			if (!TextUtils.isEmpty(userCodeStr)) {
				Map<String, String> paramsMap = new HashMap<String, String>();
				paramsMap.put("addr", addr);
				paramsMap.put("clientType", "android");
				paramsMap.put("code", userCodeStr);
				dataFlow.requestData(2, "apiService/verificationCode",paramsMap, this);
			}
			break;
		case R.id.get_code_btn:
			userCodeEditText.setText("");
			addr = userPhoneEditText.getText().toString();
			if (ValidatorUtil.isMobile(addr) || ValidatorUtil.isEmail(addr)) {
				getCodeBtn.setEnabled(false);

				Map<String, String> paramsMap = new HashMap<String, String>();
				String cc = JiaMiUtil.jiami(addr);
				paramsMap.put("addr", addr);
				paramsMap.put("clientType", "android");
				paramsMap.put("code", cc);
				dataFlow.requestData(1, "apiService/resetPwd", paramsMap, this);
			} else {
				Toast.makeText(getApplicationContext(),"您输入的不是手机号或者邮箱", Toast.LENGTH_LONG).show();
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
                time.start();
            }else {
                Toast.makeText(getApplicationContext(), dataJo.optString("errmsg"),
                        Toast.LENGTH_SHORT).show();
            }
//			if("notExsist".equals(content)){
//				Toast.makeText(this, "该手机号或者邮箱地址尚未注册!", Toast.LENGTH_SHORT).show();
//			}else if("sendSuccessful".equals(content)){
//
//			}else if("sendError".equals(content)){
//				Toast.makeText(this, "验证码发送失败请稍后再试!", Toast.LENGTH_SHORT).show();
//			}
			break;
		case 2:
            if ("1".equals(dataJo.optString("status"))){
                Intent intent = new Intent(this, UserNewPasswordActivity.class);
                intent.putExtra("addr", addr);
                intent.putExtra("code", userCodeStr);
                startActivity(intent);
            }else {
                Toast.makeText(getApplicationContext(), dataJo.optString("errmsg"),
                        Toast.LENGTH_SHORT).show();
            }
//			if("succeed".equals(content)){
//				Intent intent = new Intent(this, UserNewPasswordActivity.class);
//				intent.putExtra("addr", addr);
//				intent.putExtra("code", userCodeStr);
//				startActivity(intent);
//
//			}else{
//				Toast.makeText(getApplicationContext(), "验证码错误",Toast.LENGTH_LONG).show();
//			}
		}
	}

	class TimeCount extends CountDownTimer {

		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			getCodeBtn.setTextColor(Color.parseColor("#666666"));
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
