package com.bbk.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.bbk.util.SharedPreferencesUtil;

public class UserRegisterNewActivity extends BaseActivity implements OnClickListener,ResultEvent {
	
	private DataFlow dataFlow;
	private ImageButton goBackBtn, cleanInputBtn;
	private TextView loginBtn;
	
	private EditText userInputEditText;
	
	private Button actionBtn;
	
	private String userPhoneNum = "", userPassword = "";
	private TextView mprotocol;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_register_set_password_new);
		dataFlow = new DataFlow(this);
//		MyApplication.getInstance().addActivity(this);
		
		userPhoneNum = getIntent().getStringExtra("userPhoneNum");
		
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
		mprotocol = (TextView) findViewById(R.id.mprotocol);
		mprotocol.setOnClickListener(this);
		
		userInputEditText = (EditText) findViewById(R.id.user_input);
		userInputEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String userInput = userInputEditText.getText().toString();
				if(!TextUtils.isEmpty(userInput)) {
					actionBtn.setEnabled(true);
					actionBtn.setTextColor(Color.parseColor("#FFFFFF"));
					actionBtn.setBackgroundResource(R.drawable.bg_user_btn);
					
				} else {
					actionBtn.setEnabled(false);
					actionBtn.setTextColor(Color.parseColor("#666666"));
					actionBtn.setBackgroundResource(R.drawable.bg_user_btn_unable);
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
		
		actionBtn = (Button) findViewById(R.id.action_btn);
		actionBtn.setOnClickListener(this);
		
	}

	private void initData() {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topbar_goback:
			finish();
			break;
		case R.id.mprotocol:
			Intent intent = new Intent(this, WebViewActivity.class);
			String url = Constants.MAIN_BASE_URL_MOBILE +"mobile/user/agreement";
			intent.putExtra("url", url);
			startActivity(intent);
			break;
		case R.id.topbar_text_right:
			finish();
			break;
		case R.id.clean_input_btn:
			userInputEditText.setText("");
			actionBtn.setEnabled(false);
			actionBtn.setTextColor(Color.parseColor("#666666"));
			actionBtn.setBackgroundResource(R.drawable.bg_user_btn_unable);
			break;
		case R.id.action_btn:
			userPassword = userInputEditText.getText().toString();
			if(TextUtils.isEmpty(userPassword)) {
				Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			registerThread();
			break;
		default:
			break;
		}
	}
	
	private void registerThread() {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("phone", userPhoneNum);
		paramsMap.put("password", userPassword);

		dataFlow.requestData(1, "apiService/registerUser", paramsMap, this);
				
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
		String str = content;
		try {
			JSONObject jsonObj = new JSONObject(str);
			Intent intent;

			
			switch (requestCode) {
			case 1:
				if("1".equals(jsonObj.optString("status"))) {
					JSONObject inforJsonObj = jsonObj.optJSONObject("info");
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
			        intent = new Intent();
			        setResult(1, intent);
			        finish();
				}else {
					loginBtn.setText("完成");
					loginBtn.setEnabled(true);
					Toast toast = Toast.makeText(getApplicationContext(), jsonObj.optString("msg"), Toast.LENGTH_LONG );
					toast.show();
				}
				break;
			default:
				break;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
