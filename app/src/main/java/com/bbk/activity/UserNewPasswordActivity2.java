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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.util.SharedPreferencesUtil;

public class UserNewPasswordActivity2 extends BaseActivity implements OnClickListener, TextWatcher,ResultEvent {
	private DataFlow dataFlow;
	private Button newPasswordSubmitBtn;
	private EditText newPassword, confrimPassword;
	private String newPasswordStr = "", confrimPasswordStr = "";
	private ImageButton goBackBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_new_password_main);
		dataFlow = new DataFlow(this);
		initView();
		initData();
	}
	
	private void initView() {
		newPassword = (EditText) findViewById(R.id.new_password);
		confrimPassword = (EditText) findViewById(R.id.confrim_new_password);
		
		newPasswordSubmitBtn = (Button) findViewById(R.id.new_password_submit);
		
		goBackBtn = (ImageButton) findViewById(R.id.topbar_goback);
		goBackBtn.setOnClickListener(this);
	}
	
	private void initData() {
		newPasswordSubmitBtn.setOnClickListener(this);
		
		newPassword.addTextChangedListener(this);
		confrimPassword.addTextChangedListener(this);
	}

	private boolean getPassword() {
		newPasswordStr = newPassword.getText().toString();
		confrimPasswordStr = confrimPassword.getText().toString();
		
		if(TextUtils.isEmpty(newPasswordStr)) {
			Toast toast = Toast.makeText(getApplicationContext(), "新密码不能为空",Toast.LENGTH_LONG );
			toast.show();
			return false;
		}
		if(TextUtils.isEmpty(confrimPasswordStr)) {
			Toast toast = Toast.makeText(getApplicationContext(), "确认密码不能为空",Toast.LENGTH_LONG );
			toast.show();
			return false;
		}
		if(!newPasswordStr.equals(confrimPasswordStr)) {
			Toast toast = Toast.makeText(getApplicationContext(), "两次输入密码不一致",Toast.LENGTH_LONG );
			toast.show();
			return false;
		}
		return true;
	}
	
	private void newPasswordHttp() {
		newPasswordSubmitBtn.setText("新密码设置中...");
		newPasswordSubmitBtn.setEnabled(false);
		Map<String, String> paramsMap = new HashMap<String, String>();
		String userId = SharedPreferencesUtil.getSharedData(UserNewPasswordActivity2.this, "userInfor", "userID");
		paramsMap.put("userid", userId);
		paramsMap.put("password", confrimPasswordStr);
		dataFlow.requestData(1, "apiService/newpassword",paramsMap, this);
	}
	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo,
			String content) {
		newPasswordSubmitBtn.setText("确定");
		newPasswordSubmitBtn.setEnabled(true);
		Log.e("=====content=====", content+"");
		switch (requestCode) {
		case 1:
			if("1".equals(dataJo.optString("status"))) {
				Toast toast = Toast.makeText(getApplicationContext(), "新密码设置成功",Toast.LENGTH_LONG );
				toast.show();
				finish();
			}else {
				Toast.makeText(getApplicationContext(), dataJo.optString("errmsg"),
						Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.new_password_submit) {
			if(getPassword()) {
				newPasswordHttp();
			}
		} 
		
		if(v.getId() == R.id.topbar_goback) {
			finish();
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		String password1 = newPassword.getText().toString();
		String password2 = confrimPassword.getText().toString();
		
		if(!TextUtils.isEmpty(password1) && !TextUtils.isEmpty(password2)) {
			newPasswordSubmitBtn.setEnabled(true);
			newPasswordSubmitBtn.setTextColor(Color.parseColor("#FFFFFF"));
			newPasswordSubmitBtn.setBackgroundResource(R.drawable.bg_user_btn);
		} else {
			newPasswordSubmitBtn.setEnabled(false);
			newPasswordSubmitBtn.setTextColor(Color.parseColor("#666666"));
			newPasswordSubmitBtn.setBackgroundResource(R.drawable.bg_user_btn_unable);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onDestroy() {
		setContentView(R.layout.view_null);
		System.gc();
		super.onDestroy();
	}
}
