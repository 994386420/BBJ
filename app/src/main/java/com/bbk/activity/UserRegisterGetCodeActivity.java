package com.bbk.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.util.JiaMiUtil;
import com.bbk.util.StringUtil;

import static com.bbk.util.MD5Util.Md5;

public class UserRegisterGetCodeActivity extends BaseActivity implements
		OnClickListener, ResultEvent {

	private DataFlow dataFlow;
	private ImageButton goBackBtn, cleanInputBtn;
	private TextView loginBtn, timeTick;

	private EditText userInputEditText, userCodeEditText;

	private Button actionBtn;

	private RelativeLayout codeLayout;
	private static String userPhoneNum = "";
	private TimeCount time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_register_get_code_new);
		dataFlow = new DataFlow(this);
//		MyApplication.getInstance().addActivity(this);
		
		initView();
		initData();
	}

	private void initView() {
		goBackBtn = (ImageButton) findViewById(R.id.topbar_goback);
		goBackBtn.setOnClickListener(this);

		cleanInputBtn = (ImageButton) findViewById(R.id.clean_input_btn);
		cleanInputBtn.setOnClickListener(this);

		loginBtn = (TextView) findViewById(R.id.topbar_text_right);
		loginBtn.setOnClickListener(this);

		actionBtn = (Button) findViewById(R.id.action_btn);
		actionBtn.setOnClickListener(this);

		userInputEditText = (EditText) findViewById(R.id.user_input);
		userInputEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String userInput = userInputEditText.getText().toString();
				if (!TextUtils.isEmpty(userInput)) {
					actionBtn.setEnabled(true);
					actionBtn.setTextColor(Color.parseColor("#FFFFFF"));
					actionBtn.setBackgroundResource(R.drawable.bg_user_btn);
				} else {
					actionBtn.setEnabled(false);
					actionBtn.setTextColor(Color.parseColor("#666666"));
					actionBtn
							.setBackgroundResource(R.drawable.bg_user_btn_unable);
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

		userCodeEditText = (EditText) findViewById(R.id.user_code);
		userCodeEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String userCode = userCodeEditText.getText().toString();
				String userInput = userInputEditText.getText().toString();
				if (!TextUtils.isEmpty(userCode) && !TextUtils.isEmpty(userInput)) {
					actionBtn.setEnabled(true);
					actionBtn.setTextColor(Color.parseColor("#FFFFFF"));
					actionBtn.setBackgroundResource(R.drawable.bg_user_btn);
				} else {
					actionBtn.setEnabled(false);
					actionBtn.setTextColor(Color.parseColor("#666666"));
					actionBtn
							.setBackgroundResource(R.drawable.bg_user_btn_unable);
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

		codeLayout = (RelativeLayout) findViewById(R.id.code_layout);

		timeTick = (TextView) findViewById(R.id.time_tick);
	}

	private void initData() {
		time = new TimeCount(120000, 1000);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topbar_goback:
			finish();
			break;
		case R.id.topbar_text_right:
			finish();
			break;
		case R.id.clean_input_btn:
			userInputEditText.setText("");
			userCodeEditText.setText("");
			actionBtn.setText("获取验证码");
			actionBtn.setEnabled(false);
			actionBtn.setTextColor(Color.parseColor("#666666"));
			actionBtn.setBackgroundResource(R.drawable.bg_user_btn_unable);
			break;
		case R.id.action_btn:
			btnClick();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 1:
			Intent intent = new Intent();
			setResult(1, intent);
			finish();
			break;

		default:
			break;
		}
	}

	public void btnClick() {
		String btnStr = actionBtn.getText().toString();
		if ("获取验证码".equals(btnStr)) {
			userPhoneNum = userInputEditText.getText().toString();
//			if (!StringUtil.isPhoneNum(userPhoneNum))
//				Toast.makeText(getApplicationContext(), "手机号格式错误",
//						Toast.LENGTH_SHORT).show();
//			else
				checkThread();
		} else if ("注册".equals(btnStr)) {
			if (!TextUtils.isEmpty(userCodeEditText.getText().toString())) {
				Map<String, String> paramsMap = new HashMap<String, String>();
				paramsMap.put("addr", userPhoneNum);
				paramsMap.put("clientType", "android");
				paramsMap.put("code", userCodeEditText.getText().toString());
				dataFlow.requestData(3, "apiService/verificationCode",paramsMap, this);
			} else {
				Toast.makeText(getApplicationContext(), "验证码不能为空",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	class TimeCount extends CountDownTimer {

		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			timeTick.setText(Html.fromHtml("验证码已发送，<font color='#0098FF'>"
					+ millisUntilFinished / 1000 + " </font>秒后可重新发送"));
		}

		@Override
		public void onFinish() {
			userCodeEditText.setText("");

			timeTick.setText("验证码已过期，请重新获取");

			actionBtn.setEnabled(true);
			actionBtn.setTextColor(Color.parseColor("#FFFFFF"));
			actionBtn.setText("获取验证码");
			actionBtn.setBackgroundResource(R.drawable.bg_user_btn);
		}

		public void stop() {
			super.cancel();
			timeTick.setVisibility(View.GONE);

			userInputEditText.setText("");
			userCodeEditText.setText("");
			actionBtn.setText("获取验证码");
			actionBtn.setEnabled(false);
			actionBtn.setTextColor(Color.parseColor("#666666"));
			actionBtn.setBackgroundResource(R.drawable.bg_user_btn_unable);
		}
	}

	private void checkThread() {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("phone", userPhoneNum);
		dataFlow.requestData(1, "apiService/exsistPhone", paramsMap, this,false);
	}

	@Override
	protected void onDestroy() {
		setContentView(R.layout.view_null);
		System.gc();
		super.onDestroy();
	}

	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo,
			String content) {
		try {
			if (requestCode == 1) {
                if ("1".equals(dataJo.optString("status"))) {
					String v = JiaMiUtil.jiami(userPhoneNum);
                    Map<String, String> paramsMap = new HashMap<String, String>();
                    paramsMap.put("phone", userPhoneNum);
                    paramsMap.put("type", "0");
                    paramsMap.put("code", v);
                    dataFlow.requestData(2, "apiService/sendMcode", paramsMap,this);
                } else {
                    Toast.makeText(getApplicationContext(), dataJo.optString("errmsg"),
                            Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == 2) {
                if ("1".equals(dataJo.optString("status"))) {
                    actionBtn.setText("注册");
                    actionBtn.setEnabled(false);
                    actionBtn.setTextColor(Color.parseColor("#666666"));
                    actionBtn.setBackgroundResource(R.drawable.bg_user_btn_unable);
                } else {
                    Toast.makeText(getApplicationContext(), dataJo.optString("errmsg"),
                            Toast.LENGTH_SHORT).show();
                }
            }else if(requestCode == 3){
				if ("1".equals(dataJo.optString("status"))){
					Intent intent = new Intent(getApplicationContext(),
							UserRegisterNewActivity.class);
					intent.putExtra("userPhoneNum", userPhoneNum);
					startActivityForResult(intent, 1);
					time.stop();
				}else {
					Toast.makeText(getApplicationContext(), dataJo.optString("errmsg"),
							Toast.LENGTH_SHORT).show();
				}

            } else {
                Toast.makeText(getApplicationContext(), "请求失败，请稍后重试",
                        Toast.LENGTH_SHORT).show();
            }
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
